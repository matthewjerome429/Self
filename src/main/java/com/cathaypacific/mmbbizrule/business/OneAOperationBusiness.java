package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mmbbizrule.dto.request.oneaoperation.AddRmRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.oneaoperation.AddSkRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.oneaoperation.OneAOperationResponseDTO;

public interface OneAOperationBusiness {
	
	/**
	 * add 1A SK element into PNR without session/PNRReply
	 * 
	 * @param request
	 * @return
	 */
	public OneAOperationResponseDTO addSkElements(AddSkRequestDTO request);
	
	/**
	 * add 1A RM element into PNR without session/PNRReply
	 * 
	 * @param request
	 * @return
	 */
	public OneAOperationResponseDTO addRmElements(AddRmRequestDTO request);
	
}
