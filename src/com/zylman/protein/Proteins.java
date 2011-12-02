package com.zylman.protein;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Proteins {
	
	Map<String, String> proteins = new HashMap<String, String>();
	Map<String, String> shuffledProteins = new HashMap<String, String>();
	
	Proteins(String filePath) {
		readFastaFileIntoMap(filePath, proteins);
		String shuffledFilePath = "shuffled-" + filePath;
		readFastaFileIntoMap(shuffledFilePath, shuffledProteins);
	}
	
	private void readFastaFileIntoMap(String filePath, Map<String, String> map) {
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(filePath));
			
			String line;
			String idWorkingOn = "";
			String sequenceWorkingOn = "";
			while ((line = br.readLine()) != null) {
				if (line.indexOf(">") != -1) {
					
					if (!idWorkingOn.equals("")) {
						map.put(idWorkingOn, sequenceWorkingOn);
						sequenceWorkingOn = "";
					}
					
					String[] id = line.split(">dip:");
					if (id.length < 2) {
						System.out.println("Split isn't long enough, line is: " + line);
					}
					int split = id[1].indexOf('|');
					idWorkingOn = split != -1 ? id[1].substring(0, split) : id[1];
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
	
	String get(String id) {
		return proteins.get(id);
	}
	
	String getShuffled(String id) {
		return shuffledProteins.get(id);
	}
}
