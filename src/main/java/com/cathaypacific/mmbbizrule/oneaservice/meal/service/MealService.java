package com.cathaypacific.mmbbizrule.oneaservice.meal.service;

import java.util.List;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mmbbizrule.dto.request.meal.addMeal.AddMealDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.cancelMeal.CancelMealDetailDTO;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.oneaconsumer.model.header.Session;


public interface MealService {

	public RetrievePnrBooking addMeal(String rloc, List<AddMealDetailDTO> requestDtos, Session session) throws ExpectedException, SoapFaultException, BusinessBaseException;
	public RetrievePnrBooking cancelMeal(RetrievePnrBooking pnr, List<CancelMealDetailDTO> requestDto, Session session) throws BusinessBaseException;
	public List<String> getMealList(RetrievePnrSegment segment, String cabinClass);
	public boolean isRemoveOTMap(RetrievePnrBooking pnr, List<CancelMealDetailDTO> requestDtos);
}
