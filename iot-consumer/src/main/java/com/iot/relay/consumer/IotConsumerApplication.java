package com.iot.relay.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories("com.iot.relay.repository")
public class IotConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotConsumerApplication.class, args);
	}

}
