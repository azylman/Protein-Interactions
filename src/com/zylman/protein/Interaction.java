package com.zylman.protein;

public class Interaction {
	
	private String first;
	private String second;
	private Boolean classification;
	
	Interaction(String first, String second) {
		this(first, second, null);
	}
	
	Interaction(String first, String second, Boolean classification) {
		this.first = first.compareTo(second) == -1 ? first : second;
		this.second = first.compareTo(second) == -1 ? second : first;
		this.classification = classification;
	}
	
	String getFirst() {
		return first;
	}
	
	String getSecond() {
		return second;
	}
	
	boolean getClassification() {
		return classification;
	}
}
