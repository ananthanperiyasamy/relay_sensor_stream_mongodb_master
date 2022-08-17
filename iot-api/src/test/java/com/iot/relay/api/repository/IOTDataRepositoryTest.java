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
import com.iot.relay.constants.IOTConstant;
import com.iot.relay.model.IOTDataEntity;
import com.iot.relay.repository.IOTDataRepository;

@DataMongoTest(properties = { "spring.profiles.active=test", "local.server.port=27017" })
@AutoConfigureDataMongo
@Import(MongoConfiguration.class)
public class IOTDataRepositoryTest {

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private IOTDataRepository iotDataRepository;

	@BeforeEach
	public void initDataBase() {
		createSensorDataEntities();
	}

	@AfterEach
	public void deleteDataBase() {
		iotDataRepository.deleteAll();
	}

	/**
	 * Method to test
	 * {@link IOTDataRepositoryCustomImpl#fetchMinimumValue(Long, String, java.time.OffsetDateTime, java.time.OffsetDateTime)}
	 */
	@Test
	public void fetchMinimumValue() {
		BigDecimal result = iotDataRepository.fetchMinimumValue(1l, "HUMIDITY",
				OffsetDateTime.parse("2022-08-01T18:18:55.479998Z"),
				OffsetDateTime.parse("2022-12-01T12:18:55.356996Z"),IOTConstant.SENSOR_OPERATION_MINIMUM);
		assertEquals(result.toString(), "111111", "result should be 1");

	}

	/**
	 * Method to test
	 * {@link IOTDataRepositoryCustomImpl#fetchMaximumValue(Long, String, java.time.OffsetDateTime, java.time.OffsetDateTime)}
	 */

	@Test
	public void fetchMaximumValue() {
		BigDecimal result = iotDataRepository.fetchMaximumValue(1l, "HUMIDITY",
				OffsetDateTime.parse("2022-08-01T18:18:55.479998Z"),
				OffsetDateTime.parse("2022-12-01T12:18:55.356996Z"),IOTConstant.SENSOR_OPERATION_MAXIMUM);
		assertEquals(result.toString(), "444444", "result should be 4");

	}

	/**
	 * Method to test
	 * {@link IOTDataRepositoryCustomImpl#fetchAverageValue(Long, String, java.time.OffsetDateTime, java.time.OffsetDateTime)}
	 */

	@Test
	public void fetchAverageValue() {
		BigDecimal result = iotDataRepository.fetchAverageValue(1l, "HUMIDITY",
				OffsetDateTime.parse("2022-08-01T18:18:55.479998Z"),
				OffsetDateTime.parse("2022-12-01T12:18:55.356996Z"),IOTConstant.SENSOR_OPERATION_AVERAGE);
		assertEquals(result.toString(), "277777.5");

	}

	/**
	 * Method to test
	 * {@link IOTDataRepositoryCustomImpl#fetchMedianValue(Long, String, java.time.OffsetDateTime, java.time.OffsetDateTime)}
	 */

	@Test
	public void fetchMedianValue() {
		BigDecimal result = iotDataRepository.fetchMedianValue(1l, "HUMIDITY",
				OffsetDateTime.parse("2022-08-01T18:18:55.479998Z"),
				OffsetDateTime.parse("2022-12-01T12:18:55.356996Z"),IOTConstant.SENSOR_OPERATION_MEDIAN);
		assertEquals(result.toString(), "333333");

	}

	/**
	 * Create list of mock enities when MongoTemplate
	 * 
	 * @return
	 */
	private List<IOTDataEntity> createSensorDataEntities() {
		List<IOTDataEntity> entities = new ArrayList<>();
		IOTDataEntity entity1 = IOTDataEntity.builder().id(1l).clusterId(1l).type("HUMIDITY")
				.name("Living Room Temp").value(new BigDecimal("111111"))
				.timestamp(OffsetDateTime.parse("2022-08-13T12:18:55.999998Z")).build();

		IOTDataEntity entity2 = IOTDataEntity.builder().id(2l).clusterId(1l).type("HUMIDITY")
				.name("Living Room Temp").value(new BigDecimal("222222"))
				.timestamp(OffsetDateTime.parse("2022-08-13T12:18:55.999998Z")).build();

		IOTDataEntity entity3 = IOTDataEntity.builder().id(3l).clusterId(1l).type("HUMIDITY")
				.name("Living Room Temp").value(new BigDecimal("333333"))
				.timestamp(OffsetDateTime.parse("2022-08-13T12:18:55.999998Z")).build();

		IOTDataEntity entity4 = IOTDataEntity.builder().id(4l).clusterId(1l).type("HUMIDITY")
				.name("Living Room Temp").value(new BigDecimal("444444"))
				.timestamp(OffsetDateTime.parse("2022-08-13T12:18:55.999998Z")).build();
		entities.add(entity1);
		entities.add(entity2);
		entities.add(entity3);
		entities.add(entity4);
		mongoTemplate.save(entity1, IOTConstant.SENSOR_COLLECTION_NAME);
		mongoTemplate.save(entity2, IOTConstant.SENSOR_COLLECTION_NAME);
		mongoTemplate.save(entity3, IOTConstant.SENSOR_COLLECTION_NAME);
		mongoTemplate.save(entity4, IOTConstant.SENSOR_COLLECTION_NAME);
		return entities;
	}
}