package game.helper;

import game.EtothGame;

import java.io.File;
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

public class GameSounds {
	
	class SoundFile {
		private String name;
		private URL sound;
		
		public SoundFile(String name, URL sound) {
			this.name = name;
			this.sound = sound;
		}
		
		public String getName() {
			return this.name;
		}
		
		public URL getSound() {
			return this.sound;
		}
	}
	private EtothGame game;
	private ArrayList<SoundFile> sounds;
	
	public GameSounds(EtothGame game) throws ParserConfigurationException, 
			SAXException, IOException {
		this.game = game;
		sounds = new ArrayList<SoundFile>();		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(game.getResourceURL(game.GAMESOUNDSSFILEPATH).openStream());
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("sound");
		for (int i = 0; i < nodeLst.getLength(); i++) {
			Node fstNode = nodeLst.item(i);
			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
				Element fstElmnt = (Element) fstNode;

				//TODO: Exceptions?
				String name = fstElmnt.getAttribute("name");
				String soundPath = fstElmnt.getTextContent();

				sounds.add(
					new SoundFile(
							name,
							new URL(
									game.getResourceURL(game.SOUNDPATH) + 
									"/" + 
									soundPath
							)
					)
					
				);
			}
		}
	}
	
	public void playSound(String name) {
		for (SoundFile soundFile : sounds) {
			if (name.equals(soundFile.getName())) {
				game.bsSound.play(soundFile.getSound().toString());
			}
		}
	}
	
	public void stopSound(String name) {
		for (SoundFile soundFile : sounds) {
			if (name.equals(soundFile.getName())) {
				game.bsSound.stop(soundFile.getSound().toString());
			}
		}
	}
	
}
