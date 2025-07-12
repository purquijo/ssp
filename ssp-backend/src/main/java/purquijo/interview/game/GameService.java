package purquijo.games.game;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import purquijo.games.game.entity.*;
import purquijo.games.game.exception.GameNotFoundException;
import purquijo.games.game.exception.InvalidGameException;
import purquijo.games.game.exception.InvalidGameRequestException;
import purquijo.games.game.repository.GameRepository;
import purquijo.games.game.repository.GameRoundRepository;
import purquijo.games.game.repository.PlayerRepository;
import purquijo.games.user.AppUser;
import purquijo.games.user.AppUserRepository;
import purquijo.games.game.dto.GameRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
public class GameService {

    private final GameRoundRepository gameRoundRepository;
    private final GameRepository gameRepository;
    private final AppUserRepository appUserRepository;
    private final PlayerRepository playerRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public GameService(GameRoundRepository gameRoundRepository, GameRepository gameRepository, AppUserRepository appUserRepository, PlayerRepository playerRepository, SimpMessagingTemplate messagingTemplate) {
        this.gameRoundRepository = gameRoundRepository;
        this.gameRepository = gameRepository;
        this.appUserRepository = appUserRepository;
        this.playerRepository = playerRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public Game getGame(AppUser appUser, Long gameId) {
        Game game = this.gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        getGameValidation(game, appUser);
        return game;
    }

    public Game stopGame(Long gameId) {
        Game game = this.gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        game.setStatus(GameStatus.STOPPED);
        return publishGameState(gameRepository.save(game));
    }

    public Game startGame(GameRequest gameRequest, AppUser appUser) {
        GameType gameType = gameRequest.getType();
        int scoreToReach = gameRequest.getScoreToReach();
        if (scoreToReach <= 0 || gameType == null) {
           throw new InvalidGameRequestException();
        }
        Game newGame = new Game();
        newGame.setStartedAt(LocalDateTime.now());
        newGame.addPlayer(new Player(appUser));
        newGame.setScoreToReach(scoreToReach);
        if (gameType == GameType.TTS_BOT) {
            newGame.addPlayer(new Player());
        }
        newGame.setType(gameType);
        newGame.setStatus(gameType.equals(GameType.TTS_BOT) ? GameStatus.ONGOING : GameStatus.WAITING_FOR_PLAYERS);
        return gameRepository.save(newGame);
    }

    public Game joinGame(Long gameId, AppUser appUser) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException(gameId));
        gameValidation(game, appUser);
        game.setStatus(GameStatus.ONGOING);
        game.addPlayer(new Player(appUser));
        return publishGameState(gameRepository.save(game));
    }

    public Game choice(Long gameId, AppUser appUser, ChoiceType choiceType) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        gameChoiceValidation(game, appUser);
        GameRound gameRound = getOrCreateOngoingRound(game);
        if (game.getType().equals(GameType.TTS_BOT)) {
            gameRound.addChoice(generateBotChoice(game, gameRound));
        }
        gameRound.addChoice(generatePlayerChoice(game, gameRound, choiceType, appUser));
        if (haveAllChosen(game, gameRound)) {
            finishRound(game, gameRound);
        }
        game.addRound(gameRound);
        return publishGameState(gameRepository.save(game));
    }

    private GameRound getOrCreateOngoingRound(Game game) {
        return game.getOngoingRound()
                .orElseGet(() -> {
                    int nextRound = game.getRounds().size();
                    GameRound newRound = new GameRound(nextRound);
                    newRound.setGame(game);
                    return gameRoundRepository.save(newRound);
                });
    }

    private void finishRound(Game game, GameRound gameRound) {
        gameRound.setStatus(GameRoundStatus.FINISHED);
        determineRoundResults(gameRound);
        Player winner = gameRound.getWinner();
        if (!hasScoreBeenReached(game, winner)) {
            return;
        }
        game.setStatus(GameStatus.FINISHED);
        game.setWinner(winner);
        rewardPlayers(game, winner);
    }

    private void determineRoundResults(GameRound gameRound) {
        gameRound.getChoices().forEach(gameChoice -> {
            List<ChoiceResult> results = gameRound.getOtherChoices(gameChoice)
                    .stream()
                    .map(other -> gameChoice.getType().against(other.getType()))
                    .toList();

            if (results.contains(ChoiceResult.WIN) && !results.contains(ChoiceResult.LOSE)) {
                Player winner = gameChoice.getPlayer();
                gameChoice.setResult(ChoiceResult.WIN);
                gameRound.setWinner(winner);
                winner.setScore(winner.getScore() + 1);
                playerRepository.save(winner);
            } else if (results.contains(ChoiceResult.LOSE)) {
                gameChoice.setResult(ChoiceResult.LOSE);
            } else {
                gameChoice.setResult(ChoiceResult.DRAW);
            }
        });
    }

    private void rewardPlayers(Game game, Player winner) {
        List<AppUser> updatedAppUsers = game.getPlayers().stream()
                .filter(player -> player.getAppUser() != null)
                .map(player -> {
                    AppUser appUser = player.getAppUser();
                    appUser.addPoints(player.getScore() * 5L);
                    if (player.equals(winner)) {
                        appUser.addPoints(100);
                    }
                    return appUser;
                }).toList();

        appUserRepository.saveAll(updatedAppUsers);
    }

    private boolean hasScoreBeenReached(Game game, Player roundWinner) {
        if (roundWinner == null) {
            return false;
        }
        return roundWinner.getScore() >= game.getScoreToReach();
    }

    private boolean haveAllChosen(Game game, GameRound gameRound) {
        List<Player> players = game.getPlayers();
        List<Player> chosenPlayers = gameRound.getChoices().stream().map(GameChoice::getPlayer).toList();
        return players.size() == chosenPlayers.size();
    }

    private Player getPlayer(Game game, AppUser appUser) {
        return game.getPlayers().stream()
                .filter(player -> player.getAppUser() != null && Objects.equals(player.getAppUser().getId(), appUser.getId()))
                .findFirst()
                .orElseThrow();
    }

    private Player getBotPlayer(Game game) {
        return game.getPlayers().stream()
                .filter(player -> player.getAppUser() == null)
                .findFirst()
                .orElseThrow();
    }

    private Game publishGameState(Game game) {
        if (!game.getType().equals(GameType.TTS_BOT)) {
            this.messagingTemplate.convertAndSend("/topic/game/" + game.getId(), game);
        }
        return game;
    }

    private GameChoice generateBotChoice(Game game, GameRound round) {
        GameChoice gameChoice = new GameChoice();
        ChoiceType botChoiceType = getRandomChoiceType();
        gameChoice.setType(botChoiceType);
        gameChoice.setPlayer(getBotPlayer(game));
        gameChoice.setRound(round);
        return gameChoice;
    }

    private GameChoice generatePlayerChoice(Game game, GameRound round, ChoiceType choiceType, AppUser appUser) {
        GameChoice gameChoice = new GameChoice();
        gameChoice.setType(choiceType);
        gameChoice.setPlayer(getPlayer(game, appUser));
        gameChoice.setRound(round);
        return gameChoice;
    }

    private void gameValidation(Game game, AppUser appUser) {
        if (game.getPlayers().stream().anyMatch(player ->  player.getAppUser() != null && player.getAppUser().getId().equals(appUser.getId()))) {
            throw new InvalidGameException("Player " + appUser.getId() + " is already in this game");
        }
        if (game.getStatus().equals(GameStatus.ONGOING)) {
            throw new InvalidGameException("Game is already ongoing");
        }
    }

    private void gameChoiceValidation(Game game, AppUser appUser) {
        if (game.getPlayers().stream().noneMatch(player -> player.getAppUser() != null && player.getAppUser().getId().equals(appUser.getId()))) {
            throw new InvalidGameException("Player " + appUser.getId() + " is not in this game");
        }
        if (!game.getStatus().equals(GameStatus.ONGOING)) {
            throw new InvalidGameException("Game is not ongoing");
        }
    }

    private void getGameValidation(Game game, AppUser appUser) {
        if (game.getPlayers().stream().noneMatch(player -> player.getAppUser() != null && player.getAppUser().getId().equals(appUser.getId()))) {
            throw new InvalidGameException("Player " + appUser.getId() + " is not in this game");
        }
        if (List.of(GameStatus.FINISHED, GameStatus.STOPPED).contains(game.getStatus())) {
            throw new InvalidGameException("Game has already ended");
        }
    }

    public static ChoiceType getRandomChoiceType() {
        ChoiceType[] values = ChoiceType.values();
        int randomIndex = new Random().nextInt(values.length);
        return values[randomIndex];
    }

}
