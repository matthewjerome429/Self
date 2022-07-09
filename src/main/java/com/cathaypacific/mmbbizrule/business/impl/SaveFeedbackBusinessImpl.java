package com.cathaypacific.mmbbizrule.business.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.SaveFeedbackBusiness;
import com.cathaypacific.mmbbizrule.dto.request.feedback.SaveFeedbackRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.feedback.SaveFeedbackResponseDTO;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.service.SaveFeedbackService;

@Service
public class SaveFeedbackBusinessImpl implements SaveFeedbackBusiness{

	@Autowired
	private SaveFeedbackService saveFeedbackService;
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;
	
	@Override
	public SaveFeedbackResponseDTO saveFeedback(LoginInfo loginInfo, SaveFeedbackRequestDTO requestDTO) throws BusinessBaseException{	 
		SaveFeedbackResponseDTO responseDTO = new SaveFeedbackResponseDTO();		
		if(loginInfo == null || requestDTO == null) {
			responseDTO.setSuccess(false);
			return responseDTO;
		}
		
		String rloc = requestDTO.getRloc();
		
		// if RLOC and e-ticket are both empty or both not empty
		if(StringUtils.isEmpty(rloc)){
			saveFeedbackService.saveFeedback(requestDTO, rloc, null);
		} else {
			RetrievePnrBooking pnrBooking = pnrInvokeService.retrievePnrByRloc(requestDTO.getRloc());
			paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
			
			// get primary passenger
			RetrievePnrPassenger pnrPassenger = pnrBooking.getPassengers().stream()
					.filter(pax -> pax != null && !StringUtils.isEmpty(pax.getPassengerID()) && (pax.isPrimaryPassenger() != null && pax.isPrimaryPassenger()))
					.findFirst().orElse(null);
			
			// get the first available e-ticket by passengerId
			String eticket = pnrBooking.getPassengerSegments().stream()
					.filter(ps -> ps != null && pnrPassenger.getPassengerID().equals(ps.getPassengerId()) && !CollectionUtils.isEmpty(ps.getEtickets()))
					.map(ps -> ps.getEtickets().get(0).getTicketNumber()).findFirst().orElse(null);
			
			saveFeedbackService.saveFeedback(requestDTO, rloc, eticket);
		}
		responseDTO.setSuccess(true);
		return responseDTO;
	}
}
