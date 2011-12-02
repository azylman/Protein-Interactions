package com.zylman.protein;

public class NegativeInteraction extends Interaction {

	NegativeInteraction(String first, String second) {
		super(first, second);
	}

	@Override public boolean getClassification() {
		return false;
	}
}
