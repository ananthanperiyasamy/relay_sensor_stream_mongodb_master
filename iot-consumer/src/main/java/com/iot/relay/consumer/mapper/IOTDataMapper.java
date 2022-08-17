package com.iot.relay.consumer.mapper;

import java.time.OffsetDateTime;
import org.springframework.stereotype.Component;
import com.iot.relay.model.IOTData;
import com.iot.relay.model.IOTDataEntity;

/**
 * Mapper class to map sensor data object to mongoDB entity
 * 
 * @author Ananthan Periyasamy
 */
@Component
public class IOTDataMapper {

	/**
	 * Maps iot stream data to entity
	 * 
	 * @param sensor input data
	 * @return the entity to store it in database
	 */
	public IOTDataEntity fromEventToEntity(IOTData iotData) {
		return IOTDataEntity.builder()
				.name(iotData.getName())
				.id(iotData.getId())
				.value(iotData.getValue())
				.type(iotData.getType())
				.clusterId(iotData.getClusterId())
				.timestamp(OffsetDateTime.parse(iotData.getTimestamp())).build();
	}

}