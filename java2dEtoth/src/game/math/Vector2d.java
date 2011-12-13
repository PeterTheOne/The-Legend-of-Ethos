package game.math;

public class Vector2d {

	private double x, y;
	
	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2d() {
		this(0, 0);
	}
	
	public Vector2d(int x, int y) {
		this((double) x, (double) y);
	}
	
	public Vector2d(Vector2d arg) {
		set(arg);
	}

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(Vector2d arg) {
		set(arg.getX(), arg.getY());
	}
	
	public Vector2d add(double x, double y) {
		return new Vector2d(this.x + x, this.y + y);
	}

	public Vector2d add(double s) {
		return add(s, s);
	}

	public Vector2d add(Vector2d arg) {
		return add(arg.getX(), arg.getY());
	}

	public Vector2d multi(double s) {
		return new Vector2d(this.x * s, this.y * s);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public String toString() {
		return "Vector2d: [x:" + x + "], [y:" + y +"]";
	}
	
	public boolean equals(Vector2d arg) {
		if (((Double)x).equals(arg.getX()) && ((Double)y).equals(arg.getY())) {
			return true;
		}
		return false;
	}

	public Vector2d abs() {
		return new Vector2d(Math.abs(this.x), Math.abs(this.y));
	}

	public double length() {
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
	}

	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public double getDistance(Vector2d arg) {
		return new Vector2d(this.x - arg.x, this.y - arg.y).length();
	}
	
}
