package com.zylman.protein;

public abstract class Interaction {
	
	private String first;
	private String second;
	
	Interaction(String first, String second) {
		this.first = first.compareTo(second) == -1 ? first : second;
		this.second = first.compareTo(second) == -1 ? second : first;
	}
	
	String getFirst() {
		return first;
	}
	
	String getSecond() {
		return second;
	}
	
	abstract boolean getClassification();
}
