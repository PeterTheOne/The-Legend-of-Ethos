package game.manager;

import game.EtothGame;
import game.exceptions.FolderContainsNoFilesException;
import game.exceptions.NotFoundException;
import game.fight.PlayerFightSprite;
import game.helper.IOHelper;
import game.helper.DirectionHelper.Direction;
import game.tileObjects.Item;
import game.tileObjects.MonsterItem;

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

//TODO: TileManager, ItemManager und 
//			NPCManager von einer gleich Klasse erben lassen!!?
public class ItemManager {	

	private EtothGame game;
	private ArrayList<Item> items;

	public ItemManager(EtothGame game) throws ParserConfigurationException, 
			SAXException, IOException, FolderContainsNoFilesException {
		this.game = game;
		loadItemsArray();
	}

	private void loadItemsArray() throws ParserConfigurationException, 
			SAXException, IOException, FolderContainsNoFilesException {
		items = new ArrayList<Item>();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(game.getResourceURL(game.ITEMFILEPATH).openStream());
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("item");
		for (int i = 0; i < nodeLst.getLength(); i++) {
			Node fstNode = nodeLst.item(i);
			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
				Element fstElmnt = (Element) fstNode;

				//TODO: Exceptions? wen etwas schief geht beim parsen..
				int type = IOHelper.XMLreadInt(fstElmnt, "type");
				String name = fstElmnt.getAttribute("name");
				String info = fstElmnt.getTextContent();				
				String imgFile = IOHelper.XMLreadString( fstElmnt, "img" );
				BufferedImage[] imgArray = game.gameImages.getImages( game.ITEMSIMG, imgFile );
				
				
				boolean monster = IOHelper.XMLreadBooleanSafe(
						fstElmnt, "monster");
				
				if (monster) {
					String imagesPath = IOHelper.XMLreadString(
							fstElmnt,
							"fightImgPath");
					String projImgsPath = IOHelper.XMLreadString(
							fstElmnt, 
							"fightProjPath");
					
					BufferedImage[] images = game.gameImages.getImages(game.FIGHTCHARSIDEIMG, imagesPath);
					BufferedImage[] projImgs = game.gameImages.getImages(game.FIGHTIMG, projImgsPath); 
					
					String fightName = fstElmnt.getAttribute("fightName");
					
					String hitSound = IOHelper.XMLreadString(
							fstElmnt, "hitSound");
					String projectileSound = IOHelper.XMLreadString(
							fstElmnt, "projectileSound");
					
					PlayerFightSprite fightSpr = new PlayerFightSprite(game, 
							fightName, images, projImgs, Direction.RIGHT, 
							hitSound, projectileSound);
					

					Integer maxJumps = IOHelper.XMLreadInt(
							fstElmnt,"maxJumps");
					Integer damage = IOHelper.XMLreadInt(
							fstElmnt, "damage");
					Integer jumpHeight = IOHelper.XMLreadInt(
							fstElmnt, "jumpHeight");
					Integer shootFreezeTime = IOHelper.XMLreadInt(
							fstElmnt, "shootFreezeTime");
					Double resistance = IOHelper.XMLreadDouble(
							fstElmnt, "resistance");
					
					//TODO: exception? or what
					if (!(maxJumps == null ||
							damage == null ||
							jumpHeight == null ||
							shootFreezeTime == null || 
							resistance == null)) {
						fightSpr.setAttributes(maxJumps, damage, jumpHeight, 
								shootFreezeTime, resistance);
					}
					
					items.add(
							new MonsterItem(
								game,
								imgArray,
								type,
								name,
								info,
								fightSpr
							)
						);
				} else {
					items.add(
						new Item(
							game,
							imgArray,
							type,
							name,
							info
						)
					);
				}
			}
		}
	}

	public Item getItem(int type) throws NotFoundException {
		for (Item item: items) {
			if (((Integer) item.getType()).equals(type)) {
				return item.clone();
			}
		}
		throw new NotFoundException("Item not Found");
	}

}
