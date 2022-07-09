package com.cathaypacific.mmbbizrule.validator.constraints;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import com.cathaypacific.mmbbizrule.validator.ValidWithGroupValidator;

/**
 * Enhancement of <tt>javax.validation</tt> with specified groups.
 */
@Documented
@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD })
@Constraint(validatedBy = ValidWithGroupValidator.class)
@ReportAsSingleViolation
public @interface ValidWithGroup {

	String message() default "";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
