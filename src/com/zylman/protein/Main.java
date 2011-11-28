package com.zylman.protein;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		DIP dip = new DIP("dip20111027.txt");
		Proteins sequences = new Proteins("fasta20101010.seq");
	}
}
