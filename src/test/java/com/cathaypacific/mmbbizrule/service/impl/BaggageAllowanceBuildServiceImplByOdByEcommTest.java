package com.cathaypacific.mmbbizrule.service.impl;

import static org.mockito.Mockito.*;

import java.math.BigInteger;

import static com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductsResponseDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.BaggageAllowanceDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CabinBaggageDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CabinBaggagePieceDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CabinBaggageWeightDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CabinDimensionsDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CabinSmallItemDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CheckinBaggageDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CheckinBaggagePieceDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CheckinBaggageWeightDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.DimensionDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.od.BgAlOdBookingResponseDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.od.BgAlOdJourneyDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.od.BgAlOdResponseDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.od.BgAlOdSegmentDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.verifier.od.BgAlOdBookingRequestDTOVerifier.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.verifier.od.BgAlOdRequestDTOVerifier.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.verifier.od.BgAlOdSegmentDTOVerifier.*;
import static com.cathaypacific.mmbbizrule.model.detail.mocker.AirportUpgradeInfoMocker.*;
import static com.cathaypacific.mmbbizrule.model.detail.mocker.BaggageAllowanceMocker.*;
import static com.cathaypacific.mmbbizrule.model.detail.mocker.BaggageDetailMocker.*;
import static com.cathaypacific.mmbbizrule.model.detail.mocker.BookingMocker.*;
import static com.cathaypacific.mmbbizrule.model.detail.mocker.CheckInBaggageMocker.*;
import static com.cathaypacific.mmbbizrule.model.detail.mocker.DepartureArrivalTimeMocker.*;
import static com.cathaypacific.mmbbizrule.model.detail.mocker.FQTVInfoMocker.*;
import static com.cathaypacific.mmbbizrule.model.detail.mocker.PassengerMocker.*;
import static com.cathaypacific.mmbbizrule.model.detail.mocker.PassengerSegmentMocker.*;
import static com.cathaypacific.mmbbizrule.model.detail.mocker.SegmentMocker.*;
import static com.cathaypacific.mmbbizrule.model.detail.verifier.BaggageAllowanceVerifier.*;
import static com.cathaypacific.mmbbizrule.model.detail.verifier.BaggageDetailVerifier.*;
import static com.cathaypacific.mmbbizrule.model.detail.verifier.BookingVerifier.*;
import static com.cathaypacific.mmbbizrule.model.detail.verifier.CabinBaggageVerifier.*;
import static com.cathaypacific.mmbbizrule.model.detail.verifier.CheckInBaggageVerifier.*;
import static com.cathaypacific.mmbbizrule.model.detail.verifier.PassengerSegmentVerifier.*;
import static com.cathaypacific.mmbbizrule.model.detail.verifier.PassengerVerifier.*;
import static com.cathaypacific.mmbbizrule.model.detail.verifier.SegmentVerifier.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.enums.baggage.BaggageUnitEnum;
import com.cathaypacific.mmbbizrule.cxservice.aep.AEPProductIdEnum;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CabinBaggagePieceDTOMocker;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CabinBaggageWeightDTOMocker;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CabinSmallItemDTOMocker;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CheckinBaggagePieceDTOMocker;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CheckinBaggageWeightDTOMocker;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.od.BgAlOdBookingResponseDTOMocker;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.od.BgAlOdSegmentDTOMocker;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.btu.BgAlBtuRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.od.BgAlOdRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.od.BgAlOdResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.service.impl.BaggageAllowanceServiceImpl;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.verifier.od.BgAlOdBookingRequestDTOVerifier;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.verifier.od.BgAlOdSegmentDTOVerifier;
import com.cathaypacific.mmbbizrule.cxservice.ecomm.model.product.ProductsResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.detail.mocker.CheckInBaggageMocker;
import com.cathaypacific.mmbbizrule.model.detail.mocker.PassengerMocker;
import com.cathaypacific.mmbbizrule.model.detail.mocker.PassengerSegmentMocker;
import com.cathaypacific.mmbbizrule.model.detail.mocker.SegmentMocker;
import com.cathaypacific.mmbbizrule.model.detail.verifier.CabinBaggageVerifier;
import com.cathaypacific.mmbbizrule.model.detail.verifier.CheckInBaggageVerifier;
import com.cathaypacific.mmbbizrule.model.detail.verifier.PassengerSegmentVerifier;
import com.cathaypacific.mmbbizrule.model.detail.verifier.PassengerVerifier;
import com.cathaypacific.mmbbizrule.model.detail.verifier.SegmentVerifier;
import com.cathaypacific.mmbbizrule.service.BaggageAllowanceBuildService.BaggageAllowanceInfo;
import com.cathaypacific.mmbbizrule.verifier.Verifier;

@RunWith(MockitoJUnitRunner.class)
public class BaggageAllowanceBuildServiceImplByOdByEcommTest {

	@InjectMocks
	private BaggageAllowanceBuildServiceImpl baggageAllowanceBuildService;
	
	@Mock
	private BaggageAllowanceServiceImpl baggageAllowanceService;
	
	/**
	 * Retrieve baggage allowance by OD when baggage product is not returned from AEP.
	 * Baggage allowance by BTU API should not be invoked.
	 * 
	 * @throws Exception
	 */
	@Test
	public void retrieveBaggageAllowanceInfo() throws Exception {
		
		Booking booking = mock(oneARloc("AAA000"), 
			passengers(
				mock(PassengerMocker.passengerID("1"))
			),
			segments(
				mock(SegmentMocker.segmentID("1"), port("OG1", "DT1"),
					marketing("MK1", "100"), operating("OP1", "4100"), SegmentMocker.cabinClass("Y"),
					departure(mock(timeZoneOffset("+0800"), pnrTime("2000-01-20 08:15"))),
					arrival(mock(timeZoneOffset("+1000"), pnrTime("2000-01-20 20:30")))
				)
			),
			passengerSegments(
				mock(PassengerSegmentMocker.passengerSegmentID("1", "1"))
			)
		);
		
		// Baggage product is not returned from Ecomm service.
		ProductsResponseDTO ecommProductsResponse = mock(
			products()
		);
		
		// Detail data of baggage allowance are not used in this test case.
		BgAlOdResponseDTO responseDTO = mock(
			bookings(
				BgAlOdBookingResponseDTOMocker.mock()
			)
		);
		
		when(baggageAllowanceService.getBaggageAllowanceByOd(isA(BgAlOdRequestDTO.class))).thenReturn(responseDTO);
		
		baggageAllowanceBuildService.retrieveBaggageAllowanceInfo(booking, ecommProductsResponse);
		
		// Baggage allowance by BTU is NOT invoked.
		verify(baggageAllowanceService, never()).getBaggageAllowanceByBtu(any(BgAlBtuRequestDTO.class));
		
		/*
		 *  Verify request of baggage allowance by OD.
		 */
		Verifier<BgAlOdRequestDTO> requestVerifier = expect(bookings(
				expect(BgAlOdBookingRequestDTOVerifier.bookingId("AAA000"),
					segments(
						expect(BgAlOdSegmentDTOVerifier.point("OG1", "DT1"),
							BgAlOdSegmentDTOVerifier.marketingCarrier("MK1"), BgAlOdSegmentDTOVerifier.operatingCarrier("OP1"),
							BgAlOdSegmentDTOVerifier.cabinClass(BaggageAllowanceBuildServiceImpl.BAGGAGE_CABIN_CLASS_ECON_CLASS),
							BgAlOdSegmentDTOVerifier.departure("200100", "0015"), BgAlOdSegmentDTOVerifier.arrival("200100", "1030")
						)
					)
				)
			)
		);
		
		ArgumentCaptor<BgAlOdRequestDTO> requestCaptor = ArgumentCaptor.forClass(BgAlOdRequestDTO.class);
		verify(baggageAllowanceService, times(1)).getBaggageAllowanceByOd(requestCaptor.capture());
		BgAlOdRequestDTO requestDTO = requestCaptor.getValue();
		requestVerifier.verify(requestDTO);		
	}
	
	/**
	 * Retrieve baggage allowance by OD when baggage product is not returned from AEP.
	 * Baggage allowance by BTU API should not be invoked.
	 * 
	 * Use airport upgrade cabin class instead of original segment cabin class in baggage allowance OD request.
	 * Every passenger is considered as separated booking in baggage allowance OD request.
	 * 
	 * @throws Exception
	 */
	@Test
	public void retrieveBaggageAllowanceInfoWithAirportUpgrade() throws Exception {
		
		Booking booking = mock(oneARloc("AAA000"), 
			passengers(
				mock(PassengerMocker.passengerID("1")),
				mock(PassengerMocker.passengerID("2"))
			),
			segments(
				mock(SegmentMocker.segmentID("1"), port("OG1", "DT1"),
					marketing("MK1", "100"), operating("OP1", "4100"), SegmentMocker.cabinClass("Y"),
					departure(mock(timeZoneOffset("+0800"), pnrTime("2000-01-20 08:15"))),
					arrival(mock(timeZoneOffset("+1000"), pnrTime("2000-01-20 20:30")))
				)
			),
			passengerSegments(
				mock(PassengerSegmentMocker.passengerSegmentID("1", "1")),
				mock(PassengerSegmentMocker.passengerSegmentID("2", "1"),
					airportUpgradeInfo(mock(newCabinClass("J")))
				)
			)
		);
		
		// Baggage product is not returned from Ecomm service.
		ProductsResponseDTO ecommProductsResponse = mock(
			products()
		);
		
		// Detail data of baggage allowance are not used in this test case.
		BgAlOdResponseDTO responseDTO = mock(
			bookings(
				BgAlOdBookingResponseDTOMocker.mock()
			)
		);
		
		when(baggageAllowanceService.getBaggageAllowanceByOd(isA(BgAlOdRequestDTO.class))).thenReturn(responseDTO);
		
		baggageAllowanceBuildService.retrieveBaggageAllowanceInfo(booking, ecommProductsResponse);
		
		// Baggage allowance by BTU is NOT invoked.
		verify(baggageAllowanceService, never()).getBaggageAllowanceByBtu(any(BgAlBtuRequestDTO.class));
		
		/*
		 *  Verify request of baggage allowance by OD.
		 */
		Verifier<BgAlOdRequestDTO> requestVerifier = expect(bookings(
				expect(BgAlOdBookingRequestDTOVerifier.bookingId("AAA000"),
					segments(
						expect(BgAlOdSegmentDTOVerifier.point("OG1", "DT1"),
							BgAlOdSegmentDTOVerifier.marketingCarrier("MK1"), BgAlOdSegmentDTOVerifier.operatingCarrier("OP1"),
							BgAlOdSegmentDTOVerifier.cabinClass(BaggageAllowanceBuildServiceImpl.BAGGAGE_CABIN_CLASS_ECON_CLASS),
							BgAlOdSegmentDTOVerifier.departure("200100", "0015"), BgAlOdSegmentDTOVerifier.arrival("200100", "1030")
						)
					)
				),
				expect(BgAlOdBookingRequestDTOVerifier.bookingId("AAA000"),
					segments(
						expect(BgAlOdSegmentDTOVerifier.point("OG1", "DT1"),
							BgAlOdSegmentDTOVerifier.marketingCarrier("MK1"), BgAlOdSegmentDTOVerifier.operatingCarrier("OP1"),
							BgAlOdSegmentDTOVerifier.cabinClass(BaggageAllowanceBuildServiceImpl.BAGGAGE_CABIN_CLASS_BUSINESS_CLASS),
							BgAlOdSegmentDTOVerifier.departure("200100", "0015"), BgAlOdSegmentDTOVerifier.arrival("200100", "1030")
						)
					)
				)
			)
		);
		
		ArgumentCaptor<BgAlOdRequestDTO> requestCaptor = ArgumentCaptor.forClass(BgAlOdRequestDTO.class);
		verify(baggageAllowanceService, times(1)).getBaggageAllowanceByOd(requestCaptor.capture());
		BgAlOdRequestDTO requestDTO = requestCaptor.getValue();
		requestVerifier.verify(requestDTO);		
	}
	
	@Test
	public void populateBaggageAllowance() throws Exception {
		
		Booking booking = mock(oneARloc("AAA000"), 
			passengers(
				mock(PassengerMocker.passengerID("1"))
			),
			segments(
				mock(SegmentMocker.segmentID("1"), port("OG1", "DT1"),
					marketing("MK1", "100"), operating("OP1", "4100"), SegmentMocker.cabinClass("Y"),
					departure(mock(timeZoneOffset("+0800"), pnrTime("2000-01-20 08:15"))),
					arrival(mock(timeZoneOffset("+1000"), pnrTime("2000-01-20 20:30")))
				)
			),
			passengerSegments(
				mock(PassengerSegmentMocker.passengerSegmentID("1", "1"),
					fqtvInfo(mock(tierLevel("GO")))
				)
			)
		);
		
		// Detail data of baggage allowance are not used in this test case.
		BgAlOdResponseDTO responseDTO = mock(
			bookings(
				mock(BgAlOdBookingResponseDTOMocker.bookingId("AAA000"),
					journeys(
						mock(journeyId("1"),
							baggageAllowanceType(AEPProductIdEnum.BAGGAGE_COMMON.getId()),
							segments(
								mock(BgAlOdSegmentDTOMocker.point("OG1", "DT1"),
									BgAlOdSegmentDTOMocker.marketingCarrier("MK1"), BgAlOdSegmentDTOMocker.operatingCarrier("OP1"),
									BgAlOdSegmentDTOMocker.cabinClass(BaggageAllowanceBuildServiceImpl.BAGGAGE_CABIN_CLASS_ECON_CLASS),
									BgAlOdSegmentDTOMocker.departure("200100", "0015"), BgAlOdSegmentDTOMocker.arrival("200100", "1030")
								)
							),
							baggageAllowance(
								mock(memberTier("GO"),
									cabinBaggage(mock(
										weight(mock(CabinBaggageWeightDTOMocker.standard(10), CabinBaggageWeightDTOMocker.member(5))),
										piece(mock(CabinBaggagePieceDTOMocker.standard(1), CabinBaggagePieceDTOMocker.member(1))),
										smallItem(mock(CabinSmallItemDTOMocker.standard(1), CabinSmallItemDTOMocker.member(0))),
										dimensions(mock(
											standard(mock(values(1, 1, 1))),
											small(mock(values(1, 1, 1)))
										))
									)),
									checkinBaggage(mock(
										weight(mock(CheckinBaggageWeightDTOMocker.standard(30), CheckinBaggageWeightDTOMocker.member(15),
											CheckinBaggageWeightDTOMocker.infant(10))),
										piece(mock(CheckinBaggagePieceDTOMocker.standard(3), CheckinBaggagePieceDTOMocker.member(2),
											CheckinBaggagePieceDTOMocker.infant(1))),
										dimension(mock(values(1, 1, 1)))
									))
								)
							)
						)
					)
				)
			)
		);
		
		BaggageAllowanceInfo baggageAllowanceInfo = new BaggageAllowanceInfo();
		baggageAllowanceInfo.setBookingDTOs(responseDTO.getBookings());
			
		baggageAllowanceBuildService.populateBaggageAllowance(booking, baggageAllowanceInfo);
		
		Verifier<Booking> bookingVerifier = expect(
			passengers(
				expect(PassengerVerifier.passengerID("1"))
			),
			segments(
				expect(SegmentVerifier.segmentID("1"))
			),
			passengerSegments(
				expect(PassengerSegmentVerifier.passengerSegmentID("1", "1"),
					baggageAllowance(expect(
						cabinBaggage(expect(
							CabinBaggageVerifier.standardBaggage(expect(value("10", BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT))),
							CabinBaggageVerifier.memberBaggage(expect(value("5", BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT))),
							CabinBaggageVerifier.limit(expect(value("15", BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT))),
							total(expect(value("2", BaggageUnitEnum.BAGGAGE_PIECE_UNIT)))
						)),
						checkInBaggage(expect(
							CheckInBaggageVerifier.memberBaggage(expect(value("15", BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT))),
							CheckInBaggageVerifier.limit(expect(value("5", BaggageUnitEnum.BAGGAGE_PIECE_UNIT)))
						))
					))
				)
			)
		);
		
		bookingVerifier.verify(booking);
	}

	
	/**
	 * When airport upgrade, standard check in baggage value is overwritten by baggage allowance API response.
	 * 
	 * @throws Exception
	 */
	@Test
	public void populateBaggageAllowanceWithAirportUpgrade() throws Exception {
		
		Booking booking = mock(oneARloc("AAA000"), 
			passengers(
				mock(PassengerMocker.passengerID("1")),
				mock(PassengerMocker.passengerID("2"))
			),
			segments(
				mock(SegmentMocker.segmentID("1"), port("OG1", "DT1"),
					marketing("MK1", "100"), operating("OP1", "4100"), SegmentMocker.cabinClass("Y"),
					departure(mock(timeZoneOffset("+0800"), pnrTime("2000-01-20 08:15"))),
					arrival(mock(timeZoneOffset("+1000"), pnrTime("2000-01-20 20:30")))
				)
			),
			passengerSegments(
				mock(PassengerSegmentMocker.passengerSegmentID("1", "1"),
					fqtvInfo(mock(tierLevel("GO"))),
					baggageAllowance(mock(
						checkInBaggage(mock(
							CheckInBaggageMocker.standardBaggage(mock(
								value(new BigInteger("20"), BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT)
							))
						))
					))
				),
				mock(PassengerSegmentMocker.passengerSegmentID("2", "1"),
					fqtvInfo(mock(tierLevel("GO"))),
					airportUpgradeInfo(mock(newCabinClass("J"))),
					baggageAllowance(mock(
						checkInBaggage(mock(
							CheckInBaggageMocker.standardBaggage(mock(
								value(new BigInteger("20"), BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT)
							))
						))
					))
				)
			)
		);
		
		// Detail data of baggage allowance are not used in this test case.
		BgAlOdResponseDTO responseDTO = mock(
			bookings(
				mock(BgAlOdBookingResponseDTOMocker.bookingId("AAA000"),
					journeys(
						mock(journeyId("1"),
							baggageAllowanceType(AEPProductIdEnum.BAGGAGE_COMMON.getId()),
							segments(
								mock(BgAlOdSegmentDTOMocker.point("OG1", "DT1"),
									BgAlOdSegmentDTOMocker.marketingCarrier("MK1"), BgAlOdSegmentDTOMocker.operatingCarrier("OP1"),
									BgAlOdSegmentDTOMocker.cabinClass(BaggageAllowanceBuildServiceImpl.BAGGAGE_CABIN_CLASS_ECON_CLASS),
									BgAlOdSegmentDTOMocker.departure("200100", "0015"), BgAlOdSegmentDTOMocker.arrival("200100", "1030")
								)
							),
							baggageAllowance(
								mock(memberTier("GO"),
									cabinBaggage(mock(
										weight(mock(CabinBaggageWeightDTOMocker.standard(10), CabinBaggageWeightDTOMocker.member(5))),
										piece(mock(CabinBaggagePieceDTOMocker.standard(1), CabinBaggagePieceDTOMocker.member(1))),
										smallItem(mock(CabinSmallItemDTOMocker.standard(1), CabinSmallItemDTOMocker.member(0))),
										dimensions(mock(
											standard(mock(values(1, 1, 1))),
											small(mock(values(1, 1, 1)))
										))
									)),
									checkinBaggage(mock(
										weight(mock(CheckinBaggageWeightDTOMocker.standard(30), CheckinBaggageWeightDTOMocker.member(15),
											CheckinBaggageWeightDTOMocker.infant(10))),
										piece(mock(CheckinBaggagePieceDTOMocker.standard(3), CheckinBaggagePieceDTOMocker.member(2),
											CheckinBaggagePieceDTOMocker.infant(1))),
										dimension(mock(values(1, 1, 1)))
									))
								)
							)
						)
					)
				),
				mock(BgAlOdBookingResponseDTOMocker.bookingId("AAA000"),
					journeys(
						mock(journeyId("1"),
							baggageAllowanceType(AEPProductIdEnum.BAGGAGE_COMMON.getId()),
							segments(
								mock(BgAlOdSegmentDTOMocker.point("OG1", "DT1"),
									BgAlOdSegmentDTOMocker.marketingCarrier("MK1"), BgAlOdSegmentDTOMocker.operatingCarrier("OP1"),
									BgAlOdSegmentDTOMocker.cabinClass(BaggageAllowanceBuildServiceImpl.BAGGAGE_CABIN_CLASS_ECON_CLASS),
									BgAlOdSegmentDTOMocker.departure("200100", "0015"), BgAlOdSegmentDTOMocker.arrival("200100", "1030")
								)
							),
							baggageAllowance(
								mock(memberTier("GO"),
									cabinBaggage(mock(
										weight(mock(CabinBaggageWeightDTOMocker.standard(20), CabinBaggageWeightDTOMocker.member(15))),
										piece(mock(CabinBaggagePieceDTOMocker.standard(2), CabinBaggagePieceDTOMocker.member(2))),
										smallItem(mock(CabinSmallItemDTOMocker.standard(1), CabinSmallItemDTOMocker.member(0))),
										dimensions(mock(
											standard(mock(values(1, 1, 1))),
											small(mock(values(1, 1, 1)))
										))
									)),
									checkinBaggage(mock(
										weight(mock(CheckinBaggageWeightDTOMocker.standard(40), CheckinBaggageWeightDTOMocker.member(25),
											CheckinBaggageWeightDTOMocker.infant(20))),
										piece(mock(CheckinBaggagePieceDTOMocker.standard(4), CheckinBaggagePieceDTOMocker.member(3),
											CheckinBaggagePieceDTOMocker.infant(1))),
										dimension(mock(values(1, 1, 1)))
									))
								)
							)
						)
					)
				)
			)
		);
		
		BaggageAllowanceInfo baggageAllowanceInfo = new BaggageAllowanceInfo();
		baggageAllowanceInfo.setBookingDTOs(responseDTO.getBookings());
			
		baggageAllowanceBuildService.populateBaggageAllowance(booking, baggageAllowanceInfo);
		
		Verifier<Booking> bookingVerifier = expect(
			passengers(
				expect(PassengerVerifier.passengerID("1")),
				expect(PassengerVerifier.passengerID("2"))
			),
			segments(
				expect(SegmentVerifier.segmentID("1"))
			),
			passengerSegments(
				expect(PassengerSegmentVerifier.passengerSegmentID("1", "1"),
					baggageAllowance(expect(
						cabinBaggage(expect(
							CabinBaggageVerifier.standardBaggage(expect(value("10", BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT))),
							CabinBaggageVerifier.memberBaggage(expect(value("5", BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT))),
							CabinBaggageVerifier.limit(expect(value("15", BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT))),
							total(expect(value("2", BaggageUnitEnum.BAGGAGE_PIECE_UNIT)))
						)),
						checkInBaggage(expect(
							CheckInBaggageVerifier.standardBaggage(expect(value("20", BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT))),
							CheckInBaggageVerifier.memberBaggage(expect(value("15", BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT))),
							CheckInBaggageVerifier.limit(expect(value("5", BaggageUnitEnum.BAGGAGE_PIECE_UNIT)))
						))
					))
				),
				expect(PassengerSegmentVerifier.passengerSegmentID("2", "1"),
					baggageAllowance(expect(
						cabinBaggage(expect(
							CabinBaggageVerifier.standardBaggage(expect(value("20", BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT))),
							CabinBaggageVerifier.memberBaggage(expect(value("15", BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT))),
							CabinBaggageVerifier.limit(expect(value("35", BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT))),
							total(expect(value("4", BaggageUnitEnum.BAGGAGE_PIECE_UNIT)))
						)),
						checkInBaggage(expect(
							CheckInBaggageVerifier.standardBaggage(expect(value("40", BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT))),
							CheckInBaggageVerifier.memberBaggage(expect(value("25", BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT))),
							CheckInBaggageVerifier.limit(expect(value("7", BaggageUnitEnum.BAGGAGE_PIECE_UNIT)))
						))
					))
				)
			)
		);
		
		bookingVerifier.verify(booking);
	}
	
}
