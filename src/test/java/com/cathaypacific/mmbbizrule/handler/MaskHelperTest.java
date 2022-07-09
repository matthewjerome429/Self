package com.cathaypacific.mmbbizrule.handler;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cathaypacific.mbcommon.enums.mask.MaskFieldName;
import com.cathaypacific.mbcommon.model.common.MaskInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateAdultSegmentInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateEmailDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateEmergencyContactDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdatePassengerDetailsRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdatePhoneInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateTravelDocDTO;
import com.cathaypacific.mmbbizrule.dto.request.passengerdetails.UpdateTsDTO;

@RunWith(MockitoJUnitRunner.class)
public class MaskHelperTest {
	
	@InjectMocks
	private MaskHelper maskHelper;
	
	@Mock
	private MbTokenCacheRepository mbTokenCacheRepository;

	@Test
	public void testMask() {
		// fail("Not yet implemented");
	}

	@Test
	public void testUnmask() {
		UpdatePassengerDetailsRequestDTO request = new UpdatePassengerDetailsRequestDTO();
		request.setPassengerId("1");
		request.setRloc("AAABBB");
		
		UpdateEmailDTO email = new UpdateEmailDTO();
		email.setEmail("abc@●●●●●");
		request.setEmail(email);
		
		UpdatePhoneInfoDTO phoneInfo = new UpdatePhoneInfoDTO();
		phoneInfo.setPhoneNo("1333333●●●●");
		request.setPhoneInfo(phoneInfo);
		
		UpdateEmergencyContactDTO emergencyContact = new UpdateEmergencyContactDTO();
		emergencyContact.setPhoneNo("1863333●●●●");
		request.setEmergencyContact(emergencyContact);
		
		UpdateTsDTO ktn = new UpdateTsDTO();
		ktn.setNumber("●●●●1111");
		request.setKtn(ktn);
		
		UpdateTsDTO redress = new UpdateTsDTO();
		redress.setNumber("●●●●2222");
		request.setRedress(redress);
		
		UpdateAdultSegmentInfoDTO segment = new UpdateAdultSegmentInfoDTO();
		segment.setSegmentId("1");
		
		UpdateTravelDocDTO primaryTravelDoc = new UpdateTravelDocDTO();
		primaryTravelDoc.setTravelDocumentNumber("●●●●3333");
		primaryTravelDoc.setDateOfBirth("●●●●-02-●●");
		segment.setPrimaryTravelDoc(primaryTravelDoc);
		
		UpdateTravelDocDTO secondaryTravelDoc = new UpdateTravelDocDTO();
		secondaryTravelDoc.setTravelDocumentNumber("●●●●5555");
		segment.setSecondaryTravelDoc(secondaryTravelDoc);
		
		List<UpdateAdultSegmentInfoDTO> segments = new ArrayList<>();
		segments.add(segment);
		request.setSegments(segments);
		
		ArrayList<MaskInfo> maskInfos = new ArrayList<>();
		
		MaskInfo emailMaskInfo = new MaskInfo();
		emailMaskInfo.setFieldName(MaskFieldName.EMAIL);
		emailMaskInfo.setMaskedValue("abc@●●●●●");
		emailMaskInfo.setOriginalValue("abc@a.com");
		emailMaskInfo.setPassengerId("1");
		maskInfos.add(emailMaskInfo);
		
		MaskInfo emergencyContactMaskInfo = new MaskInfo();
		emergencyContactMaskInfo.setFieldName(MaskFieldName.EMER_PHONE_NO);
		emergencyContactMaskInfo.setMaskedValue("1863333●●●●");
		emergencyContactMaskInfo.setOriginalValue("18633333333");
		emergencyContactMaskInfo.setPassengerId("1");
		maskInfos.add(emergencyContactMaskInfo);
		
		MaskInfo ktnMaskInfo = new MaskInfo();
		ktnMaskInfo.setFieldName(MaskFieldName.KTN);
		ktnMaskInfo.setMaskedValue("●●●●1111");
		ktnMaskInfo.setOriginalValue("11111111");
		ktnMaskInfo.setPassengerId("1");
		maskInfos.add(ktnMaskInfo);
		
		MaskInfo phoneInfoMaskInfo = new MaskInfo();
		phoneInfoMaskInfo.setFieldName(MaskFieldName.PHONE_NO);
		phoneInfoMaskInfo.setMaskedValue("1333333●●●●");
		phoneInfoMaskInfo.setOriginalValue("13333333333");
		phoneInfoMaskInfo.setPassengerId("1");
		maskInfos.add(phoneInfoMaskInfo);
		
		MaskInfo redressMaskInfo = new MaskInfo();
		redressMaskInfo.setFieldName(MaskFieldName.REDRESS_NO);
		redressMaskInfo.setMaskedValue("●●●●2222");
		redressMaskInfo.setOriginalValue("22222222");
		redressMaskInfo.setPassengerId("1");
		maskInfos.add(redressMaskInfo);
		
		MaskInfo priBirthYearMaskInfo = new MaskInfo();
		priBirthYearMaskInfo.setFieldName(MaskFieldName.PRI_TRAVELDOC_DOB_YEAR);
		priBirthYearMaskInfo.setMaskedValue("●●●●");
		priBirthYearMaskInfo.setOriginalValue("2000");
		priBirthYearMaskInfo.setPassengerId("1");
		priBirthYearMaskInfo.setSegmentId("1");
		maskInfos.add(priBirthYearMaskInfo);
		
		MaskInfo priBirthDayMaskInfo = new MaskInfo();
		priBirthDayMaskInfo.setFieldName(MaskFieldName.PRI_TRAVELDOC_DOB_DAY);
		priBirthDayMaskInfo.setMaskedValue("●●");
		priBirthDayMaskInfo.setOriginalValue("01");
		priBirthDayMaskInfo.setPassengerId("1");
		priBirthDayMaskInfo.setSegmentId("1");
		maskInfos.add(priBirthDayMaskInfo);
		
		MaskInfo priTravelDocNumMaskInfo = new MaskInfo();
		priTravelDocNumMaskInfo.setFieldName(MaskFieldName.PRI_TRAVELDOC_NO);
		priTravelDocNumMaskInfo.setMaskedValue("●●●●3333");
		priTravelDocNumMaskInfo.setOriginalValue("33333333");
		priTravelDocNumMaskInfo.setPassengerId("1");
		priTravelDocNumMaskInfo.setSegmentId("1");
		maskInfos.add(priTravelDocNumMaskInfo);
		
		MaskInfo secTravelDocNumMaskInfo = new MaskInfo();
		secTravelDocNumMaskInfo.setFieldName(MaskFieldName.SEC_TRAVELDOC_NO);
		secTravelDocNumMaskInfo.setMaskedValue("●●●●5555");
		secTravelDocNumMaskInfo.setOriginalValue("55555555");
		secTravelDocNumMaskInfo.setPassengerId("1");
		secTravelDocNumMaskInfo.setSegmentId("1");
		maskInfos.add(secTravelDocNumMaskInfo);
		
		when(mbTokenCacheRepository.get(anyString(), anyObject(),
				anyString(), anyObject())).thenReturn(maskInfos);
		
		maskHelper.unmask(request);
		
		Assert.assertEquals("abc@a.com", request.getEmail().getEmail());
		Assert.assertEquals("18633333333", request.getEmergencyContact().getPhoneNo());
		Assert.assertEquals("11111111", request.getKtn().getNumber());
		Assert.assertEquals("13333333333", request.getPhoneInfo().getPhoneNo());
		Assert.assertEquals("22222222", request.getRedress().getNumber());
		Assert.assertEquals("2000-02-01", request.getSegments().get(0).getPrimaryTravelDoc().getDateOfBirth());
		Assert.assertEquals("33333333", request.getSegments().get(0).getPrimaryTravelDoc().getTravelDocumentNumber());
		Assert.assertEquals("55555555", request.getSegments().get(0).getSecondaryTravelDoc().getTravelDocumentNumber());
	}

	@Test
	public void testGetOriginalValue() {
		ArrayList<MaskInfo> maskInfos = new ArrayList<>();
		
		MaskInfo emailMaskInfo = new MaskInfo();
		emailMaskInfo.setFieldName(MaskFieldName.EMAIL);
		emailMaskInfo.setMaskedValue("abc@●●●●●");
		emailMaskInfo.setOriginalValue("abc@a.com");
		emailMaskInfo.setPassengerId("1");
		maskInfos.add(emailMaskInfo);
		
		when(mbTokenCacheRepository.get(anyString(), anyObject(),
				anyString(), anyObject())).thenReturn(maskInfos);
		
		String unmaskedEmail = maskHelper.getOriginalValue(MaskFieldName.EMAIL, "abc@●●●●●", "1", null, maskInfos);
		Assert.assertEquals("abc@a.com", unmaskedEmail);
	}

}
