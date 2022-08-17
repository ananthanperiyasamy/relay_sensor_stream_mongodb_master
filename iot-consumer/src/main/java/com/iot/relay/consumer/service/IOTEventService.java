package com.iot.relay.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.iot.relay.consumer.mapper.IOTDataMapper;
import com.iot.relay.model.IOTData;
import com.iot.relay.repository.IOTDataRepository;

@Service
public class IOTEventService {
	
	@Autowired
	private IOTDataMapper iotDataMapper;
	@Autowired
	private IOTDataRepository iotDataRepository;

	/**
	 * Save the sensor data in repository
	 * 
	 * @param sensorDataEntity
	 */
	public void save(IOTData iotData) {
		iotDataRepository.save(iotDataMapper.fromEventToEntity(iotData));
	}

}
