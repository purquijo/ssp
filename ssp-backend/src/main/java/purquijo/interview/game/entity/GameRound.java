package purquijo.games.game.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GameRound implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int round;

    private GameRoundStatus status;

    @ManyToOne
    @JsonIgnore
    private Game game;

    @OneToMany(cascade = CascadeType.ALL)
    private List<GameChoice> choices;

    @ManyToOne
    private Player winner;

    public GameRound() {
    }

    public GameRound(int round) {
        this.round = round;
        this.status = GameRoundStatus.ONGOING;
        this.choices = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public GameRoundStatus getStatus() {
        return status;
    }

    public void setStatus(GameRoundStatus status) {
        this.status = status;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<GameChoice> getChoices() {
        if(GameRoundStatus.ONGOING.equals(status)){
            return choices.stream()
                    .map(GameChoice::createHiddenChoice)
                    .toList();
        }
        return choices;
    }

    public List<GameChoice> getOtherChoices(GameChoice choice) {
        return choices.stream()
                .filter(gameChoice -> !gameChoice.equals(choice))
                .toList();
    }

    public void setChoices(List<GameChoice> choices) {
        this.choices = choices;
    }

    public void addChoice(GameChoice choice) {
        this.choices.add(choice);
        choice.setRound(this);
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }
}
