package com.cathaypacific.mmbbizrule.oneaservice.pnradd;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.constant.CommonConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateDestinationAddressDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateEmailDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateEmergencyContactDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateInfantDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdatePassengerDetailsRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdatePhoneInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateAdultSegmentInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateBasicSegmentInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateTravelDocDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateTsDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SegmentStatus;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrAddressDetails;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrContactPhone;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEmail;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEmrContactInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTravelDoc;
import com.cathaypacific.mmbbizrule.oneaservice.updatepassenger.UpdatePassengerRequestBuilder;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements.DataElementsMaster.DataElementsIndiv;

@RunWith(MockitoJUnitRunner.class)
public class AddPnrRequestBuilderTest {
	private UpdatePassengerDetailsRequestDTO request = new UpdatePassengerDetailsRequestDTO();
	private RetrievePnrBooking booking = new RetrievePnrBooking();

	@Before
	public void setUp(){
		/** initialize request **/
		request.setRloc("OXWKYR");
		request.setPassengerId("2");
		
		//add segments to request
		List<UpdateAdultSegmentInfoDTO> segments = new ArrayList<>();
		UpdateAdultSegmentInfoDTO segment = new UpdateAdultSegmentInfoDTO();
		segment.setSegmentId("1");
		
		//add primary travel document to segment
		UpdateTravelDocDTO priTravelDoc = new UpdateTravelDocDTO();
		priTravelDoc.setCountryOfIssuance("USA");
		priTravelDoc.setDateOfExpire("2020-12-31");
		priTravelDoc.setDateOfBirth("2000-01-01");
		priTravelDoc.setGender("M");
		priTravelDoc.setFamilyName("zhang");
		priTravelDoc.setGivenName("san");
		priTravelDoc.setNationality("USA");
		priTravelDoc.setTravelDocumentNumber("123456");
		priTravelDoc.setTravelDocumentType("P");
		segment.setPrimaryTravelDoc(priTravelDoc);
		

		segments.add(segment);
		request.setSegments(segments);
		
		//add KTN 
		UpdateTsDTO ktn = new UpdateTsDTO();
		ktn.setNumber("1234567");
		ktn.setQualifierId(new BigInteger("10"));
		request.setKtn(ktn);
		
		//add contact info to request
		UpdatePhoneInfoDTO phoneInfo = new UpdatePhoneInfoDTO();
		phoneInfo.setPhoneCountryNumber("86");
		phoneInfo.setPhoneNo("18888888888");
		request.setPhoneInfo(phoneInfo);

		UpdateEmailDTO email = new UpdateEmailDTO();
		email.setEmail("zhang_san@sina_cn");
		request.setEmail(email);
		
		
		//add emergency info to request
		UpdateEmergencyContactDTO emergencyContact = new UpdateEmergencyContactDTO();
		emergencyContact.setContactName("wang wu");
		emergencyContact.setPhoneCountryNumber("852");
		emergencyContact.setPhoneNo("18666666666");
		request.setEmergencyContact(emergencyContact);
		
		//add destination info to request
		UpdateDestinationAddressDTO destinationAddress = new UpdateDestinationAddressDTO();
		destinationAddress.setCity("sky");
		destinationAddress.setStateCode("AAA");
		destinationAddress.setStreetName("huang he da dao");
		destinationAddress.setZipCode("A66666");
		request.setDestinationAddress(destinationAddress);
		
		//add infant info to request
		UpdateInfantDTO infant = new UpdateInfantDTO();
		infant.setPassengerId("2I");
		
		//add segments to infant info
		List<UpdateBasicSegmentInfoDTO> infantSegments = new ArrayList<>();
		UpdateBasicSegmentInfoDTO infantSegment = new UpdateAdultSegmentInfoDTO();
		infantSegment.setSegmentId("1");
		
		//add primary travel document to infant segment
		UpdateTravelDocDTO infantPriTravelDoc = new UpdateTravelDocDTO();
		infantPriTravelDoc.setCountryOfIssuance("USA");
		infantPriTravelDoc.setCountryOfResidence("CHN");
		infantPriTravelDoc.setDateOfExpire("2020-12-31");
		infantPriTravelDoc.setDateOfBirth("2000-01-02");
		infantPriTravelDoc.setGender("M");
		infantPriTravelDoc.setFamilyName("li");
		infantPriTravelDoc.setGivenName("si");
		infantPriTravelDoc.setNationality("USA");
		infantPriTravelDoc.setTravelDocumentNumber("654321");
		infantPriTravelDoc.setTravelDocumentType("P");
		
		infantSegment.setPrimaryTravelDoc(infantPriTravelDoc);
		infantSegments.add(infantSegment);
		infant.setSegments(infantSegments);
		
		//add destination info to infant
		UpdateDestinationAddressDTO infantDestinationAddress = new UpdateDestinationAddressDTO();
		infantDestinationAddress.setCity("sky");
		infantDestinationAddress.setStateCode("AAA");
		infantDestinationAddress.setStreetName("huang he da dao");
		infantDestinationAddress.setZipCode("A66666");
		infant.setDestinationAddress(infantDestinationAddress);
		
		request.setInfant(infant);
		
		/** initialize booking **/
		booking.setOneARloc("OXWKYR");
		
		//add passenger to booking
		List<RetrievePnrPassenger> passengers = new ArrayList<>();
		RetrievePnrPassenger passenger = new RetrievePnrPassenger();
		passenger.setPassengerID("2");
		
		//add contact phones to passenger
		List<RetrievePnrContactPhone> contactPhones = new ArrayList<>();
		RetrievePnrContactPhone contactPhone = new RetrievePnrContactPhone();
		contactPhone.setQualifierId(new BigInteger("1"));
		contactPhones.add(contactPhone);
		passenger.setContactPhones(contactPhones);
		
		//add emails to passenger
		List<RetrievePnrEmail> emails = new ArrayList<>();
		RetrievePnrEmail emailPnr = new RetrievePnrEmail();
		emailPnr.setQualifierId(new BigInteger("2"));
		emails.add(emailPnr);
		passenger.setEmails(emails);
		
		//add emergency contact info to passenger
		List<RetrievePnrEmrContactInfo> emrContactInfos = new ArrayList<>();
		RetrievePnrEmrContactInfo emrContactInfo = new RetrievePnrEmrContactInfo();
		emrContactInfo.setQualifierId(new BigInteger("3"));
		emrContactInfos.add(emrContactInfo);
		passenger.setEmrContactInfos(emrContactInfos);
		
		passengers.add(passenger);
		booking.setPassengers(passengers);
		
		//add passenger segments to booking
		List<RetrievePnrPassengerSegment> passengerSegments = new ArrayList<>();
		RetrievePnrPassengerSegment passengerSegment = new RetrievePnrPassengerSegment();
		passengerSegment.setPassengerId("2");
		passengerSegment.setSegmentId("1");
		
		//add primary travel segment to passenger segment
		List<RetrievePnrTravelDoc> priTravelDocs = new ArrayList<>();
		RetrievePnrTravelDoc priTravelDocPnr = new RetrievePnrTravelDoc();
		priTravelDocPnr.setQualifierId(new BigInteger("4"));
		priTravelDocs.add(priTravelDocPnr);
		passengerSegment.setPriTravelDocs(priTravelDocs);
		
		//add country of residence to passenger segment
		List<RetrievePnrAddressDetails> resAddresses = new ArrayList<>();
		RetrievePnrAddressDetails resAddress = new RetrievePnrAddressDetails();
		resAddress.setQualifierId(new BigInteger("5"));
		resAddresses.add(resAddress);
		passengerSegment.setResAddresses(resAddresses);
		
		//add destination address to passenger segment
		List<RetrievePnrAddressDetails> desAddresses = new ArrayList<>();
		RetrievePnrAddressDetails desAddress = new RetrievePnrAddressDetails();
		desAddress.setQualifierId(new BigInteger("6"));
		desAddresses.add(desAddress);
		passengerSegment.setDesAddresses(desAddresses);
		
		passengerSegments.add(passengerSegment);
		
		//add infant passenger segments to booking
		RetrievePnrPassengerSegment infantPassengerSegment = new RetrievePnrPassengerSegment();
		infantPassengerSegment.setPassengerId("2I");
		infantPassengerSegment.setSegmentId("1");
		
		//add primary travel segment to passenger segment
		List<RetrievePnrTravelDoc> infantPriTravelDocs = new ArrayList<>();
		RetrievePnrTravelDoc infantPriTravelDocPnr = new RetrievePnrTravelDoc();
		infantPriTravelDocPnr.setQualifierId(new BigInteger("7"));
		infantPriTravelDocs.add(infantPriTravelDocPnr);
		infantPassengerSegment.setPriTravelDocs(infantPriTravelDocs);
		
		//add country of residence to passenger segment
		List<RetrievePnrAddressDetails> infantResAddresseses = new ArrayList<>();
		RetrievePnrAddressDetails infantResAddresses = new RetrievePnrAddressDetails();
		infantResAddresses.setQualifierId(new BigInteger("8"));
		infantResAddresseses.add(infantResAddresses);
		infantPassengerSegment.setResAddresses(infantResAddresseses);
		
		//add destination address to passenger segment
		List<RetrievePnrAddressDetails> infantDesAddresses = new ArrayList<>();
		RetrievePnrAddressDetails infantDesAddress = new RetrievePnrAddressDetails();
		infantDesAddress.setQualifierId(new BigInteger("9"));
		infantDesAddresses.add(infantDesAddress);
		infantPassengerSegment.setDesAddresses(infantDesAddresses);
		
		passengerSegments.add(infantPassengerSegment);
		booking.setPassengerSegments(passengerSegments);
	}
	
	@Test
	public void buildRequestTest() throws BusinessBaseException{
		Map<String, List<String>> deleteMap = new HashMap<>();
		
		UpdatePassengerRequestBuilder builder = new UpdatePassengerRequestBuilder();
		
		List<RetrievePnrSegment> segmentsss=new ArrayList<>();
		RetrievePnrSegment segmentss=new RetrievePnrSegment();
		segmentss.setDestPort("CX");
		segmentss.setMarketCompany("CX");
		segmentsss.add(segmentss);
		booking.setSegments(segmentsss);
		
		Booking booking1 = new Booking();
		booking1.setSegments(new ArrayList<>());
		Segment segment = new Segment();
		segment.setSegmentID("1");
		segment.setSegmentStatus(new SegmentStatus());
		segment.getSegmentStatus().setStatus(FlightStatusEnum.CONFIRMED);
		booking1.getSegments().add(segment);
		
		PNRAddMultiElements result = builder.buildRequest(request, booking, booking1, deleteMap, false, false);
		
		Assert.assertEquals("OXWKYR",result.getReservationInfo().getReservation().getControlNumber());
		
		List<DataElementsIndiv>  dataElementsIndivs = result.getDataElementsMaster().getDataElementsIndiv();
		Assert.assertTrue(dataElementsIndivs.size() == 10);
		for(DataElementsIndiv dataElementsIndiv : dataElementsIndivs) {
			if(dataElementsIndiv.getServiceRequest() != null && dataElementsIndiv.getServiceRequest().getSsr() != null) {
				if(OneAConstants.DOCO.equals(dataElementsIndiv.getServiceRequest().getSsr().getType())) {//KTN
					List<String> freeTexts = dataElementsIndiv.getServiceRequest().getSsr().getFreetext();
					Assert.assertTrue(freeTexts.size() == 1);
					String target = "/K/1234567///";
					Assert.assertTrue(target.equals(freeTexts.get(0)));
				} else if(OneAConstants.DOCS.equals(dataElementsIndiv.getServiceRequest().getSsr().getType())){
					String freetext = dataElementsIndiv.getServiceRequest().getSsr().getFreetext().get(0);
					if(freetext.indexOf("-MI-")>0 || freetext.indexOf("-FI-")>0) {
						//infant travel doc
						Assert.assertEquals("SSR",dataElementsIndiv.getElementManagementData().getSegmentName());
						Assert.assertEquals("PT",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getQualifier());
						Assert.assertEquals("2",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getNumber());
						Assert.assertEquals("ST",dataElementsIndiv.getReferenceForDataElement().getReference().get(1).getQualifier());
						Assert.assertEquals("1",dataElementsIndiv.getReferenceForDataElement().getReference().get(1).getNumber());
						Assert.assertEquals("DOCS",dataElementsIndiv.getServiceRequest().getSsr().getType());
						Assert.assertEquals("P-USA-654321-USA-02Jan00-MI-31Dec20-li-si",dataElementsIndiv.getServiceRequest().getSsr().getFreetext().get(0));
					}else {
						Assert.assertEquals("SSR",dataElementsIndiv.getElementManagementData().getSegmentName());
						Assert.assertEquals("PT",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getQualifier());
						Assert.assertEquals("2",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getNumber());
						Assert.assertEquals("ST",dataElementsIndiv.getReferenceForDataElement().getReference().get(1).getQualifier());
						Assert.assertEquals("1",dataElementsIndiv.getReferenceForDataElement().getReference().get(1).getNumber());
						Assert.assertEquals("DOCS",dataElementsIndiv.getServiceRequest().getSsr().getType());
						Assert.assertEquals("P-USA-123456-USA-01Jan00-M-31Dec20-zhang-san",dataElementsIndiv.getServiceRequest().getSsr().getFreetext().get(0));
					}
				}else if(OneAConstants.DOCA.equals(dataElementsIndiv.getServiceRequest().getSsr().getType())){
					String freetext = dataElementsIndiv.getServiceRequest().getSsr().getFreetext().get(0);
					if(freetext.startsWith("R")) {
						if(freetext.endsWith("I")) {
							//country of residence - infant
							Assert.assertEquals("SSR",dataElementsIndiv.getElementManagementData().getSegmentName());
							Assert.assertEquals("PT",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getQualifier());
							Assert.assertEquals("2",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getNumber());
							Assert.assertEquals("DOCA",dataElementsIndiv.getServiceRequest().getSsr().getType());
							Assert.assertEquals("R-CHN-----I",dataElementsIndiv.getServiceRequest().getSsr().getFreetext().get(0));
						}else {
							//country of residence
							Assert.assertEquals("SSR",dataElementsIndiv.getElementManagementData().getSegmentName());
							Assert.assertEquals("PT",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getQualifier());
							Assert.assertEquals("2",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getNumber());
							Assert.assertEquals("ST",dataElementsIndiv.getReferenceForDataElement().getReference().get(1).getQualifier());
							Assert.assertEquals("1",dataElementsIndiv.getReferenceForDataElement().getReference().get(1).getNumber());
							Assert.assertEquals("DOCA",dataElementsIndiv.getServiceRequest().getSsr().getType());
							Assert.assertEquals("R-CHN----",dataElementsIndiv.getServiceRequest().getSsr().getFreetext().get(0));
						}
					}else if(freetext.startsWith("D")) {
						if(freetext.endsWith("I")) {
							//destination address - infant
							Assert.assertEquals("SSR",dataElementsIndiv.getElementManagementData().getSegmentName());
							Assert.assertEquals("PT",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getQualifier());
							Assert.assertEquals("2",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getNumber());
							Assert.assertEquals("DOCA",dataElementsIndiv.getServiceRequest().getSsr().getType());
							Assert.assertEquals("D--huang he da dao-sky-AAA-A66666-I",dataElementsIndiv.getServiceRequest().getSsr().getFreetext().get(0));
						}else {
							//destination address
							Assert.assertEquals("SSR",dataElementsIndiv.getElementManagementData().getSegmentName());
							Assert.assertEquals("PT",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getQualifier());
							Assert.assertEquals("2",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getNumber());
							Assert.assertEquals("DOCA",dataElementsIndiv.getServiceRequest().getSsr().getType());
							Assert.assertEquals("D--huang he da dao-sky-AAA-A66666",dataElementsIndiv.getServiceRequest().getSsr().getFreetext().get(0));
						}
					}
				}else if(OneAConstants.CTCM.equals(dataElementsIndiv.getServiceRequest().getSsr().getType())){
					//contact info - phone number
					Assert.assertEquals("SSR",dataElementsIndiv.getElementManagementData().getSegmentName());
					Assert.assertEquals("PT",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getQualifier());
					Assert.assertEquals("2",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getNumber());
					Assert.assertEquals("CTCM",dataElementsIndiv.getServiceRequest().getSsr().getType());
					Assert.assertEquals("8618888888888/XX",dataElementsIndiv.getServiceRequest().getSsr().getFreetext().get(0));
				}else if(OneAConstants.CTCE.equals(dataElementsIndiv.getServiceRequest().getSsr().getType())){
					//contact info - email
					Assert.assertEquals("SSR",dataElementsIndiv.getElementManagementData().getSegmentName());
					Assert.assertEquals("PT",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getQualifier());
					Assert.assertEquals("2",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getNumber());
					Assert.assertEquals("CTCE",dataElementsIndiv.getServiceRequest().getSsr().getType());
					Assert.assertEquals("zhang..san//sina..cn/XX",dataElementsIndiv.getServiceRequest().getSsr().getFreetext().get(0));
				}else if(OneAConstants.PCTC.equals(dataElementsIndiv.getServiceRequest().getSsr().getType())){
					//emergency contact
					Assert.assertEquals("SSR",dataElementsIndiv.getElementManagementData().getSegmentName());
					Assert.assertEquals("PT",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getQualifier());
					Assert.assertEquals("2",dataElementsIndiv.getReferenceForDataElement().getReference().get(0).getNumber());
					Assert.assertEquals("PCTC",dataElementsIndiv.getServiceRequest().getSsr().getType());
					Assert.assertEquals("wang wu/HK18666666666",dataElementsIndiv.getServiceRequest().getSsr().getFreetext().get(0));
				}
			}
		}
		
		//check delete map
		Assert.assertNotNull(deleteMap.get(CommonConstants.MAP_TRAVEL_DOCUMENT_KEY));
		Assert.assertTrue(deleteMap.get(CommonConstants.MAP_TRAVEL_DOCUMENT_KEY).contains("4"));
		Assert.assertTrue(deleteMap.get(CommonConstants.MAP_TRAVEL_DOCUMENT_KEY).contains("7"));
		
		Assert.assertNotNull(deleteMap.get(CommonConstants.MAP_PHONENO_KEY));
		Assert.assertTrue(deleteMap.get(CommonConstants.MAP_PHONENO_KEY).contains("1"));
		
		Assert.assertNotNull(deleteMap.get(CommonConstants.MAP_EMAIL_KEY));
		Assert.assertTrue(deleteMap.get(CommonConstants.MAP_EMAIL_KEY).contains("2"));
		
		Assert.assertNotNull(deleteMap.get(CommonConstants.MAP_EMERGENCY_INFO_KEY));
		Assert.assertTrue(deleteMap.get(CommonConstants.MAP_EMERGENCY_INFO_KEY).contains("3"));
		
		Assert.assertNotNull(deleteMap.get(CommonConstants.MAP_DESTINATION_ADDRESS_KEY));
		Assert.assertTrue(deleteMap.get(CommonConstants.MAP_DESTINATION_ADDRESS_KEY).contains("6"));
		Assert.assertTrue(deleteMap.get(CommonConstants.MAP_DESTINATION_ADDRESS_KEY).contains("9"));
	}
}
