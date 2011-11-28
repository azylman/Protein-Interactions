package com.zylman.protein;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DIP {
	
	private static final Proteins sequences = new Proteins("fasta20101010.seq");
	
	private List<PositiveInteraction> interactions = new ArrayList<PositiveInteraction>();
	
	DIP(String filePath) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		
		String line = br.readLine(); // Remove column headings
		while ((line = br.readLine()) != null) {
			String[] fields = line.split("\t");
			interactions.add(new PositiveInteraction(sequences.get(fields[0]), sequences.get(fields[1])));
		}
	}
	
	List<PositiveInteraction> getPositiveInteractions() {
		return interactions;
	}
}
