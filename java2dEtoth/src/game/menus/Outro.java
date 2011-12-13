package game.menus;

import game.EtothGame;
import game.math.Vector2d;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import com.golden.gamedev.object.GameFont;

public class Outro {
	
	private EtothGame game;
	private BufferedImage bgImg;
	private Vector2d pos;
	private String text;

	public Outro(EtothGame game) {
		this.game = game;
		this.bgImg = game.getImage(
				new File(
						game.IMGPATH + 
						File.separator + 
						"inventory" + 
						File.separator + 
						"pergament.png"
				)
				.getPath()
		);
		this.pos = new Vector2d(75,90);
		this.text = game.gameTexts.getText("finished");
	}

	public void render(Graphics2D g) {
		//clearRectangle
		g.fillRect(0, 0, game.getWidth(), game.getHeight());
		g.drawImage(
				this.bgImg, 
				(int) pos.getX(), 
				(int) pos.getY(), 
				null
		);

		//TODO: Machen das er \n versteht...?
		g.setColor(Color.BLACK);
		game.font.drawText(
				g, 
				text, 
				GameFont.LEFT,
				(int) 135, 
				(int) 200 , 
				(int) 550,
				0, 
				0
		);
		
	}

}
