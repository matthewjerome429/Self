/*package com.cathaypacific.mmbbizrule.util;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import com.cathaypacific.mbcommon.model.common.UnlockedPaxInfo;
import com.cathaypacific.mmbbizrule.dto.common.booking.ContactInfoDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.DestinaitionAddressDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.EmailDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.EmrContactInfoDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.FlightBookingDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.GenderDobDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.KtnRedressDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.PassengerDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.PassengerSegmentDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.PhoneInfoDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.TravelDocDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.TravelDocsDTO;
*//**remove this because all of the mask logic should changed in sprint20*//*
//@RunWith(MockitoJUnitRunner.class)
public class MaskInfoUtilTest {
	//@InjectMocks
	//MaskInfoUtil maskInfoUtil;
	@Test
	public void test() {
		FlightBookingDTO flightBookingDTO=new FlightBookingDTO();
		List<PassengerDTO> passengers =new ArrayList<>();
		PassengerDTO passenger=new PassengerDTO();
		passenger.setPassengerId("1");
		ContactInfoDTO contactInfo=new ContactInfoDTO();
		EmailDTO email=new EmailDTO();
		email.setEmail("123@qq.com");
		contactInfo.setEmail(email);
		PhoneInfoDTO phoneInfo=new PhoneInfoDTO();
		phoneInfo.setPhoneNo("123456");
		contactInfo.setPhoneInfo(phoneInfo);
		passenger.setContactInfo(contactInfo);
		EmrContactInfoDTO emrContactInfo=new EmrContactInfoDTO();
		emrContactInfo.setCountryCode("SH");
		emrContactInfo.setPhoneNumber("123456");
		passenger.setEmrContactInfo(emrContactInfo);
		GenderDobDTO genderAndDob=new GenderDobDTO();
		genderAndDob.setBirthDateDay("02");
		DestinaitionAddressDTO destinaitionAddress=new DestinaitionAddressDTO();
		destinaitionAddress.setCity("SH");
		passenger.setDestinaitionAddress(destinaitionAddress);
		passengers.add(passenger);
		flightBookingDTO.setPassengers(passengers);
		
		List<PassengerSegmentDTO> passengerSegments =new ArrayList<>();
		PassengerSegmentDTO passengerSegment=new PassengerSegmentDTO();
		passengerSegment.setPassengerId("1");
		TravelDocsDTO travelDocTs=new TravelDocsDTO();
		TravelDocDTO priTravelDoc=new TravelDocDTO();
		priTravelDoc.setCompanyId("1");
		travelDocTs.setPriTravelDoc(priTravelDoc);
		TravelDocDTO secTravelDoc=new TravelDocDTO();
		secTravelDoc.setCompanyId("1");
		travelDocTs.setSecTravelDoc(secTravelDoc);
		KtnRedressDTO ktn=new KtnRedressDTO();
		ktn.setNumber("123456");
		passenger.setKtn(ktn);
		KtnRedressDTO redress=new KtnRedressDTO();
		redress.setNumber("12345");
		passenger.setRedress(redress);
		passengerSegment.setTravelDoc(travelDocTs);
		passengerSegments.add(passengerSegment);	
		flightBookingDTO.setPassengerSegments(passengerSegments);
		
		List<UnlockedPaxInfo> unlockedPassengerList=new ArrayList<>();
		maskInfoUtil.maskUserInfo(flightBookingDTO,unlockedPassengerList);
		Assert.assertEquals(0, unlockedPassengerList.size());
		Assert.assertEquals("1", flightBookingDTO.getPassengers().get(0).getPassengerId());
		Assert.assertEquals("1", flightBookingDTO.getPassengerSegments().get(0).getPassengerId());
	}
	@Test
	public void test1() {
		FlightBookingDTO flightBookingDTO=new FlightBookingDTO();
		List<PassengerDTO> passengers =new ArrayList<>();
		PassengerDTO passenger=new PassengerDTO();
		passenger.setPassengerId("1");
		ContactInfoDTO contactInfo=new ContactInfoDTO();
		EmailDTO email=new EmailDTO();
		email.setEmail("123@qq.com");
		contactInfo.setEmail(email);
		PhoneInfoDTO phoneInfo=new PhoneInfoDTO();
		phoneInfo.setPhoneNo("123456");
		contactInfo.setPhoneInfo(phoneInfo);
		passenger.setContactInfo(contactInfo);
		EmrContactInfoDTO emrContactInfo=new EmrContactInfoDTO();
		emrContactInfo.setCountryCode("SH");
		emrContactInfo.setPhoneNumber("123456");
		passenger.setEmrContactInfo(emrContactInfo);
		GenderDobDTO genderAndDob=new GenderDobDTO();
		genderAndDob.setBirthDateDay("02");
		DestinaitionAddressDTO destinaitionAddress=new DestinaitionAddressDTO();
		destinaitionAddress.setCity("SH");
		passenger.setDestinaitionAddress(destinaitionAddress);
		passengers.add(passenger);
		flightBookingDTO.setPassengers(passengers);
		
		List<PassengerSegmentDTO> passengerSegments =new ArrayList<>();
		PassengerSegmentDTO passengerSegment=new PassengerSegmentDTO();
		passengerSegment.setPassengerId("1");
		TravelDocsDTO travelDocTs=new TravelDocsDTO();
		TravelDocDTO priTravelDoc=new TravelDocDTO();
		priTravelDoc.setCompanyId("1");
		travelDocTs.setPriTravelDoc(priTravelDoc);
		TravelDocDTO secTravelDoc=new TravelDocDTO();	
		secTravelDoc.setCompanyId("1");
		travelDocTs.setSecTravelDoc(secTravelDoc);
		KtnRedressDTO ktn=new KtnRedressDTO();
		ktn.setNumber("123456");
		passenger.setKtn(ktn);
		KtnRedressDTO redress=new KtnRedressDTO();
		redress.setNumber("12345");
		passenger.setRedress(redress);
		passengerSegment.setTravelDoc(travelDocTs);
		passengerSegments.add(passengerSegment);	
		flightBookingDTO.setPassengerSegments(passengerSegments);
		
		List<UnlockedPaxInfo> unlockedPassengerList=new ArrayList<>();
		UnlockedPaxInfo unlockedPassenger=new UnlockedPaxInfo();
		unlockedPassenger.setPassengerId("1");
		unlockedPassenger.setUnlocked(true);
		unlockedPassengerList.add(unlockedPassenger);
		maskInfoUtil.maskUserInfo(flightBookingDTO,unlockedPassengerList);
		Assert.assertEquals("1", flightBookingDTO.getPassengers().get(0).getPassengerId());
		Assert.assertEquals("1", flightBookingDTO.getPassengerSegments().get(0).getPassengerId());
	}
	@Test
	public void test2() {
		FlightBookingDTO flightBookingDTO=new FlightBookingDTO();
		List<PassengerDTO> passengers =new ArrayList<>();
		PassengerDTO passenger=new PassengerDTO();
		passenger.setPassengerId("1");
		ContactInfoDTO contactInfo=new ContactInfoDTO();
		EmailDTO email=new EmailDTO();
		email.setEmail("123@qq.com");
		contactInfo.setEmail(email);
		PhoneInfoDTO phoneInfo=new PhoneInfoDTO();
		phoneInfo.setPhoneNo("123456");
		contactInfo.setPhoneInfo(phoneInfo);
		passenger.setContactInfo(contactInfo);
		EmrContactInfoDTO emrContactInfo=new EmrContactInfoDTO();
		emrContactInfo.setCountryCode("SH");
		emrContactInfo.setPhoneNumber("123456");
		passenger.setEmrContactInfo(emrContactInfo);
		GenderDobDTO genderAndDob=new GenderDobDTO();
		genderAndDob.setBirthDateDay("02");
		DestinaitionAddressDTO destinaitionAddress=new DestinaitionAddressDTO();
		destinaitionAddress.setCity("SH");
		passenger.setDestinaitionAddress(destinaitionAddress);
		passengers.add(passenger);
		flightBookingDTO.setPassengers(passengers);
		
		List<PassengerSegmentDTO> passengerSegments =new ArrayList<>();
		PassengerSegmentDTO passengerSegment=new PassengerSegmentDTO();
		passengerSegment.setPassengerId("1");
		TravelDocsDTO travelDocTs=new TravelDocsDTO();
		TravelDocDTO priTravelDoc=new TravelDocDTO();
		priTravelDoc.setCompanyId("1");
		travelDocTs.setPriTravelDoc(priTravelDoc);
		TravelDocDTO secTravelDoc=new TravelDocDTO();
		secTravelDoc.setCompanyId("1");
		travelDocTs.setSecTravelDoc(secTravelDoc);
		KtnRedressDTO ktn=new KtnRedressDTO();
		ktn.setNumber("123456");
		passenger.setKtn(ktn);
		KtnRedressDTO redress=new KtnRedressDTO();
		redress.setNumber("12345");
		passenger.setRedress(redress);
		passengerSegment.setTravelDoc(travelDocTs);
		passengerSegments.add(passengerSegment);	
		flightBookingDTO.setPassengerSegments(passengerSegments);
		
		List<UnlockedPaxInfo> unlockedPassengerList=new ArrayList<>();
		UnlockedPaxInfo unlockedPassenger=new UnlockedPaxInfo();
		unlockedPassenger.setPassengerId("1");
		unlockedPassenger.setUnlocked(false);
		unlockedPassengerList.add(unlockedPassenger);
		maskInfoUtil.maskUserInfo(flightBookingDTO,unlockedPassengerList);
		Assert.assertEquals("1", flightBookingDTO.getPassengers().get(0).getPassengerId());
		Assert.assertEquals("1", flightBookingDTO.getPassengerSegments().get(0).getPassengerId());
	}
}
*/