package com.cathaypacific.mmbbizrule.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.enums.error.ErrorTypeEnum;
import com.cathaypacific.mbcommon.exception.UnexpectedException;

@Component
public class ValidateHelper {

	private static final String PATH_SEPARATOR = ".";

	@Autowired
	private Validator validator;

	/**
	 * Validate all constraints on {@code object}, and convert violation to
	 * {@link #ErrorInfo }.
	 * 
	 * @param object
	 *            object to validate
	 * @param groups
	 *            the group or list of groups targeted for validation (defaults
	 *            to {@link Default})
	 * @return errors or an empty list if none.
	 * @throws BusinessException
	 */
	public List<ErrorInfo> validate(Object object, Class<?>... groups) throws UnexpectedException {
		List<ErrorInfo> errors = new ArrayList<>();
		try {
			Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
			if (!constraintViolations.isEmpty())
				for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
					ErrorInfo error = new ErrorInfo();
					String message = constraintViolation.getMessage();
					String path = constraintViolation.getPropertyPath().toString();
					
					error.setErrorCode(message + getPropertyPathLastLevelUpperCase(path));
					error.setFieldName(path);
					error.setType(ErrorTypeEnum.VALIDATION);
					errors.add(error);
				}
		} catch (Exception exception) {
			throw new UnexpectedException("Validation failed", new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM), exception);
		}
		return errors;
	}

	private String getPropertyPathLastLevelUpperCase(String path) {
		if (StringUtils.isEmpty(path)) {
			return path;
		}

		int lastDotIndex = path.lastIndexOf(PATH_SEPARATOR);
		if (lastDotIndex == path.length() - 1) {
			return "";
		}

		return path.substring(lastDotIndex + 1).toUpperCase();
	}

}
