package game.tileObjects;

import game.EtothGame;
import game.exceptions.FolderContainsNoFilesException;
import game.helper.IOHelper;
import game.math.Vector2d;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import com.golden.gamedev.object.AnimatedSprite;

public class Tile extends TileObject {
	
	protected boolean solid;
	protected AnimatedSprite sprHidden;
	
	public Tile(EtothGame game, BufferedImage[] images, Vector2d tilePos, int type, boolean solid) throws FolderContainsNoFilesException {
		super(game, images, tilePos, type);
		this.game = game;
		createSprHidden(images);
		this.solid = solid;
	}
	
	private void createSprHidden(BufferedImage[] images) throws FolderContainsNoFilesException {
		BufferedImage[] newImages = new BufferedImage[images.length];
		for (int i = 0; i < newImages.length; i++) {
			newImages[i] = images[i];
		}
		//RescaleOp rescaleOp = new RescaleOp(1, 2, null);
		for (int i = 0; i < newImages.length; i++) {
			//rescaleOp.filter(newImages[i], newImages[i]);
			//TODO: change Image!
			newImages[i] = game.getImage(
					EtothGame.fileToURL(game.IMGPATH + 
					File.separator + 
					"fogofwar" + 
					File.separator + 
					"fogofwar1.png")
			);
		}
		this.sprHidden = new AnimatedSprite(newImages);
	}
	
	public void setSprHidden(URL path) throws FolderContainsNoFilesException {
		BufferedImage[] images = IOHelper.getImages(game, path);
		this.sprHidden = new AnimatedSprite(images);
	}
	
	public Tile(EtothGame game, BufferedImage[] images, int type, boolean solid) throws FolderContainsNoFilesException {
		super(game, images, type);
		this.solid = solid;
		
		createSprHidden(images);
		
		this.visible = !game.FOGOFWAR;
	}

	public Tile clone() {
		try {
			return new Tile(game, spr.getImages(), getTilePos(), getType(), solid);
		} catch (FolderContainsNoFilesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean getSolid() {
		return solid;
	}
	
	public void setTilePos(Vector2d tilePos) {
		super.setTilePos(tilePos);
		sprHidden.setX(getPos().getX());
		sprHidden.setY(getPos().getY());
	}
	
	public void update(long elapsedTime) {
		super.update(elapsedTime);
		sprHidden.update(elapsedTime);
	}
	
	public void render(Graphics2D g) {
		if (visible) {
			super.render(g);
		} else {
			//TODO: maybe use transparentFilter:
			// see: http://download.oracle.com/javase/tutorial/2d/images/drawimage.html
			// somehow doesn't work..
			
			sprHidden.render(g);
		}
	}
}
