package com.iot.relay.api.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = QueryValidator.class)
@Documented
public @interface ValidQuery {

	String message() default "{Start time shoudn't be greater than end time}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
