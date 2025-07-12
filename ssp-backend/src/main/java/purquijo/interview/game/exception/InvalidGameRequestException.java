package purquijo.games.game.exception;

import org.springframework.http.HttpStatus;
import purquijo.games.exception.SspException;

public class InvalidGameRequestException extends SspException {
    public InvalidGameRequestException() {
        super("Invalid game request", HttpStatus.BAD_REQUEST);
    }
}
