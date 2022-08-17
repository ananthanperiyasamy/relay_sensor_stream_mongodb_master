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

/**
 * 
 * Test class for link
 * {@link SensorDataOperationService#execute(String, com.relay.iot.services.api.request.QueryRequest)}
 * 
 * @author Ananthan Periyasamy
 */
@ExtendWith(MockitoExtension.class)
public class SensorOperationServiceTest {
	@Mock
	private SensorOperationService sensorOperation;

	@Test
	public void testExecuteSuccess() {
		when(sensorOperation.execute(anyString(), any(QueryRequest.class))).thenReturn(BigDecimal.ONE);
		BigDecimal result = sensorOperation.execute("min", new QueryRequest());
		assertEquals(result, BigDecimal.ONE, "Result be 1.");
	}

	/*@Test
	public void testExecuteAndExpectNotSupportedOperationException() {
		when(sensorOperation.execute(anyString(), any(QueryRequest.class))).thenReturn(BigDecimal.ONE);
		SensorCustomException thrown = Assertions.assertThrows(SensorCustomException.class, () -> {
			sensorOperation.execute("newOperation", new QueryRequest());
		},"SensorCustomException was expected");

	  Assertions.assertEquals("SensorCustomException was expected", thrown.getMessage());
	}*/

}
