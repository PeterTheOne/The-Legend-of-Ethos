package game.menus;

import game.EtothGame;
import game.GameStateMachine.GameState;
import game.math.Vector2d;
import game.tileObjects.Item;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import com.golden.gamedev.object.AnimatedSprite;
import com.golden.gamedev.object.GameFont;

public class InventoryMenu {

	private EtothGame game;
	private ArrayList<ArrayList<Item>> inventory;
	private int selected;
	private AnimatedSprite spr;
	private BufferedImage bg;
	private BufferedImage paper;
	
	private Vector2d bgPos;
	private Vector2d paperPos;
	private Vector2d textStartPos;
	private Vector2d descStartPos;
	private Vector2d itemImagePos;
	private Vector2d textSpacing;
	
	public InventoryMenu(EtothGame game) {
		this.game = game;
		inventory = new ArrayList<ArrayList<Item>>();
		selected = 0;
		spr = null;
		

		bg = game.getImage(
				new File(game.CHARMENUIMGPATH + File.separator + "menu_gross_transp.gif")
				.getPath()
		);

		paper = game.getImage(
				new File(game.INVENTORYIMGPATH + File.separator + "pergament.png")
				.getPath()
		);
		
		bgPos = new Vector2d(0, 0);
		paperPos = new Vector2d(70, 70).add(bgPos);
		textStartPos = new Vector2d(30, 70).add(paperPos);
		descStartPos = new Vector2d(300, 160).add(paperPos);
		itemImagePos = new Vector2d(450, 70).add(paperPos);
		textSpacing = new Vector2d(0, 30);
	}

	public void render(Graphics2D g) {
		//TODO: dont do all this in render!!!!!!!!!!!!!!!!!!!!!!!!!11111111
		
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
		inventory = game.player.getInv().getInventory();
		if (inventory.isEmpty()) {
			drawText(g, textStartPos, game.gameTexts.getText("invexit"));
		} else {
			Vector2d currentPos = textStartPos;
			for (ArrayList<Item> arrayListOfItems : inventory) {
				String text = arrayListOfItems.size() + "x " + arrayListOfItems.get(0).getName();
				drawText(g, currentPos, text);
				currentPos = currentPos.add(textSpacing);
			}
			String text = game.gameTexts.getText("invexit");
			drawText(g, currentPos, text);
		}
		String desText;
		spr = null;
		if (inventory.isEmpty()) {
			desText = game.gameTexts.getText("invemptyinfo");
		} else if (selected == inventory.size()) {
			desText = game.gameTexts.getText("invexitinfo");
		} else {
			Item item = inventory.get(selected).get(0);
			desText = item.getInfo();
			spr = item.getSprite();
			spr.setX(itemImagePos.getX()+10);
			spr.setY(itemImagePos.getY());
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
		String text = game.gameTexts.getText("invhead");
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
		int size = game.player.getInv().getSize() + 1;
		if (size > 1) {
			this.selected = (selected + 1) % size;
		}
	}
	
	public void selectPrev() {
		int size = game.player.getInv().getSize() + 1;
		if (size > 1) {
			this.selected = (selected - 1 + size) % size;
		}
	}

	public void interact() {
		int size = game.player.getInv().getSize();
		if (size == 0 || selected == size) {
			leaveMenu();
			return;
		} else {
			if (activateItem(selected)) {
				leaveMenu();
			}
		}
	}
	
	public void leaveMenu() {
		selected = 0;
		game.gameStateMachine.switchStateToPrev();
	}

	private boolean activateItem(int selected) {
		switch(inventory.get(selected).get(0).getType()) {
		case 0:
			if (game.player.getHealth() >= 10) {
				break;
			}
			File walkSound = new File(game.SOUNDPATH + File.separator + "heilung.wav");
			game.bsSound.play(walkSound.getAbsolutePath());
			game.player.heal(10);
			if (game.gameStateMachine.getPrevState() == GameState.FIGHT) {
				game.fightMana.reloadHealth();
			}
			game.player.getInv().removeFromInventory(selected);
			return true;
		case 1:
			//TODO: msg, omg wie mach ich das? wtf :P
			break;
		case 2:
			//TODO:msg
			break;
		case 3:
			//TODO:msg
			break;
		}
		return false;
	}

	public void update(long elapsedTime) {
		if (spr != null) {
			spr.update(elapsedTime);
		}
	}
}
