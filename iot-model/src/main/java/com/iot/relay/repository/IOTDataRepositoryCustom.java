package com.iot.relay.repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import com.iot.relay.exception.SensorCustomException;
public interface IOTDataRepositoryCustom {

	public BigDecimal fetchMinimumValue(Long clusterId, String eventType, OffsetDateTime startDate,
			OffsetDateTime endDate,String operationType) throws SensorCustomException;

	public BigDecimal fetchMaximumValue(Long clusterId, String eventType, OffsetDateTime startDate,
			OffsetDateTime endDate,String operationType) throws SensorCustomException;

	public BigDecimal fetchAverageValue(Long clusterId, String eventType, OffsetDateTime startDate,
			OffsetDateTime endDate,String operationType) throws SensorCustomException;

	public BigDecimal fetchMedianValue(Long clusterId, String eventType,OffsetDateTime startDate,
			OffsetDateTime endDate,String operationType) throws SensorCustomException;

}
