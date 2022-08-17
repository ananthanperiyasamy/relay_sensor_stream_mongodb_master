package com.iot.relay.api.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

import com.iot.relay.api.validator.ValidQuery;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@ValidQuery
@Data
public class QueryRequest {

	@NotEmpty(message = "Start time shouldn't be null")
	@Schema(description = "Start date time to filter the data.", required = true)
	private String startDateTime;

	@NotEmpty(message = "End time shouldn't be null")
	@Schema(description = "End date time to filter the data.", required = true)
	private String endDateTime;

	@Nullable
	@Schema(description = "Type of event.")
	private String eventType;

	@Nullable
	@Schema(description = "Cluster id.")
	private Long clusterId;

}
