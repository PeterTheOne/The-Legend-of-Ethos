package game.quest;

import game.EtothGame;
import game.quest.objectives.NpcInteractObjective;
import game.quest.objectives.NpcWinObjective;
import game.quest.objectives.QuestObjective;
import game.quest.rewards.QuestReward;

import java.util.ArrayList;

public class Quest {
	private QuestObjective startObjective;
	private ArrayList<QuestReward> startRewards;
	private ArrayList<QuestObjective> objectives;
	private ArrayList<QuestReward> beforeEndRewards;
	private QuestObjective endObjective;
	private ArrayList<QuestReward> rewards;
	private boolean finished;
	private boolean active;
	private boolean allAchieved; //"objectives" are achieved

	private EtothGame game;
	private String startText;
	private String endText;
	//TODO: Remove start and end text?
	
	public Quest(EtothGame game, String startText, String endText, 
			QuestObjective startObjective, QuestObjective endObjective) {
		this.game = game;
		this.startText = startText;
		this.endText = endText;
		this.startObjective = startObjective;
		this.endObjective = endObjective;
		
		startRewards = new ArrayList<QuestReward>();
		beforeEndRewards = new ArrayList<QuestReward>();
		objectives = new ArrayList<QuestObjective>();
		rewards = new ArrayList<QuestReward>();
		finished = false;
		active = false;
		allAchieved = false;
	}
	
	public void addObjective(QuestObjective objective) {
		objectives.add(objective);
	}
	
	public void addReward(QuestReward reward) {
		rewards.add(reward);
	}
	
	public void addStartRewards(QuestReward reward) {
		startRewards.add(reward);
	}
	
	public void addBeforeEndRewards(QuestReward reward) {
		beforeEndRewards.add(reward);
	}
	
	public void setActive() throws Exception {
		this.active = true;
		if (!startText.equals("")) {
			game.msgMana.setMsg(startText);
			game.gameSounds.playSound("info");
		}
		for (QuestReward reward : startRewards) {
			reward.reward();
		}
		game.player.setQuestStart();
	}
	
	public boolean getActive() {
		return active;
	}
	
	public void update(long elapsedTime) throws Exception {
		if (!finished) {
			if (active) {
				for (QuestObjective objective : objectives) {
					objective.update(elapsedTime);
				}
				allAchieved = true;
				for (QuestObjective objective : objectives) {
					if (!objective.isAchieved()) {
						allAchieved = false;
						break;
					}
				}
				if (allAchieved) {
					for (QuestReward beforeEndReward : beforeEndRewards) {
						beforeEndReward.reward();
					}
					endObjective.update(elapsedTime);
					if (endObjective.isAchieved()) {
						finished = true;
						if (!endText.equals("")) {
							game.msgMana.setMsg(endText);
							game.gameSounds.playSound("info");
						}
						for (QuestReward reward : rewards) {
							reward.reward();
						}
					}
				}
			} else {
				if (startObjective != null) {
					startObjective.update(elapsedTime);
					if (startObjective.isAchieved()) {
						setActive();
					}
				} else {
					setActive();
				}
			}
		}
	}
	
	public void updateNpcInteract(int npcMapId) throws Exception {
		if (!finished) {
			if (active) {
				for (QuestObjective objective : objectives) {
					if (objective instanceof NpcInteractObjective) {
						((NpcInteractObjective) objective).checkIsAchieved(npcMapId);
					}
				}
				if (this.allAchieved) {
					if (endObjective instanceof NpcInteractObjective) {
						((NpcInteractObjective) endObjective).checkIsAchieved(npcMapId);
					}
				}
			} else {
				if (startObjective != null) {
					if (startObjective instanceof NpcInteractObjective) {
						((NpcInteractObjective) startObjective).checkIsAchieved(npcMapId);
					}				
					if (startObjective.isAchieved()) {
						setActive();
					}
				} else {
					setActive();
				}
			}
		}
	}
	
	public void updateNpcWin(int npcMapId) throws Exception {
		if (!finished) {
			if (active) {
				for (QuestObjective objective : objectives) {
					if (objective instanceof NpcWinObjective) {
						((NpcWinObjective) objective).checkIsAchieved(npcMapId);
					}
				}
				if (this.allAchieved) {
					if (endObjective instanceof NpcWinObjective) {
						((NpcWinObjective) endObjective).checkIsAchieved(npcMapId);
					}
				}
			} else {
				if (startObjective != null) {
					if (startObjective instanceof NpcWinObjective) {
						((NpcWinObjective) startObjective).checkIsAchieved(npcMapId);
					}				
					if (startObjective.isAchieved()) {
						setActive();
					}
				} else {
					setActive();
				}
			}
		}
	}
}
