package com.iot.relay.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import com.iot.relay.constants.IOTConstant;
import lombok.Builder;
import lombok.Data;

@Document(collection = IOTConstant.SENSOR_COLLECTION_NAME)
@Data
@Builder
public class IOTDataEntity {

	@Id
	@Builder.Default
	private String uuid = UUID.randomUUID().toString();

	private Long id;

	@Field(targetType = FieldType.DECIMAL128)
	private BigDecimal value;
	@Field("timestamp")
	private OffsetDateTime timestamp;
	@Field("type")
	private String type;
	@Field("name")
	private String name;
	@Field("clusterId")
	private Long clusterId;

}
