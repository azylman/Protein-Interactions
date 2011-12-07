package com.zylman.protein;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	
	String toString(Map<String, FeatureVector> featureVectors) {
		StringBuilder result = new StringBuilder();
		
		MutableInteger startPoint = new MutableInteger(1);
		
		result.append(classification ? "1" : "-1");
		result.append(" ");
		result.append(getFeatureString(featureVectors.get(first), startPoint));
		result.append(" ");
		result.append(getFeatureString(featureVectors.get(second), startPoint));
		
		return result.toString();
	}
	
	private String getFeatureString(FeatureVector featureVector, MutableInteger startPoint) {
		StringBuilder result = new StringBuilder();
		
		List<List<Double>> features = new ArrayList<List<Double>>();
		features.add(featureVector.getHydrophobicity());
		features.add(featureVector.getHydrophicility());
		features.add(featureVector.getVolume());
		features.add(featureVector.getPolarity());
		features.add(featureVector.getPolarizability());
		features.add(featureVector.getSASA());
		features.add(featureVector.getNCI());
		
		for (List<Double> feature : features) {
			for (Double value : feature) {
				result.append(startPoint.get());
				result.append(":");
				result.append(value);
				result.append(" ");
				startPoint.increment();
			}
		}
		
		return result.toString().trim();
	}
}
