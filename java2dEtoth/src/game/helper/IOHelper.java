package game.helper;

import game.EtothGame;
import game.exceptions.FolderContainsNoFilesException;
import game.fileFilters.ImageFileFilter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.imageio.ImageIO;

import org.w3c.dom.Element;

public class IOHelper {
	public static URL[] getXMLFiles(EtothGame game, URL xmlFile) throws FolderContainsNoFilesException
	{
		// returns inputstream oder string
		//TODO: Exception?
		URL xmlArray[];
		if (xmlFile.getFile().contains(".xml")) {
			xmlArray = new URL[1];
			try {
				if(xmlFile.toString().startsWith("file:"))
				{
					xmlFile = new URL("jar:file:" + xmlFile.toString().replace("file:", ""));
				}
				//System.out.println("hmm " + xmlFile);
				
			   xmlArray[0] = xmlFile;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			String[] filenames = new String[1];
			try {
				filenames = getResourceListing(EtothGame.class, xmlFile.getPath() + "/", "xml");
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//File imgFile2 = new File(imgFile.getFile());
			//File imgList[] = imgFile2.listFiles(new ImageFileFilter());
			URL xmlList[] = new URL[filenames.length];
			for(int i = 0; i < filenames.length; i++)
			{
				//System.out.println("lol2: " + EtothGame.fileToURL(filenames[i]));
				try {
					xmlList[i] = new URL( "jar:" + EtothGame.fileToURL(filenames[i]) );
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (xmlList == null) {
				System.out.println("err: " + xmlFile);
			}
			int len = xmlList.length;
			if (len > 0) {
				xmlArray = new URL[len];
				for (int i = 0; i < xmlArray.length; i++) {
						//System.out.println("hmm " + xmlList[i]);
						xmlArray[i] = xmlList[i];
				}
			} else {
				throw new FolderContainsNoFilesException();
			}
		}
		return xmlArray;
	}
	
	public static BufferedImage[] getImages(EtothGame game, URL imgFile) throws FolderContainsNoFilesException
	{
		//TODO: Exception?
		if (imgFile == null || imgFile.getFile() == null)
		{
			throw new FolderContainsNoFilesException();
		}
		
		BufferedImage imgArray[];
		if (imgFile.getFile().contains(".png") || imgFile.getFile().contains(".jpg") || imgFile.getFile().contains(".gif")) {
			imgArray = new BufferedImage[1];
			try {
				if(imgFile.toString().startsWith("file:"))
				{
					imgFile = new URL("jar:file:" + imgFile.toString().replace("file:", ""));
				}
				System.out.println("hmm " + imgFile);
				
			   imgArray[0] = ImageIO.read(imgFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			String[] filenames = new String[1];
			try {
				filenames = getResourceListing(EtothGame.class, imgFile.getPath() + "/", "img");
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//File imgFile2 = new File(imgFile.getFile());
			//File imgList[] = imgFile2.listFiles(new ImageFileFilter());
			URL imgList[] = new URL[filenames.length];
			for(int i = 0; i < filenames.length; i++)
			{
				//System.out.println("lol2: " + EtothGame.fileToURL(filenames[i]));
				try {
					imgList[i] = new URL( "jar:" + EtothGame.fileToURL(filenames[i]) );
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (imgList == null) {
				System.out.println("err: " + imgFile);
			}
			int len = imgList.length;
			if (len > 0) {
				imgArray = new BufferedImage[len];
				for (int i = 0; i < imgArray.length; i++) {
					try {
						System.out.println("hmm " + imgList[i]);
					   imgArray[i] = ImageIO.read(imgList[i]);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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

	public static URL XMLreadPath(Element elem, File startPath,
			String string) {
		String pathStr = (String) elem.getAttribute(string);
		try {
			String protocol = "file:";
			return new URL(protocol + startPath + File.separator + pathStr);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	  /**
	   * List directory contents for a resource folder. Not recursive.
	   * This is basically a brute-force implementation.
	   * Works for regular files and also JARs.
	   * 
	   * @author Greg Briggs
	   * @param clazz Any java class that lives in the same place as the resources you want.
	   * @param path Should end with "/", but not start with one.
	   * @return Just the name of each member item, not the full paths.
	   * @throws URISyntaxException 
	   * @throws IOException 
	   */
	  public static String[] getResourceListing(Class clazz, String path, String type) throws URISyntaxException, IOException {
	      URL dirURL = clazz.getClassLoader().getResource(path);
	      if (dirURL != null && dirURL.getProtocol().equals("file")) {
	        /* A file path: easy enough */
	    	  //System.out.println("1" + path);
	        return new File(dirURL.toURI()).list();
	      } 

	      if (dirURL == null) {
	    	  //System.out.println("2" + path);
	        /* 
	         * In case of a jar file, we can't actually find a directory.
	         * Have to assume the same jar as clazz.
	         */
	        String me = clazz.getName().replace(".", "/")+".class";
	        dirURL = clazz.getClassLoader().getResource(me);
	      }
	      
	      if (dirURL.getProtocol().equals("jar")) {
	    	  //System.out.println("3" + path);
	        /* A JAR path */
	        String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
	        //System.out.println("3" + jarPath);
	        jarPath = jarPath.replace("ethos.jar", "ethos_assets.jar");
	        JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
	        //System.out.println("3" + jarPath);
	        Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
	        Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
	        
	        String subpath = path.substring(path.indexOf("!")+2);
	        //System.out.println("path: " + path);
	        
	        while(entries.hasMoreElements()) {
	          String name = entries.nextElement().getName();
	          //System.out.println("jar inside: " + name);
	          if (name.startsWith(subpath)) { //filter according to the path
	            String entry = name.substring(subpath.length());
	            int checkSubdir = entry.indexOf("/");
	            if (checkSubdir >= 0) {
	              // if it is a subdirectory, we just return the directory name
	              entry = entry.substring(0, checkSubdir);
	            }
	            if (type.equals("img") && (entry.contains(".png") || entry.contains(".jpg") || entry.contains(".gif")) ||
	            	type.equals("xml") && entry.contains(".xml") ) {
	            	result.add(path + entry);
	          }
	           //System.out.println("jar filtered: " + entry);
	          }
	        }
	        return result.toArray(new String[result.size()]);
	      } 
	        
	      throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
	  }
}
