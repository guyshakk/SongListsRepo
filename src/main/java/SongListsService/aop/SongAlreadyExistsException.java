package SongListsService.aop;

public class SongAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -8864742884664458267L;

	public SongAlreadyExistsException() {
	}

	public SongAlreadyExistsException(String message) {
		super(message);
	}

	public SongAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	public SongAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
