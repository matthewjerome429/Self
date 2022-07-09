package com.cathaypacific.mmbbizrule.handler;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mmbbizrule.db.model.TbPortFlight;
import com.cathaypacific.olciconsumer.model.response.db.TravelDocDisplay;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mbcommon.enums.staff.StaffBookingType;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.db.dao.TbPortFlightDAO;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocDisplayCacheHelper;
import com.cathaypacific.mmbbizrule.dto.common.booking.FlightBookingDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.ContactInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.Email;
import com.cathaypacific.mmbbizrule.model.booking.detail.MealDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.MealSelection;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.PhoneInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatPreference;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatSelection;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SegmentStatus;
import com.cathaypacific.mmbbizrule.model.booking.detail.TS;
import com.cathaypacific.mmbbizrule.model.booking.detail.TravelDoc;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrStaffDetail;
import com.cathaypacific.mmbbizrule.util.security.EncryptionHelper;


@RunWith(MockitoJUnitRunner.class)
public class DTOConverterTest {
	@InjectMocks
	private DTOConverter dTOConverter;
	@Mock
	TbTravelDocDisplayCacheHelper tbTravelDocDisplayCacheHelper;
	
	@Mock
	TbPortFlightDAO tbPortFlightDAO;
	@Mock
	EncryptionHelper encryptionHelper;
	@Mock
	BizRuleConfig bizRuleConfig;
	@Test
	public void convertToBookingDTO_Test() {
		Booking booking=new  Booking();
		booking.setOneARloc("ENHSJ");
		booking.setSpnr("123");
		booking.setCanCheckIn(true);
		booking.setRedemptionBooking(true);
		booking.setCanIssueTicket(true);
		booking.setEncryptedRloc("345");
		booking.setMandatoryContactInfo(true);
		List<Segment> segments=new ArrayList<>();
		Segment segment=new Segment();
		segment.setCabinClass("456");
		DepartureArrivalTime departureTime1=new DepartureArrivalTime();
		departureTime1.setTimeZoneOffset("+0800");
		departureTime1.setRtfsScheduledTime("2018-04-28 17:14");
		departureTime1.setRtfsActualTime("2018-04-28 17:14");
		segment.setDepartureTime(departureTime1);
		SegmentStatus segmentStatus=new SegmentStatus();
		segmentStatus.setStatus(FlightStatusEnum.CONFIRMED);
		segment.setSegmentStatus(segmentStatus);
		segments.add(segment);
		booking.setSegments(segments);
		List<Passenger> passengers=new ArrayList<>();
		Passenger passenger=new Passenger();
		passenger.setPassengerId("1");
		ContactInfo contactInfo=new ContactInfo();
		Email email=new Email();
		email.setType("E");
		email.setEmailAddress("123@qq.com");
		contactInfo.setEmail(email);
		PhoneInfo phoneInfo=new PhoneInfo();
		phoneInfo.setCountryCode("SH");
		phoneInfo.setPhoneNo("0211122");
		phoneInfo.setType("S");
		contactInfo.setPhoneInfo(phoneInfo);
		passenger.setContactInfo(contactInfo);
		passengers.add(passenger);
		booking.setPassengers(passengers);
		List<PassengerSegment> passengerSegments=new ArrayList<>();
		PassengerSegment passengerSegment=new PassengerSegment();
		passengerSegment.setPassengerId("1");
		//passengerSegments.add(passengerSegment);
		booking.setPassengerSegments(passengerSegments);
		LoginInfo loginInfo=new LoginInfo();
		List<TravelDocDisplay> tbTravelDocDisplays=new ArrayList<>();
		TravelDocDisplay tbTravelDocDisplay=new TravelDocDisplay();

		tbTravelDocDisplays.add(tbTravelDocDisplay);
		when(tbTravelDocDisplayCacheHelper.findAll()).thenReturn(tbTravelDocDisplays);
		List<TbPortFlight> tbPortFlights=new ArrayList<>();
		TbPortFlight tbPortFlight=new TbPortFlight();
		tbPortFlight.setAppCode("MMB");
		tbPortFlight.setOrigin("***");
		tbPortFlight.setFlightNumFrom(0);
		tbPortFlight.setFlightNumTo(9999);
		tbPortFlight.setAirlineCode("**");
		tbPortFlight.setDestination("***");
		tbPortFlights.add(tbPortFlight);
		when(tbPortFlightDAO.findByAppCode(MMBConstants.APP_CODE)).thenReturn(tbPortFlights);
		FlightBookingDTO flightBookingDTO=dTOConverter.convertToBookingDTO(booking, loginInfo);
		Assert.assertEquals("123@qq.com", flightBookingDTO.getPassengers().get(0).getContactInfo().getEmail().getEmail());
		Assert.assertEquals("SH", flightBookingDTO.getPassengers().get(0).getContactInfo().getPhoneInfo().getCountryCode());
		Assert.assertEquals("1", flightBookingDTO.getPassengers().get(0).getPassengerId());
		Assert.assertEquals("456", flightBookingDTO.getSegments().get(0).getCabinClass());
		Assert.assertEquals(0, flightBookingDTO.getPassengerSegments().size());
	}
	@Test
	public void convertToBookingDTO_Test1() {
		Booking booking=new Booking();
		List<Segment> segments=new ArrayList<>();
		Segment segment=new Segment();
		segment.setCabinClass("456");
		segment.setSegmentID("1");
		DepartureArrivalTime departureTime=new DepartureArrivalTime();
		departureTime.setTimeZoneOffset("+0800");
		departureTime.setPnrTime("2018-04-29 17:14");
		segment.setDepartureTime(departureTime);
		SegmentStatus segmentStatus=new SegmentStatus();
		segmentStatus.setStatus(FlightStatusEnum.CONFIRMED);
		segment.setSegmentStatus(segmentStatus);
		segments.add(segment);
		booking.setSegments(segments);
		List<PassengerSegment> passengerSegments=new ArrayList<>();
		PassengerSegment passengerSegment=new PassengerSegment();
		passengerSegment.setPassengerId("1");
		passengerSegment.setSegmentId("1");

		MealDetail meal=new MealDetail();
		meal.setCompanyId("CX");
		meal.setMealCode("DD");
		meal.setQuantity(12);
		meal.setStatus("Good");
		passengerSegment.setMeal(meal);
		SeatDetail seat=new SeatDetail();
		seat.setSeatNo("J");
		seat.setExlSeat(true);
		seat.setPaid(true);
		seat.setStatus("K");
		seat.setFromDCS(true);
		passengerSegment.setSeat(seat);
		List<SeatDetail> extraSeats=new ArrayList<>();
		SeatDetail extraSeat=new SeatDetail();
		extraSeat.setSeatNo("789");
		extraSeat.setExlSeat(true);
		extraSeat.setPaid(true);
		extraSeats.add(extraSeat);
		passengerSegment.setExtraSeats(extraSeats);
		
		SeatPreference preference=new SeatPreference();
		preference.setPreferenceCode("BN");
		preference.setSpeicalPreference(true);
		passengerSegment.setPreference(preference);
		
		SeatSelection seatSelection=new SeatSelection();
		seatSelection.setEligible(true);
		seatSelection.setLowRBD(true);
		seatSelection.setPaidASR(true);
		List<String> disabilities=new ArrayList<>();
		seatSelection.setDisabilities(disabilities);
		passengerSegment.setMmbSeatSelection(seatSelection);
		
		MealSelection mealSelection=new MealSelection();
		
		passengerSegment.setMealSelection(mealSelection);
		
		passengerSegments.add(passengerSegment);
		passengerSegments.add(passengerSegment);
		booking.setPassengerSegments(passengerSegments);
		List<Passenger> passengers=new ArrayList<>();
		Passenger passenger=new Passenger();
		passenger.setPassengerId("1");
		RetrievePnrStaffDetail staffDetail = new RetrievePnrStaffDetail();
		staffDetail.setType(StaffBookingType.codeOf("ID"));
		passenger.setStaffDetail(staffDetail);
		List<TravelDoc> priTravelDocs=new ArrayList<>();
		TravelDoc priTravelDoc=new TravelDoc();
		//priTravelDoc.setQualifierId(b);
		priTravelDoc.setBirthDateDay("02");
		priTravelDoc.setExpiryDateYear("2019");
		priTravelDoc.setExpiryDateDay("02");
		priTravelDoc.setExpiryDateMonth("05");
		priTravelDoc.setBirthDateMonth("04");
		priTravelDoc.setBirthDateYear("2018");
		priTravelDoc.setFamilyName("TEST");
		priTravelDoc.setGivenName("LIAN");
		//priTravelDoc.setQualifierId(b);
		priTravelDoc.setNationality("CN");
		priTravelDoc.setTravelDocumentNumber("147");
		priTravelDoc.setTravelDocumentType("ZF");
		priTravelDoc.setCountryOfIssuance("123");
		priTravelDocs.add(priTravelDoc);
		passenger.setPriTravelDocs(priTravelDocs);
		passenger.setSecTravelDocs(priTravelDocs);
		TS ktn=new TS();
		ktn.setNumber("147258");
		passenger.setKtn(ktn);
		TS redress=new TS();
		redress.setNumber("369852");
		passenger.setRedress(redress);
		passengers.add(passenger);
		booking.setPassengers(passengers);
		booking.setMandatoryContactInfo(true);
		LoginInfo loginInfo=new LoginInfo();
		FlightBookingDTO flightBookingDTO=dTOConverter.convertToBookingDTO(booking, loginInfo);
		Assert.assertEquals("1", flightBookingDTO.getPassengers().get(0).getPassengerId());
		Assert.assertEquals("456", flightBookingDTO.getSegments().get(0).getCabinClass());
		Assert.assertEquals(2, flightBookingDTO.getPassengerSegments().size());
		Assert.assertEquals(true, booking.isStaffBooking());
	}
	
	@Test
	public void convertToBookingDTO_GenderDOBTest() throws UnexpectedException {
		Booking booking=new Booking();
		List<Segment> segments=new ArrayList<>();
		Segment segment1=new Segment();
		Segment segment2=new Segment();

		segment1.setSegmentID("1");
		DepartureArrivalTime departureTime1=new DepartureArrivalTime();
		departureTime1.setTimeZoneOffset("+0800");
		departureTime1.setRtfsScheduledTime("2018-04-28 17:14");
		departureTime1.setRtfsActualTime("2018-04-28 17:14");
		segment1.setDepartureTime(departureTime1);
		SegmentStatus segmentStatus1=new SegmentStatus();
		segmentStatus1.setStatus(FlightStatusEnum.CONFIRMED);
		segment1.setSegmentStatus(segmentStatus1);
		segment1.setMarketCabinClass("Y");
		segment1.setMarketSubClass("S");
		segment2.setSegmentID("2");
		DepartureArrivalTime departureTime2=new DepartureArrivalTime();
		departureTime2.setTimeZoneOffset("+0800");
		departureTime2.setRtfsScheduledTime("2018-04-29 17:14");
		departureTime2.setRtfsActualTime("2018-04-28 17:14");
		segment2.setDepartureTime(departureTime2);
		SegmentStatus segmentStatus2=new SegmentStatus();
		segmentStatus2.setStatus(FlightStatusEnum.CONFIRMED);
		segment2.setSegmentStatus(segmentStatus2);	
		segment2.setMarketCabinClass("Y");
		segment2.setMarketSubClass("S");
		segments.add(segment1);
		segments.add(segment2);
		booking.setSegments(segments);
		List<PassengerSegment> passengerSegments=new ArrayList<>();
		PassengerSegment passengerSegment1=new PassengerSegment();
		passengerSegment1.setPassengerId("1");
		passengerSegment1.setSegmentId("2");
		
		PassengerSegment passengerSegment2=new PassengerSegment();
		passengerSegment2.setPassengerId("1");
		passengerSegment2.setSegmentId("1");

		passengerSegments.add(passengerSegment1);
		passengerSegments.add(passengerSegment2);
		booking.setPassengerSegments(passengerSegments);
		List<Passenger> passengers=new ArrayList<>();
		Passenger passenger=new Passenger();
		passenger.setPassengerId("1");
		
		passengers.add(passenger);
		booking.setPassengers(passengers);
		booking.setMandatoryContactInfo(true);
		LoginInfo loginInfo=new LoginInfo();
		when(encryptionHelper.encryptMessage(anyObject(), anyObject(), anyObject())).thenReturn("uMD6Cd/zty/r5kTToHxnq0iyRYjS2lFZ/4rfMi1sz1U=");
		FlightBookingDTO flightBookingDTO=dTOConverter.convertToBookingDTO(booking, loginInfo);
		Assert.assertEquals("1", flightBookingDTO.getPassengers().get(0).getPassengerId());
		Assert.assertEquals("Y", flightBookingDTO.getSegments().get(0).getMarketCabinClass());
		Assert.assertEquals("S", flightBookingDTO.getSegments().get(0).getMarketSubClass());
	}
}
