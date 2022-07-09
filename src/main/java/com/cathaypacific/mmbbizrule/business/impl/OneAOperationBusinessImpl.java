package com.cathaypacific.mmbbizrule.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.business.OneAOperationBusiness;
import com.cathaypacific.mmbbizrule.dto.request.oneaoperation.AddRmRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.oneaoperation.AddSkRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.oneaoperation.OneAOperationResponseDTO;
import com.cathaypacific.mmbbizrule.oneaservice.addbooking.AddBookingBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.pnraddelement.sevice.AddPnrElementsInvokeService;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;

@Service
public class OneAOperationBusinessImpl implements OneAOperationBusiness {
	
	private static LogAgent logger = LogAgent.getLogAgent(OneAOperationBusinessImpl.class);
	
	@Autowired
	private AddPnrElementsInvokeService addPnrElementsInvokeService;

	@Override
	public OneAOperationResponseDTO addSkElements(AddSkRequestDTO request){
		OneAOperationResponseDTO response = new OneAOperationResponseDTO();
		AddBookingBuilder builder = new AddBookingBuilder();
		try {
			PNRReply pnrReply = addPnrElementsInvokeService.addMutiElements(builder.buildSkRequest(request.getRloc(), request.getAddSkDetails()), null);			
			response.setPnrReply(pnrReply);
			response.setSuccess(true);
		} catch(Exception e) {
			logger.error("OneAOperationBusinessImpl -> addSkElements failure!", e);
			response.setSuccess(false);
		}
		return response;
	}

	@Override
	public OneAOperationResponseDTO addRmElements(AddRmRequestDTO request) {
		OneAOperationResponseDTO response = new OneAOperationResponseDTO();
		AddBookingBuilder builder = new AddBookingBuilder();
		try {
			PNRReply pnrReply = addPnrElementsInvokeService.addMutiElements(builder.buildRmRequest(request.getRloc(), request.getAddRmDetails()), null);			
			response.setPnrReply(pnrReply);
			response.setSuccess(true);
		} catch(Exception e) {
			logger.error("OneAOperationBusinessImpl -> addRmElements failure!", e);
			response.setSuccess(false);
		}
		return response;
	}

}
