package game.quest.rewards;


import game.EtothGame;

public class ChangeDialogReward extends QuestReward {

	private String mapName;
	private int npcMapId;
	private String dialogPath;
	
	public ChangeDialogReward(EtothGame game, String mapName, int npcMapId, String dialogPath) {
		super(game);
		this.mapName = mapName;
		this.npcMapId = npcMapId;
		this.dialogPath = dialogPath;
		
	}
	
	public void reward() {
		if (rewarded) return;
		//TODO: find solution for exceptions..
		try {
			game.mapMana.getMap(mapName)
				.getNPC(npcMapId).setDialogPath(this.dialogPath);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rewarded = true;
	}

}
