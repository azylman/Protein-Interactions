package com.zylman.protein;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
			
			BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"));
			for (Interaction interaction : interactions) {
				out.write(interaction.toString(proteins));
			}
		} catch (IOException ex) {
			System.out.println("IOException: " + ex.getMessage());
		} catch (ProteinException ex) {
			System.out.println("ProteinException: " + ex.getMessage());
		}
	}
}
