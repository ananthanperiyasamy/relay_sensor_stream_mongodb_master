package com.iot.relay.consumer.mapper;

import java.time.OffsetDateTime;
import org.springframework.stereotype.Component;
import com.iot.relay.model.SensorData;
import com.iot.relay.model.SensorDataEntity;

/**
 * Mapper class to map sensor data object to mongoDB entity
 * 
 * @author Ananthan Periyasamy
 */
@Component
public class SensorDataMapper {

	/**
	 * Maps iot stream data to entity
	 * 
	 * @param sensor input data
	 * @return the entity to store it in database
	 */
	public SensorDataEntity fromEventToEntity(SensorData sensorData) {
		return SensorDataEntity.builder().name(sensorData.getName()).id(sensorData.getId()).value(sensorData.getValue())
				.type(sensorData.getType()).clusterId(sensorData.getClusterId())
				.timestamp(OffsetDateTime.parse(sensorData.getTimestamp())).build();
	}

}