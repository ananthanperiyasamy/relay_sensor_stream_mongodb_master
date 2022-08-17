package com.iot.relay.api.service;

import java.math.BigDecimal;
import org.springframework.dao.DataAccessException;
import com.iot.relay.api.request.QueryRequest;

public interface IOTOperationService {

	BigDecimal execute(String operationType, QueryRequest request);

	BigDecimal fetchMinimumValue(QueryRequest request,String operationType) throws DataAccessException;

	BigDecimal fetchAverageValue(QueryRequest request,String operationType) throws DataAccessException;

	BigDecimal fetchMaximumValue(QueryRequest request,String operationType) throws DataAccessException;

	BigDecimal fetchMedianValue(QueryRequest request,String operationType) throws DataAccessException;

}
