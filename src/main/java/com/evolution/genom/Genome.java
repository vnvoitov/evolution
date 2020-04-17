package com.evolution.genom;

public class Genome {
	private float genA;
	private float genB;
	private float genC;
	private float Enrg;
	{
		genA = (float) Math.random();
		genB = (float) Math.random();
		genC = (float) Math.random();
		Enrg = 0 ;
	}
	public float getGenA() {
		return genA;
	}

	public void setGenA(float genA) {
		this.genA = genA;
	}

	public float getGenB() {
		return genB;
	}

	public void setGenB(float genB) {
		this.genB = genB;
	}

	public float getGenC() {
		return genC;
	}

	public void setGenC(float genC) {
		this.genC = genC;
	}

	public float getGen(String key) {
		float rc = 0;
		switch (key) {
			case "A":
				rc =  getGenA();
				break;
			case "B":
				rc = getGenB();
			break;
			case "C":
				rc = getGenC();
			break;
		}
		return rc ;
	}

	public float getEnrg() {
		return Enrg;
	}

	public void setEnrg(float enrg) {
		Enrg = enrg;
	}
}
