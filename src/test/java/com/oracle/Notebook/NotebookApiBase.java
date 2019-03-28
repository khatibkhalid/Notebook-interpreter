package com.oracle.Notebook;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import com.oracle.notebook.NotebookApplication;

import io.restassured.module.mockmvc.RestAssuredMockMvc;



@RunWith(SpringRunner.class)
@SpringBootTest(classes = NotebookApplication.class)
@DirtiesContext
@AutoConfigureMessageVerifier
public abstract class NotebookApiBase {

	@Autowired
	WebApplicationContext context;

	@Before
	public void setup() {
		RestAssuredMockMvc.webAppContextSetup(this.context);
	}
	
}