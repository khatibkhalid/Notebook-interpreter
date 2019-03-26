package com.oracle.notebook.service;

import java.io.IOException;

import com.oracle.notebook.dto.InterpreterResult;
import com.oracle.notebook.exception.UnsupportedInterpreterException;
import com.oracle.notebook.util.INTERPRETER;

public interface InterpreterService {

	public InterpreterResult executeCodeWithInterpreter(String codeSnippet, INTERPRETER interpreter)  throws UnsupportedInterpreterException, IOException ;
	
	public InterpreterResult executeCodeWithPythonInterpreter(String codeSnippet) throws IOException ;
}
