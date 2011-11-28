package com.zylman.protein;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Proteins {
	
	Map<String, FeatureVector> featureVectors = new HashMap<String, FeatureVector>();
	
	Proteins(String filePath) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		
		//String line = br.readLine(); // Remove column headings
		String line;
		while ((line = br.readLine()) != null) {
			String[] id = line.split(">dip:");
			String sequence = br.readLine();
			featureVectors.put(id[0], new FeatureVector(sequence));
		}
	}
}
