package com.iot.relay.repository;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.iot.relay.model.IOTDataEntity;

@Repository
public interface IOTDataRepository extends MongoRepository<IOTDataEntity, String>, IOTDataRepositoryCustom {

	@Aggregation(pipeline = { "{$group: { _id: '', total: {$max: $value }}}" })
	public double max();

	@Aggregation(pipeline = { "{$group: { _id: '', total: {$min: $value }}}" })
	public double min();

	@Aggregation(pipeline = { "{$group: { _id: '', total: {$avg: $value }}}" })
	public double avg();

}
