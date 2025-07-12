package purquijo.games.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import purquijo.games.user.stadistics.UserStatsDto;
import purquijo.games.user.stadistics.UserStatsService;

@RestController
@RequestMapping("/user")
public class AppUserController {

    private final UserStatsService userStatsService;

    public AppUserController(UserStatsService userStatsService) {
        this.userStatsService = userStatsService;
    }

    @GetMapping("/points")
    public Long getUserPoints(@AuthenticationPrincipal AppUser user) {
        return user.getPoints();
    }

    @GetMapping("/stats")
    public UserStatsDto getUserStats(@AuthenticationPrincipal AppUser user) {
        return userStatsService.getStatsForUser(user.getId());
    }
}
