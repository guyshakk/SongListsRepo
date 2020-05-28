package SongListsService.aop;

public class SongNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8864742884664458267L;

	public SongNotFoundException() {
	}

	public SongNotFoundException(String message) {
		super(message);
	}

	public SongNotFoundException(Throwable cause) {
		super(cause);
	}

	public SongNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
