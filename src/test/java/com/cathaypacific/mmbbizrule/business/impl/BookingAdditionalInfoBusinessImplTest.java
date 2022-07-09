package com.cathaypacific.mmbbizrule.business.impl;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.commonapi.TaggingBusiness;
import com.cathaypacific.mmbbizrule.config.OLCIConfig;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.common.ProductTypeEnum;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductsResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.service.EcommService;
import com.cathaypacific.mmbbizrule.dto.request.bookingproperties.additional.BookingAdditionalInfoRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.additional.BookingAdditionalInfoResponseDTO;
import com.cathaypacific.mmbbizrule.handler.JourneyCalculateHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatSelection;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.journey.JourneySegment;
import com.cathaypacific.mmbbizrule.model.journey.JourneySummary;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.google.common.collect.Lists;
@RunWith(MockitoJUnitRunner.class)
public class BookingAdditionalInfoBusinessImplTest {
	@InjectMocks
	BookingAdditionalInfoBusinessImpl bookingAdditionalInfoBusinessImpl;
	
	@Mock
	private OLCIConfig olciConfig;

	@Mock
	private PnrInvokeService pnrInvokeService;

	@Mock
	private BookingBuildService bookingBuildService;
	
	@Mock
	private PaxNameIdentificationService paxNameIdentificationService;
	
	@Mock
	private JourneyCalculateHelper journeyCalculateHelper;

	@Mock
	private EcommService ecommService;
	
	@Mock
	private TaggingBusiness taggingBusiness;
	
	private String rloc; 
	
	private LoginInfo loginInfo;
	
	private String language;
	
	BookingAdditionalInfoRequestDTO requestDTO;
	RetrievePnrBooking retrievePnrBooking;
	Booking booking;
	List<JourneySummary> journeys;
	ProductsResponseDTO productsResponseDTO;
	
	@Before
	public void setUp() throws BusinessBaseException {
		loginInfo=new LoginInfo();
		loginInfo.setMmbToken("qwertyu");
		rloc="K7U6LI"; 
		language="en";
		requestDTO =new BookingAdditionalInfoRequestDTO();
		requestDTO.setAncillaryASRSeatBanner(true);
		requestDTO.setAncillaryEXLSeatBanner(true);
		requestDTO.setAncillaryBaggageBanner(true);
		retrievePnrBooking=new RetrievePnrBooking();
		retrievePnrBooking.setOneARloc(rloc);
		List<RetrievePnrPassenger> passengers =new ArrayList<>();
		RetrievePnrPassenger passenger=new RetrievePnrPassenger();
		passenger.setPassengerID("1");
		passenger.setPrimaryPassenger(new Boolean(true));
		passengers.add(passenger);
		retrievePnrBooking.setPassengers(passengers);
		booking =new Booking();
		List<Passenger> paxs = Lists.newArrayList();
		Passenger pax = new Passenger();
		pax.setPassengerId("1");
		pax.setPrimaryPassenger(new Boolean(true));
		paxs.add(pax);
		booking.setPassengers(paxs);
		List<Segment> segments = new ArrayList<>();
		Segment segment=new Segment();
		segment.setSegmentID("1");
		DepartureArrivalTime departureTime =new DepartureArrivalTime();
		 Date date = new Date();  
		 Calendar calendar = Calendar.getInstance();    
		 calendar.setTime(date);    
		 calendar.add(Calendar.MONTH, 1);
		 calendar.getTime();
		 SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		 sf.format(calendar.getTime());
		departureTime.setRtfsActualTime(sf.format(calendar.getTime()).toString());
		departureTime.setTimeZoneOffset("+0800");
		segment.setDepartureTime(departureTime);
		segment.setAirCraftType("333");
		segment.setCabinClass("Y");
		segments.add(segment);
		booking.setSegments(segments);
		List<PassengerSegment> passengerSegments = new ArrayList<>();
		PassengerSegment passengerSegment=new PassengerSegment();
		passengerSegment.setSegmentId("1");
		passengerSegment.setPassengerId("1");
		SeatSelection seatSelection = new SeatSelection();
		seatSelection.setEligible(true);
		seatSelection.setXlFOC(false);
		passengerSegment.setMmbSeatSelection(seatSelection);
		SeatDetail seat = new SeatDetail();
		seat.setSeatNo(null);
		passengerSegment.setSeat(seat);
		passengerSegments.add(passengerSegment);
		booking.setPassengerSegments(passengerSegments);
		journeys=new ArrayList<>();
		JourneySummary journey=new JourneySummary();
		List<JourneySegment> jSegments =new ArrayList<>();
		JourneySegment jSegment=new JourneySegment();
		jSegment.setSegmentId("1");
		jSegments.add(jSegment);
		journey.setSegments(jSegments);
		journeys.add(journey);
		productsResponseDTO=new ProductsResponseDTO();
		List<ProductDTO> products = new ArrayList<>();
		ProductDTO exlProduct = new ProductDTO();
		ProductDTO baggageProduct = new ProductDTO();
		ProductDTO asrProduct = new ProductDTO();
		exlProduct.setProductType(ProductTypeEnum.SEAT_EXTRA_LEG_ROOM);
		exlProduct.setPassengerId("1");
		List<String> segmentIds = new ArrayList<>();
		segmentIds.add("1");
		segmentIds.add("2");
		asrProduct.setProductType(ProductTypeEnum.SEAT_ASR_REGULAR);
		asrProduct.setPassengerId("2");
		asrProduct.setSegmentIds(segmentIds);
		baggageProduct.setProductType(ProductTypeEnum.BAGGAGE_COMMON);
		baggageProduct.setPassengerId("1");
		baggageProduct.setSegmentIds(segmentIds);
		exlProduct.setSegmentIds(segmentIds);
		products.add(exlProduct);
		products.add(asrProduct);
		products.add(baggageProduct);
		productsResponseDTO.setProducts(products);
		when(pnrInvokeService.retrievePnrByRloc(rloc)).thenReturn(retrievePnrBooking);
		when(journeyCalculateHelper.calculateJourneyFromDpEligibility(retrievePnrBooking.getOneARloc())).thenReturn(journeys);
		when(ecommService.getEcommEligibleProducts(rloc,
				retrievePnrBooking.getPos(), loginInfo.getMmbToken())).thenReturn(productsResponseDTO);
		when(taggingBusiness.checkRedemptionBannerUpgrade(booking)).thenReturn(true);
		
		doNothing().when(paxNameIdentificationService).primaryPassengerIdentificationByInFo(loginInfo, retrievePnrBooking);
		when(bookingBuildService.buildBooking(anyObject(), anyObject(),
				anyObject())).thenReturn(booking);
		
	}
	@Test
	public void getBookingAdditional_ASRAndEXLBanner() throws BusinessBaseException {
	
		BookingAdditionalInfoResponseDTO additionalInfo=bookingAdditionalInfoBusinessImpl.getBookingAdditional(loginInfo, rloc, language, requestDTO);
		Assert.assertEquals("1", additionalInfo.getPassengerSegments().get(0).getPassengerId());
		Assert.assertEquals("1", additionalInfo.getPassengerSegments().get(0).getSegmentId());
		Assert.assertEquals(true, additionalInfo.getPassengerSegments().get(0).isAsrBannerEligible());
		Assert.assertEquals(true, additionalInfo.getPassengerSegments().get(0).isExlBannerEligible());
		Assert.assertEquals(false, additionalInfo.getPassengerSegments().get(0).isBaggageBannerEligible());
	}

}
