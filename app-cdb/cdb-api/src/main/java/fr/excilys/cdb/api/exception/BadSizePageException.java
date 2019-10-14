package fr.excilys.cdb.api.exception;

public class BadSizePageException extends Throwable {

	private static final long serialVersionUID = 5045077244753990653L;

	public BadSizePageException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadSizePageException(String message) {
		super(message);
	}
	
	
}
