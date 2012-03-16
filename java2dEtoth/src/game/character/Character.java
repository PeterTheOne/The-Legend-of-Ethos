package game.character;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import game.EtothGame;
import game.exceptions.FolderContainsNoFilesException;
import game.exceptions.NotFoundException;
import game.helper.DirectionHelper;
import game.helper.DirectionHelper.Direction;
import game.math.Vector2d;

public abstract class Character {

	protected EtothGame game;
	public static enum CharacterState {IDLE, WALK}
	protected CharacterState charState;
	protected Direction direction;
	protected Vector2d tilePos;			//Tilecoordinates (tiles 0 - 15/20)
	protected Vector2d realPos;			//Realcoordinates (px)
	protected Vector2d speed;
	protected CharacterSprite charSprite;
	protected String imgPath;
	protected double health;
	private static double TOL = 2;

	public Character(EtothGame game, Vector2d tilePos, String imgPath) 
			throws FolderContainsNoFilesException {
		this.game = game;
		this.tilePos = tilePos;
		this.realPos = new Vector2d(
				tilePos.getX() * game.TILESIZE, 
				tilePos.getY() * game.TILESIZE
		);
		speed = new Vector2d();
		direction = Direction.DOWN;
		charState = CharacterState.IDLE;
		this.imgPath = imgPath; 
		charSprite = loadSprite(imgPath);
		setAniSprDirection();
		health = 10;
	}
	
	public Character(EtothGame game, Vector2d tilePos, String imgPath, 
			String direction) 
			throws FolderContainsNoFilesException {
		this(game, tilePos, imgPath);
		setDirection(direction);
	}

	protected CharacterSprite loadSprite(String imgPath) 
			throws FolderContainsNoFilesException {
		String basePath = game.CHARIMG + "/" + imgPath;
		String direPaths[] = new String[4];
		direPaths[0] = "up";
		direPaths[1] = "down";
		direPaths[2] = "left";
		direPaths[3] = "right";
		int aniLengths[] = new int[direPaths.length];
		for (int i = 0; i < aniLengths.length; i++) {
			aniLengths[i] = game.gameImages.getNoOfFrames( basePath, direPaths[i] );
		}
		int countImagFiles = 0;
		for (int i = 0; i < direPaths.length; i++) {
			countImagFiles += aniLengths[i];
		}

		BufferedImage imageFiles[];
		BufferedImage images[] = new BufferedImage[countImagFiles];
		int j = 0;
		for (int i = 0; i < direPaths.length; i++) {
			imageFiles = game.gameImages.getImages( basePath, direPaths[i] );
			
			//System.out.println("imageFiles: " + imageFiles.length);
			for (int k = 0; k < aniLengths[i]; j++, k++) {
				images[j] = imageFiles[k];
				//System.out.println("l " + j + " " + k);
			}
		}
		
		//System.out.println("images: " + images.length + " anilengths " + aniLengths.length);
		CharacterSprite charSprite = new CharacterSprite(images, aniLengths);
		charSprite.getAnimationTimer().setDelay((long) (game.CHARANIMSPEED));
		return charSprite;
	}

	public void update(Long elapsedTime) throws Exception {
		if (charState == CharacterState.WALK) {
			realPos = realPos.add(
					speed.getX() * elapsedTime, 
					speed.getY() * elapsedTime
			);

			if (
					((direction == Direction.UP || direction == Direction.DOWN) && Math.abs(realPos.getY() - tilePos.getY() * game.TILESIZE) < TOL)  ||
					((direction == Direction.RIGHT || direction == Direction.LEFT) && Math.abs(realPos.getX() - tilePos.getX() * game.TILESIZE) < TOL)
			) {
				stop(false, false);
			} else if (
					(direction == Direction.UP && realPos.getY() + TOL / 2d <= tilePos.getY() * game.TILESIZE)  ||
					(direction == Direction.DOWN && realPos.getY() - TOL / 2d >= tilePos.getY() * game.TILESIZE)
			) {
				stop(true, false);
			} else if (
					(direction == Direction.RIGHT && realPos.getX() + TOL / 2d >= tilePos.getX() * game.TILESIZE) ||
					(direction == Direction.LEFT && realPos.getX() - TOL / 2d <= tilePos.getX() * game.TILESIZE)
			) {
				stop(false, true);
			}
		}
		updateSprPos();
		charSprite.update(elapsedTime);
		setAniSprDirection();
	}

	protected void stop(boolean resetTilePosX, boolean resetTilePosY) 
			throws Exception {
		speed.set(0, 0);
		//TODO: fix reset Bug..
		if (resetTilePosX) realPos.set(tilePos.getX() * game.TILESIZE, realPos.getY());
		if (resetTilePosY) realPos.set(realPos.getX(), tilePos.getY() * game.TILESIZE);
		charState = CharacterState.IDLE;
		//TODO: versch. obenfälchen, spielen wenn walk nicht wenn stop
		//game.gameSounds.playSound("walkGras");
	}

	protected void updateSprPos() {
		charSprite.setLocation(realPos.getX(), realPos.getY());
	}

	protected void setAniSprDirection() {
		if (direction == Direction.UP) {
			charSprite.setDirection(0);
		} else if (direction == Direction.DOWN) {
			charSprite.setDirection(1);
		} else if (direction == Direction.LEFT) {
			charSprite.setDirection(2);
		} else if (direction == Direction.RIGHT) {
			charSprite.setDirection(3);
		}
		if (charState == CharacterState.WALK) {
			charSprite.setAnimate(true);
		} else {
			charSprite.setAnimate(false);
		}
	}

	public void render(Graphics2D g) {
		charSprite.render(g);
	}

	public void move(double x, double y) throws NotFoundException {
		Vector2d newPos = tilePos.add(x, y);
		setDirection(DirectionHelper.getDirection(tilePos, newPos));
		if (!game.mapMana.getCurrentMap().isSolid(newPos, direction)) {
			tilePos = newPos;
			charState = CharacterState.WALK;
			speed = new Vector2d(x * game.CHARSPEED, y * game.CHARSPEED);
		}
	}

	public void setPos(Vector2d newPos) {
		tilePos = newPos;
		if (game.printCurrentPos) System.out.println("newPos: " + newPos);
		resetRealPos();
	}

	protected void resetRealPos() {
		realPos = tilePos.multi(game.TILESIZE);
		updateSprPos();
	}

	public Vector2d getTilePos() {
		return tilePos;
	}
	
	public double getHealth() {
		return health;
	}
	
	public void heal(int add) {
		if (add > 0) {
			this.health = this.health + add;
			this.health = (this.health > 10) ? 10 : this.health;
		}
	}

	public void setDirection(String direction) {
		if (direction.toLowerCase().equals("up")) {
			this.direction = Direction.UP;
		} else if (direction.toLowerCase().equals("down")) {
			this.direction = Direction.DOWN;
		} else if (direction.toLowerCase().equals("right")) {
			this.direction = Direction.RIGHT;
		} else if (direction.toLowerCase().equals("left")) {
			this.direction = Direction.LEFT;
		}
		setAniSprDirection();
	}
	
	public void setDirection(Direction dir) {
		this.direction = dir;
		setAniSprDirection();
	}
	
	public CharacterState getCharState() {
		return charState;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public String getImgPath() {
		return this.imgPath;
	}
}
