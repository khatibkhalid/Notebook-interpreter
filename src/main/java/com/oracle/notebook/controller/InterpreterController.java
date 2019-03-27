package com.oracle.notebook.controller;

import java.io.IOException;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.notebook.dto.InterpreterRequest;
import com.oracle.notebook.dto.InterpreterResult;
import com.oracle.notebook.exception.UnsupportedInterpreterException;
import com.oracle.notebook.service.InterpreterService;
import com.oracle.notebook.util.INTERPRETER;

@RestController
@RequestMapping("/interpreter")
public class InterpreterController {
	
	private static final String CODE_DELIMITER = " ";

	private static final String INTERPRETER_DELIMITER = "%";
	
	@Autowired
	private InterpreterService interpreterService;

	
	@PostMapping("/execute")
	public InterpreterResult execute(@RequestHeader(required=false, name="sessionId") String sessionId, @RequestBody InterpreterRequest interpreterRequest) throws UnsupportedInterpreterException, IOException {
		String requestCode = interpreterRequest.getCode();
		String requestCodeSnippet = requestCode.substring(requestCode.indexOf(CODE_DELIMITER)+1);
		String requestInterpreter = requestCode.substring(requestCode.indexOf(INTERPRETER_DELIMITER)+1, requestCode.indexOf(CODE_DELIMITER));
		if(EnumUtils.isValidEnum(INTERPRETER.class, requestInterpreter.toUpperCase())) {
			return interpreterService.executeCodeWithInterpreter(requestCodeSnippet, EnumUtils.getEnum(INTERPRETER.class, requestInterpreter.toUpperCase()),sessionId);
		} else {
			throw new UnsupportedInterpreterException();
		}
	}
	
}
