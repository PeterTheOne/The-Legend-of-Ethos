package game.menus;

import game.EtothGame;
import game.GameStateMachine.GameState;
import game.exceptions.FolderContainsNoFilesException;
import game.math.Vector2d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import com.golden.gamedev.object.GameFont;

public class CharMenu {

	private EtothGame game;
	private boolean selected;

	private BufferedImage c1sImg;
	private BufferedImage c1nsImg;
	private BufferedImage c2sImg;
	private BufferedImage c2nsImg;

	private BufferedImage c1current;
	private BufferedImage c2current;
	
	private String text;
	private Boolean done;
	
	public CharMenu(EtothGame game) {
		this.game = game;
		File c1s = new File(game.CHARMENUIMGPATH + File.separator + 
				"char1_selected.png");
		File c1ns = new File(game.CHARMENUIMGPATH + File.separator + 
				"char1_notselected.png");
		File c2s = new File(game.CHARMENUIMGPATH + File.separator + 
				"char2_selected.png");
		File c2ns = new File(game.CHARMENUIMGPATH + File.separator + 
				"char2_notselected.png");
		this.c1sImg = game.getImage(c1s.getPath());
		this.c1nsImg = game.getImage(c1ns.getPath());
		this.c2sImg = game.getImage(c2s.getPath());
		this.c2nsImg = game.getImage(c2ns.getPath());
		this.selected = true;
		this.c1current = c1sImg;
		this.c2current = c2nsImg;
		
		this.text = game.gameTexts.getText("choosechar");
		this.done = false;
	}
	
	public void selectNext() {
		selected = !selected;
		if (selected) {
			c1current = c1sImg;
			c2current = c2nsImg;
		} else {
			c1current = c1nsImg;
			c2current = c2sImg;
		}
	}
	
	public void accept() throws FolderContainsNoFilesException {
		if (selected) {
			game.playerName = "player";
		} else {
			game.playerName = "player_f";
		}
		this.text = game.gameTexts.getText("loading");
		this.done = true;
	}
	
	public void update(long elapsedTime) {
		if (done) {
			game.gameStateMachine.switchState(GameState.PLAY);
		}
	}
	
	public void render(Graphics2D g) {
		g.fillRect(0, 0, game.getWidth(), game.getHeight());
		Vector2d pos1 = new Vector2d(50, 90);
		Vector2d pos2 = new Vector2d(
				game.getWidth() - pos1.getX() - c2nsImg.getWidth(), 
				pos1.getY());
		g.drawImage(c1current, (int) pos1.getX(), (int) pos1.getY(), null);
		g.drawImage(c2current, (int) pos2.getX(), (int) pos2.getY(), null);
		
		g.setColor(Color.WHITE);
		game.font.drawText(
				g, 
				text, 
				GameFont.LEFT,
				(int) 270, 
				(int) 540 , 
				(int) (game.getWidth() * 0.8d),
				0, 
				0
		);
		
	}
	

}
