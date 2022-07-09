package com.cathaypacific.mmbbizrule.business.impl;

import static org.mockito.Mockito.*;
import static com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductsResponseDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductValueDTOMocker.*;
import static com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.BookingPropertiesDTOMocker.*;
import static com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.PassengerPropertiesDTOMocker.*;
import static com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.PassengerSegmentPropertiesDTOMocker.*;
import static com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.SegmentPropertiesDTOMocker.*;
import static com.cathaypacific.mmbbizrule.model.journey.JourneySegmentMocker.*;
import static com.cathaypacific.mmbbizrule.model.journey.JourneySummaryMocker.*;
import static com.cathaypacific.mmbbizrule.dto.request.baggage.ExtraBaggageRequestDTOMocker.*;
import static com.cathaypacific.mmbbizrule.dto.response.baggage.BaggageProductDTOVerifier.*;
import static com.cathaypacific.mmbbizrule.dto.response.baggage.ExtraBaggageResponseDTOVerifier.*;
import static com.cathaypacific.mmbbizrule.dto.response.baggage.BaggageProductValueDTOVerifier.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.BookingPropertiesBusiness;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.common.ProductTypeEnum;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductDTOMocker;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductValueDTOMocker;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductsResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductsResponseDTOMocker;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.service.EcommService;
import com.cathaypacific.mmbbizrule.dto.response.baggage.BaggageProductDTOVerifier;
import com.cathaypacific.mmbbizrule.dto.response.baggage.BaggageProductValueDTOVerifier;
import com.cathaypacific.mmbbizrule.dto.response.baggage.ExtraBaggageResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.baggage.IneligibleReasonEnum;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.BookingCommercePropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.PassengerPropertiesDTOMocker;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.SegmentPropertiesDTOMocker;
import com.cathaypacific.mmbbizrule.handler.JourneyCalculateHelper;
import com.cathaypacific.mmbbizrule.model.journey.JourneySegmentMocker;
import com.cathaypacific.mmbbizrule.model.journey.JourneySummary;
import com.cathaypacific.mmbbizrule.verifier.Verifier;

@RunWith(MockitoJUnitRunner.class)
public class BaggageBusinessImplTest {

	@InjectMocks
	private BaggageBusinessImpl baggageBusinessImpl;

	@Mock
	private EcommService ecommService;
	
	@Mock
	private BookingPropertiesBusiness bookingPropertiesBusiness;
	
	@Mock
	private JourneyCalculateHelper journeyCalculateHelper;
	
	@Test
	public void getBaggageProducts() throws Exception {
		
		ProductsResponseDTO ecommProductsResponse = mock(
			products(
				mock(
					ProductDTOMocker.productType(ProductTypeEnum.BAGGAGE_USA), sellOnOffline(false),
					ProductDTOMocker.segmentIds("1"),
					ProductDTOMocker.passengerId("1"),
					ProductDTOMocker.unit("PC"),
					productValues(
						mock(ProductValueDTOMocker.value("1"), ProductValueDTOMocker.amount("1111.00"), ProductValueDTOMocker.currency("HKD"))
					)
				)
			)
		);
		
		BookingCommercePropertiesDTO bookingCommercePropertiesDTO = mock(
			staffBooking(false), groupBooking(false),
			passengerProperties(
				mock(PassengerPropertiesDTOMocker.passengerId("1"), infant(false))
			),
			segmentProperties(
				mock(SegmentPropertiesDTOMocker.segmentId("1"), marketedByCxKa(true), operatedByCxKa(true), before24H(true))
			),
			passengerSegmentProperties(
				mock(passengerSegmentId("1", "1"),
					eticketIssued(true), cxKaET(true), haveWaiverBaggage(false)
				)
			)
		);
		
		List<JourneySummary> journeySummaries = Arrays.asList(
			mock(
				journeyId("0"),
				segments(
					mock(JourneySegmentMocker.segmentId("1"))
				)
			)
		);
		
		@SuppressWarnings("unchecked")
		Future<List<JourneySummary>> futureJourneySummaries = mock(Future.class);
		when(futureJourneySummaries.get()).thenReturn(journeySummaries);
		
		when(ecommService.getEcommBaggageProducts(anyString(), anyString(), anyString())).thenReturn(ecommProductsResponse);
		when(bookingPropertiesBusiness.getBookingCommerceProperties(any(), anyString())).thenReturn(bookingCommercePropertiesDTO);
		when(journeyCalculateHelper.asyncCalculateJourneyFromDpEligibility(anyString())).thenReturn(futureJourneySummaries);
		
		ExtraBaggageResponseDTO responseDTO = baggageBusinessImpl.getExtraBaggage(mock(rloc("AAA111")), new LoginInfo());
		
		Verifier<ExtraBaggageResponseDTO> responseDTOVerifier = expect(
			products(
				expect(
					BaggageProductDTOVerifier.ineligibleReason(null),
					BaggageProductDTOVerifier.productType(ProductTypeEnum.BAGGAGE_USA),
					BaggageProductDTOVerifier.segmentIds("1"),
					BaggageProductDTOVerifier.passengerId("1"),
					BaggageProductDTOVerifier.unit("PC"),
					BaggageProductDTOVerifier.productValues(
						expect(
							BaggageProductValueDTOVerifier.value("1"),
							BaggageProductValueDTOVerifier.amount("1111.00"),
							BaggageProductValueDTOVerifier.currency("HKD"))
					)
				)
			)
		);
		responseDTOVerifier.verify(responseDTO);
	}
	
	@Test
	public void getBaggageProductsSellOnOffline() throws Exception {
		
		ProductsResponseDTO ecommProductsResponse = mock(
			products(
				mock(
					ProductDTOMocker.productType(ProductTypeEnum.BAGGAGE_USA), sellOnOffline(true),
					ProductDTOMocker.segmentIds("1"),
					ProductDTOMocker.passengerId("1"),
					ProductDTOMocker.unit("PC"),
					productValues(
						mock(ProductValueDTOMocker.value("1"), ProductValueDTOMocker.amount("1111.00"), ProductValueDTOMocker.currency("HKD"))
					)
				)
			)
		);
		
		BookingCommercePropertiesDTO bookingCommercePropertiesDTO = mock(
			staffBooking(false), groupBooking(false),
			passengerProperties(
				mock(PassengerPropertiesDTOMocker.passengerId("1"), infant(false))
			),
			segmentProperties(
				mock(SegmentPropertiesDTOMocker.segmentId("1"), marketedByCxKa(true), operatedByCxKa(true), before24H(true))
			),
			passengerSegmentProperties(
				mock(passengerSegmentId("1", "1"),
					eticketIssued(true), cxKaET(true), haveWaiverBaggage(false)
				)
			)
		);
		
		List<JourneySummary> journeySummaries = Arrays.asList(
			mock(
				journeyId("0"),
				segments(
					mock(JourneySegmentMocker.segmentId("1"))
				)
			)
		);
		
		@SuppressWarnings("unchecked")
		Future<List<JourneySummary>> futureJourneySummaries = mock(Future.class);
		when(futureJourneySummaries.get()).thenReturn(journeySummaries);
		
		when(ecommService.getEcommBaggageProducts(anyString(), anyString(), anyString())).thenReturn(ecommProductsResponse);
		when(bookingPropertiesBusiness.getBookingCommerceProperties(any(), anyString())).thenReturn(bookingCommercePropertiesDTO);
		when(journeyCalculateHelper.asyncCalculateJourneyFromDpEligibility(anyString())).thenReturn(futureJourneySummaries);
		
		ExtraBaggageResponseDTO responseDTO = baggageBusinessImpl.getExtraBaggage(mock(rloc("AAA111")), new LoginInfo());
		
		Verifier<ExtraBaggageResponseDTO> responseDTOVerifier = expect(
			products(
				expect(
					BaggageProductDTOVerifier.ineligibleReason(IneligibleReasonEnum.SELL_OFFLINE)
				)
			)
		);
		responseDTOVerifier.verify(responseDTO);
	}
	
	@Test
	public void getBaggageProductsMaxReached() throws Exception {
		
		ProductsResponseDTO ecommProductsResponse = mock(
			products(
				mock(
					ProductDTOMocker.productType(ProductTypeEnum.BAGGAGE_USA), sellOnOffline(false),
					ProductDTOMocker.segmentIds("1"),
					ProductDTOMocker.passengerId("1"),
					ProductDTOMocker.unit("PC"),
					ProductDTOMocker.productValues()
				)
			)
		);
		
		BookingCommercePropertiesDTO bookingCommercePropertiesDTO = mock(
			staffBooking(false), groupBooking(false),
			passengerProperties(
				mock(PassengerPropertiesDTOMocker.passengerId("1"), infant(false))
			),
			segmentProperties(
				mock(SegmentPropertiesDTOMocker.segmentId("1"), marketedByCxKa(true), operatedByCxKa(true), before24H(true))
			),
			passengerSegmentProperties(
				mock(passengerSegmentId("1", "1"),
					eticketIssued(true), cxKaET(true), haveWaiverBaggage(false)
				)
			)
		);
		
		List<JourneySummary> journeySummaries = Arrays.asList(
			mock(
				journeyId("0"),
				segments(
					mock(JourneySegmentMocker.segmentId("1"))
				)
			)
		);
		
		@SuppressWarnings("unchecked")
		Future<List<JourneySummary>> futureJourneySummaries = mock(Future.class);
		when(futureJourneySummaries.get()).thenReturn(journeySummaries);
		
		when(ecommService.getEcommBaggageProducts(anyString(), anyString(), anyString())).thenReturn(ecommProductsResponse);
		when(bookingPropertiesBusiness.getBookingCommerceProperties(any(), anyString())).thenReturn(bookingCommercePropertiesDTO);
		when(journeyCalculateHelper.asyncCalculateJourneyFromDpEligibility(anyString())).thenReturn(futureJourneySummaries);
		
		ExtraBaggageResponseDTO responseDTO = baggageBusinessImpl.getExtraBaggage(mock(rloc("AAA111")), new LoginInfo());
		
		Verifier<ExtraBaggageResponseDTO> responseDTOVerifier = expect(
			products(
				expect(
					BaggageProductDTOVerifier.ineligibleReason(IneligibleReasonEnum.MAX_REACHED),
					BaggageProductDTOVerifier.productType(ProductTypeEnum.BAGGAGE_USA),
					BaggageProductDTOVerifier.segmentIds("1"),
					BaggageProductDTOVerifier.passengerId("1"),
					BaggageProductDTOVerifier.unit("PC")
				)
			)
		);
		responseDTOVerifier.verify(responseDTO);
	}
	
	@Test
	public void getBaggageProductsCloseToDeparture() throws Exception {
		
		ProductsResponseDTO ecommProductsResponse = mock(
			products(
				mock(
					ProductDTOMocker.productType(ProductTypeEnum.BAGGAGE_USA), sellOnOffline(false),
					ProductDTOMocker.segmentIds("3"),
					ProductDTOMocker.passengerId("1"),
					ProductDTOMocker.unit("PC"),
					productValues(
						mock(ProductValueDTOMocker.value("1"), ProductValueDTOMocker.amount("1111.00"), ProductValueDTOMocker.currency("HKD"))
					)
				)
			)
		);
		
		BookingCommercePropertiesDTO bookingCommercePropertiesDTO = mock(
			staffBooking(false), groupBooking(false),
			passengerProperties(
				mock(PassengerPropertiesDTOMocker.passengerId("1"), infant(false))
			),
			segmentProperties(
				mock(SegmentPropertiesDTOMocker.segmentId("1"), marketedByCxKa(true), operatedByCxKa(true), before24H(false)),
				mock(SegmentPropertiesDTOMocker.segmentId("2"), marketedByCxKa(true), operatedByCxKa(true), before24H(true)),
				mock(SegmentPropertiesDTOMocker.segmentId("3"), marketedByCxKa(true), operatedByCxKa(true), before24H(true))
			),
			passengerSegmentProperties(
				mock(passengerSegmentId("1", "1"),
					eticketIssued(true), cxKaET(true), haveWaiverBaggage(false)
				),
				mock(passengerSegmentId("1", "2"),
					eticketIssued(true), cxKaET(true), haveWaiverBaggage(false)
				),
				mock(passengerSegmentId("1", "3"),
					eticketIssued(true), cxKaET(true), haveWaiverBaggage(false)
				)
			)
		);
		
		List<JourneySummary> journeySummaries = Arrays.asList(
			mock(
				journeyId("0"),
				segments(
					mock(JourneySegmentMocker.segmentId("1")),
					mock(JourneySegmentMocker.segmentId("2"))
				)
			),
			mock(
				journeyId("1"),
				segments(
					mock(JourneySegmentMocker.segmentId("3"))
				)
			)
		);
		
		@SuppressWarnings("unchecked")
		Future<List<JourneySummary>> futureJourneySummaries = mock(Future.class);
		when(futureJourneySummaries.get()).thenReturn(journeySummaries);
		
		when(ecommService.getEcommBaggageProducts(anyString(), anyString(), anyString())).thenReturn(ecommProductsResponse);
		when(bookingPropertiesBusiness.getBookingCommerceProperties(any(), anyString())).thenReturn(bookingCommercePropertiesDTO);
		when(journeyCalculateHelper.asyncCalculateJourneyFromDpEligibility(anyString())).thenReturn(futureJourneySummaries);
		
		ExtraBaggageResponseDTO responseDTO = baggageBusinessImpl.getExtraBaggage(mock(rloc("AAA111")), new LoginInfo());
		
		Verifier<ExtraBaggageResponseDTO> responseDTOVerifier = expect(
			products(
				expect(
					BaggageProductDTOVerifier.ineligibleReason(null),
					BaggageProductDTOVerifier.productType(ProductTypeEnum.BAGGAGE_USA),
					BaggageProductDTOVerifier.segmentIds("3"), BaggageProductDTOVerifier.passengerId("1"),
					BaggageProductDTOVerifier.unit("PC"),
					productValues(
						expect(
							BaggageProductValueDTOVerifier.value("1"),
							BaggageProductValueDTOVerifier.amount("1111.00"),
							BaggageProductValueDTOVerifier.currency("HKD"))
					)
				),
				expect(
					BaggageProductDTOVerifier.ineligibleReason(IneligibleReasonEnum.CLOSE_TO_DEPARTURE),
					BaggageProductDTOVerifier.productType(null),
					BaggageProductDTOVerifier.segmentIds("1", "2"),
					BaggageProductDTOVerifier.passengerId("1"),
					BaggageProductDTOVerifier.unit(null)
				)
			)
		);
		responseDTOVerifier.verify(responseDTO);
	}
	
	@Test
	public void getBaggageProductsStaffBooking() throws Exception {
		
		ProductsResponseDTO ecommProductsResponse = mock(
			ProductsResponseDTOMocker.products()	
		);
		
		BookingCommercePropertiesDTO bookingCommercePropertiesDTO = mock(
			staffBooking(true), groupBooking(false),
			passengerProperties(
				mock(PassengerPropertiesDTOMocker.passengerId("1"), infant(false))
			),
			segmentProperties(
				mock(SegmentPropertiesDTOMocker.segmentId("1"), marketedByCxKa(true), operatedByCxKa(true), before24H(true))
			),
			passengerSegmentProperties(
				mock(passengerSegmentId("1", "1"),
					eticketIssued(true), cxKaET(true), haveWaiverBaggage(false)
				)
			)
		);
		
		List<JourneySummary> journeySummaries = Arrays.asList(
			mock(
				journeyId("0"),
				segments(
					mock(JourneySegmentMocker.segmentId("1"))
				)
			)
		);
		
		@SuppressWarnings("unchecked")
		Future<List<JourneySummary>> futureJourneySummaries = mock(Future.class);
		when(futureJourneySummaries.get()).thenReturn(journeySummaries);
		
		when(ecommService.getEcommBaggageProducts(anyString(), anyString(), anyString())).thenReturn(ecommProductsResponse);
		when(bookingPropertiesBusiness.getBookingCommerceProperties(any(), anyString())).thenReturn(bookingCommercePropertiesDTO);
		when(journeyCalculateHelper.asyncCalculateJourneyFromDpEligibility(anyString())).thenReturn(futureJourneySummaries);
		
		ExtraBaggageResponseDTO responseDTO = baggageBusinessImpl.getExtraBaggage(mock(rloc("AAA111")), new LoginInfo());
		
		Verifier<ExtraBaggageResponseDTO> responseDTOVerifier = expect(
			products(
				expect(
					BaggageProductDTOVerifier.ineligibleReason(IneligibleReasonEnum.STAFF_BOOKING),
					BaggageProductDTOVerifier.productType(null),
					BaggageProductDTOVerifier.segmentIds("1"),
					BaggageProductDTOVerifier.passengerId("1"),
					BaggageProductDTOVerifier.unit(null)
				)
			)
		);
		responseDTOVerifier.verify(responseDTO);
	}
	
	@Test
	public void getBaggageProductsGroupBooking() throws Exception {
		
		ProductsResponseDTO ecommProductsResponse = mock(
			ProductsResponseDTOMocker.products()	
		);
		
		BookingCommercePropertiesDTO bookingCommercePropertiesDTO = mock(
			staffBooking(false), groupBooking(true),
			passengerProperties(
				mock(PassengerPropertiesDTOMocker.passengerId("1"), infant(false))
			),
			segmentProperties(
				mock(SegmentPropertiesDTOMocker.segmentId("1"), marketedByCxKa(true), operatedByCxKa(true), before24H(true))
			),
			passengerSegmentProperties(
				mock(passengerSegmentId("1", "1"),
					eticketIssued(true), cxKaET(true), haveWaiverBaggage(false)
				)
			)
		);
		
		List<JourneySummary> journeySummaries = Arrays.asList(
			mock(
				journeyId("0"),
				segments(
					mock(JourneySegmentMocker.segmentId("1"))
				)
			)
		);
		
		@SuppressWarnings("unchecked")
		Future<List<JourneySummary>> futureJourneySummaries = mock(Future.class);
		when(futureJourneySummaries.get()).thenReturn(journeySummaries);
		
		when(ecommService.getEcommBaggageProducts(anyString(), anyString(), anyString())).thenReturn(ecommProductsResponse);
		when(bookingPropertiesBusiness.getBookingCommerceProperties(any(), anyString())).thenReturn(bookingCommercePropertiesDTO);
		when(journeyCalculateHelper.asyncCalculateJourneyFromDpEligibility(anyString())).thenReturn(futureJourneySummaries);
		
		ExtraBaggageResponseDTO responseDTO = baggageBusinessImpl.getExtraBaggage(mock(rloc("AAA111")), new LoginInfo());
		
		Verifier<ExtraBaggageResponseDTO> responseDTOVerifier = expect(
			products(
				expect(
					BaggageProductDTOVerifier.ineligibleReason(IneligibleReasonEnum.GROUP_BOOKING),
					BaggageProductDTOVerifier.productType(null),
					BaggageProductDTOVerifier.segmentIds("1"),
					BaggageProductDTOVerifier.passengerId("1"),
					BaggageProductDTOVerifier.unit(null)
				)
			)
		);
		responseDTOVerifier.verify(responseDTO);
	}
	
	@Test
	public void getBaggageProductsNonCxKaOpFlight() throws Exception {

		ProductsResponseDTO ecommProductsResponse = mock(
			ProductsResponseDTOMocker.products()	
		);
		
		BookingCommercePropertiesDTO bookingCommercePropertiesDTO = mock(
			staffBooking(false), groupBooking(false),
			passengerProperties(
				mock(PassengerPropertiesDTOMocker.passengerId("1"), infant(false))
			),
			segmentProperties(
				mock(SegmentPropertiesDTOMocker.segmentId("1"), marketedByCxKa(true), operatedByCxKa(false), before24H(true))
			),
			passengerSegmentProperties(
				mock(passengerSegmentId("1", "1"),
					eticketIssued(true), cxKaET(true), haveWaiverBaggage(false)
				)
			)
		);
		
		List<JourneySummary> journeySummaries = Arrays.asList(
			mock(
				journeyId("0"),
				segments(
					mock(JourneySegmentMocker.segmentId("1"))
				)
			)
		);
		
		@SuppressWarnings("unchecked")
		Future<List<JourneySummary>> futureJourneySummaries = mock(Future.class);
		when(futureJourneySummaries.get()).thenReturn(journeySummaries);
		
		when(ecommService.getEcommBaggageProducts(anyString(), anyString(), anyString())).thenReturn(ecommProductsResponse);
		when(bookingPropertiesBusiness.getBookingCommerceProperties(any(), anyString())).thenReturn(bookingCommercePropertiesDTO);
		when(journeyCalculateHelper.asyncCalculateJourneyFromDpEligibility(anyString())).thenReturn(futureJourneySummaries);
		
		ExtraBaggageResponseDTO responseDTO = baggageBusinessImpl.getExtraBaggage(mock(rloc("AAA111")), new LoginInfo());
		
		Verifier<ExtraBaggageResponseDTO> responseDTOVerifier = expect(
			products(
				expect(
					BaggageProductDTOVerifier.ineligibleReason(IneligibleReasonEnum.NON_CX_KA_OP_FLIGHT),
					BaggageProductDTOVerifier.productType(null),
					BaggageProductDTOVerifier.segmentIds("1"),
					BaggageProductDTOVerifier.passengerId("1"),
					BaggageProductDTOVerifier.unit(null)
				)
			)
		);
		responseDTOVerifier.verify(responseDTO);
	}
	
	@Test
	public void getBaggageProductsWaiverBaggage() throws Exception {

		ProductsResponseDTO ecommProductsResponse = mock(
			ProductsResponseDTOMocker.products()	
		);
		
		BookingCommercePropertiesDTO bookingCommercePropertiesDTO = mock(
			staffBooking(false), groupBooking(false),
			passengerProperties(
				mock(PassengerPropertiesDTOMocker.passengerId("1"), infant(false))
			),
			segmentProperties(
				mock(SegmentPropertiesDTOMocker.segmentId("1"), marketedByCxKa(true), operatedByCxKa(true), before24H(true))
			),
			passengerSegmentProperties(
				mock(passengerSegmentId("1", "1"),
					eticketIssued(true), cxKaET(true), haveWaiverBaggage(true)
				)
			)
		);
		
		List<JourneySummary> journeySummaries = Arrays.asList(
			mock(
				journeyId("0"),
				segments(
					mock(JourneySegmentMocker.segmentId("1"))
				)
			)
		);
		
		@SuppressWarnings("unchecked")
		Future<List<JourneySummary>> futureJourneySummaries = mock(Future.class);
		when(futureJourneySummaries.get()).thenReturn(journeySummaries);
		
		when(ecommService.getEcommBaggageProducts(anyString(), anyString(), anyString())).thenReturn(ecommProductsResponse);
		when(bookingPropertiesBusiness.getBookingCommerceProperties(any(), anyString())).thenReturn(bookingCommercePropertiesDTO);
		when(journeyCalculateHelper.asyncCalculateJourneyFromDpEligibility(anyString())).thenReturn(futureJourneySummaries);
		
		ExtraBaggageResponseDTO responseDTO = baggageBusinessImpl.getExtraBaggage(mock(rloc("AAA111")), new LoginInfo());
		
		Verifier<ExtraBaggageResponseDTO> responseDTOVerifier = expect(
			products(
				expect(
					BaggageProductDTOVerifier.ineligibleReason(IneligibleReasonEnum.WAVIER_BAGGAGE),
					BaggageProductDTOVerifier.productType(null),
					BaggageProductDTOVerifier.segmentIds("1"),
					BaggageProductDTOVerifier.passengerId("1"),
					BaggageProductDTOVerifier.unit(null)
				)
			)
		);
		responseDTOVerifier.verify(responseDTO);
	}
	
	@Test
	public void getBaggageProductsWaitlistBaggage() throws Exception {

		ProductsResponseDTO ecommProductsResponse = mock(
			ProductsResponseDTOMocker.products()	
		);
		
		BookingCommercePropertiesDTO bookingCommercePropertiesDTO = mock(
			staffBooking(false), groupBooking(false),
			passengerProperties(
				mock(PassengerPropertiesDTOMocker.passengerId("1"), infant(false))
			),
			segmentProperties(
				mock(SegmentPropertiesDTOMocker.segmentId("1"), marketedByCxKa(true), operatedByCxKa(true), before24H(true), waitlisted(true))
			),
			passengerSegmentProperties(
				mock(passengerSegmentId("1", "1"),
					eticketIssued(true), cxKaET(true)
				)
			)
		);
		
		List<JourneySummary> journeySummaries = Arrays.asList(
			mock(
				journeyId("0"),
				segments(
					mock(JourneySegmentMocker.segmentId("1"))
				)
			)
		);
		
		@SuppressWarnings("unchecked")
		Future<List<JourneySummary>> futureJourneySummaries = mock(Future.class);
		when(futureJourneySummaries.get()).thenReturn(journeySummaries);
		
		when(ecommService.getEcommBaggageProducts(anyString(), anyString(), anyString())).thenReturn(ecommProductsResponse);
		when(bookingPropertiesBusiness.getBookingCommerceProperties(any(), anyString())).thenReturn(bookingCommercePropertiesDTO);
		when(journeyCalculateHelper.asyncCalculateJourneyFromDpEligibility(anyString())).thenReturn(futureJourneySummaries);
		
		ExtraBaggageResponseDTO responseDTO = baggageBusinessImpl.getExtraBaggage(mock(rloc("AAA111")), new LoginInfo());
		
		Verifier<ExtraBaggageResponseDTO> responseDTOVerifier = expect(
			products(
				expect(
					BaggageProductDTOVerifier.ineligibleReason(IneligibleReasonEnum.WAITLIST),
					BaggageProductDTOVerifier.productType(null),
					BaggageProductDTOVerifier.segmentIds("1"),
					BaggageProductDTOVerifier.passengerId("1"),
					BaggageProductDTOVerifier.unit(null)
				)
			)
		);
		responseDTOVerifier.verify(responseDTO);
	}
	
	@Test
	public void getBaggageProductsTicketNotIssued() throws Exception {

		ProductsResponseDTO ecommProductsResponse = mock(
			ProductsResponseDTOMocker.products()	
		);
		
		BookingCommercePropertiesDTO bookingCommercePropertiesDTO = mock(
			staffBooking(false), groupBooking(false),
			passengerProperties(
				mock(PassengerPropertiesDTOMocker.passengerId("1"), infant(false))
			),
			segmentProperties(
				mock(SegmentPropertiesDTOMocker.segmentId("1"), marketedByCxKa(true), operatedByCxKa(true), before24H(true))
			),
			passengerSegmentProperties(
				mock(passengerSegmentId("1", "1"),
					eticketIssued(false), cxKaET(false), haveWaiverBaggage(false)
				)
			)
		);
		
		List<JourneySummary> journeySummaries = Arrays.asList(
			mock(
				journeyId("0"),
				segments(
					mock(JourneySegmentMocker.segmentId("1"))
				)
			)
		);
		
		@SuppressWarnings("unchecked")
		Future<List<JourneySummary>> futureJourneySummaries = mock(Future.class);
		when(futureJourneySummaries.get()).thenReturn(journeySummaries);
		
		when(ecommService.getEcommBaggageProducts(anyString(), anyString(), anyString())).thenReturn(ecommProductsResponse);
		when(bookingPropertiesBusiness.getBookingCommerceProperties(any(), anyString())).thenReturn(bookingCommercePropertiesDTO);
		when(journeyCalculateHelper.asyncCalculateJourneyFromDpEligibility(anyString())).thenReturn(futureJourneySummaries);
		
		ExtraBaggageResponseDTO responseDTO = baggageBusinessImpl.getExtraBaggage(mock(rloc("AAA111")), new LoginInfo());
		
		Verifier<ExtraBaggageResponseDTO> responseDTOVerifier = expect(
			products(
				expect(
					BaggageProductDTOVerifier.ineligibleReason(IneligibleReasonEnum.TICKET_NOT_ISSUED),
					BaggageProductDTOVerifier.productType(null),
					BaggageProductDTOVerifier.segmentIds("1"),
					BaggageProductDTOVerifier.passengerId("1"),
					BaggageProductDTOVerifier.unit(null)
				)
			)
		);
		responseDTOVerifier.verify(responseDTO);
	}
	
	@Test
	public void getBaggageProductsNonCxKaEticket() throws Exception {

		ProductsResponseDTO ecommProductsResponse = mock(
			ProductsResponseDTOMocker.products()	
		);
		
		BookingCommercePropertiesDTO bookingCommercePropertiesDTO = mock(
			staffBooking(false), groupBooking(false),
			passengerProperties(
				mock(PassengerPropertiesDTOMocker.passengerId("1"), infant(false))
			),
			segmentProperties(
				mock(SegmentPropertiesDTOMocker.segmentId("1"), marketedByCxKa(true), operatedByCxKa(true), before24H(true))
			),
			passengerSegmentProperties(
				mock(passengerSegmentId("1", "1"),
					eticketIssued(true), cxKaET(false), haveWaiverBaggage(false)
				)
			)
		);
		
		List<JourneySummary> journeySummaries = Arrays.asList(
			mock(
				journeyId("0"),
				segments(
					mock(JourneySegmentMocker.segmentId("1"))
				)
			)
		);
		
		@SuppressWarnings("unchecked")
		Future<List<JourneySummary>> futureJourneySummaries = mock(Future.class);
		when(futureJourneySummaries.get()).thenReturn(journeySummaries);
		
		when(ecommService.getEcommBaggageProducts(anyString(), anyString(), anyString())).thenReturn(ecommProductsResponse);
		when(bookingPropertiesBusiness.getBookingCommerceProperties(any(), anyString())).thenReturn(bookingCommercePropertiesDTO);
		when(journeyCalculateHelper.asyncCalculateJourneyFromDpEligibility(anyString())).thenReturn(futureJourneySummaries);
		
		ExtraBaggageResponseDTO responseDTO = baggageBusinessImpl.getExtraBaggage(mock(rloc("AAA111")), new LoginInfo());
		
		Verifier<ExtraBaggageResponseDTO> responseDTOVerifier = expect(
			products(
				expect(
					BaggageProductDTOVerifier.ineligibleReason(IneligibleReasonEnum.NON_CX_KA_ETICKET),
					BaggageProductDTOVerifier.productType(null),
					BaggageProductDTOVerifier.segmentIds("1"),
					BaggageProductDTOVerifier.passengerId("1"),
					BaggageProductDTOVerifier.unit(null)
				)
			)
		);
		responseDTOVerifier.verify(responseDTO);
	}
	
}
