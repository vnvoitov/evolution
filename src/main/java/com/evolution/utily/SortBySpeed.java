package com.evolution.utily;

import com.evolution.creature.Agent;

import java.util.Comparator;

public class SortBySpeed implements Comparator<Agent> {
	public int compare(Agent a, Agent b) {
		if ((b.getSpeed() - a.getSpeed()) < 0) {
			return -1;
		}
		if ((b.getSpeed() - a.getSpeed()) > 0) {
			return 1;
		}
		return 0;
	}
}
