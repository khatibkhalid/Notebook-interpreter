package com.oracle.notebook.exception;

public class UnsupportedInterpreterException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5534174061345122077L;
	
	private static final String DEFAULT_MESSAGE = "Unsupported interpreter !";
	
	public UnsupportedInterpreterException() {
		super(DEFAULT_MESSAGE);
	}
	
	public UnsupportedInterpreterException(String message) {
		super(message);
	}

}
