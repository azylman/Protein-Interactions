package com.zylman.protein;

@SuppressWarnings("serial")
public class ProteinException extends Exception {

	private Exception underlyingException = null;
	private String message = null;
	
	public ProteinException(Exception underlyingException, String message) {
		this.underlyingException = underlyingException;
		this.message = message;
	}
	
	public ProteinException(Exception underlyingException) {
		this(underlyingException, null);
	}
	
	public ProteinException(String message) {
		this(null, message);
	}

	public Exception getUnderlyingException() {
		return underlyingException;
	}
	
	public String getMessage() {
		return message;
	}
}
