package com.cathaypacific.mmbbizrule.cxservice.memberprofile.service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import com.cathaypacific.mmbbizrule.cxservice.memberprofile.model.response.socialaccount.ClsSocialAccountResponse;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePreference;
import com.cathaypacific.mmbbizrule.model.profile.v2.ProfilePreferenceV2;

public interface RetrieveProfileService {
	
	/**
	 * Get person info by member id
	 * @param memberId
	 * @param mbToken used by cache
	 * @return
	 */
	public ProfilePersonInfo retrievePersonInfo(String memberId, String mbToken);
	
	/**
	 * Get preference info by member id
	 * @param memberId
	 * @param mbToken used by cache
	 * @return
	 */
	public ProfilePreference retrievePreference(String memberId, String mbToken);

	/**
	 * Get Member Holiday check by memberIds
	 * @param memberIds
	 * @param mmbToken
	 * @return
	 */
	public Future<Map<String, ProfilePersonInfo>> asyncCheckMemberHoliday(Set<String> memberIds, String mmbToken);
	
	/**
	 * Get social account details by member id
	 * @param memberId
	 * @return SocialAccountResponse
	 */
	public ClsSocialAccountResponse retrieveSocialAccount(String memberId);

	/**
	 * retrieve PreferenceV2
	 * @param memberId
	 * @param mmbToken
	 * @return
	 */
	public ProfilePreferenceV2 retrievePreferenceV2(String memberId, String mmbToken);
}
