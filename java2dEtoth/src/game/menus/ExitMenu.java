package game.menus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import com.golden.gamedev.object.GameFont;

import game.EtothGame;
import game.GameStateMachine.GameState;
import game.math.Vector2d;

public class ExitMenu {
	
	private EtothGame game;
	private boolean selected;

	private BufferedImage resume;
	private BufferedImage exit;
	
	public ExitMenu(EtothGame game) {
		this.game = game;
		selected = true;

		File resumePath = new File(game.EXITMENUIMGPATH + File.separator + 
				"resumeSelected.png");
		File exitPath =  new File(game.EXITMENUIMGPATH + File.separator + 
				"exitSelected.png");
		this.resume = game.getImage(resumePath.getPath());
		this.exit = game.getImage(exitPath.getPath());
	}
	
	public void selectNext() {
		selected = !selected;
	}
	
	public void accept() {
		if (selected) {
			game.gameStateMachine.switchStateToPrev();
		} else {
			game.gameStateMachine.switchState(GameState.END);
		}
	}
	
	public void render(Graphics2D g) {
		//clearRectangle		
		g.fillRect(0, 0, game.getWidth(), game.getHeight());
		
		Vector2d pos = new Vector2d(85, 90);
		if (selected) {
			g.drawImage(resume, (int) pos.getX(), (int) pos.getY(), null);
		} else {
			g.drawImage(exit, (int) pos.getX(), (int) pos.getY(), null);
		}
		
		String text = game.gameTexts.getText("exitgame");
		
		g.setColor(Color.WHITE);
		game.font.drawText(
				g, 
				text, 
				GameFont.LEFT,
				(int) 215, 
				(int) 155 , 
				(int) (game.getWidth() * 0.8d),
				0, 
				0
		);
	}
}
