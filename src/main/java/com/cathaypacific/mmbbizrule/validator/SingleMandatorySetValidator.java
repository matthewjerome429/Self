package com.cathaypacific.mmbbizrule.validator;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.cathaypacific.mmbbizrule.util.ReflectUtil;
import com.cathaypacific.mmbbizrule.util.ReflectUtil.ReflectResult;
import com.cathaypacific.mmbbizrule.validator.constraints.SingleMandatorySet;

public class SingleMandatorySetValidator implements ConstraintValidator<SingleMandatorySet, Object> {

	private String[] fieldNames;

	private String message;
	
	@Override
	public void initialize(SingleMandatorySet constraintAnnotation) {
		
		fieldNames = constraintAnnotation.fieldNames();
		message = constraintAnnotation.message();
	}

	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		if (object == null) {
			return true;
		}

		List<Object> validValues = new ArrayList<>(fieldNames.length);
		for (String fieldName : fieldNames) {

			ReflectResult<?> result = ReflectUtil.getFieldValue(object, fieldName);
			if (result.isAvailable()) {
				Object value = result.getResult();
				if (isFieldValueValid(value)) {
					validValues.add(value);
				}
			}
		}
		
		if (validValues.size() != 1) {
			context.disableDefaultConstraintViolation();
			for (String fieldName : fieldNames) {
				context.buildConstraintViolationWithTemplate(message)
					.addPropertyNode(fieldName).addConstraintViolation();
			}
			return false;
		}
		
		return true;
	}

	private boolean isFieldValueValid(Object value) {

		if (value == null) {
			return false;
		}

		if (value instanceof String) {
			return !((String) value).isEmpty();
		} else if (value instanceof Collection) {
			return !((Collection<?>) value).isEmpty();
		} else if (value instanceof Map) {
			return !((Map<?, ?>) value).isEmpty();
		} else if (value.getClass().isArray()) {
			return (Array.getLength(value)) > 0;
		}
		
		return true;
	}

}
