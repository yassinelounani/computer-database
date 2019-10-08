package exception;

public class NotFoundComputerException extends Exception{
	
	public NotFoundComputerException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public NotFoundComputerException(String message) {
		super(message);
	}
	
	
}
