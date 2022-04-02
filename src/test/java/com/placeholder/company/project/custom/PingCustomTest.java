package com.placeholder.company.project.custom;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

public class PingCustomTest extends AbstractCustomCandidateTest {

	@Test
	public void testPing() throws Exception {
		MockHttpServletResponse response = postHttpRequest( "/ping", "", new HttpHeaders() );

		Assert.assertEquals( HttpStatus.OK.value(), response.getStatus() );
		Assert.assertEquals( "Pong!", response.getContentAsString() );
	}
}
