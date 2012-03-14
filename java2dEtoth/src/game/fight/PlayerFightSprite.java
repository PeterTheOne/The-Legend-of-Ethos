package game.fight;

import game.EtothGame;
import game.helper.DirectionHelper.Direction;
import game.math.Vector2d;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class PlayerFightSprite extends FightSprite {

	public PlayerFightSprite(EtothGame game, String name, 
			BufferedImage[] images, BufferedImage[] projImgs, Direction dir, 
			String hitSound, String projectileSound) {
		super(game, name, images, projImgs, dir, hitSound, projectileSound);
	}
	
	public void setAttributes(int maxJumps, int damage, 
			int jumpHeight, int shootFreezeTime, double resistance) {
		this.maxJumps = maxJumps;							//default: 2
		this.damage = damage;								//default: 1	
		this.jumpVector = new Vector2d(0, - jumpHeight);	//default: 120
		this.shootFreezeTime = shootFreezeTime; 			//default: 500
		this.resistance = resistance;						//default: 1d
	}
	
	public PlayerFightSprite clone() {
		PlayerFightSprite pFightSpr = 
			new PlayerFightSprite(game, name, spr.getImages(), projImgs, dir, 
					hitSound, projectileSound);
		pFightSpr.setAttributes(maxJumps, damage, (int)- jumpVector.getY(), 
				shootFreezeTime, resistance);
		return pFightSpr;
	}

	public void goRight() {
		//TODO: this is only for playerFightSpr but not for FIGHTSPR n shiat
		this.pos = pos.add(10, 0);
		if (pos.getX() > game.getWidth() / 2d - spr.getWidth()) {
			pos.setX(game.getWidth() / 2d - spr.getWidth());
		}
	}

	public void goLeft() {
		this.pos = pos.add(-10, 0);
		if (pos.getX() < 0) {
			pos.setX(0);
		}
	}
}
