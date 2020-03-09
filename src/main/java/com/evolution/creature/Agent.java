package com.evolution.creature;

import com.evolution.area.Area;
import com.evolution.meal.Meal;
//import com.evolution.utily.SpeedVector;

public class Agent implements Comparable<Agent>{
	private int x;
	private int y;

	//private SpeedVector speedVector;
	private int speed;
	private int acq_range;
	private int sense_range;
	private int energy;
	private float strength;
	private float criticalStrength;

	public Agent(int x, int y, int sense_range, int acq_range, int enrg, int speed, int strengthMax, float criticalStrength) {
		this.x = (int) (Math.random() * x);
		this.y = (int) (Math.random() * y);
		this.sense_range = (int) (Math.random() * (sense_range-1))+1 ;
		this.energy = enrg;
		this.acq_range = acq_range ;
		this.speed = (int)(Math.random() * (speed - 1 )) + 1 ;
		this.strength = (float) (Math.random() * (strengthMax - 1 )) + 1 ;
		this.criticalStrength = criticalStrength ;
	}
	public void kill() {
		this.sense_range = 0;
		this.acq_range = 0;
		this.energy = 0 ;
		this.strength = 0;
		this.x = 0;
		this.y = 0;
		this.speed = 0;
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
////	public SpeedVector getSpeedVector() {
////		return speedVector;
////	}
//	public void setSpeedVector(SpeedVector speedVector) {
//		this.speedVector = speedVector;
//	}
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
	public boolean isSeen (Meal m) {
		int bb = (int) Math.pow((m.getX()-this.getX()),2) + (int) Math.pow((m.getY()-this.getY()),2);
		int aa = (int) Math.sqrt(bb);
		return (sense_range >= aa);
	}
	public int distance(Meal m) {
		int bb = (int) Math.pow((m.getX()-this.getX()),2) + (int) Math.pow((m.getY()-this.getY()),2);
		int aa = (int) Math.sqrt(bb);
		return aa;
	}
	public int distance(Agent m) {
		int bb = (int) Math.pow((m.getX()-this.getX()),2) + (int) Math.pow((m.getY()-this.getY()),2);
		int aa = (int) Math.sqrt(bb);
		return aa;
	}
	public boolean isEatable(Agent a) {
		int bb = (int) Math.pow((a.getX()-this.getX()),2) + (int) Math.pow((a.getY()-this.getY()),2);
		int aa = (int) Math.sqrt(bb);
		return (acq_range >= aa) && (this.strength / a.getStrength() > this.criticalStrength);
	}
	public boolean isSeen(Agent a) {
		int bb = (int) Math.pow((a.getX()-this.getX()),2) + (int) Math.pow((a.getY()-this.getY()),2);
		int aa = (int) Math.sqrt(bb);
		return (sense_range >= aa) && (a.getStrength() / this.strength > this.criticalStrength);
	}
	public boolean isKilled() {
		return this.energy == 0;
	}
	public void addEnergy (int energy) {
		this.energy += energy;
	}
	// Убегаем от агента
	public void moveFrom(Agent a, Area area) {
		int bb = (int) Math.pow((a.getX()-this.getX()),2) + (int) Math.pow((a.getY()-this.getY()),2);
		int aa = (int) Math.sqrt(bb);
		this.x = this.x + this.speed * (this.x - a.getX()) / aa ;
		this.y = this.y + this.speed * (this.y - a.getY()) / aa ;
		this.x = bound(this.x, area.getWidth());
		this.y = bound(this.y, area.getHeigh());
		energyDec();
	}
	// Догоняем агента
	public void moveTo(Agent a) {
		int bb = (int) Math.pow((a.getX()-this.getX()),2) + (int) Math.pow((a.getY()-this.getY()),2);
		int aa = (int) Math.sqrt(bb);
		this.x = this.x + this.speed * (a.getX() - this.x) / aa ;
		this.y = this.y + this.speed * (a.getY() - this.y) / aa ;
		if (aa < (this.speed-this.acq_range)) {
			this.x = a.getX();
			this.y = a.getY();
		}
		energyDec();
	}
	// Идем к еде
	public void moveTo(Meal a) {
		int bb = (int) Math.pow((a.getX()-this.getX()),2) + (int) Math.pow((a.getY()-this.getY()),2);
		int aa = (int) Math.sqrt(bb);
		this.x = this.x + this.speed * (a.getX() - this.x) / aa ;
		this.y = this.y + this.speed * (a.getY() - this.y) / aa ;
		if (aa < (this.speed-this.acq_range)) {
			this.x = a.getX();
			this.y = a.getY();
		}
		energyDec();
	}
	// Движемся рандомно
	public void move(Area area) {
		this.x = (int) (speed * Math.sin(Math.toRadians(Math.random() * 359)) + this.x);
		this.y = (int) (speed * Math.sin(Math.toRadians(Math.random() * 359)) + this.y);
		this.x = bound(this.x, area.getWidth());
		this.y = bound(this.y, area.getHeigh());
		energyDec();
	}
	// Уменьшить энергию. Если энергия == 0, то агент убит
	private void energyDec() {
		if (energy != 0) {
			energy--;
		}
	}

	// Выход r за границу a
	private int bound (int r, int a) {
		if (r > a) {
			return 2 * a - r;
		}
		return r;
	}
	@Override
	public int compareTo(Agent o) {
		return o.speed - this.speed;
	}
	public float getStrength() {
		return strength;
	}
	public void setStrength(float strength) {
		this.strength = strength;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public float getCriticalStrength() {
		return criticalStrength;
	}
	public void setCriticalStrength(float criticalStrength) {
		this.criticalStrength = criticalStrength;
	}

}
