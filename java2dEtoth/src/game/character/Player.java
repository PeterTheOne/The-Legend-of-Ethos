package game.character;

import java.awt.Graphics2D;

import game.EtothGame;
import game.Inventory;
import game.Map;
import game.exceptions.FolderContainsNoFilesException;
import game.exceptions.NotFoundException;
import game.helper.DirectionHelper;
import game.helper.DirectionHelper.Direction;
import game.math.Vector2d;
import game.tileObjects.Door;

public class Player extends Character {
	
	private String questMap;
	private Vector2d questPos;
	private Direction questDir;
	private Inventory inv;
	
	private Vector2d prevTilePos;
	private FollowingCharacter followingChar;

	public Player(EtothGame game, Vector2d tilePos, String imgPath) 
			throws FolderContainsNoFilesException {
		super(game, tilePos, imgPath);
		this.inv = new Inventory(game);
		this.questMap = game.STARTMAPFILENAME;
		this.questPos = tilePos;
		this.followingChar = null;
		this.prevTilePos = DirectionHelper.getPosInDirectionReverse(
				tilePos, direction);
	}

	protected void stop(boolean resetTilePosX, boolean resetTilePosY) 
			throws Exception {
		super.stop(resetTilePosX, resetTilePosY);
		inv.addToInventory(game.mapMana.getCurrentMap().getItem(tilePos), true);

		Door door = game.mapMana.getCurrentMap().getDoor(tilePos);
		goThroughDoor(door);
		
		Map currentMap = game.mapMana.getCurrentMap();		
		NonPlayerCharacter npc = currentMap.getNPCLookingAt(tilePos);
		if (npc != null) {
			npc.interact(game.player.getTilePos());
		}
		currentMap.updateVisible();
		
		if (followingChar != null) {
			//System.out.println("setTarget: tilePos: " + tilePos + ", prevTilePos: " + prevTilePos);
			followingChar.setTarget(prevTilePos, tilePos);
		}
		prevTilePos = tilePos;
	}
	
	private void goThroughDoor(Door door) throws Exception {
		if (door != null) {
			game.mapMana.changeMap(door.getTarget());
			prevTilePos = door.getTargetDoorPos(game);
			setPos(door.getTargetPos(game));
		}
	}
	
	public void setPos(Vector2d newPos) {
		super.setPos(newPos);
		if (followingChar != null) {
			followingChar.setTarget(prevTilePos, tilePos);
			followingChar.setPos(prevTilePos);
		}
	}

	public void setHealth(double d) {
		this.health = d;
	}

	public void setQuestStart() {
		this.questMap = game.mapMana.getCurrentMap().getName();
		this.questPos = this.tilePos;
		this.questDir = this.direction;
	}

	public void setToQuestStart() throws NotFoundException {
		game.mapMana.changeMap(this.questMap);
		setPos(questPos);
		this.direction = this.questDir;
	}
	
	public Inventory getInv() {
		return inv;
	}
	
	public void setFollowingCharacter(FollowingCharacter followingChar) {
		this.followingChar = followingChar;
		if (followingChar != null) {
			followingChar.setTarget(prevTilePos, tilePos);
			followingChar.setPos(prevTilePos);
		}
	}

	public void setFollowingCharacter(FollowingCharacter followingChar,
			Vector2d pos) {
		this.followingChar = followingChar;
		followingChar.setTarget(pos, tilePos);
		followingChar.setPos(pos);
	}
	
	public FollowingCharacter getFollowingCharacter() {
		return followingChar;
	}
	
	public void update(Long elapsedTime) throws Exception {
		super.update(elapsedTime);
		if (followingChar != null) {
			followingChar.update(elapsedTime);
		}
	}
	
	public void render(Graphics2D g) {
		if (followingChar != null) {
			followingChar.render(g);
		}
		super.render(g);
	}

	public void stopFollowing(boolean forever) {
		if (followingChar != null) {
			NonPlayerCharacter npc = followingChar.getNpc();
			if (forever && npc instanceof FriendlyNPC) {
				((FriendlyNPC) npc).setFollow(false);
			}
			game.mapMana.getCurrentMap().addNPC(npc);
			setFollowingCharacter(null);
		}
	}
}
