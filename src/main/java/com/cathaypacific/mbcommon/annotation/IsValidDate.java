package com.cathaypacific.mbcommon.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.utils.DateUtil;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = IsValidDate.ValidChecker.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface IsValidDate {
	
	String message() default "date is invalid.";
	
	String dateFormat() default DateUtil.DATE_PATTERN_YYYY_MM_DD;
	
	boolean comparable() default false;
	
	int compareRule() default Calendar.DATE;
	
	boolean future() default false;
	
	boolean nullable() default false;

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	
	class ValidChecker implements ConstraintValidator<IsValidDate, String>{
		
		private String dateFormat;
		
		private boolean comparable;
		
		private int compareRule;
		
		private boolean future;
		
		private boolean nullable;
		
		@Override
		public void initialize(IsValidDate constraintAnnotation) {
			this.dateFormat = constraintAnnotation.dateFormat();
			this.comparable = constraintAnnotation.comparable();
			this.compareRule = constraintAnnotation.compareRule();
			this.future = constraintAnnotation.future();
			this.nullable = constraintAnnotation.nullable();
		}

		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			boolean empty = StringUtils.isEmpty(value);
			if(nullable) {
				if(empty) {
					return true;					
				}
			} else {
				if(empty) {
					return false;
				}
			}
			
			Date date; 
			try {
				//valid String "yyyy-MM-dd" format and get date by this format
				date = DateUtil.getStrToDate(dateFormat, value);
			} catch (ParseException e) {
				return false;
			}
			//valid date when non-leap year
			Calendar cal = DateUtil.getDateToCal(date);
			if(cal.get(Calendar.MONTH) == 1 && !DateUtil.isLeapYear(cal.get(Calendar.YEAR)) && cal.get(Calendar.DATE) > 28) {
				return false;
			}
			
			if(comparable) {
				//compare with now by future
				Date now = new Date();
				if(future) {
					if(DateUtil.compareDate(date, now, compareRule) < 0) {
						return false;
					}
				} else {
					if(DateUtil.compareDate(date, now, compareRule) > 0) {
						return false;
					}
				}				
			}
			return true;
		}
		
	}

	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		IsValidDate[] value();
	}
	
}