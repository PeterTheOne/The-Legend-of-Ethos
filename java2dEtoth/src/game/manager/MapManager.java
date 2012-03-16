package game.manager;

import java.awt.Graphics2D;
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

import game.EtothGame;
import game.Map;
import game.exceptions.CanNotReadFileException;
import game.exceptions.FolderContainsNoFilesException;
import game.exceptions.IsNoDirectoryException;
import game.exceptions.NotFoundException;

public class MapManager {

	private EtothGame game;
	private ArrayList<Map> maps;
	private Map currentMap;

	public MapManager(EtothGame game) throws ParserConfigurationException, 
			SAXException, IOException, CanNotReadFileException, 
			IsNoDirectoryException, FolderContainsNoFilesException, 
			NotFoundException {
		this.game = game;
		loadMapsArray();
		currentMap = getMap(game.STARTMAPFILENAME);
	}

	private void loadMapsArray() throws ParserConfigurationException, 
			SAXException, IOException, CanNotReadFileException, 
			IsNoDirectoryException, FolderContainsNoFilesException, 
			NotFoundException {		
		maps = new ArrayList<Map>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(game.getResourceURL(game.MAPFILEPATH).openStream());
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("map");
		for (int i = 0; i < nodeLst.getLength(); i++) {
			Node fstNode = nodeLst.item(i);
			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
				Element fstElmnt = (Element) fstNode;

				//TODO: Exceptions?
				String name = fstElmnt.getAttribute("name");
				String xmlPath = fstElmnt.getTextContent();

				URL xmlFile = new URL(
						game.getResourceURL(game.MAPPATH) + 
						"/" + 
						xmlPath
				);
				
				maps.add(new Map(game, xmlFile));
			}
		}
	}

	public Map getMap(String name) throws NotFoundException {
		for (Map map : maps) {
			if(map.getName().equals(name)) {
				return map;
			}
		}
		throw new NotFoundException("Map not Found: " + name);
	}

	public Map getCurrentMap() {
		return currentMap;
	}

	public void update(long elapsedTime) throws Exception {
		currentMap.update(elapsedTime);
	}

	public void render(Graphics2D g) {
		currentMap.render(g);
	}

	public void changeMap(String target) throws NotFoundException {
		currentMap = getMap(target);
		currentMap.playEnterMapSound();
	}
}
