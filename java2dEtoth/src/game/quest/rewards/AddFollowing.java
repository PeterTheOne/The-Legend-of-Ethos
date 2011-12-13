package game.quest.rewards;

import game.EtothGame;
import game.character.FollowingCharacter;
import game.character.NonPlayerCharacter;
import game.helper.DirectionHelper;
import game.math.Vector2d;

public class AddFollowing extends QuestReward {

	private String mapName;
	private int npcMapId;
	
	public AddFollowing(EtothGame game, String mapName, int npcMapId) {
		super(game);
		this.mapName = mapName;
		this.npcMapId = npcMapId;
	}
	
	public void reward() throws Exception {
		if (rewarded) return;
		NonPlayerCharacter npc = game.mapMana.getMap(mapName).getNPC(npcMapId);
		FollowingCharacter followingChar = npc.getFollowingChar();
		Vector2d pos = npc.getTilePos();
		game.player.setFollowingCharacter(followingChar, pos);
		game.mapMana.getMap(mapName).removeNpc(npc);
	}

}
