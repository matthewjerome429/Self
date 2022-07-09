package com.cathaypacific.mmbbizrule.business.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.feedback.SaveFeedbackRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.feedback.SaveFeedbackResponseDTO;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.TicketProcessInvokeService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.service.SaveFeedbackService;
@RunWith(MockitoJUnitRunner.class)
public class SaveFeedbackBusinessImplTest {
	@InjectMocks
	SaveFeedbackBusinessImpl saveFeedbackBusinessImpl;
	@Mock
	private SaveFeedbackService saveFeedbackService;
	
	@Mock
	private PnrInvokeService pnrInvokeService;
	
	@Mock
	private TicketProcessInvokeService ticketProcessInvokeService;
	
	@Mock
	private PaxNameIdentificationService paxNameIdentificationService;

	@Test
	public void saveFeedBackTest() throws BusinessBaseException {
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setLoginType("M");
		SaveFeedbackRequestDTO requestDTO=new SaveFeedbackRequestDTO();
		requestDTO.setRloc("NMC25");
		RetrievePnrBooking pnrBooking=new RetrievePnrBooking();
		List<RetrievePnrPassenger> passengers =new ArrayList<>();
		RetrievePnrPassenger passenger=new RetrievePnrPassenger();
		passenger.setPassengerID("1");
		passenger.setPrimaryPassenger(true);
		passenger.setLoginMember(false);
		passengers.add(passenger);
		pnrBooking.setPassengers(passengers);
		List<RetrievePnrPassengerSegment> passengerSegments=new ArrayList<>();
		RetrievePnrPassengerSegment passengerSegment=new RetrievePnrPassengerSegment();
		passengerSegment.setPassengerId("1");
		passengerSegments.add(passengerSegment);
		pnrBooking.setPassengerSegments(passengerSegments);
		
		doNothing().when(saveFeedbackService).saveFeedback(requestDTO, requestDTO.getRloc(), null);
		doNothing().when(paxNameIdentificationService).primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
		when(pnrInvokeService.retrievePnrByRloc(requestDTO.getRloc())).thenReturn(pnrBooking);
		SaveFeedbackResponseDTO saveFeedbackResponseDTO=saveFeedbackBusinessImpl.saveFeedback(loginInfo, requestDTO);
		assertEquals(true,saveFeedbackResponseDTO.isSuccess());
	}
	@Test
	public void saveFeedBackTest1() throws BusinessBaseException {
		LoginInfo loginInfo=null;
		SaveFeedbackRequestDTO requestDTO=null;
		SaveFeedbackResponseDTO saveFeedbackResponseDTO=saveFeedbackBusinessImpl.saveFeedback(loginInfo, requestDTO);
		assertEquals(false,saveFeedbackResponseDTO.isSuccess());
	}
	@Test
	public void saveFeedBackTest2() throws BusinessBaseException {
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setLoginType("M");
		SaveFeedbackRequestDTO requestDTO=new SaveFeedbackRequestDTO();
		RetrievePnrBooking pnrBooking=new RetrievePnrBooking();
		List<RetrievePnrPassenger> passengers =new ArrayList<>();
		RetrievePnrPassenger passenger=new RetrievePnrPassenger();
		passenger.setPassengerID("1");
		passenger.setPrimaryPassenger(true);
		passenger.setLoginMember(false);
		passengers.add(passenger);
		pnrBooking.setPassengers(passengers);
		List<RetrievePnrPassengerSegment> passengerSegments=new ArrayList<>();
		RetrievePnrPassengerSegment passengerSegment=new RetrievePnrPassengerSegment();
		passengerSegment.setPassengerId("1");
		passengerSegments.add(passengerSegment);
		pnrBooking.setPassengerSegments(passengerSegments);
		doNothing().when(saveFeedbackService).saveFeedback(requestDTO, requestDTO.getRloc(), null);
		doNothing().when(paxNameIdentificationService).primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
		when(pnrInvokeService.retrievePnrByRloc(requestDTO.getRloc())).thenReturn(pnrBooking);
		try {
			saveFeedbackBusinessImpl.saveFeedback(loginInfo, requestDTO);
			assertEquals("1",pnrBooking.getPassengers().get(0).getPassengerID());
			assertEquals(true,pnrBooking.getPassengers().get(0).isPrimaryPassenger());
			assertEquals(false,pnrBooking.getPassengers().get(0).isLoginMember());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
