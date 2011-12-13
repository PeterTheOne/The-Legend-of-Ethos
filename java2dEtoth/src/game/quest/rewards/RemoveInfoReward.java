package game.quest.rewards;

import game.EtothGame;
import game.math.Vector2d;

public class RemoveInfoReward extends QuestReward {

	private String mapName;
	private Vector2d infoPos;
	
	public RemoveInfoReward(EtothGame game, String mapName, Vector2d infoPos) {
		super(game);
		this.mapName = mapName;
		this.infoPos = infoPos;
	}
	
	public void reward() throws Exception {
		if (rewarded) return;
		game.mapMana.getMap(mapName).removeInfo(infoPos);
		rewarded = true;
	}

}
