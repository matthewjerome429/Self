package com.cathaypacific.mmbbizrule.validator.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import com.cathaypacific.mmbbizrule.validator.TravelDocValidator;

@Documented
@Constraint(validatedBy = TravelDocValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface TravelDoc {

	String message() default "";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	
	String level() default "primary";
	

}
