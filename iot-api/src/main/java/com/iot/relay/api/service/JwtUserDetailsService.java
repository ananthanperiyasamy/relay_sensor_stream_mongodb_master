package com.iot.relay.api.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.iot.relay.model.UserData;
import com.iot.relay.model.UserDataEntity;
import com.iot.relay.repository.UserDataRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserDataRepository userDataRepository;

	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDataEntity user = userDataRepository.findUserByName(username);
		if (user == null)
			throw new UsernameNotFoundException("User not found : " + username);
		return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
	}

	public void save(UserData userData) {
		userDataRepository.save(UserDataEntity.builder().username(userData.getUsername())
				.password(bcryptEncoder.encode(userData.getPassword())).build());
	}

}