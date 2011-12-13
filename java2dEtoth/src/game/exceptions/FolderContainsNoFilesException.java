package game.exceptions;

public class FolderContainsNoFilesException extends Exception {

	public FolderContainsNoFilesException() {
		super("Folder Contains no Files");
	}

	public FolderContainsNoFilesException(String message) {
		super(message);
	}

}
