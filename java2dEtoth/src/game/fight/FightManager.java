package game.fight;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.golden.gamedev.object.Sprite;

import game.EtothGame;
import game.GameStateMachine.GameState;
import game.character.EvilNPC;
import game.character.FollowingCharacter;
import game.character.NonPlayerCharacter;
import game.exceptions.FolderContainsNoFilesException;
import game.exceptions.NotFoundException;
import game.helper.DirectionHelper.Direction;

public class FightManager {

	private EtothGame game;
	private boolean active;
	private Sprite bgSpr;

	private NonPlayerCharacter npc;
	private NpcFightSprite npcSprite;
	private PlayerFightSprite playerSprite;
	
	private ArrayList<FightProjectileSprite> projectiles;
	private ArrayList<FightProjectileSprite> projectilesToDelete;

	final int COLLISIONWIDTH = 60;

	public FightManager(EtothGame game) {
		this.game = game;
		bgSpr = new Sprite(
			game.gameImages.getImage(game.FIGHTIMG, "fight_bg")
		);
		active = false;
		projectiles = new ArrayList<FightProjectileSprite>();
		projectilesToDelete = new ArrayList<FightProjectileSprite>();
		
		playerSprite = null;
		npcSprite = null;
	}

	public void setFight(PlayerFightSprite playerSprite, NonPlayerCharacter npc) 
			throws FolderContainsNoFilesException {
		this.npc = npc;
		this.playerSprite = playerSprite; 
		this.npcSprite = ((EvilNPC) npc).getFightSpr();
		this.playerSprite.reset();
		this.npcSprite.reset();
		this.playerSprite.setHealth(game.player.getHealth());
		this.npcSprite.setHealth(npc.getHealth());
		
		active = true;
		game.gameStateMachine.switchState(GameState.FIGHT);
		bgSpr = game.mapMana.getCurrentMap().getFightBgSpr();
	}

	public PlayerFightSprite getPlayerSprite() {
		return playerSprite;
	}

	public NpcFightSprite getNPCSprite() {
		return npcSprite;
	}
	
	public void addProjectile(FightProjectileSprite proj) {
		projectiles.add(
			proj
		);
	}

	public void update(long elapsedTime) throws Exception {
		if (!active && game.gameStateMachine.getState() == GameState.FIGHT) {
			game.gameStateMachine.switchState(GameState.PLAY);
		} else if (active && game.gameStateMachine.getState() == GameState.FIGHT) {			
			bgSpr.update(elapsedTime);
			playerSprite.update(elapsedTime);
			npcSprite.update(elapsedTime);
			
			//TODO: Diese Collisionsabfrage in eine eigene Klasse packen..
			for (FightProjectileSprite projectile : projectiles) {
				projectile.update(elapsedTime);
				if (projectile.getDir() == Direction.RIGHT) { //projectile RIGHT
					if (
						projectile.getPos().getX() < game.getWidth() - projectile.getWidth() - npcSprite.getWidth() + COLLISIONWIDTH
						&& projectile.getPos().getX() > game.getWidth() - projectile.getWidth()  - npcSprite.getWidth()
						&& projectile.getMidPos().getY() > npcSprite.getPos().getY()
						&& projectile.getMidPos().getY() < npcSprite.getPos().getY() + npcSprite.getHeight()
					) { //HIT
						npcSprite.hit(projectile.getDamage());
						projectilesToDelete.add(projectile);
					} else if (
						projectile.getPos().getX() > game.getWidth()
					) {
						projectilesToDelete.add(projectile);
					}
				} else { //projectile LEFT
					if (
						projectile.getPos().getX() > playerSprite.getPos().getX() + playerSprite.getWidth() - COLLISIONWIDTH
						&& projectile.getPos().getX() < playerSprite.getPos().getX() + playerSprite.getWidth()
						&& projectile.getMidPos().getY() > playerSprite.getPos().getY()
						&& projectile.getMidPos().getY() < playerSprite.getPos().getY() + playerSprite.getHeight()
					) { //HIT
						playerSprite.hit(projectile.getDamage());
						projectilesToDelete.add(projectile);
					} else if (
						projectile.getPos().getX() < 0 - projectile.getWidth()
					) {
						projectilesToDelete.add(projectile);
					}
				}
			}
			for (FightProjectileSprite projectile : projectilesToDelete) {
				projectiles.remove(projectile);
			}
			projectilesToDelete = new ArrayList<FightProjectileSprite>();
			

			if (npcSprite.getHealth() <= 0) {
				winFight();
				return;
			}
			if (playerSprite.getHealth() <= 0) {
				looseFight();
				return;
			}
		}
	}

	private void winFight() throws Exception {
		game.mapMana.getCurrentMap().removeNpc(npc);
		game.player.setHealth(playerSprite.getHealth());
		clean();
		game.msgMana.setMsg(game.gameTexts.getText("fightwin"));
		game.questMana.updateNpcWin(npc.getMapId());
	}

	private void looseFight() throws FolderContainsNoFilesException, 
			NotFoundException {
		clean();
		game.msgMana.setMsg(game.gameTexts.getText("fightloose"));
		game.player.setHealth(10);
		game.player.stopFollowing(false);
		game.player.setToQuestStart();
	}
	
	private void clean() {
		active = false;
		playerSprite = null;
		npcSprite = null;
		projectiles.clear();
		projectilesToDelete.clear();
		game.gameStateMachine.switchState(GameState.PLAY);
	}

	public void render(Graphics2D g) {
		bgSpr.render(g);
		for (FightProjectileSprite projectile : projectiles) {
			projectile.render(g);
		}
		playerSprite.render(g);
		npcSprite.render(g);
		
		//this is for fight-collision debug:
		/*g.drawRect(
				(int) playerSprite.getPos().getX() + playerSprite.getWidth() - COLLISIONWIDTH, 
				(int) playerSprite.getPos().getY(), 
				COLLISIONWIDTH, 
				playerSprite.getHeight()
		);
		g.drawRect(
				(int) game.getWidth() - npcSprite.getWidth(), 
				(int) npcSprite.getPos().getY(), 
				COLLISIONWIDTH, 
				npcSprite.getHeight()
		);*/
	}

	public ArrayList<FightProjectileSprite> getProjectiles() {
		return this.projectiles;
	}

	public void reloadHealth() {
		this.playerSprite.setHealth(game.player.getHealth());
	}

}
