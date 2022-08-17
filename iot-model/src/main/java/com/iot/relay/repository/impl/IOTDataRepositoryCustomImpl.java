package com.iot.relay.repository.impl;

import java.math.BigDecimal;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import com.iot.relay.constants.IOTConstant;
import com.iot.relay.exception.SensorCustomException;
import com.iot.relay.model.IOTDataEntity;
import com.iot.relay.repository.IOTDataRepositoryCustom;
/**
 * Repository class which have queries for all operations
 * 
 * @author Ananthan periyasamy
 */
@Repository
public class IOTDataRepositoryCustomImpl implements IOTDataRepositoryCustom {

	@Autowired
	private final MongoTemplate mongoTemplate;

	@Autowired
	public IOTDataRepositoryCustomImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public BigDecimal fetchMinimumValue(Long clusterId, String eventType, OffsetDateTime startDate,
			OffsetDateTime endDate, String operationType) throws SensorCustomException {
		MatchOperation filterData = createMatchOperation(clusterId, eventType, startDate, endDate);
		GroupOperation groupByValue = group().min("value").as(operationType);
		Aggregation aggregation = newAggregation(filterData, groupByValue);

		return executeMongoQuery(aggregation, operationType);
	}

	@Override
	public BigDecimal fetchMaximumValue(Long clusterId, String eventType, OffsetDateTime startDate,
			OffsetDateTime endDate, String operationType) throws SensorCustomException {
		MatchOperation filterData = createMatchOperation(clusterId, eventType, startDate, endDate);
		GroupOperation groupByValue = group().max("value").as(operationType);
		Aggregation aggregation = newAggregation(filterData, groupByValue);

		return executeMongoQuery(aggregation, operationType);
	}

	@Override
	public BigDecimal fetchAverageValue(Long clusterId, String eventType, OffsetDateTime startDate,
			OffsetDateTime endDate, String operationType) throws SensorCustomException {
		MatchOperation filterData = createMatchOperation(clusterId, eventType, startDate, endDate);
		GroupOperation groupByValue = group().avg("value").as(operationType);
		Aggregation aggregation = newAggregation(filterData, groupByValue);

		return executeMongoQuery(aggregation, operationType);
	}

	@Override
	public BigDecimal fetchMedianValue(Long clusterId, String eventType, OffsetDateTime startDate,
			OffsetDateTime endDate, String operationType) throws SensorCustomException {
		BigDecimal result = BigDecimal.ZERO;
		MatchOperation filterData = createMatchOperation(clusterId, eventType, startDate, endDate);
		SortOperation sort = Aggregation.sort(Sort.Direction.ASC, "value");

		Aggregation aggregation = newAggregation(filterData, sort);
		AggregationResults<IOTDataEntity> aggregationResults = mongoTemplate.aggregate(aggregation,
				IOTConstant.SENSOR_COLLECTION_NAME, IOTDataEntity.class);
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
	
	/**
	 * Create MatchOperation based on condition
	 * 
	 * clusterId and eventType are optional and skip these fields when its empty
	 */
	private MatchOperation createMatchOperation(Long clusterId, String eventType, OffsetDateTime startDate,OffsetDateTime endDate) {
		Criteria baseCriteria = new Criteria().andOperator(Criteria.where("timestamp").gte(startDate).lte(endDate));

		if (clusterId != null && eventType == null) {
			baseCriteria.and("clusterId").is(clusterId);
		}
		else if (clusterId == null && eventType != null) {
			baseCriteria.and("type").is(eventType);
		}
		return Aggregation.match(baseCriteria);
	}
	
	/**
	 * Generic method to execute the mongoTemplate.aggregate execution
	 */
	private BigDecimal executeMongoQuery(Aggregation aggregation,String operationType) {
		AggregationResults<org.bson.Document> aggregationResults = mongoTemplate.aggregate(aggregation,
				IOTConstant.SENSOR_COLLECTION_NAME, org.bson.Document.class);
		if (aggregationResults.getRawResults().isEmpty()) {
			throw new SensorCustomException("Result not found");
		} else {
			return new BigDecimal(aggregationResults.getUniqueMappedResult().get(operationType).toString());
		}
	}

	
}
