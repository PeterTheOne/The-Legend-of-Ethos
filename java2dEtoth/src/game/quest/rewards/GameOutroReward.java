package game.quest.rewards;

import java.io.File;

import game.EtothGame;
import game.GameStateMachine.GameState;

public class GameOutroReward extends QuestReward {

	
	public GameOutroReward(EtothGame game) {
		super(game);
	}
	
	public void reward() throws Exception {
		if (rewarded) return;		
		game.gameSounds.playSound("gameWin");
		game.gameStateMachine.switchState(GameState.OUTRO);
		rewarded = true;
	}

}
