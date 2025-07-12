package purquijo.games.user.stadistics;

import org.springframework.stereotype.Service;

@Service
public class UserStatsService {

    private final UserStatsRepository userStatsRepository;

    public UserStatsService(UserStatsRepository userStatsRepository) {
        this.userStatsRepository = userStatsRepository;
    }

    public UserStatsDto getStatsForUser(Long id) {
        UserStatsProjection stats = userStatsRepository.getUserStats(id);

        double winRate = stats.getGamesPlayed() > 0
                ? (double) stats.getGamesWon() / stats.getGamesPlayed()
                : 0;

        UserStatsDto dto = new UserStatsDto();
        dto.setGamesPlayed(stats.getGamesPlayed());
        dto.setGamesWon(stats.getGamesWon());
        dto.setMostUsedChoice(stats.getMostUsedChoice());
        dto.setWinRate(winRate);
        return dto;
    }
}
