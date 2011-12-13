package game.tileObjects;

import game.EtothGame;
import game.fight.PlayerFightSprite;
import game.math.Vector2d;

import java.awt.image.BufferedImage;

public class MonsterItem extends Item {
	
	private PlayerFightSprite fightSpr;
	
	public MonsterItem(EtothGame game, BufferedImage[] images, 
			Vector2d tilePos, int type, String name, 
			String info, PlayerFightSprite fightSpr) {
		super(game, images, tilePos, type, name, info);
		this.fightSpr = fightSpr;
	}
	
	public MonsterItem(EtothGame game, BufferedImage[] images, 
			int type, String name, String info, 
			PlayerFightSprite fightSpr) {
		super(game, images, type, name, info);
		this.fightSpr = fightSpr;
	}
	
	public MonsterItem clone() {
		MonsterItem item = 
			new MonsterItem(game, spr.getImages(), getTilePos(), 
					getType(), name, info, fightSpr);
		return item;	
	}

	public PlayerFightSprite getPlayerFightSpr() {
		return this.fightSpr;
	}
}
