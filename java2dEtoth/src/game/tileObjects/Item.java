
package game.tileObjects;

import game.EtothGame;
import game.math.Vector2d;

import java.awt.image.BufferedImage;

public class Item extends TileObject {
	
	protected String name;
	protected String info;
	
	public Item(EtothGame game, BufferedImage[] images, Vector2d tilePos, int type, String name, String info) {
		super(game, images, tilePos, type);
		this.name = name;
		this.info = info;
	}
	
	public Item(EtothGame game, BufferedImage[] images, int type, String name, String info) {
		super(game, images, type);
		this.name = name;
		this.info = info;
	}

	public Item clone() {
		Item item =  new Item(game, spr.getImages(), getTilePos(), getType(), name, info);
		item.setVisible(visible);
		return item;	
	}
	
	public String toString() {
		return name + ": " + info;
	}

	public String getName() {
		return name;
	}

	public String getInfo() {
		return info;
	}
}
