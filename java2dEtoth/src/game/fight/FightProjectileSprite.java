package game.fight;

import game.helper.DirectionHelper.Direction;
import game.math.Vector2d;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.golden.gamedev.object.AnimatedSprite;

public class FightProjectileSprite {	
	
	private Vector2d pos;
	private Vector2d speed;
	private Direction dir;	//Is direction important?
	private AnimatedSprite spr;
	private int damage;
	
	private static final int SPEED = 30;
	
	public FightProjectileSprite (BufferedImage[] images, Vector2d pos, 
			Direction dir, int damage) {
		this.pos = pos;
		this.dir = dir;
		if (dir == Direction.RIGHT) {
			this.speed = new Vector2d(SPEED, 0);
		} else if (dir == Direction.LEFT) {
			this.speed = new Vector2d(- SPEED, 0);
		} else {
			//TODO: Exception!!
		}
		this.spr = new AnimatedSprite(images, pos.getX(), pos.getY());
		this.damage = damage;
	}
	
	public void update(long elapsedTime) {
		this.pos = pos.add(speed.multi(elapsedTime * 0.01));
		this.spr.setX(pos.getX());
		this.spr.setY(pos.getY());
		this.spr.update(elapsedTime);
	}
	
	public void render(Graphics2D g) {
		this.spr.render(g);
	}
	
	public Vector2d getPos() {
		return pos;
	}

	public Direction getDir() {
		return dir;
	}

	public double getHeight() {
		return spr.getHeight();
	}

	public double getWidth() {
		return spr.getWidth();
	}

	public Vector2d getMidPos() {
		return new Vector2d(spr.getCenterX(), spr.getCenterY()); 
	}

	public int getDamage() {
		return this.damage;
	}
}
