package com.cathaypacific.mmbbizrule.oneaservice.pnraddelement;

import java.math.BigInteger;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.dto.request.oneaoperation.AddRmDetail;
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

public class AddRemarkBuilder {
	
	private ObjectFactory objFactory = new ObjectFactory();
	
	public PNRAddMultiElements buildRequest(String rloc, Session session, String... freeTexts) {
		PNRAddMultiElements request = objFactory.createPNRAddMultiElements();
		
		if (session == null ||SessionStatus.START.getStatus().equals(session.getStatus())) {
			/* set reservationInfo */
			request.setReservationInfo(createReservationInfo(rloc));
		}
		OptionalPNRActionsType pnrActions = objFactory.createOptionalPNRActionsType();
		/*set pnrActions*/
		String actionCode = "11";
		if(session != null && !SessionStatus.END.getStatus().equals(session.getStatus()) ) {
			actionCode = "0";
		} 
		pnrActions.getOptionCode().add(new BigInteger(actionCode));
		request.setPnrActions(pnrActions);
		
		/*set dataElementsMaster*/
		DataElementsMaster dataElementsMaster = objFactory.createPNRAddMultiElementsDataElementsMaster();
		request.setDataElementsMaster(dataElementsMaster);
		
		/*add marker1 to dataElementsMaster*/
		dataElementsMaster.setMarker1(objFactory.createDummySegmentTypeI());
		
		/*add elementManagementData & miscellaneousRemarks*/
		for(String freeText : freeTexts) {
			DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
			dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.RM_SEGMENT));
			dataElementsIndiv.setMiscellaneousRemark(createMiscellaneousRemark(freeText, OneAConstants.REMARK_TYPE_RM));
			dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
		}
		
		/*add the last dataElementsIndiv to dataElementsMaster*/
		createLastDataElementsIndiv(dataElementsMaster);
		
		return request;
	}
	
	public PNRAddMultiElements buildRmRequest(String rloc, List<AddRmDetail> addRmDetails, Session session) {
		PNRAddMultiElements request = objFactory.createPNRAddMultiElements();
		
		if (session == null ||SessionStatus.START.getStatus().equals(session.getStatus())) {
			/* set reservationInfo */
			request.setReservationInfo(createReservationInfo(rloc));
		}
		OptionalPNRActionsType pnrActions = objFactory.createOptionalPNRActionsType();
		/*set pnrActions*/
		String actionCode = "11";
		if(session != null && !SessionStatus.END.getStatus().equals(session.getStatus()) ) {
			actionCode = "0";
		} 
		pnrActions.getOptionCode().add(new BigInteger(actionCode));
		request.setPnrActions(pnrActions);
		
		// add dataElementsMaster
		DataElementsMaster dataElementsMaster = objFactory.createPNRAddMultiElementsDataElementsMaster();
		request.setDataElementsMaster(dataElementsMaster);
		// add marker1 to dataElementsMaster
		dataElementsMaster.setMarker1(objFactory.createDummySegmentTypeI());
		
		for(AddRmDetail addRmDetail : addRmDetails) {
			dataElementsMaster.getDataElementsIndiv().add(createRmDataElementsIndiv(addRmDetail));
		}
		
		// add the last dataElementsIndiv to dataElementsMaster
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
		
		dataElementsIndiv.setMiscellaneousRemark(createMiscellaneousRemark(addRmDetail.getFreeText(), OneAConstants.REMARK_TYPE_RM));
		dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(addRmDetail.getPassegnerIds(), addRmDetail.getSegmentIds()));
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
	 * create miscellaneousRemark bean
	 * @param freeText
	 * @return ElementManagementSegmentType
	 */
	private MiscellaneousRemarksType createMiscellaneousRemark(String freeText, String type) {
		MiscellaneousRemarksType miscellaneousRemarksType = objFactory.createMiscellaneousRemarksType();
		MiscellaneousRemarkType miscellaneousRemarkType = objFactory.createMiscellaneousRemarkType();
		miscellaneousRemarkType.setType(type);
		miscellaneousRemarkType.setFreetext(freeText);
		miscellaneousRemarksType.setRemarks(miscellaneousRemarkType);
		return miscellaneousRemarksType;
	}
	
	/**
	 * create the last DataElementsIndiv bean 
	 * @param
	 * @return ReferencingDetailsType
	 */
	private void createLastDataElementsIndiv(DataElementsMaster dataElementsMaster) {
		DataElementsIndiv dataElementsIndiv1 = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
		dataElementsIndiv1.setElementManagementData(createElementManagementData("RF"));
		dataElementsIndiv1.setFreetextData(createFreetextData("3",OneAConstants.P22_TYPE,OneAConstants.CXMB));
		dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv1);
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

}