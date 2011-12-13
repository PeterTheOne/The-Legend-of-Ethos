package game.tileObjects;

import game.EtothGame;
import game.Map;
import game.exceptions.NotFoundException;
import game.helper.DirectionHelper;
import game.helper.DirectionHelper.Direction;
import game.math.Vector2d;

public class Door {

	private int id;
	private Vector2d pos;
	private Direction direction;
	private String target;
	private int targetId;
	
	public Door(int id, int x, int y, Direction direction, String target, 
			int targetId) {
		this.id = id;
		this.pos = new Vector2d(x, y);
		this.direction = direction;
		this.target = target;
		this.targetId = targetId;
	}

	public Vector2d getPos() {
		return pos;
	}

	public String getTarget() {
		return target;
	}

	public int getId() {
		return id;
	}

	public Direction getDirection() {
		return direction;
	}

	public int getTargetId() {
		return targetId;
	}

	public Vector2d getTargetPos(EtothGame game) throws NotFoundException {
		Map targetMap = game.mapMana.getMap(target);
		Door targetDoor = targetMap.getDoor(targetId);
		Vector2d targetPos = targetDoor.getPos();
		targetPos = DirectionHelper.getPosInDirectionReverse(
				targetPos, direction);
		return targetPos;
	}
	
	public Vector2d getTargetDoorPos(EtothGame game) throws NotFoundException {
		Map targetMap = game.mapMana.getMap(target);
		Door targetDoor = targetMap.getDoor(targetId);
		return targetDoor.getPos();
	}

}
