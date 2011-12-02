package com.zylman.protein;

import java.util.Date;

public class Main {
	public static void main(String[] args) {
		
		long startTime = new Date().getTime() / 1000;
		
		DIP dip = new DIP("dip20111027.txt");
		
		long endTime = new Date().getTime() / 1000;
		
		System.out.println("Found " + dip.getPositiveInteractions().size() + " positive interactions and "
				+ dip.getNegativeInteractions().size() + " negative interactions "
				+ "in " + (endTime - startTime) + " seconds");
	}
}
