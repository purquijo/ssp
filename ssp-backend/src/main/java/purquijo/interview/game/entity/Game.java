package purquijo.games.game.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import purquijo.games.user.AppUser;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Game implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private GameStatus status;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Player> players;

    @OneToMany(mappedBy = "game")
    private List<GameRound> rounds;

    private int scoreToReach;

    private GameType type;

    @ManyToOne
    @JsonIgnore
    private Player winner;

    @ManyToOne
    @JsonIgnore
    private AppUser createdBy;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        if (this.players == null) {
            this.players = new ArrayList<>();
        }
        player.setGame(this);
        this.players.add(player);
    }

    public List<GameRound> getRounds() {
        return rounds;
    }

    public void setRounds(List<GameRound> rounds) {
        this.rounds = rounds;
    }

    public void addRound(GameRound round) {
        this.rounds.add(round);
        round.setGame(this);
    }

    @JsonInclude
    public Optional<GameRound> getOngoingRound() {
        if (rounds == null || rounds.isEmpty()) {
            return Optional.empty();
        }
        return rounds.stream()
                .filter(round -> round.getStatus() == GameRoundStatus.ONGOING)
                .findFirst();
    }

    public int getScoreToReach() {
        return scoreToReach;
    }

    public void setScoreToReach(int scoreToReach) {
        this.scoreToReach = scoreToReach;
    }

    public GameType getType() {
        return type;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public String getWinnerUsername() {
        if (status != GameStatus.FINISHED) {
            return null;
        }
        return winner.getUsername();
    }

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUser createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }
}
