package com.evolution.meal;

import com.evolution.entities.Ext;
import com.evolution.genom.Genome;

import java.util.ArrayList;
import java.util.List;

public class Meal{
	private int x;
	private int y;
	private List<Genome> energy;
	private boolean eaten ;
	private Ext ext ;

	public Meal (int x, int y, int enrg, Ext ext) {
		this.x = (int) (Math.random() * x);
		this.y = (int) (Math.random() * y);
		energy = new ArrayList<>(enrg);
		for (int i=0;i<enrg;i++) {
			this.energy.add(new Genome());
		}
		this.eaten = false;
		this.ext = ext ;
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
		eaten = true ;
		return energy;
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

	public boolean isEaten() {
		return eaten;
	}
}
