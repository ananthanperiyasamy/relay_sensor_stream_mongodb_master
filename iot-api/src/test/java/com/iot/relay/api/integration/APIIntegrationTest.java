package com.iot.relay.api.integration;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.iot.relay.api.IotApiApplication;
import com.iot.relay.api.config.JwtRequestFilter;
import com.iot.relay.api.config.JwtTokenUtil;
import com.iot.relay.api.request.QueryRequest;
import com.iot.relay.model.IOTDataEntity;
import com.iot.relay.repository.IOTDataRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
		"spring.profiles.active=test" }, classes = IotApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureDataMongo
public class APIIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	TestRestTemplate restTemplate;

	@Autowired
	private IOTDataRepository iotDataRepository;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Test
	public void testMinWithClusterIdAndTypeSuccess() {
		initTable(iotDataRepository);
		QueryRequest request = new QueryRequest();
		request.setClusterId(1l);
		request.setEventType("TEMPERATURE");
		request.setStartDateTime("2021-12-23");
		request.setEndDateTime("2023-12-23");

		final UserDetails userDetails = new User("ananthan", "password", new ArrayList<>());
		var tokenString = "Bearer " + jwtTokenUtil.generateToken(userDetails);
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", tokenString);
        ResponseEntity<Object> entity = new TestRestTemplate().exchange(
        		createURLWithPort("/query/min/", request), HttpMethod.GET, new HttpEntity<Object>(headers),
                Object.class);
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
        Assertions.assertEquals(1, entity.getBody());
	}

	@Test
	public void testMaxWithClusterIdAndTypeSuccess() {
		initTable(iotDataRepository);
		QueryRequest request = new QueryRequest();
		request.setClusterId(1l);
		request.setEventType("TEMPERATURE");
		request.setStartDateTime("2021-12-23");
		request.setEndDateTime("2023-12-23");

		final UserDetails userDetails = new User("ananthan", "password", new ArrayList<>());
		var tokenString = "Bearer " + jwtTokenUtil.generateToken(userDetails);
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", tokenString);
        ResponseEntity<Object> entity = new TestRestTemplate().exchange(
        		createURLWithPort("/query/max/", request), HttpMethod.GET, new HttpEntity<Object>(headers),
                Object.class);
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
        Assertions.assertEquals(444444, entity.getBody());
	}
	
	@Test
	public void testMedianWithClusterIdAndTypeSuccess() {
		initTable(iotDataRepository);
		QueryRequest request = new QueryRequest();
		request.setClusterId(1l);
		request.setEventType("TEMPERATURE");
		request.setStartDateTime("2021-12-23");
		request.setEndDateTime("2023-12-23");

		final UserDetails userDetails = new User("ananthan", "password", new ArrayList<>());
		var tokenString = "Bearer " + jwtTokenUtil.generateToken(userDetails);
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", tokenString);
        ResponseEntity<Object> entity = new TestRestTemplate().exchange(
        		createURLWithPort("/query/median/", request), HttpMethod.GET, new HttpEntity<Object>(headers),
                Object.class);
        Assertions.assertEquals(HttpStatus.OK, entity.getStatusCode());
        Assertions.assertEquals(222222, entity.getBody());
	}
	
	private String createURLWithPort(String uri, QueryRequest request) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://localhost:" + port + uri)
				.queryParam("startDateTime", request.getStartDateTime())
				.queryParam("endDateTime", request.getEndDateTime()).queryParam("clusterId", request.getClusterId())
				.queryParam("eventType", request.getEventType());
		return builder.build().toUriString();
	}

	/**
	 * Fill table with dummy data to query
	 * 
	 * @param sensorDataRepository repository where data needs to be stored
	 */
	public static void initTable(IOTDataRepository iotDataRepository) {
		IOTDataEntity entity1 = IOTDataEntity.builder().id(1l).clusterId(1l).type("TEMPERATURE")
				.name("Living Room Temp").value(new BigDecimal("111111"))
				.timestamp(OffsetDateTime.parse("2022-08-14T18:18:55.479998Z")).build();
		iotDataRepository.save(entity1);

		IOTDataEntity entity2 = IOTDataEntity.builder().id(1l).clusterId(1l).type("TEMPERATURE")
				.name("Living Room Temp").value(new BigDecimal("222222"))
				.timestamp(OffsetDateTime.parse("2022-08-14T18:18:57.479998Z")).build();
		iotDataRepository.save(entity2);

		IOTDataEntity entity3 = IOTDataEntity.builder().id(1l).clusterId(1l).type("TEMPERATURE")
				.name("Living Room Temp").value(new BigDecimal("333333"))
				.timestamp(OffsetDateTime.parse("2022-08-14T18:18:58.479998Z")).build();
		iotDataRepository.save(entity3);

		IOTDataEntity entity4 = IOTDataEntity.builder().id(1l).clusterId(1l).type("TEMPERATURE")
				.name("Living Room Temp").value(new BigDecimal("444444"))
				.timestamp(OffsetDateTime.parse("2022-08-14T18:18:59.479998Z")).build();
		iotDataRepository.save(entity4);
	}

}
