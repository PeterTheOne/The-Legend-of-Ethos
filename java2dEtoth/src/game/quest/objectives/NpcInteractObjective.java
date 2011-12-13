package game.quest.objectives;

import game.EtothGame;

public class NpcInteractObjective extends QuestObjective {

	private String mapName;
	private int npcMapId;
	
	public NpcInteractObjective(EtothGame game, String mapName, int npcMapId) {
		super(game);
		this.mapName = mapName;
		this.npcMapId = npcMapId;
	}
	
	public void update(long elapsedTime) {
		//empty
	}
	
	public void checkIsAchieved(int npcMapId) { //TODO: add attr mapName?
		if (this.mapName.equals(game.mapMana.getCurrentMap().getName()) &&
				this.npcMapId == npcMapId) {
			this.achieved = true;
		}
	}

}
