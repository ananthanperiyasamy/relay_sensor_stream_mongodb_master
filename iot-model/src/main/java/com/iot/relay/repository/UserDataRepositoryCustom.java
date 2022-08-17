package com.iot.relay.repository;

import com.iot.relay.model.UserDataEntity;

public interface UserDataRepositoryCustom {

	public UserDataEntity findUserByName(String userName);

}
