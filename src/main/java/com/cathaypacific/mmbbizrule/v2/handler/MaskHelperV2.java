package com.cathaypacific.mmbbizrule.v2.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.enums.mask.MaskFieldName;
import com.cathaypacific.mbcommon.model.common.MaskInfo;
import com.cathaypacific.mbcommon.model.common.TokenMaskInfo;
import com.cathaypacific.mbcommon.token.MbTokenCacheRepository;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.ContactInfoDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.DobDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.EmrContactInfoDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.FlightBookingDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.KtnRedressDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.PassengerDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.PassengerSegmentDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.TravelDocDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateAdultSegmentInfoDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateBasicSegmentInfoDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateEmailDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateEmergencyContactDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateInfantDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdatePassengerDetailsRequestDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdatePhoneInfoDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateTravelDocDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails.UpdateTsDTOV2;
import com.google.common.base.Objects;

@Component
public class MaskHelperV2 {

	public static final String PHONE_MASK_SIGN = "●●●●";
	public static final String TRAVEL_DOC_NUMBER = "●●●●";
	public static final String EMAIL_MASK_SIGN = "●●●●●●●●●●";
	public static final String KTN_REDRESS = "●●●●";
	public static final String DATEOFYEAR_SIGN = "●●●●";
	public static final String DATEOFDAY_SIGN = "●●";
	public static final String EMAIL_SIGN = "@";
	
	@Autowired
	private MbTokenCacheRepository mbTokenCacheRepository;

	/**
	 * mask User Info
	 * 
	 * @param flightBookingDTO
	 * @param unlockedPassengerList
	 */
	public void mask(FlightBookingDTOV2 flightBookingDTO) {
		TokenMaskInfo tokenMaskInfo = new TokenMaskInfo();
		
		//set the update flag as false
		tokenMaskInfo.setMaskInfoUpdated(false);
		
		@SuppressWarnings("unchecked")
		List<MaskInfo> maskInfos = mbTokenCacheRepository.get(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.MASK_INFO, flightBookingDTO.getOneARloc(), ArrayList.class);
		
		//create new if null
		tokenMaskInfo.setMaskInfos(maskInfos == null ? new ArrayList<>() : maskInfos);
		
		maskAllUserInfo(flightBookingDTO, tokenMaskInfo);
		
		//store the maskInfo to redis if any change of the mask info list 
		if(tokenMaskInfo.isMaskInfoUpdated()){
			mbTokenCacheRepository.add(MMBUtil.getCurrentMMBToken(),TokenCacheKeyEnum.MASK_INFO, flightBookingDTO.getOneARloc(), tokenMaskInfo.getMaskInfos());
		}

	}
	
	/**
	 * mask All User Info
	 * 
	 * @param flightBookingDTO
	 */
	private void maskAllUserInfo(FlightBookingDTOV2 flightBookingDTO, TokenMaskInfo tokenMaskInfo) {
		List<PassengerDTOV2> passengers = flightBookingDTO.getPassengers();
		List<PassengerSegmentDTOV2> passengerSegments = flightBookingDTO.getPassengerSegments();

		String loginMemberPaxId =
				passengers.stream().filter(pax -> BooleanUtils.isTrue(pax.getLoginMember())).map(PassengerDTOV2::getPassengerId).findFirst().orElse(StringUtils.EMPTY);

		maskCustomerLevel(passengers, tokenMaskInfo, loginMemberPaxId);
		maskProductLevel(passengerSegments, tokenMaskInfo, loginMemberPaxId);

	}

	/**
	 * 
	 * mask All User Info By Product Level
	 * 
	 * @param passengerSegments
	 */

	private void maskProductLevel(List<PassengerSegmentDTOV2> passengerSegments, TokenMaskInfo tokenMaskInfo, String loginMemberPaxId) {

		for (PassengerSegmentDTOV2 passengerSegmentDTO : passengerSegments) {
			if (passengerSegmentDTO.getTravelDoc() != null) {

				if (StringUtils.equals(loginMemberPaxId, passengerSegmentDTO.getPassengerId())) {
					// no need mask for login member
					continue;
				}
				maskPriTravelDocDTO(passengerSegmentDTO.getTravelDoc().getPriTravelDoc(), passengerSegmentDTO.getPassengerId(),	passengerSegmentDTO.getSegmentId(), tokenMaskInfo);
				maskSecTravelDocDTO(passengerSegmentDTO.getTravelDoc().getSecTravelDoc(), passengerSegmentDTO.getPassengerId(),	passengerSegmentDTO.getSegmentId(), tokenMaskInfo);
			}
		}
	}
	
	
	/**
	 * mask Qualified User Info
	 * 
	 * @param passengers
	 */
	private void maskCustomerLevel(List<PassengerDTOV2> passengers, TokenMaskInfo tokenMaskInfo,String loginMemberPaxId ) {

		for (PassengerDTOV2 passenger : passengers) {
			if(StringUtils.equals(loginMemberPaxId, passenger.getPassengerId())){
				//no need mask for login member
				continue;
			}
			// contact info
			ContactInfoDTOV2 contactInfo = passenger.getContactInfo();
			maskContactInfoDTO(contactInfo,passenger.getPassengerId(), tokenMaskInfo);
			// emergence contact info
			EmrContactInfoDTOV2 emrContactInfo = passenger.getEmrContactInfo();
			maskEmgerContactInfo(emrContactInfo,passenger.getPassengerId(),tokenMaskInfo);
			// ktn & redress
			maskKtnDTO(passenger.getKtn(), passenger.getPassengerId(),tokenMaskInfo);
			maskRedressDTO(passenger.getRedress(), passenger.getPassengerId(),tokenMaskInfo);
			//Dob
			maskDobDTO(passenger.getDob(), passenger.getPassengerId(), tokenMaskInfo);

		}

	}

	/**
	 * mask travel doc dto
	 * @param travelDocDTO
	 * @param passengerId
	 * @param segmentId
	 * @param tokenMaskInfo
	 */
	private void maskPriTravelDocDTO(TravelDocDTOV2 travelDocDTO,String passengerId, String segmentId, TokenMaskInfo tokenMaskInfo) {

		if (travelDocDTO != null) {
			travelDocDTO.setTravelDocumentNumber(getAccessableValue(MaskFieldName.PRI_TRAVELDOC_NO, travelDocDTO.getTravelDocumentNumber(), passengerId, segmentId, tokenMaskInfo, travelDocDTO::setDocNumberMasked));
			travelDocDTO.setBirthDateYear(getAccessableValue(MaskFieldName.PRI_TRAVELDOC_DOB_YEAR, travelDocDTO.getBirthDateYear(), passengerId, segmentId, tokenMaskInfo, travelDocDTO::setBirthDateYearMasked));
			travelDocDTO.setBirthDateDay(getAccessableValue(MaskFieldName.PRI_TRAVELDOC_DOB_DAY, travelDocDTO.getBirthDateDay(), passengerId, segmentId, tokenMaskInfo, travelDocDTO::setBirthDateDayMasked));
		}

	}
	
	/**
	 * mask travel doc dto
	 * @param travelDocDTO
	 * @param passengerId
	 * @param segmentId
	 * @param tokenMaskInfo
	 */
	private void maskSecTravelDocDTO(TravelDocDTOV2 travelDocDTO,String passengerId, String segmentId, TokenMaskInfo tokenMaskInfo) {

		if (travelDocDTO != null) {
			travelDocDTO.setTravelDocumentNumber(getAccessableValue(MaskFieldName.SEC_TRAVELDOC_NO, travelDocDTO.getTravelDocumentNumber(), passengerId, segmentId, tokenMaskInfo, travelDocDTO::setDocNumberMasked));
			travelDocDTO.setBirthDateYear(getAccessableValue(MaskFieldName.SEC_TRAVELDOC_DOB_YEAR, travelDocDTO.getBirthDateYear(), passengerId, segmentId, tokenMaskInfo, travelDocDTO::setBirthDateYearMasked));
			travelDocDTO.setBirthDateDay(getAccessableValue(MaskFieldName.SEC_TRAVELDOC_DOB_DAY, travelDocDTO.getBirthDateDay(), passengerId, segmentId, tokenMaskInfo, travelDocDTO::setBirthDateDayMasked));
		}

	}
	/**
	 * Mask redress DTO
	 * @param redress
	 * @param passengerId
	 * @param tokenMaskInfo
	 */
	private void maskRedressDTO(KtnRedressDTOV2 redress, String passengerId, TokenMaskInfo tokenMaskInfo) {

		if (redress != null) {
			redress.setNumber(getAccessableValue(MaskFieldName.REDRESS_NO, redress.getNumber(), passengerId, null, tokenMaskInfo, redress::setNumberMasked));
		}

	}

	/**
	 * mask KTN dto
	 * @param ktn
	 * @param passengerId
	 * @param tokenMaskInfo
	 */
	private void maskKtnDTO(KtnRedressDTOV2 ktn,String passengerId,TokenMaskInfo tokenMaskInfo) {

		if (ktn != null) {
			ktn.setNumber(getAccessableValue(MaskFieldName.KTN, ktn.getNumber(), passengerId, null, tokenMaskInfo, ktn::setNumberMasked));
		}
		 
	}

	/**
	 * mask KTN dto
	 * @param ktn
	 * @param passengerId
	 * @param tokenMaskInfo
	 */
	private void maskDobDTO(DobDTOV2 dob,String passengerId,TokenMaskInfo tokenMaskInfo) {

		if (dob != null) {
			dob.setBirthDateDay(getAccessableValue(MaskFieldName.DOB_DAY, dob.getBirthDateDay(), passengerId, null, tokenMaskInfo, dob::setDayMasked));
			dob.setBirthDateYear(getAccessableValue(MaskFieldName.DOB_YEAR, dob.getBirthDateYear(), passengerId, null, tokenMaskInfo, dob::setYearMasked));
		}
		 
	}
	
	/**
	 * mask Contact Info
	 * 
	 * @param contactInfoDto
	 * @param unlocked
	 */

	private void maskContactInfoDTO(ContactInfoDTOV2 contactInfoDto, String passengerId, TokenMaskInfo tokenMaskInfo) {

		if (contactInfoDto != null) {

			if (contactInfoDto.getPhoneInfo() != null) {
				contactInfoDto.getPhoneInfo().setPhoneNo(
						getAccessableValue(MaskFieldName.PHONE_NO, contactInfoDto.getPhoneInfo().getPhoneNo(), passengerId, null, tokenMaskInfo, contactInfoDto.getPhoneInfo()::setPhoneNumberMasked));
			}

			if (contactInfoDto.getEmail() != null) {
				contactInfoDto.getEmail().setEmail(getAccessableValue(MaskFieldName.EMAIL, contactInfoDto.getEmail().getEmail(), passengerId, null, tokenMaskInfo, contactInfoDto.getEmail()::setEmailMasked));
			}

		}

	}
	
	/**
	 * mask emergency Contact Info
	 * 
	 * @param emrContactInfo
	 * @param unlocked
	 */

	private void maskEmgerContactInfo(EmrContactInfoDTOV2 emrContactInfo,String passengerId, TokenMaskInfo tokenMaskInfo) {

		if (emrContactInfo != null ) {
			emrContactInfo.setPhoneNumber(getAccessableValue(MaskFieldName.EMER_PHONE_NO, emrContactInfo.getPhoneNumber(), passengerId, null, tokenMaskInfo, emrContactInfo::setPhoneNumberMasked));
		}  

	}

	/**
	 * Mask the value if the field need mask, and update the new value to mask info.
	 * only return the masked value if the field need mask, otherwise return request value
	 * @param fieldName
	 * @param value
	 * @param paxId
	 * @param segId
	 * @param tokenMaskInfo
	 * @return
	 */
	private String getAccessableValue(MaskFieldName fieldName, String value,String paxId, String segId, TokenMaskInfo tokenMaskInfo, Consumer<Boolean> maskedSetter){
		if(StringUtils.isEmpty(value) || tokenMaskInfo == null || tokenMaskInfo.getMaskInfos() == null){
			return value;
		}
		MaskInfo maskInfo = getOrCreateMaskInfo(fieldName, paxId, segId, tokenMaskInfo);
		
		//sync 1A pnr value to maskInfos, if pnr update in other client, the value should mask in mmb even the value edited in MMB 
		if (!value.equalsIgnoreCase(maskInfo.getOriginalValue())) {
			maskInfo.setOriginalValue(value);
			maskInfo.setEditedInCurrentSession(false);
			tokenMaskInfo.setMaskInfoUpdated(true);
		}
		
		String result = null;
		//return pnr value if no need mask
		if (maskInfo.isEditedInCurrentSession()) {
			result = value;
		} else {
			result = maskFieldValue(fieldName, value);
			maskedSetter.accept(true);
			//set the mask value to mask info if the new masked value not same as before, this case in first time do mask or pnr updated in other client
			if (!Objects.equal(result, maskInfo.getMaskedValue())) {
				maskInfo.setMaskedValue(result);
				tokenMaskInfo.setMaskInfoUpdated(true);
			}
		}
		return result;
	}
	
	/**
	 * get or create mask info 
	 * @param fieldName
	 * @param paxId
	 * @param segId
	 * @param tokenMaskInfo
	 * @return
	 */
	private MaskInfo getOrCreateMaskInfo(MaskFieldName fieldName, String paxId, String segId, TokenMaskInfo tokenMaskInfo) {

		MaskInfo matchedMaskInfo = getMaskInfo(fieldName, paxId, segId, tokenMaskInfo.getMaskInfos());
		if(matchedMaskInfo == null){
			matchedMaskInfo = createMaskInfo(fieldName, paxId, segId, tokenMaskInfo);
		}
		
		return matchedMaskInfo;
	}
	
	/**
	 * Get <code>MaskInfo</code>.
	 * 
	 * @param fieldName
	 * @param paxId
	 * @param segId
	 * @param maskInfos
	 * @return matched mask info, or null if it is not found.
	 */
	private MaskInfo getMaskInfo(MaskFieldName fieldName, String paxId, String segId, List<MaskInfo> maskInfos) {
		
		return maskInfos
				.stream()
				.filter(maskInfo -> Objects.equal(maskInfo.getPassengerId(), paxId)
						&& Objects.equal(maskInfo.getSegmentId(), segId)
						&& Objects.equal(maskInfo.getFieldName(), fieldName))
				.findFirst()
				.orElse(null);
	}
	
	/**
	 * Create <code>MaskInfo</code>.
	 * 
	 * @param fieldName
	 * @param paxId
	 * @param segId
	 * @param tokenMaskInfo
	 * @return
	 */
	private MaskInfo createMaskInfo(MaskFieldName fieldName, String paxId, String segId, TokenMaskInfo tokenMaskInfo) {
		MaskInfo maskInfo = new MaskInfo();
		maskInfo.setFieldName(fieldName);
		maskInfo.setPassengerId(paxId);
		maskInfo.setSegmentId(segId);
		tokenMaskInfo.getMaskInfos().add(maskInfo);
		tokenMaskInfo.setMaskInfoUpdated(true);
		
		return maskInfo;
	}
	
	
	/**-----------------------------------------------------------Mask Str -----------------------------------------*/
	
	/**
	 * Mask the value by filed name
	 * @param filedName
	 * @param value
	 * @return
	 */
	private String maskFieldValue(MaskFieldName filedName, String value) {
		
		String maskedValue = "";

		switch (filedName) {

			case PHONE_NO:
				// same as emer phone no
			case EMER_PHONE_NO:
				maskedValue = maskPhoneNumber(value);
				break;
			case EMAIL:
				maskedValue = maskEmail(value);
				break;
			case PRI_TRAVELDOC_DOB_YEAR:
				// same as DOB year
			case SEC_TRAVELDOC_DOB_YEAR:
				// same as DOB year
			case DOB_YEAR:
				maskedValue = maskYear(value);
				break;
			case PRI_TRAVELDOC_DOB_DAY:
				// same as DOB day
			case SEC_TRAVELDOC_DOB_DAY:
				// same as DOB day
			case DOB_DAY:
				maskedValue = maskDay(value);
				break;
			case PRI_TRAVELDOC_NO:
				// same as secondary travel doc mask
			case SEC_TRAVELDOC_NO:
				maskedValue = maskTravelDocNumber(value);
				break;
			case KTN:
				// same as redress number
			case REDRESS_NO:
				maskedValue = maskKtnRedress(value);
				break;
			default:
				break;
		}
		return maskedValue;

	}
	
	/**
	 * mask phoneNumber
	 * 
	 * @param phoneNumber
	 * @return
	 */
	private String maskPhoneNumber(String phoneNumber) {

		String maskedMobileNUmber = null;
		if (StringUtils.isNotEmpty(phoneNumber)) {
			int length = phoneNumber.length();
			if (length > 4) {
				maskedMobileNUmber = phoneNumber.substring(0, length - 4) + PHONE_MASK_SIGN;
			} else {
				maskedMobileNUmber = PHONE_MASK_SIGN;
			}
		}
		return maskedMobileNUmber;
	}


	/**
	 * mask Email
	 * 
	 * @param email
	 * @return
	 */
	private String maskEmail(String email) {
		String maskedEmail = null;
		if (StringUtils.isNotEmpty(email)) {
			maskedEmail = email.substring(0, email.indexOf(EMAIL_SIGN) + 1) + EMAIL_MASK_SIGN;
		}
		return maskedEmail;

	}


	/**
	 * mask travel doc number
	 * 
	 * @param travelDocNumber
	 * @return
	 */
	private String maskTravelDocNumber(String travelDocNumber) {


		String maskedTravelDocNumber = null;

		if (StringUtils.isNotEmpty(travelDocNumber)) {

			if (travelDocNumber.length() > 4) {
				maskedTravelDocNumber = TRAVEL_DOC_NUMBER + travelDocNumber.substring(4, travelDocNumber.length());
			} else {
				maskedTravelDocNumber = TRAVEL_DOC_NUMBER;
			}

		}
		return maskedTravelDocNumber;
	}

	/**
	 * mask year
	 * 
	 * @param year
	 * @return
	 */
	private String maskYear(String year) {
		return StringUtils.isNotEmpty(year) ? DATEOFYEAR_SIGN : null;

	}


	/**
	 * mask day
	 * 
	 * @param day
	 * @return
	 */
	private String maskDay(String day) {
		return StringUtils.isNotEmpty(day) ? DATEOFDAY_SIGN : null;

	}
	
	
	/**
	 * mask ktn redress number
	 * @param ktnRedress
	 * @return
	 */
	private String maskKtnRedress(String ktnRedress) {

		String maskedKtnRedress = null;
		if (StringUtils.isNotEmpty(ktnRedress)) {

			if (ktnRedress.length() > 4) {
				maskedKtnRedress = KTN_REDRESS + ktnRedress.substring(4, ktnRedress.length());
			} else {
				maskedKtnRedress = KTN_REDRESS;
			}
		}
		
		return maskedKtnRedress;
	}
	
	/*-----------------------------------------------------------Unmask-----------------------------------------*/	
	/**
	 * Unmask sensitive fields of <code>UpdatePassengerDetailsRequestDTO</code>.
	 * 
	 * @param updatePassengerDetailsRequestDTO
	 */
	public TokenMaskInfo unmask(UpdatePassengerDetailsRequestDTOV2 updatePassengerDetailsRequestDTO) {

		TokenMaskInfo tokenMaskInfo = new TokenMaskInfo();
		@SuppressWarnings("unchecked")
		List<MaskInfo> maskInfos = mbTokenCacheRepository.get(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.MASK_INFO,
				updatePassengerDetailsRequestDTO.getRloc(), ArrayList.class);
		
		// Set the update flag false by default.
		tokenMaskInfo.setMaskInfoUpdated(false);
		tokenMaskInfo.setMaskInfos(maskInfos == null ? new ArrayList<>() : maskInfos);

		unmaskPassengerInfo(updatePassengerDetailsRequestDTO, tokenMaskInfo);
		
		return tokenMaskInfo;

	}
	
	/**
	 * save maskInfos
	 * @param tokenMaskInfo
	 */
	public void updateTokenMaskInfo(List<MaskInfo> maskInfos, String rloc, String token) {
		// Save the mask info list to redis .
		if (!CollectionUtils.isEmpty(maskInfos)) {
			mbTokenCacheRepository.add(token, TokenCacheKeyEnum.MASK_INFO,
					rloc, maskInfos);
		}
	}

	/**
	 * Unmask sensitive fields of <code>UpdatePassengerDetailsRequestDTO</code>.
	 * 
	 * @param updatePassengerDetailsRequestDTO
	 * @param tokenMaskInfo
	 */
	private void unmaskPassengerInfo(UpdatePassengerDetailsRequestDTOV2 updatePassengerDetailsRequestDTO,
			@NotNull TokenMaskInfo tokenMaskInfo) {

		if (updatePassengerDetailsRequestDTO != null) {
			unmaskCustomerLevel(updatePassengerDetailsRequestDTO, tokenMaskInfo);

			if (!CollectionUtils.isEmpty(updatePassengerDetailsRequestDTO.getSegments())) {
				unmaskProductLevel(updatePassengerDetailsRequestDTO.getPassengerId(),
						updatePassengerDetailsRequestDTO.getSegments(), tokenMaskInfo);
			}

			UpdateInfantDTOV2 infantDTO = updatePassengerDetailsRequestDTO.getInfant();
			if (infantDTO != null) {
				unmaskInfantCustomerLevel(infantDTO, tokenMaskInfo);

				if (!CollectionUtils.isEmpty(infantDTO.getSegments())) {
					unmaskInfantProductLevel(infantDTO.getPassengerId(), infantDTO.getSegments(), tokenMaskInfo);
				}
			}
		}

	}

	/**
	 * Unmask sensitive fields of customer level data.
	 * 
	 * @param passengerDTO
	 * @param tokenMaskInfo
	 */
	private void unmaskCustomerLevel(@NotNull UpdatePassengerDetailsRequestDTOV2 passengerDTO,
			@NotNull TokenMaskInfo tokenMaskInfo) {

		unmaskRedressDTO(passengerDTO.getRedress(), passengerDTO.getPassengerId(), tokenMaskInfo);
		unmaskKtnDTO(passengerDTO.getKtn(), passengerDTO.getPassengerId(), tokenMaskInfo);
		unmaskPhoneInfoDTO(passengerDTO.getPhoneInfo(), passengerDTO.getPassengerId(), tokenMaskInfo);
		unmaskEmailDTO(passengerDTO.getEmail(), passengerDTO.getPassengerId(), tokenMaskInfo);
		unmaskEmgerContactInfoDTO(passengerDTO.getEmergencyContact(), passengerDTO.getPassengerId(), tokenMaskInfo);

	}

	/**
	 * Unmask sensitive fields of product level data.
	 * 
	 * @param passengerId
	 * @param segments
	 * @param tokenMaskInfo
	 */
	private void unmaskProductLevel(@NotNull String passengerId, @NotNull List<UpdateAdultSegmentInfoDTOV2> segments,
			@NotNull TokenMaskInfo tokenMaskInfo) {

		for (UpdateAdultSegmentInfoDTOV2 segment : segments) {
			unmaskPriTravelDocDTO(segment.getPrimaryTravelDoc(), passengerId, segment.getSegmentId(), tokenMaskInfo);
			unmaskSecTravelDocDTO(segment.getSecondaryTravelDoc(), passengerId, segment.getSegmentId(), tokenMaskInfo);
		}

	}

	/**
	 * Unmask sensitive fields of customer level data of infant.
	 * 
	 * @param passengerDTO
	 * @param tokenMaskInfo
	 */
	private void unmaskInfantCustomerLevel(@NotNull UpdateInfantDTOV2 infantDTO, @NotNull TokenMaskInfo tokenMaskInfo) {

		unmaskRedressDTO(infantDTO.getRedress(), infantDTO.getPassengerId(), tokenMaskInfo);
		unmaskKtnDTO(infantDTO.getKtn(), infantDTO.getPassengerId(), tokenMaskInfo);

	}

	/**
	 * Unmask sensitive fields of product level data of infant.
	 * 
	 * @param passengerId
	 * @param segments
	 * @param tokenMaskInfo
	 */
	private void unmaskInfantProductLevel(@NotNull String passengerId,
			@NotNull List<UpdateBasicSegmentInfoDTOV2> segments, @NotNull TokenMaskInfo tokenMaskInfo) {

		for (UpdateBasicSegmentInfoDTOV2 segment : segments) {
			unmaskPriTravelDocDTO(segment.getPrimaryTravelDoc(), passengerId, segment.getSegmentId(), tokenMaskInfo);
			unmaskSecTravelDocDTO(segment.getSecondaryTravelDoc(), passengerId, segment.getSegmentId(), tokenMaskInfo);
		}

	}

	/**
	 * Unmask travel document number of primary <code>UpdateTravelDocDTO</code>.
	 * 
	 * @param travelDocDTO
	 * @param passengerId
	 * @param segmentId
	 * @param tokenMaskInfo
	 */
	private void unmaskPriTravelDocDTO(UpdateTravelDocDTOV2 travelDocDTO, String passengerId, String segmentId,
			TokenMaskInfo tokenMaskInfo) {

		if (travelDocDTO != null) {
			// the segment that the request value is from (since front-end has toggle on function for primary travel doc, the value of this travel doc may comes from another segment)
			String originSegmentId = StringUtils.isEmpty(travelDocDTO.getCopyFrom()) ? segmentId : travelDocDTO.getCopyFrom();
			String travelDocNumber = travelDocDTO.getTravelDocumentNumber();
			travelDocDTO.setTravelDocumentNumber(travelDocNumber == null ? travelDocNumber
					: getSubmissionValue(MaskFieldName.PRI_TRAVELDOC_NO, travelDocNumber.toUpperCase(), passengerId, segmentId,
							originSegmentId, tokenMaskInfo));
	
			travelDocDTO.setDateOfBirth(getPriDobSubmissionValue(travelDocDTO.getDateOfBirth(), passengerId, segmentId, originSegmentId, tokenMaskInfo));
		}
	}

	/**
	 * Unmask primary travel doc DOB
	 * @param dateOfBirth
	 * @param passengerId
	 * @param segmentId
	 * @param tokenMaskInfo
	 * @return String
	 */
	private String getPriDobSubmissionValue(String dateOfBirth, String passengerId, String segmentId, String originSegmentId,
			TokenMaskInfo tokenMaskInfo) {
		if (StringUtils.isEmpty(dateOfBirth) || tokenMaskInfo == null || tokenMaskInfo.getMaskInfos() == null) {
			return dateOfBirth;
		}
		
		String dateOfBirthDay = BizRulesUtil.getDayFromDateOfBirth(dateOfBirth);
		String dateOfBirthMonth = BizRulesUtil.getMonthFromDateOfBirth(dateOfBirth);
		String dateOfBirthYear = BizRulesUtil.getYearFromDateOfBirth(dateOfBirth);

		if (StringUtils.isEmpty(dateOfBirthDay) && StringUtils.isEmpty(dateOfBirthMonth)
				&& StringUtils.isEmpty(dateOfBirthYear)) {
			return dateOfBirth;
		} else {
			return getPriDobYearSubmissionValue(passengerId, segmentId, originSegmentId, tokenMaskInfo, dateOfBirthYear) + "-"
					+ dateOfBirthMonth + "-"
					+ getPriDobDaySubmissionValue(passengerId, segmentId, originSegmentId, tokenMaskInfo, dateOfBirthDay);
			
		}
	}

	/**
	 * Unmask day value in primary travel doc DOB
	 * @param passengerId
	 * @param segmentId
	 * @param originSegmentId 
	 * @param tokenMaskInfo
	 * @param dateOfBirthDay
	 * @return
	 */
	private String getPriDobDaySubmissionValue(String passengerId, String segmentId, String originSegmentId, TokenMaskInfo tokenMaskInfo,
			String dateOfBirthDay) {
		/**
		 * Since front-end has toggle on logic for travel doc, which means the request value of current segment may come from another segment, 
		 * so we may use MaskInfo of original segment to unmask the value, but the new value should always be updated to current segment
		 */
		
		// the MaskInfo the request value should compare to, if originSegmentId is not empty, should come from the origin segment, otherwise should come from current segment
		MaskInfo originPriDobDayMaskInfo = null;
		if (!StringUtils.isEmpty(originSegmentId)) {
			originPriDobDayMaskInfo = getMaskInfo(MaskFieldName.PRI_TRAVELDOC_DOB_DAY, passengerId, originSegmentId, tokenMaskInfo.getMaskInfos());		
		} else {
			originPriDobDayMaskInfo = getMaskInfo(MaskFieldName.PRI_TRAVELDOC_DOB_DAY, passengerId, segmentId, tokenMaskInfo.getMaskInfos());		
		}
		
		// the MaskInfo of current segment
		MaskInfo currentPriDobDayMaskInfo = getMaskInfo(MaskFieldName.PRI_TRAVELDOC_DOB_DAY, passengerId, segmentId, tokenMaskInfo.getMaskInfos());
		if (currentPriDobDayMaskInfo == null) {
			currentPriDobDayMaskInfo = createMaskInfo(MaskFieldName.PRI_TRAVELDOC_DOB_DAY, passengerId, segmentId, tokenMaskInfo);
		}
		
		if (originPriDobDayMaskInfo == null) {
			MaskInfo dobDayMaskInfo = getMaskInfo(MaskFieldName.DOB_DAY, passengerId, null, tokenMaskInfo.getMaskInfos());	
			if (dobDayMaskInfo != null && dateOfBirthDay.equals(dobDayMaskInfo.getMaskedValue())) {
				/**
				 * if PRI_TRAVELDOC_DOB_DAY mask info is not found but DOB_DAY maskInfo is found
				 * and the request value is same with the masked value in it, populate the
				 * maskInfo to PRI_TRAVELDOC_DOB_DAY
				 */
				currentPriDobDayMaskInfo.setOriginalValue(dobDayMaskInfo.getOriginalValue());
				currentPriDobDayMaskInfo.setMaskedValue(dobDayMaskInfo.getMaskedValue());
				currentPriDobDayMaskInfo.setEditedInCurrentSession(false);
				tokenMaskInfo.setMaskInfoUpdated(true);
				return dobDayMaskInfo.getOriginalValue();
			} else {
				/**
				 * if PRI_TRAVELDOC_DOB_DAY maskInfo is not found and no masked info matched in
				 * DOB_DAY maskInfo, create a new PRI_TRAVELDOC_DOB_DAY maskInfo accordingly
				 */
				currentPriDobDayMaskInfo.setOriginalValue(dateOfBirthDay);
				currentPriDobDayMaskInfo.setEditedInCurrentSession(true);
				tokenMaskInfo.setMaskInfoUpdated(true);
				return dateOfBirthDay;
			}
		} else if (Objects.equal(dateOfBirthDay, originPriDobDayMaskInfo.getMaskedValue())) {
			/**
			 * When requested value is same as stored masked value, unmask to original value.
			 */
			currentPriDobDayMaskInfo.setOriginalValue(originPriDobDayMaskInfo.getOriginalValue());
			currentPriDobDayMaskInfo.setMaskedValue(originPriDobDayMaskInfo.getMaskedValue());
			currentPriDobDayMaskInfo.setEditedInCurrentSession(false);
			return originPriDobDayMaskInfo.getOriginalValue();
			
		} else {
			/**
			 * When requested value is different with stored masked value, update new value to current session.
			 */
			currentPriDobDayMaskInfo.setOriginalValue(dateOfBirthDay);
			currentPriDobDayMaskInfo.setEditedInCurrentSession(true);
			tokenMaskInfo.setMaskInfoUpdated(true);
			return dateOfBirthDay;
		}
	}
	
	/**
	 * Unmask year value in primary travel doc DOB
	 * @param passengerId
	 * @param segmentId
	 * @param originSegmentId 
	 * @param tokenMaskInfo
	 * @param dateOfBirthYear
	 * @return String
	 */
	private String getPriDobYearSubmissionValue(String passengerId, String segmentId, String originSegmentId, TokenMaskInfo tokenMaskInfo,
			String dateOfBirthYear) {
		/**
		 * Since front-end has toggle on logic for travel doc, which means the request value of current segment may come from another segment, 
		 * so we may use MaskInfo of original segment to unmask the value, but the new value should always be updated to current segment
		 */
		
		// the MaskInfo the request value should compare to, if originSegmentId is not empty, should come from the origin segment, otherwise should come from current segment
		MaskInfo originPriDobDayMaskInfo = null;
		if (!StringUtils.isEmpty(originSegmentId)) {
			originPriDobDayMaskInfo = getMaskInfo(MaskFieldName.PRI_TRAVELDOC_DOB_YEAR, passengerId, originSegmentId, tokenMaskInfo.getMaskInfos());		
		} else {
			originPriDobDayMaskInfo = getMaskInfo(MaskFieldName.PRI_TRAVELDOC_DOB_YEAR, passengerId, segmentId, tokenMaskInfo.getMaskInfos());		
		}
		
		// the MaskInfo of current segment
		MaskInfo currentPriDobDayMaskInfo = getMaskInfo(MaskFieldName.PRI_TRAVELDOC_DOB_YEAR, passengerId, segmentId, tokenMaskInfo.getMaskInfos());
		if (currentPriDobDayMaskInfo == null) {
			currentPriDobDayMaskInfo = createMaskInfo(MaskFieldName.PRI_TRAVELDOC_DOB_YEAR, passengerId, segmentId, tokenMaskInfo);
		}
		
		if (originPriDobDayMaskInfo == null) {
			MaskInfo dobYearMaskInfo = getMaskInfo(MaskFieldName.DOB_YEAR, passengerId, null, tokenMaskInfo.getMaskInfos());	
			if (dobYearMaskInfo != null && dateOfBirthYear.equals(dobYearMaskInfo.getMaskedValue())) {
				/**
				 * if PRI_TRAVELDOC_DOB_YEAR mask info is not found but DOB_YEAR maskInfo is found
				 * and the request value is same with the masked value in it, populate the
				 * maskInfo to PRI_TRAVELDOC_DOB_YEAR
				 */
				currentPriDobDayMaskInfo.setOriginalValue(dobYearMaskInfo.getOriginalValue());
				currentPriDobDayMaskInfo.setMaskedValue(dobYearMaskInfo.getMaskedValue());
				currentPriDobDayMaskInfo.setEditedInCurrentSession(false);
				tokenMaskInfo.setMaskInfoUpdated(true);
				return dobYearMaskInfo.getOriginalValue();
			} else {
				/**
				 * if PRI_TRAVELDOC_DOB_YEAR maskInfo is not found and no masked info matched in
				 * DOB_YEAR maskInfo, create a new PRI_TRAVELDOC_DOB_YEAR maskInfo accordingly
				 */
				currentPriDobDayMaskInfo.setOriginalValue(dateOfBirthYear);
				currentPriDobDayMaskInfo.setEditedInCurrentSession(true);
				tokenMaskInfo.setMaskInfoUpdated(true);
				return dateOfBirthYear;
			}
		} else if (Objects.equal(dateOfBirthYear, originPriDobDayMaskInfo.getMaskedValue())) {
			/**
			 * When requested value is same as stored masked value, unmask to original value.
			 */
			currentPriDobDayMaskInfo.setOriginalValue(originPriDobDayMaskInfo.getOriginalValue());
			currentPriDobDayMaskInfo.setMaskedValue(originPriDobDayMaskInfo.getMaskedValue());
			currentPriDobDayMaskInfo.setEditedInCurrentSession(false);
			return originPriDobDayMaskInfo.getOriginalValue();
			
		} else {
			/**
			 * When requested value is different with stored masked value, update new value to current session.
			 */
			currentPriDobDayMaskInfo.setOriginalValue(dateOfBirthYear);
			currentPriDobDayMaskInfo.setEditedInCurrentSession(true);
			tokenMaskInfo.setMaskInfoUpdated(true);
			return dateOfBirthYear;
		}
	}
	
	/**
	 * Unmask travel document number of secondary <code>UpdateTravelDocDTO</code>.
	 * 
	 * @param travelDocDTO
	 * @param passengerId
	 * @param segmentId
	 * @param tokenMaskInfo
	 */
	private void unmaskSecTravelDocDTO(UpdateTravelDocDTOV2 travelDocDTO, String passengerId, String segmentId,
			TokenMaskInfo tokenMaskInfo) {

		if (travelDocDTO != null) {
			String travelDocNumber = travelDocDTO.getTravelDocumentNumber();
			travelDocDTO.setTravelDocumentNumber(travelDocNumber == null ? travelDocNumber
					: getSubmissionValue(MaskFieldName.SEC_TRAVELDOC_NO, travelDocNumber.toUpperCase(), passengerId,
							segmentId, null, tokenMaskInfo));
			
			String dateOfBirth = travelDocDTO.getDateOfBirth();
			String dateOfBirthDay = BizRulesUtil.getDayFromDateOfBirth(dateOfBirth);
			String dateOfBirthMonth = BizRulesUtil.getMonthFromDateOfBirth(dateOfBirth);
			String dateOfBirthYear = BizRulesUtil.getYearFromDateOfBirth(dateOfBirth);
			
			if (StringUtils.isEmpty(dateOfBirthDay) && StringUtils.isEmpty(dateOfBirthMonth)
					&& StringUtils.isEmpty(dateOfBirthYear)) {
				travelDocDTO.setDateOfBirth(dateOfBirth);
			} else {
				String submissionDay = getSubmissionValue(MaskFieldName.SEC_TRAVELDOC_DOB_DAY, dateOfBirthDay, passengerId, segmentId, null, tokenMaskInfo);
				String submissionYear = getSubmissionValue(MaskFieldName.SEC_TRAVELDOC_DOB_YEAR, dateOfBirthYear, passengerId, segmentId, null, tokenMaskInfo);
		
				travelDocDTO.setDateOfBirth(submissionYear+"-"+dateOfBirthMonth+"-"+submissionDay);
			}
		}

	}

	/**
	 * Unmask number of <code>UpdateTsDTO</code> of redress.
	 * 
	 * @param redressDTO
	 * @param passengerId
	 * @param tokenMaskInfo
	 */
	private void unmaskRedressDTO(UpdateTsDTOV2 redressDTO, String passengerId, TokenMaskInfo tokenMaskInfo) {

		if (redressDTO != null && !StringUtils.isEmpty(redressDTO.getNumber())) {
			redressDTO.setNumber(
					getSubmissionValue(MaskFieldName.REDRESS_NO, redressDTO.getNumber().toUpperCase(), passengerId, null, null, tokenMaskInfo));
		}

	}

	/**
	 * Unmask number of <code>UpdateTsDTO</code> of KTN.
	 * 
	 * @param ktnDTO
	 * @param passengerId
	 * @param tokenMaskInfo
	 */
	private void unmaskKtnDTO(UpdateTsDTOV2 ktnDTO, String passengerId, TokenMaskInfo tokenMaskInfo) {

		if (ktnDTO != null && !StringUtils.isEmpty(ktnDTO.getNumber())) {
			ktnDTO.setNumber(getSubmissionValue(MaskFieldName.KTN, ktnDTO.getNumber().toUpperCase(), passengerId, null, null, tokenMaskInfo));
		}

	}

	/**
	 * Unmask phone number of <code>UpdatePhoneInfoDTO</code>.
	 * 
	 * @param phoneInfoDTO
	 * @param unlocked
	 */
	private void unmaskPhoneInfoDTO(UpdatePhoneInfoDTOV2 phoneInfoDTO, String passengerId, TokenMaskInfo tokenMaskInfo) {

		if (phoneInfoDTO != null) {
			phoneInfoDTO.setPhoneNo(getSubmissionValue(MaskFieldName.PHONE_NO, phoneInfoDTO.getPhoneNo(), passengerId,
					null, null, tokenMaskInfo));

			if (!CollectionUtils.isEmpty(phoneInfoDTO.getApplyPassengerId())) {
				setMaskInfosToApplyPax(passengerId, phoneInfoDTO.getApplyPassengerId(), MaskFieldName.PHONE_NO, null, tokenMaskInfo);
			}
		}
		

	}

	/**
	 * Unmask Email of <code>UpdateEmailDTO</code>.
	 * 
	 * @param emailDTO
	 * @param passengerId
	 * @param tokenMaskInfo
	 */
	private void unmaskEmailDTO(UpdateEmailDTOV2 emailDTO, String passengerId, TokenMaskInfo tokenMaskInfo) {

		if (emailDTO != null && !StringUtils.isEmpty(emailDTO.getEmail())) {
			emailDTO.setEmail(
					getSubmissionValue(MaskFieldName.EMAIL, emailDTO.getEmail().toLowerCase(), passengerId, null, null, tokenMaskInfo));
			
			if (!CollectionUtils.isEmpty(emailDTO.getApplyPassengerId())) {
				setMaskInfosToApplyPax(passengerId, emailDTO.getApplyPassengerId(), MaskFieldName.EMAIL, null, tokenMaskInfo);
			}
		}

	}

	/**
	 * Unmask phone number of <code>UpdateEmergencyContactDTO</code>.
	 * 
	 * @param emrContactInfoDTO
	 * @param unlocked
	 */

	private void unmaskEmgerContactInfoDTO(UpdateEmergencyContactDTOV2 emrContactInfoDTO, String passengerId,
			TokenMaskInfo tokenMaskInfo) {

		if (emrContactInfoDTO != null) {
			emrContactInfoDTO.setPhoneNo(getSubmissionValue(MaskFieldName.EMER_PHONE_NO, emrContactInfoDTO.getPhoneNo(),
					passengerId, null, null, tokenMaskInfo));
		}

	}

	/**
	 * Unmask the value if the field is masked and not changed.
	 * 
	 * @param fieldName
	 * @param updateRequestValue
	 * @param paxId
	 * @param segId
	 * @param tokenMaskInfo
	 * @return
	 */
	private String getSubmissionValue(MaskFieldName fieldName, String updateRequestValue, String paxId, String segId, String originSegId, TokenMaskInfo tokenMaskInfo) {
		
		if (StringUtils.isEmpty(updateRequestValue) || tokenMaskInfo == null || tokenMaskInfo.getMaskInfos() == null) {
			return updateRequestValue;
		}
		
		/** FIXME - HOT FIX FOR MOBILE: SUPPORT "*" TO UNMASK **/
		String convertedUpdateRequestValue = null;
		if (!StringUtils.isEmpty(updateRequestValue) && updateRequestValue.contains("*")) {
			convertedUpdateRequestValue = updateRequestValue.replaceAll("\\*", "●");
		}
		
		/**
		 * Since front-end has toggle on logic for travel doc, which means the request value of current segment may come from another segment, 
		 * so we may use MaskInfo of original segment to unmask the value, but the new value should always be updated to current segment
		 */
		
		// the mask info where the request value should compare to, if originSegId is not empty, should come from the origin segment, else come from current segment
		MaskInfo originMaskInfo = null;
		if (StringUtils.isEmpty(originSegId)) {
			originMaskInfo = getMaskInfo(fieldName, paxId, segId, tokenMaskInfo.getMaskInfos());
		} else {
			originMaskInfo = getMaskInfo(fieldName, paxId, originSegId, tokenMaskInfo.getMaskInfos());
		}
		
		// the mask info of current segment
		MaskInfo currentMaskInfo = getMaskInfo(fieldName, paxId, segId, tokenMaskInfo.getMaskInfos());
		if (currentMaskInfo == null) {
			currentMaskInfo = createMaskInfo(fieldName, paxId, segId, tokenMaskInfo);
		}
		
		if (originMaskInfo == null) {
			/*
			 * When existing mask info is not found, update the new mask info to current segment.
			 */
			currentMaskInfo.setOriginalValue(updateRequestValue);
			currentMaskInfo.setEditedInCurrentSession(true);
			tokenMaskInfo.setMaskInfoUpdated(true);
			return updateRequestValue;
		} else {
			if (Objects.equal(updateRequestValue, originMaskInfo.getMaskedValue())
					|| (!StringUtils.isEmpty(convertedUpdateRequestValue)
							&& convertedUpdateRequestValue.equals(originMaskInfo.getMaskedValue()))) {
				/*
				 * When requested value is same as stored masked value, unmask to original
				 * value.
				 */
				currentMaskInfo.setOriginalValue(originMaskInfo.getOriginalValue());
				currentMaskInfo.setMaskedValue(originMaskInfo.getMaskedValue());
				currentMaskInfo.setEditedInCurrentSession(false);
				return originMaskInfo.getOriginalValue();

			} else {
				/*
				 * When requested value is different with stored masked value, update new value
				 * to current session.
				 */
				currentMaskInfo.setOriginalValue(updateRequestValue);
				currentMaskInfo.setEditedInCurrentSession(true);
				tokenMaskInfo.setMaskInfoUpdated(true);
				return updateRequestValue;
			}
		}
		
	}
	
	/**
	 * get original value of the masked value from maskInfo list
	 *		| if no matched maskInfo is found in the list, return ""
	 *		| else if input masked value is the same as the mask value stored in the list, return original value
	 *		| else if input masked value is the same as the original value stored in the list, return original value
	 *		| else return ""
	 * @param fieldName
	 * @param maskedValue
	 * @param paxId
	 * @param segId
	 * @param rloc
	 * @return
	 */
	public String getOriginalValue(MaskFieldName fieldName, String maskedValue, String paxId, String segId, List<MaskInfo> maskInfos) {
		if (maskInfos == null || StringUtils.isEmpty(maskedValue) || fieldName == null) {
			return "";
		}
		
		MaskInfo maskInfo = getMaskInfo(fieldName, paxId, segId, maskInfos);
		if (maskInfo == null) {
			/*
			 * When existing mask info is not found, return ""
			 */
			return "";
		} else if (Objects.equal(maskedValue, maskInfo.getMaskedValue())) {
			/*
			 * When maskedValue is same as stored masked value, unmask to original value.
			 */
			return maskInfo.getOriginalValue();
			
		} else if (Objects.equal(maskedValue, maskInfo.getOriginalValue())){
			/*
			 * When maskedValue is different with stored masked value but same as stored original value, return original value
			 */
			return maskInfo.getOriginalValue();
		} else {
			return "";
		}

	}
	
	/**
	 * get mask info list
	 * @param rloc
	 * @return
	 */
	public List<MaskInfo> getMaskInfos(String rloc){
		@SuppressWarnings("unchecked")
		List<MaskInfo> maskInfos = mbTokenCacheRepository.get(MMBUtil.getCurrentMMBToken(), TokenCacheKeyEnum.MASK_INFO,
				rloc, ArrayList.class);
		
		return maskInfos;
	}
	
	/**
	 * Copy maskInfo of update passenger to apply passengers
	 * @param passengerId
	 * @param applyPassengerId
	 * @param fieldName
	 * @param segId
	 * @param tokenMaskInfo
	 */
	private void setMaskInfosToApplyPax(String passengerId, List<String> applyPassengerId, MaskFieldName fieldName, String segId, TokenMaskInfo tokenMaskInfo) {
		MaskInfo updatePassengerMaskInfo = getMaskInfo(fieldName, passengerId, segId, tokenMaskInfo.getMaskInfos());
		if (updatePassengerMaskInfo == null) {
			return;
		}
		
		for(String paxId: applyPassengerId) {
			MaskInfo applyPassengerMaskInfo = Optional.ofNullable(getMaskInfo(fieldName, paxId, segId, tokenMaskInfo.getMaskInfos())).orElse(createMaskInfo(fieldName, paxId, segId, tokenMaskInfo));
			
			applyPassengerMaskInfo.setOriginalValue(updatePassengerMaskInfo.getOriginalValue());
			applyPassengerMaskInfo.setMaskedValue(updatePassengerMaskInfo.getMaskedValue());
			applyPassengerMaskInfo.setEditedInCurrentSession(updatePassengerMaskInfo.isEditedInCurrentSession());
		}
	}
	
}
