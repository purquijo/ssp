package purquijo.games.game.dto;

import purquijo.games.game.entity.GameType;

import java.io.Serializable;

public class GameRequest implements Serializable {
    private GameType type;
    private int scoreToReach = 3;

    public GameRequest() {
    }

    public GameType getType() {
        return type;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public int getScoreToReach() {
        return scoreToReach;
    }

    public void setScoreToReach(int scoreToReach) {
        this.scoreToReach = scoreToReach;
    }
}
