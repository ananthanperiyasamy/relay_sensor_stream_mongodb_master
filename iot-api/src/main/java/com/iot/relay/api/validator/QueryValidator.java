package com.iot.relay.api.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.iot.relay.api.request.QueryRequest;
import com.iot.relay.api.utils.ApplicationUtils;

public class QueryValidator implements ConstraintValidator<ValidQuery, QueryRequest> {

	@Override
	public boolean isValid(QueryRequest request, ConstraintValidatorContext context) {
		if (request.getStartDateTime() == null || request.getEndDateTime() == null) {
			return false;
		}
		return ApplicationUtils.convertStringToOffsetDateTime(request.getStartDateTime())
				.isBefore(ApplicationUtils.convertStringToOffsetDateTime(request.getEndDateTime()));
	}

}
