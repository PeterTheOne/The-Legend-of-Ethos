package game.menus;

import game.EtothGame;
import game.GameStateMachine.GameState;
import game.exceptions.FolderContainsNoFilesException;
import game.helper.IOHelper;
import game.math.Vector2d;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.golden.gamedev.object.AnimatedSprite;

public class PlayerHealthBar {

	private EtothGame game;
	private double pHealth;
	private double npcHealth;
	
	private BufferedImage bg;
	private BufferedImage heart;
	
	private Vector2d playerPos;
	private Vector2d npcPos;
	
	public PlayerHealthBar(EtothGame game) {
		this.game = game;
		this.pHealth = -1;
		this.npcHealth = -1;

		this.bg = game.gameImages.getImage(game.FIGHTIMG, "health_bg");
		this.heart = game.gameImages.getImage(game.FIGHTIMG, "heart");
		
		this.playerPos = new Vector2d(10, 10);
		this.npcPos = new Vector2d(	game.getWidth() - bg.getWidth(), 0)
				.add(-playerPos.getX(), playerPos.getY());
	}

	public void update(long elapsedTime) {
		if (game.gameStateMachine.getState() == GameState.PLAY) {
			this.pHealth = game.player.getHealth();
		} else if (game.gameStateMachine.getState() == GameState.FIGHT) {
			this.pHealth = game.fightMana.getPlayerSprite().getHealth();
			this.npcHealth = game.fightMana.getNPCSprite().getHealth();
		}
	}

	public void render(Graphics2D g) {
		renderHealthBar(g, playerPos, pHealth);
		
		if (game.gameStateMachine.getState() == GameState.FIGHT) {
			
			
			renderHealthBar(g, npcPos, npcHealth);
		}
	}
	
	private void renderHealthBar(Graphics2D g, Vector2d pos, double health) {
		g.drawImage(
				bg, 
				(int) pos.getX(), 
				(int) pos.getY(), 
				null
		);
		Vector2d heartPos = pos.add(new Vector2d(10, 2));
		for (int i = 0; i < health; i++) {
			g.drawImage(heart, (int) heartPos.getX(), (int) heartPos.getY(), null);
			heartPos.setX(heartPos.getX() + heart.getWidth() * 1.5);
		}
	}

}
