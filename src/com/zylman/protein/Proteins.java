package com.zylman.protein;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Proteins {
	
	List<FeatureVector> featureVectors = new ArrayList<FeatureVector>();
	
	Proteins(String filePath) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		
		//String line = br.readLine(); // Remove column headings
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}
}
