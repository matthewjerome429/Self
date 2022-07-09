package com.cathaypacific.mbcommon.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.DateUtil;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = IsValidDateFieldGroup.DateFieldsGroupValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
@Repeatable(IsValidDateFieldGroups.class)
public @interface IsValidDateFieldGroup {

	String message() default "date is invalid.";

	/**
	 * Field name of year.
	 */
	String year();

	/**
	 * Field name of month.
	 */
	String month();

	/**
	 * Field name of day.
	 */
	String day();

	/**
	 * The annotated fields must be a date in the past.
	 */
	boolean past() default false;

	/**
	 * The annotated fields must be a date in the future.
	 */
	boolean future() default false;

	/**
	 * The annotated fields can be null;
	 */
	boolean nullable() default false;

	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default { };

	class DateFieldsGroupValidator implements ConstraintValidator<IsValidDateFieldGroup, Object> {

		private static final LogAgent LOGGER = LogAgent.getLogAgent(DateFieldsGroupValidator.class);

		private String year;
		private String month;
		private String day;

		private boolean past;
		private boolean future;
		private boolean nullable;

		@Override
		public void initialize(IsValidDateFieldGroup constraintAnnotation) {
			this.year = constraintAnnotation.year();
			this.month = constraintAnnotation.month();
			this.day = constraintAnnotation.day();

			this.past = constraintAnnotation.past();
			this.future = constraintAnnotation.future();
			this.nullable = constraintAnnotation.nullable();
		}

		@Override
		public boolean isValid(Object obj, ConstraintValidatorContext context) {

			if (obj == null) {
				return true;
			}

			Class<?> clazz = obj.getClass();

			String yearValue;
			String monthValue;
			String dayValue;
			try {
				Field yearField = ReflectionUtils.findField(clazz, this.year);
				yearValue = (String) ReflectionUtils.getField(yearField, obj);

				Field monthField = ReflectionUtils.findField(clazz, this.month);
				monthValue = (String) ReflectionUtils.getField(monthField, obj);

				Field dayField = ReflectionUtils.findField(clazz, this.day);
				dayValue = (String) ReflectionUtils.getField(dayField, obj);
			} catch (Exception ex) {
				LOGGER.warn("Get date field failed.", ex);
				return false;
			}

			if (StringUtils.isEmpty(yearValue) && StringUtils.isEmpty(monthValue) && StringUtils.isEmpty(dayValue)) {
				return nullable;
			} else if (StringUtils.isEmpty(yearValue) || StringUtils.isEmpty(monthValue)
					|| StringUtils.isEmpty(dayValue)) {
				return false;
			}

			Date date;
			try {
				date = DateUtil.getStrToDate(DateUtil.DATE_PATTERN_YYYY_MM_DD,
						yearValue + "-" + monthValue + "-" + dayValue);
			} catch (Exception ex) {
				LOGGER.warn("Invalid date format.", ex);
				return false;
			}

			if (past) {
				Date now = new Date();
				if (DateUtil.compareDate(date, now, Calendar.DATE) > 0) {
					return false;
				}
			}

			if (future) {
				Date now = new Date();
				if (DateUtil.compareDate(date, now, Calendar.DATE) < 0) {
					return false;
				}
			}

			return true;
		}

	}

}
