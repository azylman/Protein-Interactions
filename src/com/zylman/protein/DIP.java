package com.zylman.protein;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DIP {
	
	private static final Proteins sequences = new Proteins("fasta20101010.seq");
	
	private List<PositiveInteraction> positiveInteractions = new ArrayList<PositiveInteraction>();
	private List<NegativeInteraction> negativeInteractions = new ArrayList<NegativeInteraction>();
	private List<Interaction> interactions = new ArrayList<Interaction>();
	
	DIP(String filePath) {
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(filePath));
			String line = br.readLine(); // Remove column headings
				
			int interactionsAdded = 0;
			
			while ((line = br.readLine()) != null) {
				String[] fields = line.split("\t");
				int split1 = fields[0].indexOf('|');
				int split2 = fields[1].indexOf('|');
				String id1 = split1 != -1 ? fields[0].substring(0, fields[0].indexOf('|')) : fields[0];
				String id2 = split2 != -1 ? fields[1].substring(0, fields[1].indexOf('|')) : fields[1];
				String posSequence1 = sequences.get(id1);
				String posSequence2 = sequences.get(id2);
				String negSequence = sequences.getShuffled(id2);
				if (posSequence1 != null && posSequence2 != null && negSequence != null) {
					String filteredSeq1 = posSequence1.replaceAll("[^BJOUXZ]", "");
					String filteredSeq2 = posSequence2.replaceAll("[^BJOUXZ]", "");
					if (filteredSeq1.length() == 0 && filteredSeq2.length() == 0) {
						Protein posProtein1 = new Protein(id1, posSequence1);
						Protein posProtein2 = new Protein(id2, posSequence2);
						Protein negProtein = new Protein(id2, negSequence);
						positiveInteractions.add(new PositiveInteraction(posProtein1, posProtein2));
						negativeInteractions.add(new NegativeInteraction(posProtein1, negProtein));
						if (interactionsAdded++ % 1000 == 0) {
							System.out.println("Added " + interactionsAdded + " positive and negative interactions so far");
						}
					}
				}
			}
		} catch (FileNotFoundException ex) {
			System.out.println("FNF exception reading DIP: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("IO exception reading DIP: " + ex.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ex) {
					System.out.println("Error closing reading after reading DIP: " + ex.getMessage());
				}
			}
		}
		
		interactions.addAll(negativeInteractions);
		interactions.addAll(positiveInteractions);
	}
	
	List<Interaction> getInteractions() {
		return interactions;
	}
	
	List<PositiveInteraction> getPositiveInteractions() {
		return positiveInteractions;
	}
	
	List<NegativeInteraction> getNegativeInteractions() {
		return negativeInteractions;
	}
}
