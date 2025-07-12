package purquijo.games.user.stadistics;

import purquijo.games.game.entity.ChoiceType;

public interface UserStatsProjection {
    Integer getGamesPlayed();
    Integer getGamesWon();
    ChoiceType getMostUsedChoice();
}
