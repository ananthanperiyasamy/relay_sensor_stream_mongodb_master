package com.iot.relay.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.iot.relay.model.UserDataEntity;

@Repository
public interface UserDataRepository extends MongoRepository<UserDataEntity, String>,UserDataRepositoryCustom {

}
