package com.iot.relay.repo.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import com.iot.relay.constants.SensorConstant;
import com.iot.relay.exception.SensorCustomException;
import com.iot.relay.model.SensorDataEntity;
import com.iot.relay.repo.SensorDataRepositoryCustom;

/**
 * Repository class which have queries for all operations
 * 
 * @author Ananthan periyasamy
 */
@Repository
public class SensorDataRepositoryCustomImpl implements SensorDataRepositoryCustom {

	@Autowired
	private final MongoTemplate mongoTemplate;

	@Autowired
	public SensorDataRepositoryCustomImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public BigDecimal findMinValueByClusterIdAndTypeAndTimestamp(Long clusterId, String eventType, OffsetDateTime start,
			OffsetDateTime end) throws SensorCustomException {
		BigDecimal result = BigDecimal.ZERO;

		List<AggregationOperation> operationsList = createBaseAggregateOperations(clusterId, eventType, start, end);
		operationsList.add(Aggregation.sort(Sort.Direction.ASC, "value"));
		operationsList.add(Aggregation.limit(1));

		Aggregation aggregation = Aggregation.newAggregation(operationsList);
		AggregationResults<SensorDataEntity> aggregationResults = mongoTemplate.aggregate(aggregation,
				SensorConstant.SENSOR_COLLECTION_NAME, SensorDataEntity.class);
		if (aggregationResults.getMappedResults().isEmpty()) {
			throw new SensorCustomException("Result not found");
		} else {
			result = aggregationResults.getMappedResults().get(0).getValue();
		}
		return result;
	}

	@Override
	public BigDecimal findMaxValueByClusterIdAndTypeAndTimestamp(Long clusterId, String eventType, OffsetDateTime start,
			OffsetDateTime end) throws SensorCustomException {
		BigDecimal result = BigDecimal.ZERO;

		List<AggregationOperation> operationsList = createBaseAggregateOperations(clusterId, eventType, start, end);
		operationsList.add(Aggregation.sort(Sort.Direction.DESC, "value"));
		operationsList.add(Aggregation.limit(1));

		Aggregation aggregation = Aggregation.newAggregation(operationsList);
		AggregationResults<SensorDataEntity> aggregationResults = mongoTemplate.aggregate(aggregation,
				SensorConstant.SENSOR_COLLECTION_NAME, SensorDataEntity.class);
		if (aggregationResults.getMappedResults().isEmpty()) {
			throw new SensorCustomException("Result not found");
		} else {
			result = aggregationResults.getMappedResults().get(0).getValue();
		}
		return result;
	}

	@Override
	public BigDecimal findAvgValueByClusterIdAndTypeAndTimestamp(Long clusterId, String eventType, OffsetDateTime start,
			OffsetDateTime end) throws SensorCustomException {
		BigDecimal result = BigDecimal.ZERO;
		List<AggregationOperation> operationsList = createBaseAggregateOperations(clusterId, eventType, start, end);

		Aggregation aggregation = Aggregation.newAggregation(operationsList);
		AggregationResults<SensorDataEntity> aggregationResults = mongoTemplate.aggregate(aggregation,
				SensorConstant.SENSOR_COLLECTION_NAME, SensorDataEntity.class);
		if (aggregationResults.getMappedResults().isEmpty()) {
			throw new SensorCustomException("Result not found");
		} else {
			BigDecimal sum = aggregationResults.getMappedResults().stream().map(data -> data.getValue())
					.reduce(BigDecimal.ZERO, BigDecimal::add);
			Integer resultsSize = aggregationResults.getMappedResults().size();
			result = sum.divide(new BigDecimal(resultsSize), RoundingMode.CEILING);
		}
		return result;
	}

	@Override
	public BigDecimal findMedianValueByClusterIdAndTypeAndTimestamp(Long clusterId, String eventType,
			OffsetDateTime start, OffsetDateTime end) throws SensorCustomException {
		BigDecimal result = BigDecimal.ZERO;
		List<AggregationOperation> operationsList = createBaseAggregateOperations(clusterId, eventType, start, end);
		operationsList.add(Aggregation.sort(Sort.Direction.ASC, "value"));

		Aggregation aggregation = Aggregation.newAggregation(operationsList);
		AggregationResults<SensorDataEntity> aggregationResults = mongoTemplate.aggregate(aggregation,
				SensorConstant.SENSOR_COLLECTION_NAME, SensorDataEntity.class);
		if (aggregationResults.getMappedResults().isEmpty()) {
			throw new SensorCustomException("Result not found");
		} else {
			int mapSize = aggregationResults.getMappedResults().size();
			if (mapSize == 1) {
				result = aggregationResults.getMappedResults().get(0).getValue();
			} else {
				result = aggregationResults.getMappedResults().get(Math.floorDiv(mapSize, 2)).getValue();
			}
		}
		return result;
	}

	private List<AggregationOperation> createBaseAggregateOperations(Long clusterId, String eventType,
			OffsetDateTime start, OffsetDateTime end) {
		List<AggregationOperation> operationsList = new ArrayList<>();
		if (clusterId != null && clusterId != 0) {
			operationsList.add(Aggregation.match(Criteria.where("clusterId").is(clusterId)));
		}
		if (StringUtils.hasText(eventType)) {
			operationsList.add(Aggregation.match(Criteria.where("type").is(eventType)));
		}
		operationsList.add(Aggregation.match(Criteria.where("timestamp").gte(start)));
		operationsList.add(Aggregation.match(Criteria.where("timestamp").lte(end)));
		operationsList.add(Aggregation.unwind("value"));
		return operationsList;
	}


}
