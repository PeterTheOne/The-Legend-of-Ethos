package game.character;

import java.awt.Graphics2D;
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

import game.EtothGame;
import game.GameStateMachine.GameState;
import game.exceptions.CanNotReadFileException;
import game.exceptions.FolderContainsNoFilesException;
import game.helper.DirectionHelper;
import game.math.Vector2d;

public abstract class NonPlayerCharacter extends Character {

	protected String msgArray[];
	private ArrayList<String> msgList;
	protected String msgImgPath;
	protected BufferedImage msgImg;
	
	protected boolean visible;
	protected int type;
	protected int mapId;
	
	private boolean walks;
	private long elapsedTimeCache;
	private boolean attacks;

	public NonPlayerCharacter(EtothGame game, int mapId, int type, 
			Vector2d tilePos, String imgPath, String msgImgPath) 
			throws FolderContainsNoFilesException, CanNotReadFileException, 
			SAXException, IOException, ParserConfigurationException {
		super(game, tilePos, imgPath);
		this.mapId = mapId;
		this.type = type;
		this.msgList = new ArrayList<String>();
		this.visible = !game.FOGOFWAR;;
		this.msgImgPath = msgImgPath;
		this.msgImg = loadMsgImgXML(msgImgPath);
		this.walks = false;
		this.elapsedTimeCache = 0;
		this.attacks = false;
	}

	public void setWalks(boolean walks) {
		this.walks = walks;
	}
	
	public void setAttacks(boolean attacks) {
		this.attacks = attacks;
	}
	
	public boolean getAttacks() {
		return this.attacks;
	}
	
	public void setDialogPath(String dialogPath) 
			throws CanNotReadFileException, SAXException, 
			IOException, ParserConfigurationException {
		msgList = new ArrayList<String>();
		this.msgArray = loadMsgXML(dialogPath);
		msgList.toArray(msgArray);
	}

	private String[] loadMsgXML(String dialogPath) 
			throws CanNotReadFileException, SAXException, IOException, 
			ParserConfigurationException {
		
		File xmlfile = new File(game.getResourceFile(game.NPCDIALOGPATH) + File.separator + dialogPath);
		//if (!xmlfile.canRead())	throw new CanNotReadFileException();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(game.getResourceURL(new File(game.NPCDIALOGPATH + File.separator + dialogPath)).openStream());

		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("line");
		for (int i = 0; i < nodeLst.getLength(); i++) {
			Node fstNode = nodeLst.item(i);
			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
				Element fstElmnt = (Element) fstNode;
				msgList.add(fstElmnt.getTextContent());
			}
		}
		String msgArray[] = new String[msgList.size()];
		return msgArray;
	}

	private BufferedImage loadMsgImgXML(String dialogImgPath) 
			throws CanNotReadFileException, SAXException, IOException, 
			ParserConfigurationException, FolderContainsNoFilesException {
		if (dialogImgPath == null || dialogImgPath == "") {
			return null;
		}
		String path = new String(
				game.fileToURL(game.CHARDIAIMGPATH + File.separator + dialogImgPath));
		return game.getImage(path);
	}
	
	public void interact(Vector2d playerPos) throws Exception {		
		direction = DirectionHelper.getDirection(tilePos, playerPos);
		game.player.setDirection(DirectionHelper.reverse(direction));
		game.gameSounds.playSound("npcTalk");
		game.msgMana.setEndInteractionCallback(this);
		if (msgImg != null) {
			game.msgMana.setMsg(msgArray, msgImg);
		} else {
			game.msgMana.setMsg(msgArray);
		}
		game.questMana.updateNpcInteract(mapId);
	}
	
	public void update(Long elapsedTime) throws Exception {
		super.update(elapsedTime);
		if (walks && game.gameStateMachine.getState() == GameState.PLAY) {
			//TODO: freeze only the fnpc that you are talking to
			elapsedTimeCache += elapsedTime;
			if (elapsedTimeCache > 1000) {
				elapsedTimeCache = 0;
				if (Math.random() * 3 <= 1) {
					int value = Math.round((Math.random())) == 1 ? -1 : 1;
					boolean xOrY = Math.round((Math.random())) == 1 ? true : false;
					if (xOrY) {
						//System.out.print("random: " + value + ", " + 0);
						move(value, 0);
					} else {
						//System.out.print("random: " + 0 + ", " + value);
						move(0, value);
					}
					if (this.attacks) {
						Vector2d playerPos = game.player.getTilePos();
						if (DirectionHelper.getPosInDirection(tilePos, direction)
								.equals(playerPos)) {
							interact(playerPos);
						}
					}
					
					//System.out.println(", " + getTilePos());
				}
			}
		}
	}
	
	public void interactStop() {
		game.gameSounds.stopSound("npcTalk");
	}
	
	protected void stop(boolean resetTilePosX, boolean resetTilePosY) 
			throws Exception {
		super.stop(resetTilePosX, resetTilePosY);
		/*Vector2d playerPos = game.player.getTilePos();
		if (DirectionHelper.getPosInDirection(tilePos, direction).
				equals(playerPos)) {
			interact(playerPos);
		}*/
		game.mapMana.getCurrentMap().updateVisible();
	}

	public void updateVisible() {
		if (visible != true 
				&& game.player.getTilePos().getDistance(tilePos) 
				< game.VISIBLEDIS) {
			visible = true;
		}
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void render(Graphics2D g) {
		if (this.visible) {
			this.charSprite.render(g);
		}
	}

	public int getType() {
		return this.type;
	}

	public int getMapId() {
		return this.mapId;
	}

	public void setMapId(int i) {
		this.mapId = i;
	}
	
	public abstract NonPlayerCharacter clone();
	
	public FollowingCharacter getFollowingChar() 
			throws FolderContainsNoFilesException, CanNotReadFileException, 
			SAXException, IOException, ParserConfigurationException {
		return new FollowingCharacter(game, this);
	}
}
