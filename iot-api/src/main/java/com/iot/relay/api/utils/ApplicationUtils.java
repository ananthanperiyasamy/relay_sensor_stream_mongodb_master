package com.iot.relay.api.utils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import com.iot.relay.constants.IOTConstant;
import com.iot.relay.exception.SensorCustomException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ApplicationUtils {

	/**
	 * Method to convert date from yyyy/dd/MM or yyyy-dd-MM format to yyyy-MM-dd'T'HH:mm:ss'Z' (OffsetDateTime format)
	 * 
	 * @throws SensorCustomException when input date format is different then expected
	 */
	public static OffsetDateTime convertStringToOffsetDateTime(String dateString) throws SensorCustomException {
		if (dateString.length() == 10 && dateString.indexOf(IOTConstant.FORWARD_SLASH) == 4) {
			return OffsetDateTime.parse(dateString.replace(IOTConstant.FORWARD_SLASH, IOTConstant.HYPHEN)
					+ IOTConstant.DEFAULT_OFFSET_DATE_TIME_FORMAT, DateTimeFormatter.ISO_ZONED_DATE_TIME);
		} else if (dateString.length() == 10 && dateString.indexOf(IOTConstant.HYPHEN) == 4) {
			return OffsetDateTime.parse(dateString + IOTConstant.DEFAULT_OFFSET_DATE_TIME_FORMAT,
					DateTimeFormatter.ISO_ZONED_DATE_TIME);
		}
		throw new SensorCustomException(
				"Date format in API request not correct (Supported format is yyyy/dd/MM or yyyy-dd-MM");
	}

}
