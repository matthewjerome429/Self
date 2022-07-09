package com.cathaypacific.mmbbizrule.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.db.dao.ConsentRecordDAO;
import com.cathaypacific.mmbbizrule.dto.response.retrievepnr.ConsentInfoRecordResponseDTO;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDepartureArrivalTime;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEticket;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;

@RunWith(MockitoJUnitRunner.class)
public class ConsentInfoServiceImplTest {

	@InjectMocks
	private ConsentInfoServiceImpl consentInfoServiceImpl;
	
	@Mock
	private ConsentRecordDAO consentRecordDAO;
	
	@Mock
	private AEMService aemService;
	
	@Test
	public void test_saveConsentInfo() throws BusinessBaseException{
		
		RetrievePnrBooking retrievePnrBooking = new RetrievePnrBooking();
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.setLoginType("M");
		loginInfo.setMemberId("147258");
		String rloc = "LoginInfo";
		String acceptLanguage = "en-UK";
		RetrievePnrPassenger retrievePassenger = new RetrievePnrPassenger();
		retrievePassenger.setFamilyName("QIN");
		retrievePassenger.setGivenName("DONGDONG");
		retrievePassenger.setPassengerID("1234567");
		retrievePassenger.setPassengerType("ADT");
		List<RetrievePnrPassenger> passengers = new ArrayList<>();
		passengers.add(retrievePassenger);
		retrievePnrBooking.setPassengers(passengers);
		
		RetrievePnrSegment segment = new RetrievePnrSegment();
		//segment.setIsFlown(false);
		segment.setOriginPort("HKG");
		segment.setSegmentID("1");
		segment.setMarketSegmentNumber("FA2593");
		segment.setSubClass("Z");
		List<String> status =new ArrayList<>();
		status.add("UNN");
		segment.setStatus(status);
		segment.setOriginPort("HKG");
		segment.setDestPort("CN");
		RetrievePnrDepartureArrivalTime departureTime=new RetrievePnrDepartureArrivalTime();
		departureTime.setTimeZoneOffset("+8800");
		departureTime.setPnrTime("2018-03-22 14:00");
		segment.setDepartureTime(departureTime);
		
		RetrievePnrSegment segment2 = new RetrievePnrSegment();
		//segment.setIsFlown(false);
		segment2.setOriginPort("HKG");
		segment2.setSegmentID("1");
		segment2.setMarketSegmentNumber("FA2593");
		segment2.setSubClass("Z");
		List<String> status2 =new ArrayList<>();
		status.add("UNN");
		segment2.setStatus(status2);
		segment2.setOriginPort("HKG");
		segment2.setDestPort("CN");
		RetrievePnrDepartureArrivalTime departureTime2=new RetrievePnrDepartureArrivalTime();
		departureTime2.setTimeZoneOffset("+8800");
		departureTime2.setPnrTime("2018-04-22 14:00");
		segment2.setDepartureTime(departureTime2);
		
		List<RetrievePnrSegment> segments = new ArrayList<>();
		segments.add(segment);
		segments.add(segment2);
		retrievePnrBooking.setSegments(segments);
		List<RetrievePnrPassengerSegment> passengerSegments =new ArrayList<>();
		RetrievePnrPassengerSegment passengerSegment = new RetrievePnrPassengerSegment();
		List<RetrievePnrEticket> etickets = new ArrayList<>();
		RetrievePnrEticket retrievePnrEticket = new RetrievePnrEticket();
		retrievePnrEticket.setTicketNumber("123456789");
		etickets.add(retrievePnrEticket);
		passengerSegment.setSegmentId("1");
		passengerSegment.setPassengerId("1234567");
		passengerSegment.setEtickets(etickets);
		passengerSegments.add(passengerSegment);
		retrievePnrBooking.setPassengerSegments(passengerSegments);
		ConsentInfoRecordResponseDTO consentInfoResponse = consentInfoServiceImpl.saveConsentInfo(retrievePnrBooking, loginInfo, rloc, acceptLanguage);
		Assert.assertEquals(false, consentInfoResponse.isConsentInfoRecord());

	}
}
