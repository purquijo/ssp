package purquijo.games.user.stadistics;

import purquijo.games.game.entity.ChoiceType;

import java.io.Serializable;

public class UserStatsDto implements Serializable {
    private int gamesPlayed;
    private int gamesWon;
    private ChoiceType mostUsedChoice;
    private double winRate;

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public ChoiceType getMostUsedChoice() {
        return mostUsedChoice;
    }

    public void setMostUsedChoice(ChoiceType mostUsedChoice) {
        this.mostUsedChoice = mostUsedChoice;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }
}
