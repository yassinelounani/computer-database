package exception;

public class ConnextionFaild extends Exception {

    public ConnextionFaild(String message) {
        super(message);
    }

    public ConnextionFaild(String message, Throwable cause) {
        super(message, cause);
    }
}
