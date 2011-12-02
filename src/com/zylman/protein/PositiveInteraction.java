package com.zylman.protein;

public class PositiveInteraction extends Interaction {
	
	PositiveInteraction(Protein first, Protein second) {
		super(first, second);
	}

	@Override public boolean getClassification() {
		return true;
	}
}
