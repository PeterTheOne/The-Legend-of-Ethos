package game.menus;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import game.EtothGame;
import game.math.Vector2d;

public class HelpInfo {
	
	private BufferedImage img;

	public HelpInfo(EtothGame game) {
		img = game.getImage(
						game.fileToURL(game.IMGPATH + 
						File.separator + 
						"helpmenu" + 
						File.separator + 
						"help.png")
		);
	}
	
	public void render(Graphics2D g) {
		Vector2d pos = new Vector2d(85, 90);
		g.drawImage(img, (int) pos.getX(), (int) pos.getY(), null);
	}
	
}
