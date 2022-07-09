package com.cathaypacific.mbcommon.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Set;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

@Documented
@Constraint(validatedBy = ValueIsInPair.ValueIsInPairValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface ValueIsInPair {
	String message() default "companyId and membershipNumber value is not in pair";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	
	class ValueIsInPairValidator implements ConstraintValidator<ValueIsInPair, Object>{
		private Validator validator;
		private static final String COMPANY_ID_FIELD_NAME = "companyId";
		private static final String MEMBERSHIP_NUMBER_FIELD_NAME = "membershipNumber";
		
		@Override
		public void initialize(ValueIsInPair constraintAnnotation) {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();	
		}

		@Override
		public boolean isValid(Object ffpObj, ConstraintValidatorContext context) {
			if(ffpObj == null){
				return true;
			}
			Class<?> clazz = ffpObj.getClass();
			String companyId;
			String membershipNumber;
			
			try {
				Field companyIdField = clazz.getDeclaredField(COMPANY_ID_FIELD_NAME);
				Field membershipNumberField = clazz.getDeclaredField(MEMBERSHIP_NUMBER_FIELD_NAME);
				
				companyIdField.setAccessible(true);
				membershipNumberField.setAccessible(true);
				
				companyId = (String) companyIdField.get(ffpObj);
				membershipNumber = (String) membershipNumberField.get(ffpObj);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				return false;
			}
					
			if((StringUtils.isEmpty(companyId) && !StringUtils.isEmpty(membershipNumber)) || (!StringUtils.isEmpty(companyId) && StringUtils.isEmpty(membershipNumber))){
				return false;
			}
			Set<ConstraintViolation<Object>> constraintViolations = validator.validate(ffpObj);
			if (!CollectionUtils.isEmpty(constraintViolations)) {
				for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate(constraintViolation.getMessageTemplate())
							.addPropertyNode(constraintViolation.getPropertyPath().toString())
							.addConstraintViolation();
				}
				return false;
			}
			return true;
		}
		
	}
}
