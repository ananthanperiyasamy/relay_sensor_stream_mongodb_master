package com.iot.relay.api.service;

import java.math.BigDecimal;
import org.springframework.dao.DataAccessException;
import com.iot.relay.api.request.QueryRequest;

public interface SensorOperationService {

	BigDecimal execute(String operationType, QueryRequest request);

	BigDecimal fetchMinimum(QueryRequest request) throws DataAccessException;

	BigDecimal fetchAverage(QueryRequest request) throws DataAccessException;

	BigDecimal fetchMaximum(QueryRequest request) throws DataAccessException;

	BigDecimal fetchMedian(QueryRequest request) throws DataAccessException;

}
