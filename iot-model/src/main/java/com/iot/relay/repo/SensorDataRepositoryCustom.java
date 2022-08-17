package com.iot.relay.repo;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import com.iot.relay.exception.SensorCustomException;
public interface SensorDataRepositoryCustom {

	public BigDecimal findMinValueByClusterIdAndTypeAndTimestamp(Long clusterId, String eventType, OffsetDateTime start,
			OffsetDateTime end) throws SensorCustomException;

	public BigDecimal findMaxValueByClusterIdAndTypeAndTimestamp(Long clusterId, String eventType, OffsetDateTime start,
			OffsetDateTime end) throws SensorCustomException;

	public BigDecimal findAvgValueByClusterIdAndTypeAndTimestamp(Long clusterId, String eventType, OffsetDateTime start,
			OffsetDateTime end) throws SensorCustomException;

	public BigDecimal findMedianValueByClusterIdAndTypeAndTimestamp(Long clusterId, String eventType,
			OffsetDateTime start, OffsetDateTime end) throws SensorCustomException;

}
