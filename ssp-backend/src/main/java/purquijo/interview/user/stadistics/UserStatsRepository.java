package purquijo.games.user.stadistics;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import purquijo.games.game.entity.ChoiceType;

@Repository
public class UserStatsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public UserStatsProjection getUserStats(Long userId) {
        String sql = """
                SELECT
                  (SELECT COUNT(DISTINCT g.id)
                   FROM game g
                   JOIN player p ON p.game_id = g.id
                   WHERE p.app_user_id = :userId) AS gamesPlayed,
                
                  (SELECT COUNT(*)
                   FROM game g
                   JOIN player p ON g.winner_id = p.id
                   WHERE p.app_user_id = :userId) AS gamesWon,
                
                  (SELECT gc.type
                   FROM game_choice gc
                   JOIN player p ON gc.player_id = p.id
                   WHERE p.app_user_id = :userId
                   GROUP BY gc.type
                   ORDER BY COUNT(*) DESC
                   LIMIT 1) AS mostUsedChoice
                """;

        Object[] row = (Object[]) entityManager.createNativeQuery(sql).setParameter("userId", userId).getSingleResult();

        return new UserStatsProjection() {
            @Override
            public Integer getGamesPlayed() {
                return ((Number) row[0]).intValue();
            }

            @Override
            public Integer getGamesWon() {
                return ((Number) row[1]).intValue();
            }

            @Override
            public ChoiceType getMostUsedChoice() {
                return row[2] != null ? ChoiceType.valueOf(row[2].toString()) : null;
            }
        };
    }
}
