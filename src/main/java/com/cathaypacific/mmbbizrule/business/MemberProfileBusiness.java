package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.response.memberprofile.ProfileTravelDocResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.memberprofile.SocialAccountResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.memberprofile.v2.ProfileTravelDocDTO;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;

public interface MemberProfileBusiness {
	/**
	 * Retrieve the member summary info by memberid
	 * @param memberId
	 * @param loginInfo
	 * @return
	 * @throws BusinessBaseException
	 */
	public ProfilePersonInfo retrieveMemberProfileSummary(String memberId,LoginInfo loginInfo) throws BusinessBaseException;
	

	/**
	 * Retrieve member profile travel doc, especially for travel documents including companion 
	 * @param mmbMemberId
	 * @param orgin
	 * @param destination
	 * @param requirePriDocs
	 * @param requireSecDocs
	 * @return
	 * @throws BusinessBaseException
	 */
	public ProfileTravelDocResponseDTO retrieveMemberProfileTravelDoc(String mmbMemberId, String orgin, String destination, boolean requirePriDocs, boolean requireSecDocs,LoginInfo loginInfo ) throws BusinessBaseException;

	/**
	 * Retrieve social account by memberId
	 * @param memberId
	 * @return SocialAccountResponseDTO
	 */
	public SocialAccountResponseDTO retrieveSocialAccount(String memberId);

	/**
	 * retrieve MemberProfile TravelDocV2
	 * @param memberId
	 * @param origin
	 * @param destination
	 * @param requirePriDocs
	 * @param requireSecDocs
	 * @param loginInfo
	 * @return
	 */
	public ProfileTravelDocDTO retrieveMemberProfileTravelDocV2(String memberId, String origin, String destination,
			boolean requirePriDocs, boolean requireSecDocs, LoginInfo loginInfo);
}
