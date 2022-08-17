package com.iot.relay.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.iot.relay.config.MongoConfiguration;
import com.iot.relay.constants.SensorConstant;
import com.iot.relay.model.SensorDataEntity;
import com.iot.relay.repo.SensorDataRepository;

@DataMongoTest(properties = { "spring.profiles.active=test", "local.server.port=27017" })
@AutoConfigureDataMongo
@Import(MongoConfiguration.class)
public class SensorDataRepositoryTest {

	private List<String> mongoIds = new ArrayList<>();

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private SensorDataRepository sensorDataRepository;

	@BeforeEach
	public void initDataBase() {
		createSensorDataEntities().stream().map(entity -> entity.getUuid()).collect(Collectors.toList());
	}

	@AfterEach
	public void deleteDataBase() {
		sensorDataRepository.deleteAllById(mongoIds);
	}

	/**
	 * Method to test
	 * {@link SensorDataRepositoryCustomImpl#findMinValueByClusterIdAndTypeAndTimestamp(Long, String, java.time.OffsetDateTime, java.time.OffsetDateTime)}
	 */

	@Test
	public void findMinValueByClusterIdAndTypeAndTimestamp() {
		BigDecimal result = sensorDataRepository.findMinValueByClusterIdAndTypeAndTimestamp(1l, "HUMIDITY",
				OffsetDateTime.parse("2022-08-01T18:18:55.479998Z"),
				OffsetDateTime.parse("2022-12-01T12:18:55.356996Z"));
		assertEquals(result.toString(), "111111", "result should be 1");

	}

	/**
	 * Method to test
	 * {@link SensorDataRepositoryCustomImpl#findMaxValueByClusterIdAndTypeAndTimestamp(Long, String, java.time.OffsetDateTime, java.time.OffsetDateTime)}
	 */

	@Test
	public void findMaxValueByClusterIdAndTypeAndTimestamp() {
		BigDecimal result = sensorDataRepository.findMaxValueByClusterIdAndTypeAndTimestamp(1l, "HUMIDITY",
				OffsetDateTime.parse("2022-08-01T18:18:55.479998Z"),
				OffsetDateTime.parse("2022-12-01T12:18:55.356996Z"));
		assertEquals(result.toString(), "444444", "result should be 4");

	}

	/**
	 * Method to test
	 * {@link SensorDataRepositoryCustomImpl#findAverageValueByClusterIdAndTypeAndTimestamp(Long, String, java.time.OffsetDateTime, java.time.OffsetDateTime)}
	 */

	@Test
	public void findAverageValueByClusterIdAndTypeAndTimestamp() {
		BigDecimal result = sensorDataRepository.findAvgValueByClusterIdAndTypeAndTimestamp(1l, "HUMIDITY",
				OffsetDateTime.parse("2022-08-01T18:18:55.479998Z"),
				OffsetDateTime.parse("2022-12-01T12:18:55.356996Z"));
		assertEquals(result.toString(), "277778", "result should be 1");

	}

	/**
	 * Method to test
	 * {@link SensorDataRepositoryCustomImpl#findMedianValueByClusterIdAndTypeAndTimestamp(Long, String, java.time.OffsetDateTime, java.time.OffsetDateTime)}
	 */

	@Test
	public void findMedianValueByClusterIdAndTypeAndTimestamp() {
		BigDecimal result = sensorDataRepository.findMedianValueByClusterIdAndTypeAndTimestamp(1l, "HUMIDITY",
				OffsetDateTime.parse("2022-08-01T18:18:55.479998Z"),
				OffsetDateTime.parse("2022-12-01T12:18:55.356996Z"));
		assertEquals(result.toString(), "333333", "result should be 1");

	}

	/**
	 * Create list of mock enities when MongoTemplate
	 * 
	 * @return
	 */
	private List<SensorDataEntity> createSensorDataEntities() {
		List<SensorDataEntity> entities = new ArrayList<>();
		SensorDataEntity entity1 = SensorDataEntity.builder().id(1l).clusterId(1l).type("HUMIDITY")
				.name("Living Room Temp").value(new BigDecimal("111111"))
				.timestamp(OffsetDateTime.parse("2022-08-13T12:18:55.999998Z")).build();

		SensorDataEntity entity2 = SensorDataEntity.builder().id(2l).clusterId(1l).type("HUMIDITY")
				.name("Living Room Temp").value(new BigDecimal("222222"))
				.timestamp(OffsetDateTime.parse("2022-08-13T12:18:55.999998Z")).build();

		SensorDataEntity entity3 = SensorDataEntity.builder().id(3l).clusterId(1l).type("HUMIDITY")
				.name("Living Room Temp").value(new BigDecimal("333333"))
				.timestamp(OffsetDateTime.parse("2022-08-13T12:18:55.999998Z")).build();

		SensorDataEntity entity4 = SensorDataEntity.builder().id(4l).clusterId(1l).type("HUMIDITY")
				.name("Living Room Temp").value(new BigDecimal("444444"))
				.timestamp(OffsetDateTime.parse("2022-08-13T12:18:55.999998Z")).build();
		entities.add(entity1);
		entities.add(entity2);
		entities.add(entity3);
		entities.add(entity4);
		mongoTemplate.save(entity1, SensorConstant.SENSOR_COLLECTION_NAME);
		mongoTemplate.save(entity2, SensorConstant.SENSOR_COLLECTION_NAME);
		mongoTemplate.save(entity3, SensorConstant.SENSOR_COLLECTION_NAME);
		mongoTemplate.save(entity4, SensorConstant.SENSOR_COLLECTION_NAME);
		return entities;
	}
}