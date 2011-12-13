package game.menus;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import com.golden.gamedev.object.AnimatedSprite;

import game.EtothGame;
import game.GameStateMachine.GameState;
import game.character.NonPlayerCharacter;
import game.exceptions.FolderContainsNoFilesException;
import game.fight.PlayerFightSprite;
import game.helper.IOHelper;
import game.math.Vector2d;

public class MonsterTransformation {

	private EtothGame game;
	private PlayerFightSprite playerFightSpr;
	private NonPlayerCharacter npc;

	private AnimatedSprite windMSpr;
	private AnimatedSprite windTSpr;
	private AnimatedSprite feuerMSpr;
	private AnimatedSprite feuerTSpr;
	private AnimatedSprite steinMSpr;
	private AnimatedSprite steinTSpr;
	private AnimatedSprite pflanzeMSpr;
	private AnimatedSprite pflanzeTSpr;
	
	private AnimatedSprite playerSpr;
	
	private BufferedImage cloudImg;
	private BufferedImage cloudImgScaled;
	private float cloudScale;
	private int cloudWidth;
	private int cloudHeight;
	
	private AnimatedSprite monsterSpr;
	private AnimatedSprite trankSpr;
	
	private Vector2d playerPos;
	private Vector2d cloudPos;
	private Vector2d monsterPos;
	private Vector2d trankPos;
	
	private static enum TransState{BEGINWAIT, TRANKMOVE, CLOUDSCALE, ENDWAIT};
	private TransState transState;
	
	private long elapsedTimeCache;

	private static final int BEGINWAITTIME = 1500;
	private static final int ENDWAITTIME = 1500;
	
	public MonsterTransformation(EtothGame game, String playerName) 
			throws FolderContainsNoFilesException {
		this.game = game;
		
		this.elapsedTimeCache = 0;
		
		File path;
		if (playerName.equals("player")) {
			path = new File(game.FIGHTCHANGEIMGPATH + File.separator + "char_player1.gif");
		} else {
			path = new File(game.FIGHTCHANGEIMGPATH + File.separator + "char_player_f.png");
		}
		playerSpr = new AnimatedSprite(IOHelper.getImages(game, path));
		
		path = new File(game.FIGHTCHANGEIMGPATH + File.separator + "cloud.png");
		cloudImg = game.getImage(path.getAbsolutePath());

		path = new File(game.FIGHTCHANGEIMGPATH + File.separator + "windmonster.png");
		windMSpr = new AnimatedSprite(IOHelper.getImages(game, path));
		path = new File(game.FIGHTCHANGEIMGPATH + File.separator + "zaubertrank_wind.png");
		windTSpr = new AnimatedSprite(IOHelper.getImages(game, path));
		path = new File(game.FIGHTCHANGEIMGPATH + File.separator + "feuermonster.png");
		feuerMSpr = new AnimatedSprite(IOHelper.getImages(game, path));
		path = new File(game.FIGHTCHANGEIMGPATH + File.separator + "zaubertrank_feuer.png");
		feuerTSpr = new AnimatedSprite(IOHelper.getImages(game, path));
		path = new File(game.FIGHTCHANGEIMGPATH + File.separator + "steinmonster.png");
		steinMSpr = new AnimatedSprite(IOHelper.getImages(game, path));
		path = new File(game.FIGHTCHANGEIMGPATH + File.separator + "zaubertrank_stein.png");
		steinTSpr = new AnimatedSprite(IOHelper.getImages(game, path));
		path = new File(game.FIGHTCHANGEIMGPATH + File.separator + "pflanze_200.gif");
		pflanzeMSpr = new AnimatedSprite(IOHelper.getImages(game, path));
		path = new File(game.FIGHTCHANGEIMGPATH + File.separator + "zaubertrank_pflanze.png");
		pflanzeTSpr = new AnimatedSprite(IOHelper.getImages(game, path));
		
	}

	public void setTrans(PlayerFightSprite playerFightSpr, 
			NonPlayerCharacter npc) throws FolderContainsNoFilesException {
		this.playerFightSpr = playerFightSpr;
		this.npc = npc;
		
		String fightName = playerFightSpr.getName();
		
		if (fightName.equals("wind")) {
			monsterSpr = windMSpr;
			trankSpr = windTSpr;
		} else if (fightName.equals("feuer")) {
			monsterSpr = feuerMSpr;
			trankSpr = feuerTSpr;
		} else if (fightName.equals("erd")) {
			monsterSpr = steinMSpr;
			trankSpr = steinTSpr;
		} else {
			monsterSpr = pflanzeMSpr;
			trankSpr = pflanzeTSpr;
		}

		cloudScale = 0.01f;
		calcCloudWH();
		
		playerPos = new Vector2d(
				(game.getWidth() / 2d) - (playerSpr.getWidth() / 2d), 
				(game.getHeight() / 2d) - (playerSpr.getHeight() / 2d)
		);
		cloudPos = new Vector2d(
				(game.getWidth() / 2d) - (cloudWidth / 2d), 
				(game.getHeight() / 2d) - (cloudHeight / 2d)
		);
		monsterPos = new Vector2d(
				(game.getWidth() / 2d) - (monsterSpr.getWidth() / 2d), 
				(game.getHeight() / 2d) - (monsterSpr.getHeight() / 2d)
		);
		trankPos = new Vector2d(
				game.getWidth(), 
				(game.getHeight() / 2d) - (trankSpr.getHeight() / 2d)
		);
		

		playerSpr.setX(playerPos.getX()); playerSpr.setY(playerPos.getY());
		monsterSpr.setX(monsterPos.getX()); monsterSpr.setY(monsterPos.getY());
		trankSpr.setX(trankPos.getX()); trankSpr.setY(trankPos.getY());
		
		transState = TransState.BEGINWAIT;
		
		game.gameStateMachine.switchState(GameState.TRANS);
	}
	
	private void calcCloudWH() {
		cloudWidth = (int) (cloudImg.getWidth() * cloudScale);
		cloudHeight = (int) (cloudImg.getHeight() * cloudScale);
		cloudWidth = cloudWidth <= 0 ? 1 : cloudWidth;
		cloudHeight = cloudHeight <= 0 ? 1 : cloudHeight;
	}

	public void update(long elapsedTime) throws FolderContainsNoFilesException {
		playerSpr.setX(playerPos.getX()); playerSpr.setY(playerPos.getY());
		monsterSpr.setX(monsterPos.getX()); monsterSpr.setY(monsterPos.getY());
		trankSpr.setX(trankPos.getX()); trankSpr.setY(trankPos.getY());
		
		playerSpr.update(elapsedTime);
		monsterSpr.update(elapsedTime);
		trankSpr.update(elapsedTime);
		
		if (transState == TransState.BEGINWAIT) {
			elapsedTimeCache += elapsedTime;
			if (elapsedTimeCache >= BEGINWAITTIME) {
				transState = TransState.TRANKMOVE;
				game.gameSounds.playSound("change");
				elapsedTimeCache = 0;
			}
		} else if (transState == TransState.TRANKMOVE) {
			trankPos = trankPos.add(new Vector2d(-15, 0));			
			if (trankPos.getX() < -trankSpr.getWidth()) {
				transState = TransState.CLOUDSCALE;
			}
		} else if (transState == TransState.CLOUDSCALE) {
			cloudScale *= 1.15f;	//TODO: elapsedTime verwenden!
			
			calcCloudWH();
			
			cloudPos = new Vector2d(
					(game.getWidth() / 2d) - (cloudWidth / 2d), 
					(game.getHeight() / 2d) - (cloudHeight / 2d)
			);
			
			cloudImgScaled = new BufferedImage(
					cloudWidth, 
					cloudHeight, 
					BufferedImage.TYPE_INT_RGB
			);
			Graphics2D graphics2D = cloudImgScaled.createGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			graphics2D.drawImage(cloudImg, 0, 0, cloudWidth, cloudHeight, null);
			
			
			if (cloudScale >= 3) {
				transState = TransState.ENDWAIT;
				elapsedTimeCache = 0;
			}
		} else if (transState == TransState.ENDWAIT) {
			elapsedTimeCache += elapsedTime;
			
			if (elapsedTimeCache >= ENDWAITTIME) {
				interact();
				elapsedTimeCache = 0;
			}
		}
	}

	public void render(Graphics2D g) {
		if (transState == TransState.BEGINWAIT) {
			playerSpr.render(g);
		} else if (transState == TransState.TRANKMOVE) {
			playerSpr.render(g);
			trankSpr.render(g);
		} else if (transState == TransState.CLOUDSCALE) {
			playerSpr.render(g);
			//TODO: bug: cload pops up in !first Fight..
			g.drawImage(cloudImgScaled, (int) cloudPos.getX(), 
					(int) cloudPos.getY(), null);
		} else if (transState == TransState.ENDWAIT) {
			monsterSpr.render(g);
		}
	}

	public void interact() throws FolderContainsNoFilesException {
		game.fightMana.setFight(playerFightSpr, npc);
	}

}
