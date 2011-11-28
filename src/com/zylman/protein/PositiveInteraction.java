package com.zylman.protein;

public class PositiveInteraction implements Interaction {

	private FeatureVector first;
	private FeatureVector second;
	
	PositiveInteraction(String first, String second) {
		this.first = new FeatureVector(first);
		this.second = new FeatureVector(second);
	}
	
	@Override
	public FeatureVector getFirst() {
		return first;
	}

	@Override
	public FeatureVector getSecond() {
		return second;
	}

	@Override
	public boolean getClassification() {
		return true;
	}

}
