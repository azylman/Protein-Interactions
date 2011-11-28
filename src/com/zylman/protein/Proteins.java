package com.zylman.protein;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Proteins {
	
	Map<String, String> proteins = new HashMap<String, String>();
	
	Proteins(String filePath) {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			
			try {
				//String line = br.readLine(); // Remove column headings
				String line;
				while ((line = br.readLine()) != null) {
					String[] id = line.split(">dip:");
					String sequence = br.readLine();
					proteins.put(id[0], sequence);
				}
			} catch (IOException ex) {
				// error handling
			}
		} catch (FileNotFoundException ex) {
			// error handling
		}
	}
	
	String get(String id) {
		return proteins.get(id);
	}
}
