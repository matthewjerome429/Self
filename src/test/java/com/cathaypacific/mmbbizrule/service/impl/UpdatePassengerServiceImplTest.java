package com.cathaypacific.mmbbizrule.service.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.service.NationalityCodeService;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateInfantDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdatePassengerDetailsRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateAdultSegmentInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateTravelDocDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnrcancel.service.DeletePnrService;
import com.cathaypacific.mmbbizrule.oneaservice.updatepassenger.service.AddPassengerInfoService;
import com.cathaypacific.mmbbizrule.service.UpdatePassengerService.TransferStatus;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ElementManagementSegmentType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements.TravellerInfo;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements.DataElementsMaster.DataElementsIndiv;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ReferencingDetailsType;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ReservationControlInformationDetailsTypeI;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.ReservationControlInformationTypeI;
@RunWith(MockitoJUnitRunner.class)
public class UpdatePassengerServiceImplTest {
	
	@InjectMocks
	UpdatePassengerServiceImpl updatePassengerServiceImpl;
	
	@Mock
	private NationalityCodeService nationalityCodeService;
	
	@Mock
	private DeletePnrService deletePnrService;
	
	@Mock
	private AddPassengerInfoService addPassengerInfoService;
	
	private PNRAddMultiElements.DataElementsMaster dataElementsMaster;

	@Before
	public void SetupContext() {
		dataElementsMaster=new PNRAddMultiElements.DataElementsMaster();			
	}

	@Test
	public void test()throws BusinessBaseException {
		UpdatePassengerDetailsRequestDTO requestDTO=new UpdatePassengerDetailsRequestDTO();
		UpdateInfantDTO infant=new UpdateInfantDTO();
		infant.setPassengerId("1");
		Session session = null;
		PNRAddMultiElements request=new PNRAddMultiElements();
		ReservationControlInformationTypeI reservationInfo=new ReservationControlInformationTypeI();
		ReservationControlInformationDetailsTypeI reservation=new ReservationControlInformationDetailsTypeI();
		reservation.setCompanyId("");
		reservation.setControlNumber("");
		reservationInfo.setReservation(reservation);
		List<PNRAddMultiElements.TravellerInfo> travellerInfo=new ArrayList<>();
		ElementManagementSegmentType elementManagementData=new ElementManagementSegmentType();
		elementManagementData.setSegmentName("Test");
		ReferencingDetailsType referencingDetailsType=new ReferencingDetailsType();
		referencingDetailsType.setNumber("10");
		referencingDetailsType.setQualifier("OT");
		elementManagementData.setReference(referencingDetailsType);
		DataElementsIndiv indiv=new DataElementsIndiv();
		indiv.setElementManagementData(elementManagementData);
		dataElementsMaster.getDataElementsIndiv().add(indiv);
		request.setReservationInfo(reservationInfo);
		request.setDataElementsMaster(dataElementsMaster);
		TravellerInfo traInfo=new TravellerInfo();
		ElementManagementSegmentType elementManagementPassenger=new ElementManagementSegmentType();
		elementManagementPassenger.setSegmentName("Test");
		traInfo.setElementManagementPassenger(elementManagementPassenger);
		travellerInfo.add(traInfo);
		requestDTO.setRloc("OXWKYR");
		requestDTO.setPassengerId("1");
		requestDTO.setInfant(infant);
		requestDTO.findEmergencyContact().setPhoneCountryNumber("852");
		requestDTO.findPhoneInfo().setPhoneCountryNumber("852");
		RetrievePnrBooking pnrBooking=new RetrievePnrBooking();
		List<RetrievePnrPassenger> passengers=new ArrayList<>();
		List<RetrievePnrPassengerSegment> passengerSegments=new ArrayList<>();
		RetrievePnrPassenger passenger=new RetrievePnrPassenger();
		RetrievePnrPassengerSegment passengerSegment=new RetrievePnrPassengerSegment();
		passenger.setPassengerID("1");
		passengers.add(passenger);
		passengerSegment.setPassengerId("1");
		passengerSegment.setSegmentId("1");
		passengerSegments.add(passengerSegment);
		String newCountryCode="Hkk";
		String newCountryCode1="HK";
		pnrBooking.setPassengers(passengers);
		pnrBooking.setPassengerSegments(passengerSegments);
		List<RetrievePnrSegment> segmentss=new ArrayList<>();
		RetrievePnrSegment segmentsss=new RetrievePnrSegment();
		segmentsss.setOriginPort("SHK");
		segmentsss.setPnrOperateCompany("CX");
		segmentss.add(segmentsss);
		pnrBooking.setSegments(segmentss);
		RetrievePnrBooking retrievePnrBooking=null;
		List<UpdateAdultSegmentInfoDTO> segments=new ArrayList<>();
		UpdateAdultSegmentInfoDTO segment=new UpdateAdultSegmentInfoDTO();
		UpdateTravelDocDTO primaryTravelDoc =new UpdateTravelDocDTO();
		primaryTravelDoc.setFamilyName("Test");
		primaryTravelDoc.setGivenName("LIAN");
		segment.setPrimaryTravelDoc(primaryTravelDoc);
		segments.add(segment);
		requestDTO.setSegments(segments);
		when(nationalityCodeService.findTwoCountryCodeByThreeCountryCode("HHK")).thenReturn(newCountryCode);
		when(nationalityCodeService.findTwoCountryCodeByThreeCountryCode("HHH")).thenReturn(newCountryCode1);
		when(addPassengerInfoService.addPassengerInfo(request, session)).thenReturn(retrievePnrBooking);
		updatePassengerServiceImpl.updatePassenger(requestDTO, pnrBooking, new Booking(), new TransferStatus(), new TransferStatus());
		
		Assert.assertEquals(null, retrievePnrBooking);
	}

}
