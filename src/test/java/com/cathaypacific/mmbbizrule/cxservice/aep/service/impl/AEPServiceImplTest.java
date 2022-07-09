/**
 * 
 */
package com.cathaypacific.mmbbizrule.cxservice.aep.service.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPAirProduct;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPPassengerWithName;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProduct;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProductsResponse;
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;

@RunWith(MockitoJUnitRunner.class)
public class AEPServiceImplTest {

	@InjectMocks
	private AEPServiceImpl aepServiceImpl;
	
	@Mock
	private AEPServiceCacheHelper aepServiceCacheHelper;
	
	@Mock
	private AEMService aemService;
	
	@Test
	public void testGetBookingProductsForBaggage() throws BusinessBaseException {
		Booking booking=new Booking();
		List<Passenger> passengers = new ArrayList<>();
		Passenger passenger = new Passenger();
		passenger.setPassengerId("1");
		passengers.add(passenger);
		booking.setPassengers(passengers);
		
		List<Segment> segments=new ArrayList<>();
		Segment segment=new Segment();
		DepartureArrivalTime departureTime=new DepartureArrivalTime();
		segment.setSegmentID("1");
		segment.setOriginPort("HKG");
		segment.setDestPort("LOS");
		segment.setMarketCompany("CX");
		segment.setMarketSegmentNumber("332");
		segment.setCabinClass("Y");
		departureTime.setPnrTime("2018-03-25 08:00");
		segment.setDepartureTime(departureTime);
		segments.add(segment);
		booking.setSegments(segments);
		
		List<PassengerSegment> passengerSegments=new ArrayList<>();
		PassengerSegment passengerSegment=new PassengerSegment();
		passengerSegment.setPassengerId("1");
		passengerSegment.setSegmentId("1");
		passengerSegments.add(passengerSegment);
		booking.setPassengerSegments(passengerSegments);

		AEPProductsResponse mockResponse = new AEPProductsResponse();
		mockResponse.setProducts(new ArrayList<>());
		
		AEPProduct mockAEPProduct = new AEPProduct();
		mockResponse.getProducts().add(mockAEPProduct);
		
		mockAEPProduct.setProductId("2");
		mockAEPProduct.setAirProduct(new ArrayList<>());
		AEPAirProduct mockAirProduct = new AEPAirProduct();
		mockAEPProduct.getAirProduct().add(mockAirProduct);
		
		mockAirProduct.setPassengers(new ArrayList<>());
		AEPPassengerWithName aepPassenger = new AEPPassengerWithName();
		aepPassenger.setPassengerRef(Integer.valueOf("2"));
		mockAirProduct.getPassengers().add(aepPassenger);
		
		mockAirProduct.setSegments(new ArrayList<>());
		AEPSegment mockAEPSegment1 = new AEPSegment();
		mockAEPSegment1.setSegmentRef(Integer.valueOf("2"));
		AEPSegment mockAEPSegment2 = new AEPSegment();
		mockAEPSegment2.setSegmentRef(Integer.valueOf("3"));
		mockAirProduct.getSegments().add(mockAEPSegment1);
		mockAirProduct.getSegments().add(mockAEPSegment2);
		
		
		when(aepServiceCacheHelper.getBookingProducts(anyString(), anyString())).thenReturn(mockResponse);
		when(aemService.getCountryCodeByPortCode("HKG")).thenReturn("HK");
		when(aemService.getCountryCodeByPortCode("LOS")).thenReturn("US");
		
		AEPProductsResponse response = aepServiceImpl.getBookingProductsForBaggage(booking);
		assertEquals("3", response.getProducts().get(0).getProductId());
		assertEquals(Integer.valueOf(1), response.getProducts().get(0).getAirProduct().get(0).getPassengers().get(0).getPassengerRef());
		assertEquals(Integer.valueOf(1), response.getProducts().get(0).getAirProduct().get(0).getSegments().get(0).getSegmentRef());
		
		passenger.setPassengerId("2");
		segment.setSegmentID("2");
		
		Segment segment1=new Segment();
		DepartureArrivalTime departureTime1=new DepartureArrivalTime();
		segment1.setSegmentID("3");
		segment1.setOriginPort("HKG");
		segment1.setDestPort("LOS");
		segment1.setMarketCompany("CX");
		segment1.setMarketSegmentNumber("332");
		segment1.setCabinClass("Y");
		departureTime1.setPnrTime("2018-03-25 08:00");
		segment1.setDepartureTime(departureTime1);
		segments.add(segment1);
		
		response = aepServiceImpl.getBookingProductsForBaggage(booking);
		assertEquals("2", response.getProducts().get(0).getProductId());
		assertEquals(Integer.valueOf(2), response.getProducts().get(0).getAirProduct().get(0).getPassengers().get(0).getPassengerRef());
		assertEquals(Integer.valueOf(2), response.getProducts().get(0).getAirProduct().get(0).getSegments().get(0).getSegmentRef());
		assertEquals(Integer.valueOf(3), response.getProducts().get(0).getAirProduct().get(0).getSegments().get(1).getSegmentRef());
	}
}
