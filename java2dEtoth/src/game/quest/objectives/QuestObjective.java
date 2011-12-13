package game.quest.objectives;

import game.EtothGame;

public abstract class QuestObjective {

	protected EtothGame game;
	protected boolean achieved;
	
	public  QuestObjective(EtothGame game) {
		this.game = game;
		this.achieved = false;
	}
	
	public abstract void update(long elapsedTime); //checkIfAchieved
	
	public boolean isAchieved() {
		return this.achieved;
	}
}
