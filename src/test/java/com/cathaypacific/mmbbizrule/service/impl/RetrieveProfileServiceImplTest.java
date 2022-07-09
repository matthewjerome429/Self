package com.cathaypacific.mmbbizrule.service.impl;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;
import com.cathaypacific.mmbbizrule.constant.MemberProfileConstants;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.ClsApiResponse;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.CustomerEmailAddressInfo;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.CustomerEmailAddressRecord;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.CustomerMobilePhoneInfo;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.CustomerMobilePhoneRecord;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.CustomerRecord;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.MemberName;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.OtherPreferenceRecord;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.PreferenceRecord;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.Profile;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.TravelDocumentRecord;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileServiceCacheHelper;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.impl.RetrieveProfileServiceImpl;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.service.impl.NationalityCodeServiceImpl;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePreference;

@RunWith(MockitoJUnitRunner.class)
public class RetrieveProfileServiceImplTest {

	@InjectMocks
	RetrieveProfileServiceImpl retrieveProfileServiceImpl;

	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	NationalityCodeServiceImpl nationalityService;
	
	@Mock
	RetrieveProfileServiceCacheHelper retrieveProfileServiceCacheHelperMock;

	@Test
	public void test_retrievePersonInfo() {

		Profile response = new Profile();
		CustomerRecord customerRecord = new CustomerRecord();
		MemberName memberName = new MemberName();

		customerRecord.setTierCode("MPO");
		memberName.setFamilyName("QIN");
		memberName.setGivenName("Dongdong");
		memberName.setTitle("MR");
		customerRecord.setMemberNumber("1234567");
		customerRecord.setMemberName(memberName);

		CustomerEmailAddressInfo customerEmailAddressInfo = new CustomerEmailAddressInfo();
		CustomerMobilePhoneInfo customerMobilePhoneInfo = new CustomerMobilePhoneInfo();
		List<CustomerEmailAddressRecord> customerEmailAddressRecord =new ArrayList<>();
		CustomerEmailAddressRecord customerEmailAddressRecor=new CustomerEmailAddressRecord();
		customerEmailAddressRecor.setEmailAddress("123@qq.com");
		customerEmailAddressRecord.add(customerEmailAddressRecor);
		customerEmailAddressInfo.setCustomerEmailAddressRecord(customerEmailAddressRecord);
		List<CustomerMobilePhoneRecord> customerMobilePhoneRecord =new ArrayList<>();
		CustomerMobilePhoneRecord customerMobilePhoneRecor=new CustomerMobilePhoneRecord();
		customerMobilePhoneRecor.setMobileType("M");
		customerMobilePhoneRecor.setMobilePhoneNumber("147258");
		customerMobilePhoneRecord.add(customerMobilePhoneRecor);
		customerMobilePhoneInfo.setCustomerMobilePhoneRecord(customerMobilePhoneRecord);

		response.setCustomerRecord(customerRecord);
		response.setCustomerEmailAddressInfo(customerEmailAddressInfo);
		response.setCustomerMobilePhoneInfo(customerMobilePhoneInfo);

		ClsApiResponse clsApiResponse = new ClsApiResponse();
		clsApiResponse.setProfile(response);
//		@SuppressWarnings("unchecked")
//		ResponseEntity<ClsApiResponse> mockedResponseEntity = Mockito.mock(ResponseEntity.class);

//		when(restTemplate.exchange(any(), Matchers.eq(ClsApiResponse.class))).thenReturn(mockedResponseEntity);
		when(retrieveProfileServiceCacheHelperMock.retrieveMemberProfileSummary(any())).thenReturn(response);
//		when(mockedResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
//		when(mockedResponseEntity.getBody()).thenReturn(clsApiResponse);
		ProfilePersonInfo profilePersonInfo = retrieveProfileServiceImpl.retrievePersonInfo("1910026122",
				"TESTTOKEN123456");
		Assert.assertEquals("MPO", profilePersonInfo.getTier());
		Assert.assertEquals("1234567", profilePersonInfo.getMemberId());
		Assert.assertEquals("QIN", profilePersonInfo.getFamilyName());
		Assert.assertEquals("Dongdong", profilePersonInfo.getGivenName());
		Assert.assertEquals("MR", profilePersonInfo.getTitle());
	}
	
	@Test
	public void test_retrievePreference() {
		String memberId="1910026122";
		String mbToken="TESTTOKEN123456";
		//Profile response = new Profile();
		ClsApiResponse clsApiResponse = new ClsApiResponse();
		PreferenceRecord preferenceRecord =new PreferenceRecord();
		List<TravelDocumentRecord> travelDocument =new ArrayList<>();
		TravelDocumentRecord travelDocumen=new TravelDocumentRecord();
		travelDocumen.setFamilyName("Test");
		travelDocumen.setDocumentNumber("147258");
		travelDocumen.setExpiryDate("2018-02-04");
		travelDocumen.setIssueCountry("BJ");
		travelDocumen.setNationality("CN");
		travelDocumen.setGivenName("LIAN");
		travelDocument.add(travelDocumen);
		List<OtherPreferenceRecord> otherPreference =new ArrayList<>();
		OtherPreferenceRecord otherPreferenc=new OtherPreferenceRecord();
		otherPreferenc.setAdditionalText("HELLO");
		otherPreferenc.setType("TDFN");
		otherPreferenc.setCategory(MemberProfileConstants.TRAVEL_COMPANION_PROFILE+MemberProfileConstants.TRAVEL_COMPANION_SEQUENCE_1);
		otherPreference.add(otherPreferenc);
	
		preferenceRecord.setOtherPreference(otherPreference);
		preferenceRecord.setTravelDocument(travelDocument);
		clsApiResponse.setPreferenceRecord(preferenceRecord);
//		@SuppressWarnings("unchecked")
//		ResponseEntity<ClsApiResponse> mockedResponseEntity = Mockito.mock(ResponseEntity.class);
		when(nationalityService.findThreeCountryCodeByTwoCountryCode("BJ")).thenReturn("BJ");
		when(nationalityService.findThreeCountryCodeByTwoCountryCode("CN")).thenReturn("CN");
//		when(restTemplate.exchange(anyString(), any(), any(), Matchers.eq(ClsApiResponse.class))).thenReturn(mockedResponseEntity);
		when(retrieveProfileServiceCacheHelperMock.retrieveMemberProfileDetails(any())).thenReturn(preferenceRecord);
//		when(mockedResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
//		when(mockedResponseEntity.getBody()).thenReturn(clsApiResponse);
		ProfilePreference profilePreference = retrieveProfileServiceImpl.retrievePreference(memberId, mbToken);
		Assert.assertEquals("Test", profilePreference.getPersonalTravelDocuments().get(0).getFamilyName());
		Assert.assertEquals("LIAN", profilePreference.getPersonalTravelDocuments().get(0).getGivenName());
		Assert.assertEquals("147258", profilePreference.getPersonalTravelDocuments().get(0).getDocumentNumber());
		Assert.assertEquals(null, profilePreference.getTravelCompanions().get(0).getDateOfBirth());
		Assert.assertEquals(null, profilePreference.getTravelCompanions().get(0).getGender());
	}
	@Test
	public void test_retrievePreference1() {
		String memberId="1910026122";
		String mbToken="TESTTOKEN123456";
		//Profile response = new Profile();
		ClsApiResponse clsApiResponse = new ClsApiResponse();
		PreferenceRecord preferenceRecord =new PreferenceRecord();
		List<TravelDocumentRecord> travelDocument =new ArrayList<>();
		TravelDocumentRecord travelDocumen=new TravelDocumentRecord();
		travelDocumen.setFamilyName("Test");
		travelDocumen.setDocumentNumber("147258");
		travelDocumen.setExpiryDate("2018-02-04");
		travelDocumen.setIssueCountry("BJ");
		travelDocumen.setNationality("CN");
		travelDocumen.setGivenName("LIAN");
		travelDocument.add(travelDocumen);
		List<OtherPreferenceRecord> otherPreference =new ArrayList<>();
		OtherPreferenceRecord otherPreferenc=new OtherPreferenceRecord();
		otherPreferenc.setAdditionalText("HELLO");
		otherPreferenc.setType("TDG");
		otherPreferenc.setCategory(MemberProfileConstants.TRAVEL_COMPANION_PROFILE+MemberProfileConstants.TRAVEL_COMPANION_SEQUENCE_1);
		otherPreference.add(otherPreferenc);
	
		preferenceRecord.setOtherPreference(otherPreference);
		preferenceRecord.setTravelDocument(travelDocument);
		clsApiResponse.setPreferenceRecord(preferenceRecord);
//		@SuppressWarnings("unchecked")
//		ResponseEntity<ClsApiResponse> mockedResponseEntity = Mockito.mock(ResponseEntity.class);
		when(nationalityService.findThreeCountryCodeByTwoCountryCode("BJ")).thenReturn("BJ");
		when(nationalityService.findThreeCountryCodeByTwoCountryCode("CN")).thenReturn("CN");
//		when(restTemplate.exchange(anyString(), any(), any(), Matchers.eq(ClsApiResponse.class)))
//				.thenReturn(mockedResponseEntity);
//		when(mockedResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
//		when(mockedResponseEntity.getBody()).thenReturn(clsApiResponse);
		when(retrieveProfileServiceCacheHelperMock.retrieveMemberProfileDetails(any())).thenReturn(preferenceRecord);
		ProfilePreference profilePreference = retrieveProfileServiceImpl.retrievePreference(memberId, mbToken);
		Assert.assertEquals("Test", profilePreference.getPersonalTravelDocuments().get(0).getFamilyName());
		Assert.assertEquals("LIAN", profilePreference.getPersonalTravelDocuments().get(0).getGivenName());
		Assert.assertEquals("147258", profilePreference.getPersonalTravelDocuments().get(0).getDocumentNumber());
		Assert.assertEquals(null, profilePreference.getTravelCompanions().get(0).getDateOfBirth());
		Assert.assertEquals("HELLO", profilePreference.getTravelCompanions().get(0).getGender());
	}
	@Test
	public void test_retrievePreference2() {
		String memberId="1910026122";
		String mbToken="TESTTOKEN123456";
		//Profile response = new Profile();
		ClsApiResponse clsApiResponse = new ClsApiResponse();
		PreferenceRecord preferenceRecord =new PreferenceRecord();
		List<TravelDocumentRecord> travelDocument =new ArrayList<>();
		TravelDocumentRecord travelDocumen=new TravelDocumentRecord();
		travelDocumen.setFamilyName("Test");
		travelDocumen.setDocumentNumber("147258");
		travelDocumen.setExpiryDate("2018-02-04");
		travelDocumen.setIssueCountry("BJ");
		travelDocumen.setNationality("CN");
		travelDocumen.setGivenName("LIAN");
		travelDocument.add(travelDocumen);
		List<OtherPreferenceRecord> otherPreference =new ArrayList<>();
		OtherPreferenceRecord otherPreferenc=new OtherPreferenceRecord();
		otherPreferenc.setAdditionalText("02-04-2018");
		otherPreferenc.setType("TDDOB");
		otherPreferenc.setCategory(MemberProfileConstants.TRAVEL_COMPANION_PROFILE+MemberProfileConstants.TRAVEL_COMPANION_SEQUENCE_1);
		otherPreference.add(otherPreferenc);
	
		preferenceRecord.setOtherPreference(otherPreference);
		preferenceRecord.setTravelDocument(travelDocument);
		clsApiResponse.setPreferenceRecord(preferenceRecord);
//		@SuppressWarnings("unchecked")
//		ResponseEntity<ClsApiResponse> mockedResponseEntity = Mockito.mock(ResponseEntity.class);
		when(nationalityService.findThreeCountryCodeByTwoCountryCode("BJ")).thenReturn("BJ");
		when(nationalityService.findThreeCountryCodeByTwoCountryCode("CN")).thenReturn("CN");
//		when(restTemplate.exchange(anyString(), any(), any(), Matchers.eq(ClsApiResponse.class)))
//				.thenReturn(mockedResponseEntity);
//		when(mockedResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
//		when(mockedResponseEntity.getBody()).thenReturn(clsApiResponse);
		when(retrieveProfileServiceCacheHelperMock.retrieveMemberProfileDetails(any())).thenReturn(preferenceRecord);
		ProfilePreference profilePreference = retrieveProfileServiceImpl.retrievePreference(memberId, mbToken);
		Assert.assertEquals("Test", profilePreference.getPersonalTravelDocuments().get(0).getFamilyName());
		Assert.assertEquals("LIAN", profilePreference.getPersonalTravelDocuments().get(0).getGivenName());
		Assert.assertEquals("147258", profilePreference.getPersonalTravelDocuments().get(0).getDocumentNumber());
		Assert.assertEquals("2018-04-02", profilePreference.getTravelCompanions().get(0).getDateOfBirth().toString());
		Assert.assertEquals(null, profilePreference.getTravelCompanions().get(0).getGender());
	}
	@Test
	public void test_retrievePreference3() {
		String memberId="1910026122";
		String mbToken="TESTTOKEN123456";
		//Profile response = new Profile();
		ClsApiResponse clsApiResponse = new ClsApiResponse();
		PreferenceRecord preferenceRecord =new PreferenceRecord();
		List<TravelDocumentRecord> travelDocument =new ArrayList<>();
		TravelDocumentRecord travelDocumen=new TravelDocumentRecord();
		travelDocumen.setFamilyName("Test");
		travelDocumen.setDocumentNumber("147258");
		travelDocumen.setExpiryDate("2018-02-04");
		travelDocumen.setIssueCountry("BJ");
		travelDocumen.setNationality("CN");
		travelDocumen.setGivenName("LIAN");
		travelDocument.add(travelDocumen);
		List<OtherPreferenceRecord> otherPreference =new ArrayList<>();
		OtherPreferenceRecord otherPreferenc=new OtherPreferenceRecord();
		otherPreferenc.setAdditionalText("02-04-2018");
		otherPreferenc.setType("TDLN");
		otherPreferenc.setCategory(MemberProfileConstants.TRAVEL_COMPANION_PROFILE+MemberProfileConstants.TRAVEL_COMPANION_SEQUENCE_1);
		otherPreference.add(otherPreferenc);
	
		preferenceRecord.setOtherPreference(otherPreference);
		preferenceRecord.setTravelDocument(travelDocument);
		clsApiResponse.setPreferenceRecord(preferenceRecord);
//		@SuppressWarnings("unchecked")
//		ResponseEntity<ClsApiResponse> mockedResponseEntity = Mockito.mock(ResponseEntity.class);
		when(nationalityService.findThreeCountryCodeByTwoCountryCode("BJ")).thenReturn("BJ");
		when(nationalityService.findThreeCountryCodeByTwoCountryCode("CN")).thenReturn("CN");
//		when(restTemplate.exchange(anyString(), any(), any(), Matchers.eq(ClsApiResponse.class)))
//				.thenReturn(mockedResponseEntity);
//		when(mockedResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
//		when(mockedResponseEntity.getBody()).thenReturn(clsApiResponse);
		when(retrieveProfileServiceCacheHelperMock.retrieveMemberProfileDetails(any())).thenReturn(preferenceRecord);
		ProfilePreference profilePreference = retrieveProfileServiceImpl.retrievePreference(memberId, mbToken);
		Assert.assertEquals("Test", profilePreference.getPersonalTravelDocuments().get(0).getFamilyName());
		Assert.assertEquals("LIAN", profilePreference.getPersonalTravelDocuments().get(0).getGivenName());
		Assert.assertEquals("147258", profilePreference.getPersonalTravelDocuments().get(0).getDocumentNumber());
		Assert.assertEquals(null, profilePreference.getTravelCompanions().get(0).getDateOfBirth());
		Assert.assertEquals(null, profilePreference.getTravelCompanions().get(0).getGender());
	}
	@Test
	public void test_retrievePreference4() {
		String memberId="1910026122";
		String mbToken="TESTTOKEN123456";
		//Profile response = new Profile();
		ClsApiResponse clsApiResponse = new ClsApiResponse();
		PreferenceRecord preferenceRecord =new PreferenceRecord();
		List<TravelDocumentRecord> travelDocument =new ArrayList<>();
		TravelDocumentRecord travelDocumen=new TravelDocumentRecord();
		travelDocumen.setFamilyName("Test");
		travelDocumen.setDocumentNumber("147258");
		travelDocumen.setExpiryDate("2018-02-04");
		travelDocumen.setIssueCountry("BJ");
		travelDocumen.setNationality("CN");
		travelDocumen.setGivenName("LIAN");
		travelDocument.add(travelDocumen);
		List<OtherPreferenceRecord> otherPreference =new ArrayList<>();
		OtherPreferenceRecord otherPreferenc=new OtherPreferenceRecord();
		otherPreferenc.setAdditionalText("02-04-2018");
		otherPreferenc.setType("TDLN");
		otherPreferenc.setCategory(MemberProfileConstants.TRAVEL_COMP_COUNTRY+MemberProfileConstants.TRAVEL_COMPANION_SEQUENCE_1);
		otherPreference.add(otherPreferenc);
	
		preferenceRecord.setOtherPreference(otherPreference);
		preferenceRecord.setTravelDocument(travelDocument);
		clsApiResponse.setPreferenceRecord(preferenceRecord);
//		@SuppressWarnings("unchecked")
//		ResponseEntity<ClsApiResponse> mockedResponseEntity = Mockito.mock(ResponseEntity.class);
        when(nationalityService.findThreeCountryCodeByTwoCountryCode("TDLN")).thenReturn("BJ");
       // when( nationalityService.findThreeCountryCodeByTwoCountryCode("CN")).thenReturn("CN");
//        when(restTemplate.exchange(anyString(), any(), any(), Matchers.eq(ClsApiResponse.class))).thenReturn(mockedResponseEntity);
//		when(mockedResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
//		when(mockedResponseEntity.getBody()).thenReturn(clsApiResponse);
		when(retrieveProfileServiceCacheHelperMock.retrieveMemberProfileDetails(any())).thenReturn(preferenceRecord);
		ProfilePreference profilePreference = retrieveProfileServiceImpl.retrievePreference(memberId, mbToken);
		Assert.assertEquals("Test", profilePreference.getPersonalTravelDocuments().get(0).getFamilyName());
		Assert.assertEquals("LIAN", profilePreference.getPersonalTravelDocuments().get(0).getGivenName());
		Assert.assertEquals("147258", profilePreference.getPersonalTravelDocuments().get(0).getDocumentNumber());
		Assert.assertEquals(null, profilePreference.getTravelCompanions().get(0).getDateOfBirth());
		Assert.assertEquals(null, profilePreference.getTravelCompanions().get(0).getGender());
	}
	@Test
	public void test_retrievePreference5() {
		String memberId="1910026122";
		String mbToken="TESTTOKEN123456";
		//Profile response = new Profile();
		ClsApiResponse clsApiResponse = new ClsApiResponse();
		PreferenceRecord preferenceRecord =new PreferenceRecord();
		List<TravelDocumentRecord> travelDocument =new ArrayList<>();
		TravelDocumentRecord travelDocumen=new TravelDocumentRecord();
		travelDocumen.setFamilyName("Test");
		travelDocumen.setDocumentNumber("147258");
		travelDocumen.setExpiryDate("02-04-2018");
		travelDocumen.setIssueCountry("BJ");
		travelDocumen.setNationality("CN");
		travelDocumen.setGivenName("LIAN");
		travelDocument.add(travelDocumen);
		List<OtherPreferenceRecord> otherPreference =new ArrayList<>();
		OtherPreferenceRecord otherPreferenc=new OtherPreferenceRecord();
		otherPreferenc.setAdditionalText("02-04-2018");
		otherPreferenc.setType("TDLN");
		otherPreferenc.setCategory(MemberProfileConstants.TRAVEL_COMP_EXP_DATE+MemberProfileConstants.TRAVEL_COMPANION_SEQUENCE_1);
		otherPreference.add(otherPreferenc);
	
		preferenceRecord.setOtherPreference(otherPreference);
		preferenceRecord.setTravelDocument(travelDocument);
		clsApiResponse.setPreferenceRecord(preferenceRecord);
//		@SuppressWarnings("unchecked")
//		ResponseEntity<ClsApiResponse> mockedResponseEntity = Mockito.mock(ResponseEntity.class);
		when(nationalityService.findThreeCountryCodeByTwoCountryCode("TDLN")).thenReturn("BJ");
		// when( nationalityService.findThreeCountryCodeByTwoCountryCode("CN")).thenReturn("CN");
//		when(restTemplate.exchange(anyString(), any(), any(), Matchers.eq(ClsApiResponse.class))).thenReturn(mockedResponseEntity);
//		when(mockedResponseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
//		when(mockedResponseEntity.getBody()).thenReturn(clsApiResponse);
		when(retrieveProfileServiceCacheHelperMock.retrieveMemberProfileDetails(any())).thenReturn(preferenceRecord);
		ProfilePreference profilePreference = retrieveProfileServiceImpl.retrievePreference(memberId, mbToken);
		Assert.assertEquals("Test", profilePreference.getPersonalTravelDocuments().get(0).getFamilyName());
		Assert.assertEquals("LIAN", profilePreference.getPersonalTravelDocuments().get(0).getGivenName());
		Assert.assertEquals("147258", profilePreference.getPersonalTravelDocuments().get(0).getDocumentNumber());
		Assert.assertEquals("2018-04-02", profilePreference.getPersonalTravelDocuments().get(0).getExpiryDate().toString());
		Assert.assertEquals(null, profilePreference.getTravelCompanions().get(0).getGender());
	}
	
	@Test
	public void test_ru_upgrade_am() {
		String memberId="1910026122";
		String mbToken="TESTTOKEN123456";
		//Profile response = new Profile();
		ClsApiResponse clsApiResponse = new ClsApiResponse();
		PreferenceRecord preferenceRecord =new PreferenceRecord();

		List<OtherPreferenceRecord> otherPreference =new ArrayList<>();
		OtherPreferenceRecord otherPreferenc=new OtherPreferenceRecord();
		otherPreferenc.setAdditionalText("1910029650");
		otherPreferenc.setType("RU");
		otherPreferenc.setCategory("PROFILE TRANSIT FROM");
		otherPreference.add(otherPreferenc);
		preferenceRecord.setOtherPreference(otherPreference);
		clsApiResponse.setPreferenceRecord(preferenceRecord);
 
		when(retrieveProfileServiceCacheHelperMock.retrieveMemberProfileDetails(any())).thenReturn(preferenceRecord);
		ProfilePreference profilePreference = retrieveProfileServiceImpl.retrievePreference(memberId, mbToken);
		Assert.assertEquals("1910029650",profilePreference.getOriginalMemberId());
	}
}
