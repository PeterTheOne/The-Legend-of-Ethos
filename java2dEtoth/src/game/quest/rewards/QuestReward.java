package game.quest.rewards;

import game.EtothGame;

public abstract class QuestReward {
	
	protected EtothGame game;
	protected boolean rewarded;
	
	public QuestReward(EtothGame game) {
		this.game = game;
		this.rewarded = false; 
	}
	
	public abstract void reward() throws Exception;
}
