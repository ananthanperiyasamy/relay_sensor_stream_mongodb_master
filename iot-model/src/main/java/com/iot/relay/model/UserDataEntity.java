package com.iot.relay.model;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.iot.relay.constants.IOTConstant;
import lombok.Builder;
import lombok.Data;

@Document(collection = IOTConstant.USER_COLLECTION_NAME)
@Data
@Builder
public class UserDataEntity {

	@Id
	@Builder.Default
	private String uuid = UUID.randomUUID().toString();

	private String username;
	
	private String password;

}
