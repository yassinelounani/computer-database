package fr.excilys.cdb.api.exception;

public class NotFoundComputerException extends Exception {
	private static final long serialVersionUID = 396293435271236843L;

	public NotFoundComputerException(String message, Throwable throwable) {
		super(message, throwable);
	}
	public NotFoundComputerException(String message) {
		super(message);
	}
}
