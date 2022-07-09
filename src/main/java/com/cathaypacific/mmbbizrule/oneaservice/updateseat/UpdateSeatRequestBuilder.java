package com.cathaypacific.mmbbizrule.oneaservice.updateseat;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.AswrInfo;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.ExtraSeatDetail;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.PaxSeatDetail;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.RemarkInfo;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.UpdateSeatRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.XlwrInfo;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.header.SessionStatus;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ElementManagementSegmentType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.FreeTextQualificationType;
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
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.SeatEntityType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.SeatRequestType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.SeatRequierementsDataType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.SpecialRequirementsDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.SpecialRequirementsTypeDetailsTypeI;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc this builder is used to convert front end DTO to oneA request model
 * @author fengfeng.jiang
 * @date Jan 29, 2018 5:28:29 PM
 * @version V1.0
 */
public class UpdateSeatRequestBuilder {
	
	private ObjectFactory objFactory = new ObjectFactory();
	
	/**
	 * 
	* @Description convert front end DTO to oneA request model
	* @param
	* @param map 
	* @return PNRAddMultiElements
	* @author fengfeng.jiang
	 * @param aswrInfos 
	 * @throws ExpectedException 
	 */
	public PNRAddMultiElements buildRequest(UpdateSeatRequestDTO requestDTO, Session session, List<XlwrInfo> xlwrInfos, List<RemarkInfo> remarkInfos, List<AswrInfo> aswrInfos){
		PNRAddMultiElements request = objFactory.createPNRAddMultiElements();
		
		/*add rloc*/
		String rloc = requestDTO.getRloc();
		if(session != null && session.getStatus() != SessionStatus.START.getStatus()) {
			rloc = "";
		}
		request.setReservationInfo(createReservationInfo(rloc));
		
		/*add pnrActions*/
		String actionCode = "11";
		if(session != null && session.getStatus() != SessionStatus.END.getStatus()) {
			actionCode = "0";
		}
		request.setPnrActions(createPnrActions(actionCode));
		
		/*add dataElementsMaster*/
		DataElementsMaster dataElementsMaster = objFactory.createPNRAddMultiElementsDataElementsMaster();
		request.setDataElementsMaster(dataElementsMaster);
		
		//add marker1 to dataElementsMaster
		dataElementsMaster.setMarker1(objFactory.createDummySegmentTypeI());
		
		//add seat or seat preference to dataElementsMaster
		createSeatOrPreference(dataElementsMaster, requestDTO);
		
		//add SK XLMR to dataElementsMaster
		createSkXLWR(dataElementsMaster, xlwrInfos);
		
		//add SK ASWR to dataElementsMaster
		createSkASWR(dataElementsMaster, aswrInfos);
		
		//add remark to dataElementsMaster
		createRemark(dataElementsMaster, remarkInfos);
		
		//add the last dataElementsIndiv to dataElementsMaster
		createLastDataElementsIndiv(dataElementsMaster);
		
		return request;
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
	* @Description
	* @param
	* @return void
	* @author fengfeng.jiang
	 * @throws ExpectedException 
	 */
	private void createSeatOrPreference(DataElementsMaster dataElementsMaster, UpdateSeatRequestDTO requestDTO){
		if(!CollectionUtils.isEmpty(requestDTO.getPaxSeatDetails())){
			for(PaxSeatDetail details: requestDTO.getPaxSeatDetails()) {
				List<String> passengerIds = getPassengerIds(details);
				List<String> segmentIds = new ArrayList<>();
				segmentIds.add(requestDTO.getSegmentId());
				String seatNo = details.getSeatNo();
				List<String> seatNoList = getSeatNoList(details);
				String preference = details.getSeatPreference();
				
				if(StringUtils.isNotBlank(seatNo) || StringUtils.isNotBlank(preference)) {
					//build dataElementsIndiv
					DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
					dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.STR_SEGMENT));
					dataElementsIndiv.setSeatGroup(createSeatGroup(seatNoList,preference));
					dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(passengerIds, segmentIds));
					dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
				}
			}
		}
	}

	private List<String> getPassengerIds(PaxSeatDetail details) {
		List<String> passengerIds = new ArrayList<>();
		passengerIds.add(details.getPassengerID());
		if(!CollectionUtils.isEmpty(details.getExtraSeats())) {
			for(ExtraSeatDetail extraSeat : details.getExtraSeats()) {
				if(!StringUtils.isEmpty(extraSeat.getSeatNo()) && !StringUtils.isEmpty(extraSeat.getPassengerId())) {
					passengerIds.add(extraSeat.getPassengerId());
				}
			}
		}
		return passengerIds;
	}

	/**
	 * get seat number list from details
	 * @param details
	 * @return List<String>
	 */
	private List<String> getSeatNoList(PaxSeatDetail details) {
		List<String> seatNoList = new ArrayList<>();
		if(!StringUtils.isEmpty(details.getSeatNo())) {
			seatNoList.add(details.getSeatNo());
		}
		if(!CollectionUtils.isEmpty(details.getExtraSeats())) {
			for(ExtraSeatDetail extraSeat : details.getExtraSeats()) {
				if(!StringUtils.isEmpty(extraSeat.getSeatNo())) {
					seatNoList.add(extraSeat.getSeatNo());
				}
			}
		}
		return seatNoList;
	}
	
	/**
	 * 
	 * @Description
	 * @param
	 * @return void
	 * @author fengfeng.jiang
	 * @throws ExpectedException 
	 */
	private void createSkXLWR(DataElementsMaster dataElementsMaster, List<XlwrInfo> xlwrInfos){
		if (!CollectionUtils.isEmpty(xlwrInfos)) {
			for(XlwrInfo xlwr:xlwrInfos) {
				String passengerId = xlwr.getPassengerId();
				String segmentId = xlwr.getSegmentId();
				if(StringUtils.isNotBlank(passengerId) || StringUtils.isNotBlank(segmentId)) {
					//build dataElementsIndiv
					DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
					dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.SK_SEGMENT));
					dataElementsIndiv.setServiceRequest(createServiceRequest(OneAConstants.SK_TYPE_XLWR, OneAConstants.HK_STATUS, "1", xlwr.getCompanyId(), "WAIVED"));
					List<String> passengerIds = new ArrayList<>();
					List<String> segmentIds = new ArrayList<>();
					passengerIds.add(passengerId);
					segmentIds.add(segmentId);
					dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(passengerIds, segmentIds));
					dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
				}
			}
		}
	}
	
	/**
	 * create Sk ASWR
	 * @param dataElementsMaster
	 * @param aswrInfos
	 */
	private void createSkASWR(DataElementsMaster dataElementsMaster, List<AswrInfo> aswrInfos) {
		if (!CollectionUtils.isEmpty(aswrInfos)) {
			for(AswrInfo aswrInfo : aswrInfos) {
				String paxId = aswrInfo.getPassengerId();
				String segId = aswrInfo.getSegmentId();
				if(StringUtils.isNotBlank(paxId) || StringUtils.isNotBlank(segId)) {
					//build dataElementsIndiv
					DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
					dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.SK_SEGMENT));
					dataElementsIndiv.setServiceRequest(createServiceRequest(OneAConstants.SK_TYPE_ASWR, OneAConstants.HK_STATUS, "1", aswrInfo.getCompanyId(), "WAIVED"));
					List<String> passengerIds = new ArrayList<>();
					List<String> segmentIds = new ArrayList<>();
					passengerIds.add(paxId);
					segmentIds.add(segId);
					dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(passengerIds, segmentIds));
					dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
				}
			}
		}
	}
	
	/**
	 * 
	 * @Description
	 * @param
	 * @return void
	 * @author fengfeng.jiang
	 * @throws ExpectedException 
	 */
	private void createRemark(DataElementsMaster dataElementsMaster, List<RemarkInfo> remarkInfos){
		if(CollectionUtils.isEmpty(remarkInfos)) return;
		
		for(RemarkInfo remarkInfo:remarkInfos) {
			String passengerId = remarkInfo.getPassengerId();
			String segmentId = remarkInfo.getSegmentId();
			if(StringUtils.isNotBlank(passengerId) || StringUtils.isNotBlank(segmentId)) {
				//build dataElementsIndiv
				DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
				dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.RM_SEGMENT));
				dataElementsIndiv.setMiscellaneousRemark(createMiscellaneousRemark(passengerId, segmentId));
				List<String> passengerIds = new ArrayList<>();
				List<String> segmentIds = new ArrayList<>();
				passengerIds.add(passengerId);
				segmentIds.add(segmentId);
				dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(passengerIds, segmentIds));
				dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
			}
		}
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
	 * @Description create miscellaneousRemark bean
	 * @param
	 * @return ElementManagementSegmentType
	 * @author fengfeng.jiang
	 */
	private MiscellaneousRemarksType createMiscellaneousRemark(String passengerId, String segmentId) {
		MiscellaneousRemarksType miscellaneousRemarksType = objFactory.createMiscellaneousRemarksType();
		MiscellaneousRemarkType miscellaneousRemarkType = objFactory.createMiscellaneousRemarkType();
		miscellaneousRemarkType.setType("RM");
		miscellaneousRemarkType.setFreetext(OneAConstants.REMARK_FREETEXT_OF_VOLUNTARY_CHANGE_FROM_EXL_TO_NORMAL);
		miscellaneousRemarksType.setRemarks(miscellaneousRemarkType);
		return miscellaneousRemarksType;
	}
	
	/**
	 * 
	 * @Description create elementManagementData bean
	 * @param
	 * @return SeatEntityType
	 * @author fengfeng.jiang
	 * @throws ExpectedException 
	 */
	private SeatEntityType createSeatGroup(List<String> seatNoList, String preference){
		SeatRequestType seatRequestType = objFactory.createSeatRequestType();
		if(!CollectionUtils.isEmpty(seatNoList)){
			//add seat number
			for(String seatNo : seatNoList) {
				SeatRequierementsDataType seatRequierement = objFactory.createSeatRequierementsDataType();
				seatRequierement.setData(seatNo);
				seatRequestType.getSpecial().add(seatRequierement);
			}
		}else if(!StringUtils.isEmpty(preference)) {
			//add seat preference
			SeatRequierementsDataType seatRequierement = objFactory.createSeatRequierementsDataType();
			seatRequierement.getSeatType().add(preference);
			seatRequestType.getSpecial().add(seatRequierement);
		}

		SeatEntityType seatGroup = objFactory.createSeatEntityType();
		seatGroup.setSeatRequest(seatRequestType);
		
		return seatGroup;
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
	 * @Description create referenceForDataElement bean
	 * @param
	 * @return void
	 * @author fengfeng.jiang
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
		if(!StringUtils.isEmpty(quantity)) {
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
}
