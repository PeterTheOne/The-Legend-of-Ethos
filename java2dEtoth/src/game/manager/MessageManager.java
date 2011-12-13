package game.manager;

import game.EtothGame;
import game.GameStateMachine.GameState;
import game.character.NonPlayerCharacter;
import game.exceptions.FolderContainsNoFilesException;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import com.golden.gamedev.object.GameFont;

public class MessageManager {

	private boolean active;
	private EtothGame game;
	private String msgArray[];
	private int i;
	private boolean fightCallback;
	private boolean endInteractionCallback;
	private NonPlayerCharacter npc;
	private boolean hasImg;
	private BufferedImage img;
	
	public MessageManager(EtothGame game) {
		this.game = game;
		this.active = false;
		this.i = 0;
		this.fightCallback = false;
		this.endInteractionCallback = false; 
		this.hasImg = false;
	}

	public void setMsg(String msgStr) 
			throws FolderContainsNoFilesException {
		String msgA[] = {msgStr};
		setMsg(msgA);
	}

	public void setMsg(String msgStr, BufferedImage img) 
			throws FolderContainsNoFilesException {
		String msgA[] = {msgStr};
		setMsg(msgA, img);
	}
	
	public void setMsg(String[] msgArray) 
			throws FolderContainsNoFilesException {
		if (!active && msgArray.length > 0) {
			game.gameStateMachine.switchState(GameState.MSG);
			this.msgArray = msgArray;
			hasImg = false;
			printMsg();
		}
	}
	
	public void setMsg(String[] msgArray, BufferedImage img) 
			throws FolderContainsNoFilesException {
		if (!active && msgArray.length > 0) {
			game.gameStateMachine.switchState(GameState.MSG);
			this.msgArray = msgArray;
			hasImg = true;
			this.img = img;
			printMsg();
		}
	}

	//TODO: Optimieren :D brauche ich active?
	private void printMsg() throws FolderContainsNoFilesException {
		if (this.i < msgArray.length) {
			active = true;
		} else {
			active = false;
			i = 0;
			msgArray = null;
			hasImg = false;
			img = null;
			game.gameStateMachine.switchState(GameState.PLAY);
			if (fightCallback) {
				game.gameStateMachine.switchState(GameState.CHOOSEMONSTER);
				game.monsterMenu.setNpc(npc);
			}
			if (endInteractionCallback) {
				npc.interactStop();
			}
			fightCallback = false;
			endInteractionCallback = false;
			npc = null;
		}
	}

	public void printNext() throws FolderContainsNoFilesException {
		if (active) {
			i++;
			printMsg();
		}
	}

	public boolean isActive() {
		return active;
	}

	public void setFightCallback(NonPlayerCharacter npc) {
		this.fightCallback = true;
		this.npc = npc;
	}
	
	public void setEndInteractionCallback(NonPlayerCharacter npc) {
		endInteractionCallback = true;
		this.npc = npc;
	}

	public void render(Graphics2D g) {
		
		if (this.hasImg) {
			g.drawImage(
					this.img, 
					0,
					0, 
					null
			);
		}
		
		BufferedImage box = game.getImage(
				new File(
						game.IMGPATH + 
						File.separator + 
						"msg" + 
						File.separator + 
						"msg.png"
				)
				.getPath()
		);
		g.drawImage(
				box, 
				(int) (game.getWidth() / 2d - box.getWidth() / 2d), 
				(int) (3 * game.getHeight() / 4d - box.getHeight() / 2d), 
				null
		);
		//TODO: Machen das er \n versteht...?
		g.setColor(Color.WHITE);
		game.font.drawText(
				g, 
				msgArray[i], 
				GameFont.LEFT,
				(int) (game.getWidth() / 2d - box.getWidth() / 2d + 40), 
				(int) (3 * game.getHeight() / 4d - box.getHeight() / 2d + 25 ), 
				(int) (game.getWidth() * 0.8d),
				0, 
				0
		);
	}

}
