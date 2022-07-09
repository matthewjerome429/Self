package com.cathaypacific.mmbbizrule.business.impl;


import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.constant.MealOption;
import com.cathaypacific.mmbbizrule.db.dao.MealIneligibilityDAO;
import com.cathaypacific.mmbbizrule.db.model.MealOptionModel;
import com.cathaypacific.mmbbizrule.dto.common.booking.FlightBookingDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.MealRequestDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.addMeal.AddMealDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.cancelMeal.CancelMealDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.updateMeal.UpdateMealRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.updateMeal.UpdateMealRequestDetailDTO;
import com.cathaypacific.mmbbizrule.dto.response.meal.updateMeal.UpdateMealResponseDTO;
import com.cathaypacific.mmbbizrule.handler.FlightBookingConverterHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.oneaservice.meal.service.MealDTOService;
import com.cathaypacific.mmbbizrule.oneaservice.meal.service.MealService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.header.SessionStatus;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class MealBusinessImplTest {
	
	@InjectMocks
	MealBusinessImpl mealBusinessImpl;
	
	@Mock
	private PnrInvokeService pnrInvokeService;
	
	@Mock
	private FlightBookingConverterHelper flightBookingConverterHelper;
	
	@Mock
	MealService mealServiceImpl;
	
	@Mock
	private MealDTOService mealDTOServiceImpl;
	
	@Mock
	private MealIneligibilityDAO mealIneligibilityDao;
	
	@Test
	public void updateMeal_Test() throws BusinessBaseException {
		UpdateMealRequestDTO mealRequestDTO=new UpdateMealRequestDTO();
		List<UpdateMealRequestDetailDTO> updateMealDetails=new ArrayList<>();
		UpdateMealRequestDetailDTO updateMealDetail=new UpdateMealRequestDetailDTO();
		updateMealDetail.setCompanyId("CX");
		updateMealDetails.add(updateMealDetail);
		mealRequestDTO.setUpdateMealDetails(updateMealDetails);
		LoginInfo loginInfo=new LoginInfo();
		RetrievePnrBooking pnr=new RetrievePnrBooking();
		CancelMealDetailDTO cancelMealRequestDTO=new CancelMealDetailDTO();
		doNothing().when(mealDTOServiceImpl).verifyUpdateMealRequest(updateMealDetail);
		when(pnrInvokeService.retrievePnrByRloc(mealRequestDTO.getRloc())).thenReturn(pnr);
		when(mealDTOServiceImpl.convertToCancelMealRequest(anyObject())).thenReturn(cancelMealRequestDTO);
		AddMealDetailDTO addMealRequestDTO=new AddMealDetailDTO();
		addMealRequestDTO.setCompanyId("CX");
		List<MealRequestDetailDTO> paxMealDetails=new ArrayList<>();
		MealRequestDetailDTO paxMealDetail=new MealRequestDetailDTO();
		paxMealDetail.setQuantity(5);
		paxMealDetails.add(paxMealDetail);
		addMealRequestDTO.setPaxMealDetails(paxMealDetails);
		when(mealDTOServiceImpl.convertToAddMealRequest(anyObject(), anyObject())).thenReturn(addMealRequestDTO);
		when(mealDTOServiceImpl.convertToAddAssociatedMealRequest(anyObject(), anyObject())).thenReturn(addMealRequestDTO);
		RetrievePnrBooking booking =new RetrievePnrBooking();
		Session session =new Session();
		session.setStatus(SessionStatus.END.getStatus());
		booking.setSession(session);
		when(mealServiceImpl.cancelMeal(anyObject(), anyObject(), anyObject())).thenReturn(booking);
		List<AddMealDetailDTO> addMealRequestDTOs = Lists.newArrayList();
		addMealRequestDTOs.add(addMealRequestDTO);
		when(mealServiceImpl.addMeal(mealRequestDTO.getRloc(), addMealRequestDTOs, session)).thenReturn(booking);
		FlightBookingDTO flightBookingDTO=new FlightBookingDTO();
		when(flightBookingConverterHelper.flightBookingDTOConverter(booking, loginInfo, new BookingBuildRequired(), false)).thenReturn(flightBookingDTO);
		UpdateMealResponseDTO updateMealResponseDTO=mealBusinessImpl.updateMeal(mealRequestDTO, loginInfo);
		Assert.assertEquals(true, updateMealResponseDTO.isSuccess());
	}
	
	/* THIS TEST CASE IS RELY ON TEST CODE
	@Test
	public void retrieveMealEligibleCode_Test(){		
		MealOptionModel mealOptionModel1=new MealOptionModel();
		mealOptionModel1.setAppCode("123");
		mealOptionModel1.setMealOption(MealOption.MA);
		Optional<MealOptionModel> mealOptionModel=Optional.of(mealOptionModel1);
		when(mealIneligibilityDao.findOne(anyObject())).thenReturn(mealOptionModel);
		String  string=mealBusinessImpl.retrieveMealEligibleCode();
		Assert.assertEquals("MA", string);
	}
	*/
}
