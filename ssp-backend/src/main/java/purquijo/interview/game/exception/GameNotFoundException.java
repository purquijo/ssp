package purquijo.games.game.exception;

import org.springframework.http.HttpStatus;
import purquijo.games.exception.SspException;

public class GameNotFoundException extends SspException {

    public GameNotFoundException(Long gameId) {
      super("Game with id " + gameId + " not found", HttpStatus.NOT_FOUND);
    }

    public GameNotFoundException(String message) {
        super(message);
    }
}
