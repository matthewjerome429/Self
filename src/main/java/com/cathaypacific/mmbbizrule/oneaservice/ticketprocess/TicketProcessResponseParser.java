package com.cathaypacific.mmbbizrule.oneaservice.ticketprocess;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.baggage.BaggageUnitEnum;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.enums.PurchaseProductTypeEnum;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessBaggageAllowance;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessCouponGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessCouponInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessDetailGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessDocGroup;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessFareDetail;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessFlightInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessOriginIdentification;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessOriginator;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessPaxInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessPricingInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessProductInfo;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessRloc;
import com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model.TicketProcessSysProvider;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.BaggageDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.CouponInformationDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.MessageActionDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.MonetaryInformationDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.ReservationControlInformationDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.TaxDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.TaxTypeI;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.TicketProcessEDocReply;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.TicketProcessEDocReply.DocGroup;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.TicketProcessEDocReply.DocGroup.DocDetailsGroup;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.TicketProcessEDocReply.DocGroup.DocDetailsGroup.CouponGroup;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.TravelProductInformationTypeI29340S;
import com.cathaypacific.oneaconsumer.model.response.tatres_15_2_1a.TravellerInformationTypeI;

public class TicketProcessResponseParser {
	
	private static LogAgent logger = LogAgent.getLogAgent(TicketProcessResponseParser.class);

	private static final String BAGGAGE_ALLOWANCE_QUANTITY_CODE_WEIGHT = "W";
	private static final String BAGGAGE_ALLOWANCE_QUANTITY_CODE_NUMBER = "N";
	private static final String TYPE_QUALIFIER_TOTAL = "T";
	private static final String TEXT_INFO_FREETEXT_SEAT = "SEAT ASSIGNMENT";
	private static final String TEXT_INFO_FREETEXT_SEAT_CHARACTERISTIC_EXL = "CHARACTERISTICS: L";
	private static final String TEXT_INFO_FREETEXT_SEAT_CHARACTERISTIC_ASR = "CHARACTERISTICS: CH";
	private static final String TEXT_INFO_FREETEXT_SEAT_CHARACTERISTIC_WINDOW = "CHARACTERISTICS: W";
	private static final String TEXT_INFO_FREETEXT_SEAT_CHARACTERISTIC_AISLE = "CHARACTERISTICS: A";
	private static final String TEXT_INFO_FREETEXT_BAGGAGE = "PREPAID BAGGAGE";
	private static final String TEXT_INFO_FREETEXT_LOUNGE_BUSINESS = "BUSINESS CLASS LOUNGE ACCESS";
	private static final String TEXT_INFO_FREETEXT_LOUNGE_FIRST = "FIRST CLASS LOUNGE ACCESS";
	
	public TicketProcessInfo paserResponse(TicketProcessEDocReply reply) {
		if (reply == null) {
			return null;
		}

		TicketProcessInfo ticketProcessInfo = new TicketProcessInfo();
		//parser messageFunction
		parserMessageFunction(ticketProcessInfo, reply.getMsgActionDetails());
		// parse infoGroup
		parserInfoGroup(ticketProcessInfo, reply.getDocGroup());
		
		return ticketProcessInfo;
	}

	/**
	 * parse messageFunction
	 * @param ticketProcessInfo
	 * @param msgActionDetails
	 */
	private void parserMessageFunction(TicketProcessInfo ticketProcessInfo,
			MessageActionDetailsTypeI msgActionDetails) {
		if(ticketProcessInfo == null || msgActionDetails == null || msgActionDetails.getMessageFunctionDetails() == null) {
			return;
		}
		
		ticketProcessInfo.setMessageFunction(msgActionDetails.getMessageFunctionDetails().getMessageFunction());
	}

	/**
	 * 
	* @Description parse infoGroup
	* @param ticketProcessInfo
	* @param docGroup
	* @return void
	 */
	private void parserInfoGroup(TicketProcessInfo ticketProcessInfo, List<DocGroup> docGroups) {
		if(ticketProcessInfo == null || CollectionUtils.isEmpty(docGroups)){
			return;
		}
		
		for(TicketProcessEDocReply.DocGroup docGroup : docGroups){
			TicketProcessDocGroup infoGroup = new TicketProcessDocGroup();
			// parse RLOC
			parserRloc(infoGroup, docGroup);			
			// parse originatorId
			parserOriginatorId(infoGroup, docGroup);		
			// parse detail info
			parserDetailInfo(infoGroup, docGroup);
			// parse pax info
			parserPaxInfo(infoGroup, docGroup);
			// parse fare info
			parserFareInfo(infoGroup, docGroup);
			// parse product info
			parserProductInfo(infoGroup, docGroup);		
			// parse tax info
			parserTaxInfo(infoGroup, docGroup);
			// parse sysProvider
			parseSysProvider(infoGroup, docGroup);
			ticketProcessInfo.findDocGroups().add(infoGroup);
		}
		
	}
	
	/**
	 * Parse sysProvider(including booking IATA number)
	 * 
	 * @param infoGroup
	 * @param docGroup
	 */
	private void parseSysProvider(TicketProcessDocGroup infoGroup, DocGroup docGroup) {
		if(docGroup == null || docGroup.getSysProvider() == null || docGroup.getSysProvider().getBookingIataNumber() == null) {
			return;
		}
		
		TicketProcessSysProvider sysProvider = new TicketProcessSysProvider();
		sysProvider.setBookingIataNumber(docGroup.getSysProvider().getBookingIataNumber());
		infoGroup.setSysProvider(sysProvider);
	}

	/**
	 * parse tax info
	 * @param infoGroup
	 * @param docGroup
	 */
	private void parserTaxInfo(TicketProcessDocGroup infoGroup, DocGroup docGroup) {
		if (infoGroup == null || docGroup == null || CollectionUtils.isEmpty(docGroup.getTaxInfo())) {
			return;
		}
	
		// get total tax
		TicketProcessFareDetail taxInfo = getTotalTax(docGroup);
		
		if (taxInfo != null && taxInfo.getAmount() != null && !StringUtils.isEmpty(taxInfo.getCurrency())) {
			infoGroup.setTaxInfo(taxInfo);
		}
		
	}

	/**
	 * populate tax detail from docGroup to taxInfo
	 * @param docGroup
	 * @param taxInfo
	 */
	private TicketProcessFareDetail getTotalTax(DocGroup docGroup) {
		if(docGroup == null || CollectionUtils.isEmpty(docGroup.getTaxInfo())) {
			return null;
		}
		TicketProcessFareDetail taxInfo = new TicketProcessFareDetail();
		for (TaxTypeI ticketProcessTax : docGroup.getTaxInfo()) {
			TicketProcessFareDetail totalTaxOfTaxType = getTotalTaxOfTaxType(ticketProcessTax);
			if (totalTaxOfTaxType == null || totalTaxOfTaxType.getAmount() == null
					|| StringUtils.isEmpty(totalTaxOfTaxType.getCurrency())) {
				continue;
			}
			
			if(StringUtils.isEmpty(taxInfo.getCurrency())) {
				taxInfo.setAmount(totalTaxOfTaxType.getAmount());
				taxInfo.setCurrency(totalTaxOfTaxType.getCurrency());
			} else if (totalTaxOfTaxType.getCurrency().equals(taxInfo.getCurrency())) {
				taxInfo.setAmount(taxInfo.getAmount() == null ? totalTaxOfTaxType.getAmount()
						: taxInfo.getAmount().add(totalTaxOfTaxType.getAmount()));
			}
		}
		return taxInfo;	
	}

	/**
	 * get total tax of TaxTypeI
	 * @param taxInfo
	 * @param ticketProcessTax
	 * @return TicketProcessFareDetail
	 */
	private TicketProcessFareDetail getTotalTaxOfTaxType(TaxTypeI ticketProcessTax) {
		if (ticketProcessTax == null || CollectionUtils.isEmpty(ticketProcessTax.getTaxDetails())) {
			return null;
		}
		TicketProcessFareDetail totalTax = new TicketProcessFareDetail();
		for (TaxDetailsTypeI ticketProcessTaxDetail : ticketProcessTax.getTaxDetails()) {
			BigDecimal amount = null;
			try {
				amount = new BigDecimal(ticketProcessTaxDetail.getRate());
			} catch (Exception e) {
				logger.warn(String.format("Amount string %s cannot be convert to decimal.", ticketProcessTaxDetail.getRate()));
				continue;
			}
			String currency = ticketProcessTaxDetail.getCurrencyCode();
			
			if(StringUtils.isEmpty(currency)) {
				continue;
			}
			
			if(StringUtils.isEmpty(totalTax.getCurrency())) {
				totalTax.setAmount(amount);
				totalTax.setCurrency(currency);
			} else if (currency.equals(totalTax.getCurrency())) {
				totalTax.setAmount(totalTax.getAmount() == null ? amount : totalTax.getAmount().add(amount));
			}
		}
		
		return totalTax;
	}

	/**
	 * parse product info
	 * @param infoGroup
	 * @param docGroup
	 */
	private void parserProductInfo(TicketProcessDocGroup infoGroup, DocGroup docGroup) {
		if(infoGroup == null || docGroup == null || docGroup.getProductInfo() == null || docGroup.getProductInfo().getProductDateTimeDetails() == null) {
			return;
		}
		
		TicketProcessProductInfo productInfo = new TicketProcessProductInfo();
		productInfo.setProductDate(docGroup.getProductInfo().getProductDateTimeDetails().getDepartureDate());
		infoGroup.setProductInfo(productInfo);
	}

	/**
	 * parse fare info
	 * @param infoGroup
	 * @param docGroup
	 */
	private void parserFareInfo(TicketProcessDocGroup infoGroup, DocGroup docGroup) {
		if (infoGroup == null || docGroup == null || docGroup.getFareInfo() == null
				|| CollectionUtils.isEmpty(docGroup.getFareInfo().getMonetaryDetails())) {
			return;
		}
		
		List<MonetaryInformationDetailsTypeI> monetaryDetails = docGroup.getFareInfo().getMonetaryDetails();
		
		MonetaryInformationDetailsTypeI monetaryDetail = monetaryDetails
				.stream().filter(detail -> TYPE_QUALIFIER_TOTAL.equals(detail.getTypeQualifier())
						&& detail.getAmount() != null && !StringUtils.isEmpty(detail.getCurrency()))
				.findFirst().orElse(null);
		if(monetaryDetail != null) {
			TicketProcessFareDetail fareInfo = new TicketProcessFareDetail();
			BigDecimal amount;
			// if the amount String can't be converted to decimal, log and return  
			try {
				amount = new BigDecimal(monetaryDetail.getAmount());
			} catch (Exception e) {
				logger.warn(String.format("Amount string %s cannot be convert to decimal.", monetaryDetail.getAmount()));
				return;
			}
			fareInfo.setAmount(amount);
			fareInfo.setCurrency(monetaryDetail.getCurrency());
			infoGroup.setFareInfo(fareInfo);
		}
	}

	/**
	 * parse pax info
	 * @param infoGroup
	 * @param docGroup
	 */
	private void parserPaxInfo(TicketProcessDocGroup infoGroup, DocGroup docGroup) {
		if(infoGroup == null || docGroup == null || docGroup.getPaxInfo() == null){
			return;
		}
		TravellerInformationTypeI ticketProcessPaxInfo = docGroup.getPaxInfo();
		TicketProcessPaxInfo paxInfo = new TicketProcessPaxInfo();
		if(ticketProcessPaxInfo.getPaxDetails() != null) {
			paxInfo.setFamilyName(ticketProcessPaxInfo.getPaxDetails().getSurname());
		}
		
		if (!CollectionUtils.isEmpty(ticketProcessPaxInfo.getOtherPaxDetails())) {
			paxInfo.setGivenName(ticketProcessPaxInfo.getOtherPaxDetails().stream()
					.filter(detail -> detail != null && !StringUtils.isEmpty(detail.getGivenName()))
					.map(detail -> detail.getGivenName()).findFirst().orElse(null));
		}
		
		infoGroup.setPaxInfo(paxInfo);
	}

	/**
	 * 
	* @Description parse detailInfo
	* @param infoGroup
	* @param docGroup
	* @return void
	* @author haiwei.jia
	 */
	private void parserDetailInfo(TicketProcessDocGroup infoGroup, DocGroup docGroup) {
		if(infoGroup == null || CollectionUtils.isEmpty(docGroup.getDocDetailsGroup())){
			return;
		}
		
		for(DocDetailsGroup docDetailsGroup : docGroup.getDocDetailsGroup()){
			TicketProcessDetailGroup detailInfo = new TicketProcessDetailGroup();
			// parse e-ticket
			parserEticket(detailInfo, docDetailsGroup);
			
			// parse flightInfo
			parserCouponGroup(detailInfo, docDetailsGroup);
			
			infoGroup.findDetailInfos().add(detailInfo);
		}
	}
	
	/**
	 * 
	* @Description parse originatorId
	* @param docGroup
	* @param infoGroup
	* @return void
	* @author kent.cheung
	 */
	private void parserOriginatorId(TicketProcessDocGroup infoGroup, DocGroup docGroup) {
		if (infoGroup == null || docGroup == null 
				|| docGroup.getOriginatorInfo() == null
				|| docGroup.getOriginatorInfo().getOriginIdentification() == null
				|| StringUtils.isEmpty(docGroup.getOriginatorInfo().getOriginIdentification().getOriginatorId())){
			return;
		}
		
		TicketProcessOriginator originatorInfo = new TicketProcessOriginator();
		TicketProcessOriginIdentification originIdentification = new TicketProcessOriginIdentification();
		
		originIdentification.setOriginatorId(docGroup.getOriginatorInfo().getOriginIdentification().getOriginatorId());
		originatorInfo.setOriginIdentification(originIdentification);
		
		
		infoGroup.setOriginatorInfo(originatorInfo);
	}

	
	/**
	 * 
	* @Description parser couponInfo
	* @param detailInfo
	* @param docDetailsGroup
	* @return void
	* @author haiwei.jia
	 */
	private void parserCouponGroup(TicketProcessDetailGroup detailInfo, DocDetailsGroup docDetailsGroup) {
		if(detailInfo == null || docDetailsGroup == null || CollectionUtils.isEmpty(docDetailsGroup.getCouponGroup())){
			return;
		}
		
		for(CouponGroup couponGroup : docDetailsGroup.getCouponGroup()){
			TicketProcessCouponGroup tpCouponGroup = new TicketProcessCouponGroup();
			// parse flight info
			parserFlightInfo(tpCouponGroup, couponGroup);
			// parse baggage allowance
			parserBaggageAllowance(tpCouponGroup, couponGroup);
			// parse purchase product type
			parserPurchaseProductType(tpCouponGroup, couponGroup);
			// parse coupon informations
			parseCouponInfos(tpCouponGroup, couponGroup);
			
			// parse pricing information
			parsePricingInfo(tpCouponGroup, couponGroup);
			
			detailInfo.findCouponGroups().add(tpCouponGroup);
		}
	}

	/**
	 * Parse pricing information
	 * 
	 * @param tpCouponGroup
	 * @param couponGroup
	 */
	private void parsePricingInfo(TicketProcessCouponGroup tpCouponGroup, CouponGroup couponGroup) {
		if(couponGroup == null || couponGroup.getPricingInfo() == null
				|| couponGroup.getPricingInfo().getFareBasisDetails() == null
				|| StringUtils.isEmpty(couponGroup.getPricingInfo().getFareBasisDetails().getRateTariffClass())) {
			return;
		}
		
		TicketProcessPricingInfo tpPricingInfo = new TicketProcessPricingInfo();
		tpPricingInfo.setRateTariffClass(couponGroup.getPricingInfo().getFareBasisDetails().getRateTariffClass());
		tpCouponGroup.setPricingInfo(tpPricingInfo);
	}

	/**
	 * Parse coupon informations
	 * 
	 * @param tpCouponGroup
	 * @param couponGroup
	 */
	private void parseCouponInfos(TicketProcessCouponGroup tpCouponGroup, CouponGroup couponGroup) {
		if(tpCouponGroup == null || couponGroup == null) {
			return;
		}
		List<TicketProcessCouponInfo> couponInfos = tpCouponGroup.findCouponInfos();
		List<CouponInformationDetailsTypeI> couponDetails = couponGroup.getCouponInfo() == null ? Collections.emptyList() : couponGroup.getCouponInfo().getCouponDetails();
		for(CouponInformationDetailsTypeI couponDetail : couponDetails) {
			TicketProcessCouponInfo couponInfo = new TicketProcessCouponInfo();
			couponInfo.setCpnNumber(couponDetail.getCpnNumber());
			couponInfo.setCpnStatus(couponDetail.getCpnStatus());
			couponInfos.add(couponInfo);
		}
	}

	/**
	 * parse purchase product type
	 * @param couponInfo
	 * @param couponGroup
	 */
	private void parserPurchaseProductType(TicketProcessCouponGroup couponInfo, CouponGroup couponGroup) {
		if (couponInfo == null || couponGroup == null || CollectionUtils.isEmpty(couponGroup.getTextInfo())) {
			return;
		}
		
		// get all freeTexts in textInfo
		List<String> allFreeTexts = couponGroup.getTextInfo().stream()
				.filter(textInfo -> !CollectionUtils.isEmpty(textInfo.getFreeText()))
				.map(textInfo -> textInfo.getFreeText()).reduce(new ArrayList<String>(), (all, item) -> {
					all.addAll(item);
					return all;
				});
		
		if (allFreeTexts.stream().anyMatch(TEXT_INFO_FREETEXT_BAGGAGE::equals)) {
			couponInfo.setPurchaseProductType(PurchaseProductTypeEnum.BAGGAGE);
		} else if (allFreeTexts.stream().anyMatch(TEXT_INFO_FREETEXT_SEAT::equals)) {
			if (allFreeTexts.stream()
					.anyMatch(TEXT_INFO_FREETEXT_SEAT_CHARACTERISTIC_EXL::equals)) {
				couponInfo.setPurchaseProductType(PurchaseProductTypeEnum.SEAT_EXTRA_LEGROOM);
			} else if (allFreeTexts.stream()
					.anyMatch(TEXT_INFO_FREETEXT_SEAT_CHARACTERISTIC_ASR::equals)) {
				couponInfo.setPurchaseProductType(PurchaseProductTypeEnum.SEAT_ASR_REGULAR);
			} else if (allFreeTexts.stream()
					.anyMatch(TEXT_INFO_FREETEXT_SEAT_CHARACTERISTIC_WINDOW::equals)) {
				couponInfo.setPurchaseProductType(PurchaseProductTypeEnum.SEAT_ASR_WINDOW);
			} else if (allFreeTexts.stream()
					.anyMatch(TEXT_INFO_FREETEXT_SEAT_CHARACTERISTIC_AISLE::equals)) {
				couponInfo.setPurchaseProductType(PurchaseProductTypeEnum.SEAT_ASR_AISLE);
			}
		} else if (allFreeTexts.stream().anyMatch(TEXT_INFO_FREETEXT_LOUNGE_BUSINESS::equals)) {
			couponInfo.setPurchaseProductType(PurchaseProductTypeEnum.LOUNGE_BUSINESS);
		} else if (allFreeTexts.stream().anyMatch(TEXT_INFO_FREETEXT_LOUNGE_FIRST::equals)) {
			couponInfo.setPurchaseProductType(PurchaseProductTypeEnum.LOUNGE_FIRST);
		}
	}

	/**
	 * 
	* @Description parse baggage allowance
	* @param couponInfo
	* @param couponGroup
	* @return void
	* @author haiwei.jia
	 */
	private void parserBaggageAllowance(TicketProcessCouponGroup couponInfo, CouponGroup couponGroup) {
		if (couponInfo == null || couponGroup == null || couponGroup.getBaggageInfo() == null
				|| CollectionUtils.isEmpty(couponGroup.getBaggageInfo().getBaggageDetails())) {
			return;
		}
		
		for(BaggageDetailsTypeI baggageDetail : couponGroup.getBaggageInfo().getBaggageDetails()){
			TicketProcessBaggageAllowance baggageAllowance = getBaggageAllowance(baggageDetail);
			if(baggageAllowance != null){
				couponInfo.findBaggageAllowances().add(baggageAllowance);
			}
		}
	}

	/**
	 * 
	* @Description get baggage allowance
	* @param baggageDetail
	* @return TicketProcessBaggageAllowance
	* @author haiwei.jia
	 */
	private TicketProcessBaggageAllowance getBaggageAllowance(BaggageDetailsTypeI baggageDetail) {
		if(baggageDetail == null){
			return null;
		}
		
		TicketProcessBaggageAllowance baggageAllowance = new TicketProcessBaggageAllowance();
		baggageAllowance.setNumber(baggageDetail.getFreeAllowance());
		if(BAGGAGE_ALLOWANCE_QUANTITY_CODE_NUMBER.equals(baggageDetail.getQuantityCode())){
			baggageAllowance.setUnit(BaggageUnitEnum.BAGGAGE_PIECE_UNIT);
			return baggageAllowance;
		} else if(BAGGAGE_ALLOWANCE_QUANTITY_CODE_WEIGHT.equals(baggageDetail.getQuantityCode())){
			baggageAllowance.setUnit(BaggageUnitEnum.BAGGAGE_WEIGHT_UNIT);
			return baggageAllowance;
		} else{
			return null;
		}
	}

	/**
	 * 
	* @Description parser flight info
	* @param couponInfo
	* @param couponGroup
	* @return void
	* @author haiwei.jia
	 */
	private void parserFlightInfo(TicketProcessCouponGroup couponInfo, CouponGroup couponGroup) {
		if(couponInfo == null || couponGroup == null || CollectionUtils.isEmpty(couponGroup.getLeg())){
			return;
		}
		
		for(TravelProductInformationTypeI29340S leg : couponGroup.getLeg()){
			TicketProcessFlightInfo flightInfo = getFlightInfo(leg);
			if(flightInfo != null){
				couponInfo.findFlightInfos().add(flightInfo);
			}
		}
	}
	
	/**
	 * 
	* @Description get flight info from leg
	* @param leg
	* @return TicketProcessFlightBasicInfo
	* @author haiwei.jia
	 */
	private TicketProcessFlightInfo getFlightInfo(TravelProductInformationTypeI29340S leg) {
		if(leg == null){
			return null;
		}
		
		TicketProcessFlightInfo flightInfo = new TicketProcessFlightInfo();
		
		// flight date
		if(leg.getFlightDate() != null){
			flightInfo.findFlightDate().setDepartureDate(leg.getFlightDate().getDepartureDate());
			flightInfo.findFlightDate().setDepartureTime(leg.getFlightDate().getDepartureTime());
			flightInfo.findFlightDate().setArrivalDate(leg.getFlightDate().getArrivalDate());
			flightInfo.findFlightDate().setArrivalTime(leg.getFlightDate().getArrivalTime());
		}
		
		// board point
		if(leg.getBoardPointDetails() != null){
			flightInfo.setBoardPoint(leg.getBoardPointDetails().getTrueLocationId());
		}
		
		// off point
		if(leg.getOffpointDetails() != null){
			flightInfo.setOffpoint(leg.getOffpointDetails().getTrueLocationId());
		}
		
		// company details
		if(leg.getCompanyDetails() != null){
			flightInfo.setMarketingCompany(leg.getCompanyDetails().getMarketingCompany());
			flightInfo.setOperatingCompany(leg.getCompanyDetails().getOperatingCompany());
		}
		// booking class and flight number
		if(leg.getFlightIdentification() != null){
			flightInfo.setBookingClass(leg.getFlightIdentification().getBookingClass());
			flightInfo.setFlightNumber(leg.getFlightIdentification().getFlightNumber());
		}
		
		return flightInfo;
	}

	/**
	 * 
	* @Description parse e-ticket
	* @param detailInfo
	* @param docDetailsGroup
	* @return void
	* @author haiwei.jia
	 */
	private void parserEticket(TicketProcessDetailGroup detailInfo, DocDetailsGroup docDetailsGroup) {
		if (detailInfo == null || docDetailsGroup == null 
				|| docDetailsGroup.getDocInfo() == null
				|| docDetailsGroup.getDocInfo().getDocumentDetails() == null
				|| StringUtils.isEmpty(docDetailsGroup.getDocInfo().getDocumentDetails().getNumber())){
			return;
		}
			
		detailInfo.setEticket(docDetailsGroup.getDocInfo().getDocumentDetails().getNumber());
	}

	/**
	 * 
	* @Description parse RLOC
	* @param docGroup
	* @param infoGroup
	* @return void
	* @author haiwei.jia
	 */
	private void parserRloc(TicketProcessDocGroup infoGroup, TicketProcessEDocReply.DocGroup docGroup) {
		if (infoGroup == null || docGroup.getReferenceInfo() == null
				|| CollectionUtils.isEmpty(docGroup.getReferenceInfo().getReservation())) {
			return;
		}
		
		//get RLOC from each reservation
		for(ReservationControlInformationDetailsTypeI reservation : docGroup.getReferenceInfo().getReservation()){
			if(reservation != null){
				TicketProcessRloc rloc = new TicketProcessRloc();
				rloc.setCompanyId(reservation.getCompanyId());
				rloc.setControlNumber(reservation.getControlNumber());
				infoGroup.findRlocs().add(rloc);
			}
		}
	}
	
	/**
	 * Check if any error in reply
	 * 
	 * @param reply
	 * @throws UnexpectedException
	 */
	public static void checkTicketProcessEDocReplyErrors(TicketProcessEDocReply reply) throws UnexpectedException {
		if(reply.getError() == null) {
			return;
		}
		
		String errorCode = StringUtils.EMPTY;
		if(reply.getError().getErrorDetails() != null) {
			errorCode = reply.getError().getErrorDetails().getErrorCode();
		}
		
		String textInfo = reply.getTextInfo().stream().filter(t -> CollectionUtils.isNotEmpty(t.getFreeText())).map(t -> t.getFreeText().get(0)).findFirst().orElse(StringUtils.EMPTY);
		throw new UnexpectedException(String.format("1A checkTicketProcessEDocReply error found, errorCode:[%s], textInfo:[%s]", errorCode, textInfo),
				new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
	}

}
