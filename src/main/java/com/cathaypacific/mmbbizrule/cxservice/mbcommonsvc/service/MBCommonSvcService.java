package com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.service;

import com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.constant.ContactType;
import com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.model.response.PhoneNumberValidationResponseDTO;

public interface MBCommonSvcService {

	/**
	 * Check the phone number is valid
	 * @param phoneCountryNumber
	 * @param phonenNumber
	 * @param ContactType
	 * @return
	 */
	public PhoneNumberValidationResponseDTO validatePhoneNumber(String phoneCountryNumber, String phonenNumber, ContactType contactType);
}
