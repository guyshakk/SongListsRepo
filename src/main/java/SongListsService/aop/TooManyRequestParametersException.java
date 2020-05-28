package SongListsService.aop ;

public class TooManyRequestParametersException extends RuntimeException {

	private static final long serialVersionUID = -8864742884664458267L;

	public TooManyRequestParametersException() {
	}

	public TooManyRequestParametersException(String message) {
		super(message);
	}

	public TooManyRequestParametersException(Throwable cause) {
		super(cause);
	}

	public TooManyRequestParametersException(String message, Throwable cause) {
		super(message, cause);
	}
}
