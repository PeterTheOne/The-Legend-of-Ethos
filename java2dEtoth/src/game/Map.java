package game;

import game.character.EvilNPC;
import game.character.FriendlyNPC;
import game.character.NonPlayerCharacter;
import game.exceptions.CanNotReadFileException;
import game.exceptions.FolderContainsNoFilesException;
import game.exceptions.NotFoundException;
import game.helper.DirectionHelper;
import game.helper.IOHelper;
import game.helper.DirectionHelper.Direction;
import game.math.Vector2d;
import game.tileObjects.Door;
import game.tileObjects.Info;
import game.tileObjects.Item;
import game.tileObjects.Tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

import com.golden.gamedev.object.Sprite;

public class Map {

	private EtothGame game;
	private URL mapFilePath;
	private String name;
	private ArrayList<Tile> tiles;
	private ArrayList<Door> doors;
	private ArrayList<Info> information;
	private ArrayList<Item> items;
	private ArrayList<NonPlayerCharacter> npcs;
	private Vector2d playerStart;
	private Long lastModified;
	private Sprite fightBgSpr;
	private File enterMapSound;
	private boolean caveMode;

	public Map(EtothGame game, URL mapFilePath)
			throws ParserConfigurationException, SAXException, IOException, 
			CanNotReadFileException, FolderContainsNoFilesException, 
			NotFoundException {
		this.game = game;
		this.mapFilePath = mapFilePath;
		this.name = mapFilePath.toString().substring( mapFilePath.toString().lastIndexOf('/')+1, mapFilePath.toString().length() );/*.getName()*/;
		loadMapFile(mapFilePath);
	}

	private void loadMapFile(URL mapFilePath)
			throws ParserConfigurationException, SAXException, IOException,
			CanNotReadFileException, FolderContainsNoFilesException,
			NotFoundException {
		//if (!mapFilePath.canRead())	throw new CanNotReadFileException();

		//lastModified = mapFilePath.lastModified();

		tiles = new ArrayList<Tile>();
		doors = new ArrayList<Door>();
		information = new ArrayList<Info>();
		items = new ArrayList<Item>();
		npcs = new ArrayList<NonPlayerCharacter>();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		// Convert a URL to a URI
		/*URI uri = null;
		try {
		    uri = new URI(mapFilePath.toString());
		} catch (URISyntaxException e) {
		}
		Document doc = db.parse(uri.toString());*/
		Document doc = db.parse(mapFilePath.openStream());

		doc.getDocumentElement().normalize();
		NodeList nodeLst = doc.getElementsByTagName("row");
		for (int i = 0; i < nodeLst.getLength(); i++) {
			Node fstNode = nodeLst.item(i);
			if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
				NodeList nodeLst2 = fstNode.getChildNodes();
				for (int j = 0; j < nodeLst2.getLength(); j++) {
					Node fstNode2 = nodeLst2.item(j);
					Element fstElmnt2 = (Element) fstNode2;

					int id = IOHelper.XMLreadInt(fstElmnt2, "id");
					Tile tile = game.tileMana.getTile(id);
					tile.setTilePos(new Vector2d(j, i));
					tiles.add(tile);

				}
			}
		}

		nodeLst = doc.getElementsByTagName("player");
		Element fstElmnt = (Element) nodeLst.item(0);
		playerStart = new Vector2d(
				Integer.parseInt(fstElmnt.getAttribute("startX")), 
				Integer.parseInt(fstElmnt.getAttribute("startY"))
		);

		this.enterMapSound = null; 
		nodeLst = doc.getElementsByTagName("enterMapSound");
		fstElmnt = (Element) nodeLst.item(0);
		if (fstElmnt != null) {
			this.enterMapSound = new File(game.getResourceFile(game.SOUNDPATH) + 
					File.separator + 
					fstElmnt.getAttribute("path") 
			);
		}

		nodeLst = doc.getElementsByTagName("background");
		fstElmnt = (Element) nodeLst.item(0);
		String path;
		if (fstElmnt != null) {
			path = fstElmnt.getAttribute("path");
			if (path == null || path.equals("") || path.equals(" ")) {
				path = "fight_bg.jpg";
			}
		} else {
			path = "fight_bg.jpg";
		}
		
		nodeLst = doc.getElementsByTagName("caveMode");
		fstElmnt = (Element) nodeLst.item(0);
		if (fstElmnt != null) {
			caveMode = IOHelper.XMLreadBooleanSafe(fstElmnt, "on");
		} else {
			caveMode = false;
		}
		if (caveMode) {
			//TODO...
			URL fogPath = new URL(game.getResourceURL(game.IMGPATH) + 
				"/" + 
				"fogofwar" + 
				"/" + 
				"fogofwar_cave.png");
			for (Tile tile : tiles) {
				tile.setSprHidden(fogPath);
			}
		}
		
		fightBgSpr = new Sprite(
				game.getImage(
						game.fileToURL(game.FIGHTIMGPATH + 
						File.separator + 
						path)
				)
		);

		nodeLst = doc.getElementsByTagName("door");
		for (int i = 0; i < nodeLst.getLength(); i++) {
			fstElmnt = (Element) nodeLst.item(i);

			//TODO: Exceptions
			int id = Integer.parseInt((String) fstElmnt.getAttribute("id"));
			int x = Integer.parseInt((String) fstElmnt.getAttribute("x"));
			int y = Integer.parseInt((String) fstElmnt.getAttribute("y"));
			String front = fstElmnt.getAttribute("direction");
			Direction direction = Direction.DOWN;
			if (front.toLowerCase().equals("up")) {
				direction = Direction.UP;
			} else if (front.toLowerCase().equals("down")) {
				direction = Direction.DOWN;
			} else if (front.toLowerCase().equals("right")) {
				direction = Direction.RIGHT;
			} else if (front.toLowerCase().equals("left")) {
				direction = Direction.LEFT;
			}
			String target = fstElmnt.getAttribute("target");
			int targetId = Integer.parseInt((String) fstElmnt.getAttribute("targetId"));

			doors.add(new Door(id, x, y, direction, target, targetId));
		}

		nodeLst = doc.getElementsByTagName("info");
		for (int i = 0; i < nodeLst.getLength(); i++) {
			fstElmnt = (Element) nodeLst.item(i);

			//TODO: Exceptions
			int x = Integer.parseInt((String) fstElmnt.getAttribute("x"));
			int y = Integer.parseInt((String) fstElmnt.getAttribute("y"));
			Vector2d tilePos = new Vector2d(x, y); 
			String info = fstElmnt.getTextContent();
			Boolean solid = Boolean.parseBoolean((String) fstElmnt.getAttribute("solid"));
			URL imgFile = game.getResourceURL(new File(EtothGame.fileToURL(game.INOFSIMGPATH + File.separator +(String) fstElmnt.getAttribute("img"))));
			BufferedImage imgArray[] = IOHelper.getImages(game, imgFile);
			boolean light = IOHelper.XMLreadBooleanSafe(fstElmnt, "light");

			information.add(
					new Info(
							game,
							imgArray,
							tilePos,
							solid,
							info,
							light
					)
			);
		}

		nodeLst = doc.getElementsByTagName("item");
		for (int i = 0; i < nodeLst.getLength(); i++) {
			fstElmnt = (Element) nodeLst.item(i);

			//TODO: Exceptions
			int type = Integer.parseInt((String) fstElmnt.getAttribute("type"));
			int x = Integer.parseInt((String) fstElmnt.getAttribute("x"));
			int y = Integer.parseInt((String) fstElmnt.getAttribute("y"));

			Item item = game.itemMana.getItem(type);
			item.setTilePos(new Vector2d(x, y));
			items.add(item);
		}

		nodeLst = doc.getElementsByTagName("npc");
		for (int i = 0; i < nodeLst.getLength(); i++) {
			fstElmnt = (Element) nodeLst.item(i);

			//TODO: Exceptions
			int type = Integer.parseInt((String) fstElmnt.getAttribute("type"));
			int x = Integer.parseInt((String) fstElmnt.getAttribute("x"));
			int y = Integer.parseInt((String) fstElmnt.getAttribute("y"));
			Vector2d tilePos = new Vector2d(x, y);
			String dialogPath = fstElmnt.getAttribute("dialogPath");
			String direction = fstElmnt.getAttribute("direction");
			boolean walks = IOHelper.XMLreadBooleanSafe(fstElmnt, "walks");

			NonPlayerCharacter npc = game.npcMana.getNpc(type);
			npc.setPos(tilePos);
			npc.setDialogPath(dialogPath);
			npc.setDirection(direction);
			npc.setMapId(i);
			npc.setWalks(walks);
			
			if (npc instanceof EvilNPC) {
				boolean attacks = IOHelper.XMLreadBooleanSafe(fstElmnt, "attacks");
				((EvilNPC) npc).setAttacks(attacks);
				Integer maxJumps = IOHelper.XMLreadInt(fstElmnt, "maxJumps");
				Integer damage = IOHelper.XMLreadInt(fstElmnt, "damage");
				Boolean enableUltraAI = IOHelper.XMLreadBoolean(fstElmnt, "enableUltraAI");
				Integer shootFreq = IOHelper.XMLreadInt(fstElmnt, "shootFreq");
				Integer jumpFreq = IOHelper.XMLreadInt(fstElmnt, "jumpFreq");
				
				if (!(maxJumps == null ||
						damage == null ||
						enableUltraAI == null ||
						shootFreq == null ||
						jumpFreq == null)) {
					((EvilNPC) npc).getFightSpr().setAttributes(maxJumps, 
							damage, enableUltraAI, shootFreq, jumpFreq);
				}
			} else if (npc instanceof FriendlyNPC) {
				boolean follow = IOHelper.XMLreadBooleanSafe(fstElmnt, "follow");
				((FriendlyNPC) npc).setFollow(follow);
			}
			
			npcs.add(npc);
		}
	}

	public void reload()
	throws
	ParserConfigurationException,
	SAXException,
	IOException,
	CanNotReadFileException,
	FolderContainsNoFilesException,
	NotFoundException {
		System.out.print("reloadCurrentMapMode: on | try reload: ");
		/*if (mapFilePath.lastModified() > lastModified) {
			loadMapFile(mapFilePath);
			System.out.println("reloaded: " + name);
		} else {*/
			System.out.println("no reload: " + name);
		//}
	}

	public boolean isSolid(Vector2d pos, Direction dir) {
		return (pos.getX() < 0
				|| pos.getX() > 19 
				|| pos.getY() < 0
				|| pos.getY() > 14
				||isSolidTiles(pos) 
				|| isSolidNpcs(pos) 
				|| isSolidInfos(pos) 
				|| isSolidDoors(pos, dir)
				|| game.player.getTilePos().equals(pos));
	}

	public boolean isSolidTiles(Vector2d pos) {
		Tile tile = getTile(pos);
		if (tile == null) {
			return true;
		}
		return tile.getSolid();
	}
	
	private Tile getTile(Vector2d pos) {
		//Optimierte Methode:
		if (tiles.size() == 15 * 20) {
			return tiles.get((int) (pos.getY() * 20 + pos.getX()));
		}

		//Alternative Methode:
		for (Tile tile : tiles) {
			if (tile.getTilePos().equals(pos)) {
				return tile;
			}
		}
		return null;
		//Verhinter das abstürzen wenn die map "offen" ist
	}

	public boolean isSolidNpcs(Vector2d pos) {
		for (NonPlayerCharacter npc : npcs) {
			if (((Vector2d) npc.getTilePos()).equals(pos)) {
				return true;
			}
		}
		return false;
	}

	private boolean isSolidInfos(Vector2d pos) {
		for (Info info : information) {
			if (((Vector2d) info.getTilePos()).equals(pos)
					&& info.getSolid()) {
				return true;
			}
		}
		return false;
	}

	private boolean isSolidDoors(Vector2d pos, Direction dir) {
		for (Door door : doors) {
			if (((Vector2d) door.getPos()).equals(pos) 
					&& !DirectionHelper.isOpposite(dir, door.getDirection())) {
				return true;
			}
		}
		return false;
	}

	public void update(long elapsedTime) throws Exception {
		for (Tile tile : tiles) {
			tile.update(elapsedTime);
		}
		for (Item item : items) {
			item.update(elapsedTime);
		}
		for (Info info : information) {
			info.update(elapsedTime);
		}
		for (NonPlayerCharacter npc : npcs) {
			npc.update(elapsedTime);
		}
	}

	public void render(Graphics2D g) {
		for (Tile tile : tiles) {
			tile.render(g);
		}
		for (Info info : information) {
			info.render(g);
		}
		for (Item item : items) {
			item.render(g);
		}
		//if (!(caveMode 
		//		&& game.gameStateMachine.getState().equals(GameState.PLAY))) {
			for (NonPlayerCharacter npc : npcs) {
				npc.render(g);
			}	
		//}
	}

	public void removeItem(Item item) {
		items.remove(item);
	}

	public void interact() throws Exception {
		Vector2d pos = DirectionHelper.getPosInDirection(
							game.player.getTilePos(),
							game.player.getDirection()
		);
		
		NonPlayerCharacter npc = getNPC(pos);
		if (npc != null) {
			npc.interact(game.player.getTilePos());
			return;
		}
		Info info = getInfo(pos);
		if (info != null) {
			game.gameSounds.playSound("info");
			game.msgMana.setMsg(info.getInfo());
			return;
		}
		Item item = getItem(pos);
		if (item != null) {
			game.gameSounds.playSound("findItem");
			game.msgMana.setMsg(
					game.gameTexts.getText("seesomething") + item.toString());
			return;
		}
	}

	public void updateVisible() {
		if (game.FOGOFWAR) {
			for (Tile tile : tiles) {
				tile.updateVisible(game);
			}
			/*for (NonPlayerCharacter npc : npcs) {
				npc.updateVisible();
			}
			for (Item item : items) {
				item.updateVisible(game);
			}
			for (Info info : information) {
				info.updateVisible(game);
			}*/
		}
		//TODO: optimize!
		for (Tile tile : tiles) {
			if (tile.isVisible()) {
				Vector2d tilePos = tile.getTilePos();
				NonPlayerCharacter npc = getNPC(tilePos);
				if (npc != null) {
					npc.setVisible(true);
				}
				Item item = getItem(tilePos);
				if (item != null) {
					item.setVisible(true);
				}
				Info info = getInfo(tilePos);
				if (info != null) {
					info.setVisible(true);
				}
			} else {
				Vector2d tilePos = tile.getTilePos();
				NonPlayerCharacter npc = getNPC(tilePos);
				if (npc != null) {
					npc.setVisible(false);
				}
				Item item = getItem(tilePos);
				if (item != null) {
					item.setVisible(false);
				}
				Info info = getInfo(tilePos);
				if (info != null) {
					info.setVisible(false);
				}
			}
		}
	}

	public void removeNpc(NonPlayerCharacter npc) {
		npcs.remove(npc);
	}

	public String getName() {
		return name;
	}

	public Door getDoor(int id) throws NotFoundException {
		for (Door door : doors) {
			if (((Integer)door.getId()).equals(id)) {
				return door;
			}
		}
		throw new NotFoundException("Door not Found");
	}

	public Info getInfo(Vector2d pos) {
		for (Info info : information) {
			if (info.getTilePos().equals(pos)) {
				return info;
			}
		}
		return null;
	}

	public NonPlayerCharacter getNPCLookingAt(Vector2d pos) {
		ArrayList<NonPlayerCharacter> npcsLooking = new ArrayList<NonPlayerCharacter>();
		npcsLooking.add(getNPC(pos.add(1, 0)));
		npcsLooking.add(getNPC(pos.add(0, 1)));
		npcsLooking.add(getNPC(pos.add(-1, 0)));
		npcsLooking.add(getNPC(pos.add(0, -1)));
		for (NonPlayerCharacter npc : npcsLooking) {
			if (
					npc != null
					&& (npc instanceof EvilNPC && ((EvilNPC) npc).getAttacks())
					&& DirectionHelper.getPosInDirection(npc.getTilePos(), npc.getDirection()).equals(pos)
			) {
				return npc;
			}
		}
		return null;
	}

	public NonPlayerCharacter getNPC(Vector2d pos) {
		//TODO: not found exception
		for (NonPlayerCharacter npc : npcs) {
			if (npc.getTilePos().equals(pos)) {
				return npc;
			}
		}
		return null;
	}

	public NonPlayerCharacter getNPC(int npcMapId) {
		//TODO: not found exception
		if (npcMapId == -1) {
			return npcs.get(npcs.size() - 1);
		}
		for (NonPlayerCharacter npc : npcs) {
			if (npc.getMapId() == npcMapId) {
				return npc;
			}
		}
		return null;
	}

	public Vector2d getPlayerStart() {
		return playerStart;
	}

	public Door getDoor(Vector2d tilePos) {
		for (Door door : doors) {
			if (door.getPos().equals(tilePos)) {
				return door;
			}
		}
		return null;
	}

	public Item getItem(Vector2d pos) {
		for (Item item : items) {
			if (item.getTilePos().equals(pos)) {
				return item;
			}
		}
		return null;
	}

	public void addNPC(NonPlayerCharacter npc) {
		int maxId = -1;
		for (NonPlayerCharacter npcForId : npcs) {
			int npcId = npcForId.getMapId();
			if (npcId > maxId) {
				maxId = npcId;
			}
		}
		npc.setMapId(maxId);
		npcs.add(npc);
	}

	public void addInfo(Info info) {
		Tile tile = getTile(info.getTilePos());
		if (tile.isVisible()) {
			info.setVisible(true);
		}
		this.information.add(info);
	}

	public void removeInfo(Vector2d infoPos) {
		Info infoRemove = null;
		for (Info info : information) {
			if (info.getTilePos().equals(infoPos)) {
				infoRemove = info;
			}
		}
		if (infoRemove != null) {
			information.remove(infoRemove);
		}
	}
	
	public Sprite getFightBgSpr() {
		return fightBgSpr;
	}
	
	public void playEnterMapSound() {
		if (enterMapSound != null) {
			game.bsSound.play(enterMapSound.getAbsolutePath());
		}
	}

	public boolean getCaveMode() {
		return this.caveMode;
	}

	public ArrayList<Info> getInformation() {
		return this.information;
	}
}
