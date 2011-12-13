package game.character;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import game.EtothGame;
import game.exceptions.CanNotReadFileException;
import game.exceptions.FolderContainsNoFilesException;
import game.fight.NpcFightSprite;
import game.math.Vector2d;

public class EvilNPC extends NonPlayerCharacter {

	private NpcFightSprite fightSpr;
	
	public EvilNPC(EtothGame game, int mapId, int type, Vector2d tilePos, 
			String imgPath, String msgImgPath, NpcFightSprite fightSpr) 
			throws FolderContainsNoFilesException, CanNotReadFileException, 
			SAXException, IOException, ParserConfigurationException {
		super(game, mapId, type, tilePos, imgPath, msgImgPath);
		this.fightSpr = fightSpr;
	}
	
	public void interact(Vector2d playerPos) throws Exception {
		game.msgMana.setFightCallback(this);
		super.interact(playerPos);
	}

	public NpcFightSprite getFightSpr() {
		return this.fightSpr;
	}
	
	public EvilNPC clone() {
		//TODO: exception handling..
		try {
			NpcFightSprite newFightSprite = null;
			newFightSprite = fightSpr.clone();
			return new EvilNPC(game, mapId, type, tilePos, imgPath, 
					msgImgPath, newFightSprite);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
}
