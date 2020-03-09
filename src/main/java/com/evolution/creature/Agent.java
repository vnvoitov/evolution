package com.evolution.creature;

import com.evolution.area.Area;
import com.evolution.meal.Meal;
//import com.evolution.utily.SpeedVector;

public class Agent {
	private int x;
	private int y;

	//private SpeedVector speedVector;
	private float speed;
	private int acq_range;
	private float sense_range;
	private int energy;
	private float strength;
	private float criticalStrength;
	private int energyDec ;
	private float mutationDepth;


	private int num;

	public Agent(int x, int y, int sense_range, int acq_range, int enrg, int speed, int strengthMax, float criticalStrength, float mutationDepth, int num) {
		this.x = (int) Math.round(Math.random() * x);
		this.y = (int) Math.round((Math.random() * y));
		this.sense_range = (int) Math.round(Math.random() * (sense_range-1)+1) ;
		this.energy = enrg;
		this.acq_range = acq_range ;
		this.speed = Math.round(Math.random() * (speed-1))+1 ;
		this.strength = (float) Math.round(Math.random() * (strengthMax-1)+1);
		this.criticalStrength = criticalStrength ;
		this.energyDec = 1;
		this.mutationDepth = mutationDepth ;
		this.num = num ;
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

	public int getAcq_range() {
		return acq_range;
	}
	public void setAcq_range(int acq_range) {
		this.acq_range = acq_range;
	}
	public float getSense_range() {
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
		this.x = (int) (this.x + this.speed * (this.x - a.getX()) / aa);
		this.y = (int) (this.y + this.speed * (this.y - a.getY()) / aa);
		this.x = bound(this.x, area.getWidth());
		this.y = bound(this.y, area.getHeigh());
		energyDec();
	}
	// Догоняем агента
	public void moveTo(Agent a) {
		int bb = (int) Math.pow((a.getX()-this.getX()),2) + (int) Math.pow((a.getY()-this.getY()),2);
		int aa = (int) Math.sqrt(bb);
		this.x = (int) (this.x + this.speed * (a.getX() - this.x) / aa);
		this.y = (int) (this.y + this.speed * (a.getY() - this.y) / aa);
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
		this.x = (int) (this.x + this.speed * (a.getX() - this.x) / aa);
		this.y = (int) (this.y + this.speed * (a.getY() - this.y) / aa);
		if (aa < (this.speed-this.acq_range)) {
			this.x = a.getX();
			this.y = a.getY();
		}
		energyDec();
	}
	// Движемся рандомно
	public void move(Area area) {
		this.x = (int) Math.round(speed * Math.sin(Math.toRadians(Math.random() * 359)) + this.x);
		this.y = (int) Math.round(speed * Math.sin(Math.toRadians(Math.random() * 359)) + this.y);
		this.x = bound(this.x, area.getWidth());
		this.y = bound(this.y, area.getHeigh());
		energyDec();
	}
	// Уменьшить энергию. Если энергия == 0, то агент убит
	private void energyDec() {
		energy -= energyDec;
		if (energy < 0) {
			energy = 0;
		}
	}

	// Выход r за границу a
	private int bound (int r, int a) {
		if (r > a) {
			return 2 * a - r;
		}
		return r;
	}

	public float getStrength() {
		return strength;
	}
	public void setStrength(float strength) {
		this.strength = strength;
	}
	public float getSpeed() {
		return speed;
	}

	public float getCriticalStrength() {
		return criticalStrength;
	}
	public void setCriticalStrength(float criticalStrength) {
		this.criticalStrength = criticalStrength;
	}
	// Эволюция. Рандомная из трех характеристик рандомно увеличивается
	public void evaluate() {
		int property = (int) Math.round(Math.random() * 2);
		energyDec ++ ;
		switch (property) {
			case 0:
				this.strength *= 1+mutationDepth ;
				break;
			case 1:
				this.speed *= 1+mutationDepth;
				break;
			case 2:
				this.sense_range *= 1+mutationDepth ;
				break;
		}
	}
	public int getNum() {
		return num;
	}

}
