package game.helper;

import game.math.Vector2d;

public class DirectionHelper {

	public static enum Direction {UP, DOWN, LEFT, RIGHT}

	public static Vector2d getPosInDirection(Vector2d pos, Direction dir) {
		if (dir == Direction.UP) {
			return new Vector2d(pos.getX(), pos.getY() - 1);
		} else if (dir == Direction.DOWN) {
			return new Vector2d(pos.getX(), pos.getY() + 1);
		} else if (dir == Direction.RIGHT) {
			return new Vector2d(pos.getX() + 1, pos.getY());
		} else {
			return new Vector2d(pos.getX() - 1, pos.getY());
		}
	}

	public static Vector2d getPosInDirectionReverse(Vector2d pos, Direction dir) {
		if (dir == Direction.UP) {
			return new Vector2d(pos.getX(), pos.getY() + 1);
		} else if (dir == Direction.DOWN) {
			return new Vector2d(pos.getX(), pos.getY() - 1);
		} else if (dir == Direction.RIGHT) {
			return new Vector2d(pos.getX() - 1, pos.getY());
		} else {
			return new Vector2d(pos.getX() + 1, pos.getY());
		}
	}

	public static Direction getDirection(Vector2d pos1, Vector2d pos2) {
		int dx = (int) (pos2.getX() - pos1.getX());
		int dy = (int) (pos2.getY() - pos1.getY());
		Direction dir;

		if (Math.abs(dx) >= Math.abs(dy)) {
			if (dx > 0) {
				dir = Direction.RIGHT;
			} else {
				dir = Direction.LEFT;
			}
		} else {
			if (dy > 0) {
				dir = Direction.DOWN;
			} else {
				dir = Direction.UP;
			}
		}
		return dir;
	}

	public static boolean isOpposite(Direction dir1, Direction dir2) {
		if ((dir1 == Direction.RIGHT && dir2 == Direction.LEFT) 
				|| (dir1 == Direction.LEFT && dir2 == Direction.RIGHT) 
				|| (dir1 == Direction.UP && dir2 == Direction.DOWN) 
				|| (dir1 == Direction.DOWN && dir2 == Direction.UP)) {
			return true;
		}
		return false;
	}
	
	public static Direction reverse(Direction dir) {
		if (dir == Direction.DOWN) {
			return Direction.UP;
		} else if (dir == Direction.UP) {
			return Direction.DOWN;
		} else if (dir == Direction.RIGHT) {
			return Direction.LEFT;
		} else {
			return Direction.RIGHT;
		}
	}
}
