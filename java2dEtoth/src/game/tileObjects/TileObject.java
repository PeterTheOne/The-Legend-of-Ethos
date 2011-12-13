package game.tileObjects;

import game.EtothGame;
import game.Map;
import game.math.Vector2d;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.golden.gamedev.object.AnimatedSprite;

public abstract class TileObject {
	
	protected EtothGame game;
	protected AnimatedSprite spr;
	private Vector2d tilePos;
	private int type;
	protected boolean visible;
	
	public TileObject(EtothGame game, BufferedImage[] images, 
			Vector2d tilePos, int type) {
		this.game = game;
		this.tilePos = tilePos;
		spr = new AnimatedSprite(images, getPos().getX(), getPos().getY());
		if (images.length > 1) {
			spr.getAnimationTimer().setDelay(200);
			spr.setAnimate(true);
			spr.setLoopAnim(true);
		}
		this.type = type;
		visible =  !game.FOGOFWAR;;
	}
	
	public TileObject(EtothGame game, BufferedImage[] images, int type) {
		this.game = game;
		this.tilePos = new Vector2d();
		this.spr = new AnimatedSprite(images, getPos().getX(), getPos().getY());
		if (images.length > 1) {
			this.spr.getAnimationTimer().setDelay(200);
			this.spr.setAnimate(true);
			this.spr.setLoopAnim(true);
		}
		this.type = type;
	}
	
	public int getType() {
		return this.type;
	}
	
	public Vector2d getPos() {
		return getTilePos().multi(game.TILESIZE);
	}
	
	public Vector2d getTilePos() {
		return tilePos;
	}

	public void setTilePos(Vector2d tilePos) {
		this.tilePos = tilePos;
		this.spr.setX(getPos().getX());
		this.spr.setY(getPos().getY());
	}
	
	public String toString() {
		return "EtothSprite: [type=" + type + "]";
	}
	
	public void update(long elapsedTime) {
		this.spr.update(elapsedTime);
	}

	public void render(Graphics2D g) {
		if (visible) {
			this.spr.render(g);
		}
	}
	
	public AnimatedSprite getSprite() {
		return this.spr;
	}
	
	public void updateVisible(EtothGame game) {
		Map currentMap = game.mapMana.getCurrentMap();
		if (!currentMap.getCaveMode()) {
			if (!visible 
					&& game.player.getTilePos().getDistance(getTilePos()) 
					< game.VISIBLEDIS) {
				visible = true;
			}
		} else {
			if (game.player.getTilePos().getDistance(getTilePos()) 
					< game.VISIBLEDISCAVE) {
				visible = true;
			} else {
				visible = false;
			}
			ArrayList<Info> information = currentMap.getInformation();
			for (Info info : information) {
				if (info.isVisible()
						&& info.getLight()
						&& info.getTilePos().getDistance(getTilePos()) 
						< game.VISIBLEDISCAVE) {
					visible = true;
				}
			}
		}
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
