package SongListsService.aop;

public class SongListAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -8864742884664458267L;

	public SongListAlreadyExistsException() {
	}

	public SongListAlreadyExistsException(String message) {
		super(message);
	}

	public SongListAlreadyExistsException(Throwable cause) {
		super(cause);
	}

	public SongListAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
