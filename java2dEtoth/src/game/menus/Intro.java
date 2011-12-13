package game.menus;

import game.EtothGame;
import game.math.Vector2d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import com.golden.gamedev.object.GameFont;

public class Intro {
	
	private EtothGame game;
	private BufferedImage img;
	private String text;

	public Intro(EtothGame game) {
		this.game = game;
		
		this.img = game.getImage(
				new File(game.IMGPATH + File.separator + "inventory" + 
						File.separator + "logoEND.gif")
				.getPath()
		);
		
		this.text = game.gameTexts.getText("continue");
	}

	public void render(Graphics2D g) {
		//clearRectangle
		g.fillRect(0, 0, game.getWidth(), game.getHeight());
		
		
		Vector2d pos = new Vector2d(85,90);
		g.drawImage(
				img, 
				(int) pos.getX(), 
				(int) pos.getY(), 
				null
		);
		
		//TODO: Machen das er \n versteht...?
		g.setColor(Color.WHITE);
		game.font.drawText(
				g, 
				text, 
				GameFont.LEFT,
				(int) (game.getWidth() / 2d - img.getWidth() / 2d + 170), 
				(int) (3 * game.getHeight() / 4d - img.getHeight() / 2d + 320 ), 
				(int) (game.getWidth() * 0.8d),
				0, 
				0
		);
		
	}

}
