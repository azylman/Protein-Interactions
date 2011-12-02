package com.zylman.protein;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Proteins {
	
	Map<String, Protein> realProteins = new HashMap<String, Protein>();
	Map<String, Protein> shuffledProteins = new HashMap<String, Protein>();
	Map<String, Protein> proteins = new HashMap<String, Protein>();
	
	Proteins(String filePath) {
		readFastaFileIntoMap(filePath, realProteins, false);
		String shuffledFilePath = "shuffled-" + filePath;
		readFastaFileIntoMap(shuffledFilePath, shuffledProteins, true);
		proteins.putAll(realProteins);
		proteins.putAll(shuffledProteins);
	}
	
	private void readFastaFileIntoMap(String filePath, Map<String, Protein> map, boolean shuffled) {
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(filePath));
			
			String line;
			String idWorkingOn = "";
			String sequenceWorkingOn = "";
			while ((line = br.readLine()) != null) {
				if (line.indexOf(">") != -1) {
					
					if (!idWorkingOn.equals("")) {
						String filteredSequence = sequenceWorkingOn.replaceAll("[^BJOUXZ]", "");
						if (filteredSequence.length() == 0) map.put(idWorkingOn, new Protein(idWorkingOn, sequenceWorkingOn));
						sequenceWorkingOn = "";
					}
					
					String[] id = line.split(">dip:");
					if (id.length < 2) {
						System.out.println("Split isn't long enough, line is: " + line);
					}
					int split = id[1].indexOf('|');
					idWorkingOn = split != -1 ? id[1].substring(0, split) : id[1];
					if (shuffled) idWorkingOn += "-S";
				} else {
					sequenceWorkingOn += line;
				}
			}
		} catch (FileNotFoundException ex) {
			System.out.println("FNF exception reading proteins: " + ex.getMessage());
		} catch (IOException ex) {
			System.out.println("IO exception reading proteins: " + ex.getMessage());
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ex) {
					System.out.println("Error closing reading after reading DIP: " + ex.getMessage());
				}
			}
		}
	}
	
	Protein get(String id) {
		return proteins.get(id);
	}
	
	Collection<Protein> getProteins() {
		return proteins.values();
	}
}
