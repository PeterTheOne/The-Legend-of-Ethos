package game.quest.rewards;

import game.EtothGame;

public class RemoveItemReward extends QuestReward {

	private int itemType;
	
	public RemoveItemReward(EtothGame game, int itemType) {
		super(game);
		this.itemType = itemType;
	}
	
	public int getItemType() {
		return this.itemType;
	}
	
	public void reward() {
		if (rewarded) return;
		game.player.getInv().removeFromInventoryByType(itemType);
		rewarded = true;
	}

}
