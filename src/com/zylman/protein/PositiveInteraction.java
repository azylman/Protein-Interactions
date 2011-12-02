package com.zylman.protein;

public class PositiveInteraction extends Interaction {
	
	PositiveInteraction(String first, String second) {
		super(first, second);
	}

	@Override public boolean getClassification() {
		return true;
	}
}
