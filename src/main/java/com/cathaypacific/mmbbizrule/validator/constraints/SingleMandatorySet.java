package com.cathaypacific.mmbbizrule.validator.constraints;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.cathaypacific.mmbbizrule.validator.SingleMandatorySetValidator;

/**
 * Validate a set of fields, one and only one of them should be not null (not empty for collection and string).
 *
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE })
@Constraint(validatedBy = SingleMandatorySetValidator.class)
@Repeatable(SingleMandatorySets.class)
public @interface SingleMandatorySet {

	/**
	 * Name of the fields belong to the set.
	 */
	String[] fieldNames();
	
	String message() default "";

	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}
