package com.placeholder.company.project.scoring;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.lang.time.StopWatch;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.placeholder.company.project.Application;
import com.placeholder.company.project.scoring.xml.TestCase;
import com.placeholder.company.project.scoring.xml.TestSuite;

/**
 * Must NOT be implemented by the candidate in their own tests!
 */
@RunWith( SpringRunner.class )
@SpringBootTest( classes = Application.class )
@WebAppConfiguration
public abstract class AbstractApplicationScoringTest {

	protected static final ObjectMapper mapper = new ObjectMapper();

	private static AbstractApplicationScoringTest clazz;
	private static StopWatch testSuiteTimer;
	private static StopWatch testCaseTimer;
	private static List<TestCase> testCases;
	private static int failures;
	private static int errors;
	private static int skipped;

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Rule
	public TestWatcher watchman = new TestWatcher() {

		@Override
		protected void failed( Throwable e, Description description ) {
			super.failed( e, description );

			if ( e instanceof AssertionError ) {
				failures++;
			} else {
				errors++;
			}

			generateTestCaseResult( e.getMessage() == null ? e.toString() : e.getMessage(), description );
		}

		@Override
		protected void skipped( org.junit.AssumptionViolatedException e, Description description ) {
			super.skipped( e, description );

			skipped++;

			generateTestCaseResult( "SKIPPED", description );
		}

		@Override
		protected void succeeded( Description description ) {
			super.succeeded( description );
			generateTestCaseResult( null, description );
		}

		private void generateTestCaseResult( String errorMsg, Description description ) {
			testCaseTimer.stop();

			testCases.add( new TestCase.TestCaseBuilder()
					.withName( description.getMethodName() )
					.withClassName( description.getTestClass().getName() )
					.withTime( testCaseTimer.getTime() / 1000d )
					.withFailure( errorMsg )
					.build() );
		}
	};

	@BeforeClass
	public static void beforeClass() {
		testSuiteTimer = new StopWatch();
		testSuiteTimer.start();
		testCases = new ArrayList<>();
		failures = 0;
		errors = 0;
		skipped = 0;
	}

	@Before
	public void beforeMethod() {
		testCaseTimer = new StopWatch();
		testCaseTimer.start();
		clazz = this;
		mockMvc = webAppContextSetup( webApplicationContext ).build();
		assertNotNull( mockMvc );
	}

	@AfterClass
	public static void afterClass() throws IOException {
		testSuiteTimer.stop();

		TestSuite testSuite = new TestSuite.TestSuiteBuilder()
				.withName( clazz.getClass().getName() )
				.withTime( testSuiteTimer.getTime() / 1000d )
				.withTests( testCases.size() )
				.withErrors( errors )
				.withSkipped( skipped )
				.withFailures( failures )
				.withTestCases( testCases )
				.build();

		writeTestResults( testSuite );
	}

	protected <T> T assertHttpResponseOk( MockHttpServletResponse response, Class<T> tClass ) throws Exception {
		return assertHttpResponseErr( response, tClass, HttpStatus.OK );
	}

	protected <T> T assertHttpResponseErr( MockHttpServletResponse response, Class<T> tClass, HttpStatus expectedStatus ) throws Exception {
		Assert.assertEquals( "Incorrect HTTP status: ", expectedStatus.value(), response.getStatus() );
		return mapper.readValue( response.getContentAsString(), tClass );
	}

	protected MockHttpServletResponse postHttpRequest( String url, Object content, HttpHeaders headers ) throws Exception {
		String jsonContent = mapper.writeValueAsString( content );
		return postHttpRequest( url, jsonContent, headers );
	}

	protected MockHttpServletResponse postHttpRequest( String url, String content, HttpHeaders headers ) throws Exception {
		System.out.println( "Request: {url: " + url + ", headers: " + headers.toString() + ", content: " + content + "}" );
		RequestBuilder request = post( url ).content( content ).contentType( MediaType.APPLICATION_JSON_UTF8 ).headers( headers );
		MockHttpServletResponse response = mockMvc.perform( request ).andReturn().getResponse();
		System.out.println( "Response: {status: " + response.getStatus() + ", content: " + response.getContentAsString() + "}" );
		return response;
	}

	private static void writeTestResults( TestSuite testSuite ) throws IOException {
		String PATH = "target/customReports/";
		Path directoryPath = Paths.get( PATH );
		Path filePath = Paths.get( PATH + clazz.getClass().getSimpleName() + "Result.xml" );

		if ( !Files.exists( filePath ) ) {
			if ( !Files.exists( directoryPath ) ) {
				Files.createDirectory( directoryPath );
			}
			Files.createFile( filePath );
		}

		try (BufferedWriter writer = Files.newBufferedWriter( filePath )) {
			writer.write( toXML( testSuite ) );
		}
	}

	private static String toXML( Object data ) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance( data.getClass() );

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );

			StringWriter writer = new StringWriter();
			jaxbMarshaller.marshal( data, writer );

			return writer.toString();
		} catch ( JAXBException e ) {
			throw new RuntimeException( e );
		}
	}
}
