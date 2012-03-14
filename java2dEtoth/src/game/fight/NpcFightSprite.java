package game.fight;

import game.EtothGame;
import game.exceptions.FolderContainsNoFilesException;
import game.helper.DirectionHelper.Direction;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class NpcFightSprite extends FightSprite {
	
	private long kiCache;
	
	private boolean enableUltraAI;
	private int shootFreq;
	private int jumpFreq;

	public NpcFightSprite(EtothGame game, String name, BufferedImage[] images, 
			BufferedImage[] projImgs, Direction dir, String  hitSound, 
			String projectileSound) {
		super(game, name, images, projImgs, dir, hitSound, projectileSound);
		this.kiCache = 0;
		
		this.maxJumps = 2;
		this.damage = 1;
		this.enableUltraAI = false;
		this.shootFreq = 3;	
		this.jumpFreq = 30;
	}
	
	public void setAttributes(int maxJumps, int damage, 
			boolean enableUltraAI, int shootFreq, int jumpFreq) {
		this.maxJumps = maxJumps;				//default: 2
		this.damage = damage;					//default: 1
		this.enableUltraAI = enableUltraAI;		//default: false
		this.shootFreq = shootFreq;				//default: 3
		this.jumpFreq = jumpFreq;				//default: 30
	}
	
	public void reset() {
		super.reset();
		this.kiCache = 0;
	}
	
	
	public void update(long elapsedTime) throws FolderContainsNoFilesException {
		super.update(elapsedTime);
		AI(elapsedTime);
	}
	
	private void AI(long elapsedTime) throws FolderContainsNoFilesException {
		kiCache += elapsedTime;
		
		if(enableUltraAI) {
			if (kiCache > 200) {
				ArrayList<FightProjectileSprite> projectiles = 
						game.fightMana.getProjectiles();
				for (FightProjectileSprite projectile : projectiles) {
					//TODO: re-test / optimize
					if (
						projectile.getDir() == Direction.RIGHT &&
						projectile.getPos().getX() + projectile.getWidth() < 
						pos.getX() + game.fightMana.COLLISIONWIDTH &&
						pos.getX() <= 
						50 + projectile.getWidth() + projectile.getPos().getX() &&
						getMidY() - projectile.getMidPos().getY() > - 200 &&
						getMidY() - projectile.getMidPos().getY() < getHeight() / 2d
					) {
						jump();
						kiCache = 0;
					}
				}
			}
		} else {
			int rand = (int) Math.round(Math.random() * jumpFreq);
			if (rand == 1) {
				jump();
			}
		}
		if (shootCoolDown > shootFreezeTime) {
			int rand = (int) Math.round(Math.random() * shootFreq);
			if (rand == 1) {
				shoot();
			}
		}
	}
	
	public NpcFightSprite clone() {
		NpcFightSprite npcFightSpr = new NpcFightSprite(game, name, 
				spr.getImages(), projImgs, dir, hitSound, projectileSound);
		npcFightSpr.setAttributes(maxJumps, damage, enableUltraAI, shootFreq, 
				jumpFreq);
		return npcFightSpr;
	}
}
