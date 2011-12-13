package game.manager;

import game.EtothGame;
import game.character.Character.CharacterState;
import game.helper.DirectionHelper.Direction;
import game.tileObjects.MonsterItem;
import game.GameStateMachine.GameState;

import java.awt.event.KeyEvent;

import com.golden.gamedev.engine.BaseInput;

public class InputManager {

	private EtothGame game;
	private BaseInput bsInput;
	
	private StringBuffer moveString;

	public InputManager(EtothGame game) {
		this.game = game;
		this.bsInput = game.bsInput;
		moveString = new StringBuffer("");
	}

	public void update(Long elapsedTime) throws Exception {
		if (game.gameStateMachine.getState() == GameState.PLAY) {
			if (game.player.getCharState() == CharacterState.IDLE) {
				//TODO: credit Chris Lipphart

				//keydown
				if (bsInput.isKeyDown(KeyEvent.VK_UP) 
						&& moveString.indexOf("u") == -1) {
					moveString.append("u");
				} else if (bsInput.isKeyDown(KeyEvent.VK_DOWN) 
						&& moveString.indexOf("d") == -1) {
					moveString.append("d");
				} else if (bsInput.isKeyDown(KeyEvent.VK_RIGHT) 
						&& moveString.indexOf("r") == -1) {
					moveString.append("r");
				} else if (bsInput.isKeyDown(KeyEvent.VK_LEFT) 
						&& 	moveString.indexOf("l") == -1) {
					moveString.append("l");
				}
				
				//keyup
				if (!bsInput.isKeyDown(KeyEvent.VK_UP) 
						&& moveString.indexOf("u") != -1) {
					moveString.deleteCharAt(moveString.indexOf("u"));
				} else if (!bsInput.isKeyDown(KeyEvent.VK_DOWN) 
						&& moveString.indexOf("d") != -1) {
					moveString.deleteCharAt(moveString.indexOf("d"));
				} else if (!bsInput.isKeyDown(KeyEvent.VK_RIGHT) 
						&& moveString.indexOf("r") != -1) {
					moveString.deleteCharAt(moveString.indexOf("r"));
				} else if (!bsInput.isKeyDown(KeyEvent.VK_LEFT) 
						&& moveString.indexOf("l") != -1) {
					moveString.deleteCharAt(moveString.indexOf("l"));
				}
				
				int len = moveString.length();
				if (len > 0) {
					char lastChar = moveString.charAt(len - 1);
					if (lastChar == 'u') {
						game.player.move(0, -1);
					} else if (lastChar == 'd') {
						game.player.move(0, 1);
					} else if (lastChar == 'r') {
						game.player.move(1, 0);
					} else if (lastChar == 'l') {
						game.player.move(-1, 0);
					}
				}
				if (bsInput.isKeyPressed(KeyEvent.VK_ENTER)) {
					game.mapMana.getCurrentMap().interact();
				} else if (bsInput.isKeyPressed(KeyEvent.VK_I)) {
					game.gameStateMachine.switchState(GameState.INV);
				}
			}
			if (bsInput.isKeyPressed(KeyEvent.VK_PAUSE)) {
				game.gameStateMachine.switchState(GameState.PAUSE);
			} else if (bsInput.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				game.gameStateMachine.switchState(GameState.EXITMENU);
			} else if (bsInput.isKeyPressed(KeyEvent.VK_H) 
					|| bsInput.isKeyPressed(KeyEvent.VK_HELP)) {
				game.gameStateMachine.switchState(GameState.HELP);
			}
		} else if (game.gameStateMachine.getState() == GameState.PAUSE) {
			if (bsInput.isKeyPressed(KeyEvent.VK_PAUSE)) {
				game.gameStateMachine.switchStateToPrev();
			} else if (bsInput.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				game.gameStateMachine.switchState(GameState.EXITMENU);
			}
		} else if (game.gameStateMachine.getState() == GameState.MSG) {
			if (bsInput.isKeyPressed(KeyEvent.VK_ENTER)) {
				game.msgMana.printNext();
			} else if (bsInput.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				game.gameStateMachine.switchState(GameState.EXITMENU);
			}
		} else if (game.gameStateMachine.getState() == GameState.FIGHT) {
			if (bsInput.isKeyPressed(KeyEvent.VK_SPACE)) {
				game.fightMana.getPlayerSprite().shoot();
			}
			if (bsInput.isKeyPressed(KeyEvent.VK_UP)) {
				game.fightMana.getPlayerSprite().jump();
			}
			if (bsInput.isKeyDown(KeyEvent.VK_RIGHT)) {
				game.fightMana.getPlayerSprite().goRight();
			}
			if (bsInput.isKeyDown(KeyEvent.VK_LEFT)) {
				game.fightMana.getPlayerSprite().goLeft();
			}
			if (bsInput.isKeyPressed(KeyEvent.VK_PAUSE)) {
				game.gameStateMachine.switchState(GameState.PAUSE);
			}
			if (bsInput.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				game.gameStateMachine.switchState(GameState.EXITMENU);
			}
			if (bsInput.isKeyPressed(KeyEvent.VK_I)) {
				game.gameStateMachine.switchState(GameState.INV);
			}
		} else if (game.gameStateMachine.getState() == GameState.INTRO) {
			if (bsInput.isKeyPressed(KeyEvent.VK_ENTER)) {
				game.gameStateMachine.switchState(GameState.CHARMENU);
			} else if (bsInput.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				game.gameStateMachine.switchState(GameState.EXITMENU);
			}
		} else if (game.gameStateMachine.getState() == GameState.OUTRO) {
				if (bsInput.isKeyPressed(KeyEvent.VK_ENTER)) {
					game.gameStateMachine.switchState(GameState.END);
				}
		} else if (game.gameStateMachine.getState() == GameState.CHARMENU) {
			if (bsInput.isKeyPressed(KeyEvent.VK_ENTER)) {
				game.charMenu.accept();
			} else if (bsInput.isKeyPressed(KeyEvent.VK_RIGHT)
				|| bsInput.isKeyPressed(KeyEvent.VK_LEFT)) {
				game.charMenu.selectNext();
			} else if (bsInput.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				game.gameStateMachine.switchState(GameState.EXITMENU);
			}
		} else if (game.gameStateMachine.getState() == GameState.EXITMENU) {
			if (bsInput.isKeyPressed(KeyEvent.VK_ENTER)) {
				game.exitMenu.accept();
			} else if (bsInput.isKeyPressed(KeyEvent.VK_RIGHT)
				|| bsInput.isKeyPressed(KeyEvent.VK_LEFT)) {
				game.exitMenu.selectNext();
			} else if (bsInput.isKeyPressed(KeyEvent.VK_ESCAPE)) {
				game.gameStateMachine.switchStateToPrev();
			}
		} else if (game.gameStateMachine.getState() == GameState.INV) {
			if (bsInput.isKeyPressed(KeyEvent.VK_ENTER)) {
				game.invMenu.interact();
			} else if (bsInput.isKeyPressed(KeyEvent.VK_UP)) {
				game.invMenu.selectPrev();
			} else if (bsInput.isKeyPressed(KeyEvent.VK_DOWN)) {
				game.invMenu.selectNext();
			} else if (bsInput.isKeyPressed(KeyEvent.VK_I)) {
				game.invMenu.leaveMenu();
			}
		} else if (game.gameStateMachine.getState() == GameState.CHOOSEMONSTER) {
			if (bsInput.isKeyPressed(KeyEvent.VK_ENTER)) {
				game.monsterMenu.interact();
			} else if (bsInput.isKeyPressed(KeyEvent.VK_UP)) {
				game.monsterMenu.selectPrev();
			} else if (bsInput.isKeyPressed(KeyEvent.VK_DOWN)) {
				game.monsterMenu.selectNext();
			}
		} else if (game.gameStateMachine.getState() == GameState.TRANS) {
			if (bsInput.isKeyPressed(KeyEvent.VK_ENTER)) {
				game.trans.interact();
			}
		} else if (bsInput.isKeyPressed(KeyEvent.VK_H) 
				|| bsInput.isKeyPressed(KeyEvent.VK_HELP) 
				|| bsInput.isKeyPressed(KeyEvent.VK_ENTER) 
				|| bsInput.isKeyPressed(KeyEvent.VK_SPACE)) {
			game.gameStateMachine.switchStateToPrev();
		}
		if (game.CHEATS == true ) {
			if (bsInput.isKeyPressed(KeyEvent.VK_1)) {
				game.gameStateMachine.switchState(GameState.OUTRO);
			} else if (bsInput.isKeyPressed(KeyEvent.VK_2)) {
				game.trans.setTrans(
						((MonsterItem) game.itemMana.getItem(4))
								.getPlayerFightSpr(), 
						game.mapMana.getCurrentMap().getNPC(0)
				);
				game.gameStateMachine.switchState(GameState.TRANS);
			}
			if (game.gameStateMachine.getState() == GameState.FIGHT) {
				if (bsInput.isKeyPressed(KeyEvent.VK_K)) {
					game.fightMana.getNPCSprite().hit(100000);
				}
			}
		}
	}

}
