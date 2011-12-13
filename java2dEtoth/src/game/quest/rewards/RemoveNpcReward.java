package game.quest.rewards;

import game.EtothGame;
import game.Map;
import game.exceptions.NotFoundException;

public class RemoveNpcReward extends QuestReward {

	private String mapName;
	private int npcMapId;
	
	public RemoveNpcReward(EtothGame game, String mapName, int npcMapId) {
		super(game);
		this.mapName = mapName;
		this.npcMapId = npcMapId;
	}

	public void reward() {
		if (rewarded) return;
		//TODO: find solution for exceptions..
		Map map;
		try {
			map = game.mapMana.getMap(mapName);
			map.removeNpc(map.getNPC(npcMapId));
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rewarded = true;
	}
}
