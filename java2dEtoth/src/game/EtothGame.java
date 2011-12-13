package game;

import game.character.Player;
import game.exceptions.FolderContainsNoFilesException;
import game.fight.FightManager;
import game.helper.GameSounds;
import game.helper.GameTexts;
import game.manager.InputManager;
import game.manager.ItemManager;
import game.manager.MapManager;
import game.manager.MessageManager;
import game.manager.NonPlayerCharacterManager;
import game.manager.TileManager;
import game.menus.CharMenu;
import game.menus.ChooseMonsterMenu;
import game.menus.ExitMenu;
import game.menus.HelpInfo;
import game.menus.Intro;
import game.menus.InventoryMenu;
import game.menus.MonsterTransformation;
import game.menus.Outro;
import game.menus.PlayerHealthBar;
import game.quest.QuestManager;
import game.tileObjects.Item;

import java.awt.*;
import java.io.File;

import com.golden.gamedev.*;
import com.golden.gamedev.object.GameFont;

public class EtothGame extends Game {

	//Constants:
	public final int TILESIZE = 40;
	private static final Dimension DIMENSION = new Dimension(800, 600);
	private static final boolean FULLSCREEN = true;
	public final double CHARSPEED = 0.2;
	public final double CHARANIMSPEED = 20 / CHARSPEED;
	public final double VISIBLEDIS = 4.5;
	public final double VISIBLEDISCAVE = 2.5;

	//Paths:
	public final File IMGPATH = new File("img");
	public final File TILESIMGPATH = new File(IMGPATH + File.separator + "tiles");
	public final File CHARIMGPATH = new File(IMGPATH + File.separator + "characters");
	public final File CHARDIAIMGPATH = new File(IMGPATH + File.separator + "charactersdialog");
	public final File ITEMSIMGPATH = new File(IMGPATH + File.separator + "items");
	public final File INOFSIMGPATH = new File(IMGPATH + File.separator + "infos");
	public final File FIGHTIMGPATH = new File(IMGPATH + File.separator + "fight");
	public final File FIGHTCHANGEIMGPATH = new File(FIGHTIMGPATH + File.separator + "change");
	public final File FIGHTCHARSIDEIMGPATH = new File(FIGHTIMGPATH + File.separator + "charactersideviews");
	public final File CHARMENUIMGPATH = new File(IMGPATH + File.separator + "charmenu");
	public final File EXITMENUIMGPATH = new File(IMGPATH + File.separator + "exitmenu");
	public final File INVENTORYIMGPATH = new File(IMGPATH + File.separator + "inventory");
	public final File XMLPATH = new File("xml");
	public final File MAPPATH = new File(XMLPATH + File.separator + "maps");
	public final File TILEFILEPATH = new File(XMLPATH + File.separator + "tiles.xml");
	public final File ITEMFILEPATH = new File(XMLPATH + File.separator + "items.xml");
	public final File NPCFILEPATH = new File(XMLPATH + File.separator + "npcs.xml");
	public final File NPCDIALOGPATH = new File(XMLPATH + File.separator + "npcdialog");
	public final File GAMETEXTSFILEPATH = new File(XMLPATH + File.separator + "gametexts.xml");
	public final File GAMESOUNDSSFILEPATH = new File(XMLPATH + File.separator + "gamesounds.xml");
	public final File SOUNDPATH = new File("sound");
	public final File FONTPATH = new File("font");
	public final String STARTMAPFILENAME = "map_castle_start.xml";

	//Options:
	{ distribute = false; }
	private int FPS = 30;

	//DebugModes:
	private final boolean SHOWFPS = false;
	public final boolean RELOADCURRENTMAPMODE = false;
	public final boolean FOGOFWAR = true;
	public final boolean printCurrentPos = false;
	public final boolean CHEATS = false; //schaltet keys ein zum welchseln in menu states..

	//Variables:
	public GameStateMachine gameStateMachine;
	public TileManager tileMana;
	public ItemManager itemMana;
	public NonPlayerCharacterManager npcMana;
	public MapManager mapMana;
	public Player player;
	private InputManager inputMana;
	public MessageManager msgMana;
	public InventoryMenu invMenu;
	public ChooseMonsterMenu monsterMenu ;
	public MonsterTransformation trans;
	public FightManager fightMana;
	public Intro intro;
	public Outro outro;
	public ExitMenu exitMenu;
	public CharMenu charMenu;
	public HelpInfo helpInfo;
	public PlayerHealthBar pHealthBar;
	public Long elapsedCache;
	private boolean initalized = false;
	private boolean playInitalized = false;
	public String playerName;
	public GameFont font;
	public GameFont font20;
	public GameFont font30;
	public GameTexts gameTexts;
	public GameSounds gameSounds;
	public QuestManager questMana;

	public void initResources() {
		super.hideCursor();
		gameStateMachine = new GameStateMachine(this);
	}
	
	public boolean init() {
		try {
			if (!initalized) {
				initalized = true;
				
				setFPS(FPS);
				elapsedCache = 0L;

				inputMana = new InputManager(this);
				gameTexts = new GameTexts(this);
				gameSounds = new GameSounds(this);
				intro = new Intro(this);
				outro = new Outro(this);
				exitMenu = new ExitMenu(this);
				charMenu = new CharMenu(this);
				helpInfo = new HelpInfo(this);
				loadFont();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			notifyExit();
		}
		return false;
	}
	
	private void loadFont() {
		Font font;
		Font andyFont;
		//TODO: fix font..
		String fName = "Andyb.ttf";
		try {
			andyFont = Font.createFont(Font.TRUETYPE_FONT, new File(FONTPATH + File.separator + fName));
			font = andyFont.deriveFont(26f);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(fName + " not loaded.  Using serif font.");
			font = new Font("sanserif", Font.PLAIN, 18);
			andyFont = font;
		}

		Font font20 = andyFont.deriveFont(20f);
		Font font30 = andyFont.deriveFont(50f);
		
		this.font = fontManager.getFont(font);
		this.font20 = fontManager.getFont(font);
		this.font30 = fontManager.getFont(font);
	}
	
	public boolean initPlay() {
		try {
			if (!playInitalized) {
				playInitalized = true;
				
				tileMana = new TileManager(this);
				itemMana = new ItemManager(this);
				npcMana = new NonPlayerCharacterManager(this);
				mapMana = new MapManager(this);
				
				//TODO: make player a part of current map... ??!
				player = new Player(this, mapMana.getCurrentMap().getPlayerStart(), playerName);
				
				// das einkommentieren, f�r ein startitem:
				/*Item item = itemMana.getItem(6);
				player.getInv().addToInventory(item, false);*/
				
				/*FollowingCharacter followingChar = new FollowingCharacter(
						this, 1, "opponent");
				player.setFollowingCharacter(followingChar);*/
				
				
				msgMana = new MessageManager(this);
				invMenu = new InventoryMenu(this);
				monsterMenu = new ChooseMonsterMenu(this);
				trans = new MonsterTransformation(this, playerName);
				fightMana = new FightManager(this);
				pHealthBar = new PlayerHealthBar(this);
				mapMana.getCurrentMap().updateVisible();
				
				questMana = new QuestManager(this);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			notifyExit();
		}
		return false;
	}

	public void update(long elapsedTime) {
		try {
			if (SHOWFPS) System.out.println("FPS: " + getCurrentFPS());

			gameStateMachine.update(elapsedTime);
			inputMana.update(elapsedTime);
		} catch (Exception e) {
			e.printStackTrace();
			notifyExit();
		}
	}

	public void render(Graphics2D g) {
		//TODO: kill this exception (do not load files in render!!!!)
		try {
			gameStateMachine.render(g);
		} catch (FolderContainsNoFilesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		GameLoader game = new GameLoader();
		game.setup(new EtothGame(), DIMENSION, FULLSCREEN);
		game.start();
		
	}
	
}