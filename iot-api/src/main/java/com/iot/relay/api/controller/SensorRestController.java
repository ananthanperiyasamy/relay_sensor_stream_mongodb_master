package com.iot.relay.api.controller;

import java.math.BigDecimal;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.iot.relay.api.request.QueryRequest;
import com.iot.relay.api.service.SensorOperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;

/**
 * Controller to fetch sensor data from monogdb
 * 
 * @author Ananthan Periyasamy
 */
@RestController
@CrossOrigin
@RequestMapping("query/")
@AllArgsConstructor
public class SensorRestController {

	private SensorOperationService sensorOperationService;

	@Operation(summary = "Perform query operation on sensor data.")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = " Result of the operation."),
			@ApiResponse(responseCode = "400", description = "Invalid input. Dates are mandatory and check date format."),
			@ApiResponse(responseCode = "416", description = "Data for the requested range is not found") })
	@GetMapping(value = "{operation}")
	public ResponseEntity<BigDecimal> performOperation(@PathVariable("operation") String operation,
			@Valid QueryRequest queryDTO) throws Exception {
		return new ResponseEntity<>(sensorOperationService.execute(operation, queryDTO), HttpStatus.OK);
	}

}
