package com.zylman.protein;

public class NegativeInteraction extends Interaction {

	NegativeInteraction(Protein first, Protein second) {
		super(first, second);
	}

	@Override public boolean getClassification() {
		return false;
	}
}
