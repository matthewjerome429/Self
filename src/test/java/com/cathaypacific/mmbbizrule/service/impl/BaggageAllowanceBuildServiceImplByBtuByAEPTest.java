package com.cathaypacific.mmbbizrule.service.impl;

import static org.mockito.Mockito.*;

import java.math.BigInteger;

import static com.cathaypacific.mmbbizrule.cxservice.aep.mocker.aep_1_1.AEPAirProductMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.aep.mocker.aep_1_1.AEPPassengerWithNameMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.aep.mocker.aep_1_1.AEPProductMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.aep.mocker.aep_1_1.AEPProductValueMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.aep.mocker.aep_1_1.AEPProductsResponseMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.aep.mocker.aep_1_1.AEPSegmentMocker.*;
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
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.btu.BgAlBtuResponseDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.btu.BtuResponseDTOMocker.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.verifier.btu.BgAlBtuRequestDTOVerifier.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.verifier.btu.BgAlBtuSegmentDTOVerifier.*;
import static com.cathaypacific.mmbbizrule.cxservice.baggageallowance.verifier.btu.BtuRequestDTOVerifier.*;
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
import com.cathaypacific.mmbbizrule.cxservice.aep.model.aep_1_1.AEPProductsResponse;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CabinBaggagePieceDTOMocker;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CabinBaggageWeightDTOMocker;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CabinSmallItemDTOMocker;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CheckinBaggagePieceDTOMocker;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.CheckinBaggageWeightDTOMocker;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.btu.BgAlBtuResponseDTOMocker;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.mocker.btu.BtuResponseDTOMocker;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.btu.BgAlBtuRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.od.BgAlOdRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.btu.BgAlBtuResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.service.impl.BaggageAllowanceServiceImpl;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.verifier.btu.BgAlBtuSegmentDTOVerifier;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.verifier.btu.BtuRequestDTOVerifier;
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
public class BaggageAllowanceBuildServiceImplByBtuByAEPTest {

	@InjectMocks
	private BaggageAllowanceBuildServiceImpl baggageAllowanceBuildService;
	
	@Mock
	private BaggageAllowanceServiceImpl baggageAllowanceService;
	
	/**
	 * Retrieve baggage allowance by BTU by AEP baggage products.
	 * Baggage allowance by OD API should not be invoked.
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
				mock(SegmentMocker.segmentID("1"), SegmentMocker.cabinClass("J"),
					departure(mock(timeZoneOffset("+0800"), pnrTime("2000-01-20 08:15")))
				)
			),
			passengerSegments(
				mock(PassengerSegmentMocker.passengerSegmentID("1", "1"))
			)
		);
		
		AEPProductsResponse aepProductsResponse = mock( 
			products(
				mock(productId(AEPProductIdEnum.BAGGAGE_COMMON.getId()), sellOnOffline(false),
					airProduct(
						mock( 
							segments(
								mock(segmentRef(1), airport("OG1","DS1"), flight("FL1", 500))
							),
							passengers(
								mock(passengerRef(1), name("FN1", "GN1"))
							)
						)
					),
					productValues(
						mock(value("1"), amount("1111.00"), currency("HKD"))
					)
				)
			)
		);
		
		// Detail data of baggage allowance are not used in this test case.
		BgAlBtuResponseDTO responseDTO = mock(
			BgAlBtuResponseDTOMocker.btu(
				BtuResponseDTOMocker.mock()
			)
		);
		
		when(baggageAllowanceService.getBaggageAllowanceByBtu(isA(BgAlBtuRequestDTO.class))).thenReturn(responseDTO);
		
		baggageAllowanceBuildService.retrieveBaggageAllowanceInfo(booking, aepProductsResponse);
		
		// Baggage allowance by OD is NOT invoked.
		verify(baggageAllowanceService, never()).getBaggageAllowanceByOd(any(BgAlOdRequestDTO.class));
		
		/*
		 *  Verify request of baggage allowance by BTU.
		 */
		Verifier<BgAlBtuRequestDTO> requestDTOVerifier = expect(btu(
				expect(
					BtuRequestDTOVerifier.btuId("1"), baggageAllowanceType(AEPProductIdEnum.BAGGAGE_COMMON.getId()),
					segments(
						expect(BgAlBtuSegmentDTOVerifier.point("OG1", "DS1"), BgAlBtuSegmentDTOVerifier.operatingCarrier("FL1"),
							BgAlBtuSegmentDTOVerifier.cabinClass("J"), BgAlBtuSegmentDTOVerifier.departure("200100", "0015")
						)
					)
				)
			));
		
		ArgumentCaptor<BgAlBtuRequestDTO> requestCaptor = ArgumentCaptor.forClass(BgAlBtuRequestDTO.class);
		verify(baggageAllowanceService, times(1)).getBaggageAllowanceByBtu(requestCaptor.capture());
		BgAlBtuRequestDTO requestDTO = requestCaptor.getValue();
		requestDTOVerifier.verify(requestDTO);
	}
	
	/**
	 * Retrieve baggage allowance by BTU by AEP baggage products.
	 * Baggage allowance by OD API should not be invoked.
	 * 
	 * Use airport upgrade cabin class instead of original segment cabin class in baggage allowance BTU request.
	 * 
	 * @throws Exception
	 */
	@Test
	public void retrieveBaggageAllowanceInfoWithAirportUpgrade() throws Exception {
		
		Booking booking = mock(oneARloc("AAA000"), 
			passengers(
				mock(PassengerMocker.passengerID("1"))
			),
			segments(
				mock(SegmentMocker.segmentID("1"), SegmentMocker.cabinClass("J"),
					departure(mock(timeZoneOffset("+0800"), pnrTime("2000-01-20 08:15")))
				)
			),
			passengerSegments(
				mock(PassengerSegmentMocker.passengerSegmentID("1", "1"),
					airportUpgradeInfo(mock(newCabinClass("F")))
				)
			)
		);
		
		AEPProductsResponse aepProductsResponse = mock( 
			products(
				mock(productId(AEPProductIdEnum.BAGGAGE_COMMON.getId()), sellOnOffline(false),
					airProduct(
						mock( 
							segments(
								mock(segmentRef(1), airport("OG1","DS1"), flight("FL1", 500))
							),
							passengers(
								mock(passengerRef(1), name("FN1", "GN1"))
							)
						)
					),
					productValues(
						mock(value("1"), amount("1111.00"), currency("HKD"))
					)
				)
			)
		);
		
		// Detail data of baggage allowance are not used in this test case.
		BgAlBtuResponseDTO responseDTO = mock(
			BgAlBtuResponseDTOMocker.btu(
				BtuResponseDTOMocker.mock()
			)
		);
		
		when(baggageAllowanceService.getBaggageAllowanceByBtu(isA(BgAlBtuRequestDTO.class))).thenReturn(responseDTO);
		
		baggageAllowanceBuildService.retrieveBaggageAllowanceInfo(booking, aepProductsResponse);
		
		// Baggage allowance by OD is NOT invoked.
		verify(baggageAllowanceService, never()).getBaggageAllowanceByOd(any(BgAlOdRequestDTO.class));
		
		/*
		 *  Verify request of baggage allowance by BTU.
		 */
		Verifier<BgAlBtuRequestDTO> requestDTOVerifier = expect(btu(
				expect(
					BtuRequestDTOVerifier.btuId("1"), baggageAllowanceType(AEPProductIdEnum.BAGGAGE_COMMON.getId()),
					segments(
						expect(BgAlBtuSegmentDTOVerifier.point("OG1", "DS1"), BgAlBtuSegmentDTOVerifier.operatingCarrier("FL1"),
							BgAlBtuSegmentDTOVerifier.cabinClass("F"), BgAlBtuSegmentDTOVerifier.departure("200100", "0015")
						)
					)
				)
			));
		
		ArgumentCaptor<BgAlBtuRequestDTO> requestCaptor = ArgumentCaptor.forClass(BgAlBtuRequestDTO.class);
		verify(baggageAllowanceService, times(1)).getBaggageAllowanceByBtu(requestCaptor.capture());
		BgAlBtuRequestDTO requestDTO = requestCaptor.getValue();
		requestDTOVerifier.verify(requestDTO);
	}
	
	@Test
	public void populateBaggageAllowance() throws Exception {
		
		Booking booking = mock(oneARloc("AAA000"), 
			passengers(
				mock(PassengerMocker.passengerID("1"))
			),
			segments(
				mock(SegmentMocker.segmentID("1"), SegmentMocker.cabinClass("J"),
					departure(mock(timeZoneOffset("+0800"), pnrTime("2000-01-20 08:15")))
				)
			),
			passengerSegments(
				mock(PassengerSegmentMocker.passengerSegmentID("1", "1"),
					fqtvInfo(mock(tierLevel("GO")))
				)
			)
		);
		
		AEPProductsResponse aepProductsResponse = mock( 
			products(
				mock(productId(AEPProductIdEnum.BAGGAGE_COMMON.getId()), sellOnOffline(false),
					airProduct(
						mock( 
							segments(
								mock(segmentRef(1), airport("OG1","DS1"), flight("FL1", 500))
							),
							passengers(
								mock(passengerRef(1), name("FN1", "GN1"))
							)
						)
					),
					productValues(
						mock(value("1"), amount("1111.00"), currency("HKD"))
					)
				)
			)
		);
		
		BgAlBtuResponseDTO responseDTO = mock(
			BgAlBtuResponseDTOMocker.btu(
				mock(BtuResponseDTOMocker.btuId("1"),
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
		);
		
		BaggageAllowanceInfo baggageAllowanceInfo = new BaggageAllowanceInfo();
		baggageAllowanceInfo.setBaggageAEPProducts(aepProductsResponse.getProducts());
		baggageAllowanceInfo.setBtuDTOs(responseDTO.getBtu());
		
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
				mock(SegmentMocker.segmentID("1"), SegmentMocker.cabinClass("J"),
					departure(mock(timeZoneOffset("+0800"), pnrTime("2000-01-20 08:15")))
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
					airportUpgradeInfo(mock(newCabinClass("F"))),
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
		
		AEPProductsResponse aepProductsResponse = mock( 
			products(
				mock(productId(AEPProductIdEnum.BAGGAGE_COMMON.getId()), sellOnOffline(false),
					airProduct(
						mock( 
							segments(
								mock(segmentRef(1), airport("OG1","DS1"), flight("FL1", 500))
							),
							passengers(
								mock(passengerRef(1), name("FN1", "GN1"))
							)
						)
					),
					productValues(
						mock(value("1"), amount("1111.00"), currency("HKD"))
					)
				),
				mock(productId(AEPProductIdEnum.BAGGAGE_COMMON.getId()), sellOnOffline(false),
					airProduct(
						mock( 
							segments(
								mock(segmentRef(1), airport("OG1","DS1"), flight("FL1", 500))
							),
							passengers(
								mock(passengerRef(2), name("FN2", "GN2"))
							)
						)
					),
					productValues(
						mock(value("1"), amount("1111.00"), currency("HKD"))
					)
				)
			)
		);
		
		BgAlBtuResponseDTO responseDTO = mock(
			BgAlBtuResponseDTOMocker.btu(
				mock(BtuResponseDTOMocker.btuId("1"),
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
				),
				mock(BtuResponseDTOMocker.btuId("2"),
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
									CheckinBaggageWeightDTOMocker.infant(10))),
								piece(mock(CheckinBaggagePieceDTOMocker.standard(4), CheckinBaggagePieceDTOMocker.member(3),
									CheckinBaggagePieceDTOMocker.infant(1))),
								dimension(mock(values(1, 1, 1)))
							))
						)
					)
				)
			)
		);
		
		BaggageAllowanceInfo baggageAllowanceInfo = new BaggageAllowanceInfo();
		baggageAllowanceInfo.setBaggageAEPProducts(aepProductsResponse.getProducts());
		baggageAllowanceInfo.setBtuDTOs(responseDTO.getBtu());
		
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
