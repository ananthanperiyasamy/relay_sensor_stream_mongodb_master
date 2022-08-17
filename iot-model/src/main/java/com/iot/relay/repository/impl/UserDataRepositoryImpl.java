package com.iot.relay.repository.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import com.iot.relay.constants.IOTConstant;
import com.iot.relay.exception.SensorCustomException;
import com.iot.relay.model.UserDataEntity;
import com.iot.relay.repository.UserDataRepositoryCustom;

/**
 * User Repository class which store/fetch user details from DB
 * 
 * @author Ananthan periyasamy
 */
@Repository
public class UserDataRepositoryImpl implements UserDataRepositoryCustom {

	@Autowired
	private final MongoTemplate mongoTemplate;

	@Autowired
	public UserDataRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public UserDataEntity findUserByName(String userName) {

		List<AggregationOperation> operationsList = createBaseAggregateOperations(userName);
		operationsList.add(Aggregation.limit(1));

		Aggregation aggregation = Aggregation.newAggregation(operationsList);
		AggregationResults<UserDataEntity> aggregationResults = mongoTemplate.aggregate(aggregation,
				IOTConstant.USER_COLLECTION_NAME, UserDataEntity.class);
		if (aggregationResults.getMappedResults().isEmpty()) {
			throw new SensorCustomException("Result not found");
		} else {
			return aggregationResults.getMappedResults().get(0);
		}
	}

	private List<AggregationOperation> createBaseAggregateOperations(String userName) {
		List<AggregationOperation> operationsList = new ArrayList<>();
		if (userName != null) {
			operationsList.add(Aggregation.match(Criteria.where("username").is(userName)));
		}
		return operationsList;
	}


	


}
