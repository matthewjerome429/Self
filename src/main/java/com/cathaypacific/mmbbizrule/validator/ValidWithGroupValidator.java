package com.cathaypacific.mmbbizrule.validator;

import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.util.CollectionUtils;

import com.cathaypacific.mmbbizrule.validator.constraints.ValidWithGroup;

public class ValidWithGroupValidator implements ConstraintValidator<ValidWithGroup, Object> {

	private Validator validator;
	
	private Class<?>[] groups;

	@Override
	public void initialize(ValidWithGroup constraintAnnotation) {
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.validator = factory.getValidator();
		this.groups = constraintAnnotation.groups();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(value);
		Set<ConstraintViolation<Object>> groupConstraintViolations = validator.validate(value, groups);
		constraintViolations.addAll(groupConstraintViolations);
		
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
