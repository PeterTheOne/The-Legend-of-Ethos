package game.quest.objectives;

import game.EtothGame;
import game.character.FollowingCharacter;

public class HaveFollowing extends QuestObjective {

	private String mapName;
	private int npcType;
	
	public HaveFollowing(EtothGame game, String mapName, int npcType) {
		super(game);
		this.mapName = mapName;
		this.npcType = npcType;
	}
	
	public void update(long elapsedTime) {
		//checkIfAchieved
		FollowingCharacter followingChar = game.player.getFollowingCharacter();
		if (followingChar != null 
				&& game.mapMana.getCurrentMap().getName().equals(mapName)
				&& followingChar.getType() == npcType) {
			this.achieved = true;
		}
	}

}
