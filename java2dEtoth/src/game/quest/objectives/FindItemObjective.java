package game.quest.objectives;

import game.EtothGame;

public class FindItemObjective extends QuestObjective {
	
	private int itemType;

	public FindItemObjective(EtothGame game, int itemType) {
		super(game);
		this.itemType = itemType;
	}
	
	public void update(long elapsedTime) {
		//checkIfAchieved
		if (game.player.getInv().isTypeInInventory(itemType)) {
			this.achieved = true;
		}
	}

}
