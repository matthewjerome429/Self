package com.cathaypacific.mmbbizrule.service;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.consent.ConsentAddRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.consent.ConsentsDeleteRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.consent.ConsentCommonRecordResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.consent.ConsentDeleteResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.consent.ConsentsDeleteResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.ConsentInfoRecordResponseDTO;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;

public interface ConsentInfoService {

	public ConsentInfoRecordResponseDTO saveConsentInfo(RetrievePnrBooking retrievePnrBooking, LoginInfo loginInfo, String rloc, String acceptLanguage) throws BusinessBaseException;

	public ConsentCommonRecordResponseDTO saveConsentCommon(ConsentAddRequestDTO requestDTO,
			RetrievePnrBooking retrievePnrBooking, LoginInfo loginInfo, String rloc, String acceptLanguage)
			throws BusinessBaseException;
	
	public ConsentDeleteResponseDTO deleteConsentCommon(int id) throws BusinessBaseException;
	
	public ConsentsDeleteResponseDTO deleteConsentCommons(ConsentsDeleteRequestDTO requestDTO) throws BusinessBaseException;
}
