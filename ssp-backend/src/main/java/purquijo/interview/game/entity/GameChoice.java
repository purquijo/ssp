package purquijo.games.game.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class GameChoice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ChoiceType type;

    private ChoiceResult result;

    @ManyToOne
    @JsonIgnore
    private GameRound round;

    @ManyToOne
    private Player player;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChoiceType getType() {
        return type;
    }

    public void setType(ChoiceType type) {
        this.type = type;
    }

    public ChoiceResult getResult() {
        return result;
    }

    public void setResult(ChoiceResult result) {
        this.result = result;
    }

    public GameRound getRound() {
        return round;
    }

    public void setRound(GameRound round) {
        this.round = round;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GameChoice createHiddenChoice(){
        GameChoice hiddenChoice = new GameChoice();
        hiddenChoice.result = this.result;
        hiddenChoice.round = this.round;
        hiddenChoice.player = this.player;
        return hiddenChoice;
    }
}
