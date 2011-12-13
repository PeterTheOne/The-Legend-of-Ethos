package game.quest.rewards;

import game.EtothGame;
import game.exceptions.FolderContainsNoFilesException;
import game.exceptions.NotFoundException;

public class GetItemReward extends QuestReward {

	private int itemType;
	
	public GetItemReward(EtothGame game, int itemType) {
		super(game);
		this.itemType = itemType;
	}
	
	public int getItemType() {
		return this.itemType;
	}
	
	public void reward() throws Exception {
		if (rewarded) return;
		game.player.getInv().addToInventory(
			game.itemMana.getItem(
				itemType
			), 
			false
		);
		rewarded = true;
	}
}
