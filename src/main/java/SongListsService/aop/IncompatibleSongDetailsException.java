package SongListsService.aop;

public class IncompatibleSongDetailsException extends RuntimeException {

	private static final long serialVersionUID = -8864742884664458267L;

	public IncompatibleSongDetailsException() {
	}

	public IncompatibleSongDetailsException(String message) {
		super(message);
	}

	public IncompatibleSongDetailsException(Throwable cause) {
		super(cause);
	}

	public IncompatibleSongDetailsException(String message, Throwable cause) {
		super(message, cause);
	}
}
