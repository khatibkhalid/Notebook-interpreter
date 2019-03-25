package com.oracle.notebook.dto;

import java.io.Serializable;

public class InterpreterResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3022406026038987571L;
	
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}


}
