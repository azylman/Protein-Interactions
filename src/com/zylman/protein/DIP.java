package com.zylman.protein;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class DIP {
	DIP(String filePath) throws FileNotFoundException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		
		String line = br.readLine();
		System.out.println(line);
		while ((line = br.readLine()) != null)   {
			System.out.println(line);
		}
	}
}
