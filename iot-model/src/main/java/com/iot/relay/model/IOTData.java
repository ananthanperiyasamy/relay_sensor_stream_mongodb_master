package com.iot.relay.model;

import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import lombok.Data;

@Data
public class IOTData {

	private Long id;

	private BigDecimal value;

	private String timestamp;

	private String type;

	private String name;

	@Nullable
	private Long clusterId;

}
