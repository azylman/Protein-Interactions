package com.zylman.protein;

public class Protein {
	String dipId;
	String sequence;
	FeatureVector featureVector;
	
	Protein(String dipId, String sequence, FeatureVector featureVector) {
		this.dipId = dipId;
		this.sequence = sequence;
		this.featureVector = featureVector;
	}
	
	Protein(String dipId, String sequence) {
		this(dipId, sequence, null);
	}
	
	public String getId() {
		return dipId;
	}
	
	public String getSequence() {
		return sequence;
	}
	
	public FeatureVector getFeatureVector() {
		return featureVector;
	}
	
	public void setFeatureVector(FeatureVector featureVector) {
		this.featureVector = featureVector;
	}
}
