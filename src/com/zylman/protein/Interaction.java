package com.zylman.protein;

public abstract class Interaction {
	
	private Protein first;
	private Protein second;
	
	Interaction(Protein first, Protein second) {
		this.first = first.getId().compareTo(second.getId()) == -1 ? first : second;
		this.second = first.getId().compareTo(second.getId()) == -1 ? second : first;
	}
	
	Protein getFirst() {
		return first;
	}
	
	Protein getSecond() {
		return second;
	}
	
	abstract boolean getClassification();
}
