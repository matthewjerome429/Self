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
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.dto.Updated;

@Documented
@Constraint(validatedBy = IsUpdated.IsUpdatedValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface IsUpdated {

	String message() default "";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	
	class IsUpdatedValidator implements ConstraintValidator<IsUpdated, Object>{
		
		private Validator validator;
		private static final String CONVERTOOLSSCONTACTINFO_FIELD_NAME = "convertToOlssContactInfo";
		
		@Override
		public void initialize(IsUpdated constraintAnnotation) {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();	
		}

		@Override
		public boolean isValid(Object obj, ConstraintValidatorContext context) {
			if (obj == null) {
				return true;
			}
			
			if(obj instanceof Updated) {
				Updated updated = (Updated)obj;
				if(updated.isBlank()) {
					return true;
				}
				Class<?> clazz = obj.getClass();				
				List<String> fieldNames = Arrays.asList(clazz.getDeclaredFields()).stream().map(field -> field.getName()).collect(Collectors.toList());
			
				// if obj contains "convertToOlssContactInfo" field, get the value
				Boolean convertToOlssContactInfo = null;
				if (fieldNames.contains(CONVERTOOLSSCONTACTINFO_FIELD_NAME)) {
					Field convertToOlssContactInfoField;
					try {
						convertToOlssContactInfoField = clazz.getDeclaredField(CONVERTOOLSSCONTACTINFO_FIELD_NAME);
						convertToOlssContactInfoField.setAccessible(true);	
						convertToOlssContactInfo = (Boolean) convertToOlssContactInfoField.get(obj);
					} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
						return false;
					}
				}
				
				// if obj contains "convertToOlssContactInfo" field, and is true, no need to validate other field
				if (fieldNames.contains(CONVERTOOLSSCONTACTINFO_FIELD_NAME) && BooleanUtils.isTrue(convertToOlssContactInfo)) {
					return true;
				} else {
					Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj);
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
			} else {
				return true;
			}
			
		}
	}
}
