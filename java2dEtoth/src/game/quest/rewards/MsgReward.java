package game.quest.rewards;

import java.io.File;

import game.EtothGame;

public class MsgReward extends QuestReward {

	private String text;
	
	public MsgReward(EtothGame game, String text) {
		super(game);
		this.text = text;
	}
	
	public void reward() throws Exception {
		if (rewarded) return;
		game.msgMana.setMsg(text);
		rewarded = true;
		game.gameSounds.playSound("info");
	}

}
