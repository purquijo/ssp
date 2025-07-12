package purquijo.games.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import purquijo.games.exception.SspException;
import purquijo.games.game.entity.ChoiceType;
import purquijo.games.user.AppUser;
import purquijo.games.user.AppUserRepository;

import static purquijo.games.game.GameService.getRandomChoiceType;

@Service
public class AuthService {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authManager, JwtUtil jwtUtil, AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(AuthRequest request) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(request.username, request.password);
            authManager.authenticate(authToken);
            String token = jwtUtil.generateToken(request.username);
            AppUser user = appUserRepository.findByUsername(request.username).orElseThrow();
            return new AuthResponse(token, request.username, user.getPoints(), user.getAvatar());
        } catch (AuthenticationException e) {
            throw new SspException("Incorrect username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    public AuthResponse register(AuthRequest request) {
        createUser(request);
        return login(request);
    }

    private void createUser(AuthRequest request) {
        if (appUserRepository.existsUserByUsername(request.username)) {
            throw new SspException("Username already exists", HttpStatus.BAD_REQUEST);
        }
        AppUser appUser = new AppUser();
        appUser.setUsername(request.username);
        appUser.setPassword(passwordEncoder.encode(request.password));
        appUser.setRole(Role.USER);
        appUser.setPoints(0L);
        appUser.setAvatar(getRandomChoiceType());
        appUserRepository.save(appUser);
    }

    public record AuthRequest(String username, String password) {
    }

    public record AuthResponse(String token, String username, Long points, ChoiceType avatar) {
    }
}
