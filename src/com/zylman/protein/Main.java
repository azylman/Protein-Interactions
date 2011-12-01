package com.zylman.protein;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		
		List<Interaction> enhancedDatabase = new ArrayList<Interaction>();
		
		long startTime = new Date().getTime() / 1000;
		
		DIP dip = new DIP("dip20111027.txt");
		
		long endTime = new Date().getTime() / 1000;
		
		System.out.println("Found " + dip.getInteractions().size() + " interactions in " + (endTime - startTime) + "seconds");
		
		for (PositiveInteraction interaction : dip.getInteractions()) {
			enhancedDatabase.add(interaction);
			enhancedDatabase.add(createNegativeInteraction(interaction));
		}
		
		// split up dataset into testing and training set
		
		// write the sets to the disk
		
		// call svm light to create the model
		
		// classify our test set with our learned model
		
		// determine accuracy
		
		// train/test with different parameters/species?
	}
	
	private static NegativeInteraction createNegativeInteraction(PositiveInteraction positiveInteraction) {
		return new NegativeInteraction(shuffle(positiveInteraction.getFirst().getSequence()), positiveInteraction.getSecond().getSequence());
	}
	
	private static String shuffle(String sequence) {
		return sequence; // TODO: shuffle sequence
	}
}
