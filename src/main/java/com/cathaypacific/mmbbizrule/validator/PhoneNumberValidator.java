package com.cathaypacific.mmbbizrule.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.constant.ContactType;
import com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.model.response.PhoneNumberValidationResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.service.MBCommonSvcService;
import com.cathaypacific.mmbbizrule.dto.request.boardingpass.spbp.PhoneNumberDTOV2;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.PhoneInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateEmergencyContactDTO;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UMNRFormPhoneInfoRemarkDTO;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.PhoneInfoDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateEmergencyContactDTOV2;
import com.cathaypacific.mmbbizrule.validator.constraints.PhoneNum;

@Component
public class PhoneNumberValidator implements ConstraintValidator<PhoneNum, Object>{
	
	private static LogAgent logger = LogAgent.getLogAgent(PhoneNumberValidator.class);
	
	@Autowired
	private MBCommonSvcService mbCommonSvcService;
	
	@Override
	public void initialize(PhoneNum constraintAnnotation) {
		// do nothing
	}

	@Override
	public boolean isValid(Object obj, ConstraintValidatorContext context) {
		
		if (obj == null) {
			return true;
		}
		
		// Allow empty for emergency contact info
		if (obj instanceof UpdateEmergencyContactDTO) {
			PhoneInfoDTO phoneInfo = (PhoneInfoDTO)obj;
			if (StringUtils.isEmpty(phoneInfo.getCountryCode()) && StringUtils.isEmpty(phoneInfo.getPhoneCountryNumber())) {
				return true;
			}
			return isValidPhoneNumber(phoneInfo.getPhoneCountryNumber(), phoneInfo.getPhoneNo(), ContactType.EMR_CONTACT);
		}
		
		if (obj instanceof UpdateEmergencyContactDTOV2) {
			PhoneInfoDTOV2 phoneInfo = (PhoneInfoDTOV2)obj;
			if (StringUtils.isEmpty(phoneInfo.getCountryCode()) && StringUtils.isEmpty(phoneInfo.getPhoneCountryNumber())) {
				return true;
			}
			return isValidPhoneNumber(phoneInfo.getPhoneCountryNumber(), phoneInfo.getPhoneNo(), ContactType.EMR_CONTACT);
		}
		
		
		// Verify generic phone info
		if(obj instanceof PhoneInfoDTO){
			PhoneInfoDTO phoneInfo = (PhoneInfoDTO)obj;
			return isValidPhoneNumber(phoneInfo.getPhoneCountryNumber(), phoneInfo.getPhoneNo(), ContactType.CONTACT);
			 
		}
		
		if(obj instanceof PhoneInfoDTOV2){
			PhoneInfoDTOV2 phoneInfo = (PhoneInfoDTOV2)obj;
			return isValidPhoneNumber(phoneInfo.getPhoneCountryNumber(), phoneInfo.getPhoneNo(), ContactType.CONTACT);
		}
		
		// Verify UMNR phone info
		if(obj instanceof UMNRFormPhoneInfoRemarkDTO){
			UMNRFormPhoneInfoRemarkDTO phoneInfo = (UMNRFormPhoneInfoRemarkDTO)obj;
			return isValidPhoneNumber(phoneInfo.getPhoneCountryNumber(), phoneInfo.getPhoneNo(), ContactType.EMR_CONTACT);
		}
		
		// Verify BP phone info
		if (obj instanceof PhoneNumberDTOV2) {
			PhoneNumberDTOV2 phoneInfo = (PhoneNumberDTOV2) obj;
			return isValidPhoneNumber(phoneInfo.getCountryCode(), phoneInfo.getNumber(), ContactType.CONTACT);
		}
			
		return false;
	}
	/**
	 * check phone number valid
	 * @param phoneCountryNumber
	 * @param phonenNumber
	 * @param contactType
	 * @return
	 */
	private boolean isValidPhoneNumber(String phoneCountryNumber, String phonenNumber, ContactType contactType){
		PhoneNumberValidationResponseDTO phoneNumberValidationResponseDTO = null;
		try {
			 phoneNumberValidationResponseDTO = mbCommonSvcService.validatePhoneNumber(phoneCountryNumber, phonenNumber, contactType);
		} catch (Exception e) {
			logger.error("Invoke mbcommonsvc service failed.",e);
		}
		
		return phoneNumberValidationResponseDTO != null && phoneNumberValidationResponseDTO.isValid();
	}
}
