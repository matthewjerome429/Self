package com.cathaypacific.mmbbizrule.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.config.BookingStatusConfig;
import com.cathaypacific.mmbbizrule.constant.CommonConstants;
import com.cathaypacific.mmbbizrule.cxservice.seatmap.service.SeatMapService;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.PaxSeatDetail;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.UpdateSeatRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.seatmap.GridDTO;
import com.cathaypacific.mmbbizrule.dto.response.seatmap.GridRowDTO;
import com.cathaypacific.mmbbizrule.dto.response.seatmap.RetrieveSeatMapDTO;
import com.cathaypacific.mmbbizrule.dto.response.seatmap.SeatCharacteristicDTO;
import com.cathaypacific.mmbbizrule.dto.response.seatmap.SeatDetailsDTO;
import com.cathaypacific.mmbbizrule.dto.response.seatmap.SeatMapDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.FQTVInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatSelection;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SpecialSeatEligibility;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnrcancel.service.DeletePnrService;
import com.cathaypacific.mmbbizrule.oneaservice.updateseat.service.AddSeatService;
import com.cathaypacific.oneaconsumer.model.header.Session;

@RunWith(MockitoJUnitRunner.class)
public class UpdateSeatServiceImplTest {
	
	@InjectMocks
	UpdateSeatServiceImpl updateSeatServiceImpl;
	
	@Mock
	AddSeatService addSeatService;
	
	@Mock
	DeletePnrService deletePnrService;
	
	@Mock
	private BookingStatusConfig bookingStatusConfig;
	
	@Mock
	private SeatMapService seatMapService;
	
	@Mock
	private RestTemplate restTemplate;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void test()throws BusinessBaseException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		UpdateSeatRequestDTO requestDTO=new UpdateSeatRequestDTO();
		List<PaxSeatDetail> paxSeatDetails=new ArrayList<>();
		PaxSeatDetail paxSeatDetail=new PaxSeatDetail();
		RetrievePnrBooking retrievePnrBooking=new RetrievePnrBooking();
		List<RetrievePnrPassenger> passengers=new ArrayList<>();
		RetrievePnrPassenger passenger=new RetrievePnrPassenger();
		List<RetrievePnrPassengerSegment> passengerSegments=new ArrayList<>();
		RetrievePnrPassengerSegment passengerSegment=new RetrievePnrPassengerSegment();
		Booking booking=new Booking();
		List<RetrievePnrSegment> segmentss=new ArrayList<>();
		List<PassengerSegment> passengerSegmentss=new ArrayList<>();
		PassengerSegment passengersss=new PassengerSegment();
		FQTVInfo fQTVInfo=new FQTVInfo();
		List<Segment> segments=new ArrayList<>();
		Segment segmentssss=new Segment();
		DepartureArrivalTime departureTime=new DepartureArrivalTime();
		
		segmentssss.setSegmentID("1");
		segmentssss.setOriginPort("SHK");
		segmentssss.setDestPort("SHK");
		segmentssss.setMarketCompany("CX");
		segmentssss.setMarketSegmentNumber("198654");
		segmentssss.setCabinClass("323223");
		departureTime.setPnrTime("2018-03-25 08:00");
		segmentssss.setDepartureTime(departureTime);
		segments.add(segmentssss);
		fQTVInfo.setMembershipNumber("369258");
		fQTVInfo.setCompanyId("64");
		fQTVInfo.setTopTier(true);
		passengersss.setPassengerId("1");
		passengersss.setSegmentId("1");
		passengersss.setFqtvInfo(fQTVInfo);
		passengersss.setSeatQulifierId(new BigInteger("1"));
		passengersss.setMmbSeatSelection(new SeatSelection());
		passengersss.getMmbSeatSelection().setEligible(true);
		passengersss.getMmbSeatSelection().setSpecialSeatEligibility(new SpecialSeatEligibility());
		passengersss.getMmbSeatSelection().getSpecialSeatEligibility().setExlSeat(true);
		passengersss.getMmbSeatSelection().setAsrFOC(true);
		passengersss.getMmbSeatSelection().setXlFOC(true);
		passengersss.setEticketNumber("160888888");
		passengerSegmentss.add(passengersss);
		passengerSegment.setPassengerId("1");
		passengerSegments.add(passengerSegment);
		passenger.setPassengerID("1");
		passengers.add(passenger);
		retrievePnrBooking.setPassengers(passengers);
		RetrievePnrSegment segmentsss=new RetrievePnrSegment();
		segmentsss.setOriginPort("SHK");
		segmentsss.setPnrOperateCompany("CX");
		segmentsss.setSegmentID("1");
		segmentss.add(segmentsss);
		retrievePnrBooking.setSegments(segmentss);
		retrievePnrBooking.setPassengerSegments(passengerSegments);
		Session session =new Session();		
		retrievePnrBooking.setSession(session);
		
//		String seatMapLink ="http://localhost:8080/v1/retrieveSeatMap";
//		Field limithoursField = UpdateSeatServiceImpl.class.getDeclaredField("seatMapLink");
//		limithoursField.setAccessible(true);
//		limithoursField.set(updateSeatServiceImpl, seatMapLink);
		
		paxSeatDetail.setPassengerID("1");
		paxSeatDetail.setSeatNo("61A");
		paxSeatDetail.setExlSeat(true);
		paxSeatDetail.setSeatPreference("HJK");
		paxSeatDetails.add(paxSeatDetail);
		requestDTO.setPaxSeatDetails(paxSeatDetails);
		requestDTO.setRloc("MNHD5");
		requestDTO.setSegmentId("1");
		List<RetrievePnrDataElements> skLists=new ArrayList<>();
		RetrievePnrDataElements skList=new RetrievePnrDataElements();
		
		skList.setPassengerId("1");
		//skList.setSegmentId("1");
		skList.setType("XLWR");
		skList.setQulifierId(new BigInteger("38"));
		skLists.add(skList);
		booking.setPassengerSegments(passengerSegmentss);
		booking.setSegments(segments);
		booking.setSkList(skLists);
		LoginInfo loginInfo=new LoginInfo();
		loginInfo.setLoginType("R");
		loginInfo.setLoginFamilyName("TEST");
		loginInfo.setLoginGivenName("LAIN");
		loginInfo.setMemberId("196185");
		loginInfo.setLoginEticket("147258369");
		loginInfo.setMmbToken("75845555");
		loginInfo.setLoginRloc("LOFN8K");
		
//		StringBuilder linkSb = new StringBuilder(seatMapLink);
//		linkSb.append("?departureDate=").append("250318");
//		linkSb.append("&originAirportCode=").append("SHK");
//		linkSb.append("&destinationAirportCode=").append("SHK");
//		linkSb.append("&marketingCompany=").append("CX");
//		linkSb.append("&flightNum=").append("198654");
//		linkSb.append("&bookingClass=").append("323223");
//		linkSb.append("&rloc=").append("LOFN8K");
		
		RetrieveSeatMapDTO seatMaps=new RetrieveSeatMapDTO();
		ResponseEntity<RetrieveSeatMapDTO> seatMap =new  ResponseEntity<RetrieveSeatMapDTO>(seatMaps, HttpStatus.OK);
		
		List<SeatMapDTO> seatMapss = new ArrayList<>();
		List<GridRowDTO> rows =new ArrayList<>();
		List<GridDTO> grids =new ArrayList<>();
		GridDTO gridDTO=new GridDTO();
		SeatDetailsDTO seatDetails=new SeatDetailsDTO();
		SeatCharacteristicDTO seatCharacteristic =new SeatCharacteristicDTO();
		
		seatCharacteristic.setIsLegSpace(true);
		seatDetails.setSeatCharacteristic(seatCharacteristic);
		gridDTO.setColumn("A");
		gridDTO.setSeatDetails(seatDetails);
		grids.add(gridDTO);
		
		GridRowDTO rowDTO=new GridRowDTO();
		rowDTO.setRowNum(61);
		rowDTO.setGrids(grids);
		rows.add(rowDTO);
		SeatMapDTO seatMapsss=new SeatMapDTO();
		seatMapsss.setBookingClass("323223");
		seatMapsss.setFlightNumber("198654");
		seatMapsss.setOrigin("SHK");
		seatMapsss.setDestination("SHK");
		seatMapsss.setRows(rows);
		seatMapss.add(seatMapsss);
		seatMaps.setSeatMap(seatMapss);
		seatMap.getBody().setSeatMap(seatMapss);
		
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("MMB-Token", "75845555");
        
        HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
        Map<String, List<String>> deleteMap =new HashMap<String, List<String>>();
        List<String> qulifierList=new ArrayList<>();
        qulifierList.add(skList.getQulifierId().toString());
        deleteMap.put(CommonConstants.MAP_SEAT_KEY, qulifierList);
		
		when(restTemplate.postForObject(Matchers.anyString(), Matchers.anyObject(), Matchers.any())).thenReturn(seatMaps);
		when(deletePnrService.deletePnr(Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(retrievePnrBooking);
		
		updateSeatServiceImpl.updateSeat(requestDTO, loginInfo,booking);
		assertEquals("1", retrievePnrBooking.getPassengers().get(0).getPassengerID());
		assertEquals("1", retrievePnrBooking.getPassengerSegments().get(0).getPassengerId());
		
		passengersss.getMmbSeatSelection().setEligible(false);
		thrown.expect(UnexpectedException.class);
		thrown.expectMessage("Ineligible to update the seat.");
		updateSeatServiceImpl.updateSeat(requestDTO, loginInfo,booking);
		
	}
	
}
