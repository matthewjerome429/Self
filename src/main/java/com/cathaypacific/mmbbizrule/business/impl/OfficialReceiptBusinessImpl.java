package com.cathaypacific.mmbbizrule.business.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.aem.model.City;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.business.OfficialReceiptBusiness;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.db.dao.CabinClassDAO;
import com.cathaypacific.mmbbizrule.db.dao.ConstantDataDAO;
import com.cathaypacific.mmbbizrule.db.dao.PaxNameSequenceDAO;
import com.cathaypacific.mmbbizrule.db.dao.TbCardPaymentMappingDAO;
import com.cathaypacific.mmbbizrule.db.dao.TbOfficialReceiptDownloadHistoryDAO;
import com.cathaypacific.mmbbizrule.db.model.ConstantData;
import com.cathaypacific.mmbbizrule.db.model.PaxNameSequence;
import com.cathaypacific.mmbbizrule.db.model.TbCardPaymentMapping;
import com.cathaypacific.mmbbizrule.db.model.TbOfficialReceiptDownloadHistory;
import com.cathaypacific.mmbbizrule.dto.request.oneaoperation.AddRmDetail;
import com.cathaypacific.mmbbizrule.dto.response.officialreceipt.OfficialReceiptPassengerDTO;
import com.cathaypacific.mmbbizrule.dto.response.officialreceipt.OfficialReceiptResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.ETicket;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.oneaservice.addbooking.AddBookingBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEticket;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRemark;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.util.FreeTextUtil;
import com.cathaypacific.mmbbizrule.oneaservice.pnraddelement.sevice.AddPnrElementsInvokeService;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.cathaypacific.mmbbizrule.util.PDFUtil;
import com.google.gson.Gson;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

@Service
public class OfficialReceiptBusinessImpl implements OfficialReceiptBusiness {
	
	private static LogAgent logger = LogAgent.getLogAgent(OfficialReceiptBusinessImpl.class);
	private static final String CPR_PASSENGER_ID_PREFIX = "P";
	
	@Value("${officialreceipt.flight.flown.limithours}")
	private Integer limithours;
	
	@Autowired
	private BizRuleConfig bizRuleConfig;
	
	@Autowired
	private AEMService aemService;
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Autowired
	private BookingBuildService bookingBuildService;
	
	@Autowired
	private PaxNameIdentificationService paxNameIdentificationService;
	
	@Autowired
	private CabinClassDAO cabinClassDAO;
	
	@Autowired
	private ConstantDataDAO constantDataDAO;
	
	@Autowired
	private PaxNameSequenceDAO paxNameSequenceDAO;

	@Autowired
	private TbCardPaymentMappingDAO tbCardPaymentMappingDAO;
	
	@Autowired
	private TbOfficialReceiptDownloadHistoryDAO tbOfficialReceiptDownloadHistoryDAO;
	
	@Autowired
	private AddPnrElementsInvokeService addPnrElementsInvokeService;
	
	@Override
	public OfficialReceiptResponseDTO retrievePassengers(String oneARloc, LoginInfo loginInfo) throws BusinessBaseException {
		// retrieve pnrBooking
		RetrievePnrBooking pnrBooking = pnrInvokeService.retrievePnrByRloc(oneARloc);
		
		// check booking loginInfo
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
		
		// build booking 
		Booking booking = bookingBuildService.buildBooking(pnrBooking, loginInfo, buildBookingRequired());
		
		OfficialReceiptResponseDTO response = new OfficialReceiptResponseDTO();
		
		// Booking contains BCode or not
		boolean hasBCode = BookingBuildUtil.bcodeExist(pnrBooking.getSsrList());
		
		/** 
		 * check the booking is eligible to download official receipt 
		 * if booking is in-eligible, return all passenger informations with eligible flag=false.
		 * if booking is eligible, check and build passengers.
		 * */
		if(!checkBookingOfficialReceiptEligibility(booking, hasBCode)) {
			response.setPassengers(buildInEligibilityPassengers(booking.getPassengers(), pnrBooking, loginInfo));
		} else {
			response.setPassengers(checkAndBuildPassengers(booking, pnrBooking, loginInfo));
		}
		
		response.setHasBCode(hasBCode);
		return response;
	}

	@Override
	public OutputStream generateOfficialReceiptPDF(RetrievePnrBooking pnrBooking, String passengerId, LoginInfo loginInfo,
			Locale locale) throws BusinessBaseException, IOException {
		
		// check booking loginInfo
		paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
		
		// build booking 
		Booking booking = bookingBuildService.buildBooking(pnrBooking, loginInfo, buildBookingRequired());
		
		// Check the passenger can download the official receipt in this booking or not
		checkOfficialReceiptDownload(pnrBooking, passengerId, booking);
		
		ETicket eticket = getValidEticketByPassegnerId(booking, passengerId);
		if(eticket == null) {
        	throw new ExpectedException(String.format("download official receipt failure => passegner[%s] can't find any tickets in booking[%s]",
        			passengerId, booking.getOneARloc()), new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
        }
		
		// Get fare data
		String[] fareData = retrieveSameFareData(pnrBooking, booking, passengerId);
		String currency = fareData[0];
		String amount = fareData[1];
		
		/** Save into download history DB */
		TbOfficialReceiptDownloadHistory historyRecord = saveDownloadHistory(
			eticket, passengerId, loginInfo, pnrBooking, booking, currency, amount
		);
		
		String pdfReferenceNo = TBConstants.TB_OFFICIAL_RECEIPT_DOWNLOAD_REFERENCE_PREFIX + historyRecord.getId();
		
		/** add RM into 1A */
		saveDownloadRecordToOneA(pnrBooking, passengerId, pdfReferenceNo);
		
		// OLSSMMB-16763: Don't remove me!!!
		// --- begin ---
		logger.info(String.format("Official Receipt | PDF Export | Rloc | %s | Passenger ID | %s", booking.getDisplayRloc(), passengerId), true);
		// --- end ---

		return generatePDF(passengerId, booking, pnrBooking, eticket, pdfReferenceNo, locale, currency, amount);
	}
	
	@Override
	public String generateOfficialReceiptFileName(RetrievePnrBooking pnrBooking, String passengerId, Locale locale) throws BusinessBaseException {
		/**
		 * fileName format: Official receipt_<RLOC>_<Pax name>
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("Official receipt_");
		sb.append(pnrBooking.getOneARloc());
		sb.append("_");
		
		RetrievePnrPassenger passenger = pnrBooking.getPassengers().stream()
				.filter(pax -> pax != null && passengerId.equals(pax.getPassengerID())).findFirst().orElse(null);
		if(passenger == null) {
			throw new ExpectedException(String.format("download official receipt failure => passenger[%s] can't be found in booking[%s] ", 
					passengerId, pnrBooking.getOneARloc()), new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		
		PaxNameSequence paxNameSequence = paxNameSequenceDAO.findPaxNameSequenceByAppCodeAndLocale(TBConstants.APP_CODE, locale.getCountry().toUpperCase());
		if(paxNameSequence == null || paxNameSequence.getFamilyNameSequence() == 1) {
			sb.append(passenger.getFamilyName()).append(" ").append(passenger.getGivenName());			
		} else {
			sb.append(passenger.getGivenName()).append(" ").append(passenger.getFamilyName());
		}
		
		return sb.toString();
	}
	

	/**
	 * Save Official Receipt download record into 1A(add RM)
	 * 
	 * @param pnrBooking
	 * @param passengerId
	 * @param pdfReferenceNo
	 */
	private void saveDownloadRecordToOneA(RetrievePnrBooking pnrBooking, String passengerId, String pdfReferenceNo) {
		if(pnrBooking == null || CollectionUtils.isEmpty(pnrBooking.getPassengers())) {
			logger.info(String.format("download official receipt, save download RM failure => booking[%s] is null, or have no passenger in booking, request passenger[%s]", pnrBooking == null ? null : pnrBooking.getOneARloc(), passengerId));
			return;
		}
		
		RetrievePnrPassenger passenger = pnrBooking.getPassengers().stream().filter(pax -> pax != null && pax.getPassengerID().equals(passengerId)).findFirst().orElse(null);
		if(passenger == null) {
			logger.info(String.format("download official receipt, save download RM failure => passenger[%s] can't be found in booking[%s]", passengerId, pnrBooking.getOneARloc()));
			return;
		}
		
		String nonInfantRM = "MMB Export official receipt REF " + pdfReferenceNo;
		String infantRM = "MMB Export official receipt for infant " + passenger.getGivenName() + passenger.getFamilyName() + " REF " + pdfReferenceNo;
		
		String remark = nonInfantRM;
		String passengerIdInPNR = passenger.getPassengerID();
		String paxType = passenger.getPassengerType();
		if(PnrResponseParser.PASSENGER_TYPE_INF.equals(paxType)) {
			remark = infantRM;
			passengerIdInPNR = passenger.getParentId();
		}
		
		boolean rmExist = checkAlreadyDownLoaded(pnrBooking.getRemarkList(), passengerIdInPNR, remark, pnrBooking.getPassengers().size());
		if(rmExist) {
			logger.info(String.format("download official receipt, no need to save download RM => passenger[%s] already downloaded official receipt before in booking[%s]", passengerId, pnrBooking.getOneARloc()));
			return;
		}
		
		try {
			addDownloadRM(pnrBooking.getOneARloc(), passengerIdInPNR, remark);			
		} catch(Exception e) {
			logger.error(String.format("download official receipt, save download RM failure => passenger[%s] in booking[%s]", passengerId, pnrBooking.getOneARloc()), e);
		}
		
	}

	/**
	 * Add download official receipt record
	 * 
	 * @param rloc
	 * @param passengerId
	 * @param freeText
	 * @throws BusinessBaseException
	 */
	private void addDownloadRM(String rloc, String passengerId, String freeText) throws BusinessBaseException {
		List<AddRmDetail> addRmDetails = new ArrayList<>();
		AddRmDetail addRmDetail = new AddRmDetail();
		addRmDetail.setFreeText(freeText);
		addRmDetail.setPassegnerIds(Arrays.asList(passengerId));
		addRmDetails.add(addRmDetail);
		
		AddBookingBuilder builder = new AddBookingBuilder();
		addPnrElementsInvokeService.addMutiElements(builder.buildRmRequest(rloc, addRmDetails), null);
	}

	/**
	 * check passenger already downloaded the official receipt by RM
	 * 
	 * @param remarks
	 * @param passengerIdInPNR
	 * @param remark
	 * @param passengerCount
	 * @return
	 */
	private boolean checkAlreadyDownLoaded(List<RetrievePnrRemark> remarks, String passengerIdInPNR, String remark, int passengerCount) {
		if(CollectionUtils.isEmpty(remarks)) {
			return false;
		}
		
		List<RetrievePnrRemark> downloadRecordRMs = remarks.stream().filter(rm -> rm != null && remark.equalsIgnoreCase(rm.getFreeText())).collect(Collectors.toList());
		
		if(CollectionUtils.isEmpty(downloadRecordRMs)) {
			return false;
		}
		
		if(passengerCount == 1) {
			return downloadRecordRMs.stream().anyMatch(rm -> CollectionUtils.isEmpty(rm.getPassengerIds()));
		} else {
			return downloadRecordRMs.stream().anyMatch(rm -> CollectionUtils.isNotEmpty(rm.getPassengerIds()) && rm.getPassengerIds().contains(passengerIdInPNR));
		}
		
	}

	/**
	 * Generate PDF
	 * 
	 * @param passengerId
	 * @param booking
	 * @param pnrBooking 
	 * @param eticket
	 * @param pdfReferenceNo
	 * @param locale 
	 * @return
	 * @throws IOException
	 */
	private OutputStream generatePDF(String passengerId, Booking booking, RetrievePnrBooking pnrBooking,
			ETicket eticket, String pdfReferenceNo, Locale locale,
			String currency, String amount) throws IOException {
		
		ClassPathResource classPathResource = new ClassPathResource(MMBBizruleConstants.OFFICIAL_RECEIPT_PDF_TEMPLATE_PATH);
        InputStream pdfTemplate = classPathResource.getInputStream();

        /** Build PDF data model */
 		Map<String, Object> pdfDataMap = new HashMap<>();
 		pdfDataMap.put("dateOfIssue", buildDateOfIssue(locale));
 		pdfDataMap.put("referenceNumber", pdfReferenceNo);
 		pdfDataMap.put("ticketNumber", eticket.getNumber());
 		pdfDataMap.put("passengerName", buildPassengerName(booking.getPassengers(), passengerId, locale));
 		pdfDataMap.put("itinerary", buildPassengerItinerary(booking, passengerId));
 		pdfDataMap.put("classOfService", buildClassOfService(booking, passengerId));
 		pdfDataMap.put("dateOfDocumentIssue", buildEticketIssuanceDate(eticket));
 		pdfDataMap.put("formOfPayment", buildFormOfPayment(pnrBooking, passengerId));
 		pdfDataMap.put("totalAmountPaid", buildTotalAmountPaid(currency, amount));
 		pdfDataMap.put("remarks", "Air ticket");
 		
 		logger.info(String.format("Passenger[%s] download booking[%s] official receipt PDF value:[%s]", passengerId, 
				booking.getOneARloc(), new Gson().toJson(pdfDataMap)));
		
		OutputStream pdf = PDFUtil.fillInPdfTemplate(pdfTemplate, pdfDataMap);

		// Close
		pdfTemplate.close();
		return pdf;
	}

	/**
	 * Build classOfService
	 * 
	 * @param booking
	 * @param passengerId
	 * @return
	 */
	private String buildClassOfService(Booking booking, String passengerId) {
		if(booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments())) {
			return StringUtils.EMPTY;
		}
		
		List<String> fareRateTariffClasses = booking.getPassengerSegments().stream()
				.filter(ps -> ps.getPassengerId().equals(passengerId) && ps.getEticket() != null && StringUtils.isNotEmpty(ps.getEticket().getFareRateTariffClass()))
				.map(PassengerSegment::getEticket)
				.map(ETicket::getFareRateTariffClass)
				.distinct()
				.collect(Collectors.toList());
		
		List<String> subClasses = fareRateTariffClasses.stream().map(s -> s.substring(0, 1)).distinct().collect(Collectors.toList());
		
		List<String> cabinDescriptions = new ArrayList<>();
		for(String subClass : subClasses) {
			cabinDescriptions.addAll(cabinClassDAO.findDescriptionBySubclass(TBConstants.APP_CODE, subClass));
		}
		
		return StringUtils.join(cabinDescriptions.stream().distinct().collect(Collectors.toList()), " and ");
	}

	/**
	 * Build form of payment
	 * 
	 * @param pnrBooking
	 * @param passengerId
	 * @return
	 */
	private String buildFormOfPayment(RetrievePnrBooking pnrBooking, String passengerId) {
		RetrievePnrPassenger passegner = pnrBooking.getPassengers().stream().filter(pax -> passengerId.equals(pax.getPassengerID())).findFirst().orElse(null);
		if(passegner == null || CollectionUtils.isEmpty(passegner.getFpLongFreetexts())) {
			return StringUtils.EMPTY;
		}
		
		String fopCode = null;
		String cardNumber = null;
		String expiryDate = null;
		
		String freeText = passegner.getFpLongFreetexts().get(0);
		if(StringUtils.contains(freeText, OneAConstants.FP_FOP_PREFIX_CC)) {
			String[] fpStoredCardInfo = FreeTextUtil.getFpNoStoredCardInfo(freeText);
			if(fpStoredCardInfo != null) {
				fopCode = fpStoredCardInfo[1];
				cardNumber = fpStoredCardInfo[2];
				expiryDate = fpStoredCardInfo[3];
			}			
		} else if(StringUtils.contains(freeText, OneAConstants.FP_FOP_PREFIX_IBE)) {
			String[] fpStoredCardInfo = FreeTextUtil.getFpStoredCardInfo(freeText);
			if(fpStoredCardInfo != null) {
				fopCode = fpStoredCardInfo[1];
				cardNumber = fpStoredCardInfo[3];
			}
		} else if(StringUtils.contains(freeText, OneAConstants.FP_FOP_CASH)) {
			fopCode = OneAConstants.FP_FOP_CASH;
		} else if(StringUtils.contains(freeText, OneAConstants.FP_FOP_CHQ)) {
			fopCode = OneAConstants.FP_FOP_CHQ;
		}
		
		return buildFormOfPayment(fopCode, cardNumber, expiryDate);
	}

	/**
	 * Build form of payment
	 * Format: <Payment type>: <Card # (if available - keep masked format)> expired at <Card expiry date (if available)>
	 * 
	 * @param fopPrefix
	 * @param cardNumber
	 * @param expiryDate
	 * @return
	 */
	private String buildFormOfPayment(String fopPrefix, String cardNumber, String expiryDate) {
		if(StringUtils.isEmpty(fopPrefix)) {
			return StringUtils.EMPTY;
		}
		
		StringBuilder sb = new StringBuilder();
		
		TbCardPaymentMapping typeMapping = tbCardPaymentMappingDAO.findByAppCodeAndCardType(TBConstants.APP_CODE, fopPrefix);
		sb.append(typeMapping != null && StringUtils.isNotEmpty(typeMapping.getPaymentType()) ? typeMapping.getPaymentType() : fopPrefix);
		
		if(StringUtils.isEmpty(cardNumber)) {
			return sb.toString();
		}
		sb.append(": ").append(cardNumber);
		
		if(StringUtils.isEmpty(expiryDate)) {
			return sb.toString();
		}
		sb.append(" expired at ").append(DateUtil.convertDateFormat(expiryDate, DateUtil.DATE_PATTERN_MM_YY, DateUtil.DATE_PATTERN_MMM_YYYY_WITH_SPACE));
		
		return sb.toString();
	}

	/**
	 * Build Date of issuance: Local date of PDF generation
	 * format: DD MMM YYYY
	 * 
	 * @param locale
	 * @return
	 */
	private String buildDateOfIssue(Locale locale) {
		Calendar cal = Calendar.getInstance(locale);
		return DateUtil.getCal2Str(cal, DateUtil.DATE_PATTERN_DDMMMYYYY_WITH_SPACE);
	}

	/**
	 * Build e-ticket issuance date
	 * format: DD MMM YYYY
	 * 
	 * @param eticket
	 * @return
	 */
	private String buildEticketIssuanceDate(ETicket eticket) {
		if(eticket == null || StringUtils.isEmpty(eticket.getDate())) {
			logger.warn("e-ticket issuance date is empty when downloading official receipt");
			return StringUtils.EMPTY;
		}
		return DateUtil.convertDateFormat(eticket.getDate(), ETicket.TIME_FORMAT, DateUtil.DATE_PATTERN_DDMMMYYYY_WITH_SPACE);
	}

	/**
	 * Build e-ticket Total amount paid
	 * format:<currency><amount>
	 * 
	 * @param eticket
	 * @return
	 */
	private String buildTotalAmountPaid(String currency, String amount) {
		if(StringUtils.isEmpty(currency) || StringUtils.isEmpty(amount)) {
			return StringUtils.EMPTY;
		}
		
		return currency + amount;
	}

	/**
	 * Save download history into DB
	 * 
	 * @param eticket
	 * @param passengerId
	 * @param loginInfo
	 * @param pnrBooking
	 * @param booking 
	 * @return
	 */
	private TbOfficialReceiptDownloadHistory saveDownloadHistory(
			ETicket eticket, String passengerId,
			LoginInfo loginInfo, RetrievePnrBooking pnrBooking, Booking booking,
			String currency, String amount
		) {
		TbOfficialReceiptDownloadHistory tbOfficialReceiptDownloadHistory = new TbOfficialReceiptDownloadHistory();
		
		tbOfficialReceiptDownloadHistory.setReferencePrefix(TBConstants.TB_OFFICIAL_RECEIPT_DOWNLOAD_REFERENCE_PREFIX);
		tbOfficialReceiptDownloadHistory.setAppCode(TBConstants.APP_CODE);
		tbOfficialReceiptDownloadHistory.setLoginType(loginInfo.getLoginType());
		tbOfficialReceiptDownloadHistory.setMemberId(loginInfo.getMemberId());
		tbOfficialReceiptDownloadHistory.setRloc(pnrBooking.getOneARloc());
		tbOfficialReceiptDownloadHistory.setLoginPassengerId(getLoginPassengerId(booking));
		tbOfficialReceiptDownloadHistory.setRequestPassengerId(passengerId);
		tbOfficialReceiptDownloadHistory.setEticket(eticket.getNumber());
		tbOfficialReceiptDownloadHistory.setFpCode(getFpCode(pnrBooking, passengerId));
		tbOfficialReceiptDownloadHistory.setAmount(amount);
		tbOfficialReceiptDownloadHistory.setCurrency(currency);
		
		logger.info(String.format("Passenger[%s] download booking[%s] official receipt history info:[%s]", passengerId, 
				booking.getOneARloc(), new Gson().toJson(tbOfficialReceiptDownloadHistory)));
		
		return tbOfficialReceiptDownloadHistoryDAO.save(tbOfficialReceiptDownloadHistory);
	}

	/**
	 * Get FP code from FP longFreeText
	 * 
	 * @param pnrBooking
	 * @param passengerId
	 * @return
	 */
	private String getFpCode(RetrievePnrBooking pnrBooking, String passengerId) {
		RetrievePnrPassenger passegner = pnrBooking.getPassengers().stream().filter(pax -> passengerId.equals(pax.getPassengerID())).findFirst().orElse(null);
		if(passegner == null || CollectionUtils.isEmpty(passegner.getFpLongFreetexts())) {
			return StringUtils.EMPTY;
		}
		
		String fpCode = null;
		
		String freeText = passegner.getFpLongFreetexts().get(0);
		if(StringUtils.contains(freeText, OneAConstants.FP_FOP_PREFIX_CC)) {
			String[] fpNoStoredCardInfo = FreeTextUtil.getFpNoStoredCardInfo(freeText);
			fpCode = fpNoStoredCardInfo == null ? StringUtils.EMPTY : fpNoStoredCardInfo[1];
		} else if(StringUtils.contains(freeText, OneAConstants.FP_FOP_PREFIX_IBE)) {
			String[] fpStoredCardInfo = FreeTextUtil.getFpStoredCardInfo(freeText);
			fpCode = fpStoredCardInfo == null ? StringUtils.EMPTY : fpStoredCardInfo[1];
		} else if(StringUtils.contains(freeText, OneAConstants.FP_FOP_CASH)) {
			fpCode = OneAConstants.FP_FOP_CASH;
		} else if(StringUtils.contains(freeText, OneAConstants.FP_FOP_CHQ)) {
			fpCode = OneAConstants.FP_FOP_CHQ;
		}else {
			fpCode = StringUtils.EMPTY;
		}
		
		return fpCode;
	}

	/**
	 * Get the login passenger's id in booking
	 * 
	 * @param booking
	 * @return
	 */
	private String getLoginPassengerId(Booking booking) {
		Passenger primaryPassenger = booking.getPassengers().stream()
				.filter(pax -> BooleanUtils.isTrue(pax.isPrimaryPassenger()))
				.findFirst()
				.orElse(null);
		
		return primaryPassenger != null ? primaryPassenger.getPassengerId() : null;
	}

	/**
	 * Build passenger name
	 * format:
	 * 		title(upperCase) + names(familyName & givenName order configured in DB)
	 * 
	 * @param passengers
	 * @param passengerId
	 * @param locale 
	 * @return
	 */
	private String buildPassengerName(List<Passenger> passengers, String passengerId, Locale locale) {
		Passenger passenger = passengers.stream().filter(pax -> pax != null && passengerId.equals(pax.getPassengerId())).findFirst().orElse(null);
		if(passenger == null) {
			return StringUtils.EMPTY;
		}
		
		PaxNameSequence paxNameSequence = paxNameSequenceDAO.findPaxNameSequenceByAppCodeAndLocale(TBConstants.APP_CODE, locale.getCountry().toUpperCase());
		
		StringBuilder sb = new StringBuilder();
		if(StringUtils.isNotEmpty(passenger.getTitle())) {
			sb.append(passenger.getTitle().toUpperCase()).append(" ");
		}
		
		if(paxNameSequence == null || paxNameSequence.getFamilyNameSequence() == 1) {
			sb.append(passenger.getFamilyName()).append(" ").append(passenger.getGivenName());			
		} else {
			sb.append(passenger.getGivenName()).append(" ").append(passenger.getFamilyName());
		}
		
		return sb.toString();
	}

	/**
	 * Build passenger's itinerary
	 * 
	 * @param booking
	 * @param passengerId
	 * @return
	 */
	private String buildPassengerItinerary(Booking booking, String passengerId) {
		List<String> segmentIds = booking.getPassengerSegments().stream()
				.filter(ps -> passengerId.equals(ps.getPassengerId()))
				.map(PassengerSegment::getSegmentId)
				.collect(Collectors.toList());
		
		List<Segment> segments = booking.getSegments().stream()
				.filter(s -> !OneAConstants.EQUIPMENT_TRN.equals(s.getAirCraftType()) && !OneAConstants.EQUIPMENT_LCH.equals(s.getAirCraftType()) 
						&& !OneAConstants.EQUIPMENT_BUS.equals(s.getAirCraftType()) && segmentIds.contains(s.getSegmentID()))
				.collect(Collectors.toList());
		
		StringBuilder sb = new StringBuilder();
		for(Segment segment : segments) {
			String orginCityName = getCityName(segment.getOriginPort(), new Locale("en"));
			String destCityName = getCityName(segment.getDestPort(), new Locale("en"));
			
			if(sb.length() == 0) {
				sb.append(orginCityName).append(" - ").append(destCityName);		
			} else if((sb.length() - orginCityName.length()) == sb.lastIndexOf(orginCityName)) {
				sb.append(" - ").append(destCityName);
			} else {
				sb.append("//").append(orginCityName).append(" - ").append(destCityName);
			}
			
		}
		
		return sb.toString();
	}

	/**
	 * Get city name by airPortCode from AEM
	 * 
	 * name -> defaultName -> ""
	 * 
	 * @param airPort
	 * @param locale
	 * @return
	 */
	private String getCityName(String airPort, Locale locale) {
		City city = aemService.retrieveCityByAirportCode(airPort, locale);
		if(city == null) {
			return StringUtils.EMPTY;
		}
		
		String cityName = StringUtils.isEmpty(city.getName()) ? city.getDefaultName() : city.getName();	
		return cityName == null ? StringUtils.EMPTY : cityName;
	}

	/**
	 * Get the valid eTicket from pssegnerSegment by passengerId
	 * 
	 * @param booking
	 * @param passengerId
	 * @return
	 */
	private ETicket getValidEticketByPassegnerId(Booking booking, String passengerId) {
		if(booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments())) {
			return null;
		}
		return booking.getPassengerSegments().stream().filter(ps -> ps != null && passengerId.equals(ps.getPassengerId()) 
				&& StringUtils.isNotEmpty(ps.getEticketNumber())).map(PassengerSegment::getEticket).findFirst().orElse(null);
	}

	/**
	 * Check booking and passenger can download official receipt or not.
	 * 1. Check booking is eligible;
	 * 2. Check only 1 ticket for the passenger;
	 * 3. Check the ticket is marked by CX/KA;
	 * 4. Check currency/amount exist in FA(ticket);
	 * 5. Check FP freeText for the passenger;
	 * 6. Check IATA office number is in the list from properties.
	 * 
	 * @param pnrBooking
	 * @param passengerId
	 * @param booking
	 * @throws ExpectedException
	 */
	private void checkOfficialReceiptDownload(RetrievePnrBooking pnrBooking, String passengerId, Booking booking)
			throws ExpectedException {
		/** 1. Check booking is eligible */
		boolean hasBCode = BookingBuildUtil.bcodeExist(pnrBooking.getSsrList()); // Booking contains BCode or not
		if(!checkBookingOfficialReceiptEligibility(booking, hasBCode)) {
			throw new ExpectedException(String.format("download official receipt failure => booking[%s] can't download official receipt",
					pnrBooking.getOneARloc()), new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		
		String ticketNumber = getOnlyOneEticketById(pnrBooking.getPassengerSegments(), passengerId);
		/** 2. Check only 1 ticket for the passenger */
		if(StringUtils.isEmpty(ticketNumber)) {
			throw new ExpectedException(String.format("download official receipt failure => passenger[%s] have more than 1 or no tickets in booking[%s] ", 
					passengerId, pnrBooking.getOneARloc()), new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		/** 3. Check the ticket is marked by CX/KA; */
		if(!BookingBuildUtil.isCxKaET(ticketNumber)) {
			throw new ExpectedException(String.format("download official receipt failure => passenger[%s]'s eticket[%s] is not marketed by CX/KA in booking[%s] ", 
					passengerId, ticketNumber, pnrBooking.getOneARloc()), new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		
		/** 4. Check currency/amount exist in FA(ticket) */
		/** Be optional in OLSSMMB-20219
		if(!checkTicketFareExist(booking, passengerId)) {
			throw new ExpectedException(String.format("download official receipt failure => passenger[%s]'s eticket[%s] has no currency/amount in booking[%s] ", 
					passengerId, ticketNumber, pnrBooking.getOneARloc()), new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		*/
		
		/** 5. Check FP freeText for the passenger */
		if(!checkFPByPassengerId(pnrBooking.getPassengers(), passengerId)) {
			throw new ExpectedException(String.format("download official receipt failure => passenger[%s] have no valid FP elements in booking[%s] ", 
					passengerId, pnrBooking.getOneARloc()), new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		
		/** 6. Check IATA office number is in the list from properties*/
		if(!checkIataNumber(booking, passengerId)) {
			throw new ExpectedException(String.format("download official receipt failure => passenger[%s] have no valid FP elements in booking[%s] ", 
					passengerId, pnrBooking.getOneARloc()), new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		
		/** 7. Check fare amount are the same between FP, FA and/or ticket process*/
		if(isDiffFare(pnrBooking, booking, passengerId)) {
			throw new ExpectedException(String.format("download official receipt failure => passenger[%s] fare amount are different between FP, FA and/or ticket process in booking[%s] ", 
					passengerId, pnrBooking.getOneARloc()), new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
	}

	/**
	 * Build BookingBuildRequired for official receipt
	 * 
	 * @return
	 */
	private BookingBuildRequired buildBookingRequired() {
		BookingBuildRequired required = new BookingBuildRequired();
		required.setBaggageAllowances(false);
		
		required.setCprCheck(true);
		
		required.setEmergencyContactInfo(false);
		required.setCountryOfResidence(false);
		required.setTravelDocument(false);
		
		required.setMealSelection(false);
		required.setSeatSelection(false);
		
		return required;
	}

	/**
	 * Check and build passenger informations.
	 * 
	 * @param booking 
	 * @param pnrBooking
	 * @param loginInfo 
	 * @return
	 */
	private List<OfficialReceiptPassengerDTO> checkAndBuildPassengers(Booking booking, RetrievePnrBooking pnrBooking, LoginInfo loginInfo) {
		List<OfficialReceiptPassengerDTO> officialReceiptPassengerDTOs = new ArrayList<>();
		
		List<String> nameTitles = constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE,OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList());
		
		for(Passenger passenger : booking.getPassengers()) {
			if(passenger == null) {
				continue;
			}
			String passengerId = passenger.getPassengerId();
			
			OfficialReceiptPassengerDTO officialReceiptPassengerDTO = new OfficialReceiptPassengerDTO();
			officialReceiptPassengerDTO.setPassengerId(passengerId);
			officialReceiptPassengerDTO.setPassengerType(passenger.getPassengerType());
			officialReceiptPassengerDTO.setParentId(passenger.getParentId());
			officialReceiptPassengerDTO.setFamilyName(passenger.getFamilyName());
			officialReceiptPassengerDTO.setGivenName(removeTitleFromGivenName(passenger.getGivenName(), nameTitles));
			officialReceiptPassengerDTO.setLogin(LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType()) ? passenger.getLoginMember() : passenger.isPrimaryPassenger());
			
			//check passenger eligible or not, and set the ticket is CX/KA.
			String ticketNumber = getOnlyOneEticketById(pnrBooking.getPassengerSegments(), passengerId);
			officialReceiptPassengerDTO.setIsCxKaTicket(BookingBuildUtil.isCxKaET(ticketNumber));
			populateEligibleFlag(pnrBooking, booking, passengerId, officialReceiptPassengerDTO);
			officialReceiptPassengerDTOs.add(officialReceiptPassengerDTO);
		}
		
		return officialReceiptPassengerDTOs;
	}
	
	
	/** 
	 * Populate eligible flag
	 * @param pnrBooking
	 * @param booking
	 * @param passengerId
	 * @param officialReceiptPassengerDTO
	 */
	private void populateEligibleFlag(RetrievePnrBooking pnrBooking, Booking booking, String passengerId, OfficialReceiptPassengerDTO officialReceiptPassengerDTO) {
		String onlyOneEticketNumber = getOnlyOneEticketById(pnrBooking.getPassengerSegments(), passengerId);
		
		/** if passenger linked from cpr will  */
        if (!StringUtils.isEmpty(passengerId) && passengerId.startsWith(CPR_PASSENGER_ID_PREFIX)) {
            logger.info(String.format("Set passenger eligible to false => passenger[id:%s] is linked from CPR", passengerId));
            officialReceiptPassengerDTO.setEligible(false);
            return;
        }
		
		/** The only one ET number is not empty */
		if (StringUtils.isEmpty(onlyOneEticketNumber)) {
			logger.info(String.format("Set passenger eligible to false => passenger[id:%s] has more than one e-ticket or no e-ticket", passengerId));
			officialReceiptPassengerDTO.setEligible(false);
			return;
		}
		
		/** ET is not CXKA */
		if (BooleanUtils.isFalse(officialReceiptPassengerDTO.isCxKaTicket())) {
			logger.info(String.format("Set passenger eligible to false => passenger[id:%s] Eticket is not CX/KA ticket", passengerId));
			officialReceiptPassengerDTO.setEligible(false);
			return;
		}
		
		/** FP is invalid */
		if (!checkFPByPassengerId(pnrBooking.getPassengers(), passengerId)) {
			logger.info(String.format("Set passenger eligible to false => passenger[id:%s] FP is invalid", passengerId));
			officialReceiptPassengerDTO.setEligible(false);
			return;
		}
		
		/** Currency and amount of FP, FA and ticket process are different */
		if (isDiffFare(pnrBooking, booking, passengerId)) {
			logger.info(String.format("Set passenger eligible to false => passenger[id:%s] Currency and amount of FP, FA and ticket process are different.", passengerId));
			officialReceiptPassengerDTO.setEligible(false);
			return;
		}
		
		/** the Iata number is not allowed */
		if (!checkIataNumber(booking, passengerId)) {
			logger.info(String.format("Set passenger eligible to false => passenger[id:%s] Iata number doesn't allowed.", passengerId));
			officialReceiptPassengerDTO.setEligible(false);
			return;
		}
		
		officialReceiptPassengerDTO.setEligible(true);
	}
	
	/**
	 * Remove title from givenName
	 * 
	 * @param givenName
	 * @param nameTitles
	 * @return
	 */
	private String removeTitleFromGivenName(String givenName, List<String> nameTitles) {
		if(StringUtils.isEmpty(givenName)) {
			return StringUtils.EMPTY;
		}
		String title = BookingBuildUtil.retrievePassengerTitle(givenName, nameTitles);
		return BookingBuildUtil.removeTitleFromGivenName(givenName, title);
	}

	/**
	 * Check IATA number is configured in properties
	 * 
	 * @param booking
	 * @param passengerId
	 * @return
	 */
	private boolean checkIataNumber(Booking booking, String passengerId) {
		List<String> iataOfficeNumbers = bizRuleConfig.getOfficialReceiptIataOfficeNumbers();
		if(CollectionUtils.isEmpty(iataOfficeNumbers)) {
			logger.info(String.format("Check passenger OfficalReceipt => passenger[id:%s], no iataOfficeNumbers found in properties", passengerId));
			return false;
		}
		
		boolean validIataExist = booking.getPassengerSegments().stream()
				.anyMatch(ps -> passengerId.equals(ps.getPassengerId()) && ps.getEticket() != null && ps.getEticket().getIataNumber() != null 
				&& iataOfficeNumbers.contains(String.valueOf(ps.getEticket().getIataNumber())));
		
		if(!validIataExist) {
			logger.info(String.format("Check passenger OfficalReceipt => passenger[id:%s], no iataOfficeNumber found from ET in list:%s configured in properties", passengerId, iataOfficeNumbers.toString()));
		}
		
		return validIataExist;
	}

	/**
	 * Check currency/amount exist in FA ticket.
	 * 
	 * @param booking
	 * @param passengerId
	 * @return
	 */
	private boolean checkTicketFareExist(Booking booking, String passengerId) {
		ETicket eticket = getValidEticketByPassegnerId(booking, passengerId);
		
		boolean fareExist = eticket != null && StringUtils.isNotEmpty(eticket.getCurrency()) && StringUtils.isNotEmpty(eticket.getAmount());
		if(!fareExist) {
			logger.info(String.format("Check passenger OfficalReceipt => passenger[id:%s], no currency & amount found in eticket[%s]", passengerId, eticket != null ? eticket.getNumber() : null));
		}
		
		return fareExist;
	}

	/**
	 * Check FP element freeText is validate or not
	 * 1. FP exist;
	 * 2. don't contain any '+' in whole FP element;
	 * 3. contains string configured in properties before first "/";
	 * 4. must match standard freeText format.
	 * 
	 * @param passengers
	 * @param passengerId
	 * @return
	 */
	private boolean checkFPByPassengerId(List<RetrievePnrPassenger> passengers, String passengerId) {
		RetrievePnrPassenger passenger = PnrResponseParser.getPassengerById(passengers, passengerId);
		if(passenger == null) {
			return false;
		}
		
		/** 1. FP exist and only has one */
		if(CollectionUtils.isEmpty(passenger.getFpLongFreetexts()) || passenger.getFpLongFreetexts().size() != 1) {
			logger.info(String.format("Check passenger OfficalReceipt => passenger[id:%s], has no FP / more than one in PNR.", passengerId));
			return false;
		}
		
		/** 2. don't contain any '+' */
		String fpLongFreeText = passenger.getFpLongFreetexts().get(0);
		if(StringUtils.contains(fpLongFreeText, '+')) {
			logger.info(String.format("Check passenger OfficalReceipt => passenger[id:%s], FP longFreeText[%s] contains '+'.", passengerId, fpLongFreeText));
			return false;
		}
		
		/** 3. contains string configured in properties before first "/" */
		List<String> prefixStrs = bizRuleConfig.getOfficialReceiptFpFreetextPrefix();
		boolean containVaildPrefix = prefixStrs.stream().anyMatch(prefix -> StringUtils.isNoneEmpty(prefix) && StringUtils.contains(fpLongFreeText.split("/")[0], prefix));
		if(!containVaildPrefix) {
			logger.info(String.format("Check passenger OfficalReceipt => passenger[id:%s], FP longFreeText[%s] have no matched prefix configured in properties before first '/' as %s.",
					passengerId, fpLongFreeText, prefixStrs));
			return false;
		}
		
		/** 4. must match standard freeText format. */
		if(StringUtils.contains(fpLongFreeText, OneAConstants.FP_FOP_PREFIX_CC)) {
			String[] fpStoredCardInfo = FreeTextUtil.getFpNoStoredCardInfo(fpLongFreeText);
			if(fpStoredCardInfo == null) {
				logger.info(String.format("Check passenger OfficalReceipt => passenger[id:%s], FP longFreeText[%s] does not match the standard FP format.",
						passengerId, fpLongFreeText));
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Retrieve fare data if they are all the same. Ignore those which is null.
	 * (1) FA's currency and amount AND
	 * (2) FP's currency and amount AND
	 * (3) Ticket Process's currency and amount
	 * 
	 * @param pnrBooking
	 * @param booking
	 * @param passengerId
	 * @return
	 * Object[0] = currency (null if fare data are not all the same)
	 * Object[1] = amount (null if fare data are not all the same)
	 * 
	 */
	private String[] retrieveSameFareData(RetrievePnrBooking pnrBooking, Booking booking, String passengerId) {
		String[] fareData = new String[2];
		List<String> currencies = new ArrayList<>();
		List<String> amounts = new ArrayList<>();

		// retrieve FP of PNR
		RetrievePnrPassenger passenger = PnrResponseParser.getPassengerById(pnrBooking.getPassengers(), passengerId);
		String fpLongFreeText = passenger.getFpLongFreetexts().get(0);
		if(StringUtils.contains(fpLongFreeText, OneAConstants.FP_FOP_PREFIX_CC)) {
			String[] fpStoredCardInfo = FreeTextUtil.getFpNoStoredCardInfo(fpLongFreeText);
			if (StringUtils.isNotEmpty(fpStoredCardInfo[4]) && StringUtils.isNotEmpty(fpStoredCardInfo[5])) {
				currencies.add(fpStoredCardInfo[4]);
				amounts.add(fpStoredCardInfo[5]);
			}
		}
		
		ETicket eticket = getValidEticketByPassegnerId(booking, passengerId);
		if (eticket != null) {
			// retrieve FA of PNR
			if (StringUtils.isNotEmpty(eticket.getCurrency()) && StringUtils.isNotEmpty(eticket.getAmount())) {
				currencies.add(eticket.getCurrency());
				amounts.add(eticket.getAmount());
			}
			
			// retrieve ticket process fare info
			if (StringUtils.isNotEmpty(eticket.getTicketProcessFareCurrency()) && StringUtils.isNotEmpty(eticket.getTicketProcessFareAmount())) {
				currencies.add(eticket.getTicketProcessFareCurrency());
				amounts.add(eticket.getTicketProcessFareAmount());
			}
		}
		
		// Compare all of the currency and amount
		if (!isSameElements(currencies) || !isSameElements(amounts)) {
			return fareData;
		}
		fareData[0] = currencies.get(0);
		fareData[1] = amounts.get(0);
		return fareData;
	}
	
	/**
	 * True if all elements in the list are the same (ignore case)
	 * @param items
	 * @return
	 */
	private boolean isSameElements(List<String> items) {
		if (CollectionUtils.isEmpty(items)) {
			return false;
		}
		List<String> distinctedItems = items.stream().map(StringUtils::upperCase).distinct().collect(Collectors.toList());
		return distinctedItems.size() == 1;
	}
	
	/**
	 * Check if there is any fare info are different. It compares the following.
	 * (1) FA's currency and amount AND
	 * (2) FP's currency and amount AND
	 * (3) Ticket Process's currency and amount
	 * 
	 * Return false if all of them are the same as each other. It ignores and won't compare the one which is missing.
	 * Return true if one of them is different from each other.
	 * @param booking
	 * @param passengerId
	 */
	private boolean isDiffFare(RetrievePnrBooking pnrBooking, Booking booking, String passengerId) {
		String[] fareData = retrieveSameFareData(pnrBooking, booking, passengerId);
		return fareData[0] == null || fareData[1] == null;
	}

	/**
	 * Check there is only 1 e-ticket under the passenger.
	 * 
	 * @param passengerSegments
	 * @param passengerId
	 * @return
	 */
	private String getOnlyOneEticketById(List<RetrievePnrPassengerSegment> passengerSegments, String passengerId) {
		if(StringUtils.isEmpty(passengerId) || CollectionUtils.isEmpty(passengerSegments)) {
			return null;
		}
		
		List<RetrievePnrEticket> etickets = new ArrayList<>();
		for(RetrievePnrPassengerSegment passengerSegment : passengerSegments) {
			if(passengerId.equals(passengerSegment.getPassengerId()) && CollectionUtils.isNotEmpty(passengerSegment.getEtickets())) {
				etickets.addAll(passengerSegment.getEtickets());
			}
		}
		
		List<String> eticketNumbers = etickets.stream().map(RetrievePnrEticket::getTicketNumber).distinct().collect(Collectors.toList());
		
		if(eticketNumbers.size() == 1) {
			return eticketNumbers.get(0);
		} else {
			logger.info(String.format("Check passenger OfficalReceipt => passenger[id:%s], have no/more than 1 tickets:%s",
					passengerId, CollectionUtils.isNotEmpty(eticketNumbers) ? eticketNumbers.toString() : null));
			return null;
		}
	}

	/**
	 * Build all InEligibility download official receipt list of passengers
	 * 
	 * @param passengers
	 * @param pnrBooking 
	 * @param loginInfo 
	 * @return
	 */
	private List<OfficialReceiptPassengerDTO> buildInEligibilityPassengers(List<Passenger> passengers, RetrievePnrBooking pnrBooking, LoginInfo loginInfo) {
		if(CollectionUtils.isEmpty(passengers)) {
			return Collections.emptyList();
		}
		
		List<OfficialReceiptPassengerDTO> officialReceiptPassengerDTOs = new ArrayList<>();
		for(Passenger passenger : passengers) {
			if(passenger == null) {
				continue;
			}
			
			OfficialReceiptPassengerDTO officialReceiptPassengerDTO = new OfficialReceiptPassengerDTO();
			
			officialReceiptPassengerDTO.setPassengerId(passenger.getPassengerId());
			officialReceiptPassengerDTO.setPassengerType(passenger.getPassengerType());
			officialReceiptPassengerDTO.setParentId(passenger.getParentId());
			officialReceiptPassengerDTO.setFamilyName(passenger.getFamilyName());
			officialReceiptPassengerDTO.setGivenName(passenger.getGivenName());
			officialReceiptPassengerDTO.setEligible(false);
			officialReceiptPassengerDTO.setLogin(LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType()) ? passenger.getLoginMember() : passenger.isPrimaryPassenger());
			//check passenger eligible or not, and set the ticket is CX/KA.
			String ticketNumber = getOnlyOneEticketById(pnrBooking.getPassengerSegments(), passenger.getPassengerId());
			officialReceiptPassengerDTO.setIsCxKaTicket(BookingBuildUtil.isCxKaET(ticketNumber));
			
			officialReceiptPassengerDTOs.add(officialReceiptPassengerDTO);
		}
		
		return officialReceiptPassengerDTOs;
	}

	/**
	 * Check the booking is eligible to download official receipt
	 * 1.
	 * 2. booking is not GDSBooking & NDCBooking;
	 * 3. booking is flight-only booking;
	 * 4. All sectors(only flight sector) must be marketed by CX/KA;
	 * 
	 * @param booking
	 * @param hasBCode 
	 * @return
	 */
	private boolean checkBookingOfficialReceiptEligibility(Booking booking, boolean hasBCode) {
		/** check booking contains BCode or not */
		if(BooleanUtils.isTrue(hasBCode)) {
			logger.info(String.format("Check booking OfficalReceipt => Booking[%s] contains BCode", booking.getOneARloc()));
			return false;
		}
		
		/** Check booking is not GDS/NDC booking*/
		if(BooleanUtils.isTrue(booking.isGdsBooking()) || BooleanUtils.isTrue(booking.isNdcBooking())) {
			logger.info(String.format("Check booking OfficalReceipt => Booking[%s] is GDSBooking[%s], NDCBooking[%s], officeId: %s",
					booking.getOneARloc(), booking.isGdsBooking(), booking.isNdcBooking(), booking.getOfficeId()));
			return false;
		}
		
		/** Check booking is flight only booking*/
		if(booking.getBookingPackageInfo() != null && BooleanUtils.isFalse(booking.getBookingPackageInfo().isFlightOnly())) {
			logger.info(String.format("Check booking OfficalReceipt => Booking[%s] is not flightOnly booking", booking.getOneARloc()));
			return false;
		}
		
		/** All sectors(only flight sector) must be marketed by CX/KA */
		List<String> flightIds = getFlightsWithInvaildMarketCompany(booking);
		if(CollectionUtils.isNotEmpty(flightIds)) {
			logger.info(String.format("Check booking OfficalReceipt => Booking[%s], sectors[%s] are not marketed by CX/KA", booking.getOneARloc(), flightIds.toString()));
			return false;
		}
			
		return true;
	}

	/**
	 * Get the sectors'(only flight) IDs not with market company(CX/KA)
	 * 
	 * @param booking
	 * @return
	 */
	private List<String> getFlightsWithInvaildMarketCompany(Booking booking) {
		if(booking == null || CollectionUtils.isEmpty(booking.getSegments())) {
			return Collections.emptyList();
		}
		
		return booking.getSegments().stream().filter(s -> BookingBuildUtil.isFlightSector(s.getAirCraftType()) 
				&& !OneAConstants.COMPANY_CX.equals(s.getMarketCompany())
				&& !OneAConstants.COMPANY_KA.equals(s.getMarketCompany()))
				.map(Segment::getSegmentID).collect(Collectors.toList());
	}

}