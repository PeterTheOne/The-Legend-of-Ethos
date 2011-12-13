package game.quest.rewards;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import game.EtothGame;
import game.character.EvilNPC;
import game.character.NonPlayerCharacter;
import game.exceptions.CanNotReadFileException;
import game.exceptions.NotFoundException;
import game.math.Vector2d;

public class CreateNpcReward extends QuestReward {

	private NonPlayerCharacter npc;
	private String mapName;
	
	public CreateNpcReward(EtothGame game, String mapName, 
			int type, Vector2d tilePos, String dialogPath,
			String direction, boolean attacks) throws 
			CanNotReadFileException, SAXException, 
			IOException, ParserConfigurationException, 
			NotFoundException {
		super(game);
		this.mapName = mapName;
		this.npc = game.npcMana.getNpc(type);
		this.npc.setPos(tilePos);
		this.npc.setDialogPath(dialogPath);
		this.npc.setDirection(direction);
		if (npc instanceof EvilNPC) {
			((EvilNPC) this.npc).setAttacks(attacks);
		}
	}
	
	public void reward() {
		if (rewarded) return;
		//TODO: find solution for exceptions..
		try {
			game.mapMana.getMap(mapName).addNPC(npc);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rewarded = true;
	}

}
