package com.cathaypacific.mmbbizrule.business.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.business.MemberProfileBusiness;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.socialaccount.ClsSocialAccountResponse;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.socialaccount.SocialProfileRecord;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileService;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.service.impl.NationalityCodeServiceImpl;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocListCacheHelper;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocOdCacheHelper;
import com.cathaypacific.mmbbizrule.dto.response.memberprofile.ProfileTravelDocResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.memberprofile.SocialAccountResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.memberprofile.TravelDocumentRecordDTO;
import com.cathaypacific.mmbbizrule.dto.response.memberprofile.v2.CompanionTravelerInformationDTO;
import com.cathaypacific.mmbbizrule.dto.response.memberprofile.v2.EmergencyContactDTO;
import com.cathaypacific.mmbbizrule.dto.response.memberprofile.v2.PhoneNumberDTO;
import com.cathaypacific.mmbbizrule.dto.response.memberprofile.v2.ProfileTravelDocDTO;
import com.cathaypacific.mmbbizrule.dto.response.memberprofile.v2.TravelDocumentDTO;
import com.cathaypacific.mmbbizrule.dto.response.memberprofile.v2.TravelerInformationDTO;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePreference;
import com.cathaypacific.mmbbizrule.model.profile.ProfileTravelDoc;
import com.cathaypacific.mmbbizrule.model.profile.TravelCompanion;
import com.cathaypacific.mmbbizrule.model.profile.v2.CompanionTravelerInformation;
import com.cathaypacific.mmbbizrule.model.profile.v2.ProfilePreferenceV2;
import com.cathaypacific.mmbbizrule.model.profile.v2.ProfileTravelDocV2;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;

@Service
public class MemberProfileBusinessImpl implements MemberProfileBusiness {

	private static LogAgent logger = LogAgent.getLogAgent(MemberProfileBusinessImpl.class);

	@Autowired
	private RetrieveProfileService retrieveProfileService;
	
	@Autowired
	private TbTravelDocOdCacheHelper tbTravelDocOdCacheHelper;

	@Autowired
	private TbTravelDocListCacheHelper tbTravelDocListCacheHelper;
	
	@Autowired
	NationalityCodeServiceImpl nationalityService;

	@Override
	public ProfilePersonInfo retrieveMemberProfileSummary(String memberId,LoginInfo loginInfo) throws BusinessBaseException {

		ProfilePersonInfo profilePersonInfo=retrieveProfileService.retrievePersonInfo(memberId, loginInfo.getMmbToken());	
		profilePersonInfo.setUserType(loginInfo.getUserType());
		return profilePersonInfo;
	}


	@Override
	public ProfileTravelDocResponseDTO retrieveMemberProfileTravelDoc(String memberId, String orgin,
			String destination, boolean requirePriDocs, boolean requireSecDocs,LoginInfo loginInfo) throws BusinessBaseException {

		ProfilePreference profilePreference = retrieveProfileService.retrievePreference(memberId, loginInfo.getMmbToken());
		ProfilePersonInfo profilePersonInfo = retrieveProfileService.retrievePersonInfo(memberId, loginInfo.getMmbToken());
		ProfileTravelDocResponseDTO responseDTO = null;
		if (profilePreference != null){
			responseDTO = convertToProfileTravelDocResponseDTO(profilePreference, profilePersonInfo,
					orgin, destination, requirePriDocs, requireSecDocs);
			if(!CollectionUtils.isEmpty(responseDTO.getTravelDocuments())){
				//remove the travel document which haven't type
				responseDTO.setTravelDocuments(responseDTO.getTravelDocuments().stream().filter(td->StringUtils.isNotEmpty(td.getType())).collect(Collectors.toList()));
			}
		}

		return responseDTO;
	}
	
	@Override
	public SocialAccountResponseDTO retrieveSocialAccount(String memberId) {
		ClsSocialAccountResponse socialAccountResponse = retrieveProfileService.retrieveSocialAccount(memberId);
		return convertToSocialAccountResponseDTO(socialAccountResponse);
	}

	@Override
	public ProfileTravelDocDTO retrieveMemberProfileTravelDocV2(String memberId, String origin, String destination,
			boolean requirePriDocs, boolean requireSecDocs, LoginInfo loginInfo) {
		ProfilePreferenceV2 profilePreferenceV2 = retrieveProfileService.retrievePreferenceV2(memberId, loginInfo.getMmbToken());
		ProfilePersonInfo profilePersonInfo = retrieveProfileService.retrievePersonInfo(memberId, loginInfo.getMmbToken());
		ProfileTravelDocDTO responseDTO = null;
		if (profilePreferenceV2 != null){
			responseDTO = convertToProfileTravelDocDTOV2(profilePreferenceV2, profilePersonInfo,
					origin, destination, requirePriDocs, requireSecDocs);
			if(responseDTO.getCustomerTravelerInformation() != null && !CollectionUtils.isEmpty(responseDTO.getCustomerTravelerInformation().getCustomerTravelDocument())){
				//remove the travel document which do not have type and is expired 
				responseDTO.getCustomerTravelerInformation().setCustomerTravelDocument(responseDTO.getCustomerTravelerInformation().getCustomerTravelDocument().stream().
						filter(td->StringUtils.isNotEmpty(td.getTravelDocumentType()) && !DateUtil.beforeSystemTime(td.getExpiryDate(), DateUtil.DATE_PATTERN_YYYY_MM_DD)).collect(Collectors.toList()));
				if(!CollectionUtils.isEmpty(responseDTO.getCompanionTravelerInformation())){
					responseDTO.setCompanionTravelerInformation(responseDTO.getCompanionTravelerInformation().stream().
					filter(cti -> cti.getCompanionTravelDocument() != null && !DateUtil.beforeSystemTime(cti.getCompanionTravelDocument().getExpiryDate(), DateUtil.DATE_PATTERN_YYYY_MM_DD)).collect(Collectors.toList()));
				}
			}
		}
		
		return responseDTO;
	}
	
	/**
	 * convert To ProfileTravelDocDTO
	 * 
	 * @param profilePreferenceV2
	 * @param profilePersonInfo
	 * @param origin
	 * @param destination
	 * @param requirePriDocs
	 * @param requireSecDocs
	 * @return
	 */
	private ProfileTravelDocDTO convertToProfileTravelDocDTOV2(ProfilePreferenceV2 profilePreferenceV2,
			ProfilePersonInfo profilePersonInfo, String origin, String destination, boolean requirePriDocs,
			boolean requireSecDocs) {
		ProfileTravelDocDTO responseDTO = new ProfileTravelDocDTO();
		// check if travel doc is empty
		if(profilePreferenceV2 == null || 
				(profilePreferenceV2.getCustomerTravelerInformation() == null
						&& CollectionUtils.isEmpty(profilePreferenceV2.getCompanionTravelerInformation()))){
			logger.info("Canot find traveldoc from member profile V2.");
			return responseDTO;
		}
		
		boolean checkOD = !StringUtils.isEmpty(origin)||!StringUtils.isEmpty(destination);
		if (checkOD) {
			List<String> availableTravelDocTypes = getAvailableTravelDocTypesByOD(origin, destination, requirePriDocs, requireSecDocs);
			if (!CollectionUtils.isEmpty(profilePreferenceV2.getCompanionTravelerInformation())){
				profilePreferenceV2.setCompanionTravelerInformation(profilePreferenceV2.getCompanionTravelerInformation().stream()
						.filter(companionInfo -> companionInfo.getCompanionTravelDocument() != null && availableTravelDocTypes.contains(companionInfo.getCompanionTravelDocument().getTravelDocumentType())).collect(Collectors.toList()));
			}
			if (profilePreferenceV2.getCustomerTravelerInformation() != null && !CollectionUtils.isEmpty(profilePreferenceV2.getCustomerTravelerInformation().getCustomerTravelDocument())){
				profilePreferenceV2.getCustomerTravelerInformation().setCustomerTravelDocument(
						profilePreferenceV2.getCustomerTravelerInformation().getCustomerTravelDocument().stream()
						.filter(travelDoc -> availableTravelDocTypes.contains(travelDoc.getTravelDocumentType())).collect(Collectors.toList()));
			}
		}
		// add member self travel doc
		String memberGender = Optional.ofNullable(profilePersonInfo).map(ProfilePersonInfo::getGender).orElse(null);
		if (profilePreferenceV2.getCustomerTravelerInformation() != null){
			addCustomerTravelerInfoToResponseV2(responseDTO, memberGender, profilePreferenceV2);
		}
		//companions travel doc
		if (!CollectionUtils.isEmpty(profilePreferenceV2.getCompanionTravelerInformation())){
			addCompanionTravelerInfoToResponseV2(responseDTO, profilePreferenceV2);
		}
		return responseDTO;
	}


	/**
	 * add CompanionTravelerInfo To ResponseV2
	 * @param responseDTO
	 * @param profilePreferenceV2
	 */
	private void addCompanionTravelerInfoToResponseV2(ProfileTravelDocDTO responseDTO,
			ProfilePreferenceV2 profilePreferenceV2) {
		if(CollectionUtils.isEmpty(responseDTO.getCompanionTravelerInformation())){
			responseDTO.setCompanionTravelerInformation(new ArrayList<CompanionTravelerInformationDTO>());
		}
		for(CompanionTravelerInformation companionInfo : profilePreferenceV2.getCompanionTravelerInformation()){
			CompanionTravelerInformationDTO companionInfoDTO = new CompanionTravelerInformationDTO();
			companionInfoDTO.setContactEmailAddress(companionInfo.getContactEmailAddress());
			if(StringUtils.isNotEmpty(companionInfo.getDateOfBirth())){
				companionInfoDTO.setDateOfBirth(DateUtil.convertDateFormat(companionInfo.getDateOfBirth(),DateUtil.DATE_PATTERN_DDMMYYYY,DateUtil.DATE_PATTERN_YYYY_MM_DD));
			}
			companionInfoDTO.setGender(companionInfo.getGender());
			companionInfoDTO.setKnownTravelerNumber(companionInfo.getKnownTravelerNumber());
			companionInfoDTO.setRedressNumber(companionInfo.getRedressNumber());
			companionInfoDTO.setTitle(companionInfo.getTitle());
			companionInfoDTO.setCountryOfResidence(companionInfo.getCountryOfResidence());
			if(companionInfo.getContactMobileNumber() != null){
				PhoneNumberDTO contactMobileNumberDTO = new PhoneNumberDTO();
				contactMobileNumberDTO.setCountryCode(BizRulesUtil.convertCallingCodeToIso3CountryCode(companionInfo.getContactMobileNumber().getCountryCode()));
				contactMobileNumberDTO.setIso(companionInfo.getContactMobileNumber().getIso());
				contactMobileNumberDTO.setNumber(companionInfo.getContactMobileNumber().getNumber());
				companionInfoDTO.setContactMobileNumber(contactMobileNumberDTO);
			}
			if(companionInfo.getEmergencyContact() != null){
				EmergencyContactDTO emergencyContactDTO = new EmergencyContactDTO();
				emergencyContactDTO.setName(companionInfo.getEmergencyContact().getName());
				if(companionInfo.getEmergencyContact().getPhoneNumber() != null){
					PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();
					phoneNumberDTO.setCountryCode(BizRulesUtil.convertCallingCodeToIso3CountryCode(companionInfo.getEmergencyContact().getPhoneNumber().getCountryCode()));
					phoneNumberDTO.setIso(companionInfo.getEmergencyContact().getPhoneNumber().getIso());
					phoneNumberDTO.setNumber(companionInfo.getEmergencyContact().getPhoneNumber().getNumber());
					emergencyContactDTO.setPhoneNumber(phoneNumberDTO);
				}
				companionInfoDTO.setEmergencyContact(emergencyContactDTO);
			}
			if(companionInfo.getCompanionTravelDocument() != null){
				TravelDocumentDTO companionTravelDocDTO = new TravelDocumentDTO();
				addTravelDocToResponseV2(companionTravelDocDTO, companionInfo.getCompanionTravelDocument());
				companionInfoDTO.setCompanionTravelDocument(companionTravelDocDTO);
			}
			responseDTO.getCompanionTravelerInformation().add(companionInfoDTO);
		}
	}


	/**
	 * add CustomerTravelerInfo To ResponseV2
	 * @param responseDTO
	 * @param memberGender
	 * @param profilePreferenceV2
	 */
	private void addCustomerTravelerInfoToResponseV2(ProfileTravelDocDTO responseDTO, String memberGender,
			ProfilePreferenceV2 profilePreferenceV2) {
		if(responseDTO.getCustomerTravelerInformation() == null){
			responseDTO.setCustomerTravelerInformation(new TravelerInformationDTO());
		}
		responseDTO.getCustomerTravelerInformation().setGender(memberGender);
		if(StringUtils.isNotEmpty(profilePreferenceV2.getDateOfBirth())){
			responseDTO.getCustomerTravelerInformation().setDateOfBirth(DateUtil.convertDateFormat(profilePreferenceV2.getDateOfBirth(),DateUtil.DATE_PATTERN_DDMMYYYY,DateUtil.DATE_PATTERN_YYYY_MM_DD));
		}
		responseDTO.getCustomerTravelerInformation().setCountryOfResidence(profilePreferenceV2.getCustomerTravelerInformation().getCountryOfResidence());
		responseDTO.getCustomerTravelerInformation().setKnownTravelerNumber(profilePreferenceV2.getCustomerTravelerInformation().getKnownTravelerNumber());
		responseDTO.getCustomerTravelerInformation().setRedressNumber(profilePreferenceV2.getCustomerTravelerInformation().getRedressNumber());
		if(profilePreferenceV2.getCustomerTravelerInformation().getEmergencyContact() != null){
			EmergencyContactDTO emergencyContact = new EmergencyContactDTO();
			emergencyContact.setName(profilePreferenceV2.getCustomerTravelerInformation().getEmergencyContact().getName());
			if(profilePreferenceV2.getCustomerTravelerInformation().getEmergencyContact().getPhoneNumber() != null){
				PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();
				phoneNumberDTO.setCountryCode(BizRulesUtil.convertCallingCodeToIso3CountryCode(profilePreferenceV2.getCustomerTravelerInformation().getEmergencyContact().getPhoneNumber().getCountryCode()));
				phoneNumberDTO.setIso(profilePreferenceV2.getCustomerTravelerInformation().getEmergencyContact().getPhoneNumber().getIso());
				phoneNumberDTO.setNumber(profilePreferenceV2.getCustomerTravelerInformation().getEmergencyContact().getPhoneNumber().getNumber());
				emergencyContact.setPhoneNumber(phoneNumberDTO);
			}
			responseDTO.getCustomerTravelerInformation().setEmergencyContact(emergencyContact);
			
		}
		if (!CollectionUtils.isEmpty(profilePreferenceV2.getCustomerTravelerInformation().getCustomerTravelDocument())){
			responseDTO.getCustomerTravelerInformation().setCustomerTravelDocument(new ArrayList<TravelDocumentDTO>());
			for(ProfileTravelDocV2 travelDoc : profilePreferenceV2.getCustomerTravelerInformation().getCustomerTravelDocument()){
				TravelDocumentDTO travelDocDTO = new TravelDocumentDTO();
				addTravelDocToResponseV2(travelDocDTO, travelDoc);
				responseDTO.getCustomerTravelerInformation().getCustomerTravelDocument().add(travelDocDTO);
			}
		}
	}

	

	/**
	 * add TravelDoc To ResponseV2
	 * @param travelDocDTO
	 * @param travelDoc
	 */
	private void addTravelDocToResponseV2(TravelDocumentDTO travelDocDTO, ProfileTravelDocV2 travelDoc) {
		travelDocDTO.setCountryOfIssue(travelDoc.getCountryOfIssue());
		// @JsonFormat(pattern="DDMMYYYY") ==> (pattern="yyyy-MM-dd")
		if(StringUtils.isNotEmpty(travelDoc.getExpiryDate())){
			travelDocDTO.setExpiryDate(DateUtil.convertDateFormat(travelDoc.getExpiryDate(),DateUtil.DATE_PATTERN_DDMMYYYY,DateUtil.DATE_PATTERN_YYYY_MM_DD));
		}
		travelDocDTO.setFamilyName(travelDoc.getFamilyName());
		travelDocDTO.setGivenName(travelDoc.getGivenName());
		travelDocDTO.setNationality(travelDoc.getNationality());
		travelDocDTO.setPreferredTravelDocumentIndicator(travelDoc.getPreferredTravelDocumentIndicator());
		travelDocDTO.setTravelDocumentNo(travelDoc.getTravelDocumentNo());
		travelDocDTO.setTravelDocumentType(travelDoc.getTravelDocumentType());
		travelDocDTO.setTrustedTravelDocumentIndicator(travelDoc.getTrustedTravelDocumentIndicator());
	}


	/**
	 * convert SocialAccountResponse to SocialAccountResponseDTO
	 * @param socialAccountResponse
	 * @return SocialAccountResponseDTO
	 */
	private SocialAccountResponseDTO convertToSocialAccountResponseDTO(ClsSocialAccountResponse socialAccountResponse) {
		if (socialAccountResponse == null) {
			return null;
		}

		SocialAccountResponseDTO responseDTO = new SocialAccountResponseDTO();
		List<String> socialAccounts = new ArrayList<>();
		if (!CollectionUtils.isEmpty(socialAccountResponse.getSocialProfileRecord())) {
			for (SocialProfileRecord socialProfileRecord : socialAccountResponse.getSocialProfileRecord()) {
				if (!StringUtils.isEmpty(socialProfileRecord.getSocialChannelCode())
						&& !socialAccounts.contains(socialProfileRecord.getSocialChannelCode())) {
					socialAccounts.add(socialProfileRecord.getSocialChannelCode());
				}
			}
		}

		responseDTO.setSocialAccounts(socialAccounts);
		return responseDTO;
	}


	/**
	 * profile Details Convert To DTO
	 * 
	 * @param preferenceRecord
	 * @param origin
	 * @param destination
	 * @return
	 */

	private ProfileTravelDocResponseDTO convertToProfileTravelDocResponseDTO(ProfilePreference profilePreference,
			ProfilePersonInfo profilePersonInfo, String origin, String destination, boolean requirePriDocs,
			boolean requireSecDocs) {

		ProfileTravelDocResponseDTO responseDTO = new ProfileTravelDocResponseDTO();
		// check if travel doc is empty
		if(profilePreference == null || 
				(CollectionUtils.isEmpty(profilePreference.getPersonalTravelDocuments())
						&& CollectionUtils.isEmpty(profilePreference.getTravelCompanions()))){
			logger.info("Canot find traveldoc from member profile." );
			return responseDTO;
		}
		
		boolean checkOD = !StringUtils.isEmpty(origin)||!StringUtils.isEmpty(destination);
		
		List<ProfileTravelDoc> memberSelfTravelDocs = profilePreference.getPersonalTravelDocuments();
		List<TravelCompanion> travelCompanions = null;
		// check companions
		if (!CollectionUtils.isEmpty(profilePreference.getTravelCompanions())) {
			travelCompanions = profilePreference.getTravelCompanions().stream()
					.filter(travelCompanion -> Objects.nonNull(travelCompanion.getTravelDocument())
				).collect(Collectors.toList());
		}
		
		if (checkOD) {
			List<String> availableTravelDocTypes = getAvailableTravelDocTypesByOD(origin, destination, requirePriDocs, requireSecDocs);
			memberSelfTravelDocs = filterOutInvalidTravelInfo(availableTravelDocTypes, memberSelfTravelDocs,
					ProfileTravelDoc::getType);
			travelCompanions = filterOutInvalidTravelInfo(availableTravelDocTypes, travelCompanions,
					travelCompanion -> travelCompanion.getTravelDocument().getType());
			//profilePreference.getTravelCompanions()
		}
		// member self travel doc
		String memberGender = Optional.ofNullable(profilePersonInfo).map(ProfilePersonInfo::getGender).orElse(null);
		String memberDateOfBirth = Optional.ofNullable(profilePersonInfo).map(ProfilePersonInfo::getDateOfBirth).orElse(null);
		responseDTO.addTravelDocuments(transferToDTO(memberSelfTravelDocs, true,
				profileTravelDoc -> profileTravelDoc, profileTravelDoc -> memberGender, profileTravelDoc -> memberDateOfBirth));
		//companions travel doc
		responseDTO.addTravelDocuments(transferToDTO(travelCompanions, false,
				TravelCompanion::getTravelDocument, TravelCompanion::getGender, TravelCompanion::getDateOfBirth));
		return responseDTO;

	}
	
	/**
	 * Filter out travel info in which travel document type is invalid.
	 * 
	 * @param <T>
	 *            type which contains travel document
	 * @param availableTravelDocTypes
	 * @param travelInfos
	 * @param travelDocTypeGetter method to get travel document type from travel info object.
	 * @return
	 */
	private <T> List<T> filterOutInvalidTravelInfo(List<String> availableTravelDocTypes,
			List<T> travelInfos, Function<T, String> travelDocTypeGetter){
		
		if(CollectionUtils.isEmpty(availableTravelDocTypes) || CollectionUtils.isEmpty(travelInfos)){
			return Collections.emptyList();
		}
		return travelInfos.stream().filter(
				travelInfo -> availableTravelDocTypes.contains(travelDocTypeGetter.apply(travelInfo))
			).collect(Collectors.toList());
		
	}
	/**
	 * TravelDocumentRecord transfer to TravelDocumentRecordDTO null value, it
	 * means origin and destination are all null, return all travel documents
	 * otherwise, it needs to filter.
	 * 
	 * @param <T>
	 *            type which contains travel document
	 * @return
	 */
	private <T> List<TravelDocumentRecordDTO> transferToDTO(List<T> travelInfos,
			boolean isMemberTravelDoc, Function<T, ProfileTravelDoc> profileTravelDocGetter,
			Function<T, String> genderGetter, Function<T, String> dateOfBirthGetter) {
	
		if(CollectionUtils.isEmpty(travelInfos)){
			return Collections.emptyList();
		}
		List<TravelDocumentRecordDTO> travelDocumentRecords = new ArrayList<>();
		for (T travelInfo: travelInfos) {
			ProfileTravelDoc profileTravelDoc = profileTravelDocGetter.apply(travelInfo);
			TravelDocumentRecordDTO travelDocumentRecord = new TravelDocumentRecordDTO();
			travelDocumentRecords.add(travelDocumentRecord);
			travelDocumentRecord.setDocumentNumber(profileTravelDoc.getDocumentNumber());
			travelDocumentRecord.setExpiryDate(profileTravelDoc.getExpiryDate());
			travelDocumentRecord.setFamilyName(profileTravelDoc.getFamilyName());
			travelDocumentRecord.setGivenName(profileTravelDoc.getGivenName());
			travelDocumentRecord.setIssueCountry(profileTravelDoc.getIssueCountryIosThree());
			travelDocumentRecord.setNationality(profileTravelDoc.getNationalityIosThree());
			travelDocumentRecord.setType(profileTravelDoc.getType());
			travelDocumentRecord.setGender(genderGetter.apply(travelInfo));
			travelDocumentRecord.setDateOfBirth(dateOfBirthGetter.apply(travelInfo));
			travelDocumentRecord.setMemberTravelDoc(isMemberTravelDoc);
		}
		
		return travelDocumentRecords;
	}
	/**
	 * 1.select TB_TRAVEL_DOC_OD 2.filter travel doc type by TB_TRAVEL_DOC_LIST
	 * 
	 * @param originFlight
	 * @param destinationFlight
	 * @return
	 * 
	 */

	private List<String> getAvailableTravelDocTypesByOD(String originFlight, String destinationFlight, boolean requirePriDocs, boolean requireSecDocs) {

		List<String> origins = new ArrayList<>();
		String originIosTwo = nationalityService.findTwoCountryCodeByThreeCountryCode(originFlight);
		if (StringUtils.isNotEmpty(originIosTwo)) {
			origins.add(originIosTwo);
		}
		origins.add(TBConstants.TRAVEL_DOC_WILDCARD);
		List<String> destinations = new ArrayList<>();
		String destinationTwo = nationalityService.findTwoCountryCodeByThreeCountryCode(destinationFlight);
		if (StringUtils.isNotEmpty(destinationTwo)) {
			destinations.add(destinationTwo);
		}
		
		destinations.add(TBConstants.TRAVEL_DOC_WILDCARD);
		List<String> travelDocTypes = new ArrayList<>();
		
		if(BooleanUtils.isTrue(requirePriDocs)) {
			travelDocTypes.add(TBConstants.TRAVEL_DOC_PRIMARY);
		}
		
		if(BooleanUtils.isTrue(requireSecDocs)) {
			travelDocTypes.add(TBConstants.TRAVEL_DOC_SECONDARY);
		}
		
		if(BooleanUtils.isNotTrue(requirePriDocs) && BooleanUtils.isNotTrue(requireSecDocs) ){
			// default, return primary travel docs
			travelDocTypes.add(TBConstants.TRAVEL_DOC_PRIMARY);
		//  travelDocTypes.add(CompanionTravelDocConstants.SENCONDARY_TRAVEL_DOC_TYPE);
		}
		
		int version = tbTravelDocOdCacheHelper.findTravelDocVersion(MMBConstants.APP_CODE,origins, destinations);
		return tbTravelDocListCacheHelper.findTravelDocCodeByVersion(version, travelDocTypes);
	}
	
}
