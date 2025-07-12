package purquijo.games.game;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import purquijo.games.game.entity.ChoiceType;
import purquijo.games.game.entity.Game;
import purquijo.games.user.AppUser;
import purquijo.games.game.dto.GameRequest;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/{gameId}")
    public Game getGame(@AuthenticationPrincipal AppUser appUser, @PathVariable Long gameId) {
        return gameService.getGame(appUser, gameId);
    }

    @DeleteMapping("/{gameId}")
    public Game exitGame(@PathVariable Long gameId) {
        return gameService.stopGame(gameId);
    }

    @PostMapping
    public Game startGame(@AuthenticationPrincipal AppUser appUser, @RequestBody GameRequest gameRequest) {
        return gameService.startGame(gameRequest, appUser);
    }

    @PostMapping("/{gameId}/join")
    public Game joinGame(@AuthenticationPrincipal AppUser appUser, @PathVariable Long gameId) {
        return gameService.joinGame(gameId, appUser);
    }

    @PostMapping("/{gameId}/choice/{choice}")
    public Game choice(@PathVariable Long gameId, @PathVariable ChoiceType choice, @AuthenticationPrincipal AppUser appUser) {
        return gameService.choice(gameId, appUser, choice);
    }
}
