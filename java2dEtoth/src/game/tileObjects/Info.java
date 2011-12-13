package game.tileObjects;

import game.EtothGame;
import game.math.Vector2d;

import java.awt.image.BufferedImage;

public class Info extends TileObject {

	private boolean solid;
	private String info;
	private boolean light;
	
	public Info(EtothGame game, BufferedImage[] images, Vector2d tilePos, 
			boolean solid, String info, boolean light) {
		super(game, images, tilePos, -1); //TODO: type wtf?
		this.info = info;
		this.solid = solid;
		this.light = light;
	}
	
	public String getInfo() {
		return this.info;
	}
	
	public boolean getSolid() {
		return this.solid;
	}
	
	public boolean getLight() {
		return this.light;
	}
}
