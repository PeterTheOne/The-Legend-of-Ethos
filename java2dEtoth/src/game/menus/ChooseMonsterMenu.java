package game.menus;

import game.EtothGame;
import game.GameStateMachine.GameState;
import game.character.NonPlayerCharacter;
import game.exceptions.FolderContainsNoFilesException;
import game.fight.PlayerFightSprite;
import game.helper.IOHelper;
import game.math.Vector2d;
import game.tileObjects.Item;
import game.tileObjects.MonsterItem;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.golden.gamedev.object.AnimatedSprite;
import com.golden.gamedev.object.GameFont;

public class ChooseMonsterMenu {

	private EtothGame game;
	private ArrayList<ArrayList<Item>> inventory;
	private int selected;
	private AnimatedSprite spr;
	
	private NonPlayerCharacter npc;
	
	private Vector2d bgPos;
	private Vector2d paperPos;
	private Vector2d textStartPos;
	private Vector2d descStartPos;
	private Vector2d itemImagePos;
	private Vector2d textSpacing;
	
	private BufferedImage bg;
	private BufferedImage paper;
	
	public ChooseMonsterMenu(EtothGame game) {
		this.game = game;
		this.inventory = new ArrayList<ArrayList<Item>>();
		this.selected = 0;
		this.spr = null;
		
		this.bgPos = new Vector2d(0, 0);
		this.paperPos = new Vector2d(70, 70).add(bgPos);
		this.textStartPos = new Vector2d(30, 70).add(paperPos);
		this.descStartPos = new Vector2d(300, 160).add(paperPos);
		this.itemImagePos = new Vector2d(450, 70).add(paperPos);
		this.textSpacing = new Vector2d(0, 30);
		
		this.bg = game.gameImages.getImage(game.CHARMENUIMG, "menu_bg");
		this.paper = game.gameImages.getImage(game.INVENTORYIMG, "pergament_bg");
	}

	public void update(long elapsedTime) {
		if (spr != null) {
			spr.update(elapsedTime);
		}
	}

	public void render(Graphics2D g) throws FolderContainsNoFilesException {
		//TODO: dont do all this in render!!!!!!!!!!!!!!!!!!!!!!!!!11111111
		// do more stuff in update..
		
		g.drawImage(bg, (int) bgPos.getX(), (int) bgPos.getY(), null);
		g.drawImage(paper, (int) paperPos.getX(), (int) paperPos.getY(), null);
		{
			Vector2d currentPos = textStartPos;
			for (int i = 0; i < selected; i++) {
				currentPos = currentPos.add(textSpacing);
			}
			g.setColor(new Color(0xD2,0xB4,0x8C));
			g.fillRect((int) currentPos.getX(), (int) currentPos.getY(), 230, 25);
		}

		g.setColor(new Color(0x33,0x1A,0x00));
		this.inventory = game.player.getInv().getMonsterInventory();
		if (inventory.isEmpty()) {
			drawText(g, textStartPos, game.gameTexts.getText("choosemonsterexit"));
		} else {
			Vector2d currentPos = textStartPos;
			for (ArrayList<Item> arrayListOfItems : inventory) {
				String text = arrayListOfItems.size() + "x " + arrayListOfItems.get(0).getName();
				drawText(g, currentPos, text);
				currentPos = currentPos.add(textSpacing);
			}
		}
		String desText;
		spr = null;
		if (inventory.isEmpty()) {
			desText = game.gameTexts.getText("choosemonsternomonster");
		} else if (selected == inventory.size()) {
			desText = game.gameTexts.getText("choosemonsterexitinfo");
		} else {
			Item item = inventory.get(selected).get(0);
			desText = item.getInfo();
			String path = "";
			switch (item.getType()) {
			//TODO: change this to xml...
			case 4: 
				path = "windmonster_menu";
				break;
			case 5: 
				path = "feuermonster_menu";
				break;
			case 6: 
				path = "steinmonster_menu";
				break;
			case 8:
				path = "pflanzenmonster_menu";
				break;
			}
			try {
				spr = new AnimatedSprite(
						IOHelper.getImages(
							game, 
							new URL(
								game.getResourceURL(game.FIGHTIMGPATH) + 
								"/" + "change" + "/" + 
								path
							)
						)
				);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			spr.setX(itemImagePos.getX() - 80 + 10);
			spr.setY(itemImagePos.getY() - 70);
			spr.render(g);
		}
		g.setColor(new Color(0x33,0x1A,0x00));
		game.font.drawText(
				g, 
				desText, 
				GameFont.LEFT,
				(int) descStartPos.getX(), 
				(int) descStartPos.getY(), 
				320,
				10, 
				0
		);
		String text = game.gameTexts.getText("choosemonsterhead");
		g.setColor(Color.WHITE);
		game.font30.drawText(
				g, 
				text, 
				GameFont.LEFT,
				(int) 340, 
				(int) 35 , 
				(int) (game.getWidth() * 0.8d),
				0, 
				0
	);
		//TODO: Machen das er \n versteht...?
	}
	
	private void drawText(Graphics2D g, Vector2d pos, String text) {
		game.font.drawText(
				g, 
				text, 
				GameFont.LEFT,
				(int) pos.getX(), 
				(int) pos.getY(), 
				(int) game.getWidth(),
				0, 
				0
		);
	}
	
	public void selectNext() {
		int size = game.player.getInv().getMonsterCount();
		if (size > 1) {
			this.selected = (selected + 1) % size;
		}
	}
	
	public void selectPrev() {
		int size = game.player.getInv().getMonsterCount();
		if (size > 1) {
			this.selected = (selected - 1 + size) % size;
		}
	}

	public void interact() throws FolderContainsNoFilesException {
		int size = game.player.getInv().getMonsterCount();
		if (size == 0) {
			selected = 0;
			game.gameStateMachine.switchState(GameState.PLAY);
			return;
		} else {
			PlayerFightSprite fightSpr = activateItem(selected);
			if (fightSpr != null) {
				selected = 0;
				if (npc == null) {
					//TODO: error/exception
				}
				game.trans.setTrans(fightSpr, npc);
			}
		}
	}

	private PlayerFightSprite activateItem(int selected) 
			throws FolderContainsNoFilesException {
		return ((MonsterItem) inventory.get(selected).get(0)).getPlayerFightSpr();
	}

	public void setNpc(NonPlayerCharacter npc) {
		this.npc = npc;
	}
}
