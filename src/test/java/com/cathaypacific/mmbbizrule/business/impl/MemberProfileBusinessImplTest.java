package com.cathaypacific.mmbbizrule.business.impl;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.BaseTest;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileService;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.service.impl.NationalityCodeServiceImpl;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocListCacheHelper;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocOdCacheHelper;
import com.cathaypacific.mmbbizrule.dto.response.memberprofile.ProfileTravelDocResponseDTO;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePreference;
import com.cathaypacific.mmbbizrule.model.profile.ProfileTravelDoc;
import com.cathaypacific.mmbbizrule.model.profile.TravelCompanion;

@RunWith(MockitoJUnitRunner.class)
public class MemberProfileBusinessImplTest extends BaseTest {
	
	
	@InjectMocks
	MemberProfileBusinessImpl memberProfileBusinessImpl;
	
	@Mock
	RetrieveProfileService retrieveProfileService;
	
	@Mock
	TbTravelDocOdCacheHelper tbTravelDocOdCacheHelper;

	@Mock
	TbTravelDocListCacheHelper tbTravelDocListCacheHelper;
	
	@Mock
	AEMService aemService;

	@Mock
	NationalityCodeServiceImpl nationalityService;
	
	private String memberId;
	private String origin;
	private String destination;
	private String mbToken;
	private ProfilePreference profilePreference;
	private LoginInfo loginInfo;
	@Before
	public void setUp(){
		memberId = "1910026122";
		origin   = "CHN";
		destination = "HKG";
		mbToken="TESTTOKEN8888888888";
		loginInfo = new LoginInfo();
		loginInfo.setMemberId("1910026122");
		loginInfo.setMmbToken(mbToken);
		
		profilePreference = new ProfilePreference();
		
		List<ProfileTravelDoc> personalTravelDocuments =  new ArrayList<>();
		profilePreference.setPersonalTravelDocuments(personalTravelDocuments);
		
		/**  prepare  companion travel doc data **/
		List<TravelCompanion> travelCompanions =  new ArrayList<>();
		profilePreference.setTravelCompanions(travelCompanions);
		TravelCompanion travelCompanion = new TravelCompanion();
		travelCompanions.add(travelCompanion);
		
		ProfileTravelDoc personalTravelDocument1 = new ProfileTravelDoc();
		travelCompanion.setTravelDocument(personalTravelDocument1);
		personalTravelDocument1.setExpiryDate("2033-01-04");
		personalTravelDocument1.setFamilyName("TEST");
		personalTravelDocument1.setGivenName("AA");
		personalTravelDocument1.setType("P");
		personalTravelDocument1.setDocumentNumber("111");
		
		personalTravelDocument1.setIssueCountryIosThree("CHN");
		personalTravelDocument1.setNationalityIosThree("HKG");
		personalTravelDocument1.setIssueCountry("CN");
		personalTravelDocument1.setNationality("HK");
		
		
		
		ProfileTravelDoc personalTravelDocument2 = new ProfileTravelDoc();
		personalTravelDocuments.add(personalTravelDocument2);
		personalTravelDocument2.setDocumentNumber("222");
		personalTravelDocument2.setExpiryDate("2033-11-04");
		personalTravelDocument2.setFamilyName("TEST");
		personalTravelDocument2.setGivenName("AA");
		personalTravelDocument2.setType("P");
		personalTravelDocument2.setIssueCountryIosThree("CHN");
		personalTravelDocument2.setNationalityIosThree("HKG");
		personalTravelDocument2.setIssueCountry("CN");
		personalTravelDocument2.setNationality("HK");
		
		ProfileTravelDoc personalTravelDocument3 = new ProfileTravelDoc();
		personalTravelDocuments.add(personalTravelDocument3);
		personalTravelDocument3.setDocumentNumber("333");
		personalTravelDocument3.setExpiryDate("2033-11-04");
		personalTravelDocument3.setFamilyName("TEST");
		personalTravelDocument3.setGivenName("AA");
		personalTravelDocument3.setType("V");
		personalTravelDocument3.setIssueCountryIosThree("CHN");
		personalTravelDocument3.setNationalityIosThree("HKG");
		personalTravelDocument3.setIssueCountry("CN");
		personalTravelDocument3.setNationality("HK");
	}
	
	@Test
	public void all_traveldoc_valid_test() throws BusinessBaseException{
		
		List<String> origins = new ArrayList<>();
		origins.add("CN");
		origins.add("**");
		List<String> destinations = new ArrayList<>();
		destinations.add("HK");
		destinations.add("**");
		
		List<String> typeCodes = new ArrayList<>();
		typeCodes.add("P");
		typeCodes.add("V");
		typeCodes.add("M");
		
		List<String> travelDocTypes = new ArrayList<>();
		travelDocTypes.add(TBConstants.TRAVEL_DOC_PRIMARY);
		when(retrieveProfileService.retrievePreference(memberId, mbToken)).thenReturn(profilePreference);
		 
		when(nationalityService.findTwoCountryCodeByThreeCountryCode("CHN")).thenReturn("CN");
		when(nationalityService.findTwoCountryCodeByThreeCountryCode("HKG")).thenReturn("HK");
		when(tbTravelDocOdCacheHelper.findTravelDocVersion(MMBConstants.APP_CODE,origins,destinations)).thenReturn(5);  // version 5
		when(tbTravelDocListCacheHelper.findTravelDocCodeByVersion(5, travelDocTypes)).thenReturn(typeCodes);
		ProfileTravelDocResponseDTO responseDTO =  memberProfileBusinessImpl.retrieveMemberProfileTravelDoc(memberId, origin, destination, false, false,loginInfo);
		assertEquals(3, responseDTO.getTravelDocuments().size());
		
	}
	@Test
	public void partial_traveldoc_valid_test() throws BusinessBaseException{
		
		List<String> origins = new ArrayList<>();
		origins.add("CN");
		origins.add("**");
		List<String> destinations = new ArrayList<>();
		destinations.add("HK");
		destinations.add("**");
		
		List<String> typeCodes = new ArrayList<>();
		//typeCodes.add("P");
		typeCodes.add("V");
		typeCodes.add("M");
		
		List<String> travelDocTypes = new ArrayList<>();
		travelDocTypes.add(TBConstants.TRAVEL_DOC_PRIMARY);
		when(retrieveProfileService.retrievePreference(memberId, mbToken)).thenReturn(profilePreference);
		 
		when(nationalityService.findTwoCountryCodeByThreeCountryCode("CHN")).thenReturn("CN");
		when(nationalityService.findTwoCountryCodeByThreeCountryCode("HKG")).thenReturn("HK");
		when(tbTravelDocOdCacheHelper.findTravelDocVersion(MMBConstants.APP_CODE,origins,destinations)).thenReturn(5);  // version 5
		when(tbTravelDocListCacheHelper.findTravelDocCodeByVersion(5, travelDocTypes)).thenReturn(typeCodes);
		ProfileTravelDocResponseDTO responseDTO =  memberProfileBusinessImpl.retrieveMemberProfileTravelDoc(memberId, origin, destination, false, false,loginInfo);
		assertEquals(1, responseDTO.getTravelDocuments().size());
		assertEquals("V", responseDTO.getTravelDocuments().get(0).getType());
		assertEquals("2033-11-04", responseDTO.getTravelDocuments().get(0).getExpiryDate());
		assertEquals("TEST", responseDTO.getTravelDocuments().get(0).getFamilyName());
		assertEquals("AA", responseDTO.getTravelDocuments().get(0).getGivenName());
		assertEquals("CHN", responseDTO.getTravelDocuments().get(0).getIssueCountry());
		assertEquals("HKG", responseDTO.getTravelDocuments().get(0).getNationality());
	}
	
	
}
