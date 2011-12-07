package com.zylman.protein;

public class MutableInteger {
	Integer integer;
	
	MutableInteger(int integer) {
		this.integer = integer;
	}
	
	void increment() {
		integer++;
	}
	
	Integer get() {
		return integer;
	}
}
