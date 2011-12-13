package game.quest.rewards;

import game.EtothGame;
import game.Map;
import game.tileObjects.Info;

public class CreateInfoReward extends QuestReward {
	
	private String mapName;
	private Info info;

	public CreateInfoReward(EtothGame game, String mapName, Info info) {
		super(game);
		this.mapName = mapName;
		this.info = info;
	}
	
	public void reward() throws Exception {
		if (rewarded) return;
		
		Map map = game.mapMana.getMap(mapName);
		map.addInfo(info);
		map.updateVisible();
		
		rewarded = true;
	}

}
