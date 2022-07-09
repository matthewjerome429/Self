package com.cathaypacific.mmbbizrule.oneaservice.updatepassenger;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.constant.CommonConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateDestinationAddressDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateEmailDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateEmergencyContactDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateFFPInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateInfantDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateBasicSegmentInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdatePassengerDetailsRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdatePhoneInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateAdultSegmentInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateTravelDocDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateTsDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.TravelDoc;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrAddressDetails;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrContactPhone;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEmail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEmrContactInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTravelDoc;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ElementManagementSegmentType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.FreeTextQualificationType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.FrequentTravellerIdentificationTypeU;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.FrequentTravellerInformationTypeU;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.LongFreeTextType;
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

/**
 * 
 * OLSS-MMB
 * 
 * @Desc this class is used to convert front end DTO to oneA request model
 * @author fengfeng.jiang
 * @date Jan 8, 2018 5:28:29 PM
 * @version V1.0
 */
public class UpdatePassengerRequestBuilder {
	
	private ObjectFactory objFactory = new ObjectFactory();
	
	private String passengerId;
	
	private String infantId;
	
	private RetrievePnrPassenger pnrPassenger;
	
	private Passenger passenger;
	
	private List<RetrievePnrSegment> pnrSegments;
	
	private List<Segment> segments;
	
	private List<RetrievePnrPassengerSegment> pnrPassengerSegments;
	
	private List<RetrievePnrPassengerSegment> pnrInfantPassengerSegments;
	
	private List<String> updateSegmentIds = new ArrayList<>();
	
	/**
	 * 
	* @Description convert front end DTO to oneA request model
	* @param
	* @param map 
	* @return PNRAddMultiElements
	* @author fengfeng.jiang
	 * @throws BusinessBaseException 
	 */
	public PNRAddMultiElements buildRequest(UpdatePassengerDetailsRequestDTO requestDTO, RetrievePnrBooking pnrBooking, Booking booking, Map<String, List<String>> deleteMap, boolean transferFFP, boolean transferTravelDoc) throws BusinessBaseException{
		if (requestDTO == null || deleteMap == null) {
			return null;
		}
		
		passengerId = requestDTO.getPassengerId();
		if (requestDTO.getInfant() != null) {
			infantId = requestDTO.getInfant().getPassengerId();
		}
		
		if(StringUtils.isEmpty(passengerId)) {
			return null;
		}

		//get current passenger
		pnrPassenger = PnrResponseParser.getPassengerById(pnrBooking.getPassengers(), passengerId);
		passenger = BookingBuildUtil.getPassengerById(booking.getPassengers(), passengerId);
		pnrSegments = pnrBooking.getSegments();
		segments = booking.getSegments();
		pnrPassengerSegments = PnrResponseParser.getPassengerSegmentByIds(pnrBooking.getPassengerSegments(), passengerId);
		pnrInfantPassengerSegments = PnrResponseParser.getPassengerSegmentByIds(pnrBooking.getPassengerSegments(), passengerId+"I");
		
		//get update segment list
		if (!CollectionUtils.isEmpty(requestDTO.getSegments())) {
			for(UpdateAdultSegmentInfoDTO segment:requestDTO.getSegments()) {
				String st = segment.getSegmentId();
				if(!StringUtils.isEmpty(st)) {
					updateSegmentIds.add(st);
				}
			}
		}
		
		if (requestDTO.getInfant() != null && !CollectionUtils.isEmpty(requestDTO.getInfant().getSegments())) {
			for(UpdateBasicSegmentInfoDTO segment:requestDTO.getInfant().getSegments()) {
				String st = segment.getSegmentId();
				if(!StringUtils.isEmpty(st)) {
					updateSegmentIds.add(st);
				}
			}
		}
		
		// there is invalid segment (passed check-in open time) in the request
		boolean invalidSegmentExistInRequest = updateSegmentIds.stream().anyMatch(segId -> isSegmentFlown(segId));
		if (invalidSegmentExistInRequest) {
			throw new UnexpectedException("Can't update pax info because of invaild segment",
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		
		/**** build request start ****/
		PNRAddMultiElements request = objFactory.createPNRAddMultiElements();
		
		/*add rloc*/
		request.setReservationInfo(createReservationInfo(requestDTO.getRloc()));
		
		/*add pnrActions*/
		request.setPnrActions(createPnrActions("11"));
		
		/*add dataElementsMaster*/
		DataElementsMaster dataElementsMaster = objFactory.createPNRAddMultiElementsDataElementsMaster();
		request.setDataElementsMaster(dataElementsMaster);
		//add marker1 to dataElementsMaster
		dataElementsMaster.setMarker1(objFactory.createDummySegmentTypeI());
		//add travelDOC and fill delete map
		addTravelDoc(dataElementsMaster, requestDTO, pnrBooking, booking, deleteMap, transferTravelDoc);
		//add KTN and fill delete map
		addKTN(dataElementsMaster, requestDTO, pnrBooking, deleteMap);
		//add redressNo and fill delete map
		addRedressNo(dataElementsMaster, requestDTO, pnrBooking, deleteMap);
		//add country code and phone number to dataElementsMaster
		addCountryCodeAndPhoneNo(dataElementsMaster, requestDTO.getPhoneInfo(), deleteMap);
		//add email to dataElementsMaster
		addEmail(dataElementsMaster, requestDTO.getEmail(), deleteMap);
		//add emergency contact to dataElementsMaster
		addEmergencyContact(dataElementsMaster, requestDTO.getEmergencyContact(), deleteMap);
		//add destination address to dataElementsMaster
		addDestinationAddress(dataElementsMaster, requestDTO, pnrBooking, booking, deleteMap);
		//add FFP info
		addFFP(dataElementsMaster, requestDTO,pnrBooking, deleteMap, booking, transferFFP);
		//add the last dataElementsIndiv to dataElementsMaster
		createLastDataElementsIndiv(dataElementsMaster);
		/**** build request end ****/
		
		return request;
	}
	
	/**
	 * add KTN and fill delete map
	 * 
	 * @param dataElementsMaster
	 * @param requestDTO
	 * @param booking
	 * @param deleteMap
	 */
	private void addKTN(DataElementsMaster dataElementsMaster, UpdatePassengerDetailsRequestDTO requestDTO, RetrievePnrBooking pnrBooking, Map<String, List<String>> deleteMap) {
		List<String> qualifierIds = new ArrayList<>();
		
		//Handle non-infant KTN
		if(requestDTO.getKtn() != null) {
			//add KTN
			addTS(dataElementsMaster, qualifierIds, pnrBooking, requestDTO.getPassengerId(), requestDTO.getKtn(), OneAConstants.TRAVEL_DOCUMENT_TYPE_K, false);
		}
		
		//Handle infant KTN
		UpdateInfantDTO updateInfant = requestDTO.getInfant();
		if(updateInfant != null && updateInfant.getKtn() != null) {
			//add KTN
			addTS(dataElementsMaster, qualifierIds, pnrBooking, updateInfant.getPassengerId(), updateInfant.getKtn(), OneAConstants.TRAVEL_DOCUMENT_TYPE_K, true);
		}
		
		if(!CollectionUtils.isEmpty(qualifierIds)) {
			addOtToMap(deleteMap, CommonConstants.MAP_KTN_KEY, qualifierIds);
		}
	}
	
	/**
	 * add redressNo and fill delete map
	 * 
	 * @param dataElementsMaster
	 * @param requestDTO
	 * @param booking
	 * @param deleteMap
	 */
	private void addRedressNo(DataElementsMaster dataElementsMaster, UpdatePassengerDetailsRequestDTO requestDTO, RetrievePnrBooking pnrBooking, Map<String, List<String>> deleteMap) {
		List<String> qualifierIds = new ArrayList<>();
		
		//Handle non-infant redress
		if (requestDTO.getRedress() != null) {
			addTS(dataElementsMaster, qualifierIds, pnrBooking, requestDTO.getPassengerId(), requestDTO.getRedress(), OneAConstants.TRAVEL_DOCUMENT_TYPE_R, false);
		}
		
		//Handle infant redress
		UpdateInfantDTO updateInfant = requestDTO.getInfant();
		if(updateInfant != null && updateInfant.getRedress() != null) {
			addTS(dataElementsMaster, qualifierIds, pnrBooking, updateInfant.getPassengerId(), updateInfant.getRedress(), OneAConstants.TRAVEL_DOCUMENT_TYPE_R, true);
		}
		
		if(!CollectionUtils.isEmpty(qualifierIds)) {
			addOtToMap(deleteMap, CommonConstants.MAP_REDRESS_KEY, qualifierIds);
		}
	}

	/**
	 * @param dataElementsMaster
	 * @param qualifierIds
	 * @param currentPassengerId
	 * @param ts
	 * @param type
	 * @param infant
	 */
	private void addTS(DataElementsMaster dataElementsMaster, List<String> qualifierIds, RetrievePnrBooking pnrBooking, String currentPassengerId, UpdateTsDTO ts, String type, boolean infant) {
		if(ts != null) {
			RetrievePnrPassenger currentPax = PnrResponseParser.getPassengerById(pnrBooking.getPassengers(), currentPassengerId);
			List<RetrievePnrPassengerSegment> currentPassengerSegments = PnrResponseParser.getPassengerSegmentByIds(pnrBooking.getPassengerSegments(), currentPassengerId);		
			// add all TS to qualifierIds
			addAllTsToQualifierIds(qualifierIds, type, currentPax, currentPassengerSegments);
			if(!ts.isBlank()) {
				String companyId = getFixedCompany();
				String number = ts.getNumber();
				
				RetrievePnrTravelDoc pnrTS = getTSBothLevelById(ts, currentPax, currentPassengerSegments, type);
				
				String nationality = BizRulesUtil.nvl(pnrTS.getNationality());
				String countryOfIssuance = BizRulesUtil.nvl(pnrTS.getCountryOfIssuance());
				String extraFreeText = BizRulesUtil.nvl(pnrTS.getExtraFreeText());
				
				String expireDate = "";
				if(StringUtils.isNotEmpty(pnrTS.getExpiryDateYear())
						&& StringUtils.isNotEmpty(pnrTS.getExpiryDateMonth())
						&& StringUtils.isNotEmpty(pnrTS.getExpiryDateDay())) {
					String expiryDateYear = pnrTS.getExpiryDateYear();
					String expiryDateMonth = pnrTS.getExpiryDateMonth();
					String expiryDateDay = pnrTS.getExpiryDateDay();
					expireDate = DateUtil.convertDateFormat(expiryDateDay + expiryDateMonth + expiryDateYear, DateUtil.DATE_PATTERN_DDMMYYYY, DateUtil.DATE_PATTERN_DDMMMYY);
				}
				
				String freeText = String.format(OneAConstants.TRAVEL_DOCUMENT_DOCO_FREETEXT_FORMAT, extraFreeText, type, number, nationality, expireDate, countryOfIssuance);
				if(infant) {
					freeText = String.format(OneAConstants.TRAVEL_DOCUMENT_DOCO_FREETEXT_INFANT_FORMAT, extraFreeText, type, number, nationality, expireDate, countryOfIssuance, PnrResponseParser.SSR_TYPE_DOCO_FREETEXT_INFANT);
				}
				
				//add elementManagementData
				List<ReferencingDetailsType> referencingDetailsTypes = new ArrayList<>();
				referencingDetailsTypes.add(createReference(OneAConstants.PT_QUALIFIER, passengerId));
				addTravelDoc(OneAConstants.DOCO, companyId, dataElementsMaster, freeText, referencingDetailsTypes);
			}
		}
	}

	/**
	 * add all TS to qualifierIds
	 * @param qualifierIds
	 * @param type
	 * @param passenger
	 * @param passengerSegments
	 */
	private void addAllTsToQualifierIds(List<String> qualifierIds, String type, RetrievePnrPassenger passenger,
			List<RetrievePnrPassengerSegment> passengerSegments) {
		if (qualifierIds == null) {
			return;
		}
		//  delete all ktn/redress
		if(OneAConstants.TRAVEL_DOCUMENT_TYPE_K.equals(type)) {
			if (passenger != null && !CollectionUtils.isEmpty(passenger.getKtns())) {
				// add customer level ktns to delete map
				qualifierIds.addAll(getTsIds(passenger.getKtns()));
			}
			if (!CollectionUtils.isEmpty(passengerSegments)) {
				// add product level ktns to delete map
				for (RetrievePnrPassengerSegment ps : passengerSegments) {
					qualifierIds.addAll(getTsIds(ps.getKtns()));
				}
			}
		} else if(OneAConstants.TRAVEL_DOCUMENT_TYPE_R.equals(type)) {
			if (passenger != null && !CollectionUtils.isEmpty(passenger.getRedresses())) {
				// add customer level redress no. to delete map
				qualifierIds.addAll(getTsIds(passenger.getRedresses()));
			}
			if (!CollectionUtils.isEmpty(passengerSegments)) {
				// add product level redress no. to delete map
				for (RetrievePnrPassengerSegment ps : passengerSegments) {
					qualifierIds.addAll(getTsIds(ps.getRedresses()));
				}
			}
		}
	}

	/**
	 * @param ts
	 * @param pnrPassengerSegment
	 * @param type
	 * @return
	 */
	private RetrievePnrTravelDoc getTSBothLevelById(UpdateTsDTO ts, RetrievePnrPassenger pnrPassenger, List<RetrievePnrPassengerSegment> pnrPassengerSegments, String type) {
		// all TS
		List<RetrievePnrTravelDoc> pnrTsList = new ArrayList<>();
		RetrievePnrTravelDoc pnrTS = null;
		if(OneAConstants.TRAVEL_DOCUMENT_TYPE_K.equals(type)) {
			// add customer level KTN
			pnrTsList.addAll(pnrPassenger.getKtns());
			// add product level KTN
			pnrTsList.addAll(pnrPassengerSegments.stream()
					.filter(ps -> !CollectionUtils.isEmpty(ps.getKtns())).map(RetrievePnrPassengerSegment :: getKtns)
					.reduce(new ArrayList<>(), (all, item) -> {
						all.addAll(item);
						return all;
					}));
			pnrTS = getTravelDocById(pnrTsList, ts.getQualifierId());
			
		} else if(OneAConstants.TRAVEL_DOCUMENT_TYPE_R.equals(type)) {
			// add customer level Redress No
			pnrTsList.addAll(pnrPassenger.getRedresses());
			// add product level Redress No
			pnrTsList.addAll(pnrPassengerSegments.stream()
					.filter(ps -> !CollectionUtils.isEmpty(ps.getRedresses())).map(RetrievePnrPassengerSegment :: getRedresses)
					.reduce(new ArrayList<>(), (all, item) -> {
						all.addAll(item);
						return all;
					}));
			pnrTS = getTravelDocById(pnrTsList, ts.getQualifierId());
		}
		
		if(pnrTS == null) {
			pnrTS = new RetrievePnrTravelDoc();
		}
		return pnrTS;
	}

	/**
	 * @param pnrTravelDocs
	 * @param qualifierId
	 * @return
	 */
	private RetrievePnrTravelDoc getTravelDocById(List<RetrievePnrTravelDoc> pnrTravelDocs, BigInteger qualifierId) {
		for(RetrievePnrTravelDoc pnrTravelDoc : pnrTravelDocs) {
			if(pnrTravelDoc != null && pnrTravelDoc.getQualifierId() == qualifierId) {
				return pnrTravelDoc;
			}
		}
		return null;
	}

	/**
	 * get list of id in RetrievePnrTravelDoc
	 * 
	 * @param pnrTSs
	 * @return
	 */
	private List<String> getTsIds(List<RetrievePnrTravelDoc> pnrTSs) {
		List<String> ids = new ArrayList<>();
		for(RetrievePnrTravelDoc pnrTS : pnrTSs) {
			if(pnrTS != null && pnrTS.getQualifierId() != null) {
				ids.add(pnrTS.getQualifierId().toString());
			}
		}
		return ids;
	}

	/**
	 * add travelDOC and fill delete map
	 * 
	 * @param dataElementsMaster
	 * @param requestDTO
	 * @param pnrBooking
	 * @param map
	 */
	private void addTravelDoc(DataElementsMaster dataElementsMaster, UpdatePassengerDetailsRequestDTO requestDTO, RetrievePnrBooking pnrBooking, Booking booking, Map<String, List<String>> map, boolean transferTravelDoc) {
		// if there is any travel doc need to be updated in the request
		boolean needToUpdateTravelDoc = (!CollectionUtils.isEmpty(requestDTO.getSegments()) 
				&& requestDTO.getSegments().stream().anyMatch(seg -> seg.getPrimaryTravelDoc() != null || seg.getSecondaryTravelDoc() != null))
				|| (requestDTO.getInfant() != null && !CollectionUtils.isEmpty(requestDTO.getInfant().getSegments())
						&& requestDTO.getInfant().getSegments().stream().anyMatch(seg -> seg.getPrimaryTravelDoc() != null || seg.getSecondaryTravelDoc() != null));
		if (!needToUpdateTravelDoc) {
			return;
		}
		List<String> qualifierIds = new ArrayList<>();
		List<BigInteger> cstLevelOTIdsOth = new ArrayList<>();
		cstLevelOTIdsOth.addAll(pnrPassenger.getOthTravelDocs().stream().map(RetrievePnrTravelDoc::getQualifierId).collect(Collectors.toList()));
		
		
		qualifierIds.addAll(BizRulesUtil.convertToStringList(cstLevelOTIdsOth));
		

		//Handle non-infant travelDoc
		//filter out segment which has entered check in time and after
		if (!CollectionUtils.isEmpty(requestDTO.getSegments())) {
			List<UpdateAdultSegmentInfoDTO> updateSegments = requestDTO.getSegments().stream()
					.filter(seg -> seg != null && !segmentPassedCheckInOpenTime(seg.getSegmentId()))
					.collect(Collectors.toList());
			updateTravelDocs(updateSegments, pnrBooking, qualifierIds, dataElementsMaster, false);
			addCountryOfResidence(dataElementsMaster, requestDTO.getPassengerId(), pnrPassengerSegments, updateSegments, map, false);
		}
		
		//Handle infant travelDoc
		UpdateInfantDTO infant = requestDTO.getInfant();
		if (infant != null && !CollectionUtils.isEmpty(infant.getSegments())) {
			// filter out segment which has entered check in time and after
			List<UpdateBasicSegmentInfoDTO> updateInfantSegments = infant.getSegments().stream()
					.filter(seg -> seg != null && !segmentPassedCheckInOpenTime(seg.getSegmentId()))
					.collect(Collectors.toList());
			updateTravelDocs(updateInfantSegments, pnrBooking, qualifierIds, dataElementsMaster, true);
			addCountryOfResidence(dataElementsMaster, infant.getPassengerId(), pnrInfantPassengerSegments, updateInfantSegments, map, true);
		}
		
		if (transferTravelDoc) {
			transferTravelDocs(dataElementsMaster, requestDTO, qualifierIds, booking, pnrBooking, map);
		}
		
		addOtToMap(map, CommonConstants.MAP_TRAVEL_DOCUMENT_KEY, qualifierIds);
	}

	private void addCountryOfResidence(DataElementsMaster dataElementsMaster, String currentPassengerId, List<RetrievePnrPassengerSegment> passengerSegments,
			List<? extends UpdateBasicSegmentInfoDTO> updateSegments, Map<String, List<String>> map, boolean infant) {
		
		List<String> otList = new ArrayList<>();
		
		for(UpdateBasicSegmentInfoDTO updateSegment: updateSegments){
			if(updateSegment.getPrimaryTravelDoc() == null){
				continue;
			}
			String countryCode = updateSegment.getPrimaryTravelDoc().getCountryOfResidence();
			RetrievePnrPassengerSegment pnrPassengerSegment = passengerSegments.stream()
					.filter(ps -> currentPassengerId.equals(ps.getPassengerId()) && StringUtils.isNotEmpty(updateSegment.getSegmentId())
							&& updateSegment.getSegmentId().equals(ps.getSegmentId()))
					.findFirst().orElse(null);
			//get prepopulate ResAdress
			if (StringUtils.isEmpty(countryCode) || pnrPassengerSegment == null){
				continue;
			}
			for(RetrievePnrAddressDetails addressDetails:pnrPassengerSegment.getResAddresses()) {
				otList.add(addressDetails.getQualifierId().toString());
			}
			RetrievePnrAddressDetails retrievePnrAddressDetails = getPrepopulateCountryOfResidence(pnrPassengerSegment);
			String streetAddress = retrievePnrAddressDetails.getStreet();//get street address
			String city = retrievePnrAddressDetails.getCity();//get city
			String state = retrievePnrAddressDetails.getStateCode();//get state
			String zipCode = retrievePnrAddressDetails.getZipCode();//get zip code
			String companyId = getFixedCompany();
			//add elementManagementData
			DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
			dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.SSR_SEGMENT));
			//DOCA format: type/Country/Street address/City/State/ZIP code
			//add serviceRequest
			dataElementsIndiv.setServiceRequest(createServiceRequest(OneAConstants.DOCA,OneAConstants.HK_STATUS,"1",companyId,
					String.format(infant?OneAConstants.COUNTRY_OF_RESIDENCE_FREETEXT_INFANT_FORMAT:OneAConstants.COUNTRY_OF_RESIDENCE_FREETEXT_FORMAT, countryCode,BizRulesUtil.nvl(streetAddress),BizRulesUtil.nvl(city),BizRulesUtil.nvl(state),BizRulesUtil.nvl(zipCode))));
			dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
			//add referenceForDataElement
			List<ReferencingDetailsType> referencingDetailsTypes = new ArrayList<>();
			referencingDetailsTypes.add(createReference(OneAConstants.PT_QUALIFIER, passengerId));
			referencingDetailsTypes.add(createReference(OneAConstants.ST_QUALIFIER, updateSegment.getSegmentId()));
			dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(referencingDetailsTypes));
			}
		addOtToMap(map, CommonConstants.MAP_COUNTRY_OF_RESIDENCE_KEY, otList);
	}
	
	/**
	 * 
	 * @param updateSegments
	 * @param booking
	 * @param qualifierIds
	 * @param dataElementsMaster
	 */
	private void updateTravelDocs(List<? extends UpdateBasicSegmentInfoDTO> updateSegments, RetrievePnrBooking booking, List<String> qualifierIds, DataElementsMaster dataElementsMaster, boolean infant) {
		/** update travel doc, update it to product level **/
		for (UpdateBasicSegmentInfoDTO updateSegment : updateSegments) {
			addTravelDocs(updateSegment.getPrimaryTravelDoc(), booking, updateSegment.getSegmentId(), qualifierIds,
					dataElementsMaster, true, infant);
			addTravelDocs(updateSegment.getSecondaryTravelDoc(), booking, updateSegment.getSegmentId(), qualifierIds,
					dataElementsMaster, false, infant);
		}

	}
	
	/*private boolean segmentExistInRequest(PassengerSegmentDTO passengerSegmentDTO) {
		String _segmentId = passengerSegmentDTO.getSegmentId();
		for(String segmentId : updateSegmentIds) {
			if(_segmentId.equals(segmentId)) {
				return true;
			}
		}
		return false;
	}*/

	/**
	 * 
	 * @param updateTravelDoc
	 * @param booking
	 * @param segmentId
	 * @param qualifierIds
	 * @param dataElementsMaster
	 * @param isPrimary
	 */
	private void addTravelDocs(UpdateTravelDocDTO updateTravelDoc, RetrievePnrBooking booking, String segmentId, List<String> qualifierIds, DataElementsMaster dataElementsMaster, boolean isPrimary, boolean infant) {
		if(updateTravelDoc == null) {
			return;
		}
		String _passengerId = passengerId;
		if(infant) {
			_passengerId = infantId;
		}
		RetrievePnrPassengerSegment retrievePnrPassengerSegment = PnrResponseParser.getPassengerSegmentByIds(booking.getPassengerSegments(), _passengerId, segmentId);
		if(retrievePnrPassengerSegment == null) {
			return;
		}	
		List<BigInteger> psQualifierIds;
		if(isPrimary) {
			psQualifierIds = Optional.ofNullable(retrievePnrPassengerSegment.getPriTravelDocs()).orElse(new ArrayList<>()).stream().map(RetrievePnrTravelDoc::getQualifierId).collect(Collectors.toList());				
		} else {
			psQualifierIds = Optional.ofNullable(retrievePnrPassengerSegment.getSecTravelDocs()).orElse(new ArrayList<>()).stream().map(RetrievePnrTravelDoc::getQualifierId).collect(Collectors.toList());
		}
		qualifierIds.addAll(BizRulesUtil.convertToStringList(psQualifierIds));
		
		List<BigInteger> pQualifierIds;
		if(isPrimary) {
			if (!infant) {
				pQualifierIds  = pnrPassenger.getPriTravelDocs().stream().map(RetrievePnrTravelDoc::getQualifierId).collect(Collectors.toList());
			} else {
				pQualifierIds = getPassengerTravelDoc(infantId, booking, OneAConstants.TRAVEL_DOCUMENT_TYPE_PRIMARY);
			}
		} else {
			if (!infant) {
				pQualifierIds  = pnrPassenger.getSecTravelDocs().stream().map(RetrievePnrTravelDoc::getQualifierId).collect(Collectors.toList());
			} else {
				pQualifierIds = getPassengerTravelDoc(infantId, booking, OneAConstants.TRAVEL_DOCUMENT_TYPE_SECONDARY);
			}
		}
		qualifierIds.addAll(BizRulesUtil.convertToStringList(pQualifierIds));
		
		String companyId = getFixedCompany();
		String type = BizRulesUtil.nvl(updateTravelDoc.getTravelDocumentType());
		String number = BizRulesUtil.nvl(updateTravelDoc.getTravelDocumentNumber());
		String familyName = BizRulesUtil.nvl(updateTravelDoc.getFamilyName());
		String givenName = BizRulesUtil.nvl(updateTravelDoc.getGivenName());
		String nationality = BizRulesUtil.nvl(updateTravelDoc.getNationality());
		String countryOfIssuance = BizRulesUtil.nvl(updateTravelDoc.getCountryOfIssuance());
		String dayOfBirth = StringUtils.isEmpty(updateTravelDoc.getDateOfBirth()) ? "" : DateUtil.convertDateFormat(updateTravelDoc.getDateOfBirth(), DateUtil.DATE_PATTERN_YYYY_MM_DD, DateUtil.DATE_PATTERN_DDMMMYY);
		String gender =  BizRulesUtil.nvl(updateTravelDoc.getGender());
		
		String expireDate = "";
		if (!StringUtils.isEmpty(updateTravelDoc.getDateOfExpire())) {
			Pattern pattern = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2}");
			Matcher matcher = pattern.matcher(updateTravelDoc.getDateOfExpire());
			if(matcher.matches()) {
				String[] dateOfExpiry = updateTravelDoc.getDateOfExpire().split("-", -1);
				String expiryDateYear = dateOfExpiry[0];
				String expiryDateMonth = dateOfExpiry[1];
				String expiryDateDay = dateOfExpiry[2];
				String dateText=expiryDateDay + expiryDateMonth + expiryDateYear;
				expireDate = DateUtil.convertDateFormat(dateText, DateUtil.DATE_PATTERN_DDMMYYYY, DateUtil.DATE_PATTERN_DDMMMYY);
			}
		}
		
		// if is infant and gender is F/M, add "I" after it
		if (infant && StringUtils.isNotEmpty(gender)) {
			gender += OneAConstants.GENDER_INFANT_FLAG;
		}
		//format: doc type/country of issuance/doc number/nationality/DOB/gender/doc expire date/last name/first name
		String freeText = String.format(OneAConstants.TRAVEL_DOCUMENT_DOCS_FREETEXT_FORMAT, type, countryOfIssuance, number, nationality, dayOfBirth, gender, expireDate, familyName, givenName);
		String ssrType = OneAConstants.DOCS;
		if(!isPrimary && type.equalsIgnoreCase(OneAConstants.TRAVEL_DOCUMENT_TYPE_VISA)) {
			if(infant) {
				freeText = String.format(OneAConstants.TRAVEL_DOCUMENT_DOCO_FREETEXT_INFANT_FORMAT, "", type, number, nationality, expireDate, countryOfIssuance, PnrResponseParser.SSR_TYPE_DOCO_FREETEXT_INFANT);
			} else {
				freeText = String.format(OneAConstants.TRAVEL_DOCUMENT_DOCO_FREETEXT_FORMAT, "", type, number, nationality, expireDate, countryOfIssuance);					
			}
			ssrType = OneAConstants.DOCO;
		}
		
		//add elementManagementData
		List<ReferencingDetailsType> referencingDetailsTypes = new ArrayList<>();
		referencingDetailsTypes.add(createReference(OneAConstants.PT_QUALIFIER, passengerId));
		referencingDetailsTypes.add(createReference(OneAConstants.ST_QUALIFIER, segmentId));
		addTravelDoc(ssrType, companyId, dataElementsMaster, freeText, referencingDetailsTypes);
	}

	/**
	 * get travel doc of passenger
	 * @param passengerId 
	 * @param booking
	 * @param docType
	 * @return List<BigInteger>
	 */
	private List<BigInteger> getPassengerTravelDoc(String passengerId, RetrievePnrBooking booking, String docType) {
		RetrievePnrPassenger infantPassenger = PnrResponseParser.getPassengerById(booking.getPassengers(), passengerId);	
		if (infantPassenger != null) {
			if (OneAConstants.TRAVEL_DOCUMENT_TYPE_PRIMARY.equals(docType)
					&& !CollectionUtils.isEmpty(infantPassenger.getPriTravelDocs())) {
				return infantPassenger.getPriTravelDocs().stream().map(RetrievePnrTravelDoc::getQualifierId)
						.collect(Collectors.toList());
			} else if (OneAConstants.TRAVEL_DOCUMENT_TYPE_SECONDARY.equals(docType)
					&& !CollectionUtils.isEmpty(infantPassenger.getSecTravelDocs())) {
				return infantPassenger.getSecTravelDocs().stream().map(RetrievePnrTravelDoc::getQualifierId)
						.collect(Collectors.toList());
			}
		}
		return new ArrayList<>();
	}

	/**
	 * @param segmentId
	 * @param ssrType
	 * @param companyId
	 * @param dataElementsMaster
	 * @param freeText
	 * @param referencingDetailsTypes
	 */
	private void addTravelDoc(String ssrType, String companyId, DataElementsMaster dataElementsMaster, String freeText, List<ReferencingDetailsType> referencingDetailsTypes) {
		DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
		dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.SSR_SEGMENT));
		dataElementsIndiv.setServiceRequest(createServiceRequest(ssrType, OneAConstants.HK_STATUS, "1", companyId, freeText));
		dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(referencingDetailsTypes));	
		dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
	}
	
	/**
	 * 
	* @Description judge if the segment has entered check in time and after
	* @param segmentId
	* @return boolean
	 */
	private boolean segmentPassedCheckInOpenTime(String segmentId) {
		Segment seg = segments.stream().filter(s -> s != null && s.getSegmentID().equals(segmentId)).findFirst().orElse(null);
		if(seg == null){
			return false;
		}
		return seg.isOpenToCheckIn() || seg.isWithinNinetyMins() || seg.isFlown();
	}
	
	/**
	 * 
	* @Description create pnrActions element bean
	* @param
	* @return OptionalPNRActionsType
	* @author fengfeng.jiang
	 */
	private OptionalPNRActionsType createPnrActions(String optionCode) {
		OptionalPNRActionsType pnrActions = objFactory.createOptionalPNRActionsType();
		pnrActions.getOptionCode().add(new BigInteger(optionCode));
		
		return pnrActions;
	}
	
	/**
	 * 
	 * @Description create reservationInfo element bean
	 * @param
	 * @return ReservationControlInformationTypeI
	 * @author fengfeng.jiang
	 */
	private ReservationControlInformationTypeI createReservationInfo(String rloc) {
		ReservationControlInformationDetailsTypeI reservation = objFactory.createReservationControlInformationDetailsTypeI();
		reservation.setControlNumber(rloc);
		ReservationControlInformationTypeI reservationInfo = objFactory.createReservationControlInformationTypeI();
		reservationInfo.setReservation(reservation);
		
		return reservationInfo;
	}
	
	/**
	 * 
	 * @Description create freetextData element bean
	 * @param
	 * @return LongFreeTextType
	 * @author fengfeng.jiang
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
	 * @Description create createServiceRequest bean
	 * @param
	 * @return ElementManagementSegmentType
	 * @author fengfeng.jiang
	 */
	private SpecialRequirementsDetailsTypeI createServiceRequest(String type, String status, String quantity, String companyId, String freetext) {
		SpecialRequirementsDetailsTypeI serviceRequest = objFactory.createSpecialRequirementsDetailsTypeI();
		serviceRequest.setSsr(createSsr(type,status,quantity,companyId,freetext));
		
		return serviceRequest;
	}
	
	/**
	 * 
	 * @Description create ssr bean
	 * @param
	 * @return ElementManagementSegmentType
	 * @author fengfeng.jiang
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
	 * 
	 * @Description add country code and phone number to the request
	 * @param
	 * @return void
	 * @author fengfeng.jiang
	 */
	private void addCountryCodeAndPhoneNo(DataElementsMaster dataElementsMaster, UpdatePhoneInfoDTO contactInfo, Map<String, List<String>> deleteMap) {
		if (contactInfo != null) {
			// all existing phone number <OT> entries[customer level]
			List<String> otList = pnrPassenger.getContactPhones().stream().map(RetrievePnrContactPhone::getQualifierId)
					.map(BigInteger::toString).collect(Collectors.toList());
			
			String countryNum = contactInfo.getPhoneCountryNumber();
			String phoneNo = contactInfo.getPhoneNo();

			if (!BooleanUtils.isTrue(contactInfo.getConvertToOlssContactInfo())) { // normal case, update the phone info
				// remove all customer level CTCM except the new one
				dataElementsMaster.getDataElementsIndiv().add(createPhoneDataElementsIndiv(countryNum, phoneNo));
				addOtToMap(deleteMap, CommonConstants.MAP_PHONENO_KEY, otList);
			} else if (BooleanUtils.isTrue(contactInfo.getConvertToOlssContactInfo()) && passenger != null
					&& passenger.getContactInfo() != null && passenger.getContactInfo().getPhoneInfo() != null
					&& !StringUtils.isEmpty(passenger.getContactInfo().getPhoneInfo().getPhoneNo())
					&& !StringUtils.isEmpty(passenger.getContactInfo().getPhoneInfo().getPhoneCountryNumber())
					&& !passenger.getContactInfo().getPhoneInfo().isOlssContact()) { // if convertToOlssContactInfo flag is true, then update previous phone to a OLSS contact(add "XX" to the freeText)
				dataElementsMaster.getDataElementsIndiv().add(createPhoneDataElementsIndiv(passenger.getContactInfo().getPhoneInfo().getPhoneCountryNumber(), passenger.getContactInfo().getPhoneInfo().getPhoneNo()));
				addOtToMap(deleteMap, CommonConstants.MAP_PHONENO_KEY, otList);
			}
		}
	}

	/**
	 * create DataElementsIndiv for phone
	 * @param countryNum
	 * @param phoneNo
	 * @return DataElementsIndiv
	 */
	private DataElementsIndiv createPhoneDataElementsIndiv(String countryNum, String phoneNo) {
		DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
		dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.SSR_SEGMENT));
		dataElementsIndiv.setServiceRequest(createServiceRequest(OneAConstants.CTCM,OneAConstants.HK_STATUS,"1",getCompanyIdForSegments(),String.format("%s/%s", countryNum+phoneNo, OneAConstants.XX_INDICATOR)));
		List<ReferencingDetailsType> referencingDetailsTypes = new ArrayList<>();
		referencingDetailsTypes.add(createReference(OneAConstants.PT_QUALIFIER, passengerId));
		dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(referencingDetailsTypes));
		return dataElementsIndiv;
	}
	
	/**
	 * 
	 * @Description add email to the request
	 * @param
	 * @return void
	 * @author fengfeng.jiang
	 */
	private void addEmail(DataElementsMaster dataElementsMaster, UpdateEmailDTO email, Map<String, List<String>> deleteMap) {
		if (email != null) {
			// all existing email <OT> entries[customer level]
			List<String> otList = pnrPassenger.getEmails().stream().map(RetrievePnrEmail::getQualifierId)
					.map(BigInteger::toString).collect(Collectors.toList());

			if (!BooleanUtils.isTrue(email.getConvertToOlssContactInfo())) { // if the email in requestDTO is not empty and convertToOlssContact is false, update the email
				dataElementsMaster.getDataElementsIndiv().add(createEmailDataElementsIndiv(email.getEmail()));
				// remove all customer level CTCE except the new one
				addOtToMap(deleteMap, CommonConstants.MAP_EMAIL_KEY, otList);
			} else if (BooleanUtils.isTrue(email.getConvertToOlssContactInfo())
					&& passenger != null && passenger.getContactInfo() != null
					&& passenger.getContactInfo().getEmail() != null
					&& !StringUtils.isEmpty(passenger.getContactInfo().getEmail().getEmailAddress())
					&& !passenger.getContactInfo().getEmail().isOlssContact()) { // if the email is masked and convertToOlssContactInfo flag is true, update the previous email to a OLSS contact(add "XX" to freeText)
				dataElementsMaster.getDataElementsIndiv()
						.add(createEmailDataElementsIndiv(passenger.getContactInfo().getEmail().getEmailAddress()));
				// remove all customer level CTCE except the new one
				addOtToMap(deleteMap, CommonConstants.MAP_EMAIL_KEY, otList);
			}
		}
	}

	/**
	 * create dataElementsIndiv for email
	 * @param email
	 * @return
	 */
	private DataElementsIndiv createEmailDataElementsIndiv(String email) {
		DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
		dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.SSR_SEGMENT));
		dataElementsIndiv.setServiceRequest(createServiceRequest(OneAConstants.CTCE,OneAConstants.HK_STATUS,"1",getCompanyIdForSegments(),String.format("%s/%s", email.replaceAll("@", "//").replaceAll("_", "..").replaceAll("-", "./"), OneAConstants.XX_INDICATOR)));
		List<ReferencingDetailsType> referencingDetailsTypes = new ArrayList<>();
		referencingDetailsTypes.add(createReference(OneAConstants.PT_QUALIFIER, passengerId));
		dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(referencingDetailsTypes));
		return dataElementsIndiv;
	}
	
	/**
	 * 
	 * @Description create elementManagementData bean
	 * @param
	 * @return ElementManagementSegmentType
	 * @author fengfeng.jiang
	 */
	private ElementManagementSegmentType createElementManagementData(String segmentName) {
		ElementManagementSegmentType elementManagementData = objFactory.createElementManagementSegmentType();
		elementManagementData.setSegmentName(segmentName);
		
		return elementManagementData;
	}
	
	/**
	 * 
	 * @Description create reference bean
	 * @param
	 * @return ReferencingDetailsType
	 * @author fengfeng.jiang
	 */
	private ReferencingDetailsType createReference(String qualifier, String number) {
		ReferencingDetailsType reference = objFactory.createReferencingDetailsType();
		reference.setQualifier(qualifier);
		reference.setNumber(number);
		return reference;
	}
	
	/**
	 * 
	 * @Description add emergency contact to the request
	 * @param
	 * @return void
	 * @author fengfeng.jiang
	 */
	private void addEmergencyContact(DataElementsMaster dataElementsMaster, UpdateEmergencyContactDTO emergencyContact, Map<String, List<String>> deleteMap) {
		if(emergencyContact == null) {
			return;
		}
		//all existing emergency contact <OT> entries[customer level]
		List<String> otList = pnrPassenger.getEmrContactInfos().stream().map(RetrievePnrEmrContactInfo::getQualifierId).map(BigInteger::toString).collect(Collectors.toList());

		if(!emergencyContact.isBlank()) {
			String contactName = emergencyContact.getContactName();
			String phoneNo = emergencyContact.getPhoneNo();
			String countryCode = BizRulesUtil.convertPhoneNumberToIso2CountryCode(emergencyContact.getPhoneCountryNumber() + phoneNo);
			
			DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
			dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.SSR_SEGMENT));
			dataElementsIndiv.setServiceRequest(createServiceRequest(OneAConstants.PCTC,OneAConstants.HK_STATUS,null,getCompanyIdForSegments(),String.format("%s/%s", contactName, countryCode+phoneNo)));
			List<ReferencingDetailsType> referencingDetailsTypes = new ArrayList<>();
			referencingDetailsTypes.add(createReference(OneAConstants.PT_QUALIFIER, passengerId));
			dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(referencingDetailsTypes));
			dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
		}
		//remove all customer level PCTC except the new one
		addOtToMap(deleteMap, CommonConstants.MAP_EMERGENCY_INFO_KEY, otList);
	}
	
	/**
	 * 
	 * @Description create referenceForDataElement bean
	 * @param
	 * @return void
	 * @author fengfeng.jiang
	 */
	private ReferenceInfoType createReferenceForDataElement(List<ReferencingDetailsType> referencingDetailsTypes) {
		ReferenceInfoType referenceForDataElement = objFactory.createReferenceInfoType();
		for(ReferencingDetailsType referencingDetailsType : referencingDetailsTypes) {
			referenceForDataElement.getReference().add(referencingDetailsType);
		}
		return referenceForDataElement;
	}
	
	/**
	 * 
	* @Description create referenceForDataElement bean which has two references
	* @param qualifier1
	* @param number1
	* @param qualifier2
	* @param number2
	* @return ReferenceInfoType
	* @author haiwei.jia
	 */
	private ReferenceInfoType createReferenceForDataElement(String qualifier1,String number1,String qualifier2,String number2) {
		ReferenceInfoType referenceForDataElement = objFactory.createReferenceInfoType();
		referenceForDataElement.getReference().add(createReference(qualifier1, number1));
		referenceForDataElement.getReference().add(createReference(qualifier2, number2));
		return referenceForDataElement;
	}
	
	/**
	 * 
	 * @Description add destination address to the request
	 * @param
	 * @return void
	 * @author fengfeng.jiang
	 */
	private void addDestinationAddress(DataElementsMaster dataElementsMaster, UpdatePassengerDetailsRequestDTO requestDTO, RetrievePnrBooking pnrBooking, Booking booking, Map<String, List<String>> deleteMap) {
		//update country of residence for passenger
		if (requestDTO.getDestinationAddress() != null) {
			addDestinationAddress(dataElementsMaster, pnrBooking, booking, requestDTO.getPassengerId(), requestDTO.getDestinationAddress(), deleteMap, false);
		}
		
		//update country of residence for infant
		if (requestDTO.getInfant() != null && requestDTO.getInfant().getDestinationAddress() != null) {
			addDestinationAddress(dataElementsMaster, pnrBooking, booking, requestDTO.getInfant().getPassengerId(), requestDTO.getInfant().getDestinationAddress(), deleteMap, true);
		}
	}
	
	/**
	 * 
	 * @Description add destination address to the request
	 * @param
	 * @return void
	 * @author fengfeng.jiang
	 */
	private void addDestinationAddress(DataElementsMaster dataElementsMaster, RetrievePnrBooking pnrBooking, Booking booking, String currentPassengerId, UpdateDestinationAddressDTO destinationAddress, Map<String, List<String>> deleteMap, boolean infant) {
		if (destinationAddress == null || pnrBooking == null || CollectionUtils.isEmpty(pnrBooking.getPassengers())) {
			return;
		}
		RetrievePnrPassenger currentPnrPax = PnrResponseParser.getPassengerById(pnrBooking.getPassengers(), currentPassengerId);
		List<RetrievePnrPassengerSegment> currentPnrPassengerSegments = PnrResponseParser.getPassengerSegmentByIds(pnrBooking.getPassengerSegments(), currentPassengerId);
		
		// get all level DOCAs
		List<RetrievePnrAddressDetails> allDesAddresses = new ArrayList<>();
		// customer level DOCAs
		if (currentPnrPax != null && currentPnrPax.getDesAddresses() != null) {
			allDesAddresses.addAll(currentPnrPax.getDesAddresses());
		}
		// product level DOCAs
		allDesAddresses.addAll(currentPnrPassengerSegments.stream()
				.filter(ps -> !CollectionUtils.isEmpty(ps.getDesAddresses()))
				.map(RetrievePnrPassengerSegment::getDesAddresses).reduce(new ArrayList<>(), (all, item) -> {
					all.addAll(item);
					return all;
				}));

		// all ot list
		List<String> otList = allDesAddresses.stream().filter(address -> address.getQualifierId() != null)
				.map(address -> address.getQualifierId().toString()).collect(Collectors.toList());
		
		RetrievePnrAddressDetails prepopulateDestinationAddress = getPrepopulateDestinationAddress(allDesAddresses);
		String country = prepopulateDestinationAddress.getCountry();// get country
		if (!destinationAddress.isBlank()) {
			// TODO String transitFlag = destinationAddress.getTransitFlag();
			String companyId = getFixedCompany();
			String streetName = destinationAddress.getStreetName();
			String city = destinationAddress.getCity();
			String stateCode = destinationAddress.getStateCode();
			String zipCode = destinationAddress.getZipCode();
			
			// DOCA format: type/Country/Street address/City/State/ZIP code//
			DataElementsIndiv dataElementsIndiv = objFactory
					.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
			dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.SSR_SEGMENT));
			dataElementsIndiv.setServiceRequest(
					createServiceRequest(OneAConstants.DOCA, OneAConstants.HK_STATUS, null, companyId,
							String.format(
									infant ? OneAConstants.DESTINATION_ADDRESS_FREETEXT_INFANT_FORMAT
											: OneAConstants.DESTINATION_ADDRESS_FREETEXT_FORMAT,
									BizRulesUtil.nvl(country), streetName, city, stateCode, zipCode)));
			List<ReferencingDetailsType> referencingDetailsTypes = new ArrayList<>();
			referencingDetailsTypes.add(createReference(OneAConstants.PT_QUALIFIER, passengerId));
			dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(referencingDetailsTypes));
			dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
		}

		addOtToMap(deleteMap, CommonConstants.MAP_DESTINATION_ADDRESS_KEY, otList);
	}
	
	/**
	 * add FFP info to the request
	 * @param dataElementsMaster
	 * @param requestDTO
	 * @param pnrBooking
	 * @param deleteMap
	 * @param booking
	 * @param needTransferFFP
	 * @throws BusinessBaseException
	 */
	private void addFFP(DataElementsMaster dataElementsMaster, UpdatePassengerDetailsRequestDTO requestDTO,
			RetrievePnrBooking pnrBooking, Map<String, List<String>> deleteMap, Booking booking, boolean needTransferFFP) throws BusinessBaseException {
		// if segments in requestDTO is empty, then return.
		if (CollectionUtils.isEmpty(requestDTO.getSegments())) {
			return;
		}

		// add FFP info for each segment
		for (UpdateAdultSegmentInfoDTO segment : requestDTO.getSegments()) {
			UpdateFFPInfoDTO ffpInfo = segment.getFfpInfo();
			// if the ffpinfo is null, no need to update
			if (ffpInfo == null) {
				continue;
			}
			// if the segment is flown, throw exception because shouldn't update FFP for the segment
			if (isSegmentFlown(segment.getSegmentId()) || ffpInfo == null) {
				throw new ExpectedException(
						String.format("Can't update FFP to segment %s since it has passed check-in open time",
								segment.getSegmentId()),
						new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
			}
			
			//the passengerSegment in RetrieveBooking
			RetrievePnrPassengerSegment passengerSegment = PnrResponseParser.getPassengerSegmentByIds(
					pnrBooking.getPassengerSegments(), requestDTO.getPassengerId(), segment.getSegmentId());
			
			if (passengerSegment == null) {
				throw new UnexpectedException(
						String.format("Can't find passengerSegment by passengerId: %s, segmentId: %s",
								requestDTO.getPassengerId(), segment.getSegmentId()),
						new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
			}
			//company of segment
			String segmentCompany = getCompanyIdBySt(segment.getSegmentId());
			
			//product level OT number list
			List<String> proLevelOtList = new ArrayList<>();
			if(!CollectionUtils.isEmpty(passengerSegment.getFQTVInfos())){
				proLevelOtList.addAll(passengerSegment.getFQTVInfos().stream().filter(fqtv -> fqtv != null)
						.map(fqtv -> fqtv.getQualifierId().toString()).collect(Collectors.toList()));
			}
			
			//list of all custom level OT number
			List<String> allCusLevelOtList = new ArrayList<>();
			//list of custom level OT number which have same company with segment 
			List<String> cusLevelSameCompanyOtList = new ArrayList<>();
			
			if (!CollectionUtils.isEmpty(pnrPassenger.getFQTVInfos())) {
				allCusLevelOtList.addAll(pnrPassenger.getFQTVInfos().stream().filter(fqtv -> fqtv != null)
						.map(fqtv -> fqtv.getQualifierId().toString()).collect(Collectors.toList()));
				
				cusLevelSameCompanyOtList.addAll(pnrPassenger.getFQTVInfos().stream()
						.filter(fqtv -> fqtv != null 
								&& (fqtv.getFfpCompany().equals(ffpInfo.getCompanyId())
										|| fqtv.getCompanyId().equals(segmentCompany)))
						.map(fqtv -> fqtv.getQualifierId().toString()).collect(Collectors.toList()));
			}

			/** if values in ffpinfo are all empty, then don't need to add, just
				delete related ffpinfo from both product level and custom level */
			if (ffpInfo.isBlank()) {
				addOtToMap(deleteMap, CommonConstants.MAP_FFP_KEY, proLevelOtList);			
				addOtToMap(deleteMap, CommonConstants.MAP_FFP_KEY, allCusLevelOtList);
			}
			/** else if values in ffpinfo not all empty, then add and delete
			 	product level ffpinfo */
			else {

				DataElementsIndiv dataElementsIndiv = objFactory
						.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
				dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.SSR_SEGMENT));
				dataElementsIndiv.setServiceRequest(
						createServiceRequest(OneAConstants.SSR_TYPE_FQTV, null, null, segmentCompany, null));
				dataElementsIndiv.setFrequentTravellerData(createFrequentTravellerData(ffpInfo));
				dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(OneAConstants.PT_QUALIFIER,
						passengerId, OneAConstants.ST_QUALIFIER, segment.getSegmentId()));
				dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
				
				addOtToMap(deleteMap, CommonConstants.MAP_FFP_KEY, proLevelOtList);
				addOtToMap(deleteMap, CommonConstants.MAP_FFP_KEY, cusLevelSameCompanyOtList);
			}		
		}
				
		if (needTransferFFP) { 
			// transform FFP from customer level to product level for the segments not updated in the request
			transferFFPs(dataElementsMaster, requestDTO, deleteMap, booking);
		}
	}

	/**
	 * transform FFP from customer level to product level for the segments not updated in the request
	 * @param dataElementsMaster
	 * @param requestDTO
	 * @param deleteMap
	 * @param booking
	 */
	private void transferFFPs(DataElementsMaster dataElementsMaster, UpdatePassengerDetailsRequestDTO requestDTO,
			Map<String, List<String>> deleteMap, Booking booking) {
		if (booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments())) {
			return;
		}
		
		// passengerSegments of the updating pax
		List<PassengerSegment> passengerSegmentsOfThePax = booking.getPassengerSegments().stream().filter(ps -> Objects.equals(requestDTO.getPassengerId(), ps.getPassengerId()) && ps.getFqtvInfo() != null).collect(Collectors.toList());
		for (PassengerSegment ps : passengerSegmentsOfThePax) {
			if(!checkPsFqtvUpdated(ps, requestDTO) && ps.getFqtvInfo() != null && !ps.getFqtvInfo().isBlank() && !isSegmentFlown(ps.getSegmentId())) { // if FFP of this passengerSegment is not updated in request and segment not flown, then transform it to product level
				//company of segment
				String segmentCompany = getCompanyIdBySt(ps.getSegmentId());
				
				UpdateFFPInfoDTO ffpInfo = new UpdateFFPInfoDTO();
				ffpInfo.setCompanyId(ps.getFqtvInfo().getCompanyId());
				ffpInfo.setMembershipNumber(ps.getFqtvInfo().getMembershipNumber());
				
				DataElementsIndiv dataElementsIndiv = objFactory
						.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
				dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.SSR_SEGMENT));
				dataElementsIndiv.setServiceRequest(
						createServiceRequest(OneAConstants.SSR_TYPE_FQTV, null, null, segmentCompany, null));
				dataElementsIndiv.setFrequentTravellerData(createFrequentTravellerData(ffpInfo));
				dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(OneAConstants.PT_QUALIFIER,
						passengerId, OneAConstants.ST_QUALIFIER, ps.getSegmentId()));
				dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
				
				// delete the customer level fqtv
				if (ps.getFqtvInfo().getQualifierId() != null) {
					List<String> deleteOts = new ArrayList<>();
					deleteOts.add(ps.getFqtvInfo().getQualifierId().toString());
					addOtToMap(deleteMap, CommonConstants.MAP_FFP_KEY, deleteOts);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param dataElementsMaster
	 * @param requestDTO
	 * @param map
	 * @param booking
	 * @param deleteMap 
	 */
	private void transferTravelDocs(DataElementsMaster dataElementsMaster, UpdatePassengerDetailsRequestDTO requestDTO,
			List<String> qualifierIds, Booking booking, RetrievePnrBooking pnrBooking, Map<String, List<String>> deleteMap) {
		if (booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments())
				|| CollectionUtils.isEmpty(booking.getPassengers()) || CollectionUtils.isEmpty(booking.getSegments())) {
			return;
		}
		
		if (!CollectionUtils.isEmpty(requestDTO.getSegments())) {
			Passenger adultPax = booking.getPassengers().stream().filter(pax -> requestDTO.getPassengerId().equals(pax.getPassengerId())).findFirst().orElse(new Passenger());
			List<PassengerSegment> passengerSegmentsOfPax = booking.getPassengerSegments().stream().filter(ps -> requestDTO.getPassengerId().equals(ps.getPassengerId())).collect(Collectors.toList());
			for (PassengerSegment passengerSegment : passengerSegmentsOfPax) {
				addTravelDocForTransfer(dataElementsMaster, pnrBooking, adultPax, passengerSegment, requestDTO.getSegments(), deleteMap, true, false);
				addTravelDocForTransfer(dataElementsMaster, pnrBooking, adultPax, passengerSegment, requestDTO.getSegments(), deleteMap, false, false);
			}
		}
		
		if (requestDTO.getInfant() != null && !StringUtils.isEmpty(requestDTO.getInfant().getPassengerId()) && !CollectionUtils.isEmpty(requestDTO.getInfant().getSegments())) {
			Passenger infantPax = booking.getPassengers().stream().filter(pax -> requestDTO.getInfant().getPassengerId().equals(pax.getPassengerId())).findFirst().orElse(new Passenger());
			List<PassengerSegment> passengerSegmentsOfPax = booking.getPassengerSegments().stream().filter(ps -> requestDTO.getPassengerId().equals(ps.getPassengerId())).collect(Collectors.toList());
			for (PassengerSegment passengerSegment : passengerSegmentsOfPax) {
				addTravelDocForTransfer(dataElementsMaster, pnrBooking, infantPax, passengerSegment, requestDTO.getInfant().getSegments(), deleteMap, true, true);
				addTravelDocForTransfer(dataElementsMaster, pnrBooking, infantPax, passengerSegment, requestDTO.getInfant().getSegments(), deleteMap, false, true);
			}
		}
		
	}

	/**
	 * add travel doc for transfer of passengerSegment
	 * @param dataElementsMaster
	 * @param passenger
	 * @param passengerSegment
	 * @param updateSegments
	 * @param deleteMap
	 * @param isPrimary
	 * @param isInfant
	 */
	private void addTravelDocForTransfer(DataElementsMaster dataElementsMaster, RetrievePnrBooking pnrBooking, Passenger passenger, PassengerSegment passengerSegment, List<? extends UpdateBasicSegmentInfoDTO> updateSegments, Map<String, List<String>> deleteMap, boolean isPrimary, boolean isInfant) {
		if (!checkIsTravelDocUpdated(updateSegments, passengerSegment.getSegmentId(), isPrimary) 
				&& !segmentPassedCheckInOpenTime(passengerSegment.getSegmentId())
				&& ((isPrimary && passengerSegment.getPriTravelDoc() == null && !CollectionUtils.isEmpty(passenger.getPriTravelDocs()))
				|| (!isPrimary && passengerSegment.getSecTravelDoc() == null && !CollectionUtils.isEmpty(passenger.getSecTravelDocs())))) {
			TravelDoc cusLevelTravelDoc;
			List<String> otList = new ArrayList<>();
			
			if (isPrimary) {
				cusLevelTravelDoc = passenger.getPriTravelDocs().get(0);
			} else {
				cusLevelTravelDoc = passenger.getSecTravelDocs().get(0);
			}
			
			String type = cusLevelTravelDoc.getTravelDocumentType();
			String dayOfBirth = StringUtils.isEmpty(cusLevelTravelDoc.getBirthDateDay()) || StringUtils.isEmpty(cusLevelTravelDoc.getBirthDateMonth()) || StringUtils.isEmpty(cusLevelTravelDoc.getBirthDateYear()) ? "" : DateUtil.convertDateFormat(cusLevelTravelDoc.getBirthDateDay()+cusLevelTravelDoc.getBirthDateMonth()+cusLevelTravelDoc.getBirthDateYear(), DateUtil.DATE_PATTERN_DDMMYYYY, DateUtil.DATE_PATTERN_DDMMMYY);
			String expireDate = StringUtils.isEmpty(cusLevelTravelDoc.getExpiryDateDay()) || StringUtils.isEmpty(cusLevelTravelDoc.getExpiryDateMonth()) || StringUtils.isEmpty(cusLevelTravelDoc.getExpiryDateYear()) ? "" : DateUtil.convertDateFormat(cusLevelTravelDoc.getExpiryDateDay()+cusLevelTravelDoc.getExpiryDateMonth()+cusLevelTravelDoc.getExpiryDateYear(), DateUtil.DATE_PATTERN_DDMMYYYY, DateUtil.DATE_PATTERN_DDMMMYY);
			String gender = cusLevelTravelDoc.getGender();
			// if is infant and gender is F/M, add "I" after it
			if (isInfant && StringUtils.isNotEmpty(gender)) {
				gender += OneAConstants.GENDER_INFANT_FLAG;
			}
			//format: doc type/country of issuance/doc number/nationality/DOB/gender/doc expire date/last name/first name
			String freeText = String.format(OneAConstants.TRAVEL_DOCUMENT_DOCS_FREETEXT_FORMAT, cusLevelTravelDoc.getTravelDocumentType(), cusLevelTravelDoc.getCountryOfIssuance(), cusLevelTravelDoc.getTravelDocumentNumber(), cusLevelTravelDoc.getNationality(), dayOfBirth, gender, expireDate, cusLevelTravelDoc.getFamilyName(), cusLevelTravelDoc.getGivenName());
			String ssrType = OneAConstants.DOCS;
			if(!isPrimary && type.equalsIgnoreCase(OneAConstants.TRAVEL_DOCUMENT_TYPE_VISA)) {
				if(isInfant) {
					freeText = String.format(OneAConstants.TRAVEL_DOCUMENT_DOCO_FREETEXT_INFANT_FORMAT, "", type, cusLevelTravelDoc.getTravelDocumentType(), cusLevelTravelDoc.getNationality(), expireDate, cusLevelTravelDoc.getCountryOfIssuance(), PnrResponseParser.SSR_TYPE_DOCO_FREETEXT_INFANT);
				} else {
					freeText = String.format(OneAConstants.TRAVEL_DOCUMENT_DOCO_FREETEXT_FORMAT, "", type, cusLevelTravelDoc.getTravelDocumentNumber(), cusLevelTravelDoc.getNationality(), expireDate, cusLevelTravelDoc.getCountryOfIssuance());					
				}
				ssrType = OneAConstants.DOCO;
			}
			
			//add elementManagementData
			List<ReferencingDetailsType> referencingDetailsTypes = new ArrayList<>();
			referencingDetailsTypes.add(createReference(OneAConstants.PT_QUALIFIER, passengerId));
			referencingDetailsTypes.add(createReference(OneAConstants.ST_QUALIFIER, passengerSegment.getSegmentId()));
			addTravelDoc(ssrType, getFixedCompany(), dataElementsMaster, freeText, referencingDetailsTypes);
			
			// add customer level travel doc to delete map
			if (isPrimary) {
				for (TravelDoc priTravelDoc : passenger.getPriTravelDocs()) {
					otList.add(priTravelDoc.getQualifierId().toString());
				}
			} else {
				for (TravelDoc secTravelDoc : passenger.getSecTravelDocs()) {
					otList.add(secTravelDoc.getQualifierId().toString());
				}
			}
			
			addOtToMap(deleteMap, CommonConstants.MAP_TRAVEL_DOCUMENT_KEY, otList);
			
			// since the country of residence is another SSR DOCA, so need to transfer country of residence as well
			if (isPrimary) {
				addCountryOfResidenceForTransfer(dataElementsMaster, passengerSegment.getPassengerId(), passengerSegment.getSegmentId(), pnrBooking, deleteMap, isInfant);;
			}
		}
	}
	
	/**
	 * add country of residence for transfer of passengerSegment
	 */
	private void addCountryOfResidenceForTransfer(DataElementsMaster dataElementsMaster, String currentPassengerId, String currentSegmentId, RetrievePnrBooking pnrBooking, Map<String, List<String>> map, boolean infant) {
		if (StringUtils.isEmpty(currentPassengerId) || StringUtils.isEmpty(currentSegmentId) || pnrBooking == null) {
			return;
		}
		RetrievePnrPassenger currentPnrPassenger = pnrBooking.getPassengers().stream().filter(pax -> currentPassengerId.equals(pax.getPassengerID())).findFirst().orElse(new RetrievePnrPassenger());
		RetrievePnrPassengerSegment currentPnrPassengerSegment = pnrBooking.getPassengerSegments().stream().filter(ps -> currentPassengerId.equals(ps.getPassengerId()) && currentSegmentId.equals(ps.getSegmentId())).findFirst().orElse(new RetrievePnrPassengerSegment());
		
		if (!CollectionUtils.isEmpty(currentPnrPassenger.getResAddresses())) {
			List<String> otList = new ArrayList<>();
			
			//get the largest OT resaddress from custom level
			RetrievePnrAddressDetails pnrResAddress = currentPnrPassenger.getResAddresses().stream()
			.sorted((resAddress1, resAddress2) -> resAddress2.getQualifierId().compareTo(resAddress1.getQualifierId()))
			.findFirst().orElse(null);
			
			if (pnrResAddress == null) {
				return;
			}
			
			for(RetrievePnrAddressDetails addressDetails:currentPnrPassengerSegment.getResAddresses()) {
				otList.add(addressDetails.getQualifierId().toString());
			}
			String streetAddress = pnrResAddress.getStreet();//get street address
			String city = pnrResAddress.getCity();//get city
			String state = pnrResAddress.getStateCode();//get state
			String zipCode = pnrResAddress.getZipCode();//get zip code
			String countryCode = pnrResAddress.getCountry();//get country code
			String companyId = getFixedCompany();
			//add elementManagementData
			DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
			dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.SSR_SEGMENT));
			//DOCA format: type/Country/Street address/City/State/ZIP code
			//add serviceRequest
			dataElementsIndiv.setServiceRequest(createServiceRequest(OneAConstants.DOCA,OneAConstants.HK_STATUS,"1",companyId,
					String.format(infant?OneAConstants.COUNTRY_OF_RESIDENCE_FREETEXT_INFANT_FORMAT:OneAConstants.COUNTRY_OF_RESIDENCE_FREETEXT_FORMAT, BizRulesUtil.nvl(countryCode),BizRulesUtil.nvl(streetAddress),BizRulesUtil.nvl(city),BizRulesUtil.nvl(state),BizRulesUtil.nvl(zipCode))));
			dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
			//add referenceForDataElement
			List<ReferencingDetailsType> referencingDetailsTypes = new ArrayList<>();
			referencingDetailsTypes.add(createReference(OneAConstants.PT_QUALIFIER, passengerId));
			referencingDetailsTypes.add(createReference(OneAConstants.ST_QUALIFIER, currentSegmentId));
			dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(referencingDetailsTypes));
			addOtToMap(map, CommonConstants.MAP_COUNTRY_OF_RESIDENCE_KEY, otList);
		}
		
		
	}

	/**
	 * check if the travel doc is updated
	 * @param segments
	 * @param segmentId
	 * @param isPrimary
	 * @return boolean
	 */
	private boolean checkIsTravelDocUpdated(List<? extends UpdateBasicSegmentInfoDTO> segments, String segmentId, boolean isPrimary) {
		if (isPrimary) {
			return segments.stream().anyMatch(seg -> !StringUtils.isEmpty(seg.getSegmentId()) && seg.getSegmentId().equals(segmentId) && seg.getPrimaryTravelDoc() != null);
		} else {
			return segments.stream().anyMatch(seg -> !StringUtils.isEmpty(seg.getSegmentId()) && seg.getSegmentId().equals(segmentId) && seg.getSecondaryTravelDoc() != null);
		}
	}
	
	/**
	 * check if the FFP of the passengerSegment is updated in the request
	 * @param ps
	 * @param requestDTO
	 * @return boolean
	 */
	private boolean checkPsFqtvUpdated(PassengerSegment ps, UpdatePassengerDetailsRequestDTO requestDTO) {
		if (ps == null || StringUtils.isEmpty(ps.getPassengerId()) || StringUtils.isEmpty(ps.getSegmentId()) || StringUtils.isEmpty(requestDTO.getPassengerId())) {
			return false;
		}
		
		if (ps.getPassengerId().equals(requestDTO.getPassengerId())) {
			return Optional.ofNullable(requestDTO.getSegments()).orElse(Collections.emptyList()).stream()
					.anyMatch(seg -> ps.getSegmentId().equals(seg.getSegmentId()) && seg.getFfpInfo() != null);
		}
		
		return false;
	}

	/**
	 * @Description check if the segment is flown
	 * @param segmentId
	 * @return
	 */
	private boolean isSegmentFlown(String segmentId) {
		Segment seg = segments.stream().filter(s -> s != null && s.getSegmentID().equals(segmentId)).findFirst().orElse(null);
		if(seg == null){
			return false;
		}
		return BooleanUtils.isTrue(seg.isFlown());
	}

	/**
	 * 
	* @Description create FrequentTravellerData bean by ffpInfo
	* @param ffpInfo
	* @return FrequentTravellerInformationTypeU
	* @author haiwei.jia
	 */
	private FrequentTravellerInformationTypeU createFrequentTravellerData(UpdateFFPInfoDTO ffpInfo) {
		FrequentTravellerInformationTypeU frequentTravellerData = objFactory.createFrequentTravellerInformationTypeU();
		FrequentTravellerIdentificationTypeU frequentTraveller = objFactory.createFrequentTravellerIdentificationTypeU();
		frequentTraveller.setCompanyId(ffpInfo.getCompanyId());
		frequentTraveller.setMembershipNumber(ffpInfo.getCompanyId()+ffpInfo.getMembershipNumber());
		frequentTravellerData.setFrequentTraveller(frequentTraveller);
		return frequentTravellerData;
	}

	/**
	 * 
	 * @Description create the last DataElementsIndiv bean 
	 * @param
	 * @return ReferencingDetailsType
	 * @author fengfeng.jiang
	 */
	private void createLastDataElementsIndiv(DataElementsMaster dataElementsMaster) {
		DataElementsIndiv dataElementsIndiv1 = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
		dataElementsIndiv1.setElementManagementData(createElementManagementData("RF"));
		dataElementsIndiv1.setFreetextData(createFreetextData("3",OneAConstants.P22_TYPE,OneAConstants.CXMB));
		dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv1);
	}
	
	/**
	 * 
	 * @Description add OT number to corresponding list
	 * @param
	 * @return void
	 * @author fengfeng.jiang
	 */
	private void addOtToMap(Map<String, List<String>> deleteMap, String key,List<String> otList) {
		if(CollectionUtils.isEmpty(otList)) {
			return;
		}
		
		List<String> tempOtList = deleteMap.get(key);
		if(tempOtList == null) {
			tempOtList = otList;
			deleteMap.put(key, new ArrayList<String>(new HashSet<String>(tempOtList)));
		}else {
			tempOtList.addAll(otList);
			tempOtList = new ArrayList<String>(new HashSet<String>(tempOtList));
			deleteMap.put(key, tempOtList);
		}
	}
	
	/**
	 * 
	* @Description check wheather passenger has primary travel doc info
	* @param
	* @return boolean
	* @author fengfeng.jiang
	 */
/*	private boolean isPriTravelDocExits(RetrievePnrPassenger passenger,List<RetrievePnrPassengerSegment> passengerSegments) {
		if(!CollectionUtils.isEmpty(passenger.getPriTravelDocs()))
			return true;
		for(RetrievePnrPassengerSegment passengerSegment:passengerSegments) {
			if(!CollectionUtils.isEmpty(passengerSegment.getPriTravelDocs())) {
				return true;
			}
		}
		return false;
	}*/
	
	
	/**
	 * 
	* @Description get prepopulate country Of residence
	* @param pnrPassengerSegment
	* @return RetrievePnrAddressDetails
	 */
	private RetrievePnrAddressDetails getPrepopulateCountryOfResidence(RetrievePnrPassengerSegment pnrPassengerSegment) {
		RetrievePnrAddressDetails addressDetails = new RetrievePnrAddressDetails();
		//get the largest OT residence address from product level
		Optional<RetrievePnrAddressDetails> pnrResAddress = pnrPassengerSegment.getResAddresses().stream()
				.sorted((resAddress1, resAddress2) -> resAddress2.getQualifierId().compareTo(resAddress1.getQualifierId()))
				.findFirst();
		if(pnrResAddress.isPresent()){
			addressDetails = pnrResAddress.get();
		} else {
			//get residence country code form custom level
			//get the largest OT resaddress from custom level
			pnrResAddress = pnrPassenger.getResAddresses().stream()
					.sorted((resAddress1, resAddress2) -> resAddress2.getQualifierId().compareTo(resAddress1.getQualifierId()))
					.findFirst();
			if(pnrResAddress.isPresent()){
				addressDetails = pnrResAddress.get();
			}
		}
		return addressDetails;
	}
	
	/**
	 * 
	 * @Description get prepopulate destination address
	 * @param
	 * @return RetrievePnrAddressDetails
	 * @author fengfeng.jiang
	 * @param desAddresses 
	 * @param booking 
	 * @param passengerId 
	 */
	private RetrievePnrAddressDetails getPrepopulateDestinationAddress(List<RetrievePnrAddressDetails> desAddresses) {
		RetrievePnrAddressDetails prepopulateDestinationAddress = new RetrievePnrAddressDetails();
		if (CollectionUtils.isEmpty(desAddresses)) {
			return prepopulateDestinationAddress;
		}

		prepopulateDestinationAddress = desAddresses.stream()
				.sorted((ktn1, ktn2) -> ktn2.getQualifierId().compareTo(ktn1.getQualifierId())).findFirst()
				.orElse(new RetrievePnrAddressDetails());
		
		return prepopulateDestinationAddress;
	}
	
	/**
	 * 
	 * @Description get common company id
	 * @param
	 * @return boolean
	 * @author fengfeng.jiang
	 */
	private String getFixedCompany() {
		return "YY";
	}
	
	/**
	 * 
	 * @Description get company id by segment id
	 * @param
	 * @return boolean
	 * @author fengfeng.jiang
	 */
	private String getCompanyIdBySt(String segmentId) {
		String companyId = "";
		if(pnrSegments != null) {
			for(RetrievePnrSegment segment:pnrSegments) {
				if(segmentId.equals(segment.getSegmentID())) {
					companyId = segment.getMarketCompany();
					break;
				}
			}
		}
		
		return companyId;
	}
	
	/**
	 * 
	 * @Description get company id by segment id
	 * @param
	 * @return boolean
	 * @author fengfeng.jiang
	 */
	private String getCompanyIdForSegments() {
		String companyId = "";
		if(pnrSegments != null) {
			Set<String> companyIdSet = segments.stream().filter(s ->!s.isFlown() && StringUtils.isNotEmpty(s.getOperateCompany())).map(Segment::getOperateCompany).collect(Collectors.toSet());
			for(String tempCompanyId:companyIdSet) {
				if(OneAConstants.COMPANY_CX.equals(tempCompanyId)) {
					companyId = OneAConstants.COMPANY_CX;
					break;
				}else if(OneAConstants.COMPANY_KA.equals(tempCompanyId)){
					companyId = OneAConstants.COMPANY_KA;
				}else if(StringUtils.isEmpty(companyId)){
					companyId = tempCompanyId;
				}
			}
		}
		
		return companyId;
	}
}
