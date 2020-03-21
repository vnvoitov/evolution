package com.evolution.creature;

import com.evolution.area.Area;
import com.evolution.entities.Ext;
import com.evolution.genom.Genome;
import com.evolution.meal.Meal;

import java.util.ArrayList;
import java.util.List;

public class Agent {
	private int x;
	private int y;

	//private SpeedVector speedVector;
	private float speed;
	private int acq_range;
	private float sense_range;
	private List<Genome> energy;
	private float strength;
	private float criticalStrength;
	private int energyDec ;
	private float mutationDepth;
	private boolean isKilled;
	private Ext ext ;


	private int num;

	public Agent(int x, int y, int sense_range, int acq_range, int enrg, int speed, int strengthMax, float criticalStrength, float mutationDepth, int num, Ext ext) {
		this.x = (int) Math.round(Math.random() * x);
		this.y = (int) Math.round((Math.random() * y));
		this.sense_range = (int) Math.round(Math.random() * (sense_range-1)+1) ;
		energy = new ArrayList<>(enrg);
		for (int i=0;i<enrg;i++) {
			this.energy.add(new Genome());
		}
		this.acq_range = acq_range ;
		this.speed = Math.round(Math.random() * (speed-1))+1 ;
		this.strength = (float) Math.round(Math.random() * (strengthMax-1)+1);
		this.criticalStrength = criticalStrength ;
		this.energyDec = 1;
		this.mutationDepth = mutationDepth ;
		this.num = num ;
		this.isKilled = false;
		this.ext = ext ;
	}
	public void kill() {
		this.sense_range = 0;
		this.acq_range = 0;
		this.isKilled = true ;
		this.strength = 0;
		this.x = 0;
		this.y = 0;
		this.speed = 0;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public float getSense_range() {
		return sense_range;
	}
	public float valueEnergy() {
		List<Ext.ExtT> e = ext.extOrder();
		float rc = 0 ;
		for (int i=0; i<energy.size(); i++) {
			rc += e.get(0).value * energy.get(i).getGen(e.get(0).key) +
					e.get(1).value * energy.get(i).getGen(e.get(1).key) -
					Math.abs(e.get(2).value * energy.get(i).getGen(e.get(2).key));
		}
		return rc;
	}
	public List<Genome> getEnergy() {
		return energy;
	}
	public boolean isEatable(Meal m) {
		if (!m.isEaten()) {
			int bb = (int) Math.pow((m.getX() - this.x), 2) + (int) Math.pow((m.getY() - this.y), 2);
			int aa = (int) Math.sqrt(bb);
			return acq_range >= aa;
		}
		return false;
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
		return this.isKilled;
	}
	public void addEnergy (List<Genome> energy) {
		for (Genome g: energy) {
			this.energy.add(g);
		}
	}
	// Убегаем от агента
	public void moveFrom(Agent a, Area area) {
		int bb = (int) Math.pow((a.getX()-this.getX()),2) + (int) Math.pow((a.getY()-this.getY()),2);
		int aa = (int) Math.sqrt(bb);
		this.x = (int) (this.x + this.speed * (this.x - a.getX()) / aa);
		this.y = (int) (this.y + this.speed * (this.y - a.getY()) / aa);
		this.x = bound(this.x, area.getWidth());
		this.y = bound(this.y, area.getHeigh());
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
	}
	// Движемся рандомно
	public void move(Area area) {
		this.x = (int) Math.round(speed * Math.sin(Math.toRadians(Math.random() * 359)) + this.x);
		this.y = (int) Math.round(speed * Math.sin(Math.toRadians(Math.random() * 359)) + this.y);
		this.x = bound(this.x, area.getWidth());
		this.y = bound(this.y, area.getHeigh());
	}
	// Уменьшить энергию. Если энергия == 0, то агент убит
	private void energyDec() {
		if (this.valueEnergy() <= 1) {
			kill();
		}
	}
	// Выход r за границу a
	private int bound (int r, int a) {
		if (r > a) {
			return a-1;
		}
		if (r<0) {
			return 1;
		}
		return r;
	}
	public float getStrength() {
		return strength;
	}
	public float getSpeed() {
		return speed;
	}
	public String getStrength_s() {
		return String.valueOf(strength).replace('.',',');
	}
	public String getSense_range_s() {
		return String.valueOf(sense_range).replace('.',',');
	}
	public String valueEnergy_s() {
		return String.valueOf(valueEnergy()).replace('.',',');
	}
	public String getSpeed_s() {
		return String.valueOf(speed).replace('.',',');
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
