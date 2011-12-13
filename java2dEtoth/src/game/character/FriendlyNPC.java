package game.character;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import game.EtothGame;
import game.exceptions.CanNotReadFileException;
import game.exceptions.FolderContainsNoFilesException;
import game.math.Vector2d;

public class FriendlyNPC extends NonPlayerCharacter {
	
	private boolean walks;
	private boolean follow;

	public FriendlyNPC(EtothGame game, int mapId, int type, Vector2d tilePos, 
			String imgPath, String msgImgPath) 
			throws FolderContainsNoFilesException, CanNotReadFileException, 
			SAXException, IOException, ParserConfigurationException {
		super(game, mapId, type, tilePos, imgPath, msgImgPath);
		this.follow = false;
	}
	
	public void setFollow(boolean follow) {
		this.follow = follow;
	}
	
	public void interactStop() {
		try {
			super.interactStop();
			if (follow) {
				System.out.println("follow!!!");
				FollowingCharacter followingChar;
				followingChar = this.getFollowingChar();
				game.player.setFollowingCharacter(followingChar, tilePos);
				game.mapMana.getCurrentMap().removeNpc(this);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public FriendlyNPC clone() {
		try {
			FriendlyNPC npc = new FriendlyNPC(game, mapId, type, tilePos, imgPath, 
					msgImgPath);
			npc.setWalks(walks);
			npc.setFollow(follow);
			return npc;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
