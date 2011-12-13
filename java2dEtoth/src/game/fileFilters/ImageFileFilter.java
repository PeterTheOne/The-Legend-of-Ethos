package game.fileFilters;

import java.io.File;
import java.io.FileFilter;

public class ImageFileFilter implements FileFilter {

	private final String[] allowedExtensions = 
		new String[] {"jpg", "png", "gif"};

	@Override
	public boolean accept(File file) {

		for (String extension : allowedExtensions) {
			if (file.isFile() &&
					file.getName().toLowerCase().endsWith(extension)) {
				return true;
			}
		}
		return false;
	}

}
