package purquijo.games.game.exception;

import org.springframework.http.HttpStatus;
import purquijo.games.exception.SspException;

public class InvalidGameException extends SspException {
    public InvalidGameException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
