package com.iot.relay.api.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.iot.relay.api.IotApiApplication;
import com.iot.relay.api.config.JwtTokenUtil;
import com.iot.relay.api.request.QueryRequest;
import com.iot.relay.api.service.JwtUserDetailsService;
import com.iot.relay.api.service.IOTOperationService;
import com.iot.relay.config.MongoConfiguration;

@WebMvcTest(properties = { "spring.profiles.active=test", "local.server.port=27017" })
@AutoConfigureDataMongo
@AutoConfigureMockMvc
public class IOTRestControllerTest {

	@Autowired
	private IOTOperationService sensorOperationService;

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	/**
	 * Test to get minimum value of stored sensor data
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMin() throws Exception {
		when(sensorOperationService.execute(anyString(), any(QueryRequest.class))).thenReturn(BigDecimal.ONE);
		final UserDetails userDetails = new User("ananthan", "password", new ArrayList<>());
		var tokenString = jwtTokenUtil.generateToken(userDetails);
		LinkedMultiValueMap<String, String> requestParams = createRequestParameters("2021-12-23", "2022-08-13", "1",
				"HUMIDITY");
		MvcResult result = mvc
				.perform(get("/query/{1}", "min").header("Authorization", "Bearer " + tokenString).params(requestParams))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertTrue(response.getContentAsString().contains("1"));

	}
	
	/**
	 * Test to get maximum value of stored sensor data
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMaxSuccess() throws Exception {
		when(sensorOperationService.execute(anyString(), any(QueryRequest.class)))
				.thenReturn(new BigDecimal("12345.12345"));
		LinkedMultiValueMap<String, String> requestParams = createRequestParameters("2021-08-13", "2022-08-13", "1",
				"HUMIDITY");
		MvcResult result = mvc
				.perform(get("/query/{1}", "min").with(SecurityMockMvcRequestPostProcessors.jwt()).params(requestParams))
				.andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertTrue(response.getContentAsString().contains("12345.12345"));
		verify(sensorOperationService, times(1)).execute(anyString(), any(QueryRequest.class));
	}

	/**
	 * Test to get average value of stored sensor data
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAverageSuccess() throws Exception {
		when(sensorOperationService.execute(anyString(), any(QueryRequest.class)))
				.thenReturn(new BigDecimal("123.00012345"));
		LinkedMultiValueMap<String, String> requestParams = createRequestParameters("2021-08-13", "2022-08-13", "1",
				"HUMIDITY");

		MvcResult result = mvc.perform(get("/query/{1}", "average").params(requestParams)).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertTrue(response.getContentAsString().contains("123.00012345"));
		verify(sensorOperationService, times(1)).execute(anyString(), any(QueryRequest.class));
	}

	/**
	 * Test to get middle value of stored sensor data
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMedianSuccess() throws Exception {
		when(sensorOperationService.execute(anyString(), any(QueryRequest.class))).thenReturn(new BigDecimal("123456"));
		LinkedMultiValueMap<String, String> requestParams = createRequestParameters("2021-08-01", "2021-08-13", "1",
				"HUMIDITY");
		MvcResult result = mvc.perform(get("/query/{1}", "max").params(requestParams)).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertTrue(response.getContentAsString().contains("123456"));
		verify(sensorOperationService, times(1)).execute(anyString(), any(QueryRequest.class));
	}

	private LinkedMultiValueMap<String, String> createRequestParameters(String startDateTime, String endDateTime,
			String clusterId, String eventType) {
		LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
		if (startDateTime != null)
			requestParams.add("startDateTime", startDateTime);
		if (endDateTime != null)
			requestParams.add("endDateTime", endDateTime);
		if (clusterId != null)
			requestParams.add("clusterId", clusterId);
		if (eventType != null)
			requestParams.add("eventType", eventType);
		return requestParams;
	}

}
