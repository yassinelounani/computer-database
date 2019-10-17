package fr.excilys.cdb.api.exception;

public class NotFoundCompanyException extends Exception {
	private static final long serialVersionUID = -1401637908808558760L;

	public NotFoundCompanyException(String message, Throwable cause) {
		super(message, cause);
	}
	public NotFoundCompanyException(String message) {
		super(message);
	}
	public NotFoundCompanyException(Throwable cause) {
		super(cause);
	}
}
