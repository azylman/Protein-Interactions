package com.zylman.protein;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		
		List<Interaction> enhancedDatabase = new ArrayList<Interaction>();
		
		long startTime = new Date().getTime() / 1000;
		
		DIP dip = new DIP("dip20111027.txt");
		
		long endTime = new Date().getTime() / 1000;
		
		System.out.println("Found " + dip.getInteractions().size() + " interactions in " + (endTime - startTime) + " seconds");
		
		for (PositiveInteraction interaction : dip.getInteractions()) {
			enhancedDatabase.add(interaction);
			enhancedDatabase.add(createNegativeInteraction(interaction));
		}
		
		// split up dataset into testing and training set
		
		// write the sets to the disk
		
		// call svm light to create the model
		
		// classify our test set with our learned model
		
		// determine accuracy
		
		// train/test with different parameters/species?
	}
	
	private static NegativeInteraction createNegativeInteraction(PositiveInteraction positiveInteraction) {
		return new NegativeInteraction(shuffle(positiveInteraction.getFirst().getSequence()), positiveInteraction.getSecond().getSequence());
	}
	
	private static String shuffle(String sequence) {
		System.out.print("Shuffling " + sequence + " into ");
		BufferedReader br = null;
		try {
			Process p = new ProcessBuilder("/bin/bash", "-c", "./shufflet 1 2 > out.seq").directory(null).start();
			BufferedWriter bro = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
			bro.write(sequence);
			bro.flush();
			bro.close();
			p.waitFor();
			
			br = new BufferedReader(new FileReader("out.seq"));
			
			String out = "";
			String line;
			while ((line = br.readLine()) != null) {
				out += line;
			}
			
			System.out.println(out);
			
			return out;
		} catch (IOException ex) {
			System.out.println("IO exception shuffling the sequence: " + ex.getMessage());
		} catch (InterruptedException ex) {
			System.out.println("Shufflet interrupted: " + ex.getMessage());
		} finally {			
			if (br != null) {
				try {
					br.close();
				} catch (IOException ex) {
					System.out.println("IO exception closing 'out' sequence: " + ex.getMessage());
				}
			}
		}
		return null;
	}
}
