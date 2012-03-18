package game.manager;

import game.EtothGame;
import game.exceptions.FolderContainsNoFilesException;
import game.exceptions.NotFoundException;
import game.helper.IOHelper;
import game.tileObjects.Tile;

import java.awt.image.BufferedImage;
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

//TODO: TileManager, ItemManager und NPCManager 
//				von einer gleich Klasse erben lassen!!?
public class TileManager {

	private EtothGame game;
	private ArrayList<Tile> tiles;

	public TileManager(EtothGame game) throws ParserConfigurationException, 
			SAXException, IOException, FolderContainsNoFilesException {
		this.game = game;
		loadTilesArray();
	}

	private void loadTilesArray() throws ParserConfigurationException, 
			SAXException, IOException, FolderContainsNoFilesException {
		tiles = new ArrayList<Tile>();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(game.getResourceURL(game.TILEFILEPATH).openStream());
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("tile");
		for (int i = 0; i < nodeLst.getLength(); i++) {
			Node fstNode = nodeLst.item(i);
			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
				Element fstElmnt = (Element) fstNode;
				
				//TODO: Exceptions?
				int id = Integer.parseInt((String) fstElmnt.getAttribute("id"));
				Boolean solid = Boolean.parseBoolean((String) fstElmnt.getAttribute("solid"));
				String imgFile = IOHelper.XMLreadString( fstElmnt, "img" );
				//System.out.println("---");
				BufferedImage[] imgArray = game.gameImages.getImages( game.TILESIMG, imgFile );
				
				tiles.add(
						new Tile(
								game,
								imgArray,
								id,
								solid
						)
				);
				
				//System.out.println("tile " + id + " " + imgFile + " " + imgArray[0].toString());
			}
		}
	}
	
	public Tile getTile(int id) throws NotFoundException {
		for (Tile tile: tiles) {
			if (((Integer) tile.getType()).equals(id)) {
				return tile.clone();
			}
		}
		throw new NotFoundException("Tile not Found");
	}

}
