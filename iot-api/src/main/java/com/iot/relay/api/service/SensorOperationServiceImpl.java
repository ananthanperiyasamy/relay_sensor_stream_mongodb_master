package com.iot.relay.api.service;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.iot.relay.api.request.QueryRequest;
import com.iot.relay.api.utils.ApplicationUtils;
import com.iot.relay.constants.SensorConstant;
import com.iot.relay.exception.SensorCustomException;
import com.iot.relay.repo.SensorDataRepository;

@Service
public class SensorOperationServiceImpl implements SensorOperationService {

	@Autowired
	private SensorDataRepository sensorDataRepository;

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
		if (SensorConstant.SENSOR_OPERATION_AVERAGE.equalsIgnoreCase(operationType))
			return fetchAverage(request);
		else if (SensorConstant.SENSOR_OPERATION_MAXIMUM.equalsIgnoreCase(operationType))
			return fetchMaximum(request);
		else if (SensorConstant.SENSOR_OPERATION_MEDIAN.equalsIgnoreCase(operationType))
			return fetchMedian(request);
		else if (SensorConstant.SENSOR_OPERATION_MINIMUM.equalsIgnoreCase(operationType))
			return fetchMinimum(request);
		throw new SensorCustomException("Unsupported operation. Type = " + operationType);
	}

	@Override
	public BigDecimal fetchMinimum(QueryRequest request) throws DataAccessException {
		return sensorDataRepository.findMinValueByClusterIdAndTypeAndTimestamp(request.getClusterId(),
				request.getEventType(), ApplicationUtils.convertStringToOffsetDateTime(request.getStartDateTime()),
				ApplicationUtils.convertStringToOffsetDateTime(request.getEndDateTime()));
	}

	@Override
	public BigDecimal fetchAverage(QueryRequest request) throws DataAccessException {
		return sensorDataRepository.findAvgValueByClusterIdAndTypeAndTimestamp(request.getClusterId(),
				request.getEventType(), ApplicationUtils.convertStringToOffsetDateTime(request.getStartDateTime()),
				ApplicationUtils.convertStringToOffsetDateTime(request.getEndDateTime()));
	}

	@Override
	public BigDecimal fetchMaximum(QueryRequest request) throws DataAccessException {
		return sensorDataRepository.findMaxValueByClusterIdAndTypeAndTimestamp(request.getClusterId(),
				request.getEventType(), ApplicationUtils.convertStringToOffsetDateTime(request.getStartDateTime()),
				ApplicationUtils.convertStringToOffsetDateTime(request.getEndDateTime()));
	}

	@Override
	public BigDecimal fetchMedian(QueryRequest request) throws DataAccessException {
		return sensorDataRepository.findMedianValueByClusterIdAndTypeAndTimestamp(request.getClusterId(),
				request.getEventType(), ApplicationUtils.convertStringToOffsetDateTime(request.getStartDateTime()),
				ApplicationUtils.convertStringToOffsetDateTime(request.getEndDateTime()));
	}

}
