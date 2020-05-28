package SongListsService.aop;

public class SongListNotFoundException extends RuntimeException {

	
	private static final long serialVersionUID = -8864742884664458267L;

	public SongListNotFoundException() {
	}

	public SongListNotFoundException(String message) {
		super(message);
	}

	public SongListNotFoundException(Throwable cause) {
		super(cause);
	}

	public SongListNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
