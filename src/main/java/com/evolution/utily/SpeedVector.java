package com.evolution.utily;

public class SpeedVector {
	private int x0;
	private int x1;
	private int y0;
	private int y1;
	private int speed ;

	public SpeedVector(int speed, int x, int y) {
		this.x0 = x;
		this.y0 = y;
		this.speed = (int) (Math.random() * (speed-1)) + 1;

		this.x1 = x0 + this.speed * (int) (Math.cos(direction()));
		this.y1 = y0 + this.speed * (int) (Math.cos(direction()));
	}

	private int direction() {
		return (int) (Math.random()*359);
	}

	public int getX0() {
		return x0;
	}

	public void setX0(int x0) {
		this.x0 = x0;
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY0() {
		return y0;
	}

	public void setY0(int y0) {
		this.y0 = y0;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
