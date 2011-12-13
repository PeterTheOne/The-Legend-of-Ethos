package game.character;

import java.awt.Graphics2D;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import game.EtothGame;
import game.exceptions.CanNotReadFileException;
import game.exceptions.FolderContainsNoFilesException;
import game.helper.DirectionHelper;
import game.math.Vector2d;

public class FollowingCharacter extends NonPlayerCharacter {
	
	private Vector2d targetTilePos;
	private Vector2d targetDirPos;
	private NonPlayerCharacter npc;

	/*public FollowingCharacter(EtothGame game, int type, String imgPath) 
			throws FolderContainsNoFilesException, CanNotReadFileException, 
			SAXException, IOException, ParserConfigurationException {
		super(game, -1, type, new Vector2d(0, 0), imgPath, "");
		this.targetTilePos = tilePos;
		this.targetDirPos = targetTilePos;
		this.visible = true;
	}*/
	
	public FollowingCharacter(EtothGame game,
			NonPlayerCharacter npc) throws FolderContainsNoFilesException, 
			CanNotReadFileException, SAXException, IOException, 
			ParserConfigurationException {
		super(game, -1, npc.getType(), npc.getTilePos(), npc.getImgPath(), "");
		this.npc = npc;
		this.targetTilePos = tilePos;
		this.targetDirPos = targetTilePos;
		this.visible = true;
	}

	@Override
	public NonPlayerCharacter clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void update(Long elapsedTime) throws Exception {
		super.update(elapsedTime);
		if (!getTilePos().equals(targetTilePos)) {
			//System.out.println("tilePos: " + tilePos + ", target: " + targetTilePos);
			double deltaX = targetTilePos.getX() - getTilePos().getX();
			double deltaY = targetTilePos.getY() - getTilePos().getY();
			if (Math.abs(deltaX) > Math.abs(deltaY)) {
				if (deltaX > 0) {
					move(1, 0);
				} else {
					move(-1, 0);
				}
			} else {
				if (deltaY > 0) {
					move(0, 1);
				} else {
					move(0, -1);
				}
			}
		}
	}
	
	public void render(Graphics2D g) {
		super.render(g);
	}
	
	public void setPos(Vector2d newPos) {
		super.setPos(newPos);
		setDirection(DirectionHelper.getDirection(tilePos, targetDirPos));
	}
	
	public void stop(boolean resetTilePosX, boolean resetTilePosY) throws Exception {
		super.stop(resetTilePosX, resetTilePosY);
		setDirection(DirectionHelper.getDirection(tilePos, targetDirPos));
	}
	
	public void setTarget(Vector2d targetTilePos, Vector2d targetDirPos) {
		this.targetTilePos = targetTilePos;
		this.targetDirPos = targetDirPos;
	}
	
	public NonPlayerCharacter getNpc() {
		this.npc.setPos(tilePos);
		this.npc.setDirection(direction);
		return npc;
	}

}
