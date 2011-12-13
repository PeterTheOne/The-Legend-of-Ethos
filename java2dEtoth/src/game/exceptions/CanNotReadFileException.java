package game.exceptions;

public class CanNotReadFileException extends Exception {

	public CanNotReadFileException() {
		super("Can't read File");
	}

	public CanNotReadFileException(String message) {
		super(message);
	}

}
