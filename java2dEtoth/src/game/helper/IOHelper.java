package game.helper;

import game.EtothGame;
import game.exceptions.FolderContainsNoFilesException;
import game.fileFilters.ImageFileFilter;

import java.awt.image.BufferedImage;
import java.io.File;

import org.w3c.dom.Element;

public class IOHelper {
	
	
	public static BufferedImage[] getImages(EtothGame game, File path) 
			throws FolderContainsNoFilesException {
		//TODO: Exception?
		BufferedImage imgArray[];
		if (path.isFile()) {
			imgArray = new BufferedImage[1];
			imgArray[0] = game.getImage(path.getPath());
		} else {
			File imgList[] = path.listFiles(new ImageFileFilter());
			if (imgList == null) {
				System.out.println("err: " + path);
			}
			int len = imgList.length;
			if (len > 0) {
				imgArray = new BufferedImage[len];
				for (int i = 0; i < imgArray.length; i++) {
					imgArray[i] = game.getImage(imgList[i].getPath());
				}
			} else {
				throw new FolderContainsNoFilesException();
			}
		}
		return imgArray;
	}

	public static Integer XMLreadInt(Element elem, String string) {
		String str = elem.getAttribute(string);
		if (str.equals("")) {
			return null;
		}
		return Integer.parseInt(str);
	}

	public static Double XMLreadDouble(Element elem, String string) {
		String str = elem.getAttribute(string);
		if (str.equals("")) {
			return null;
		}
		return Double.parseDouble(str);
	}

	public static Boolean XMLreadBoolean(Element elem, String string) {
		String str = elem.getAttribute(string);
		if (str.equals("")) {
			return null;
		}
		return Boolean.parseBoolean(str);
	}

	public static boolean XMLreadBooleanSafe(Element elem, String string) {
		String str = elem.getAttribute(string);
		if (str.equals("")) {
			return false;
		}
		return Boolean.parseBoolean(str);
	}

	public static File XMLreadPath(Element elem, File startPath,
			String string) {
		String pathStr = (String) elem.getAttribute(string);
		return new File(startPath + File.separator + pathStr);
	}
}
