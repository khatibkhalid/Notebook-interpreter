package com.oracle.Notebook.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.oracle.notebook.exception.UnsupportedInterpreterException;
import com.oracle.notebook.service.InterpreterService;
import com.oracle.notebook.service.impl.InterpreterServiceImpl;
import com.oracle.notebook.util.INTERPRETER;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes= {InterpreterServiceImpl.class})
public class InterpreterServiceTests {

	@Autowired
	private InterpreterService interpreterService;
	
	@Test
	public void testBaseInterpreterService() throws UnsupportedInterpreterException, IOException, TimeoutException {
		assertEquals("2", interpreterService.executeCodeWithInterpreter("print(1+1)", INTERPRETER.PYTHON, null).getResult());
	}

	
	@Test
	public void testInterpreterServiceWithSession() throws UnsupportedInterpreterException, IOException, TimeoutException {
		assertEquals("", interpreterService.executeCodeWithInterpreter("a=2", INTERPRETER.PYTHON, "khalid123").getResult());
		assertEquals("4", interpreterService.executeCodeWithInterpreter("print (a+2)", INTERPRETER.PYTHON, "khalid123").getResult());
	}
}
