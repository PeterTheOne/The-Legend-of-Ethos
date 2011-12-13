package game.exceptions;

public class NotFoundException extends Exception {

	public NotFoundException() {
		super("Something is not Found");
	}

	public NotFoundException(String message) {
		super(message);
	}

}
