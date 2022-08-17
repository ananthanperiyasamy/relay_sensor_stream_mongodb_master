package com.iot.relay.repo;

import com.iot.relay.model.UserDataEntity;

public interface UserDataRepositoryCustom {

	public UserDataEntity findUserByName(String userName);

}
