package com.zylman.protein;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DIP {
	
	public class Interaction {
		String first;
		String second;
		Interaction(String first, String second) {
			this.first = first;
			this.second = second;
		}
	}
	
	List<Interaction> interactions = new ArrayList<Interaction>();
	
	DIP(String filePath) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		
		String line = br.readLine(); // Remove column headings
		while ((line = br.readLine()) != null) {
			String[] fields = line.split("\t");
			interactions.add(new Interaction(fields[0], fields[1]));
		}
	}
}
