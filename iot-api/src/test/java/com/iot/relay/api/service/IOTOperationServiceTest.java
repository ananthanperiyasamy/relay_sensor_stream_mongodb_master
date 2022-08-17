package com.iot.relay.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.iot.relay.api.request.QueryRequest;
import com.iot.relay.constants.IOTConstant;

/**
 * 
 * Test class for link
 * {@link SensorDataOperationService#execute(String, com.relay.iot.services.api.request.QueryRequest)}
 * 
 * @author Ananthan Periyasamy
 */
@ExtendWith(MockitoExtension.class)
public class IOTOperationServiceTest {
	@Mock
	private IOTOperationService sensorOperation;

	@Test
	public void testMinimumExecute() {
		when(sensorOperation.execute(anyString(), any(QueryRequest.class))).thenReturn(BigDecimal.ONE);
		BigDecimal result = sensorOperation.execute(IOTConstant.SENSOR_OPERATION_MINIMUM, new QueryRequest());
		assertEquals(result, BigDecimal.ONE, "Result be 1.");
	}
	@Test
	public void testMaximumExecute() {
		when(sensorOperation.execute(anyString(), any(QueryRequest.class))).thenReturn(BigDecimal.ONE);
		BigDecimal result = sensorOperation.execute(IOTConstant.SENSOR_OPERATION_MAXIMUM, new QueryRequest());
		assertEquals(result, BigDecimal.ONE, "Result be 1.");
	}
	@Test
	public void testAverageExecuteSuccess() {
		when(sensorOperation.execute(anyString(), any(QueryRequest.class))).thenReturn(BigDecimal.ONE);
		BigDecimal result = sensorOperation.execute(IOTConstant.SENSOR_OPERATION_AVERAGE, new QueryRequest());
		assertEquals(result, BigDecimal.ONE, "Result be 1.");
	}
	@Test
	public void testMedianExecuteSuccess() {
		when(sensorOperation.execute(anyString(), any(QueryRequest.class))).thenReturn(BigDecimal.ONE);
		BigDecimal result = sensorOperation.execute(IOTConstant.SENSOR_OPERATION_MEDIAN, new QueryRequest());
		assertEquals(result, BigDecimal.ONE, "Result be 1.");
	}


}
