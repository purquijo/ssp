package purquijo.games.authentication;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthService.AuthResponse login(@RequestBody AuthService.AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public AuthService.AuthResponse register(@RequestBody AuthService.AuthRequest request) {
        return authService.register(request);
    }
}
