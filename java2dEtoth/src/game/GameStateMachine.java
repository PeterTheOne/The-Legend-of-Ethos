package game;

import game.exceptions.FolderContainsNoFilesException;

import java.awt.Color;
import java.awt.Graphics2D;

public class GameStateMachine {
	EtothGame game;
	public static enum GameState {INIT, INTRO, CHARMENU, PLAY, PAUSE, MSG, 
		INV, HELP, FIGHT, OUTRO, EXITMENU, END, CHOOSEMONSTER, TRANS}
	private GameState state;
	private GameState prevState;

	public GameStateMachine(EtothGame game) {
		this.game = game;
		prevState = GameState.INIT;
		switchState(GameState.INIT);
	}

	public void switchStateToPrev() {
		switchState(prevState);
	}
	
	public void switchState(GameState newState) {
		//TODO: switch to newState from what state???
		//		zb. Fightstate to PLAY or back...
		
		if (!prevState.equals(newState)) {
			prevState = state;
		}
		switch (newState) {
		case INIT:
			state = GameState.INIT;
			break;
		case INTRO:
			state = GameState.INTRO;
			break;
		case CHARMENU:
			state = GameState.CHARMENU;
			break;
		case PLAY:
			if (game.initPlay()) state = GameState.PLAY;
			break;
		case PAUSE:
			state = GameState.PAUSE;
			break;
		case MSG:
			state = GameState.MSG;
			break;
		case FIGHT:
			state = GameState.FIGHT;
			break;
		case OUTRO:
			state = GameState.OUTRO;
			game.gameSounds.playSound("gameWin");
			break;
		case EXITMENU:
			state = GameState.EXITMENU;
			break;
		case HELP:
			state = GameState.HELP;
			break;
		case END:
			state = GameState.END;
			break;
		case INV:
			state = GameState.INV;
			break;
		case CHOOSEMONSTER:
			state = GameState.CHOOSEMONSTER;
			break;
		case TRANS:
			state = GameState.TRANS;
		}
	}
	
	public void update(long elapsedTime) throws Exception {
		switch (state) {
		case INIT:
			if (game.init())switchState(GameState.INTRO);
			break;
		case INTRO:
			break;
		case OUTRO:
			break;
		case CHARMENU:
			game.charMenu.update(elapsedTime);
			break;
		case INV:
			game.invMenu.update(elapsedTime);
			break;
		case CHOOSEMONSTER:
			game.monsterMenu.update(elapsedTime);
			break;
		case TRANS:
			game.trans.update(elapsedTime);
			break;
		case END:
			game.stop();
			game.finish();
			System.exit(0); //is not needed..
			break;
		case MSG:
			game.mapMana.update(elapsedTime);
			break;
		case PLAY:
			game.pHealthBar.update(elapsedTime);
			game.questMana.update(elapsedTime);
			game.mapMana.update(elapsedTime);
			game.player.update(elapsedTime);
			game.fightMana.update(elapsedTime);
			game.elapsedCache += elapsedTime;
			if (game.RELOADCURRENTMAPMODE && game.elapsedCache > 1000) {
				game.elapsedCache = 0L;
				game.mapMana.getCurrentMap().reload();
			}
			break;
			
		case FIGHT:
			game.pHealthBar.update(elapsedTime);
			game.mapMana.update(elapsedTime);
			game.player.update(elapsedTime);
			game.fightMana.update(elapsedTime);
			game.elapsedCache += elapsedTime;
			break;
		}
	}
	
	public void render(Graphics2D g) throws FolderContainsNoFilesException {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, game.getWidth(), game.getHeight());
		
		switch(getState()) {
		case PLAY:
			game.mapMana.render(g);
			game.player.render(g);
			game.pHealthBar.render(g);
			break;
		case MSG:
			game.mapMana.render(g);
			game.player.render(g);
			game.pHealthBar.render(g);
			game.msgMana.render(g);
			break;
		case INV:
			game.mapMana.render(g);
			game.player.render(g);
			game.invMenu.render(g);
			game.pHealthBar.render(g);
			break;
		case CHOOSEMONSTER:
			game.mapMana.render(g);
			game.player.render(g);
			game.monsterMenu.render(g);
			game.pHealthBar.render(g);
			break;
		case INTRO:
			game.intro.render(g);
			break;
		case OUTRO:
			game.outro.render(g);
			break;
		case EXITMENU:
			game.exitMenu.render(g);
			break;
		case CHARMENU:
			game.charMenu.render(g);
			break;
		case TRANS:
			game.trans.render(g);
			break;
		case HELP:
			game.mapMana.render(g);
			game.player.render(g);
			game.pHealthBar.render(g);
			game.helpInfo.render(g);
			break;
		case FIGHT:
			game.fightMana.render(g);
			game.pHealthBar.render(g);
			break;
		case PAUSE:
			//TODO: PAUSEMODE Textanzeige
			break;
		case END:
			//TODO
			break;
		}
	}
	
	public GameState getState() {
		return this.state;
	}

	public GameState getPrevState() {
		return this.prevState;
	}
}
