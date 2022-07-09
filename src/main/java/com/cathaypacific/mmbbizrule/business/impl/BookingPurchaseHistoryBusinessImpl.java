package com.cathaypacific.mmbbizrule.business.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.booking.LoungeClass;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.business.BookingPurchaseHistoryBusiness;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.BaggageHistoryDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.BaggageInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.DailyHistoryDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.LoungeHistoryDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.LoungeInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.PassengerInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.PurchaseHistoryResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.SeatDetailDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.SeatHistoryDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingpurchasehistory.SegmentInfoDTO;
import com.cathaypacific.mmbbizrule.handler.JourneyCalculateHelper;
import com.cathaypacific.mmbbizrule.model.journey.JourneySegment;
import com.cathaypacific.mmbbizrule.model.journey.JourneySummary;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrFa;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrLoungeInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.util.FreeTextUtil;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.enums.PurchaseProductTypeEnum;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessBaggageAllowance;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessCouponGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessDetailGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessDocGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessFareDetail;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessFlightInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.service.TicketProcessInvokeService;

@Service
public class BookingPurchaseHistoryBusinessImpl implements BookingPurchaseHistoryBusiness{
	
	private static LogAgent logger = LogAgent.getLogAgent(BookingPurchaseHistoryBusinessImpl.class);
	@Autowired
	private PnrInvokeService pnrInvokeService;
	@Autowired
	private TicketProcessInvokeService ticketProcessInvokeService;
	@Autowired
	private JourneyCalculateHelper journeyCalculateHelper;
	@Override
	public PurchaseHistoryResponseDTO retrieveHistroy(String oneARloc) throws BusinessBaseException {	
		RetrievePnrBooking pnrBooking = pnrInvokeService.retrievePnrByRloc(oneARloc);
		if(pnrBooking == null) {
			throw new UnexpectedException(
					String.format("Unable to get booking purchase history - Cannot find booking by rloc:%s", oneARloc),
					new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}
		
		Future<List<JourneySummary>> futureJournaySummaryList = journeyCalculateHelper.asyncCalculateJourneyFromDpEligibility(oneARloc);
		
		PurchaseHistoryResponseDTO response = new PurchaseHistoryResponseDTO();
		// ticket and passengerId map
		Map<String, String> paymentTicketPaxIdMap = getPaymentTicketPaxIdMapFromFaFhd(pnrBooking);
		// get ticketProcessInfo by tickets
		TicketProcessInfo ticketProcessInfo = ticketProcessInvokeService.getTicketProcessInfo(
				OneAConstants.TICKET_PROCESS_EDOC_MESSAGE_FUNCTION_PURCHASE_HISTORY, new ArrayList<String>(paymentTicketPaxIdMap.keySet()));
		
		// build History
		buildHistory(pnrBooking, response, ticketProcessInfo, paymentTicketPaxIdMap, futureJournaySummaryList);
		if (CollectionUtils.isEmpty(response.getPurchaseHistory())) {
			throw new ExpectedException(
					String.format("No purchase history found in booking :%s", oneARloc),
					new ErrorInfo(ErrorCodeEnum.ERR_PURCHASEHISTORY_NO_HISTORY_FOUND));
		}
		
		return response;
	}
	
	/**
	 * get payment tickets from FA/FHD
	 * @param pnrBooking
	 * @return Map<String, String>
	 */
	private Map<String, String> getPaymentTicketPaxIdMapFromFaFhd(RetrievePnrBooking pnrBooking) {
		if (pnrBooking == null) {
			return null;
		}
		Map<String, String> paymentTicketPaxIdMap = new HashMap<>();
		// build map by FA
		buildMapByFa(pnrBooking, paymentTicketPaxIdMap);
		// build map by FHD
		buildMapByFhd(pnrBooking, paymentTicketPaxIdMap);
		
		return paymentTicketPaxIdMap;
	}

	/**
	 * build map by FA
	 * @param pnrBooking
	 * @param paymentTicketPaxIdMap
	 */
	private void buildMapByFhd(RetrievePnrBooking pnrBooking, Map<String, String> paymentTicketPaxIdMap) {
		if (pnrBooking != null && paymentTicketPaxIdMap != null && !CollectionUtils.isEmpty(pnrBooking.getFhdList())) {
			for (RetrievePnrDataElements fhd : pnrBooking.getFhdList()) {
				String passengerId = fhd.getPassengerId();
				String freeText = null;
				if (!CollectionUtils.isEmpty(fhd.getOtherDataList()) && !StringUtils.isEmpty(fhd.getOtherDataList().get(0).getFreeText())) {
					freeText = fhd.getOtherDataList().get(0).getFreeText();
				}
				String[] paymentInfo = FreeTextUtil.getPaymentInfoFromFreeText(freeText);
				if (!StringUtils.isEmpty(passengerId) && paymentInfo != null && paymentInfo.length > 4 && !StringUtils.isEmpty(paymentInfo[4])) {
					paymentTicketPaxIdMap.put(paymentInfo[4], passengerId);
				}
			}
		}
	}

	/**
	 * build map by FHD
	 * @param pnrBooking
	 * @param paymentTicketPaxIdMap
	 */
	private void buildMapByFa(RetrievePnrBooking pnrBooking, Map<String, String> paymentTicketPaxIdMap) {
		if (pnrBooking != null && paymentTicketPaxIdMap != null && !CollectionUtils.isEmpty(pnrBooking.getFaList())) {
			for (RetrievePnrFa fa : pnrBooking.getFaList()) {
				if (StringUtils.isEmpty(fa.getLongFreeText()) || StringUtils.isEmpty(fa.getPassengerId())) {
					continue;
				}
				
				String[] paymentInfo = FreeTextUtil.getPaymentInfoFromFreeText(fa.getLongFreeText());
				if(paymentInfo == null || paymentInfo.length< 5 || StringUtils.isEmpty(paymentInfo[4])) {
					continue;
				}
				paymentTicketPaxIdMap.put(paymentInfo[4], fa.getPassengerId());
			}
		}
	}

	/**
	 * retrieve seat history
	 * @param pnrBooking
	 * @param response 
	 * @param ticketProcessInfo 
	 * @param paymentTicketPaxIdMap 
	 * @param futureJournaySummaryList 
	 */
	private void buildHistory(RetrievePnrBooking pnrBooking, PurchaseHistoryResponseDTO response, TicketProcessInfo ticketProcessInfo,
			Map<String, String> paymentTicketPaxIdMap, Future<List<JourneySummary>> futureJournaySummaryList) {
		if (pnrBooking == null || response == null || CollectionUtils.isEmpty(pnrBooking.getSegments())
				|| CollectionUtils.isEmpty(pnrBooking.getPassengers())
				|| CollectionUtils.isEmpty(pnrBooking.getPassengerSegments())
				|| CollectionUtils.isEmpty(ticketProcessInfo.getDocGroups())) {
			return;
		}
		List<String> tickets = new ArrayList<>(paymentTicketPaxIdMap.keySet());
		
		for (String ticket : tickets) {
			buildPurchaseHistoryForTicket(ticket, paymentTicketPaxIdMap, ticketProcessInfo, response, futureJournaySummaryList, pnrBooking);
		}
		// according to OLSSMMB-21140, sometimes we can only get purchase date but can't get product info, so remove the empty dailyHistory
		// sort dailyHistory list by purchase date
		response.setPurchaseHistory(sortDailyHistoryListByDate(removeEmptyDailyHistory(response.getPurchaseHistory())));
	}
	
	/**
	 * remove empty daily history
	 * @param purchaseHistory
	 */
	private List<DailyHistoryDTO> removeEmptyDailyHistory(List<DailyHistoryDTO> dailyHistoryList) {
		if (CollectionUtils.isEmpty(dailyHistoryList)) {
			return dailyHistoryList;
		}
		
		return dailyHistoryList.stream()
				.filter(history -> !CollectionUtils.isEmpty(history.getBaggageHistory())
						|| !CollectionUtils.isEmpty(history.getLoungeHistory())
						|| !CollectionUtils.isEmpty(history.getSeatHistory()))
				.collect(Collectors.toList());	
	}

	/**
	 * build purchase History for Ticket
	 * @param ticket
	 * @param paymentTicketPaxIdMap
	 * @param ticketProcessInfo
	 * @param response
	 * @param futureJournaySummaryList
	 * @param pnrBooking
	 */
	private void buildPurchaseHistoryForTicket(String ticket,Map<String, String> paymentTicketPaxIdMap, TicketProcessInfo ticketProcessInfo, PurchaseHistoryResponseDTO response, 
			Future<List<JourneySummary>> futureJournaySummaryList, RetrievePnrBooking pnrBooking) {
		String passengerId = paymentTicketPaxIdMap == null ? null : paymentTicketPaxIdMap.get(ticket);
		/** find ticketProcessInfoGroup by ticket, if ticketProcessInfo is null or
		 	infoGroups is empty, set value as null **/
		TicketProcessDocGroup ticketProcessInfoGroup = Optional.ofNullable(ticketProcessInfo)
				.map(TicketProcessInfo::getDocGroups).orElseGet(Collections::emptyList).stream()
				.filter(infoGroup -> infoGroup.getFareInfo() != null && infoGroup.getProductInfo() != null
						&& !CollectionUtils.isEmpty(infoGroup.getDetailInfos())
						&& infoGroup.getDetailInfos().stream()
								.anyMatch(detailInfo -> ticket.equals(detailInfo.getEticket())))
				.findFirst().orElse(null);
		
		// find ticketProcessDetailInfo by ticket
		TicketProcessDetailGroup ticketProcessDetailInfo = Optional.ofNullable(ticketProcessInfoGroup)
				.map(TicketProcessDocGroup::getDetailInfos).orElseGet(Collections::emptyList)
				.stream().filter(detailInfo -> ticket.equals(detailInfo.getEticket())
						&& !CollectionUtils.isEmpty(detailInfo.getCouponGroups())
						// detailInfo should contain at least one flight info
						&& detailInfo.getCouponGroups().stream()
								.anyMatch(couponInfo -> !CollectionUtils.isEmpty(couponInfo.getFlightInfos())))
				.findFirst().orElse(null);
		
		if (StringUtils.isEmpty(ticket) || StringUtils.isEmpty(passengerId) || ticketProcessDetailInfo == null
				|| response == null || pnrBooking == null) {
			return;
		}
		
		// get dailyHistory from response by date
		DailyHistoryDTO dailyHistory = findDailyHistoryByDate(response, ticketProcessInfoGroup.getProductInfo().getProductDate());
		// if can't find dailyHistory by date, new a dailyHistory and add it to response
		if(dailyHistory == null) {
			dailyHistory = new DailyHistoryDTO();
			response.findPurchaseHistory().add(dailyHistory);
		}
		
		// get purchase product type
		PurchaseProductTypeEnum purchaseProductType = ticketProcessDetailInfo.getCouponGroups().stream()
				.filter(couponInfo -> couponInfo.getPurchaseProductType() != null)
				.map(TicketProcessCouponGroup::getPurchaseProductType).findFirst().orElse(null);
		
		// fare info
		TicketProcessFareDetail ticketProcessFareInfo = ticketProcessInfoGroup.getFareInfo();
		// tax info
		TicketProcessFareDetail ticketProcessTaxInfo = ticketProcessInfoGroup.getTaxInfo();
		
		List<JourneySummary> journeySummaryList = null;
		if (futureJournaySummaryList != null) {
			try {
				journeySummaryList = futureJournaySummaryList.get();
			} catch (Exception e) {
				logger.warn(String.format("Can't get journey summary for booking %s", pnrBooking.getOneARloc()));
			}
		}

		if (PurchaseProductTypeEnum.SEAT_EXTRA_LEGROOM.equals(purchaseProductType)
				|| PurchaseProductTypeEnum.SEAT_ASR_REGULAR.equals(purchaseProductType)
				|| PurchaseProductTypeEnum.SEAT_ASR_WINDOW.equals(purchaseProductType) 
				|| PurchaseProductTypeEnum.SEAT_ASR_AISLE.equals(purchaseProductType)) {
			// create SeatHistory by payment info
			SeatHistoryDTO seatHistory = createSeatHistory(pnrBooking, response, ticket, passengerId,
					purchaseProductType, ticketProcessFareInfo, ticketProcessTaxInfo);
			if (seatHistory != null) {
				dailyHistory.findSeatHistory().add(seatHistory);
			}
		} else if (PurchaseProductTypeEnum.BAGGAGE.equals(purchaseProductType)) {
			// create baggageHistory by payment info
			BaggageHistoryDTO baggageHistory = createBaggageHistory(pnrBooking, response, ticket, passengerId,
					purchaseProductType, ticketProcessFareInfo, ticketProcessTaxInfo, ticketProcessDetailInfo,
					journeySummaryList);
			if (baggageHistory != null ) {
				dailyHistory.findBaggageHistory().add(baggageHistory);
			}
		} else if (PurchaseProductTypeEnum.LOUNGE_BUSINESS.equals(purchaseProductType)
				|| PurchaseProductTypeEnum.LOUNGE_FIRST.equals(purchaseProductType)) {
			// create lounge history by payment info
			LoungeHistoryDTO loungeHistory = createLoungeHistory(pnrBooking, response, ticket, passengerId,
					purchaseProductType, ticketProcessFareInfo, ticketProcessTaxInfo);
			if (loungeHistory != null) {
				dailyHistory.findLoungeHistory().add(loungeHistory);
			}
		}
		
		// set purchase Date
		dailyHistory.setPurchaseDate(DateUtil.convertDateFormat(ticketProcessInfoGroup.getProductInfo().getProductDate(), DateUtil.DATE_PATTERN_DDMMYY, DateUtil.DATE_PATTERN_YYYY_MM_DD));
	
	}

	/**
	 * sort daily history list by date
	 * @param dailyHistoryList
	 * @return List<DailyHistoryDTO>
	 */
	private List<DailyHistoryDTO> sortDailyHistoryListByDate(List<DailyHistoryDTO> dailyHistoryList) {
		if (CollectionUtils.isEmpty(dailyHistoryList)) {
			return dailyHistoryList;
		}
		return dailyHistoryList.stream().sorted((history1, history2) -> {
			if (StringUtils.isEmpty(history1.getPurchaseDate())) {
				return 1;
			} else if (StringUtils.isEmpty(history2.getPurchaseDate())) {
				return -1;
			} else {
				try {
					Date date1 = DateUtil.getStrToDate(DateUtil.DATE_PATTERN_YYYY_MM_DD, history1.getPurchaseDate());
					Date date2 = DateUtil.getStrToDate(DateUtil.DATE_PATTERN_YYYY_MM_DD, history2.getPurchaseDate());
					return date1.compareTo(date2);
				} catch (Exception e) {
					logger.warn(String.format("Failed to compare date: %s & %s", history1.getPurchaseDate(), history2.getPurchaseDate()), e);
					return -1;
				}
			}
		}).collect(Collectors.toList());
	}

	/**
	 * create baggage History by payment info
	 * @param pnrBooking
	 * @param response
	 * @param ticket
	 * @param passengerId
	 * @param purchaseProductType
	 * @param ticketProcessFareInfo
	 * @param ticketProcessTaxInfo
	 * @return BaggageHistoryDTO
	 */
	private BaggageHistoryDTO createBaggageHistory(RetrievePnrBooking pnrBooking, PurchaseHistoryResponseDTO response,
			String ticket, String passengerId, PurchaseProductTypeEnum purchaseProductType,
			TicketProcessFareDetail ticketProcessFareInfo, TicketProcessFareDetail ticketProcessTaxInfo,
			TicketProcessDetailGroup ticketProcessDetailInfo, List<JourneySummary> journeySummaryList) {
		// find passengerSegments by passengerId, if pnrBooking is null or passengerSegments is empty or passengerId is empty, set value as null
		List<RetrievePnrPassengerSegment> passengerSegmentsOfPax = (pnrBooking == null 
				|| CollectionUtils.isEmpty(pnrBooking.getPassengerSegments()) || StringUtils.isEmpty(passengerId))
						? null
						: pnrBooking.getPassengerSegments().stream()
								.filter(ps -> passengerId.equals(ps.getPassengerId())).collect(Collectors.toList());

		if (response == null || StringUtils.isEmpty(ticket) || purchaseProductType == null
				|| ticketProcessFareInfo == null || CollectionUtils.isEmpty(passengerSegmentsOfPax)
				|| ticketProcessDetailInfo == null || CollectionUtils.isEmpty(ticketProcessDetailInfo.getCouponGroups())) {
			return null;
		}
		
		BaggageHistoryDTO baggageHistory = new BaggageHistoryDTO();
		// segment id list which is associated to the payment ticket
		List<String> ticketAssociatedSegmentIds = passengerSegmentsOfPax
				.stream().filter(ps -> !CollectionUtils.isEmpty(ps.getBaggages())
						// check if there is baggage associated to the payment ticket in baggage list
						&& ps.getBaggages().stream()
								.anyMatch(baggage -> baggage.getPaymentInfo() != null
										&& ticket.equals(baggage.getPaymentInfo().getTicket())))
				.map(RetrievePnrPassengerSegment::getSegmentId).collect(Collectors.toList());

		// segmentIds grouped by journey
		List<List<String>> segmentIdGroups = groupSegmentIdsByJourney(ticketAssociatedSegmentIds, journeySummaryList);
		for (List<String> segmentIds : segmentIdGroups) {
			// get baggage associated to the journey from ticketProcessDetailInfo
			TicketProcessBaggageAllowance ticketProcessBaggageAllowance = getAssociatedBaggage(pnrBooking, ticketProcessDetailInfo, segmentIds);
			if (ticketProcessBaggageAllowance != null && ticketProcessBaggageAllowance.getNumber() != null
					&& ticketProcessBaggageAllowance.getUnit() != null) {
				BaggageInfoDTO baggageInfo = new BaggageInfoDTO();
				baggageInfo.setPassengerId(passengerId);
				baggageInfo.setSegmentIds(segmentIds);
				baggageInfo.setAmount(ticketProcessBaggageAllowance.getNumber());
				baggageInfo.setUnit(ticketProcessBaggageAllowance.getUnit());
				baggageHistory.findBaggageInfos().add(baggageInfo);

				// add the passenger info to responseDTO if it's not added
				addPassengerInfoToResponse(response, pnrBooking, passengerId);
				// add segment info to responseDTO if it's not added
				addSegmentInfosToResponse(response, pnrBooking, segmentIds);
			}
		}

		// set fare info
		baggageHistory.findFare().setAmount(ticketProcessFareInfo.getAmount());
		baggageHistory.findFare().setCurrency(ticketProcessFareInfo.getCurrency());
		// set tax info
		if (ticketProcessTaxInfo != null && ticketProcessTaxInfo.getAmount() != null
				&& !StringUtils.isEmpty(ticketProcessTaxInfo.getCurrency())) {
			baggageHistory.findTax().setAmount(ticketProcessTaxInfo.getAmount());
			baggageHistory.findTax().setCurrency(ticketProcessTaxInfo.getCurrency());
		}
		
		return baggageHistory;
	}

	/**
	 * get baggage info associated to the journey 
	 * 		(since baggage of each segment in one journey should be the same, if there are multiple segments, get baggage of the first segment)
	 * @param pnrBooking
	 * @param ticketProcessDetailInfo
	 * @param segmentIds - segment ids of the journey
	 * @return TicketProcessBaggageAllowance
	 */
	private TicketProcessBaggageAllowance getAssociatedBaggage(RetrievePnrBooking pnrBooking,
			TicketProcessDetailGroup ticketProcessDetailInfo, List<String> segmentIds) {
		if (pnrBooking == null || ticketProcessDetailInfo == null || CollectionUtils.isEmpty(ticketProcessDetailInfo.getCouponGroups())) {
			return null;
		}
		return ticketProcessDetailInfo
				.getCouponGroups().stream()
				// filter the coupon which is associated to the segments
				.filter(couponInfo -> {
					if (CollectionUtils.isEmpty(couponInfo.getFlightInfos()) 
							|| CollectionUtils.isEmpty(couponInfo.getBaggageAllowances())) {
						return false;
					}
					TicketProcessFlightInfo flightInfo = couponInfo.getFlightInfos().get(0);
					for (String segmentId : segmentIds) {
						RetrievePnrSegment segment = findSegmentById(pnrBooking.getSegments(), segmentId);
						if (segmentInfoMatched(flightInfo, segment)) {
							return true;
						}
					}
					return false;
				}).map(couponInfo -> couponInfo.getBaggageAllowances().get(0)).findFirst().orElse(null);
	}
	
	/**
	 * check if segment info is matched
	 * @param flightInfo
	 * @param segment
	 * @return boolean
	 */
	private boolean segmentInfoMatched(TicketProcessFlightInfo flightInfo, RetrievePnrSegment segment) {
		if (flightInfo == null || segment == null || StringUtils.isEmpty(flightInfo.getOffpoint())
				|| StringUtils.isEmpty(flightInfo.getBoardPoint())) {
			return false;
		}
		
		return flightInfo.getBoardPoint().equals(segment.getOriginPort()) && flightInfo.getOffpoint().equals(segment.getDestPort());
	}

	/**
	 * create seat History by payment info
	 * @param pnrBooking
	 * @param response
	 * @param ticket
	 * @param passengerId
	 * @param purchaseProductType
	 * @param ticketProcessFareInfo
	 * @param ticketProcessTaxInfo
	 * @return SeatHistoryDTO
	 */
	private SeatHistoryDTO createSeatHistory(RetrievePnrBooking pnrBooking, PurchaseHistoryResponseDTO response,
			String ticket, String passengerId, PurchaseProductTypeEnum purchaseProductType,
			TicketProcessFareDetail ticketProcessFareInfo, TicketProcessFareDetail ticketProcessTaxInfo) {
		List<RetrievePnrPassengerSegment> passengerSegmentsOfPax = null;
		// find passengerSegments by passengerId, if passengerId is empty or pnrBooking is null or passengerSegments is empty, set value as empty
		if (StringUtils.isEmpty(passengerId)) {
			passengerSegmentsOfPax = Collections.emptyList();
		} else {
			passengerSegmentsOfPax = Optional.ofNullable(pnrBooking).map(RetrievePnrBooking::getPassengerSegments)
					.orElseGet(Collections::emptyList).stream().filter(ps -> passengerId.equals(ps.getPassengerId()))
					.collect(Collectors.toList());
		}
		
		if (response == null || StringUtils.isEmpty(ticket) || purchaseProductType == null
				|| ticketProcessFareInfo == null || CollectionUtils.isEmpty(passengerSegmentsOfPax)) {
			return null;
		}
		
		SeatHistoryDTO seatHistory = new SeatHistoryDTO();
		for (RetrievePnrPassengerSegment ps : passengerSegmentsOfPax) {
			String segmentId = ps.getSegmentId();
			// if the passengerSegment contains seat associated to the payment ticket
			if (ps.getSeat() != null && ps.getSeat().getSeatDetail() != null
					&& ps.getSeat().getSeatDetail().getPaymentInfo() != null
					&& ticket.equals(ps.getSeat().getSeatDetail().getPaymentInfo().getTicket())) {
				SeatDetailDTO seatDetail = new SeatDetailDTO();
				seatDetail.setPassengerId(passengerId);
				seatDetail.setSegmentId(segmentId);
				seatDetail.setSeatType(buildSeatType(purchaseProductType));
				
				seatHistory.findSeats().add(seatDetail);
				
				// add the passenger info to responseDTO if it's not added
				addPassengerInfoToResponse(response, pnrBooking, passengerId);
				// add segment info to responseDTO if it's not added
				addSegmentInfoToResponse(response, pnrBooking, segmentId);
			}
		}
		// set fare info
		seatHistory.findFare().setAmount(ticketProcessFareInfo.getAmount());
		seatHistory.findFare().setCurrency(ticketProcessFareInfo.getCurrency());
		// set tax info
		if (ticketProcessTaxInfo != null && ticketProcessTaxInfo.getAmount() != null && !StringUtils.isEmpty(ticketProcessTaxInfo.getCurrency())) {
			seatHistory.findTax().setAmount(ticketProcessTaxInfo.getAmount());
			seatHistory.findTax().setCurrency(ticketProcessTaxInfo.getCurrency());
		}
		return seatHistory;
	}
	
	private String buildSeatType(PurchaseProductTypeEnum purchaseProductType) {
		if(PurchaseProductTypeEnum.SEAT_EXTRA_LEGROOM.equals(purchaseProductType)) {
			return MMBBizruleConstants.SEAT_TYPE_EXTRA_LEGROOM;
		}
		if(PurchaseProductTypeEnum.SEAT_ASR_REGULAR.equals(purchaseProductType)) {
			return MMBBizruleConstants.SEAT_TYPE_ASR_REGULAR;
		}
		if(PurchaseProductTypeEnum.SEAT_ASR_WINDOW.equals(purchaseProductType)) {
			return MMBBizruleConstants.SEAT_TYPE_ASR_WINDOW;
		}
		if(PurchaseProductTypeEnum.SEAT_ASR_AISLE.equals(purchaseProductType)) {
			return MMBBizruleConstants.SEAT_TYPE_ASR_AISLE;
		}
		return null;
	}

	/**
	 * create lounge history by payment info
	 * 
	 * @param pnrBooking
	 * @param response
	 * @param ticket
	 * @param passengerId
	 * @param purchaseProductType
	 * @param ticketProcessFareInfo
	 * @param ticketProcessTaxInfo
	 * @return LoungeHistoryDTO
	 */
	private LoungeHistoryDTO createLoungeHistory(RetrievePnrBooking pnrBooking, PurchaseHistoryResponseDTO response,
			String ticket, String passengerId, PurchaseProductTypeEnum purchaseProductType,
			TicketProcessFareDetail ticketProcessFareInfo, TicketProcessFareDetail ticketProcessTaxInfo) {
		List<RetrievePnrPassengerSegment> passengerSegmentsOfPax = null;
		// find passengerSegments by passengerId, if passengerId is empty or pnrBooking is null or passengerSegments is empty, set value as empty
		if (StringUtils.isEmpty(passengerId)) {
			passengerSegmentsOfPax = Collections.emptyList();
		} else {
			passengerSegmentsOfPax = Optional.ofNullable(pnrBooking).map(RetrievePnrBooking::getPassengerSegments)
					.orElseGet(Collections::emptyList).stream().filter(ps -> passengerId.equals(ps.getPassengerId()))
					.collect(Collectors.toList());
		}
		
		if (response == null || StringUtils.isEmpty(ticket) || purchaseProductType == null
				|| ticketProcessFareInfo == null || CollectionUtils.isEmpty(passengerSegmentsOfPax)) {
			return null;
		}
		
		LoungeHistoryDTO loungeHistory = new LoungeHistoryDTO();
		for (RetrievePnrPassengerSegment ps : passengerSegmentsOfPax) {
			String segmentId = ps.getSegmentId();
			// if the passengerSegment contains seat associated to the payment ticket
			
			LoungeClass loungeClass;
			if (PurchaseProductTypeEnum.LOUNGE_BUSINESS.equals(purchaseProductType)) {
				loungeClass = LoungeClass.BUSINESS;
			} else {
				loungeClass = LoungeClass.FIRST;
			}
			
			RetrievePnrLoungeInfo pnrLoungeInfo = 
					Optional.ofNullable(ps.getPurchasedLounges()).orElse(Collections.emptyList()).stream().filter(
							loungeInfo -> loungeClass.equals(LoungeClass.parseCode(loungeInfo.getType()))
								&& loungeInfo.getPaymentInfo() != null
								&& ticket.equals(loungeInfo.getPaymentInfo().getTicket())
					).findFirst().orElse(null);
			
			if (pnrLoungeInfo != null && pnrLoungeInfo.getPaymentInfo() != null) {
				
				LoungeInfoDTO loungeInfo = new LoungeInfoDTO();
				loungeInfo.setPassengerId(passengerId);
				loungeInfo.setSegmentId(segmentId);
				loungeInfo.setLoungeType(loungeClass);
				loungeHistory.findLoungeInfos().add(loungeInfo);
				
				// add the passenger info to responseDTO if it's not added
				addPassengerInfoToResponse(response, pnrBooking, passengerId);
				// add segment info to responseDTO if it's not added
				addSegmentInfoToResponse(response, pnrBooking, segmentId);
			}
		}
		// set fare info
		loungeHistory.findFare().setAmount(ticketProcessFareInfo.getAmount());
		loungeHistory.findFare().setCurrency(ticketProcessFareInfo.getCurrency());
		// set tax info
		if (ticketProcessTaxInfo != null && ticketProcessTaxInfo.getAmount() != null && !StringUtils.isEmpty(ticketProcessTaxInfo.getCurrency())) {
			loungeHistory.findTax().setAmount(ticketProcessTaxInfo.getAmount());
			loungeHistory.findTax().setCurrency(ticketProcessTaxInfo.getCurrency());
		}
		return loungeHistory;
	}

	/**
	 * add segmentInfos to response
	 * @param response
	 * @param pnrBooking
	 * @param segmentIds
	 */
	private void addSegmentInfosToResponse(PurchaseHistoryResponseDTO response, RetrievePnrBooking pnrBooking,
			List<String> segmentIds) {
		if (!CollectionUtils.isEmpty(segmentIds)) {
			for (String segmentId : segmentIds) {
				addSegmentInfoToResponse(response, pnrBooking, segmentId);
			}
		}
	}

	/**
	 * group segmentIds by journey
	 * @param ticketAssociatedSegmentIds
	 * @param journeySummaryList
	 * @return
	 */
	private List<List<String>> groupSegmentIdsByJourney(List<String> segmentIds,
			List<JourneySummary> journeySummaryList) {
		List<List<String>> groupResult = new ArrayList<>();
		if (CollectionUtils.isEmpty(segmentIds)) {
			return groupResult;
		} else if(CollectionUtils.isEmpty(journeySummaryList)) {
			groupResult.add(segmentIds);
			return groupResult;
		} else {
			for (JourneySummary journeySummary : journeySummaryList) {
				if (!CollectionUtils.isEmpty(journeySummary.getSegments())) {
					// segmentId list of journeySummary
					List<String> journeySegmentIds = journeySummary.getSegments().stream()
							.filter(seg -> !StringUtils.isEmpty(seg.getSegmentId())).map(JourneySegment::getSegmentId)
							.collect(Collectors.toList());

					// list of segmentId which is in this journey
					List<String> segmentIdsInTheJourney = segmentIds.stream()
							.filter(journeySegmentIds::contains).collect(Collectors.toList());

					if (!CollectionUtils.isEmpty(segmentIdsInTheJourney)) {
						groupResult.add(segmentIdsInTheJourney);
					}
				}
			}
		}
		return groupResult;
	}

	/**
	 * add segmentInfo to response
	 * @param response
	 * @param pnrBooking
	 * @param segmentId
	 */
	private void addSegmentInfoToResponse(PurchaseHistoryResponseDTO response, RetrievePnrBooking pnrBooking,
			String segmentId) {
		if (response == null || pnrBooking == null || CollectionUtils.isEmpty(pnrBooking.getSegments())
				|| StringUtils.isEmpty(segmentId)) {
			return;
		}
		
		// if the segment is already added to response
		boolean segInfoAdded = !CollectionUtils.isEmpty(response.getSegments())
				&& response.getSegments().stream().anyMatch(pax -> segmentId.equals(pax.getSegmentId()));
		
		if (!segInfoAdded) {
			// find segment
			RetrievePnrSegment pnrSegment = findSegmentById(pnrBooking.getSegments(), segmentId);
			if (pnrSegment != null) {
				SegmentInfoDTO segmentInfo = new SegmentInfoDTO();
				segmentInfo.setSegmentId(pnrSegment.getSegmentID());
				segmentInfo.setCompanyId(pnrSegment.getMarketCompany());
				segmentInfo.setSegmentNumber(pnrSegment.getMarketSegmentNumber());
				if (pnrSegment.getDepartureTime() != null
						&& !StringUtils.isEmpty(pnrSegment.getDepartureTime().getPnrTime())) {
					segmentInfo.setDepartureTime(pnrSegment.getDepartureTime().getPnrTime());
				}
				segmentInfo.setOriginPort(pnrSegment.getOriginPort());
				segmentInfo.setDestPort(pnrSegment.getDestPort());
				
				response.findSegments().add(segmentInfo);
			}
		}
		
	}

	/**
	 * add passegnerInfo to Response
	 * @param response
	 * @param pnrBooking
	 * @param passengerId
	 */
	private void addPassengerInfoToResponse(PurchaseHistoryResponseDTO response, RetrievePnrBooking pnrBooking,
			String passengerId) {
		if (response == null || pnrBooking == null || CollectionUtils.isEmpty(pnrBooking.getPassengers())
				|| StringUtils.isEmpty(passengerId)) {
			return;
		}
		
		// if the pax info has been added
		boolean paxInfoAdded = !CollectionUtils.isEmpty(response.getPassengers())
				&& response.getPassengers().stream().anyMatch(pax -> passengerId.equals(pax.getPassengerId()));
		
		if (!paxInfoAdded) {
			// find passenger
			RetrievePnrPassenger pnrPassenger = findPassengerById(pnrBooking.getPassengers(), passengerId);
			if (pnrPassenger != null) {
				PassengerInfoDTO passengerInfoDTO = new PassengerInfoDTO();
				passengerInfoDTO.setFamilyName(pnrPassenger.getFamilyName());
				passengerInfoDTO.setGivenName(pnrPassenger.getGivenName());
				passengerInfoDTO.setPassengerId(passengerId);
				
				response.findPassengers().add(passengerInfoDTO);
			}
		}
	}

	/**
	 * find dailyHistory by productDate
	 * @param response
	 * @param productDate
	 * @return
	 */
	private DailyHistoryDTO findDailyHistoryByDate(PurchaseHistoryResponseDTO response, String productDate) {
		if(response == null || CollectionUtils.isEmpty(response.getPurchaseHistory()) || StringUtils.isEmpty(productDate)) {
			return null;
		}
		String productDateStr = DateUtil.convertDateFormat(productDate, DateUtil.DATE_PATTERN_DDMMYY,
				DateUtil.DATE_PATTERN_YYYY_MM_DD);
		return response.getPurchaseHistory().stream()
				.filter(history -> !StringUtils.isEmpty(history.getPurchaseDate()) && history.getPurchaseDate().equals(productDateStr))
				.findFirst().orElse(null);
	}

	/**
	 * find RetrievePnrPassenger by passengerId
	 * @param list
	 * @param passengerId
	 * @return RetrievePnrPassenger
	 */
	private RetrievePnrPassenger findPassengerById(List<RetrievePnrPassenger> passengers, String passengerId) {
		if (CollectionUtils.isEmpty(passengers) || StringUtils.isEmpty(passengerId)) {
			return null;
		}
		return passengers.stream().filter(pax -> passengerId.equals(pax.getPassengerID())).findFirst().orElse(null);
	}

	/**
	 * find RetrievePnrSegment by segmentId
	 * @param segments
	 * @param segmentId
	 * @return RetrievePnrSegment
	 */
	private RetrievePnrSegment findSegmentById(List<RetrievePnrSegment> segments, String segmentId) {
		if (CollectionUtils.isEmpty(segments) || StringUtils.isEmpty(segmentId)) {
			return null;
		}
		return segments.stream().filter(seg -> segmentId.equals(seg.getSegmentID())).findFirst().orElse(null);
	}
	
}
