package game.fight;

import game.EtothGame;
import game.exceptions.FolderContainsNoFilesException;
import game.helper.DirectionHelper.Direction;
import game.math.Vector2d;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.golden.gamedev.object.AnimatedSprite;

public abstract class FightSprite {
	
	protected EtothGame game;
	protected String name;
	private Vector2d originalPos;
	protected Vector2d pos;
	private Vector2d speed;
	protected Direction dir;
	protected AnimatedSprite spr;
	protected BufferedImage[] projImgs;
	protected String hitSound;
	protected String projectileSound;
	
	private int jumpCount;	
	private long animateCache;
	protected long shootCoolDown;
	private final int DEFAULTHEALTH = 10;
	private double health;

	protected int maxJumps;
	protected int damage;
	protected Vector2d jumpVector;
	protected int shootFreezeTime;
	protected double resistance;
	
	public FightSprite(EtothGame game, String name, BufferedImage[] images, 
			BufferedImage[] projImgs, Direction dir, String hitSound, 
			String projectileSound) {
		this.game = game;
		this.name = name;
		this.dir = dir;
		this.spr = new AnimatedSprite(images);
		this.projImgs = projImgs;
		this.hitSound = hitSound;
		this.projectileSound = projectileSound;
		
		int x = 0;
		if (dir == Direction.LEFT) {
			x = game.getWidth() - images[0].getWidth();
		}
		this.pos = new Vector2d(x, game.getHeight() - spr.getHeight() - 20);
		this.originalPos = this.pos;
		this.speed = new Vector2d();
		this.health = DEFAULTHEALTH;
		this.spr.getAnimationTimer().setDelay(150L);
		this.jumpCount = 0;
		this.animateCache = 0;
		this.shootCoolDown = 0;
		this.maxJumps = 2;
		this.damage = 1;
		this.jumpVector = new Vector2d(0, -120);
		this.shootFreezeTime = 500;
		this.resistance = 1d;
	}

	public void reset() {
		this.spr.setLoopAnim(false);
		this.jumpCount = 0;
		this.animateCache = 0;
		this.shootCoolDown = 0;
		this.pos = this.originalPos;
		this.health = DEFAULTHEALTH;
	}

	public void setHealth(double d) {
		this.health = d;
	}
	
	public void update(long elapsedTime) 
			throws FolderContainsNoFilesException {
		shootCoolDown += elapsedTime;
		
		speed = speed.add(new Vector2d(0, 10));
		pos = pos.add(speed.multi(elapsedTime * 0.01));
		if (pos.getY() > originalPos.getY()) {
			pos.setY(originalPos.getY());
			speed.setY(0);
		} else if (pos.getY() < 0) {
			pos.setY(0);
			speed.setY(0);
		}
		if (pos.getY() >= originalPos.getY()) {
			jumpCount = 0;
		}
		spr.setX(pos.getX());
		spr.setY(pos.getY());
		spr.update(elapsedTime);
		
		if (spr.isLoopAnim()) {
			animateCache += elapsedTime;
			if (animateCache > 300) {
				spr.setLoopAnim(false);
				animateCache = 0;
			}
		}
	}
	
	public void jump() {
		if (jumpCount < maxJumps 
				|| pos.getY() >= originalPos.getY()
				|| maxJumps == -1) {
			speed = jumpVector;
			jumpCount++;
		}
	}
	
	public void render(Graphics2D g) {
		spr.render(g);
	}

	public int getMidY() {
		return (int) (pos.getY() + spr.getHeight() / 2d);
	}

	public int getWidth() {
		return spr.getWidth();
	}

	public int getHeight() {
		return spr.getHeight();
	}
	
	public Vector2d getPos() {
		return pos;
	}

	public void hit(int damage) {
		game.gameSounds.playSound(hitSound);
		this.health -= damage / resistance;
		spr.setAnimate(true);
		spr.setLoopAnim(true);
		animateCache = 0;
	}

	public double getHealth() {
		return health;
	}
	
	public void shoot() {
		if (shootCoolDown > shootFreezeTime) {
			shootCoolDown = 0;
			
			Vector2d projStartPos;
			if (dir == Direction.RIGHT) {
				projStartPos = new Vector2d(
					0 + getWidth(),
					(int) (getMidY() - projImgs[0].getHeight() / 2d)
				);
			} else {
				projStartPos = new Vector2d(
					game.getWidth() - getWidth(),
					(int) (getMidY() - projImgs[0].getHeight() / 2d)
				);
			}
			
			FightProjectileSprite proj = new FightProjectileSprite(
				projImgs,
				projStartPos,
				dir,
				damage
			);
			
			game.gameSounds.playSound(projectileSound);
			game.fightMana.addProjectile(proj);
		}
	}

	public String getName() {
		return this.name;
	}
}
