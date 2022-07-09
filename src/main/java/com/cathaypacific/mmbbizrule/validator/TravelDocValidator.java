package com.cathaypacific.mmbbizrule.validator;

import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.dto.Updated;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateTravelDocDTO;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateTravelDocDTOV2;
import com.cathaypacific.mmbbizrule.validator.constraints.TravelDoc;
import com.cathaypacific.mmbbizrule.validator.group.COR;
import com.cathaypacific.mmbbizrule.validator.group.DOCO;
import com.cathaypacific.mmbbizrule.validator.group.DOCS;

 public class TravelDocValidator implements ConstraintValidator<TravelDoc, Object>{
		
		private Validator validator;
		
		private String level;
		
		@Override
		public void initialize(TravelDoc constraintAnnotation) {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			validator = factory.getValidator();
			this.level = constraintAnnotation.level();
		}

		@Override
		public boolean isValid(Object obj, ConstraintValidatorContext context) {
			if(obj == null) {
				return true;
			}
			
			if(obj instanceof Updated) {
				Updated updated = (Updated)obj;
				if(updated.isBlank()) {
					return true;
				}
			}

		String travelDocumentType;
		if (obj instanceof UpdateTravelDocDTO) {
			UpdateTravelDocDTO travelDoc = (UpdateTravelDocDTO) obj;
			travelDocumentType = travelDoc.getTravelDocumentType();
		} else if (obj instanceof UpdateTravelDocDTOV2) {
			UpdateTravelDocDTOV2 travelDoc = (UpdateTravelDocDTOV2) obj;
			travelDocumentType = travelDoc.getTravelDocumentType();
		} else {
			return true;
		}

		Class<?>[] groups;
		if ("secondary".equals(level)) {
			if (OneAConstants.TRAVEL_DOCUMENT_TYPE_VISA.equalsIgnoreCase(travelDocumentType)) {
				groups = new Class[] { DOCO.class };
			} else {
				groups = new Class[] { DOCS.class };
			}
		} else {
			groups = new Class[] { DOCS.class, COR.class };
		}
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(obj, groups);
		if (!CollectionUtils.isEmpty(constraintViolations)) {
			for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(constraintViolation.getMessageTemplate()).addPropertyNode(
						constraintViolation.getPropertyPath().toString()).addConstraintViolation();
			}
			return false;
		}
		return true;
	}
}
