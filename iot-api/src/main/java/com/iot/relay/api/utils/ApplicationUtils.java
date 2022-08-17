package com.iot.relay.api.utils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.iot.relay.constants.SensorConstant;
import com.iot.relay.exception.SensorCustomException;

public class ApplicationUtils {

	public static OffsetDateTime convertStringToOffsetDateTime(String dateString) throws SensorCustomException {
		if (dateString.length() == 10 && dateString.indexOf(SensorConstant.FORWARD_SLASH) == 4) {
			return OffsetDateTime.parse(dateString.replace(SensorConstant.FORWARD_SLASH, SensorConstant.HYPHEN)
					+ SensorConstant.DEFAULT_OFFSET_DATE_TIME_FORMAT, DateTimeFormatter.ISO_ZONED_DATE_TIME);
		} else if (dateString.length() == 10 && dateString.indexOf(SensorConstant.HYPHEN) == 4) {
			return OffsetDateTime.parse(dateString + SensorConstant.DEFAULT_OFFSET_DATE_TIME_FORMAT,
					DateTimeFormatter.ISO_ZONED_DATE_TIME);
		}
		throw new SensorCustomException(
				"Date format in API request not correct (Supported format is yyyy/dd/MM or yyyy-dd-MM");
	}

}
