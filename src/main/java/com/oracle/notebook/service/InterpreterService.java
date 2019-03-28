package com.oracle.notebook.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.oracle.notebook.dto.InterpreterResult;
import com.oracle.notebook.exception.UnsupportedInterpreterException;
import com.oracle.notebook.util.INTERPRETER;

public interface InterpreterService {

	public InterpreterResult executeCodeWithInterpreter(String codeSnippet, INTERPRETER interpreter, String sessionId)  throws UnsupportedInterpreterException, IOException, TimeoutException ;
	
	public InterpreterResult executeCodeWithPythonInterpreter(String codeSnippet, String sessionId) throws IOException, TimeoutException ;
}
