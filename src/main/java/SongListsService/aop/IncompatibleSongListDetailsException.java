package SongListsService.aop;

public class IncompatibleSongListDetailsException extends RuntimeException {

	private static final long serialVersionUID = -8864742884664458267L;

	public IncompatibleSongListDetailsException() {
	}

	public IncompatibleSongListDetailsException(String message) {
		super(message);
	}

	public IncompatibleSongListDetailsException(Throwable cause) {
		super(cause);
	}

	public IncompatibleSongListDetailsException(String message, Throwable cause) {
		super(message, cause);
	}
}
