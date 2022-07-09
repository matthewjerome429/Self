package com.cathaypacific.mmbbizrule.oneaservice.meal;

import java.math.BigInteger;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mmbbizrule.constant.MealConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.dto.request.meal.MealRequestDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.addMeal.AddMealDetailDTO;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.header.SessionStatus;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ElementManagementSegmentType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.FreeTextQualificationType;
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

@Service
public class MealRequestBuilder {
	
	private ObjectFactory objFactory = new ObjectFactory();

	public PNRAddMultiElements buildRequest(String rloc, List<AddMealDetailDTO> requestDtos, Session session) throws ExpectedException{
		PNRAddMultiElements request = objFactory.createPNRAddMultiElements();
		
		/*add rloc*/
		if(session != null && session.getStatus() != SessionStatus.START.getStatus()) {
			rloc = "";
		}
		request.setReservationInfo(createReservationInfo(rloc));
		
		/*add pnrActions*/
		request.setPnrActions(createPnrActions("11"));
		
		/*add dataElementsMaster*/
		DataElementsMaster dataElementsMaster = objFactory.createPNRAddMultiElementsDataElementsMaster();
		request.setDataElementsMaster(dataElementsMaster);
		
		//add marker1 to dataElementsMaster
		dataElementsMaster.setMarker1(objFactory.createDummySegmentTypeI());
		
		//add meal to dataElementsMaster
		for(AddMealDetailDTO requestDto: requestDtos) {
			createMealDataElementsIndiv(dataElementsMaster, requestDto);
		}
		
		//add the last dataElementsIndiv to dataElementsMaster
		createLastDataElementsIndiv(dataElementsMaster);
		
		return request;
	}
	
	private ReservationControlInformationTypeI createReservationInfo(String rloc) {
		ReservationControlInformationDetailsTypeI reservation = objFactory.createReservationControlInformationDetailsTypeI();
		reservation.setControlNumber(rloc);
		ReservationControlInformationTypeI reservationInfo = objFactory.createReservationControlInformationTypeI();
		reservationInfo.setReservation(reservation);
		
		return reservationInfo;
	}
	
	private OptionalPNRActionsType createPnrActions(String optionCode) {
		OptionalPNRActionsType pnrActions = objFactory.createOptionalPNRActionsType();
		pnrActions.getOptionCode().add(new BigInteger(optionCode));
		
		return pnrActions;
	}

	private void createMealDataElementsIndiv(DataElementsMaster dataElementsMaster, AddMealDetailDTO requestDto) throws ExpectedException {
		if(!CollectionUtils.isEmpty(requestDto.getPaxMealDetails())){
			
			String segmentId = requestDto.getSegmentId();
			String companyId = requestDto.getCompanyId();
			for(MealRequestDetailDTO details: requestDto.getPaxMealDetails()) {
				String passengerId = details.getPassengerId();
				passengerId = passengerId.replaceAll(MealConstants.PASSENGER_INFANT_ID_SUFFIX, "");
				String mealCode = details.getMealCode();
				int quantity = details.getQuantity();
				String freeText = details.getFreeText();

				if(StringUtils.isNotBlank(mealCode) || quantity != 0 || StringUtils.isNotBlank(companyId)) {
					//build dataElementsIndiv
					DataElementsIndiv dataElementsIndiv = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
					dataElementsIndiv.setElementManagementData(createElementManagementData(OneAConstants.SSR_SEGMENT));
					dataElementsIndiv.setServiceRequest(createServiceRequest(mealCode, quantity, companyId, freeText));
					dataElementsIndiv.setReferenceForDataElement(createReferenceForDataElement(passengerId, segmentId));
					dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
				}
			}
		}
	}
	
	private SpecialRequirementsDetailsTypeI createServiceRequest(String mealCode, int quantity, String companyId, String freeText) {
		SpecialRequirementsDetailsTypeI serviceRequest = objFactory.createSpecialRequirementsDetailsTypeI();
		serviceRequest.setSsr(createSsr(mealCode,quantity,companyId,freeText));
		
		return serviceRequest;
	}
	
	private SpecialRequirementsTypeDetailsTypeI createSsr(String mealCode, int quantity, String companyId, String freeText) {
		SpecialRequirementsTypeDetailsTypeI ssr = objFactory.createSpecialRequirementsTypeDetailsTypeI();
		ssr.setType(mealCode);
		if(quantity != 0) {
			ssr.setQuantity(new BigInteger(""+quantity));
		}
		
		if(!StringUtils.isEmpty(companyId)) {
			ssr.setCompanyId(companyId);
		}
		
		if(!StringUtils.isEmpty(freeText)) {
			ssr.getFreetext().add(freeText);
		}
		return ssr;
	}

	private ElementManagementSegmentType createElementManagementData(String segmentName) {
		ElementManagementSegmentType elementManagementData = objFactory.createElementManagementSegmentType();
		elementManagementData.setSegmentName(segmentName);
		
		return elementManagementData;
	}

	private ReferencingDetailsType createReference(String qualifier, String number) {
		ReferencingDetailsType reference = objFactory.createReferencingDetailsType();
		reference.setQualifier(qualifier);
		reference.setNumber(number);
		return reference;
	}

	private ReferenceInfoType createReferenceForDataElement(String passengerId, String segmentId) {
		ReferenceInfoType referenceForDataElement = objFactory.createReferenceInfoType();
		referenceForDataElement.getReference().add(createReference(OneAConstants.PT_QUALIFIER, passengerId));
		referenceForDataElement.getReference().add(createReference(OneAConstants.ST_QUALIFIER, segmentId));
		return referenceForDataElement;
	}

	private void createLastDataElementsIndiv(DataElementsMaster dataElementsMaster) {
		DataElementsIndiv dataElementsIndiv1 = objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv();
		dataElementsIndiv1.setElementManagementData(createElementManagementData("RF"));
		dataElementsIndiv1.setFreetextData(createFreetextData("3",OneAConstants.P22_TYPE,OneAConstants.CXMB));
		dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv1);
	}
	
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
