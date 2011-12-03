package com.zylman.protein;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DIP {
	
	private List<PositiveInteraction> positiveInteractions = new ArrayList<PositiveInteraction>();
	private List<NegativeInteraction> negativeInteractions = new ArrayList<NegativeInteraction>();
	private List<Interaction> interactions = new ArrayList<Interaction>();
	
	DIP(String filePath, Proteins sequences) {
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
				Protein posProtein1 = sequences.get(id1);
				Protein posProtein2 = sequences.get(id2);
				String negId = id2 + "-S";
				Protein negProtein = sequences.get(negId);
				if (posProtein1 != null && posProtein2 != null && negProtein != null) {
					positiveInteractions.add(new PositiveInteraction(posProtein1.getId(), posProtein2.getId()));
					negativeInteractions.add(new NegativeInteraction(posProtein1.getId(), negProtein.getId()));
					if (interactionsAdded++ % 1000 == 0) {
						System.out.println("Added " + interactionsAdded + " positive and negative interactions so far");
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
