package game;

import com.golden.gamedev.Game;
import com.golden.gamedev.GameLoader;

public class EtothGameApplet extends GameLoader {

	protected Game createAppletGame() {
		return new EtothGame();
	}
}
