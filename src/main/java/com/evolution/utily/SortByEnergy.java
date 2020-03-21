package com.evolution.utily;

import com.evolution.creature.Agent;

import java.util.Comparator;

public class SortByEnergy implements Comparator<Agent> {
		public int compare(Agent a, Agent b) {
			return (int) (b.valueEnergy() - a.valueEnergy());
		}
}
