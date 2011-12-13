package game.exceptions;

public class IsNoDirectoryException extends Exception {

	public IsNoDirectoryException() {
		super("File is no Directory");
	}

	public IsNoDirectoryException(String message) {
		super(message);
	}

}
