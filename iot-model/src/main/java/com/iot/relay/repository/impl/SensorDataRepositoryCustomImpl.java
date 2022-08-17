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
import com.iot.relay.constants.SensorConstant;
import com.iot.relay.exception.SensorCustomException;
import com.iot.relay.model.SensorDataEntity;
import com.iot.relay.repository.SensorDataRepositoryCustom;
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
		Criteria criterias = createMultiCriteria(clusterId, eventType, start, end);
		MatchOperation filterDate = Aggregation.match(criterias);
		GroupOperation groupByValue = group().min("value").as("minimumValue");

		Aggregation aggregation = newAggregation(filterDate, groupByValue);
		AggregationResults<org.bson.Document> aggregationResults = mongoTemplate.aggregate(aggregation,
				SensorConstant.SENSOR_COLLECTION_NAME, org.bson.Document.class);
		if (aggregationResults.getRawResults().isEmpty()) {
			throw new SensorCustomException("Result not found");
		} else {
			return new BigDecimal(aggregationResults.getUniqueMappedResult().get("minimumValue").toString());
		}
	}

	

	@Override
	public BigDecimal findMaxValueByClusterIdAndTypeAndTimestamp(Long clusterId, String eventType, OffsetDateTime start,
			OffsetDateTime end) throws SensorCustomException {
		Criteria criterias = createMultiCriteria(clusterId, eventType, start, end);
		MatchOperation filterDate = Aggregation.match(criterias);
		GroupOperation groupByValue = group().max("value").as("maximumValue");

		Aggregation aggregation = newAggregation(filterDate, groupByValue);
		AggregationResults<org.bson.Document> aggregationResults = mongoTemplate.aggregate(aggregation,
				SensorConstant.SENSOR_COLLECTION_NAME, org.bson.Document.class);
		if (aggregationResults.getRawResults().isEmpty()) {
			throw new SensorCustomException("Result not found");
		} else {
			return new BigDecimal(aggregationResults.getUniqueMappedResult().get("maximumValue").toString());
		}
	}

	@Override
	public BigDecimal findAvgValueByClusterIdAndTypeAndTimestamp(Long clusterId, String eventType, OffsetDateTime start,
			OffsetDateTime end) throws SensorCustomException {
		Criteria criterias = createMultiCriteria(clusterId, eventType, start, end);
		MatchOperation filterDate = Aggregation.match(criterias);
		GroupOperation groupByValue = group().avg("value").as("averageValue");

		Aggregation aggregation = newAggregation(filterDate, groupByValue);
		AggregationResults<org.bson.Document> aggregationResults = mongoTemplate.aggregate(aggregation,
				SensorConstant.SENSOR_COLLECTION_NAME, org.bson.Document.class);
		if (aggregationResults.getRawResults().isEmpty()) {
			throw new SensorCustomException("Result not found");
		} else {
			return new BigDecimal(aggregationResults.getUniqueMappedResult().get("averageValue").toString());
		}
	}

	@Override
	public BigDecimal findMedianValueByClusterIdAndTypeAndTimestamp(Long clusterId, String eventType,
			OffsetDateTime start, OffsetDateTime end) throws SensorCustomException {
		BigDecimal result = BigDecimal.ZERO;
		Criteria criterias = createMultiCriteria(clusterId, eventType, start, end);
		MatchOperation filterDate = Aggregation.match(criterias);
		SortOperation sort = Aggregation.sort(Sort.Direction.ASC, "value");

		Aggregation aggregation = newAggregation(filterDate, sort);
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
	
	/**
	 * Create criteria based on condition
	 * 
	 * clusterId and eventType are optional and skip these fields when its empty
	 */
	private Criteria createMultiCriteria(Long clusterId, String eventType, OffsetDateTime start, OffsetDateTime end) {
		if(clusterId == null  && eventType == null){
			return new Criteria()
				.andOperator(Criteria.where("timestamp").gte(start).lte(end));
		}
		else if(clusterId != null && eventType ==null){
			return new Criteria()
					.andOperator(Criteria.where("timestamp").gte(start).lte(end)
					.and("clusterId").is(clusterId));
		}
		else if(clusterId == null && eventType != null){
			return new Criteria()
					.andOperator(Criteria.where("timestamp").gte(start).lte(end)
					.and("type").is(eventType));
		}
		else {
			return new Criteria()
					.andOperator(Criteria.where("timestamp").gte(start).lte(end)
					.and("type").is(eventType)
					.and("clusterId").is(clusterId));
		}
	}




}
