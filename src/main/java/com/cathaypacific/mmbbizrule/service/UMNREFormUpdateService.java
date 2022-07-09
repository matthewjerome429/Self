package com.cathaypacific.mmbbizrule.service;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UmnrFormUpdateRequestDTO;

public interface UMNREFormUpdateService {

	/**
	 * Update UMNR Form
	 * 1. delete current UMNR OT and requested parental lock RM OT
	 * 2. add UMNR and requested parental lock RM
	 * 
	 * @param umnrFormUpdateRequest
	 * @return
	 * @throws BusinessBaseException
	 */
	public boolean updateUMNREForm(UmnrFormUpdateRequestDTO umnrFormUpdateRequest) throws BusinessBaseException;
	
}
