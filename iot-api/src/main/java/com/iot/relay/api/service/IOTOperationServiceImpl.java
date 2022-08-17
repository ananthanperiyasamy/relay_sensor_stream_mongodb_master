package com.iot.relay.api.service;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.iot.relay.api.request.QueryRequest;
import com.iot.relay.api.utils.ApplicationUtils;
import com.iot.relay.constants.IOTConstant;
import com.iot.relay.exception.SensorCustomException;
import com.iot.relay.repository.IOTDataRepository;

@Service
public class IOTOperationServiceImpl implements IOTOperationService {

	@Autowired
	private IOTDataRepository sensorDataRepository;

	/**
	 * Execute the specified operation type
	 * 
	 * @param operationType the type of operation which needs to be invoked
	 * @param request       the additional request parameters to execute the
	 *                      operation
	 * @return the operation result
	 * @throws UpsupportedOperationException when operation type is not found
	 */
	@Override
	public BigDecimal execute(String operationType, QueryRequest request) {
		if (IOTConstant.SENSOR_OPERATION_AVERAGE.equalsIgnoreCase(operationType)) {
			return fetchAverageValue(request,IOTConstant.SENSOR_OPERATION_AVERAGE);
		}
		else if (IOTConstant.SENSOR_OPERATION_MAXIMUM.equalsIgnoreCase(operationType)) {
			return fetchMaximumValue(request,IOTConstant.SENSOR_OPERATION_MAXIMUM);
		}
		else if (IOTConstant.SENSOR_OPERATION_MEDIAN.equalsIgnoreCase(operationType)) {
			return fetchMedianValue(request,IOTConstant.SENSOR_OPERATION_MEDIAN);
		}
		else if (IOTConstant.SENSOR_OPERATION_MINIMUM.equalsIgnoreCase(operationType)) {
			return fetchMinimumValue(request,IOTConstant.SENSOR_OPERATION_MINIMUM);
		}
		throw new SensorCustomException("Unsupported operation. Type = " + operationType);
	}

	@Override
	public BigDecimal fetchMinimumValue(QueryRequest request,String operationType) throws DataAccessException {
		return sensorDataRepository.fetchMinimumValue(
				request.getClusterId(),
				request.getEventType(), 
				ApplicationUtils.convertStringToOffsetDateTime(request.getStartDateTime()),
				ApplicationUtils.convertStringToOffsetDateTime(request.getEndDateTime()),
				operationType);
	}

	@Override
	public BigDecimal fetchAverageValue(QueryRequest request,String operationType) throws DataAccessException {
		return sensorDataRepository.fetchAverageValue(
				request.getClusterId(),
				request.getEventType(), 
				ApplicationUtils.convertStringToOffsetDateTime(request.getStartDateTime()),
				ApplicationUtils.convertStringToOffsetDateTime(request.getEndDateTime()),
				operationType);
	}

	@Override
	public BigDecimal fetchMaximumValue(QueryRequest request,String operationType) throws DataAccessException {
		return sensorDataRepository.fetchMaximumValue(
				request.getClusterId(),
				request.getEventType(), 
				ApplicationUtils.convertStringToOffsetDateTime(request.getStartDateTime()),
				ApplicationUtils.convertStringToOffsetDateTime(request.getEndDateTime()),
				operationType);
	}

	@Override
	public BigDecimal fetchMedianValue(QueryRequest request,String operationType) throws DataAccessException {
		return sensorDataRepository.fetchMedianValue(
				request.getClusterId(),
				request.getEventType(), 
				ApplicationUtils.convertStringToOffsetDateTime(request.getStartDateTime()),
				ApplicationUtils.convertStringToOffsetDateTime(request.getEndDateTime()),
				operationType);
	}

}
