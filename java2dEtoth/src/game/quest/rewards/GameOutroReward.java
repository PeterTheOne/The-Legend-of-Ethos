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
		File crowdSound = new File(game.SOUNDPATH + File.separator + "crowd.wav");
		game.bsSound.play(crowdSound.getAbsolutePath());
		game.gameStateMachine.switchState(GameState.OUTRO);
		rewarded = true;
	}

}
