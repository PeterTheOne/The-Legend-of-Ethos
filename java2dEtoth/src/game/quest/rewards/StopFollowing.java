package game.quest.rewards;

import game.EtothGame;

public class StopFollowing extends QuestReward {

	public StopFollowing(EtothGame game) {
		super(game);
	}
	
	public void reward() throws Exception {
		if (rewarded) return;
		game.player.stopFollowing(true);
	}

}
