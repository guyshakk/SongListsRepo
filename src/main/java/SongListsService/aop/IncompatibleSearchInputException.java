package SongListsService.aop;

public class IncompatibleSearchInputException extends RuntimeException {

	private static final long serialVersionUID = -8864742884664458267L;

	public IncompatibleSearchInputException() {
	}

	public IncompatibleSearchInputException(String message) {
		super(message);
	}

	public IncompatibleSearchInputException(Throwable cause) {
		super(cause);
	}

	public IncompatibleSearchInputException(String message, Throwable cause) {
		super(message, cause);
	}
}
