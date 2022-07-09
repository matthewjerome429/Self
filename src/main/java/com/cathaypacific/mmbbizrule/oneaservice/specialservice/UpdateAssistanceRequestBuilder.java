package com.cathaypacific.mmbbizrule.oneaservice.specialservice;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.dto.request.specialservice.SsrInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.specialservice.UpdateAssistanceInfoDTO;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.header.SessionStatus;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ElementManagementSegmentType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.FreeTextQualificationType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.LongFreeTextType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ObjectFactory;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.OptionalPNRActionsType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ReferenceInfoType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ReferencingDetailsType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements.DataElementsMaster;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements.DataElementsMaster.DataElementsIndiv;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ReservationControlInformationDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ReservationControlInformationTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.SpecialRequirementsDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.SpecialRequirementsTypeDetailsTypeI;

@Component
public class UpdateAssistanceRequestBuilder {

	private ObjectFactory objFactory = new ObjectFactory();
	
	public PNRAddMultiElements buildRequest(String rloc, List<UpdateAssistanceInfoDTO> updateAssistanceInfo, Session session) throws ExpectedException {
		PNRAddMultiElements request = objFactory.createPNRAddMultiElements();
		
		if (session == null ||SessionStatus.START.getStatus().equals(session.getStatus())) {
			/* set reservationInfo */
			request.setReservationInfo(createReservationInfo(rloc));
		}
		/*add pnrActions*/
		String actionCode = "11";
		if(session != null && !SessionStatus.END.getStatus().equals(session.getStatus()) ) {
			actionCode = "0";
		} 
		request.setPnrActions(createPnrActions(actionCode));
		
		// add dataElementsMaster
		DataElementsMaster dataElementsMaster = objFactory.createPNRAddMultiElementsDataElementsMaster();
		request.setDataElementsMaster(dataElementsMaster);
		
		//add marker1 to dataElementsMaster
		dataElementsMaster.setMarker1(objFactory.createDummySegmentTypeI());
		
		//add meal to dataElementsMaster
		for(UpdateAssistanceInfoDTO updateAssistanceInfoDTO: updateAssistanceInfo) {
			createAssistanceElementsIndiv(dataElementsMaster, updateAssistanceInfoDTO);
		}
		
		//add the last dataElementsIndiv to dataElementsMaster
		createLastDataElementsIndiv(dataElementsMaster);
		
		return request;
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
	
	private void createAssistanceElementsIndiv(DataElementsMaster dataElementsMaster, UpdateAssistanceInfoDTO updateAssistanceInfo) throws ExpectedException {
		if(!CollectionUtils.isEmpty(updateAssistanceInfo.getUpdateSsrList())){

			// Build SSR date element
			for(SsrInfoDTO ssrInfo: updateAssistanceInfo.getUpdateSsrList()) {
				if(StringUtils.isNotBlank(ssrInfo.getSsrCode())) {
					DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
					dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.SSR_SEGMENT));
					dataElementsIndiv.setServiceRequest(createServiceRequest(ssrInfo.getSsrCode(), ssrInfo.getAdditionalInfo()));
					dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(updateAssistanceInfo.getPassengerId(), updateAssistanceInfo.getSegmentId()));
					dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
				}
			}
		}
	}
	
	private ElementManagementSegmentType createElementManagementData(String segmentName) {
		ElementManagementSegmentType elementManagementData = objFactory.createElementManagementSegmentType();
		elementManagementData.setSegmentName(segmentName);
		
		return elementManagementData;
	}
	
	private SpecialRequirementsDetailsTypeI createServiceRequest(String ssrCode, String additionalInfo) {
		SpecialRequirementsDetailsTypeI serviceRequest = objFactory.createSpecialRequirementsDetailsTypeI();
		serviceRequest.setSsr(createSsr(ssrCode,additionalInfo));
		
		return serviceRequest;
	}
	
	private SpecialRequirementsTypeDetailsTypeI createSsr(String ssrCode, String additionalInfo) {
		SpecialRequirementsTypeDetailsTypeI ssr = objFactory.createSpecialRequirementsTypeDetailsTypeI();
		ssr.setType(ssrCode);
		
		if(!StringUtils.isEmpty(additionalInfo)) {
			ssr.getFreetext().add(additionalInfo);				
		}
		return ssr;
	}
	
	private ReferenceInfoType createReferenceForDataElement(String passengerId, String segmentId) {
		ReferenceInfoType referenceForDataElement = objFactory.createReferenceInfoType();
		referenceForDataElement.getReference().add(createReference(OneAConstants.PT_QUALIFIER, passengerId));
		referenceForDataElement.getReference().add(createReference(OneAConstants.ST_QUALIFIER, segmentId));
		return referenceForDataElement;
	}
	
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
}
