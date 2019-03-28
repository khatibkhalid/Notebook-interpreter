package com.oracle.notebook.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
	
	private static final int TIMEOUT_IN_MILLIS = 2000;
	private static final String SCRIPTS_FOLDER = "scripts/";
	private static final String SCRIPTS_FOLDER_NAME = "scripts";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InterpreterServiceImpl.class);

	@Override
	public InterpreterResult executeCodeWithInterpreter(String codeSnippet, INTERPRETER interpreter, String sessionId) throws UnsupportedInterpreterException, IOException, TimeoutException {
		switch (interpreter) {
		case PYTHON:
			return executeCodeWithPythonInterpreter(codeSnippet, sessionId);
		default : 
			throw new UnsupportedInterpreterException();
		}
	}

	@Override
	public InterpreterResult executeCodeWithPythonInterpreter(String codeSnippet, String sessionId) throws IOException, TimeoutException {
		LOGGER.info("Interpreter: {}, Code snippet: {}",INTERPRETER.PYTHON,codeSnippet);
		
		InterpreterResult interpreterResult = new InterpreterResult();
		String result;
		
		File scriptsFolder = new File(SCRIPTS_FOLDER_NAME);
		if(!scriptsFolder.exists()) scriptsFolder.mkdirs();
		
		String filePath;
		if(sessionId!=null)
			filePath = SCRIPTS_FOLDER+"script"+"_"+sessionId+".py";
		else
			filePath = SCRIPTS_FOLDER+"script.py";
		File f = new File(filePath);
		
		FileWriter fileWriter = new FileWriter(f, true);
		BufferedWriter out = new BufferedWriter(fileWriter);
		out.write(codeSnippet+"\n");
		out.close();
		
		Process p = Runtime.getRuntime().exec("python "+filePath);
		
		try {
			if(!p.waitFor(TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)) {
				throw new TimeoutException("Execution timed out while waiting for the process to be executed !");
			}
		} catch (InterruptedException e) {
			LOGGER.error("Error while waiting for executing Python process, error description: {}",e);
			return interpreterResult;
		}
		
		InputStream inputStream = p.getInputStream();
		InputStream errorStream = p.getErrorStream();
		
		String inputStreamString = getStringFromInputStream(inputStream);
		String errorStreamString = getStringFromInputStream(errorStream);

		if(errorStreamString == null || "".equals(errorStreamString)) {
			result = inputStreamString == null ? "" : inputStreamString;
		} else {
			result = errorStreamString;
		}

		interpreterResult.setResult(result);
		return interpreterResult;
	}
	
	private String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			LOGGER.error("error while reading from inputStream: {}",e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	@PreDestroy
	private void removeFiles() throws IOException {
		File scriptsFolder = new File(SCRIPTS_FOLDER_NAME);
				FileUtils.cleanDirectory(scriptsFolder);
	}
}
