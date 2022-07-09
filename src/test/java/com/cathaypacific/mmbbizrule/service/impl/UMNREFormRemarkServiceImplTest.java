package com.cathaypacific.mmbbizrule.service.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mmbbizrule.dto.request.umnrform.UMNRFormPhoneInfoRemarkDTO;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UmnrFormAddressRemarkDTO;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UmnrFormGuardianInfoRemarkDTO;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UmnrFormPortInfoRemarkDTO;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UmnrFormSegmentRemarkDTO;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UmnrFormUpdateRequestDTO;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormAddressRemark;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormGuardianInfoRemark;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormPortInfoRemark;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormRemark;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormSegmentRemark;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRemark;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class UMNREFormRemarkServiceImplTest {
	
	@InjectMocks
	UMNREFormRemarkServiceImpl umnreFormRemarkService;
	
	@Test
	public void withoutUMNREFormRemark() {
		RetrievePnrBooking pnrBooking = new RetrievePnrBooking();
		pnrBooking.setRemarkList(Lists.newArrayList());
		
		List<UMNREFormRemark> actual = umnreFormRemarkService.buildUMNREFormRemark(pnrBooking);
		List<UMNREFormRemark> expected = Lists.newArrayList();
		
		Assert.assertEquals(actual, expected);
	}
	
	@Test
	public void umnrEFormRemarkAtTheSameLine() {
		RetrievePnrBooking pnrBooking = new RetrievePnrBooking();
		pnrBooking.setRemarkList(Lists.newArrayList());
		RetrievePnrRemark pnrRemark = new RetrievePnrRemark();
		pnrRemark.setFreeText("MMB UMFORM/P1/07/M/%BUILDING #1%/%ROAD #1%/%CITY #1%/HKG/PARENT NAME #1/heb-fabcabca/%PARENT BUILDING #2%/%PARENT STREET #2%/%PARENT CITY #2%/TPE/CX496/01042019/HKG/NAME ONE/PARENT ONE/85200000001/%BUILDING #2%/%ROAD #2%/%CITY #2%/TPE/CX450/01042019/NRT/NAME TWO/PARENT TWO/8520000002/%BUILDING #3%/%ROAD #3%/%CITY #3%/NRT/CX255/20042019/HKG/NAME THREE/PARENT THREE/85200000003/%BUILDING #4%/%ROAD #4%/%CITY #4%/HKG/LHR/NAME FOUR/PARENT FOUR/85200000004/%BUILDING #5%/%ROAD #5%/%CITY #5%/LHR/CX252/27042019/LHR/NAME FIVE/PARENT FIVE/85200000005/%BUILDING #6%/%ROAD #6%/%CITY #6%/LHR/HKG/NAME SIX/PARENT SIX/85200000006/%BUILDING #7%/%ROAD #7%/%CITY #7%/HKG/%END!%");
		pnrBooking.getRemarkList().add(pnrRemark);
		
		List<UMNREFormRemark> actuals = umnreFormRemarkService.buildUMNREFormRemark(pnrBooking);
		UMNREFormRemark actual = null;
		if (!actuals.isEmpty()) {
			actual = actuals.get(0);
		}
		
		Assert.assertEquals("07", actual.getAge());
		Assert.assertEquals("P1", actual.getPassengerId());
		Assert.assertEquals("1", actual.getPassengerIdDigit());
		Assert.assertEquals("M", actual.getGender());
		
		UMNREFormAddressRemark actualAddressRemark = actual.getAddress();
		Assert.assertEquals("BUILDING #1", actualAddressRemark.getBuilding());
		Assert.assertEquals("ROAD #1", actualAddressRemark.getStreet());
		Assert.assertEquals("CITY #1", actualAddressRemark.getCity());
		Assert.assertEquals("HKG", actualAddressRemark.getCountryCode());
		
		UMNREFormGuardianInfoRemark parentInfoRemark = actual.getParentInfo();
		Assert.assertEquals("PARENT NAME #1", parentInfoRemark.getName());
		Assert.assertEquals("852-61231231", parentInfoRemark.getPhoneNumber());
		Assert.assertEquals(null, parentInfoRemark.getRelationship());
		Assert.assertEquals("PARENT BUILDING #2", parentInfoRemark.getAddress().getBuilding());
		Assert.assertEquals("PARENT STREET #2", parentInfoRemark.getAddress().getStreet());
		Assert.assertEquals("PARENT CITY #2", parentInfoRemark.getAddress().getCity());
		Assert.assertEquals("TPE", parentInfoRemark.getAddress().getCountryCode());
		
		List<UMNREFormSegmentRemark> actualSegmentRemarks = actual.getSegments();
		/** First segment */
		UMNREFormSegmentRemark actualSegmentRemark = actualSegmentRemarks.get(0);
		Assert.assertEquals("CX496", actualSegmentRemark.getFlightNumber());
		Assert.assertEquals("01042019", actualSegmentRemark.getFlightDate());
		
			List<UMNREFormPortInfoRemark> actualPortInfoRemarks = actualSegmentRemark.getPortInfo();
			/** First port of first segment */
			UMNREFormPortInfoRemark actualPortInfoRemark = actualPortInfoRemarks.get(0);
			Assert.assertEquals("HKG", actualPortInfoRemark.getAirportCode());
			
			UMNREFormGuardianInfoRemark actualGuardianInfoRemark = actualPortInfoRemark.getGuardianInfo();
			Assert.assertEquals("NAME ONE", actualGuardianInfoRemark.getName());
			Assert.assertEquals("PARENT ONE", actualGuardianInfoRemark.getRelationship());
			Assert.assertEquals("85200000001", actualGuardianInfoRemark.getPhoneNumber());
			
			UMNREFormAddressRemark actualGuardionAddressRemark = actualGuardianInfoRemark.getAddress();
			Assert.assertEquals("BUILDING #2", actualGuardionAddressRemark.getBuilding());
			Assert.assertEquals("ROAD #2", actualGuardionAddressRemark.getStreet());
			Assert.assertEquals("CITY #2", actualGuardionAddressRemark.getCity());
			Assert.assertEquals("TPE", actualGuardionAddressRemark.getCountryCode());

		/** Second segment */
		actualSegmentRemark = actualSegmentRemarks.get(1);
		Assert.assertEquals("CX450", actualSegmentRemark.getFlightNumber());
		Assert.assertEquals("01042019", actualSegmentRemark.getFlightDate());
		
		actualPortInfoRemarks = actualSegmentRemark.getPortInfo();
			/** First port of second segment */
			actualPortInfoRemark = actualPortInfoRemarks.get(0);
			Assert.assertEquals("NRT", actualPortInfoRemark.getAirportCode());
			
			actualGuardianInfoRemark = actualPortInfoRemark.getGuardianInfo();
			Assert.assertEquals("NAME TWO", actualGuardianInfoRemark.getName());
			Assert.assertEquals("PARENT TWO", actualGuardianInfoRemark.getRelationship());
			Assert.assertEquals("8520000002", actualGuardianInfoRemark.getPhoneNumber());
			
			actualGuardionAddressRemark = actualGuardianInfoRemark.getAddress();
			Assert.assertEquals("BUILDING #3", actualGuardionAddressRemark.getBuilding());
			Assert.assertEquals("ROAD #3", actualGuardionAddressRemark.getStreet());
			Assert.assertEquals("CITY #3", actualGuardionAddressRemark.getCity());
			Assert.assertEquals("NRT", actualGuardionAddressRemark.getCountryCode());
		
			
		/** Third segment */
		actualSegmentRemark = actualSegmentRemarks.get(2);
		Assert.assertEquals("CX255", actualSegmentRemark.getFlightNumber());
		Assert.assertEquals("20042019", actualSegmentRemark.getFlightDate());
		
		actualPortInfoRemarks = actualSegmentRemark.getPortInfo();
			/** First port of third segment */
			actualPortInfoRemark = actualPortInfoRemarks.get(0);
			Assert.assertEquals("HKG", actualPortInfoRemark.getAirportCode());
			
			actualGuardianInfoRemark = actualPortInfoRemark.getGuardianInfo();
			Assert.assertEquals("NAME THREE", actualGuardianInfoRemark.getName());
			Assert.assertEquals("PARENT THREE", actualGuardianInfoRemark.getRelationship());
			Assert.assertEquals("85200000003", actualGuardianInfoRemark.getPhoneNumber());
			
			actualGuardionAddressRemark = actualGuardianInfoRemark.getAddress();
			Assert.assertEquals("BUILDING #4", actualGuardionAddressRemark.getBuilding());
			Assert.assertEquals("ROAD #4", actualGuardionAddressRemark.getStreet());
			Assert.assertEquals("CITY #4", actualGuardionAddressRemark.getCity());
			Assert.assertEquals("HKG", actualGuardionAddressRemark.getCountryCode());
			
			/** Second port of third segment */
			actualPortInfoRemark = actualPortInfoRemarks.get(1);
			Assert.assertEquals("LHR", actualPortInfoRemark.getAirportCode());
			
			actualGuardianInfoRemark = actualPortInfoRemark.getGuardianInfo();
			Assert.assertEquals("NAME FOUR", actualGuardianInfoRemark.getName());
			Assert.assertEquals("PARENT FOUR", actualGuardianInfoRemark.getRelationship());
			Assert.assertEquals("85200000004", actualGuardianInfoRemark.getPhoneNumber());
			
			actualGuardionAddressRemark = actualGuardianInfoRemark.getAddress();
			Assert.assertEquals("BUILDING #5", actualGuardionAddressRemark.getBuilding());
			Assert.assertEquals("ROAD #5", actualGuardionAddressRemark.getStreet());
			Assert.assertEquals("CITY #5", actualGuardionAddressRemark.getCity());
			Assert.assertEquals("LHR", actualGuardionAddressRemark.getCountryCode());
			
		/** Fourth segment */
		actualSegmentRemark = actualSegmentRemarks.get(3);
		Assert.assertEquals("CX252", actualSegmentRemark.getFlightNumber());
		Assert.assertEquals("27042019", actualSegmentRemark.getFlightDate());
		
		actualPortInfoRemarks = actualSegmentRemark.getPortInfo();
			/** First port of fourth segment */
			actualPortInfoRemark = actualPortInfoRemarks.get(0);
			Assert.assertEquals("LHR", actualPortInfoRemark.getAirportCode());
			
			actualGuardianInfoRemark = actualPortInfoRemark.getGuardianInfo();
			Assert.assertEquals("NAME FIVE", actualGuardianInfoRemark.getName());
			Assert.assertEquals("PARENT FIVE", actualGuardianInfoRemark.getRelationship());
			Assert.assertEquals("85200000005", actualGuardianInfoRemark.getPhoneNumber());
			
			actualGuardionAddressRemark = actualGuardianInfoRemark.getAddress();
			Assert.assertEquals("BUILDING #6", actualGuardionAddressRemark.getBuilding());
			Assert.assertEquals("ROAD #6", actualGuardionAddressRemark.getStreet());
			Assert.assertEquals("CITY #6", actualGuardionAddressRemark.getCity());
			Assert.assertEquals("LHR", actualGuardionAddressRemark.getCountryCode());
			
			/** Second port of fourth segment */
			actualPortInfoRemark = actualPortInfoRemarks.get(1);
			Assert.assertEquals("HKG", actualPortInfoRemark.getAirportCode());
			
			actualGuardianInfoRemark = actualPortInfoRemark.getGuardianInfo();
			Assert.assertEquals("NAME SIX", actualGuardianInfoRemark.getName());
			Assert.assertEquals("PARENT SIX", actualGuardianInfoRemark.getRelationship());
			Assert.assertEquals("85200000006", actualGuardianInfoRemark.getPhoneNumber());
			
			actualGuardionAddressRemark = actualGuardianInfoRemark.getAddress();
			Assert.assertEquals("BUILDING #7", actualGuardionAddressRemark.getBuilding());
			Assert.assertEquals("ROAD #7", actualGuardionAddressRemark.getStreet());
			Assert.assertEquals("CITY #7", actualGuardionAddressRemark.getCity());
			Assert.assertEquals("HKG", actualGuardionAddressRemark.getCountryCode());

	}
	
	@Test
	public void multiUMNREFormRemark() {
		RetrievePnrBooking pnrBooking = new RetrievePnrBooking();
		pnrBooking.setRemarkList(Lists.newArrayList());
		RetrievePnrRemark pnrRemark = new RetrievePnrRemark();
		pnrRemark.setFreeText("MMB UMFORM/P1/07/M/%BUILDING #1%/%ROAD #1%/%CITY #1%/HKG/PARENT NAME #1/852-61231231/%PARENT BUILDING #2%/%PARENT STREET #2%/%PARENT CITY #2%/TPE/CX496/01042019/HKG/NAME ONE/PARENT ONE/85200000001/%BUILDING #2%/%ROAD #2%/%CITY #2%/TPE/CX450/01042019/NRT/NAME TWO/PARENT TWO/8520000002/%BUILDING #3%/%ROAD #3%/%CITY #3%/NRT/CX255/20042019/HKG/NAME THREE/PARENT THREE/85200000003/%BUILDING #4%/%ROAD #4%/%CITY #4%/HKG/LHR/NAME FOUR/PARENT FOUR/85200000004/%BUILDING #5%/%ROAD #5%/%CITY #5%/LHR/CX252/27042019/LHR/NAME FIVE/PARENT FIVE/85200000005/%BUILDING #6%/%ROAD #6%/%CITY #6%/LHR/HKG/NAME SIX/PARENT SIX/85200000006/%BUILDING #7%/%ROAD #7%/%CITY #7%/HKG/%END!%");
		pnrBooking.getRemarkList().add(pnrRemark);
		pnrBooking.getRemarkList().add(pnrRemark);
		pnrBooking.getRemarkList().add(pnrRemark);
		pnrBooking.getRemarkList().add(pnrRemark);
		
		List<UMNREFormRemark> actuals = umnreFormRemarkService.buildUMNREFormRemark(pnrBooking);

		Assert.assertEquals(4, actuals.size());
	}
	
	@Test
	public void parseModelToFreetext() {
		UmnrFormUpdateRequestDTO model = new UmnrFormUpdateRequestDTO();
		model.setRloc("A1B2C3");
		model.setAge("7");
		model.setGender("F");
		model.setPassengerId("2");
		
		UmnrFormAddressRemarkDTO address = new UmnrFormAddressRemarkDTO();
		address.setBuilding("Building #1");
		address.setCity("City #1");
		address.setCountryCode("HKG");
		address.setStreet("Street #1");
		model.setAddress(address);
		
		UmnrFormGuardianInfoRemarkDTO parentInfo = new UmnrFormGuardianInfoRemarkDTO();
		parentInfo.setName("Parent Name #2");
		parentInfo.setPhoneInfo(new UMNRFormPhoneInfoRemarkDTO());
		parentInfo.getPhoneInfo().setPhoneCountryNumber("852");
		parentInfo.getPhoneInfo().setPhoneNo("61231231");
		parentInfo.setRelationship(null);
		UmnrFormAddressRemarkDTO parentAddress = new UmnrFormAddressRemarkDTO();
		parentAddress.setBuilding("Parent Building #2");
		parentAddress.setCity("Parent City #2");
		parentAddress.setCountryCode("TPE");
		parentAddress.setStreet("Parent Street #2");
		parentInfo.setAddress(parentAddress);
		model.setParentInfo(parentInfo);
		
		List<UmnrFormSegmentRemarkDTO> segments = Lists.newArrayList();
			// Segment #1
			UmnrFormSegmentRemarkDTO segment = new UmnrFormSegmentRemarkDTO();
			segment.setFlightDate("20190401");
			segment.setFlightNumber("CX450");
		
			List<UmnrFormPortInfoRemarkDTO> portInfos = Lists.newArrayList();
				// Port info #1
				UmnrFormPortInfoRemarkDTO portInfo = new UmnrFormPortInfoRemarkDTO();
				portInfo.setAirportCode("TPE");
				UmnrFormGuardianInfoRemarkDTO guardianInfo = new UmnrFormGuardianInfoRemarkDTO();
				guardianInfo.setName("Guardian Name #3");
				guardianInfo.setPhoneInfo(new UMNRFormPhoneInfoRemarkDTO());
				guardianInfo.getPhoneInfo().setPhoneCountryNumber("852");
				guardianInfo.getPhoneInfo().setPhoneNo("61231231");
				guardianInfo.setRelationship("Guardian #3");
				UmnrFormAddressRemarkDTO guardianAddress = new UmnrFormAddressRemarkDTO();
				guardianAddress.setBuilding("Guardian Building #3");
				guardianAddress.setCity("Guardian City #3");
				guardianAddress.setCountryCode("TPE");
				guardianAddress.setStreet("Guardian Street #3");
				guardianInfo.setAddress(guardianAddress);
				portInfo.setGuardianInfo(guardianInfo);
				portInfos.add(portInfo);
				
				// Port info #2
				UmnrFormPortInfoRemarkDTO portInfo2 = new UmnrFormPortInfoRemarkDTO();
				portInfo2.setAirportCode("HND");
				UmnrFormGuardianInfoRemarkDTO guardianInfo2 = new UmnrFormGuardianInfoRemarkDTO();
				guardianInfo2.setName("Guardian Name #4");
				guardianInfo2.setPhoneInfo(new UMNRFormPhoneInfoRemarkDTO());
				guardianInfo2.getPhoneInfo().setPhoneCountryNumber("852");
				guardianInfo2.getPhoneInfo().setPhoneNo("61231231");
				guardianInfo2.setRelationship("Guardian #4");
				UmnrFormAddressRemarkDTO guardianAddress2 = new UmnrFormAddressRemarkDTO();
				guardianAddress2.setBuilding("Guardian Building #4");
				guardianAddress2.setCity("Guardian City #4");
				guardianAddress2.setCountryCode("TPE");
				guardianAddress2.setStreet("Guardian Street #4");
				guardianInfo2.setAddress(guardianAddress2);
				portInfo2.setGuardianInfo(guardianInfo2);
				portInfos.add(portInfo2);
				
				segment.setPortInfo(portInfos);
				segments.add(segment);
		

			// Segment #2
			UmnrFormSegmentRemarkDTO segment2 = new UmnrFormSegmentRemarkDTO();
			segment2.setFlightDate("20190510");
			segment2.setFlightNumber("CX620");
		
			List<UmnrFormPortInfoRemarkDTO> portInfos2 = Lists.newArrayList();
				// Port info #3
				UmnrFormPortInfoRemarkDTO portInfo3 = new UmnrFormPortInfoRemarkDTO();
				portInfo3.setAirportCode("LGW");
				UmnrFormGuardianInfoRemarkDTO guardianInfo3 = new UmnrFormGuardianInfoRemarkDTO();
				guardianInfo3.setName("Guardian Name #5");
				guardianInfo3.setPhoneInfo(new UMNRFormPhoneInfoRemarkDTO());
				guardianInfo3.getPhoneInfo().setPhoneCountryNumber("852");
				guardianInfo3.getPhoneInfo().setPhoneNo("61231231");
				guardianInfo3.setRelationship("Guardian #5");
				UmnrFormAddressRemarkDTO guardianAddress3 = new UmnrFormAddressRemarkDTO();
				guardianAddress3.setBuilding("Guardian Building #5");
				guardianAddress3.setCity("Guardian City #5");
				guardianAddress3.setCountryCode("LGW");
				guardianAddress3.setStreet("Guardian Street #5");
				guardianInfo3.setAddress(guardianAddress3);
				portInfo3.setGuardianInfo(guardianInfo3);
				portInfos2.add(portInfo3);
				
				// Port info #2
				UmnrFormPortInfoRemarkDTO portInfo4 = new UmnrFormPortInfoRemarkDTO();
				portInfo4.setAirportCode("HKG");
				UmnrFormGuardianInfoRemarkDTO guardianInfo4 = new UmnrFormGuardianInfoRemarkDTO();
				guardianInfo4.setName("Guardian Name #6");
				guardianInfo4.setPhoneInfo(new UMNRFormPhoneInfoRemarkDTO());
				guardianInfo4.getPhoneInfo().setPhoneCountryNumber("852");
				guardianInfo4.getPhoneInfo().setPhoneNo("61231234");
				guardianInfo4.setRelationship("Guardian #6");
				UmnrFormAddressRemarkDTO guardianAddress4 = new UmnrFormAddressRemarkDTO();
				guardianAddress4.setBuilding("Guardian Building #6");
				guardianAddress4.setCity("Guardian City #6");
				guardianAddress4.setCountryCode("LGW");
				guardianAddress4.setStreet("Street #6");
				guardianInfo4.setAddress(guardianAddress4);
				portInfo4.setGuardianInfo(guardianInfo4);
				portInfos2.add(portInfo4);
				
				segment2.setPortInfo(portInfos2);
				segments.add(segment2);
			
		model.setSegments(segments);
		
		String freetext = umnreFormRemarkService.buildUMNREFormRMFreeText(model);

		Assert.assertEquals(
			"MMB UMForm/P2/07/F/%Building #1%/%Street #1%/%City #1%/HKG"
			+ "/Parent Name #2/heb-fabcabca/%Parent Building #2%/%Parent Street #2%/%Parent City #2%/TPE"
			+ "/CX450/20190401"
			+ "/TPE/Guardian Name #3/%Guardian #3%/heb-fabcabca/%Guardian Building #3%/%Guardian Street #3%/%Guardian City #3%/TPE"
			+ "/HND/Guardian Name #4/%Guardian #4%/heb-fabcabca/%Guardian Building #4%/%Guardian Street #4%/%Guardian City #4%/TPE"
			+ "/CX620/20190510"
			+ "/LGW/Guardian Name #5/%Guardian #5%/heb-fabcabca/%Guardian Building #5%/%Guardian Street #5%/%Guardian City #5%/LGW"
			+ "/HKG/Guardian Name #6/%Guardian #6%/heb-fabcabcd/%Guardian Building #6%/%Street #6%/%Guardian City #6%/LGW"
			+ "/%END!%",
			freetext
		);
	}
	
}
