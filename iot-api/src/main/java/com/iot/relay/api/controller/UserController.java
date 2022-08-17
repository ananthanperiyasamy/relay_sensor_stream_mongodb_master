package com.iot.relay.api.controller;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.iot.relay.api.config.JwtTokenUtil;
import com.iot.relay.api.model.JwtRequest;
import com.iot.relay.api.model.JwtResponse;
import com.iot.relay.api.service.JwtUserDetailsService;
import com.iot.relay.model.UserData;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Controller to create new user and create jwt token
 * 
 * @author Ananthan Periyasamy
 */
@RestController
@CrossOrigin
public class UserController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsService userDetailsService;

	@Operation(summary = "Get JWT token.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = " Success and return token."),
			@ApiResponse(responseCode = "400", description = "Invalid input. Login data are not correct format."),
			@ApiResponse(responseCode = "500", description = "User not found") })
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	@Operation(summary = "Create new user")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = " User created sccessfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input. Login data are not correct format.") })
	@RequestMapping(value = "/create/user", method = RequestMethod.POST)
	public String createNewUser(@RequestBody UserData userData) throws Exception {
		userDetailsService.save(userData);
		return "User created successfully";
	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
