package game.helper;

import game.EtothGame;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GameTexts {
	
	class Text {
		private String name;
		private String text;
		
		public Text(String name, String text) {
			this.name = name;
			this.text = text;
		}
		
		public String getName() {
			return this.name;
		}
		
		public String getText() {
			return this.text;
		}
	}
	
	private ArrayList<Text> texts;
	
	public GameTexts(EtothGame game) throws ParserConfigurationException, 
			SAXException, IOException {
		texts = new ArrayList<Text>();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(game.getResourceURL(game.GAMETEXTSFILEPATH).openStream());
		
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("text");
		for (int i = 0; i < nodeLst.getLength(); i++) {
			Node fstNode = nodeLst.item(i);
			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
				Element fstElmnt = (Element) fstNode;

				//TODO: Exceptions?
				String name = fstElmnt.getAttribute("name");
				String text = fstElmnt.getTextContent();

				texts.add(
					new Text(
						name,
						text
					)
				);
			}
		}
	}
	
	public String getText(String name) {
		for (Text text : texts) {
			if (name.equals(text.getName())) {
				return text.getText();
			}
		}
		System.out.println("err: text not found..");
		return "";
	}
	
}
