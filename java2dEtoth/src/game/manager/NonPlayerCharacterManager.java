package game.manager;

import game.EtothGame;
import game.character.EvilNPC;
import game.character.FriendlyNPC;
import game.character.NonPlayerCharacter;
import game.exceptions.CanNotReadFileException;
import game.exceptions.NotFoundException;

import game.exceptions.FolderContainsNoFilesException;
import game.fight.NpcFightSprite;
import game.helper.IOHelper;
import game.helper.DirectionHelper.Direction;
import game.math.Vector2d;

import java.awt.image.BufferedImage;
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

//TODO: TileManager, ItemManager und NPCManager 
//			von einer gleich Klasse erben lassen!!?
public class NonPlayerCharacterManager {

	private EtothGame game;
	private ArrayList<NonPlayerCharacter> npcs;

	public NonPlayerCharacterManager(EtothGame game) 
			throws FolderContainsNoFilesException, CanNotReadFileException, 
			SAXException, IOException, ParserConfigurationException {
		this.game = game;
		loadNpcsArray();
	}

	private void loadNpcsArray() throws FolderContainsNoFilesException, 
			CanNotReadFileException, SAXException, IOException, 
			ParserConfigurationException {
		npcs = new ArrayList<NonPlayerCharacter>();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(game.getResourceURL(game.NPCFILEPATH).openStream());
		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("npc");
		for (int i = 0; i < nodeLst.getLength(); i++) {
			Node fstNode = nodeLst.item(i);
			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
				Element fstElmnt = (Element) fstNode;

				//TODO: Exceptions
				/*int type = IOHelper.XMLreadInt(fstElmnt, "type");
				Vector2d tilePos = new Vector2d(0, 0);
				String imgPath = fstElmnt.getAttribute("imgPath");
				boolean evil = IOHelper.XMLreadBooleanSafe(fstElmnt, "evil");
				String msgImgPath = fstElmnt.getAttribute("dialogImgPath");
				if (msgImgPath == "") {
					msgImgPath = null;
				}

				NpcFightSprite fightSpr = null;
				if (evil) {
					String fightName = fstElmnt.getAttribute("fightName");
					File imagesPath = IOHelper.XMLreadPath(
							fstElmnt, game.FIGHTCHARSIDEIMGPATH, 
							"fightImgPath");
					BufferedImage[] images = IOHelper.getImages(
							game, imagesPath);	
					File projImgsPath = IOHelper.XMLreadPath(
							fstElmnt, game.FIGHTIMGPATH, 
							"fightProjPath");			
					BufferedImage[] projImgs = IOHelper.getImages(
							game, projImgsPath);
					Direction direction = Direction.LEFT;
					File hitSound = IOHelper.XMLreadPath(
							fstElmnt, game.SOUNDPATH, "hitSound");
					File projectileSound = IOHelper.XMLreadPath(
							fstElmnt, game.SOUNDPATH, "projectileSound");
					
					fightSpr = new NpcFightSprite(game, fightName, images, 
							projImgs, direction, hitSound, projectileSound);
				}

				npcs.add(
						new NonPlayerCharacter(
								game, -1, type, tilePos, imgPath, fightSpr, 
								evil, msgImgPath
						)
				);*/
				
				NonPlayerCharacter npc;
				int type = IOHelper.XMLreadInt(fstElmnt, "type");
				Vector2d tilePos = new Vector2d(0, 0);
				String imgPath = fstElmnt.getAttribute("imgPath");
				String msgImgPath = fstElmnt.getAttribute("dialogImgPath");
				if (msgImgPath == "") {
					msgImgPath = null;
				}
				boolean evil = IOHelper.XMLreadBooleanSafe(fstElmnt, "evil");
				if (evil) {
					String fightName = fstElmnt.getAttribute("fightName");
					URL imagesPath = IOHelper.XMLreadPath(
							fstElmnt, game.getResourceFile(game.FIGHTCHARSIDEIMGPATH), 
							"fightImgPath");
					BufferedImage[] images = IOHelper.getImages(
							game, imagesPath);	
					URL projImgsPath = IOHelper.XMLreadPath(
							fstElmnt, game.getResourceFile(game.FIGHTIMGPATH), 
							"fightProjPath");			
					BufferedImage[] projImgs = IOHelper.getImages(
							game, projImgsPath);
					Direction direction = Direction.LEFT;
					String hitSound = IOHelper.XMLreadString(
							fstElmnt, game.getResourceFile(game.SOUNDPATH), "hitSound");
					String projectileSound = IOHelper.XMLreadString(
							fstElmnt, game.getResourceFile(game.SOUNDPATH), "projectileSound");
					
					NpcFightSprite fightSpr = new NpcFightSprite(game, fightName, images, 
							projImgs, direction, hitSound, projectileSound);
					npc = new EvilNPC(game, -1, type, tilePos, imgPath, msgImgPath, fightSpr);
				} else {
					npc = new FriendlyNPC(game, -1, type, tilePos, imgPath, msgImgPath);
				}
				npcs.add(npc);
			}
		}
	}

	public NonPlayerCharacter getNpc(int type) throws NotFoundException {
		for (NonPlayerCharacter npc: npcs) {
			if (((Integer) npc.getType()).equals(type)) {
				return npc.clone();
			}
		}
		throw new NotFoundException("NPC not Found with type: " + type);
	}
}
