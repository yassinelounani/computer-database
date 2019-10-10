package exception;

public class BadSizePageException extends Throwable {

	public BadSizePageException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadSizePageException(String message) {
		super(message);
	}
	
	
}
