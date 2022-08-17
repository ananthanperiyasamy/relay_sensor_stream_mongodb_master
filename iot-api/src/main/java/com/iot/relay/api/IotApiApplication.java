package com.iot.relay.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("com.iot.relay.repository")
public class IotApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotApiApplication.class, args);
	}

}
