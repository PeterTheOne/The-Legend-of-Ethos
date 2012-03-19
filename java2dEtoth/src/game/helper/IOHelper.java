package game.helper;

import org.w3c.dom.Element;

public class IOHelper {
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

	public static String XMLreadString(Element elem, String string) {
		String pathStr = (String) elem.getAttribute(string);
		return pathStr;
	}
}