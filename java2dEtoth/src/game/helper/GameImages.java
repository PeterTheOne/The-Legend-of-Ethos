package game.helper;

import game.EtothGame;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GameImages {
	class ImageFile {
		private String name;
		private String category;
		private BufferedImage[] images;
		
		public ImageFile(String name, String category, BufferedImage[] images) {
			this.name = name;
			this.category = category;
			this.images = images;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getCategory() {
			return this.category;
		}
		
		public BufferedImage[] getImages() {
			return this.images;
		}
	}
	private EtothGame game;
	private ArrayList<ImageFile> images;
	
	public GameImages(EtothGame game) throws ParserConfigurationException, 
			SAXException, IOException {
		this.game = game;
		images = new ArrayList<ImageFile>();		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(game.getResourceURL(game.GAMEIMAGESFILEPATH).openStream());
		doc.getDocumentElement().normalize();
		
		NodeList nodeLstCat = doc.getElementsByTagName("category");
		for (int j = 0; j < nodeLstCat.getLength(); j++) {
			Node fstNodeCat = nodeLstCat.item(j);
			if (fstNodeCat.getNodeType() == Node.ELEMENT_NODE) {
				Element fstElmntCat = (Element) fstNodeCat;

				//TODO: Exceptions?
				String catName = fstElmntCat.getAttribute("name");
				
				readImages(fstElmntCat, catName, "", 1);
				
				NodeList nodeLst2 = fstElmntCat.getElementsByTagName("imagesequence");
				for (int i = 0; i < nodeLst2.getLength(); i++) {
					Node fstNode = nodeLst2.item(i);
					if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
						Element fstElmnt = (Element) fstNode;
		
						//TODO: Exceptions?
						String name = fstElmnt.getAttribute("name");
		
						//System.out.println("Adding sequence " + catName + " | " + name + " ...");
						
						NodeList nodeLst = fstElmnt.getElementsByTagName("frame");
						int noImgs = nodeLst.getLength();
						
						BufferedImage[] multipleImgs = readImages(fstElmnt, catName, name, noImgs);
						
						images.add(
							new ImageFile(
									name,
									catName,
									multipleImgs
							)
						);
					}
				}
			}
		}
	}
	
	private BufferedImage[] readImages(Element fstElmntParent, String catName, String sequenceName, int noImgs)
	{
		String elementType = "image";
		if(sequenceName != "")
			elementType = "frame";
		
		if(sequenceName != "")
			sequenceName = "/" + sequenceName;
		
		BufferedImage[] imgs = new BufferedImage[noImgs];
		
		NodeList nodeLst = fstElmntParent.getElementsByTagName(elementType);
		for (int i = 0; i < nodeLst.getLength(); i++) {
			Node fstNode = nodeLst.item(i);
			if(noImgs == 1) imgs = new BufferedImage[noImgs];
			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
				Element fstElmnt = (Element) fstNode;

				//TODO: Exceptions?
				String name = fstElmnt.getAttribute("name");
				String imagePath = fstElmnt.getTextContent();

				//System.out.println("Adding " + catName + " | " + sequenceName + name + " ...");
				
				// Load image files
				String path = null;
				BufferedImage img = null;
				try {
					if(EtothGame.applet)
					{
						URL imgUrl = new URL( game.getResourceURL(game.IMGPATH) +
									"/" +
									catName +
									sequenceName +
									"/" +
									imagePath);
						path = imgUrl.toString();
					} else {
						path = game.IMGPATH +
								"/" +
								catName +
								sequenceName +
								"/" +
								imagePath;
					}
					img = game.getImage( path );
				} catch(Exception e) {
					System.out.println("Loading image " + path + " failed!");
					e.printStackTrace();
				}
				
				int index = 0;
				if(sequenceName != "")
					index = i;
				imgs[index] = img;
				
				if(noImgs == 1)
				{
					images.add(
							new ImageFile(
									name,
									catName,
									imgs
							)
					);
				}
			}
		}
		return imgs;
	}
	
	public BufferedImage getImage(String category, String name) {
		for (ImageFile imageFile : images) {
			if( name.equals( imageFile.getName() ) && category.equals( imageFile.getCategory() ) ) {
				return imageFile.getImages()[0];
			}
		}
		System.out.println("Path for image \"" + name + "\", category \"" + category + "\" not defined in XML");
		return null;
	}
	
	public BufferedImage[] getImages(String category, String name) {
		for (ImageFile imageFile : images) {
			if( name.equals( imageFile.getName() ) && category.equals( imageFile.getCategory() ) ) {
				return imageFile.getImages();
			}
		}
		System.out.println("Path for image \"" + name + "\", category \"" + category + "\" not defined in XML");
		return null;
	}
	
	public int getNoOfFrames(String category, String name) {
		return getImages(category, name).length;
	}
}