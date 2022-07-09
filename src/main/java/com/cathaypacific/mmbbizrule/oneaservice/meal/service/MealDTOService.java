package com.cathaypacific.mmbbizrule.oneaservice.meal.service;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.dto.request.meal.addMeal.AddMealDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.cancelMeal.CancelMealDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.updateMeal.UpdateMealRequestDetailDTO;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;

public interface MealDTOService {
	
	public CancelMealDetailDTO convertToCancelMealRequest(UpdateMealRequestDetailDTO updateMealRequestDTO) throws BusinessBaseException;
	
	public AddMealDetailDTO convertToAddMealRequest(RetrievePnrBooking pnr, UpdateMealRequestDetailDTO updateMealRequestDTO) throws BusinessBaseException;
	
	public AddMealDetailDTO convertToAddAssociatedMealRequest(RetrievePnrBooking pnrBooking, UpdateMealRequestDetailDTO updateMealRequestDTO) throws BusinessBaseException;
	
	public void verifyUpdateMealRequest(UpdateMealRequestDetailDTO requestDto) throws BusinessBaseException;
	
	public void verifyAddMealRequest(AddMealDetailDTO requestDto) throws BusinessBaseException;

	public void verifyCancelMealRequest(CancelMealDetailDTO requestDto) throws BusinessBaseException;
	
}
