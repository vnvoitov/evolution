package com.evolution.meal;

import com.evolution.utily.Location;

public class Meal{
	private int x;
	private int y;
	private int energy;

	public Meal (int x, int y, int energy) {
		this.x = (int) (Math.random() * x);
		this.y = (int) (Math.random() * y);
		this.energy = energy;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

}
