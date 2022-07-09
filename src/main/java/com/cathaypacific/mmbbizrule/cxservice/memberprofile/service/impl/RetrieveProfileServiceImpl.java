package com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.cathaypacific.mbcommon.enums.booking.ContactInfoTypeEnum;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.constant.MemberProfileConstants;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.request.ClsAPIRequest;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.request.UserInformation;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.request.socialaccount.ClsSocialAccountRequest;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.request.v2.TravelDocumentRetrievalRequest;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.CustomerEmailAddressRecord;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.CustomerMobilePhoneRecord;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.OtherPreferenceRecord;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.PreferenceRecord;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.Profile;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.TravelDocumentRecord;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.socialaccount.ClsSocialAccountResponse;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.v2.CompanionTravelerInformationResponse;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.v2.TravelDocumentResponse;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.v2.TravelDocumentRetrievalResponse;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileService;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileServiceCacheHelper;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.service.impl.NationalityCodeServiceImpl;
import com.cathaypacific.mmbbizrule.dto.common.booking.PhoneInfoDTO;
import com.cathaypacific.mmbbizrule.model.profile.Contact;
import com.cathaypacific.mmbbizrule.model.profile.ProfileEmail;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePhoneInfo;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePreference;
import com.cathaypacific.mmbbizrule.model.profile.ProfileTravelDoc;
import com.cathaypacific.mmbbizrule.model.profile.TravelCompanion;
import com.cathaypacific.mmbbizrule.model.profile.v2.CompanionTravelerInformation;
import com.cathaypacific.mmbbizrule.model.profile.v2.EmergencyContact;
import com.cathaypacific.mmbbizrule.model.profile.v2.PhoneNumber;
import com.cathaypacific.mmbbizrule.model.profile.v2.ProfilePreferenceV2;
import com.cathaypacific.mmbbizrule.model.profile.v2.ProfileTravelDocV2;
import com.cathaypacific.mmbbizrule.model.profile.v2.TravelerInformation;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class RetrieveProfileServiceImpl implements RetrieveProfileService {

	private static LogAgent logger = LogAgent.getLogAgent(RetrieveProfileServiceImpl.class);
	
	Gson gson = new GsonBuilder().serializeNulls().create();
	
	@Autowired
	NationalityCodeServiceImpl nationalityService;
	
	@Autowired
	RetrieveProfileServiceCacheHelper retrieveProfileServiceCacheHelper;

	@Value("${cx.memberprofile.endpoint.path.summary}")
	private String endpointPathSummary;

	@Value("${cx.memberprofile.endpoint.path.details}")
	private String endpointPathDetails;
	
	@Value("${member.services.api.key}")
	private String apiKey;

	 
	
	@Override
	public ProfilePersonInfo retrievePersonInfo(String memberId, String mbToken) {
		Profile profile = retrieveMemberProfileSummary(memberId);
		if(profile != null){
			return convertToPersonInfo(profile);
		}
		return null;
	}

	@Override
	public ProfilePreference retrievePreference(String memberId, String mbToken) {
		PreferenceRecord preferenceRecord = retrieveMemberProfileDetails(memberId);
		if(preferenceRecord != null){
			return convertToProfilePreference(preferenceRecord);
		}
		return null;
	}
	
	@Override
	public ClsSocialAccountResponse retrieveSocialAccount(String memberId) {
		ClsSocialAccountRequest request = new ClsSocialAccountRequest();
		// TODO finish all variable setting
		request.setApplicationName(MMBUtil.getCurrentAppCode());
		request.setMemberNumber(memberId);
		return retrieveProfileServiceCacheHelper.retrieveSocialAccountDetails(request);
	}
	
	@Override
	public ProfilePreferenceV2 retrievePreferenceV2(String memberId, String mmbToken) {
		TravelDocumentRetrievalResponse response = retrieveMemberProfileDetailsV2(memberId);
		if(response != null){
			return convertToProfilePreferenceV2(response);
		}
		return null;
	}

	/**
	 * convert To ProfilePreferenceV2
	 * @param response
	 * @return
	 */
	private ProfilePreferenceV2 convertToProfilePreferenceV2(TravelDocumentRetrievalResponse response) {
		ProfilePreferenceV2 profilePreferenceV2 = new ProfilePreferenceV2();
		// add dob
		profilePreferenceV2.setDateOfBirth(response.getDateOfBirth());
		// convert Customer Part
		if(response.getCustomerTravelerInformation() != null) {
			TravelerInformation customerTravelerInformation = new TravelerInformation();
			convertCustomerTravelerInfoV2(response, customerTravelerInformation);
			profilePreferenceV2.setCustomerTravelerInformation(customerTravelerInformation);
		}
		// convert companion Part
		if(!CollectionUtils.isEmpty(response.getCompanionTravelerInformation())){
			profilePreferenceV2.setCompanionTravelerInformation(new ArrayList<CompanionTravelerInformation>());
			convertCompanionTravelerInfoV2(response, profilePreferenceV2);
		}
		return profilePreferenceV2;
	}

	/**
	 * @param response
	 * @param profilePreferenceV2
	 */
	private void convertCompanionTravelerInfoV2(TravelDocumentRetrievalResponse response,
			ProfilePreferenceV2 profilePreferenceV2) {
		for (CompanionTravelerInformationResponse companionTravelerInformationResponse : response.getCompanionTravelerInformation()){
			CompanionTravelerInformation companionTravelerInformation = new CompanionTravelerInformation();
			companionTravelerInformation.setContactEmailAddress(companionTravelerInformationResponse.getContactEmailAddress());
			companionTravelerInformation.setCountryOfResidence(companionTravelerInformationResponse.getCountryOfResidence());
			companionTravelerInformation.setDateOfBirth(companionTravelerInformationResponse.getDateOfBirth());
			companionTravelerInformation.setGender(companionTravelerInformationResponse.getGender());
			companionTravelerInformation.setKnownTravelerNumber(companionTravelerInformationResponse.getKnownTravelerNumber());
			companionTravelerInformation.setRedressNumber(companionTravelerInformationResponse.getRedressNumber());
			companionTravelerInformation.setTitle(companionTravelerInformationResponse.getTitle());
			if(companionTravelerInformationResponse.getContactMobileNumber() != null){
				PhoneNumber contactMobileNumber = new PhoneNumber();
				contactMobileNumber.setCountryCode(companionTravelerInformationResponse.getContactMobileNumber().getCountryCode());
				contactMobileNumber.setIso(companionTravelerInformationResponse.getContactMobileNumber().getIso());
				contactMobileNumber.setNumber(companionTravelerInformationResponse.getContactMobileNumber().getNumber());
				companionTravelerInformation.setContactMobileNumber(contactMobileNumber);
			}
			if(companionTravelerInformationResponse.getEmergencyContact() != null){
				EmergencyContact emergencyContact = new EmergencyContact();
				emergencyContact.setName(companionTravelerInformationResponse.getEmergencyContact().getName());
				if(companionTravelerInformationResponse.getEmergencyContact().getPhoneNumber() != null){
					PhoneNumber phoneNumber = new PhoneNumber();
					phoneNumber.setCountryCode(companionTravelerInformationResponse.getEmergencyContact().getPhoneNumber().getCountryCode());
					phoneNumber.setIso(companionTravelerInformationResponse.getEmergencyContact().getPhoneNumber().getIso());
					phoneNumber.setNumber(companionTravelerInformationResponse.getEmergencyContact().getPhoneNumber().getNumber());
					emergencyContact.setPhoneNumber(phoneNumber);
				}
				companionTravelerInformation.setEmergencyContact(emergencyContact);
			}
			if(companionTravelerInformationResponse.getCompanionTravelDocument() != null){
				ProfileTravelDocV2 companionTravelDocument = new ProfileTravelDocV2();
				addTravelDocToProfileTravelDocV2(companionTravelDocument, companionTravelerInformationResponse.getCompanionTravelDocument());
				companionTravelerInformation.setCompanionTravelDocument(companionTravelDocument);
			}
			profilePreferenceV2.getCompanionTravelerInformation().add(companionTravelerInformation);
		}
	}

	/**
	 * @param response
	 * @param customerTravelerInformation
	 */
	private void convertCustomerTravelerInfoV2(TravelDocumentRetrievalResponse response,
			TravelerInformation customerTravelerInformation) {
		customerTravelerInformation.setCountryOfResidence(response.getCustomerTravelerInformation().getCountryOfResidence());
		customerTravelerInformation.setKnownTravelerNumber(response.getCustomerTravelerInformation().getKnownTravelerNumber());
		customerTravelerInformation.setRedressNumber(response.getCustomerTravelerInformation().getRedressNumber());
		if(response.getCustomerTravelerInformation().getEmergencyContact() != null){
			EmergencyContact emergencyContact = new EmergencyContact();
			emergencyContact.setName(response.getCustomerTravelerInformation().getEmergencyContact().getName());
			if(response.getCustomerTravelerInformation().getEmergencyContact().getPhoneNumber() != null){
				PhoneNumber phoneNumber = new PhoneNumber();
				phoneNumber.setCountryCode(response.getCustomerTravelerInformation().getEmergencyContact().getPhoneNumber().getCountryCode());
				phoneNumber.setIso(response.getCustomerTravelerInformation().getEmergencyContact().getPhoneNumber().getIso());
				phoneNumber.setNumber(response.getCustomerTravelerInformation().getEmergencyContact().getPhoneNumber().getNumber());
				emergencyContact.setPhoneNumber(phoneNumber);
			}
			customerTravelerInformation.setEmergencyContact(emergencyContact);
		}
		if(!CollectionUtils.isEmpty(response.getCustomerTravelerInformation().getCustomerTravelDocument())){
			customerTravelerInformation.setCustomerTravelDocument(new ArrayList<ProfileTravelDocV2>());
			for(TravelDocumentResponse travelDocResponse : response.getCustomerTravelerInformation().getCustomerTravelDocument()){
				ProfileTravelDocV2 profileTravelDocV2 = new ProfileTravelDocV2();
				addTravelDocToProfileTravelDocV2(profileTravelDocV2, travelDocResponse);
				customerTravelerInformation.getCustomerTravelDocument().add(profileTravelDocV2);
			}
		}
	}

	/**
	 * addTravelDocToProfileTravelDocV2
	 * @param profileTravelDocV2
	 * @param travelDocResponse
	 */
	private void addTravelDocToProfileTravelDocV2(ProfileTravelDocV2 profileTravelDocV2,
			TravelDocumentResponse travelDocResponse) {
		profileTravelDocV2.setCountryOfIssue(travelDocResponse.getCountryOfIssue());
		profileTravelDocV2.setExpiryDate(travelDocResponse.getExpiryDate());
		profileTravelDocV2.setFamilyName(travelDocResponse.getFamilyName());
		profileTravelDocV2.setGivenName(travelDocResponse.getGivenName());
		profileTravelDocV2.setNationality(travelDocResponse.getNationality());
		profileTravelDocV2.setPreferredTravelDocumentIndicator(travelDocResponse.getPreferredTravelDocumentIndicator());
		profileTravelDocV2.setTravelDocumentNo(travelDocResponse.getTravelDocumentNo());
		profileTravelDocV2.setTravelDocumentType(travelDocResponse.getTravelDocumentType());
		profileTravelDocV2.setTrustedTravelDocumentIndicator(travelDocResponse.getTrustedTravelDocumentIndicator());
	}

	/**
	 * retrieve Member Profile Details V2
	 * @param memberId
	 * @return
	 */
	private TravelDocumentRetrievalResponse retrieveMemberProfileDetailsV2(String memberId) {
		
		TravelDocumentRetrievalRequest request = new TravelDocumentRetrievalRequest();
		request.setApplicationName(MMBUtil.getCurrentAppCode());
		request.setCorrelationId("");
		request.setMemberIdOrUsername(memberId);
		return retrieveProfileServiceCacheHelper.retrieveMemberProfileSummaryV2(request);
	}

	/**
	 * retrieve memeber profiles summary and cache Profile in cache helper
	 * @param memberId
	 * @return
	 */
	private Profile retrieveMemberProfileSummary(String memberId) {
		Profile response = null;
		// summary info
		ClsAPIRequest summaryInfoRequest = new ClsAPIRequest();
		summaryInfoRequest.setMemberIdOrUsername(memberId);
		response = retrieveProfileServiceCacheHelper.retrieveMemberProfileSummary(summaryInfoRequest);
		return response;
	}
	
	/**
	 * retrieve memeber profiles details and cache PreferenceRecord in cache helper
	 * @param memberId
	 * @return
	 */
	private PreferenceRecord retrieveMemberProfileDetails(String memberId) {

		PreferenceRecord preferenceRecord = null;
		ClsAPIRequest summaryInfoRequest = new ClsAPIRequest();
		UserInformation userInformation = new UserInformation();
		userInformation.setMemberId(memberId);
		// TODO other user information
		summaryInfoRequest.setIdNumber(memberId);
		summaryInfoRequest.setUserInformation(userInformation);
		preferenceRecord =  retrieveProfileServiceCacheHelper.retrieveMemberProfileDetails(summaryInfoRequest);
		return preferenceRecord;
	}
	/**
	 * convert to profile model
	 * @param preferenceRecord
	 * @return
	 */
	private ProfilePreference convertToProfilePreference(PreferenceRecord preferenceRecord){
		
		ProfilePreference profilePreference = new ProfilePreference();
		if(preferenceRecord !=null){
			profilePreference.setPersonalTravelDocuments(buildMemberTravelDocs(preferenceRecord.getTravelDocument()));
			profilePreference.setTravelCompanions(buildTravelCompanions(preferenceRecord.getOtherPreference()));
			profilePreference.setOriginalMemberId(getOriginalMemberId(preferenceRecord.getOtherPreference()));
		}
		return profilePreference;
		
	}
	
	/**
	 * Get the Original RU member Id
	 * @param otherPreferenceRecords
	 * @return
	 */
	private String getOriginalMemberId(List<OtherPreferenceRecord> otherPreferenceRecords) {
		if (CollectionUtils.isEmpty(otherPreferenceRecords)) {
			return null;
		}
		return otherPreferenceRecords
				.stream()
				.filter(ot -> MemberProfileConstants.PROFILE_TRANSIT_CATEGORY.equalsIgnoreCase(ot.getCategory())
						&& MemberProfileConstants.PROFILE_TRANSIT_CATEGORY_RU.equalsIgnoreCase(ot.getType()))
				.map(OtherPreferenceRecord::getAdditionalText)
				.findFirst()
				.orElse(null);

	}
	
	/**
	 * Build Travel Companions
	 * @param otherPreferenceRecords
	 * @return
	 */
	private List<TravelCompanion> buildTravelCompanions(List<OtherPreferenceRecord> otherPreferenceRecords) {

		if (CollectionUtils.isEmpty(otherPreferenceRecords)) {
			return Collections.emptyList();
		}
		List<TravelCompanion> travelCompanions = new ArrayList<>();
		//travel companion only have three 
		TravelCompanion travelCompanion1 = buildTravelCompanion(otherPreferenceRecords, MemberProfileConstants.TRAVEL_COMPANION_SEQUENCE_1);
		TravelCompanion travelCompanion2 = buildTravelCompanion(otherPreferenceRecords,	MemberProfileConstants.TRAVEL_COMPANION_SEQUENCE_2);
		TravelCompanion travelCompanion3 = buildTravelCompanion(otherPreferenceRecords,	MemberProfileConstants.TRAVEL_COMPANION_SEQUENCE_3);
		if (travelCompanion1 != null) {
			travelCompanions.add(travelCompanion1);
		}
		if (travelCompanion2 != null) {
			travelCompanions.add(travelCompanion2);
		}
		if (travelCompanion3 != null) {
			travelCompanions.add(travelCompanion3);
		}
		return travelCompanions;

	}
	
	/**
	 * build Travel Companion
	 * @param otherPreferenceRecords
	 * @param sequence
	 * @return
	 */
	private TravelCompanion buildTravelCompanion(List<OtherPreferenceRecord> otherPreferenceRecords, String sequence) {

		ProfileTravelDoc profileTravelDoc = null;
		TravelCompanion travelCompanion = null;
		
		for (OtherPreferenceRecord otherPreferenceRecord : otherPreferenceRecords) {
			String category = otherPreferenceRecord.getCategory();
			String additionalText = otherPreferenceRecord.getAdditionalText();
			String type = otherPreferenceRecord.getType();
			/**build from TRAVEL COMPANION PROFILE*/
			if ((MemberProfileConstants.TRAVEL_COMPANION_PROFILE + sequence).equals(category)) {
				if (MemberProfileConstants.FAMILY_NAME_TYPE.equals(type)) {
					profileTravelDoc = createIfNull(profileTravelDoc);
					profileTravelDoc.setFamilyName(additionalText);
				}else if (MemberProfileConstants.GIVEN_NAME_TYPE.equals(type)) {
					profileTravelDoc = createIfNull(profileTravelDoc);
					profileTravelDoc.setGivenName(additionalText);
				}else if(MemberProfileConstants.TRAVEL_COMPANION_TYPE_GENDER.equals(type)){
					travelCompanion = createIfNull(travelCompanion);
					travelCompanion.setGender(additionalText);
				}else if(MemberProfileConstants.TRAVEL_COMPANION_TYPE_DOB.equals(type) && StringUtils.isNotEmpty(additionalText)){
					travelCompanion = createIfNull(travelCompanion);
					travelCompanion.setDateOfBirth(DateUtil.convertDateFormat(additionalText,
							DateUtil.DATE_PATTERN_DD_MM_YYYY, DateUtil.DATE_PATTERN_YYYY_MM_DD));
				}else if(MemberProfileConstants.TRAVEL_COMPANION_TYPE_TITLE.equals(type)  ){
					travelCompanion = createIfNull(travelCompanion);
					travelCompanion.setTitle(additionalText);
				}
				/**build from TRAVEL COMP XXXX*/
			}else if((MemberProfileConstants.TRAVEL_COMP_COUNTRY + sequence).equals(category) && StringUtils.isNotEmpty(type)){
				profileTravelDoc = createIfNull(profileTravelDoc);
				profileTravelDoc.setIssueCountryIosThree(nationalityService.findThreeCountryCodeByTwoCountryCode(type));
				profileTravelDoc.setIssueCountry(type);
			}else if((MemberProfileConstants.TRAVEL_COMP_EXP_DATE + sequence).equals(category) && StringUtils.isNotEmpty(additionalText)){
				profileTravelDoc = createIfNull(profileTravelDoc);
				profileTravelDoc.setExpiryDate(DateUtil.convertDateFormat(additionalText,
						DateUtil.DATE_PATTERN_DD_MM_YYYY, DateUtil.DATE_PATTERN_YYYY_MM_DD));
			}else if((MemberProfileConstants.TRAVEL_COMP_NATION + sequence).equals(category) && StringUtils.isNotEmpty(type)){
				profileTravelDoc = createIfNull(profileTravelDoc);
				profileTravelDoc.setNationalityIosThree(nationalityService.findThreeCountryCodeByTwoCountryCode(type));
				profileTravelDoc.setNationality(type);
			}else if((MemberProfileConstants.TRAVEL_COMP_TYPE + sequence).equals(category)){
				profileTravelDoc = createIfNull(profileTravelDoc);
				profileTravelDoc.setType(type);
				profileTravelDoc.setDocumentNumber(additionalText);
			}
			//set travel to  travelCompanion 
			if (profileTravelDoc != null) {
				travelCompanion = createIfNull(travelCompanion);
				travelCompanion.setTravelDocument(profileTravelDoc);
			}

		}
		return travelCompanion;
	}
	
	
	/**
	 * convert member travel docs
	 * @param travelDocumentRecords
	 * @return
	 */
	private List<ProfileTravelDoc> buildMemberTravelDocs(List<TravelDocumentRecord> travelDocumentRecords) {
		List<ProfileTravelDoc> profileTravelDocs = new ArrayList<>();
		if (!CollectionUtils.isEmpty(travelDocumentRecords)) {
			for (TravelDocumentRecord travelDocumentRecord : travelDocumentRecords) {
				ProfileTravelDoc profileTravelDoc = new ProfileTravelDoc();
				profileTravelDocs.add(profileTravelDoc);
				profileTravelDoc.setDocumentNumber(travelDocumentRecord.getDocumentNumber());
				// convert data format
				if (StringUtils.isNotEmpty(travelDocumentRecord.getExpiryDate())) {
					profileTravelDoc.setExpiryDate(DateUtil.convertDateFormat(travelDocumentRecord.getExpiryDate(),
							DateUtil.DATE_PATTERN_DD_MM_YYYY, DateUtil.DATE_PATTERN_YYYY_MM_DD));
				}
				profileTravelDoc.setFamilyName(travelDocumentRecord.getFamilyName());
				profileTravelDoc.setGivenName(travelDocumentRecord.getGivenName());
				if(!StringUtils.isEmpty(travelDocumentRecord.getIssueCountry())){
					String issueCountryCode = nationalityService.findThreeCountryCodeByTwoCountryCode(travelDocumentRecord.getIssueCountry());
					profileTravelDoc.setIssueCountryIosThree(issueCountryCode);
					profileTravelDoc.setIssueCountry(travelDocumentRecord.getIssueCountry());
				}
				if(!StringUtils.isEmpty(travelDocumentRecord.getNationality())){
					String nationalityCountryCode = nationalityService.findThreeCountryCodeByTwoCountryCode(travelDocumentRecord.getNationality());
					profileTravelDoc.setNationalityIosThree(nationalityCountryCode);
					profileTravelDoc.setNationality(travelDocumentRecord.getNationality());
				}
				profileTravelDoc.setType(travelDocumentRecord.getType());
			}
		}
		return profileTravelDocs;
	}
	/**
	 * convert passenger info to ProfilePersonInfo model
	 * @param profile
	 * @return
	 */
	private ProfilePersonInfo convertToPersonInfo(Profile profile) {
		ProfilePersonInfo profilePersonInfo = new ProfilePersonInfo();
		//person info
		if (profile.getCustomerRecord() != null) {
			if (profile.getCustomerRecord().getMemberName() != null) {
				profilePersonInfo.setFamilyName(profile.getCustomerRecord().getMemberName().getFamilyName());
				profilePersonInfo.setGivenName(profile.getCustomerRecord().getMemberName().getGivenName());
				profilePersonInfo.setTitle(profile.getCustomerRecord().getMemberName().getTitle());
				if(profile.getCustomerRecord().getAccount() != null){
					profilePersonInfo.setAsiaMiles(Integer.parseInt(profile.getCustomerRecord().getAccount().getTotalMilesAvailable()));
					if(profile.getCustomerRecord().getAccount().getMpoClubPts()!=null && StringUtils.isNotEmpty(profile.getCustomerRecord().getAccount().getMpoClubPts().getMpoClubPtsBalance())){
						profilePersonInfo.setClubPoint(Integer.parseInt(profile.getCustomerRecord().getAccount().getMpoClubPts().getMpoClubPtsBalance()));
					}
				}
				if(profile.getCustomerRecord().getMembHoliday() != null){
					profilePersonInfo.setOnHoliday("Y".equalsIgnoreCase(profile.getCustomerRecord().getMembHoliday().getMembHolidayInd()) ? Boolean.TRUE : Boolean.FALSE);
					profilePersonInfo.setHolidayEndDate(profile.getCustomerRecord().getMembHoliday().getMembHolidayEndDt());
					profilePersonInfo.setHolidayStartDate(profile.getCustomerRecord().getMembHoliday().getMembHolidayStartDt());
				}
				if (profile.getCustomerRecord().getGender() != null) {
					profilePersonInfo.setGender(profile.getCustomerRecord().getGender());
				}
				if (StringUtils.isNotBlank(profile.getCustomerRecord().getBirthDate())) {
					try {
						profilePersonInfo.setDateOfBirth(DateUtil.convertDateFormat(
								profile.getCustomerRecord().getBirthDate(),
								DateUtil.DATE_PATTERN_DDMMYYYY, DateUtil.DATE_PATTERN_YYYY_MM_DD));
					} catch (Exception e) {
						logger.warn("Finde invalid BirthDate format", e);
					}
					
				}
			}
			profilePersonInfo.setTier(profile.getCustomerRecord().getTierCode());
			profilePersonInfo.setMemberId(profile.getCustomerRecord().getMemberNumber());
			profilePersonInfo.setCustUsername(profile.getCustomerRecord().getCustUsername());
			profilePersonInfo.setJoinDate(profile.getCustomerRecord().getJoinDate());
		}
		//contact info
		profilePersonInfo.setContact(buildContect(profile));
		return profilePersonInfo;
	}
	/**
	 * Build contact info 
	 * @param profile
	 * @return
	 */
	private Contact buildContect(Profile profile){
		Contact contact = new Contact();
 
		boolean isEmailFound = parseEmailRecord(profile.getCustomerEmailAddressInfo().getCustomerEmailAddressRecord(), contact);
		boolean isMobileFound = parseMobileRecord(profile.getCustomerMobilePhoneInfo().getCustomerMobilePhoneRecord(), contact);
		 
		if (isEmailFound || isMobileFound) {
			return contact;
		}else{
			return null;
		}
	} 
	
	private boolean parseMobileRecord(List<CustomerMobilePhoneRecord> mobileRecords, Contact contact) {
		if (CollectionUtils.isEmpty(mobileRecords)) {
			return false;
		}
		PhoneInfoDTO phoneInfo;
		String memberPhone = mobileRecords.stream()
				.filter(phoneRecord -> MMBBizruleConstants.PHONE_TYPE_MOBILE.equals(phoneRecord.getMobileType()))
				.map(CustomerMobilePhoneRecord::getMobilePhoneNumber).findFirst().orElse(null);
		if (!StringUtils.isEmpty(memberPhone)) {
			phoneInfo = BizRulesUtil.parserPhoneNumber(memberPhone);
			ProfilePhoneInfo profilePhoneInfo = convertToProfilePhone(phoneInfo);
			contact.setPhoneInfo(profilePhoneInfo);
			return true;
		} else {
			return false;
		}

	}
	
	/**
	 * convert to ProfilePhoneInfo
	 * @param phoneInfo
	 * @return ProfilePhoneInfo
	 */
	private ProfilePhoneInfo convertToProfilePhone(PhoneInfoDTO phoneInfo) {
		if(phoneInfo != null) {
			ProfilePhoneInfo profilePhoneInfo = new ProfilePhoneInfo();
			profilePhoneInfo.setCountryCode(phoneInfo.getCountryCode());
			profilePhoneInfo.setOlssContact(phoneInfo.isOlssContact());
			profilePhoneInfo.setPhoneCountryNumber(phoneInfo.getPhoneCountryNumber());
			profilePhoneInfo.setPhoneNo(phoneInfo.getPhoneNo());
			profilePhoneInfo.setType(phoneInfo.getType());
			return profilePhoneInfo;
		}
		return null;
	}



	/**
	 * find first email
	 * @param emailRecords
	 * @param contact
	 * @return
	 */
	private boolean parseEmailRecord(List<CustomerEmailAddressRecord> emailRecords, Contact contact) {
		if (CollectionUtils.isEmpty(emailRecords)) {
			return false;
		}
		ProfileEmail profileEmail=new ProfileEmail();
		for (CustomerEmailAddressRecord emailRecord : emailRecords) {
			String email = getTrimmedString(emailRecord.getEmailAddress());

			if (!StringUtils.isEmpty(email)) {
				profileEmail.setEmail(email);
				profileEmail.setType(ContactInfoTypeEnum.MEMBER_PROFILE_CONTACT_INFO.getType());
				contact.setEmail(profileEmail);
				return true;
			}
		}

		return false;
	}

	private TravelCompanion createIfNull(TravelCompanion travelCompanion){
		return travelCompanion == null ? new TravelCompanion():travelCompanion;
	}
	
	private ProfileTravelDoc createIfNull(ProfileTravelDoc profileTravelDoc){
		return profileTravelDoc == null ? new ProfileTravelDoc():profileTravelDoc;
	}
	
	/**
	 * Return trimmed result of string.
	 * 
	 * @param str
	 * @return
	 */
	private String getTrimmedString(String str) {
		if (str == null) {
			return null;
		} else {
			return str.trim();
		}
	}


	@Async
	@Override
	public Future<Map<String, ProfilePersonInfo>> asyncCheckMemberHoliday(Set<String> memberIds, String mmbToken) {
		Map<String, ProfilePersonInfo> map = new HashMap<>();
		for(String memberId: memberIds){
			ProfilePersonInfo retrievePersonInfo = this.retrievePersonInfo(memberId, mmbToken);
			if (retrievePersonInfo != null){
				map.put(memberId, retrievePersonInfo);
			}
		}
		return new AsyncResult<>(map);
	}

}
