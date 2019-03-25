package com.oracle.notebook.dto;

import java.io.Serializable;

public class InterpreterRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1919403416480907544L;
	
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
