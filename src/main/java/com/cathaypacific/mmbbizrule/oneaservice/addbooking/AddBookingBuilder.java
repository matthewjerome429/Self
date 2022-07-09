package com.cathaypacific.mmbbizrule.oneaservice.addbooking;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.dto.request.oneaoperation.AddRmDetail;
import com.cathaypacific.mmbbizrule.dto.request.oneaoperation.AddSkDetail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.header.SessionStatus;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ElementManagementSegmentType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.FreeTextQualificationType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.FrequentTravellerIdentificationTypeU;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.FrequentTravellerInformationTypeU;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.LongFreeTextType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.MiscellaneousRemarkType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.MiscellaneousRemarksType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ObjectFactory;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.OptionalPNRActionsType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements.DataElementsMaster;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements.DataElementsMaster.DataElementsIndiv;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ReferenceInfoType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ReferencingDetailsType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ReservationControlInformationDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ReservationControlInformationTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.SpecialRequirementsDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.SpecialRequirementsTypeDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.TicketElementType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.TicketInformationType;

public class AddBookingBuilder {
	
	private ObjectFactory objFactory = new ObjectFactory();
	
	/**
	 * build request for add SK element
	 * @return
	 */
	public PNRAddMultiElements buildSkRequest(String rloc, List<AddSkDetail> addSkDetails){
		PNRAddMultiElements request = objFactory.createPNRAddMultiElements();
		
		// add rloc
		request.setReservationInfo(createReservationInfo(rloc));
		// add pnrActions
		request.setPnrActions(createPnrActions("11"));
		
		// add dataElementsMaster
		DataElementsMaster dataElementsMaster = objFactory.createPNRAddMultiElementsDataElementsMaster();
		request.setDataElementsMaster(dataElementsMaster);
		//add marker1 to dataElementsMaster
		dataElementsMaster.setMarker1(objFactory.createDummySegmentTypeI());
		
		// add SK DataElementsIndiv
		for(AddSkDetail addSkDetail : addSkDetails) {
			dataElementsMaster.getDataElementsIndiv().add(createSkDataElementsIndiv(addSkDetail));			
		}
		
		//add the last dataElementsIndiv to dataElementsMaster
		createLastDataElementsIndiv(dataElementsMaster);

		return request;
	}
	
	public PNRAddMultiElements buildRmRequest(String rloc, List<AddRmDetail> addRmDetails) {
		PNRAddMultiElements request = objFactory.createPNRAddMultiElements();
		
		// add rloc
		request.setReservationInfo(createReservationInfo(rloc));
		// add pnrActions
		request.setPnrActions(createPnrActions("11"));
		
		// add dataElementsMaster
		DataElementsMaster dataElementsMaster = objFactory.createPNRAddMultiElementsDataElementsMaster();
		request.setDataElementsMaster(dataElementsMaster);
		//add marker1 to dataElementsMaster
		dataElementsMaster.setMarker1(objFactory.createDummySegmentTypeI());
		
		for(AddRmDetail addRmDetail : addRmDetails) {
			dataElementsMaster.getDataElementsIndiv().add(createRmDataElementsIndiv(addRmDetail));
		}
		
		//add the last dataElementsIndiv to dataElementsMaster
		createLastDataElementsIndiv(dataElementsMaster);

		return request;
	}
	
	/**
	 * Create RM DataElementsIndiv
	 * 
	 * @param addRmDetail
	 * @return
	 */
	private DataElementsIndiv createRmDataElementsIndiv(AddRmDetail addRmDetail) {
		DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
		dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.RM_SEGMENT));
		
		dataElementsIndiv.setMiscellaneousRemark(createMiscellaneousRemark("RM", addRmDetail.getFreeText()));
		dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(addRmDetail.getPassegnerIds(), addRmDetail.getSegmentIds()));
		return dataElementsIndiv;
	}

	/**
	 * create MiscellaneousRemark by type & freeText
	 * 
	 * @param type
	 * @param freeText
	 * @return
	 */
	private MiscellaneousRemarksType createMiscellaneousRemark(String type, String freeText) {
		if(StringUtils.isEmpty(freeText) || StringUtils.isEmpty(type)) {
			return null;
		}
		MiscellaneousRemarksType miscellaneousRemarksType = objFactory.createMiscellaneousRemarksType();
		MiscellaneousRemarkType miscellaneousRemarkType = objFactory.createMiscellaneousRemarkType();
		miscellaneousRemarkType.setType(type);
		miscellaneousRemarkType.setFreetext(freeText);
		miscellaneousRemarksType.setRemarks(miscellaneousRemarkType);
		return miscellaneousRemarksType;
	}

	/**
	 * Create SK DataElementsIndiv
	 * 
	 * @param addSkDetail
	 * @return
	 */
	private DataElementsIndiv createSkDataElementsIndiv(AddSkDetail addSkDetail) {
		DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
		dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.SK_SEGMENT));
		dataElementsIndiv.setServiceRequest(createServiceRequest(addSkDetail.getSsrType(), null, "0", addSkDetail.getCompanyId(), addSkDetail.getFreeText()));
		dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(addSkDetail.getPassegnerIds(), addSkDetail.getSegmentIds()));
		return dataElementsIndiv;
	}
	
	/**
	 * create referenceForDataElement bean
	 * 
	 * @param passengerIds
	 * @param segmentIds
	 * @return ReferenceInfoType
	 */
	private ReferenceInfoType createReferenceForDataElement(List<String> passengerIds, List<String> segmentIds) {
		if(CollectionUtils.isEmpty(passengerIds) && CollectionUtils.isEmpty(segmentIds)) {
			return null;
		}
		ReferenceInfoType referenceForDataElement = objFactory.createReferenceInfoType();
		if(!CollectionUtils.isEmpty(passengerIds)) {
			for(String passengerId : passengerIds) {
				referenceForDataElement.getReference().add(createReference(OneAConstants.PT_QUALIFIER, passengerId));
			}
		}
		if(!CollectionUtils.isEmpty(segmentIds)) {
			for(String segmentId : segmentIds) {
				referenceForDataElement.getReference().add(createReference(OneAConstants.ST_QUALIFIER, segmentId));
			}
		}
		return referenceForDataElement;
	}

	/**
	 * build request for adding booking by FQTV
	 * @return
	 */
	public PNRAddMultiElements buildFQTVRequest(String rloc, String passengerId, List<String> segmentIds, String memberId, RetrievePnrBooking booking){
		/**** build request start ****/
		PNRAddMultiElements request = objFactory.createPNRAddMultiElements();
		
		// add rloc
		request.setReservationInfo(createReservationInfo(rloc));
		// add pnrActions
		request.setPnrActions(createPnrActions("11"));
		
		// add dataElementsMaster
		DataElementsMaster dataElementsMaster = objFactory.createPNRAddMultiElementsDataElementsMaster();
		request.setDataElementsMaster(dataElementsMaster);
		//add marker1 to dataElementsMaster
		dataElementsMaster.setMarker1(objFactory.createDummySegmentTypeI());
		// add FFP
		addFFP(dataElementsMaster, passengerId, segmentIds, memberId, booking.getSegments());
		//add the last dataElementsIndiv to dataElementsMaster
		createLastDataElementsIndiv(dataElementsMaster);
		/**** build request end ****/
		
		return request;
	}
	
	/**
	 * build request for adding booking by SK CUST
	 * @return
	 */
	public PNRAddMultiElements buildSkCustRequest(String rloc, String memberId, List<String> companyIds){
		/**** build request start ****/
		PNRAddMultiElements request = objFactory.createPNRAddMultiElements();
		
		// add rloc
		request.setReservationInfo(createReservationInfo(rloc));
		// add pnrActions
		request.setPnrActions(createPnrActions("11"));
		
		// add dataElementsMaster
		DataElementsMaster dataElementsMaster = objFactory.createPNRAddMultiElementsDataElementsMaster();
		request.setDataElementsMaster(dataElementsMaster);
		//add marker1 to dataElementsMaster
		dataElementsMaster.setMarker1(objFactory.createDummySegmentTypeI());
		// add SK CUST
		createSkCUST(dataElementsMaster, memberId, companyIds, MMBConstants.APP_CODE);
		//add the last dataElementsIndiv to dataElementsMaster
		createLastDataElementsIndiv(dataElementsMaster);
		/**** build request end ****/
		
		return request;
	}
	
	/**
	 * build request for adding RF element
	 * 
	 * @param rloc
	 * @param session
	 * @return
	 */
	public PNRAddMultiElements buildRFRequest(String rloc, Session session) {
		PNRAddMultiElements request = objFactory.createPNRAddMultiElements();
		
		if (session == null || SessionStatus.START.getStatus().equals(session.getStatus())) {
			/** set reservationInfo */
			request.setReservationInfo(createReservationInfo(rloc));
		}
		
		/** set pnrActions */
		setPnrActions(request, session);
		
		/** set dataElementsMaster */
		DataElementsMaster dataElementsMaster = objFactory.createPNRAddMultiElementsDataElementsMaster();
		request.setDataElementsMaster(dataElementsMaster);
		
		/** add marker1 to dataElementsMaster */
		dataElementsMaster.setMarker1(objFactory.createDummySegmentTypeI());
		
		/** add RF element */
		createLastDataElementsIndiv(dataElementsMaster);
		
		return request;
	}
	
	/**
	 * build request for adding TKTL element
	 * 
	 * @param rloc
	 * @param session
	 * @return
	 */
	public PNRAddMultiElements buildTkTlRequest(String rloc, Session session) {
		PNRAddMultiElements request = objFactory.createPNRAddMultiElements();
		
		if (session == null || SessionStatus.START.getStatus().equals(session.getStatus())) {
			/** set reservationInfo */
			request.setReservationInfo(createReservationInfo(rloc));
		}
		
		/** set pnrActions */
		setPnrActions(request, session);
		
		/** set dataElementsMaster */
		DataElementsMaster dataElementsMaster = objFactory.createPNRAddMultiElementsDataElementsMaster();
		request.setDataElementsMaster(dataElementsMaster);
		
		/** add marker1 to dataElementsMaster */
		dataElementsMaster.setMarker1(objFactory.createDummySegmentTypeI());
		
		dataElementsMaster.getDataElementsIndiv().add(createTkTlDataElementsIndiv());
		
		return request;
	}

	/**
	 * Create TKTL DataElementsIndiv
	 * 
	 * @return
	 */
	private DataElementsIndiv createTkTlDataElementsIndiv() {
		DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
		dataElementsIndiv.setElementManagementData(createTkTlElementManagementData());
		dataElementsIndiv.setTicketElement(createTkTlTicketElement());
		return dataElementsIndiv;
	}
	
	/**
	 * Create TKTL TicketElement.
	 * Add a new TK TL element with current date & time(HK), office ID(HKGCX0388) and queue type(Q31C3)
	 * 
	 * @return
	 */
	private TicketElementType createTkTlTicketElement() {
		TicketElementType ticketElement = objFactory.createTicketElementType();
		
		TicketInformationType ticket = objFactory.createTicketInformationType();
		ticket.setIndicator(OneAConstants.TICKET_INDICATOR_TL);
		
		/** using HK time, Set date & time[need to add 5 mins for adding the message onto the queue] */
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 5);
		TimeZone timeZone = TimeZone.getTimeZone("Asia/Hong_Kong");
		ticket.setDate(DateUtil.getSimpleDateFormat(timeZone, DateUtil.DATE_PATTERN_DDMMYY).format(cal.getTime()));
		ticket.setTime(DateUtil.getSimpleDateFormat(timeZone, DateUtil.TIME_PATTERN_HHMM).format(cal.getTime()));
		
		ticket.setOfficeId("HKGCX0388");
		ticket.setQueueNumber("31");
		ticket.setQueueCategory("3");
		ticketElement.setTicket(ticket);
		
		return ticketElement;
	}

	/**
	 * Create TKTL ElementManagementData
	 * 
	 * @return
	 */
	private ElementManagementSegmentType createTkTlElementManagementData() {
		ElementManagementSegmentType elementManagementData = objFactory.createElementManagementSegmentType();
		elementManagementData.setSegmentName(PnrResponseParser.SEGMENT_NAME_TK);
		ReferencingDetailsType reference = objFactory.createReferencingDetailsType();
		reference.setQualifier(PnrResponseParser.REFERENCE_QUALIFIER_OT);
		reference.setNumber("14");
		elementManagementData.setReference(reference);
		return elementManagementData;
	}

	/**
	 * set pntActions for PNR_AddMultiElements by session status
	 * 
	 * @param session
	 * @param request
	 */
	private void setPnrActions(PNRAddMultiElements request, Session session) {
		OptionalPNRActionsType pnrActions = objFactory.createOptionalPNRActionsType();
		String actionCode = "11";
		if(session != null && !SessionStatus.END.getStatus().equals(session.getStatus()) ) {
			actionCode = "0";
		} 
		pnrActions.getOptionCode().add(new BigInteger(actionCode));
		request.setPnrActions(pnrActions);
	}
	
	/**
	 * create reservationInfo element bean
	 * @param rloc
	 * @return ReservationControlInformationTypeI
	 */
	private ReservationControlInformationTypeI createReservationInfo(String rloc) {
		ReservationControlInformationDetailsTypeI reservation = objFactory.createReservationControlInformationDetailsTypeI();
		reservation.setControlNumber(rloc);
		ReservationControlInformationTypeI reservationInfo = objFactory.createReservationControlInformationTypeI();
		reservationInfo.setReservation(reservation);
		
		return reservationInfo;
	}
	
	/**
	 * create pnrActions element bean
	 * @param optionCode
	 * @return OptionalPNRActionsType
	 */
	private OptionalPNRActionsType createPnrActions(String optionCode) {
		OptionalPNRActionsType pnrActions = objFactory.createOptionalPNRActionsType();
		pnrActions.getOptionCode().add(new BigInteger(optionCode));
		
		return pnrActions;
	}
	
	/**
	 * add FFP info to the request
	 * @param dataElementsMaster
	 * @param passengerId
	 * @param segmentIds
	 * @param memberId
	 * @param segments
	 */
	private void addFFP(DataElementsMaster dataElementsMaster, String passengerId, List<String> segmentIds, String memberId, List<RetrievePnrSegment> segments) {
		// if the input is not valid, return
		if (StringUtils.isEmpty(passengerId) || CollectionUtils.isEmpty(segmentIds) 
				|| StringUtils.isEmpty(memberId) || CollectionUtils.isEmpty(segments)) {
			return;
		}

		// add FFP info for each segment
		for (String segmentId : segmentIds) {
			// get segment marketing company
			String segmentMarketingCompany = segments.stream()
					.filter(seg -> !StringUtils.isEmpty(seg.getMarketCompany()) && segmentId.equals(seg.getSegmentID()))
					.map(RetrievePnrSegment :: getMarketCompany).findFirst().orElse(null);
			
			if(!StringUtils.isEmpty(segmentMarketingCompany)) {		
				DataElementsIndiv dataElementsIndiv = objFactory
						.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
				dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.SSR_SEGMENT));
				dataElementsIndiv.setServiceRequest(
						createServiceRequest(OneAConstants.SSR_TYPE_FQTV, null, null, segmentMarketingCompany, null));
				dataElementsIndiv.setFrequentTravellerData(createFrequentTravellerData(memberId));
				dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(OneAConstants.PT_QUALIFIER,
						passengerId, OneAConstants.ST_QUALIFIER, segmentId));
				dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
			}
		}

	}
	
	/**
	 * createServiceRequest bean
	 * @param type
	 * @param status
	 * @param quantity
	 * @param companyId
	 * @param freetext
	 * @return SpecialRequirementsDetailsTypeI
	 */
	private SpecialRequirementsDetailsTypeI createServiceRequest(String type, String status, String quantity, String companyId, String freetext) {
		SpecialRequirementsDetailsTypeI serviceRequest = objFactory.createSpecialRequirementsDetailsTypeI();
		serviceRequest.setSsr(createSsr(type,status,quantity,companyId,freetext));
		
		return serviceRequest;
	}
	
	/**
	 * create SSR bean
	 * @param type
	 * @param status
	 * @param quantity
	 * @param companyId
	 * @param freetext
	 * @return SpecialRequirementsTypeDetailsTypeI
	 */
	private SpecialRequirementsTypeDetailsTypeI createSsr(String type, String status, String quantity, String companyId, String freetext) {
		SpecialRequirementsTypeDetailsTypeI ssr = objFactory.createSpecialRequirementsTypeDetailsTypeI();
		ssr.setType(type);
		ssr.setStatus(status);
		if(quantity != null) {
			ssr.setQuantity(new BigInteger(quantity));
		}
		
		if(!StringUtils.isEmpty(companyId)) {
			ssr.setCompanyId(companyId);
		}
		
		if(freetext != null) {
			if(freetext.length()>70) {
				Pattern pattern = Pattern.compile("(.*-.*-.*-.*-.*-.*-.*-)(.*-.*)");
				Matcher matcher = pattern.matcher(freetext);
				if(matcher.matches()) {
					ssr.getFreetext().add(matcher.group(1));
					ssr.getFreetext().add(matcher.group(2));
				}
			}else {
				ssr.getFreetext().add(freetext);
			}
		}
		
		return ssr;
	}
	
	/**
	 * create FrequentTravellerData bean by companyId and memberId
	 * @param companyId
	 * @param memberId
	 * @return FrequentTravellerInformationTypeU
	 */ 
	private FrequentTravellerInformationTypeU createFrequentTravellerData(String memberId) {
		FrequentTravellerInformationTypeU frequentTravellerData = objFactory.createFrequentTravellerInformationTypeU();
		FrequentTravellerIdentificationTypeU frequentTraveller = objFactory.createFrequentTravellerIdentificationTypeU();
		frequentTraveller.setCompanyId(OneAConstants.COMPANY_CX);
		frequentTraveller.setMembershipNumber(OneAConstants.COMPANY_CX+memberId);
		frequentTravellerData.setFrequentTraveller(frequentTraveller);
		return frequentTravellerData;
	}
	
	/**
	 * create elementManagementData bean
	 * @param segmentName
	 * @return ElementManagementSegmentType
	 */
	private ElementManagementSegmentType createElementManagementData(String segmentName) {
		ElementManagementSegmentType elementManagementData = objFactory.createElementManagementSegmentType();
		elementManagementData.setSegmentName(segmentName);
		
		return elementManagementData;
	}
	
	/**
	 * create referenceForDataElement bean which has two references
	 * @param qualifier1
	 * @param number1
	 * @param qualifier2
	 * @param number2
	 * @return ReferenceInfoType
	 */
	private ReferenceInfoType createReferenceForDataElement(String qualifier1,String number1,String qualifier2,String number2) {
		ReferenceInfoType referenceForDataElement = objFactory.createReferenceInfoType();
		referenceForDataElement.getReference().add(createReference(qualifier1, number1));
		referenceForDataElement.getReference().add(createReference(qualifier2, number2));
		return referenceForDataElement;
	}
	
	/**
	 * create reference bean
	 * @param qualifier
	 * @param number
	 * @return ReferencingDetailsType
	 */
	private ReferencingDetailsType createReference(String qualifier, String number) {
		ReferencingDetailsType reference = objFactory.createReferencingDetailsType();
		reference.setQualifier(qualifier);
		reference.setNumber(number);
		return reference;
	}
	
	/**
	 * create the last DataElementsIndiv bean 
	 * @param dataElementsMaster
	 */
	private void createLastDataElementsIndiv(DataElementsMaster dataElementsMaster) {
		DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
		dataElementsIndiv.setElementManagementData(createElementManagementData("RF"));
		dataElementsIndiv.setFreetextData(createFreetextData("3",OneAConstants.P22_TYPE,OneAConstants.CXMB));
		dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
	}
	
	/**
	 * create freetextData element bean
	 * @param subjectQualifier
	 * @param type
	 * @param longFreetext
	 * @return LongFreeTextType
	 */
	private LongFreeTextType createFreetextData(String subjectQualifier, String type, String longFreetext) {
		FreeTextQualificationType freetextDetail = objFactory.createFreeTextQualificationType();
		freetextDetail.setSubjectQualifier(subjectQualifier);
		freetextDetail.setType(type);
		
		LongFreeTextType freetextData = objFactory.createLongFreeTextType();
		freetextData.setFreetextDetail(freetextDetail);
		freetextData.setLongFreetext(longFreetext);
		
		return freetextData;
	}
	
	/**
	 * 
	 * @Description
	 * @param
	 * @return void
	 * @throws ExpectedException 
	 */
	private void createSkCUST(DataElementsMaster dataElementsMaster, String memberId, List<String> companyIds, String appCode){
		if(!StringUtils.isEmpty(memberId) && !CollectionUtils.isEmpty(companyIds) && !StringUtils.isEmpty(appCode)) {
			for(String companyId : companyIds) {
				//build dataElementsIndiv
				DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
				dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.SK_SEGMENT));
				dataElementsIndiv.setServiceRequest(createServiceRequest(OneAConstants.SK_TYPE_CUST, null, "0", companyId, memberId+":"+appCode));
				dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
			}
		}
	}
}
