package com.placeholder.company.project.custom;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.placeholder.company.project.Application;

/**
 * Abstract test class for willing candidates to implement their own API tests upon when they feel like it.
 * Please do not attempt to cheat with test results. This will disqualify you instantly.
 */
@RunWith( SpringRunner.class )
@DirtiesContext( classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD )
@SpringBootTest( classes = Application.class )
@WebAppConfiguration
public abstract class AbstractCustomCandidateTest {

	protected static final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;

	@Before
	public void beforeMethod() {
		mockMvc = webAppContextSetup( webApplicationContext ).build();
		assertNotNull( mockMvc );
	}

	protected MockHttpServletResponse postHttpRequest( String url, Object content, HttpHeaders headers ) throws Exception {
		String jsonContent = mapper.writeValueAsString( content );
		return postHttpRequest( url, jsonContent, headers );
	}

	protected MockHttpServletResponse postHttpRequest( String url, String content, HttpHeaders headers ) throws Exception {
		RequestBuilder request = post( url ).content( content ).contentType( MediaType.APPLICATION_JSON_UTF8 ).headers( headers );
		return mockMvc.perform( request ).andReturn().getResponse();
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
