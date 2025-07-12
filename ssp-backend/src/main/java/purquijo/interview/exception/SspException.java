package purquijo.games.exception;

import org.springframework.http.HttpStatus;

public class SspException extends RuntimeException {
    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public SspException(String message, HttpStatus status) {
      super(message);
      this.status = status;
    }
    public SspException(String message) {
        super(message);
    }

  public HttpStatus getStatus() {
    return status;
  }

  public void setStatus(HttpStatus status) {
    this.status = status;
  }
}
