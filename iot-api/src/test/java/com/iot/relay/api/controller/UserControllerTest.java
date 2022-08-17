package com.iot.relay.api.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.relay.api.service.JwtUserDetailsService;
import com.iot.relay.model.UserData;

@WebMvcTest
@AutoConfigureDataMongo
public class UserControllerTest {

	@MockBean
	private JwtUserDetailsService userDetailsService;

	@Autowired
	private MockMvc mvc;

	/**
	 * Test the create new user
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "ananthan", password = "password")
	public void createNewUser() throws Exception {
		UserData user = createTestUser();
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(user);

		MvcResult result = mvc.perform(post("/create/user").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isOk()).andReturn();
		MockHttpServletResponse response = result.getResponse();
		assertTrue(response.getContentAsString().contains("User created successfully"));

	}

	private UserData createTestUser() {
		UserData user = new UserData();
		user.setUsername("admin");
		user.setPassword("password");
		return user;
	}


}
