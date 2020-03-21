package com.evolution.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ext {
	private float extA;
	private float extB;
	private float extC;
	private ExtT extT;
	public static class ExtT {
		public String key;
		public float value;
		public ExtT(String key, float value) {
			this.key = key;
			this.value = value;
		}
	}
	{
		extChange();
	}
	public void extChange () {
		extA = (float) (Math.random()*2-1);
		extB = (float) (Math.random()*2-1);
		extC = (float) (Math.random()*2-1);
	}

	public List<ExtT> extOrder() {
		List<ExtT> rc = new ArrayList<>(3);
		if (extA > extB) {
			if (extA > extC) {
//				rc.add(new HashMap<String, Float>(){{put("A",extA);}});
				rc.add(new ExtT("A", extA));
				if (extB > extC) {
//					rc.add(new HashMap<String, Float>(){{put("B",extB);}});
//					rc.add(new HashMap<String, Float>(){{put("C",extC);}});
					rc.add(new ExtT("B", extB));
					rc.add(new ExtT("C", extC));
				} else {
//					rc.add(new HashMap<String, Float>(){{put("C",extC);}});
//					rc.add(new HashMap<String, Float>(){{put("B",extB);}});
					rc.add(new ExtT("C", extB));
					rc.add(new ExtT("B", extC));
				}
			} else {
//				rc.add(new HashMap<String, Float>(){{put("C",extC);}});
//				rc.add(new HashMap<String, Float>(){{put("A",extA);}});
//				rc.add(new HashMap<String, Float>(){{put("B",extB);}});
				rc.add(new ExtT("C", extC));
				rc.add(new ExtT("A", extA));
				rc.add(new ExtT("B", extB));
			}
		} else {
			if (extA < extC) {
//				rc.add(new HashMap<String, Float>(){{put("A",extA);}});
				rc.add(new ExtT("A", extA));
				if (extB > extC) {
//					rc.add(new HashMap<String, Float>(){{put("B",extB);}});
//					rc.add(new HashMap<String, Float>(){{put("C",extC);}});
					rc.add(new ExtT("B", extB));
					rc.add(new ExtT("C", extC));
				} else {
//					rc.add(new HashMap<String, Float>(){{put("C",extC);}});
//					rc.add(new HashMap<String, Float>(){{put("B",extB);}});
					rc.add(new ExtT("C", extC));
					rc.add(new ExtT("B", extB));
				}
			} else {
//				rc.add(new HashMap<String, Float>(){{put("B",extB);}});
//				rc.add(new HashMap<String, Float>(){{put("A",extA);}});
//				rc.add(new HashMap<String, Float>(){{put("C",extC);}});
				rc.add(new ExtT("B", extB));
				rc.add(new ExtT("A", extA));
				rc.add(new ExtT("C", extC));
			}
		}
		return rc;
	}
}
