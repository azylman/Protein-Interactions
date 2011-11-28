package com.zylman.protein;

public interface Interaction {
	FeatureVector getFirst();
	FeatureVector getSecond();
	boolean getClassification();
}
