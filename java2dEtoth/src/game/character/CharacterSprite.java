package game.character;

import java.awt.image.BufferedImage;

import com.golden.gamedev.object.sprite.AdvanceSprite;

public class CharacterSprite extends AdvanceSprite {

	int aniLengths[];

	public CharacterSprite(BufferedImage[] image, int aniLengths[]) {
		super(image);
		this.aniLengths = aniLengths;
	}

	protected void animationChanged(int oldStat, int oldDir, int status, int direction) {
		int start = 0;
		int stop = 0;
		for (int i = 0; i < direction; i++) {
			start += aniLengths[i];
		}
		stop = start + aniLengths[direction] - 1;
		setAnimationFrame(start, stop);
	}

}
