package com.cathaypacific.mmbbizrule.business.impl;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.scheduling.annotation.AsyncResult;

import com.cathaypacific.mbcommon.enums.baggage.BaggageUnitEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.BaggageHistoryDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.BaggageInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.DailyHistoryDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.PassengerInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.PurchaseHistoryResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.SeatDetailDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.SeatHistoryDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.SegmentInfoDTO;
import com.cathaypacific.mmbbizrule.handler.JourneyCalculateHelper;
import com.cathaypacific.mmbbizrule.model.journey.JourneySegment;
import com.cathaypacific.mmbbizrule.model.journey.JourneySummary;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBaggage;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElementsOtherData;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDepartureArrivalTime;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrFa;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPaymentInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSeat;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSeatDetail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.enums.PurchaseProductTypeEnum;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessBaggageAllowance;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessCouponGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessDetailGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessFareDetail;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessFlightInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessDocGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessProductInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.TicketProcessInvokeService;

@RunWith(MockitoJUnitRunner.class)
public class BookingPurchaseHistoryBusinessImplTest {
	@InjectMocks
	private BookingPurchaseHistoryBusinessImpl bookingPurchaseHistoryBusinessImpl;
	@Mock
	private PnrInvokeService pnrInvokeService;
	@Mock
	private TicketProcessInvokeService ticketProcessInvokeService;
	@Mock
	private JourneyCalculateHelper journeyCalculateHelper;

	private RetrievePnrBooking pnrBooking;

	private TicketProcessInfo ticketProcessInfo;

	private Future<List<JourneySummary>> futureJourneySummary;

	private List<JourneySummary> JourneySummarys;
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		pnrBooking = new RetrievePnrBooking();
		
		pnrBooking.setOneARloc("AAAAAA");
		
		List<RetrievePnrFa> fas = new ArrayList<>();
		RetrievePnrFa fa1 = new RetrievePnrFa();
		fa1.setLongFreeText("PAX 160-0000000001/DTCX/HKD180/01JAN18/HKGCX0988/13392573");
		fa1.setPassengerId("1");
		RetrievePnrFa fa2 = new RetrievePnrFa();
		fa2.setLongFreeText("PAX 160-0000000002/DTCX/HKD1080/01JAN18/HKGCX0988/13392573");
		fa2.setPassengerId("1");
		fas.add(fa1);
		fas.add(fa2);
		pnrBooking.setFaList(fas);
		
		List<RetrievePnrDataElements> fhds = new ArrayList<>();
		RetrievePnrDataElements fhd = new RetrievePnrDataElements();
		RetrievePnrDataElementsOtherData otherData = new RetrievePnrDataElementsOtherData();
		otherData.setFreeText("PAX 160-0000000003/DTCX/HKD2016/03JAN18/HKGCX0988/13392573");
		fhd.findOtherDataList().add(otherData);
		fhd.setPassengerId("1");
		fhds.add(fhd);
		pnrBooking.setFhdList(fhds);

		List<RetrievePnrPassenger> pnrPassengers = new ArrayList<>();
		RetrievePnrPassenger pnrPassenger1 = new RetrievePnrPassenger();
		pnrPassenger1.setPassengerID("1");
		pnrPassenger1.setFamilyName("FAMILYNAME1");
		pnrPassenger1.setGivenName("GIVENNAME1");
		pnrPassengers.add(pnrPassenger1);
		pnrBooking.setPassengers(pnrPassengers);

		List<RetrievePnrSegment> pnrSegments = new ArrayList<>();
		RetrievePnrSegment pnrSegment1 = new RetrievePnrSegment();
		pnrSegment1.setSegmentID("1");
		pnrSegment1.setMarketCompany("CX");
		pnrSegment1.setMarketSegmentNumber("111");
		RetrievePnrDepartureArrivalTime departureArrivalTime1 = new RetrievePnrDepartureArrivalTime();
		departureArrivalTime1.setPnrTime("2020-01-01 00:00");
		pnrSegment1.setDepartureTime(departureArrivalTime1);
		pnrSegment1.setOriginPort("TPE");
		pnrSegment1.setDestPort("HKG");
		
		RetrievePnrSegment pnrSegment2 = new RetrievePnrSegment();
		pnrSegment2.setSegmentID("2");
		pnrSegment2.setMarketCompany("CX");
		pnrSegment2.setMarketSegmentNumber("222");
		RetrievePnrDepartureArrivalTime departureArrivalTime2 = new RetrievePnrDepartureArrivalTime();
		departureArrivalTime2.setPnrTime("2020-01-02 00:00");
		pnrSegment2.setDepartureTime(departureArrivalTime2);
		pnrSegment2.setOriginPort("HKG");
		pnrSegment2.setDestPort("SHG");
		
		pnrSegments.add(pnrSegment1);
		pnrSegments.add(pnrSegment2);
		pnrBooking.setSegments(pnrSegments);

		List<RetrievePnrPassengerSegment> pnrPassengerSegments = new ArrayList<>();
		RetrievePnrPassengerSegment pnrPassengerSegment1 = new RetrievePnrPassengerSegment();
		pnrPassengerSegment1.setPassengerId("1");
		pnrPassengerSegment1.setSegmentId("1");

		RetrievePnrSeat seat = new RetrievePnrSeat();
		RetrievePnrSeatDetail seatDetail = new RetrievePnrSeatDetail();
		RetrievePnrPaymentInfo seatPaymentInfo = new RetrievePnrPaymentInfo();
		seatPaymentInfo.setTicket("1600000000001");
		seatDetail.setPaymentInfo(seatPaymentInfo);
		seat.setSeatDetail(seatDetail);
		pnrPassengerSegment1.setSeat(seat);

		List<RetrievePnrBaggage> baggages = new ArrayList<>();
		RetrievePnrBaggage baggage1 = new RetrievePnrBaggage();
		RetrievePnrPaymentInfo baggagePayment1 = new RetrievePnrPaymentInfo();
		baggagePayment1.setTicket("1600000000002");
		baggage1.setPaymentInfo(baggagePayment1);
		baggages.add(baggage1);
		RetrievePnrBaggage baggage2 = new RetrievePnrBaggage();
		RetrievePnrPaymentInfo baggagePayment2 = new RetrievePnrPaymentInfo();
		baggagePayment2.setTicket("1600000000003");
		baggage2.setPaymentInfo(baggagePayment2);
		baggages.add(baggage2);
		
		pnrPassengerSegment1.setBaggages(baggages);
		
		RetrievePnrPassengerSegment pnrPassengerSegment2 = new RetrievePnrPassengerSegment();
		pnrPassengerSegment2.setPassengerId("1");
		pnrPassengerSegment2.setSegmentId("2");

		List<RetrievePnrBaggage> baggages1 = new ArrayList<>();
		RetrievePnrBaggage baggage3 = new RetrievePnrBaggage();
		RetrievePnrPaymentInfo baggagePayment3 = new RetrievePnrPaymentInfo();
		baggagePayment3.setTicket("1600000000002");
		baggage3.setPaymentInfo(baggagePayment3);
		baggages1.add(baggage3);
		RetrievePnrBaggage baggage4 = new RetrievePnrBaggage();
		RetrievePnrPaymentInfo baggagePayment4 = new RetrievePnrPaymentInfo();
		baggagePayment4.setTicket("1600000000003");
		baggage4.setPaymentInfo(baggagePayment4);
		baggages1.add(baggage4);
		
		pnrPassengerSegment2.setBaggages(baggages1);
		
		pnrPassengerSegments.add(pnrPassengerSegment1);
		pnrPassengerSegments.add(pnrPassengerSegment2);
		pnrBooking.setPassengerSegments(pnrPassengerSegments);

		ticketProcessInfo = new TicketProcessInfo();
		ticketProcessInfo.setMessageFunction("791");

		List<TicketProcessDocGroup> infoGroups = new ArrayList<>();
		TicketProcessDocGroup seatInfoGroup = new TicketProcessDocGroup();
		TicketProcessFareDetail seatFare = new TicketProcessFareDetail();
		seatFare.setAmount(new BigDecimal("180"));
		seatFare.setCurrency("HKD");
		seatInfoGroup.setFareInfo(seatFare);
		TicketProcessFareDetail seatTax = new TicketProcessFareDetail();
		seatTax.setAmount(new BigDecimal("20"));
		seatTax.setCurrency("HKD");
		seatInfoGroup.setTaxInfo(seatTax);
		TicketProcessProductInfo seatProductInfo = new TicketProcessProductInfo();
		seatProductInfo.setProductDate("010118");
		seatInfoGroup.setProductInfo(seatProductInfo);
		TicketProcessDetailGroup seatTicketProcessDetailInfo = new TicketProcessDetailGroup();
		seatTicketProcessDetailInfo.setEticket("1600000000001");
		TicketProcessCouponGroup seatTicketProcessCouponInfo = new TicketProcessCouponGroup();
		seatTicketProcessCouponInfo.setPurchaseProductType(PurchaseProductTypeEnum.SEAT_EXTRA_LEGROOM);
		TicketProcessFlightInfo seatTicketProcessFlightInfo = new TicketProcessFlightInfo();
		seatTicketProcessFlightInfo.setBoardPoint("TPE");
		seatTicketProcessFlightInfo.setOffpoint("HKG");
		seatTicketProcessCouponInfo.findFlightInfos().add(seatTicketProcessFlightInfo);
		seatTicketProcessDetailInfo.findCouponGroups().add(seatTicketProcessCouponInfo);
		seatInfoGroup.findDetailInfos().add(seatTicketProcessDetailInfo);
		infoGroups.add(seatInfoGroup);

		TicketProcessDocGroup baggageInfoGroup1 = new TicketProcessDocGroup();
		TicketProcessFareDetail baggageFare1 = new TicketProcessFareDetail();
		baggageFare1.setAmount(new BigDecimal("1080"));
		baggageFare1.setCurrency("HKD");
		baggageInfoGroup1.setFareInfo(baggageFare1);
		TicketProcessFareDetail baggageTax1 = new TicketProcessFareDetail();
		baggageTax1.setAmount(new BigDecimal("20"));
		baggageTax1.setCurrency("HKD");
		baggageInfoGroup1.setTaxInfo(baggageTax1);
		TicketProcessProductInfo baggageProductInfo1 = new TicketProcessProductInfo();
		baggageProductInfo1.setProductDate("010118");
		baggageInfoGroup1.setProductInfo(baggageProductInfo1);
		TicketProcessDetailGroup baggageTicketProcessDetailInfo1 = new TicketProcessDetailGroup();
		baggageTicketProcessDetailInfo1.setEticket("1600000000002");
		
		TicketProcessCouponGroup baggageTicketProcessCouponInfo1 = new TicketProcessCouponGroup();
		baggageTicketProcessCouponInfo1.setPurchaseProductType(PurchaseProductTypeEnum.BAGGAGE);
		TicketProcessFlightInfo baggageTicketProcessFlightInfo1 = new TicketProcessFlightInfo();
		baggageTicketProcessFlightInfo1.setBoardPoint("TPE");
		baggageTicketProcessFlightInfo1.setOffpoint("HKG");
		baggageTicketProcessCouponInfo1.findFlightInfos().add(baggageTicketProcessFlightInfo1);
		TicketProcessBaggageAllowance baggageTicketProcessBaggageAllowance1 = new TicketProcessBaggageAllowance();
		baggageTicketProcessBaggageAllowance1.setNumber(new BigInteger("5"));
		baggageTicketProcessBaggageAllowance1.setUnit(BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT);
		baggageTicketProcessCouponInfo1.findBaggageAllowances().add(baggageTicketProcessBaggageAllowance1);
		baggageTicketProcessDetailInfo1.findCouponGroups().add(baggageTicketProcessCouponInfo1);
		
		TicketProcessCouponGroup baggageTicketProcessCouponInfo2 = new TicketProcessCouponGroup();
		baggageTicketProcessCouponInfo2.setPurchaseProductType(PurchaseProductTypeEnum.BAGGAGE);
		TicketProcessFlightInfo baggageTicketProcessFlightInfo2 = new TicketProcessFlightInfo();
		baggageTicketProcessFlightInfo2.setBoardPoint("HKG");
		baggageTicketProcessFlightInfo2.setOffpoint("SHG");
		baggageTicketProcessCouponInfo2.findFlightInfos().add(baggageTicketProcessFlightInfo2);
		TicketProcessBaggageAllowance baggageTicketProcessBaggageAllowance2 = new TicketProcessBaggageAllowance();
		baggageTicketProcessBaggageAllowance2.setNumber(new BigInteger("5"));
		baggageTicketProcessBaggageAllowance2.setUnit(BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT);
		baggageTicketProcessCouponInfo2.findBaggageAllowances().add(baggageTicketProcessBaggageAllowance2);
		baggageTicketProcessDetailInfo1.findCouponGroups().add(baggageTicketProcessCouponInfo1);
		baggageTicketProcessDetailInfo1.findCouponGroups().add(baggageTicketProcessCouponInfo2);
		
		baggageInfoGroup1.findDetailInfos().add(baggageTicketProcessDetailInfo1);		
		infoGroups.add(baggageInfoGroup1);
		
		TicketProcessDocGroup baggageInfoGroup2 = new TicketProcessDocGroup();
		TicketProcessFareDetail baggageFare2 = new TicketProcessFareDetail();
		baggageFare2.setAmount(new BigDecimal("2016"));
		baggageFare2.setCurrency("HKD");
		baggageInfoGroup2.setFareInfo(baggageFare2);
		TicketProcessProductInfo baggageProductInfo2 = new TicketProcessProductInfo();
		baggageProductInfo2.setProductDate("020118");
		baggageInfoGroup2.setProductInfo(baggageProductInfo2);
		TicketProcessDetailGroup baggageTicketProcessDetailInfo2 = new TicketProcessDetailGroup();
		baggageTicketProcessDetailInfo2.setEticket("1600000000003");
		
		TicketProcessCouponGroup baggageTicketProcessCouponInfo3 = new TicketProcessCouponGroup();
		baggageTicketProcessCouponInfo3.setPurchaseProductType(PurchaseProductTypeEnum.BAGGAGE);
		TicketProcessFlightInfo baggageTicketProcessFlightInfo3 = new TicketProcessFlightInfo();
		baggageTicketProcessFlightInfo3.setBoardPoint("TPE");
		baggageTicketProcessFlightInfo3.setOffpoint("HKG");
		baggageTicketProcessCouponInfo3.findFlightInfos().add(baggageTicketProcessFlightInfo3);
		TicketProcessBaggageAllowance baggageTicketProcessBaggageAllowance3 = new TicketProcessBaggageAllowance();
		baggageTicketProcessBaggageAllowance3.setNumber(new BigInteger("10"));
		baggageTicketProcessBaggageAllowance3.setUnit(BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT);
		baggageTicketProcessCouponInfo3.findBaggageAllowances().add(baggageTicketProcessBaggageAllowance3);
				
		TicketProcessCouponGroup baggageTicketProcessCouponInfo4 = new TicketProcessCouponGroup();
		baggageTicketProcessCouponInfo4.setPurchaseProductType(PurchaseProductTypeEnum.BAGGAGE);
		TicketProcessFlightInfo baggageTicketProcessFlightInfo4 = new TicketProcessFlightInfo();
		baggageTicketProcessFlightInfo4.setBoardPoint("HKG");
		baggageTicketProcessFlightInfo4.setOffpoint("SHG");
		baggageTicketProcessCouponInfo4.findFlightInfos().add(baggageTicketProcessFlightInfo4);
		TicketProcessBaggageAllowance baggageTicketProcessBaggageAllowance4 = new TicketProcessBaggageAllowance();
		baggageTicketProcessBaggageAllowance4.setNumber(new BigInteger("10"));
		baggageTicketProcessBaggageAllowance4.setUnit(BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT);
		baggageTicketProcessCouponInfo4.findBaggageAllowances().add(baggageTicketProcessBaggageAllowance4);
		
		baggageTicketProcessDetailInfo2.findCouponGroups().add(baggageTicketProcessCouponInfo3);
		baggageTicketProcessDetailInfo2.findCouponGroups().add(baggageTicketProcessCouponInfo4);
		baggageInfoGroup2.findDetailInfos().add(baggageTicketProcessDetailInfo2);
		infoGroups.add(baggageInfoGroup2);
		
		ticketProcessInfo.setDocGroups(infoGroups);
	}

	@Test
	public void testRetrieveHistory() throws BusinessBaseException, InterruptedException, ExecutionException {
		when(pnrInvokeService.retrievePnrByRloc(anyString())).thenReturn(pnrBooking);
		when(ticketProcessInvokeService.getTicketProcessInfo(anyString(), anyObject())).thenReturn(ticketProcessInfo);
		
		JourneySummarys = new ArrayList<>();
		JourneySummary journeySummary = new JourneySummary();
		journeySummary.setJourneyId("1");
		
		List<JourneySegment> journeySegments = new ArrayList<>();
		JourneySegment journeySegment = new JourneySegment();
		journeySegment.setSegmentId("1");
		JourneySegment journeySegment1 = new JourneySegment();
		journeySegment1.setSegmentId("2");
		journeySegments.add(journeySegment);
		journeySegments.add(journeySegment1);
		journeySummary.setSegments(journeySegments);
		JourneySummarys.add(journeySummary);
		futureJourneySummary = new AsyncResult<List<JourneySummary>>(JourneySummarys);
		when(journeyCalculateHelper.asyncCalculateJourneyFromDpEligibility(anyString())).thenReturn(futureJourneySummary);
		
		PurchaseHistoryResponseDTO responseDTO = bookingPurchaseHistoryBusinessImpl.retrieveHistroy("AAAAAAA");
		
		Assert.assertEquals(2, responseDTO.getPurchaseHistory().size());
		
		PassengerInfoDTO passengerInfo = responseDTO.getPassengers().get(0);
		Assert.assertEquals("1", passengerInfo.getPassengerId());
		Assert.assertEquals("FAMILYNAME1", passengerInfo.getFamilyName());
		Assert.assertEquals("GIVENNAME1", passengerInfo.getGivenName());
		
		SegmentInfoDTO segmentInfo = responseDTO.getSegments().get(0);
		Assert.assertEquals("1", segmentInfo.getSegmentId());
		Assert.assertEquals("CX", segmentInfo.getCompanyId());
		Assert.assertEquals("111", segmentInfo.getSegmentNumber());
		Assert.assertEquals("TPE", segmentInfo.getOriginPort());
		Assert.assertEquals("HKG", segmentInfo.getDestPort());
		Assert.assertEquals("2020-01-01 00:00", segmentInfo.getDepartureTime());
		
		SegmentInfoDTO segmentInfo1 = responseDTO.getSegments().get(1);
		Assert.assertEquals("2", segmentInfo1.getSegmentId());
		Assert.assertEquals("CX", segmentInfo1.getCompanyId());
		Assert.assertEquals("222", segmentInfo1.getSegmentNumber());
		Assert.assertEquals("HKG", segmentInfo1.getOriginPort());
		Assert.assertEquals("SHG", segmentInfo1.getDestPort());
		Assert.assertEquals("2020-01-02 00:00", segmentInfo1.getDepartureTime());
		
		List<DailyHistoryDTO> purchaseHistory = responseDTO.getPurchaseHistory();
		
		for (int i = 0; i < purchaseHistory.size(); i++) {
			if (i == 0) {
				List<SeatHistoryDTO> seatHistorys = purchaseHistory.get(i).getSeatHistory();
				Assert.assertEquals(1, seatHistorys.size());
				SeatHistoryDTO seatHistory = seatHistorys.get(0);
				Assert.assertEquals(1, seatHistory.getSeats().size());
				SeatDetailDTO seatDetail = seatHistory.getSeats().get(0);
				Assert.assertEquals("1", seatDetail.getPassengerId());
				Assert.assertEquals("1", seatDetail.getSegmentId());
				Assert.assertEquals("EXL", seatDetail.getSeatType());
				Assert.assertEquals(new BigDecimal(180), seatHistory.getFare().getAmount());
				Assert.assertEquals("HKD", seatHistory.getFare().getCurrency());
				Assert.assertEquals(new BigDecimal(20), seatHistory.getTax().getAmount());
				Assert.assertEquals("HKD", seatHistory.getTax().getCurrency());
								
				List<BaggageHistoryDTO> baggageHistorys = purchaseHistory.get(i).getBaggageHistory();
				Assert.assertEquals(1, baggageHistorys.size());
				BaggageHistoryDTO baggageHistory = baggageHistorys.get(0);
				Assert.assertEquals("1", baggageHistory.getBaggageInfos().get(0).getPassengerId());
				Assert.assertEquals(1, baggageHistory.getBaggageInfos().size());
				BaggageInfoDTO baggageInfo = baggageHistory.getBaggageInfos().get(0);
				Assert.assertEquals(2, baggageInfo.getSegmentIds().size());
				Assert.assertEquals("1", baggageInfo.getSegmentIds().get(0));
				Assert.assertEquals("2", baggageInfo.getSegmentIds().get(1));
				Assert.assertEquals(new BigInteger("5"), baggageInfo.getAmount());
				Assert.assertEquals("K", baggageInfo.getUnit().getUnit());
				Assert.assertEquals(new BigDecimal(1080), baggageHistory.getFare().getAmount());
				Assert.assertEquals("HKD", baggageHistory.getFare().getCurrency());
				Assert.assertEquals(new BigDecimal(20), baggageHistory.getTax().getAmount());
				Assert.assertEquals("HKD", baggageHistory.getTax().getCurrency());
				
				Assert.assertEquals("2018-01-01", purchaseHistory.get(i).getPurchaseDate());
				
			} else if (i == 1) {
				List<BaggageHistoryDTO> baggageHistorys = purchaseHistory.get(i).getBaggageHistory();
				Assert.assertEquals(1, baggageHistorys.size());
				BaggageHistoryDTO baggageHistory = baggageHistorys.get(0);
				Assert.assertEquals("1", baggageHistory.getBaggageInfos().get(0).getPassengerId());
				Assert.assertEquals(1, baggageHistory.getBaggageInfos().size());
				BaggageInfoDTO baggageInfo = baggageHistory.getBaggageInfos().get(0);
				Assert.assertEquals(2, baggageInfo.getSegmentIds().size());
				Assert.assertEquals("1", baggageInfo.getSegmentIds().get(0));
				Assert.assertEquals("2", baggageInfo.getSegmentIds().get(1));
				Assert.assertEquals(new BigInteger("10"), baggageInfo.getAmount());
				Assert.assertEquals("K", baggageInfo.getUnit().getUnit());
				Assert.assertEquals(new BigDecimal(2016), baggageHistory.getFare().getAmount());
				Assert.assertEquals("HKD", baggageHistory.getFare().getCurrency());
				Assert.assertNull(baggageHistory.getTax());
				
				Assert.assertEquals("2018-01-02", purchaseHistory.get(i).getPurchaseDate());
				
				Assert.assertNull(purchaseHistory.get(i).getSeatHistory());
			}
		}
	}
	
	@Test
	public void testNoBookingFound() throws BusinessBaseException { 
		pnrBooking.setFaList(null);
		pnrBooking.setFhdList(null);
		
		when(pnrInvokeService.retrievePnrByRloc(anyString())).thenReturn(null);
		when(ticketProcessInvokeService.getTicketProcessInfo(anyString(), anyObject())).thenReturn(ticketProcessInfo);
		
		JourneySummarys = new ArrayList<>();
		JourneySummary journeySummary = new JourneySummary();
		journeySummary.setJourneyId("1");
		
		List<JourneySegment> journeySegments = new ArrayList<>();
		JourneySegment journeySegment = new JourneySegment();
		journeySegment.setSegmentId("1");
		journeySegments.add(journeySegment);
		journeySummary.setSegments(journeySegments);
		JourneySummarys.add(journeySummary);
		futureJourneySummary = new AsyncResult<List<JourneySummary>>(JourneySummarys);
		when(journeyCalculateHelper.asyncCalculateJourneyFromDpEligibility(anyString())).thenReturn(futureJourneySummary);
		
		thrown.expect(UnexpectedException.class);
		thrown.expectMessage("Unable to get booking purchase history - Cannot find booking by rloc:AAAAAA");
		bookingPurchaseHistoryBusinessImpl.retrieveHistroy("AAAAAA");
	}
	
	@Test
	public void testNoHistoryFound() throws BusinessBaseException { 
		pnrBooking.setFaList(null);
		pnrBooking.setFhdList(null);
		
		when(pnrInvokeService.retrievePnrByRloc(anyString())).thenReturn(pnrBooking);
		when(ticketProcessInvokeService.getTicketProcessInfo(anyString(), anyObject())).thenReturn(ticketProcessInfo);
		
		JourneySummarys = new ArrayList<>();
		JourneySummary journeySummary = new JourneySummary();
		journeySummary.setJourneyId("1");
		
		List<JourneySegment> journeySegments = new ArrayList<>();
		JourneySegment journeySegment = new JourneySegment();
		journeySegment.setSegmentId("1");
		journeySegments.add(journeySegment);
		journeySummary.setSegments(journeySegments);
		JourneySummarys.add(journeySummary);
		futureJourneySummary = new AsyncResult<List<JourneySummary>>(JourneySummarys);
		when(journeyCalculateHelper.asyncCalculateJourneyFromDpEligibility(anyString())).thenReturn(futureJourneySummary);
		
		thrown.expect(com.cathaypacific.mbcommon.exception.ExpectedException.class);
		thrown.expectMessage("No purchase history found in booking :AAAAAA");
		bookingPurchaseHistoryBusinessImpl.retrieveHistroy("AAAAAA");
	}

}
