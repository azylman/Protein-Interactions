package com.zylman.protein;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		List<Interaction> enhancedDatabase = new ArrayList<Interaction>();
		
		DIP dip = new DIP("dip20111027.txt");
		
		for (PositiveInteraction interaction : dip.getPositiveInteractions()) {
			enhancedDatabase.add(interaction);
			enhancedDatabase.add(createShuffledInteraction(interaction));
		}
	}
	
	private static NegativeInteraction createShuffledInteraction(PositiveInteraction positiveInteraction) {
		return new NegativeInteraction(shuffle(positiveInteraction.getFirst().getSequence()), positiveInteraction.getSecond().getSequence());
	}
	
	private static String shuffle(String sequence) {
		return null;
	}
}
