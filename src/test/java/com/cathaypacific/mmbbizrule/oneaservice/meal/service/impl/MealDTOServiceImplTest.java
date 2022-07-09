package com.cathaypacific.mmbbizrule.oneaservice.meal.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mmbbizrule.dto.request.meal.MealRequestDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.addMeal.AddMealDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.cancelMeal.CancelMealDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.updateMeal.UpdateMealRequestDetailDTO;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrMeal;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class MealDTOServiceImplTest {

	@InjectMocks
	private MealDTOServiceImpl mealDTOServiceImpl;
	
	private UpdateMealRequestDetailDTO updateMealRequestDTO;
	
	@Mock
	private RetrievePnrBooking retrievePnrBooking;
	
	
	@Before
	public void before() throws Exception{
		/* do nothing */
	}
	
	@Test
	public void testConvertToCancelMealRequestNormalFlow() throws ExpectedException, SoapFaultException, BusinessBaseException{
		MealRequestDetailDTO paxMealDetailDTO = new MealRequestDetailDTO();
		paxMealDetailDTO.setMealCode("BBML");
		paxMealDetailDTO.setPassengerId("2I");
		paxMealDetailDTO.setQuantity(1);
		
		updateMealRequestDTO = new UpdateMealRequestDetailDTO();
		updateMealRequestDTO.setCompanyId("CX");
		updateMealRequestDTO.setSegmentId("1");
		updateMealRequestDTO.setOperator("CX");
		updateMealRequestDTO.setMealDetails(Lists.newArrayList());
		updateMealRequestDTO.getMealDetails().add(paxMealDetailDTO);
		
		CancelMealDetailDTO cancelMealRequestDTO = mealDTOServiceImpl.convertToCancelMealRequest(updateMealRequestDTO);
		
		assertTrue(cancelMealRequestDTO.getSegmentId() == updateMealRequestDTO.getSegmentId() &&
				cancelMealRequestDTO.getPaxIds().get(0) == updateMealRequestDTO.getMealDetails().get(0).getPassengerId());
	}
	
	@Test
	public void testConvertToAddMealRequestNormalFlow() throws ExpectedException, SoapFaultException, BusinessBaseException{
		MealRequestDetailDTO paxMealDetailDTO = new MealRequestDetailDTO();
		paxMealDetailDTO.setMealCode("BBML");
		paxMealDetailDTO.setPassengerId("2I");
		paxMealDetailDTO.setQuantity(1);
		
		updateMealRequestDTO = new UpdateMealRequestDetailDTO();
		updateMealRequestDTO.setCompanyId("CX");
		updateMealRequestDTO.setSegmentId("1");
		updateMealRequestDTO.setOperator("CX");
		updateMealRequestDTO.setMealDetails(Lists.newArrayList());
		updateMealRequestDTO.getMealDetails().add(paxMealDetailDTO);
		
		AddMealDetailDTO addMealRequestDTO = mealDTOServiceImpl.convertToAddMealRequest(retrievePnrBooking, updateMealRequestDTO);
		
		assertTrue(addMealRequestDTO.getSegmentId() == updateMealRequestDTO.getSegmentId());
	}
	
	@Test(expected = UnexpectedException.class)
	public void testVerifyUpdateMealRequestWithEmptyList() throws ExpectedException, SoapFaultException, BusinessBaseException{
		updateMealRequestDTO = new UpdateMealRequestDetailDTO();
		updateMealRequestDTO.setCompanyId("CX");
		updateMealRequestDTO.setSegmentId("1");
		updateMealRequestDTO.setMealDetails(Lists.newArrayList());
		
		mealDTOServiceImpl.verifyUpdateMealRequest(updateMealRequestDTO);
	}
	
	@Test(expected = UnexpectedException.class)
	public void testVerifyUpdateMealRequestWithEmptyParameter() throws ExpectedException, SoapFaultException, BusinessBaseException{
		updateMealRequestDTO = new UpdateMealRequestDetailDTO();
		updateMealRequestDTO.setCompanyId("CX");
		updateMealRequestDTO.setSegmentId("1");
		updateMealRequestDTO.setOperator("CX");
		updateMealRequestDTO.setMealDetails(Lists.newArrayList());
		
		MealRequestDetailDTO paxMealDetailDTO = new MealRequestDetailDTO();
		paxMealDetailDTO.setMealCode("BBML");
		paxMealDetailDTO.setPassengerId("");	// empty pax id
		paxMealDetailDTO.setQuantity(1);
		updateMealRequestDTO.getMealDetails().add(paxMealDetailDTO);
		
		mealDTOServiceImpl.verifyUpdateMealRequest(updateMealRequestDTO);
	}
	
	@Test(expected = UnexpectedException.class)
	public void testVerifyAddMealRequestWithEmptyList() throws ExpectedException, SoapFaultException, BusinessBaseException{
		AddMealDetailDTO addMealRequestDTO = new AddMealDetailDTO();
		addMealRequestDTO.setCompanyId("CX");
		addMealRequestDTO.setSegmentId("1");
		addMealRequestDTO.setPaxMealDetails(Lists.newArrayList());
		
		mealDTOServiceImpl.verifyAddMealRequest(addMealRequestDTO);
	}
	
	@Test(expected = UnexpectedException.class)
	public void testVerifyAddMealRequestWithEmptyParameter() throws ExpectedException, SoapFaultException, BusinessBaseException{
		AddMealDetailDTO addMealRequestDTO = new AddMealDetailDTO();
		addMealRequestDTO.setCompanyId("CX");
		addMealRequestDTO.setSegmentId("1");
		addMealRequestDTO.setPaxMealDetails(Lists.newArrayList());
		
		MealRequestDetailDTO paxMealDetailDTO = new MealRequestDetailDTO();
		paxMealDetailDTO.setMealCode("");		// empty meal code
		paxMealDetailDTO.setPassengerId("");	// empty pax id
		paxMealDetailDTO.setQuantity(0);		// zero quantity
		addMealRequestDTO.getPaxMealDetails().add(paxMealDetailDTO);
		
		mealDTOServiceImpl.verifyAddMealRequest(addMealRequestDTO);
	}
	
	@Test(expected = UnexpectedException.class)
	public void testVerifyCancelMealRequest() throws ExpectedException, SoapFaultException, BusinessBaseException{
		CancelMealDetailDTO cancelMealRequestDTO = new CancelMealDetailDTO();
		cancelMealRequestDTO.setSegmentId("1");
		cancelMealRequestDTO.setPaxIds(Lists.newArrayList());
		
		mealDTOServiceImpl.verifyCancelMealRequest(cancelMealRequestDTO);
	}
	@Test
	public void testConvertToAddAssociatedMealRequest() throws BusinessBaseException{
		RetrievePnrBooking pnrBooking=new RetrievePnrBooking();
		List<RetrievePnrPassengerSegment> passengerSegments=new ArrayList<>();
		RetrievePnrPassengerSegment passengerSegment=new RetrievePnrPassengerSegment();
		RetrievePnrMeal meal=new RetrievePnrMeal();
		meal.setQulifierId("1");
		passengerSegment.setMeal(meal);
		passengerSegment.setSegmentId("1");
		passengerSegment.setPassengerId("1");
		passengerSegments.add(passengerSegment);
		pnrBooking.setPassengerSegments(passengerSegments);
		UpdateMealRequestDetailDTO updateMealRequestDTO=new UpdateMealRequestDetailDTO();
		updateMealRequestDTO.setCompanyId("CX");
		List<MealRequestDetailDTO> mealDetails=new ArrayList<>();
		MealRequestDetailDTO mealDetail=new MealRequestDetailDTO();
		mealDetail.setPassengerId("1");
		mealDetails.add(mealDetail);
		updateMealRequestDTO.setMealDetails(mealDetails);
		updateMealRequestDTO.setSegmentId("1");
		updateMealRequestDTO.setOperator("KA");
		AddMealDetailDTO addMealDetailDTO=mealDTOServiceImpl.convertToAddAssociatedMealRequest(pnrBooking, updateMealRequestDTO);
		assertEquals(null,addMealDetailDTO);
	}
}
