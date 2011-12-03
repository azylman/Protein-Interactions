package com.zylman.protein;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Main {
	public static void main(String[] args) {
		
		try {
			ProteinDatabase pDb = new ProteinDatabase("localhost", "protein", "root", "qwerty");
			
			long startTime;
			long endTime;
			
			if (pDb.neededToCreateTables()) {
				startTime = new Date().getTime() / 1000;
				
				Proteins sequences = new Proteins("fasta20101010.seq");
				
				DIP dip = new DIP("dip20111027.txt", sequences);
				
				endTime = new Date().getTime() / 1000;
				
				System.out.println("Found " + dip.getInteractions().size() + " interactions comprising "
						+ sequences.getProteins().size() + " proteins in " + (endTime - startTime) + " seconds"); 
				
				startTime = new Date().getTime() / 1000;
				pDb.addProteinList(sequences.getProteins());
				endTime = new Date().getTime() / 1000;
				System.out.println("Took " + (endTime + startTime) + " seconds to add the proteins to the database");
				
				startTime = new Date().getTime() / 1000;
				pDb.addInteractionList(dip.getInteractions());
				endTime = new Date().getTime() / 1000;
				System.out.println("Took " + (endTime + startTime) + " seconds to add the interactions to the database");
			}
			
			startTime = new Date().getTime() / 1000;
			List<Interaction> interactions = pDb.getInteractions();
			endTime = new Date().getTime() / 1000;
			System.out.println("Retrieved interactions in " + (endTime - startTime) + " seconds");
			
			startTime = new Date().getTime() / 1000;
			Map<String, FeatureVector> proteins = pDb.getProteins();
			endTime = new Date().getTime() / 1000;
			System.out.println("Retrieved proteins in " + (endTime - startTime) + " seconds");
			
		} catch (ProteinException ex) {
			System.out.println("Protein exception: " + ex.getMessage());
		}
	}
}
