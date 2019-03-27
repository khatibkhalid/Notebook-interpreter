package com.oracle.notebook.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PreDestroy;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.oracle.notebook.dto.InterpreterResult;
import com.oracle.notebook.exception.UnsupportedInterpreterException;
import com.oracle.notebook.service.InterpreterService;
import com.oracle.notebook.util.INTERPRETER;


@Service
public class InterpreterServiceImpl implements InterpreterService {
	
	private static final String SCRIPTS_FOLDER_NAME = "scripts";
	private static final Logger LOGGER = LoggerFactory.getLogger(InterpreterServiceImpl.class);

	@Override
	public InterpreterResult executeCodeWithInterpreter(String codeSnippet, INTERPRETER interpreter, String sessionId) throws UnsupportedInterpreterException, IOException {
		switch (interpreter) {
		case PYTHON:
			return executeCodeWithPythonInterpreter(codeSnippet, sessionId);
		default : 
			throw new UnsupportedInterpreterException();
		}
	}

	@Override
	public InterpreterResult executeCodeWithPythonInterpreter(String codeSnippet, String sessionId) throws IOException {
		LOGGER.info("Interpreter: {}, Code snippet: {}",INTERPRETER.PYTHON,codeSnippet);
		
		File scriptsFolder = new File(SCRIPTS_FOLDER_NAME);
		if(!scriptsFolder.exists()) scriptsFolder.mkdirs();
		
		String filePath;
		if(sessionId!=null)
			filePath = "scripts/script"+"_"+sessionId+".py";
		else
			filePath = "scripts/script.py";
		File f = new File(filePath);
		
		InterpreterResult interpreterResult = new InterpreterResult();
		FileWriter fileWriter = new FileWriter(f, true);
		BufferedWriter out = new BufferedWriter(fileWriter);
		out.write(codeSnippet+"\n");
		out.close();
		Process p = Runtime.getRuntime().exec("python "+filePath);
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String ret = in.readLine();
		ret = ret == null ? "" : ret;
		interpreterResult.setResult(ret);
		f.deleteOnExit();
		return interpreterResult;
	}

	@PreDestroy
	public void removeFiles() throws IOException {
		File scriptsFolder = new File(SCRIPTS_FOLDER_NAME);
				FileUtils.cleanDirectory(scriptsFolder);
	}
}
