package com.evolution.utily;

import com.evolution.creature.Agent;
import com.evolution.genom.Genome;

import java.util.Comparator;

public class SortByGenomeEnrg implements Comparator<Genome> {
	public int compare(Genome a, Genome b) {
		float aa = b.getEnrg() - a.getEnrg() ;
		if (aa < 0) return -1;
		if (aa > 0) return 1;
		return 0;
	}
}
