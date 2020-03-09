package com.evolution.creature;

import com.evolution.meal.Meal;
import com.evolution.utily.SpeedVector;

public class Agent implements Comparable<Agent>{
	private int x;
	private int y;
	private SpeedVector speedVector;
	private int acq_range;
	private int sense_range;
	private int energy;

	private float strength;

	public Agent(int x, int y, int sense_range, int acq_range, int enrg, int speed, int powerMax) {
		this.x = (int) (Math.random() * x);
		this.y = (int) (Math.random() * y);
		this.sense_range = (int) (Math.random() * (sense_range-1))+1 ;
		this.energy = enrg;
		this.acq_range = acq_range ;
		this.speedVector = new SpeedVector(speed, this.x, this.y);
		this.strength = (float) (Math.random() * (powerMax - 1 )) + 1 ;
	}

	public void kill() {
		this.sense_range = 0;
		this.acq_range = 0;
		this.energy = 0 ;
		this.strength = 0;
		this.x = 0;
		this.y = 0;
		this.speedVector.setSpeed(0);
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

	public SpeedVector getSpeedVector() {
		return speedVector;
	}

	public void setSpeedVector(SpeedVector speedVector) {
		this.speedVector = speedVector;
	}

	public int getAcq_range() {
		return acq_range;
	}

	public void setAcq_range(int acq_range) {
		this.acq_range = acq_range;
	}
	public int getSense_range() {
		return sense_range;
	}
	public void setSense_range(int sense_range) {
		this.sense_range = sense_range;
	}
	public int getEnergy() {
		return energy;
	}
	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public boolean isEatable(Meal m) {
		int bb = (int) Math.pow((m.getX()-this.getX()),2) + (int) Math.pow((m.getY()-this.getY()),2);
		int aa = (int) Math.sqrt(bb);
		return acq_range >= aa;
	}
	public boolean isEatable(Agent a) {
		int bb = (int) Math.pow((a.getX()-this.getX()),2) + (int) Math.pow((a.getY()-this.getY()),2);
		int aa = (int) Math.sqrt(bb);
		return (acq_range >= aa) && (this.strength / a.getStrength() > 1.2);
	}
	public boolean isSeen(Agent a) {
		int bb = (int) Math.pow((a.getX()-this.getX()),2) + (int) Math.pow((a.getY()-this.getY()),2);
		int aa = (int) Math.sqrt(bb);
		return (sense_range >= aa) && (this.strength / a.getStrength() > 1.2);
	}
	public boolean isKilled() {
		return this.energy == 0;
	}
	public void addEnergy (int energy) {
		this.energy += energy;
	}
	@Override
	public int compareTo(Agent o) {
		return o.getSpeedVector().getSpeed() - this.getSpeedVector().getSpeed();
	}
	public float getStrength() {
		return strength;
	}
	public void setStrength(float strength) {
		this.strength = strength;
	}


}
