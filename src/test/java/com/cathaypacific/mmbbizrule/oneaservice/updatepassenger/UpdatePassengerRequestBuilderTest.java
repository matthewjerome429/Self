package com.cathaypacific.mmbbizrule.oneaservice.updatepassenger;

import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateDestinationAddressDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateEmailDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateEmergencyContactDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateFFPInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateInfantDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdatePassengerDetailsRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdatePhoneInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateAdultSegmentInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateBasicSegmentInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateTravelDocDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateTsDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrContactPhone;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrFFPInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTravelDoc;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.DummySegmentTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ElementManagementSegmentType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.FreeTextQualificationType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.FrequentTravellerIdentificationTypeU;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.FrequentTravellerInformationTypeU;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.LongFreeTextType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ObjectFactory;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.OptionalPNRActionsType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ReferenceInfoType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ReferencingDetailsType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ReservationControlInformationDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ReservationControlInformationTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.SpecialRequirementsDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.SpecialRequirementsTypeDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements.DataElementsMaster;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements.DataElementsMaster.DataElementsIndiv;

@RunWith(MockitoJUnitRunner.class)
public class UpdatePassengerRequestBuilderTest {
	@InjectMocks
	UpdatePassengerRequestBuilder updatePassengerRequestBuilder;
	@Mock
	ObjectFactory objFactory;
	@Test
	public void test() throws BusinessBaseException {
		UpdatePassengerDetailsRequestDTO requestDTO=new UpdatePassengerDetailsRequestDTO();
		requestDTO.setPassengerId("1");
		requestDTO.setRloc("MN520");
		
		UpdateDestinationAddressDTO destinationAddress=new UpdateDestinationAddressDTO();
		destinationAddress.setCity("HF");
		destinationAddress.setStreetName("SL");
		destinationAddress.setStateCode("1");
		destinationAddress.setZipCode("1");
		requestDTO.setDestinationAddress(destinationAddress);
		
		UpdateEmergencyContactDTO emergencyContact=new UpdateEmergencyContactDTO();
		emergencyContact.setContactName("M");
		emergencyContact.setPhoneCountryNumber("00237");
		emergencyContact.setPhoneNo("123456");
		requestDTO.setEmergencyContact(emergencyContact);
		
		UpdatePhoneInfoDTO phoneInfo=new UpdatePhoneInfoDTO();
		phoneInfo.setPhoneNo("123456");
		phoneInfo.setPhoneCountryNumber("010");
		requestDTO.setPhoneInfo(phoneInfo);
		
		UpdateEmailDTO email=new UpdateEmailDTO();
		email.setEmail("123@qq.com");
		requestDTO.setEmail(email);
		
		BigInteger b=new BigInteger("123");
		UpdateTsDTO ktn=new UpdateTsDTO();
		ktn.setNumber("123");
		ktn.setQualifierId(b);
		requestDTO.setKtn(ktn);
		
		UpdateTsDTO redress = new UpdateTsDTO();
		redress.setNumber("321");
		requestDTO.setRedress(redress);
		
		List<UpdateAdultSegmentInfoDTO> segments=new ArrayList<>();
		UpdateAdultSegmentInfoDTO segment=new UpdateAdultSegmentInfoDTO();
		segment.setSegmentId("1");


		segment.setPrimaryTravelDoc(null);
		segment.setSecondaryTravelDoc(null);
		
		UpdateFFPInfoDTO ffpInfo=new UpdateFFPInfoDTO();
		ffpInfo.setCompanyId("1");
		ffpInfo.setMembershipNumber("123456");
		segment.setFfpInfo(ffpInfo);
		segments.add(segment);
		requestDTO.setSegments(segments);
		
		List<UpdateBasicSegmentInfoDTO> infantSegments=new ArrayList<>();
		UpdateBasicSegmentInfoDTO infantSegment=new UpdateAdultSegmentInfoDTO();
		infantSegment.setSegmentId("1");

		infantSegment.setPrimaryTravelDoc(null);
		infantSegment.setSecondaryTravelDoc(null);

		infantSegments.add(infantSegment);

		UpdateInfantDTO infant=new UpdateInfantDTO();
		infant.setPassengerId("1");
		infant.setSegments(infantSegments);
		UpdateTsDTO ktn1=new UpdateTsDTO();
		ktn1.setNumber("123");
		ktn1.setQualifierId(b);
		infant.setKtn(ktn1);
		requestDTO.setInfant(infant);
		
		RetrievePnrBooking booking=new RetrievePnrBooking();
		List<RetrievePnrPassenger> passengers=new ArrayList<>();
		RetrievePnrPassenger passenger=new RetrievePnrPassenger();
		passenger.setPassengerID("1");
		List<RetrievePnrContactPhone> contactPhones=new ArrayList<>();
		RetrievePnrContactPhone contactPhone=new RetrievePnrContactPhone();
		contactPhone.setCompanyId("1");
		contactPhone.setQualifierId(b);
		passenger.setContactPhones(contactPhones);
		passengers.add(passenger);
		booking.setPassengers(passengers);
		
		List<RetrievePnrSegment> segmentsss=new ArrayList<>();
		RetrievePnrSegment segmentss=new RetrievePnrSegment();
		segmentss.setDestPort("CX");
		segmentss.setMarketCompany("CX");
		segmentsss.add(segmentss);
		booking.setSegments(segmentsss);
		
		List<RetrievePnrPassengerSegment> passengerSegments=new ArrayList<>();
		RetrievePnrPassengerSegment passengerSegment=new RetrievePnrPassengerSegment();
		passengerSegment.setPassengerId("1");
		passengerSegment.setSegmentId("1");
		
		List<RetrievePnrTravelDoc> priTravelDocs=new ArrayList<>();
		RetrievePnrTravelDoc priTravelDoc=new RetrievePnrTravelDoc();
		priTravelDoc.setQualifierId(b);
		priTravelDoc.setBirthDateDay("02");
		priTravelDoc.setExpiryDateYear("2019");
		priTravelDoc.setExpiryDateDay("02");
		priTravelDoc.setExpiryDateMonth("05");
		priTravelDoc.setBirthDateMonth("04");
		priTravelDoc.setBirthDateYear("2018");
		priTravelDoc.setFamilyName("TEST");
		priTravelDoc.setGivenName("LIAN");
		priTravelDoc.setQualifierId(b);
		priTravelDoc.setNationality("CN");
		priTravelDoc.setTravelDocumentNumber("147");
		priTravelDoc.setTravelDocumentType("ZF");
		priTravelDoc.setCountryOfIssuance("123");
		priTravelDocs.add(priTravelDoc);
		passengerSegment.setPriTravelDocs(priTravelDocs);
		passengerSegments.add(passengerSegment);
		booking.setPassengerSegments(passengerSegments);
		
		Map<String, List<String>> deleteMap=new HashMap<>();
		
		UpdatePassengerRequestBuilder updatePassengerRequestBuilder = new UpdatePassengerRequestBuilder();
		
		PNRAddMultiElements pnrAddMultiElements=updatePassengerRequestBuilder.buildRequest(requestDTO, booking, new Booking(), deleteMap, false, false);
		Assert.assertEquals("SSR", pnrAddMultiElements.getDataElementsMaster().getDataElementsIndiv().get(0).getElementManagementData().getSegmentName());
		int docoCount = 0;
		int docaCount = 0;
		for (DataElementsIndiv dataElement : pnrAddMultiElements.getDataElementsMaster().getDataElementsIndiv()) {
			if (dataElement.getServiceRequest() != null && "DOCA".equals(dataElement.getServiceRequest().getSsr().getType())) {
				Assert.assertEquals("D--SL-HF-1-1", dataElement.getServiceRequest().getSsr().getFreetext().get(0));
				Assert.assertEquals(1, dataElement.getReferenceForDataElement().getReference().size());
				Assert.assertEquals("PT", dataElement.getReferenceForDataElement().getReference().get(0).getQualifier());
				Assert.assertEquals("1", dataElement.getReferenceForDataElement().getReference().get(0).getNumber());
				docaCount++;
			} else if (dataElement.getServiceRequest() != null && "DOCO".equals(dataElement.getServiceRequest().getSsr().getType())) {
				Assert.assertTrue("/K/123///".equals(dataElement.getServiceRequest().getSsr().getFreetext().get(0)) || "/K/123////I".equals(dataElement.getServiceRequest().getSsr().getFreetext().get(0)) || "/R/321///".equals(dataElement.getServiceRequest().getSsr().getFreetext().get(0)));
				Assert.assertEquals(1, dataElement.getReferenceForDataElement().getReference().size());
				Assert.assertEquals("PT", dataElement.getReferenceForDataElement().getReference().get(0).getQualifier());
				Assert.assertEquals("1", dataElement.getReferenceForDataElement().getReference().get(0).getNumber());
				docoCount++;
			}
		}
		Assert.assertEquals(1, docaCount);
		Assert.assertEquals(3, docoCount);
	}
	@Test
	public void test1() throws BusinessBaseException {
		UpdatePassengerDetailsRequestDTO requestDTO=new UpdatePassengerDetailsRequestDTO();
		requestDTO.setPassengerId("1");
		requestDTO.setRloc("MN520");
		
		UpdateDestinationAddressDTO destinationAddress=new UpdateDestinationAddressDTO();
		destinationAddress.setCity("HF");
		destinationAddress.setStreetName("SL");
		destinationAddress.setStateCode("1");
		destinationAddress.setZipCode("1");
		requestDTO.setDestinationAddress(destinationAddress);
		
		UpdateEmergencyContactDTO emergencyContact=new UpdateEmergencyContactDTO();
		emergencyContact.setContactName("M");
		emergencyContact.setPhoneCountryNumber("00237");
		emergencyContact.setPhoneNo("123456");
		requestDTO.setEmergencyContact(emergencyContact);
		
		UpdatePhoneInfoDTO phoneInfo=new UpdatePhoneInfoDTO();
		phoneInfo.setPhoneNo("123456");
		phoneInfo.setPhoneCountryNumber("010");
		requestDTO.setPhoneInfo(phoneInfo);
		UpdateEmailDTO email=new UpdateEmailDTO();
		email.setEmail("123@qq.com");
		requestDTO.setEmail(email);
		
		BigInteger b=new BigInteger("123");
		
		UpdateTsDTO ktn=new UpdateTsDTO();
		ktn.setNumber("123");
		ktn.setQualifierId(b);
		requestDTO.setKtn(ktn);

		List<UpdateAdultSegmentInfoDTO> segments=new ArrayList<>();
		UpdateAdultSegmentInfoDTO segment=new UpdateAdultSegmentInfoDTO();
		segment.setSegmentId("1");

		UpdateTravelDocDTO primaryTravelDoc=new UpdateTravelDocDTO();
		primaryTravelDoc.setCountryOfIssuance("147");
		primaryTravelDoc.setFamilyName("TEST");
		primaryTravelDoc.setGivenName("LAIN");
		primaryTravelDoc.setNationality("CN");
		primaryTravelDoc.setTravelDocumentNumber("123");
		primaryTravelDoc.setTravelDocumentType("ZF");
		primaryTravelDoc.setDateOfExpire("2018-04-02");
		primaryTravelDoc.setDateOfBirth("2000-01-01");
		segment.setPrimaryTravelDoc(primaryTravelDoc);
		UpdateFFPInfoDTO ffpInfo=new UpdateFFPInfoDTO();
		ffpInfo.setCompanyId("1");
		ffpInfo.setMembershipNumber("123456");
		segment.setFfpInfo(ffpInfo);
		segments.add(segment);
		requestDTO.setSegments(segments);

		List<UpdateBasicSegmentInfoDTO> infantSegments=new ArrayList<>();
		UpdateBasicSegmentInfoDTO infantSegment=new UpdateAdultSegmentInfoDTO();
		infantSegment.setSegmentId("1");

		UpdateTravelDocDTO primaryTravelDoc1=new UpdateTravelDocDTO();
		primaryTravelDoc1.setCountryOfIssuance("147");
		primaryTravelDoc1.setFamilyName("TEST");
		primaryTravelDoc1.setGivenName("LAIN");
		primaryTravelDoc1.setNationality("CN");
		primaryTravelDoc1.setTravelDocumentNumber("123");
		primaryTravelDoc1.setTravelDocumentType("ZF");
		primaryTravelDoc1.setDateOfExpire("2018-04-02");
		primaryTravelDoc1.setDateOfBirth("2000-01-01");
		infantSegment.setPrimaryTravelDoc(primaryTravelDoc1);
		infantSegments.add(infantSegment);
		UpdateInfantDTO infant=new UpdateInfantDTO();
		infant.setPassengerId("1");
		infant.setSegments(infantSegments);
		UpdateTsDTO ktn1=new UpdateTsDTO();
		ktn1.setNumber("123");
		ktn1.setQualifierId(b);
		infant.setKtn(ktn1);
		requestDTO.setInfant(infant);
		
		RetrievePnrBooking booking=new RetrievePnrBooking();
		List<RetrievePnrPassenger> passengers=new ArrayList<>();
		RetrievePnrPassenger passenger=new RetrievePnrPassenger();
		passenger.setPassengerID("1");
		List<RetrievePnrContactPhone> contactPhones=new ArrayList<>();
		RetrievePnrContactPhone contactPhone=new RetrievePnrContactPhone();
		contactPhone.setCompanyId("1");
		contactPhone.setQualifierId(b);
		passenger.setContactPhones(contactPhones);
		List<RetrievePnrFFPInfo> fQTVInfos =new ArrayList<>();
		RetrievePnrFFPInfo fQTVInfo=new RetrievePnrFFPInfo();
		fQTVInfo.setQualifierId(b);
		fQTVInfo.setFfpCompany("1");
		fQTVInfos.add(fQTVInfo);
		passenger.setFQTVInfos(fQTVInfos);
		passengers.add(passenger);
		booking.setPassengers(passengers);
		
		List<RetrievePnrSegment> segmentsss=new ArrayList<>();
		RetrievePnrSegment segmentss=new RetrievePnrSegment();
		segmentss.setDestPort("CX");
		segmentss.setMarketCompany("CX");
		segmentsss.add(segmentss);
		booking.setSegments(segmentsss);
		
		List<RetrievePnrPassengerSegment> passengerSegments=new ArrayList<>();
		RetrievePnrPassengerSegment passengerSegment=new RetrievePnrPassengerSegment();
		passengerSegment.setPassengerId("1");
		passengerSegment.setSegmentId("1");
		List<RetrievePnrTravelDoc> priTravelDocs=new ArrayList<>();
		RetrievePnrTravelDoc priTravelDoc=new RetrievePnrTravelDoc();
		priTravelDoc.setQualifierId(b);
		priTravelDoc.setBirthDateDay("02");
		priTravelDoc.setBirthDateMonth("04");
		priTravelDoc.setBirthDateYear("2018");
		priTravelDoc.setFamilyName("TEST");
		priTravelDoc.setGivenName("LIAN");
		priTravelDoc.setNationality("CN");
		priTravelDoc.setTravelDocumentNumber("147");
		priTravelDoc.setTravelDocumentType("ZF");
		priTravelDoc.setCountryOfIssuance("123");
		priTravelDocs.add(priTravelDoc);
		passengerSegment.setPriTravelDocs(priTravelDocs);
		passengerSegments.add(passengerSegment);
		booking.setPassengerSegments(passengerSegments);
		
		Map<String, List<String>> deleteMap=new HashMap<>();
		
		PNRAddMultiElements request=new PNRAddMultiElements();
		when(objFactory.createPNRAddMultiElements()).thenReturn(request);
		DataElementsMaster dataElementsMaster =new DataElementsMaster();
		PNRAddMultiElements.DataElementsMaster.DataElementsIndiv dataElementsIndiv=new PNRAddMultiElements.DataElementsMaster.DataElementsIndiv();
		
		ElementManagementSegmentType value=new ElementManagementSegmentType();
		value.setSegmentName("CPA");
		
		ReferencingDetailsType reference=new ReferencingDetailsType();
		value.setReference(reference);
		
		dataElementsIndiv.setElementManagementData(value);
		dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
		when(objFactory.createPNRAddMultiElementsDataElementsMaster()).thenReturn(dataElementsMaster);
		
		ReservationControlInformationDetailsTypeI reservation=new ReservationControlInformationDetailsTypeI();
		when(objFactory.createReservationControlInformationDetailsTypeI()).thenReturn(reservation);
		
		OptionalPNRActionsType pnrActions=new OptionalPNRActionsType();
		when(objFactory.createOptionalPNRActionsType()).thenReturn(pnrActions);
		
		FrequentTravellerInformationTypeU frequentTravellerData=new FrequentTravellerInformationTypeU();
		when(objFactory.createFrequentTravellerInformationTypeU()).thenReturn(frequentTravellerData);
		
		FrequentTravellerIdentificationTypeU frequentTraveller=new FrequentTravellerIdentificationTypeU();
		when(objFactory.createFrequentTravellerIdentificationTypeU()).thenReturn(frequentTraveller);
		
		ElementManagementSegmentType elementManagementData=new ElementManagementSegmentType();
		when(objFactory.createElementManagementSegmentType()).thenReturn(elementManagementData);
		
		LongFreeTextType freetextData=new LongFreeTextType();
		when(objFactory.createLongFreeTextType()).thenReturn(freetextData);
		
		SpecialRequirementsDetailsTypeI serviceRequest=new SpecialRequirementsDetailsTypeI();
		when(objFactory.createSpecialRequirementsDetailsTypeI()).thenReturn(serviceRequest);
		
		SpecialRequirementsTypeDetailsTypeI ssr=new SpecialRequirementsTypeDetailsTypeI();
		when(objFactory.createSpecialRequirementsTypeDetailsTypeI()).thenReturn(ssr);
		
		DataElementsIndiv dataElementsIndiv2=new DataElementsIndiv();
		when(objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv()).thenReturn(dataElementsIndiv2);
		
		ReferenceInfoType referenceForDataElement=new ReferenceInfoType();
		when(objFactory.createReferenceInfoType()).thenReturn(referenceForDataElement);
		
	    FreeTextQualificationType freetextDetail=new FreeTextQualificationType();
		when( objFactory.createFreeTextQualificationType()).thenReturn(freetextDetail);
			
		DataElementsIndiv dataElementsIndiv1=new DataElementsIndiv();
		when(objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv()).thenReturn(dataElementsIndiv1);
		
		when(objFactory.createReferencingDetailsType()).thenReturn(reference);
		
		ReservationControlInformationTypeI reservationInfo=new ReservationControlInformationTypeI();
		when(objFactory.createReservationControlInformationTypeI()).thenReturn(reservationInfo);
		
		DummySegmentTypeI marker=new DummySegmentTypeI();
		when(objFactory.createDummySegmentTypeI()).thenReturn(marker);
		PNRAddMultiElements pNRAddMultiElements=updatePassengerRequestBuilder.buildRequest(requestDTO, booking, new Booking(), deleteMap, false, false);
		Assert.assertEquals("CPA", pNRAddMultiElements.getDataElementsMaster().getDataElementsIndiv().get(0).getElementManagementData().getSegmentName());
	}
	
	@Test
	public void testEmptySections() throws BusinessBaseException {
		UpdatePassengerDetailsRequestDTO requestDTO = new UpdatePassengerDetailsRequestDTO();
		requestDTO.setPassengerId("1");
		
		UpdateEmailDTO emailDTO = new UpdateEmailDTO();
		emailDTO.setEmail("tom@gg.com");
		emailDTO.setConvertToOlssContactInfo(false);
		requestDTO.setEmail(emailDTO);
		
		UpdateTsDTO ktnDTO = new UpdateTsDTO();
		ktnDTO.setNumber("99");
		ktnDTO.setQualifierId(null);
		requestDTO.setKtn(ktnDTO);
		
		PNRAddMultiElements request=new PNRAddMultiElements();
		when(objFactory.createPNRAddMultiElements()).thenReturn(request);
		DataElementsMaster dataElementsMaster =new DataElementsMaster();
		PNRAddMultiElements.DataElementsMaster.DataElementsIndiv dataElementsIndiv=new PNRAddMultiElements.DataElementsMaster.DataElementsIndiv();
		
		ElementManagementSegmentType value=new ElementManagementSegmentType();
		value.setSegmentName("CPA");
		
		ReferencingDetailsType reference=new ReferencingDetailsType();
		value.setReference(reference);
		
		dataElementsIndiv.setElementManagementData(value);
		dataElementsMaster.getDataElementsIndiv().add(dataElementsIndiv);
		when(objFactory.createPNRAddMultiElementsDataElementsMaster()).thenReturn(dataElementsMaster);
		
		ReservationControlInformationDetailsTypeI reservation=new ReservationControlInformationDetailsTypeI();
		when(objFactory.createReservationControlInformationDetailsTypeI()).thenReturn(reservation);
		
		OptionalPNRActionsType pnrActions=new OptionalPNRActionsType();
		when(objFactory.createOptionalPNRActionsType()).thenReturn(pnrActions);
		
		FrequentTravellerInformationTypeU frequentTravellerData=new FrequentTravellerInformationTypeU();
		when(objFactory.createFrequentTravellerInformationTypeU()).thenReturn(frequentTravellerData);
		
		FrequentTravellerIdentificationTypeU frequentTraveller=new FrequentTravellerIdentificationTypeU();
		when(objFactory.createFrequentTravellerIdentificationTypeU()).thenReturn(frequentTraveller);
		
		ElementManagementSegmentType elementManagementData=new ElementManagementSegmentType();
		when(objFactory.createElementManagementSegmentType()).thenReturn(elementManagementData);
		
		LongFreeTextType freetextData=new LongFreeTextType();
		when(objFactory.createLongFreeTextType()).thenReturn(freetextData);
		
		SpecialRequirementsDetailsTypeI serviceRequest=new SpecialRequirementsDetailsTypeI();
		when(objFactory.createSpecialRequirementsDetailsTypeI()).thenReturn(serviceRequest);
		
		SpecialRequirementsTypeDetailsTypeI ssr=new SpecialRequirementsTypeDetailsTypeI();
		when(objFactory.createSpecialRequirementsTypeDetailsTypeI()).thenReturn(ssr);
		
		DataElementsIndiv dataElementsIndiv2=new DataElementsIndiv();
		when(objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv()).thenReturn(dataElementsIndiv2);
		
		ReferenceInfoType referenceForDataElement=new ReferenceInfoType();
		when(objFactory.createReferenceInfoType()).thenReturn(referenceForDataElement);
		
	    FreeTextQualificationType freetextDetail=new FreeTextQualificationType();
		when( objFactory.createFreeTextQualificationType()).thenReturn(freetextDetail);
			
		DataElementsIndiv dataElementsIndiv1=new DataElementsIndiv();
		when(objFactory.createPNRAddMultiElementsDataElementsMasterDataElementsIndiv()).thenReturn(dataElementsIndiv1);
		
		when(objFactory.createReferencingDetailsType()).thenReturn(reference);
		
		ReservationControlInformationTypeI reservationInfo=new ReservationControlInformationTypeI();
		when(objFactory.createReservationControlInformationTypeI()).thenReturn(reservationInfo);
		
		DummySegmentTypeI marker=new DummySegmentTypeI();
		when(objFactory.createDummySegmentTypeI()).thenReturn(marker);
		
		BigInteger b=new BigInteger("123");
		
		RetrievePnrBooking booking=new RetrievePnrBooking();
		List<RetrievePnrPassenger> passengers=new ArrayList<>();
		RetrievePnrPassenger passenger=new RetrievePnrPassenger();
		passenger.setPassengerID("1");
		List<RetrievePnrContactPhone> contactPhones=new ArrayList<>();
		RetrievePnrContactPhone contactPhone=new RetrievePnrContactPhone();
		contactPhone.setCompanyId("1");
		contactPhone.setQualifierId(b);
		passenger.setContactPhones(contactPhones);
		List<RetrievePnrFFPInfo> fQTVInfos =new ArrayList<>();
		RetrievePnrFFPInfo fQTVInfo=new RetrievePnrFFPInfo();
		fQTVInfo.setQualifierId(b);
		fQTVInfo.setFfpCompany("1");
		fQTVInfos.add(fQTVInfo);
		passenger.setFQTVInfos(fQTVInfos);
		passengers.add(passenger);
		booking.setPassengers(passengers);
		
		List<RetrievePnrSegment> segmentsss=new ArrayList<>();
		RetrievePnrSegment segmentss=new RetrievePnrSegment();
		segmentss.setDestPort("CX");
		segmentss.setMarketCompany("CX");
		segmentsss.add(segmentss);
		booking.setSegments(segmentsss);
		
		List<RetrievePnrPassengerSegment> passengerSegments=new ArrayList<>();
		RetrievePnrPassengerSegment passengerSegment=new RetrievePnrPassengerSegment();
		passengerSegment.setPassengerId("1");
		passengerSegment.setSegmentId("1");
		List<RetrievePnrTravelDoc> priTravelDocs=new ArrayList<>();
		RetrievePnrTravelDoc priTravelDoc=new RetrievePnrTravelDoc();
		priTravelDoc.setQualifierId(b);
		priTravelDoc.setBirthDateDay("02");
		priTravelDoc.setBirthDateMonth("04");
		priTravelDoc.setBirthDateYear("2018");
		priTravelDoc.setFamilyName("TEST");
		priTravelDoc.setGivenName("LIAN");
		priTravelDoc.setNationality("CN");
		priTravelDoc.setTravelDocumentNumber("147");
		priTravelDoc.setTravelDocumentType("ZF");
		priTravelDoc.setCountryOfIssuance("123");
		priTravelDocs.add(priTravelDoc);
		passengerSegment.setPriTravelDocs(priTravelDocs);
		passengerSegments.add(passengerSegment);
		booking.setPassengerSegments(passengerSegments);

		Map<String, List<String>> deleteMap=new HashMap<>();
		
		PNRAddMultiElements pNRAddMultiElements = updatePassengerRequestBuilder.buildRequest(requestDTO, booking, new Booking(), deleteMap, false, false);
	
		List<DataElementsIndiv> dataElementsIndivs = pNRAddMultiElements.getDataElementsMaster().getDataElementsIndiv();
		
		Assert.assertEquals(4, dataElementsIndivs.size());
		
		for(DataElementsIndiv dataElementIndiv : dataElementsIndivs) {
			if ("SSR".equals(dataElementIndiv.getElementManagementData().getSegmentName())) {
				if ("CTCE".equals(dataElementIndiv.getServiceRequest().getSsr().getType())) {
					String freeText = dataElementIndiv.getServiceRequest().getSsr().getFreetext().get(0);
					Assert.assertEquals("tom//gg.com/XX", freeText);
				} else if ("DOCO".equals(dataElementIndiv.getServiceRequest().getSsr().getType())) {
					String freeText = dataElementIndiv.getServiceRequest().getSsr().getFreetext().get(0);
					Assert.assertEquals("/K/99///", freeText);
				}
			}
		}
		
	}

}
