
package com.cathaypacific.mmbbizrule.service.impl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.booking.BookingSources;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileService;
import com.cathaypacific.mmbbizrule.db.dao.ConstantDataDAO;
import com.cathaypacific.mmbbizrule.db.model.ConstantData;
import com.cathaypacific.mmbbizrule.model.booking.summary.TempLinkedBooking;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePreference;
import com.cathaypacific.mmbbizrule.model.profile.ProfileTravelDoc;
import com.cathaypacific.mmbbizrule.model.profile.TravelCompanion;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrEticket;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.util.FreeTextUtil;
import com.cathaypacific.mmbbizrule.repository.TempLinkedBookingRepository;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.cathaypacific.mmbbizrule.util.NameIdentficationUtil;

@Service
public class PaxNameIdentificationServiceImpl implements PaxNameIdentificationService {
	private static LogAgent logger = LogAgent.getLogAgent(PaxNameIdentificationServiceImpl.class);

	@Value("${givenName.maxCharacterToMatch}")
	Integer shortCompareSize;

	@Autowired
	ConstantDataDAO constantDataDAO;
	
	@Autowired
	private RetrieveProfileService retrieveProfileService;
	
	@Autowired
	private TempLinkedBookingRepository linkTempBookingRepository;
	
	@Override
    public void primaryPaxIdentificationForMice(LoginInfo loginInfo, RetrievePnrBooking booking)
            throws BusinessBaseException {

        RetrievePnrPassenger primaryPassenger;

        // prepare do name matching
        Map<String, String[]> bookingPaxNameMap = buildNameCheckingNameMap(booking.getPassengers());
        List<String> nameTitleList = getNameTitleList();
      

        // get primary passenger from profile
        primaryPassenger = getPrimaryPaxForMice(loginInfo, booking.getPassengers(), bookingPaxNameMap,
                nameTitleList);
        // temp booking
        if (primaryPassenger == null) {
            ProfilePreference profilePreference = retrieveProfileService.retrievePreference(loginInfo.getMemberId(), null);
            primaryPassenger = getPrimaryPaxFromLinkedBooking(loginInfo, booking, bookingPaxNameMap, nameTitleList,
                    profilePreference);
        }

        if (primaryPassenger != null) {
            primaryPassenger.setPrimaryPassenger(true);
        } else {
            throw new ExpectedException(String.format(
                    "The EODS booking [%s] is filtered out by member ID [%s] booking summary list because of mismatch of SK CUST checking",
                    booking.getOneARloc(), loginInfo.getMemberId()), new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
        }
    }

	@Override
	public void primaryPaxIdentificationForRloc(String familyName, String givenName, RetrievePnrBooking booking)
			throws BusinessBaseException {
		if (StringUtils.isEmpty(familyName) || StringUtils.isEmpty(givenName)) {
			logger.warn("empty value exception");
			throw new UnexpectedException("", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}

		List<RetrievePnrPassenger> paxs = booking.getPassengers();
		if (CollectionUtils.isEmpty(paxs)) {
			logger.warn("There is no passenger in booking");
			throw new UnexpectedException("There is no passenger in booking", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}

		nameIdentificationForRlocLogin(paxs, familyName, givenName,booking.getOneARloc());
	}

	@Override
	public void primaryPaxIdentificationForETicket(String familyName, String givenName, String eTicket,
			RetrievePnrBooking booking) throws BusinessBaseException {
		// Get passenger id list by eTicket
		List<String> paxIdList = booking.getPassengerSegments().stream().filter(paxSegment -> {
			for (String paxETicket : paxSegment.getEtickets().stream().map(RetrievePnrEticket::getTicketNumber).collect(Collectors.toList())) {
				if (eTicket.equals(paxETicket)) {
					return true;
				}
			}
			return false;
		}).map(RetrievePnrPassengerSegment::getPassengerId).distinct().collect(Collectors.toList());
		if (CollectionUtils.isEmpty(paxIdList)) {
			logger.warn("No passenger found by eTicket");
			throw new UnexpectedException("No passenger found by eTicket", new ErrorInfo(ErrorCodeEnum.ERR_LOGIN_NAME_NOT_MATCH));
		}

		// Get passenger list by passenger id
		List<RetrievePnrPassenger> paxList = booking.getPassengers().stream().filter(pax->paxIdList.contains(pax.getPassengerID())).collect(Collectors.toList());
		
		nameIdentfication(paxList, familyName, givenName,booking.getOneARloc());
		//check infant login: cannot login with infant eticket
		RetrievePnrPassenger loginPax = paxList.stream().filter(RetrievePnrPassenger::isPrimaryPassenger).findFirst().orElse(null);
		if(loginPax != null && BookingBuildUtil.isInfant(loginPax.getPassengerType())){
			throw new UnexpectedException(String.format("Login with infant eticaket:familyName[%s] givenName[%s] eticket[%s]", familyName,givenName,eTicket), new ErrorInfo(ErrorCodeEnum.ERR_INFANT_TICKET_LOGIN));
		}
	}
	
	@Override
	public void primaryPaxIdentificationForMember(LoginInfo loginInfo, RetrievePnrBooking booking)
			throws BusinessBaseException {

		RetrievePnrPassenger primaryPassenger;
		String loginMemberId = loginInfo.getMemberId();
		
		// 1) Check by fqtv
		primaryPassenger = getLoginPaxByFqtvForMember(loginMemberId, booking);

		// if the pax indentify by fqtv or fqtr, set  Primary/Login dir.
		if (primaryPassenger != null) {
			primaryPassenger.setPrimaryPassenger(true);
			primaryPassenger.setLoginMember(true);
			primaryPassenger.setLoginFFPMatched(true);
		}
		
		// prepare do name matching
		Map<String, String[]> bookingPaxNameMap = buildNameCheckingNameMap(booking.getPassengers());
		List<String> nameTitleList = getNameTitleList();  
		ProfilePreference profilePreference = retrieveProfileService.retrievePreference(loginInfo.getMemberId(), null);
		
		//parser companion
		List<RetrievePnrPassenger> companionList = getMatchedPassengersByCompanion(booking.getPassengers(), bookingPaxNameMap, nameTitleList, profilePreference);
		//set profile companion flag
		setProfileCompanionFlag(companionList); 
		
		//EODS/TEMP booking case
		if (primaryPassenger == null) {
			
			//2)check EODS booking and fqtr booking, the primary pax identification logic should same for them(related to member booking but non fqtv ), 
			boolean passengerRelatedToMember = isPassengerMatchedCust(loginInfo, booking) || getLoginPaxByFqtrForMember(loginMemberId, booking) != null;
			
			if (passengerRelatedToMember) {
				primaryPassenger = getPrimaryPaxForNonFqtvBooking(loginInfo, companionList, booking.getPassengers(), bookingPaxNameMap, nameTitleList, profilePreference);
			} 
			
			//3) temp booking
			if (primaryPassenger == null) {
				primaryPassenger = getPrimaryPaxFromLinkedBooking(loginInfo, booking, bookingPaxNameMap, nameTitleList, profilePreference);
			}
			
			if (primaryPassenger == null && passengerRelatedToMember) {
				primaryPassenger = booking.getPassengers().get(0);
			}
		}
		 
		if(primaryPassenger!=null){
			primaryPassenger.setPrimaryPassenger(true);
		}else{
			throw new ExpectedException(
					String.format("The EODS booking [%s] is filtered out by member ID [%s] booking summary list because of mismatch of SK CUST checking", booking.getOneARloc(),loginInfo.getMemberId()),
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
	}
	
	/**
	 * set profile companion flag
	 * @param companionList
	 */
	private void setProfileCompanionFlag(List<RetrievePnrPassenger> companionList) {
		if (!CollectionUtils.isEmpty(companionList)) {
			companionList.forEach(pax -> {
				if (BooleanUtils.isNotTrue(pax.isLoginMember())) {
					pax.setCompanion(true);
				}
			});
		}
	}
	/**
	 * get primary pax for Eods booking
	 * @param profileOrTravelDocMatchedPax
	 * @param companionList
	 * @param pnrPaxList
	 */
	private RetrievePnrPassenger getPrimaryPaxForNonFqtvBooking(LoginInfo loginInfo,
			List<RetrievePnrPassenger> companionList, List<RetrievePnrPassenger> pnrPaxList,
			Map<String, String[]> bookingPaxNameMap, List<String> nameTitleList, ProfilePreference profilePreference) {

		RetrievePnrPassenger matchedPax = getMatchedPassengersByProfileAndTravelDoc(loginInfo, pnrPaxList,
				bookingPaxNameMap, nameTitleList, profilePreference);

		if (matchedPax != null) {
			matchedPax.setLoginMember(true);
		} else if (!CollectionUtils.isEmpty(companionList)) {
			matchedPax = companionList.get(0);
		}

		return matchedPax;
	}
	
	/**
     * get primary pax for mice booking
     * @param profileOrTravelDocMatchedPax
     * @param companionList
     * @param pnrPaxList
     */
    private RetrievePnrPassenger getPrimaryPaxForMice(LoginInfo loginInfo, List<RetrievePnrPassenger> pnrPaxList,
            Map<String, String[]> bookingPaxNameMap, List<String> nameTitleList) {

        RetrievePnrPassenger matchedPax = getMatchedPassengersByProfile(loginInfo, pnrPaxList,
                bookingPaxNameMap, nameTitleList);

        if (matchedPax != null) {
            matchedPax.setLoginMember(true);
        }

        return matchedPax;
    }
	
	/**
	 * build the map of pax name in booking
	 * @param paxList
	 * @return
	 */
	private  Map<String, String[]> buildNameCheckingNameMap(List<RetrievePnrPassenger> paxList){
		Map<String, String[]> paxMap = new HashMap<>() ;
		for (RetrievePnrPassenger retrievePnrPassengers: paxList) {
			String[] nameArray = new String[] {retrievePnrPassengers.getGivenName(),retrievePnrPassengers.getFamilyName()};
			paxMap.put(retrievePnrPassengers.getPassengerID() , nameArray );
			
		}
		return paxMap;
	}
	
	/**
	 * Get name title list
	 * 
	 * @return
	 */
	private List<String> getNameTitleList() {

		return constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE, OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList());
	}
	
	/**
	 * check the linked booking 
	 * @param loginInfo
	 * @param booking
	 * @return
	 * @throws BusinessBaseException
	 */
	private RetrievePnrPassenger getPrimaryPaxFromLinkedBooking(LoginInfo loginInfo,RetrievePnrBooking booking,
			Map<String, String[]> bookingPaxNameMap, List<String> nameTitleList, ProfilePreference profilePreference)
			throws BusinessBaseException {

		List<TempLinkedBooking> linkedBookings = linkTempBookingRepository.getLinkedBookings(loginInfo.getMmbToken());

		TempLinkedBooking linkedBooking = linkedBookings.stream()
				.filter(lb -> Objects.equals(booking.getOneARloc(), lb.getRloc())).findFirst().orElse(null);

		if (linkedBooking == null) {
			return null;
		}

		RetrievePnrPassenger primaryPax = null;
		// check if stored primary pax id
		if (!StringUtils.isEmpty(linkedBooking.getPrimaryPassengerId())) {
			primaryPax = booking.getPassengers().stream()
					.filter(pax -> Objects.equals(pax.getPassengerID(), linkedBooking.getPrimaryPassengerId()))
					.findFirst().orElse(null);

		}
		// check by pax name
		if (primaryPax == null) {
			this.primaryPaxIdentificationForRloc(linkedBooking.getFamilyName(), linkedBooking.getGivenName(), booking);
			primaryPax = booking.getPrimaryPassenger();
		}

        if (primaryPax != null) {
            RetrievePnrPassenger matchedPax;
            if (BookingBuildUtil.isMiceBooking(booking.getSkList())) {
                matchedPax = getMatchedPassengersByProfile(loginInfo, Arrays.asList(primaryPax), bookingPaxNameMap,
                        nameTitleList);
            } else {
                matchedPax = getMatchedPassengersByProfileAndTravelDoc(loginInfo, Arrays.asList(primaryPax),
                        bookingPaxNameMap, nameTitleList, profilePreference);
            }
            if (matchedPax != null
                    // if the booking is linked after RU enrollment, then don't need to match the
                    // travel doc and regard the passenger in linked booking as a login member
                    || BookingSources.RU_ENROL.equals(linkedBooking.getBookingSources())) {

                primaryPax.setLoginMember(true);
            }
        }
		return primaryPax;
	}
	/**
	 * Check sk CUST
	 * @param loginInfo
	 * @param booking
	 * @return
	 */
	public boolean isPassengerMatchedCust(LoginInfo loginInfo, RetrievePnrBooking booking) {
		
		List<RetrievePnrDataElements> custList = Optional
				.ofNullable(booking.getSkList())
				.orElse(Collections.emptyList())
				.stream()
				.filter(sk -> MMBBizruleConstants.BOOKING_ADD_RESULT_CUST.equals(sk.getType()))
				.collect(Collectors.toList());

		return custList.stream().anyMatch(sk -> {
			
			String custMemberId = FreeTextUtil.getFfpFromCustDFreeText(sk.getFreeText());
			return Objects.equals(loginInfo.getMemberId(), custMemberId)
					|| (StringUtils.isNotEmpty(loginInfo.getOriginalRuMemberId()) && loginInfo.getOriginalRuMemberId().equals(custMemberId));
			
		});

	}
	/**
	 * get login pax by fqtr
	 * @param loginMemberId
	 * @param booking
	 * @return
	 */
	private RetrievePnrPassenger getLoginPaxByFqtrForMember(String loginMemberId,
			RetrievePnrBooking booking) {
		RetrievePnrPassenger loginPassemger;
		// find by fqtv from product level
		RetrievePnrPassengerSegment rps = booking.getPassengerSegments().stream().filter(
				ps -> ps.getFQTRInfos().stream().anyMatch(fqtr -> loginMemberId.equals(fqtr.getFfpMembershipNumber())))
				.findFirst().orElse(null);

		if (rps != null) {
			loginPassemger = booking.getPassengers().stream()
					.filter(p -> p.getPassengerID().equals(rps.getPassengerId())).findFirst().orElse(null);
			// find by fqtr from pax level
		} else {
			loginPassemger = booking.getPassengers().stream()
					.filter(p -> p.getFQTRInfos().stream()
							.anyMatch(fqtr -> loginMemberId.equals(fqtr.getFfpMembershipNumber())))
					.findFirst().orElse(null);
		}
		return loginPassemger;
	}
	/**
	 * get login pax by fqtv
	 * @param loginMemberId
	 * @param booking
	 * @return
	 */
	private RetrievePnrPassenger getLoginPaxByFqtvForMember(String loginMemberId,
			RetrievePnrBooking booking) {
		RetrievePnrPassenger loginPassemger;
		// find by fqtv from product level
		RetrievePnrPassengerSegment rps = booking.getPassengerSegments().stream().filter(
				ps -> ps.getFQTVInfos().stream().anyMatch(fqtv -> loginMemberId.equals(fqtv.getFfpMembershipNumber())))
				.findFirst().orElse(null);

		if (rps != null) {
			loginPassemger = booking.getPassengers().stream()
					.filter(p -> p.getPassengerID().equals(rps.getPassengerId())).findFirst().orElse(null);
			// find by fqtv from pax level
		} else {
			loginPassemger = booking.getPassengers().stream()
					.filter(p -> p.getFQTVInfos().stream()
							.anyMatch(fqtv -> loginMemberId.equals(fqtv.getFfpMembershipNumber())))
					.findFirst().orElse(null);
		}
		return loginPassemger;
	}
	
	private void nameIdentfication (List<RetrievePnrPassenger> paxList, String familyName, String givenName,String rloc) throws BusinessBaseException {
		Map<String, String[]> paxMap = new HashMap<>();
		for (RetrievePnrPassenger retrievePnrPassengers: paxList) {
			String[] nameArray = new String[] {retrievePnrPassengers.getGivenName(),retrievePnrPassengers.getFamilyName()};
			paxMap.put(retrievePnrPassengers.getPassengerID() , nameArray );
			
		}
		
		List<String> nameTitleList = constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE,OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList());
		
		List<String> matchedNames = NameIdentficationUtil.nameIdentification(familyName, givenName, paxMap, nameTitleList, shortCompareSize);
		
		if (CollectionUtils.isEmpty(matchedNames)) {
			logger.warn("no match with whole name exception");
			throw new ExpectedException(
					String.format("Cannot match pax name,request data[familyName:%s givenName:%s rloc:%s]", familyName,givenName, rloc),
					new ErrorInfo(ErrorCodeEnum.ERR_LOGIN_NAME_NOT_MATCH));
		} else if (matchedNames.size() > 1) {
			logger.warn("Multiple matches exception");
			throw new ExpectedException(
					String.format("Multiple matched pax name found,request data[familyName:%s givenName:%s rloc:%s]", familyName,givenName, rloc),
					new ErrorInfo(ErrorCodeEnum.ERR_MULTI_PAX_FOUND));
		}
		
		for (RetrievePnrPassenger retrievePnrPassengers: paxList) {
			if(retrievePnrPassengers.getPassengerID().equals(matchedNames.get(0))) {
				retrievePnrPassengers.setPrimaryPassenger(true);
				logger.debug(String.format("Matched passenger ID : [%s], First Name :[%s], Last Name: [%s]", retrievePnrPassengers.getPassengerID(), retrievePnrPassengers.getGivenName(), retrievePnrPassengers.getFamilyName()));
			}
		}
	}
	
	/**
	 * name identification for rloc login flow
	 * @param paxList
	 * @param familyName
	 * @param givenName
	 * @param rloc
	 * @throws BusinessBaseException
	 */
	private void nameIdentificationForRlocLogin(List<RetrievePnrPassenger> paxList, String familyName, String givenName,String rloc) throws BusinessBaseException {
		Map<String, String[]> paxMap = new HashMap<>();
		for (RetrievePnrPassenger retrievePnrPassengers: paxList) {
			String[] nameArray = new String[] {retrievePnrPassengers.getGivenName(),retrievePnrPassengers.getFamilyName()};
			paxMap.put(retrievePnrPassengers.getPassengerID() , nameArray );
			
		}
		
		List<String> nameTitleList = constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE,OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList());
		
		List<String> matchedNames = NameIdentficationUtil.nameIdentification(familyName, givenName, paxMap, nameTitleList, shortCompareSize);
		
		if (CollectionUtils.isEmpty(matchedNames)) {
			logger.warn("no match with whole name exception");
			throw new ExpectedException(
					String.format("Cannot match pax name,request data[familyName:%s givenName:%s rloc:%s]", familyName,givenName, rloc),
					new ErrorInfo(ErrorCodeEnum.ERR_LOGIN_NAME_NOT_MATCH));
		} else if (matchedNames.size() > 1) {
			// if multiple names matched with short compare logic, compare again using the entire name.
			matchedNames = NameIdentficationUtil.nameIdentification(familyName, givenName, paxMap, nameTitleList,
					BizRulesUtil.removeSpecialCharactersFromStr(givenName == null ? org.apache.commons.lang.StringUtils.EMPTY : givenName).length());
			// after comparing by entire name, if there's no name found or still multiple names found, throw multiple matched exception
			if (CollectionUtils.isEmpty(matchedNames) || matchedNames.size() > 1) {
				logger.warn("Multiple matches exception");
				throw new ExpectedException(
						String.format("Multiple matched pax name found,request data[familyName:%s givenName:%s rloc:%s]", familyName,givenName, rloc),
						new ErrorInfo(ErrorCodeEnum.ERR_MULTI_PAX_FOUND));
			}
		}
		
		for (RetrievePnrPassenger retrievePnrPassengers: paxList) {
			if(retrievePnrPassengers.getPassengerID().equals(matchedNames.get(0))) {
				retrievePnrPassengers.setPrimaryPassenger(true);
				logger.debug(String.format("Matched passenger ID : [%s], First Name :[%s], Last Name: [%s]", retrievePnrPassengers.getPassengerID(), retrievePnrPassengers.getGivenName(), retrievePnrPassengers.getFamilyName()));
			}
		}
	}
	/**
	 * 
	 * @param loginInfo
	 * @param booking
	 * @return
	 * @throws BusinessBaseException
	 */
	private RetrievePnrPassenger getMatchedPassengersByProfileAndTravelDoc (LoginInfo loginInfo, List<RetrievePnrPassenger> pnrPaxs, Map<String, String[]> paxMap,
			List<String> nameTitleList, ProfilePreference profilePreference) {
		RetrievePnrPassenger matchedPax = null;
		
		// 1)For AM/MPO member, conduct name matching against name in the member profile or travel doc
		if(MMBUtil.isAmLogin(loginInfo) || MMBUtil.isMpoLogin(loginInfo)){
			ProfilePersonInfo profilePersonInfo = retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), loginInfo.getMmbToken());
			 matchedPax = getMatchedPassengerByProfileName(pnrPaxs, paxMap, nameTitleList,  profilePersonInfo,loginInfo);
			 
		}
		
		if(matchedPax != null){
			return matchedPax;
		}
		
		// 2) For RU, if no stored travel doc, by default treat as member self-travelling booking. Else match with travel doc name.
		if (MMBUtil.isRuLogin(loginInfo) &&
			(profilePreference == null || CollectionUtils.isEmpty(profilePreference.getPersonalTravelDocuments()))
		){
			Optional<Entry<String, String[]>> optional = paxMap.entrySet().stream().findFirst();
			if (optional.isPresent()) {
				String passengerId = optional.get().getKey();
				return getMatchedPassenger(passengerId, pnrPaxs);
			}
		}
		
		
		// 3) check member by travel doc for all member if cannot find name match of member profile name 
		matchedPax  = getMatchedPassengerByMemberTraveldoc(pnrPaxs, paxMap, nameTitleList, profilePreference); 
			 
		return matchedPax;
	}
	
	/**
     * 
     * @param loginInfo
     * @param booking
     * @return
     * @throws BusinessBaseException
     */
    private RetrievePnrPassenger getMatchedPassengersByProfile(LoginInfo loginInfo, List<RetrievePnrPassenger> pnrPaxs, Map<String, String[]> paxMap,
            List<String> nameTitleList) {
        RetrievePnrPassenger matchedPax = null;

        ProfilePersonInfo profilePersonInfo = retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(),
                loginInfo.getMmbToken());
        matchedPax = getMatchedPassengerByProfileName(pnrPaxs, paxMap, nameTitleList, profilePersonInfo, loginInfo);

        if (matchedPax != null) {
            return matchedPax;
        }

        return matchedPax;
    }
 
	/**
	 * get the Companion in of the booking 
	 * @param booking
	 * @param paxMap
	 * @param nameTitleList
	 * @param profilePreference
	 * @return
	 */
	private List<RetrievePnrPassenger> getMatchedPassengersByCompanion(List<RetrievePnrPassenger> pnrPaxs, Map<String, String[]> paxMap,
			List<String> nameTitleList, ProfilePreference profilePreference) {
		
		if(profilePreference == null || CollectionUtils.isEmpty(profilePreference.getTravelCompanions())){
			return Collections.emptyList();
		}
		
		List<RetrievePnrPassenger> matchedList = new ArrayList<>();
	
		for (TravelCompanion travelCompanion : profilePreference.getTravelCompanions()) {
			if (travelCompanion.getTravelDocument() != null) {
				String passengerId = getMatchedPassengerIdFromTravDoc(travelCompanion.getTravelDocument(), paxMap,
						nameTitleList, shortCompareSize);
				if (passengerId != null) {
					RetrievePnrPassenger passenger = getMatchedPassenger(passengerId, pnrPaxs);
					if (passenger != null) {
						matchedList.add(passenger);
					}
				}
			}
		}
		return matchedList;
	}
	/**
	 * 
	 * @param booking
	 * @param paxMap
	 * @param nameTitleList
	 * @param profilePreference
	 * @return
	 * @throws BusinessBaseException
	 */
	private RetrievePnrPassenger getMatchedPassengerByMemberTraveldoc(List<RetrievePnrPassenger> pnrPaxs, Map<String, String[]> paxMap,
			List<String> nameTitleList, ProfilePreference profilePreference) {
		if(profilePreference == null || CollectionUtils.isEmpty(profilePreference.getPersonalTravelDocuments())){
			return null;
		}
		for (ProfileTravelDoc profileTravelDoc : profilePreference.getPersonalTravelDocuments()) {
			String passengerId = getMatchedPassengerIdFromTravDoc(profileTravelDoc, paxMap, nameTitleList, shortCompareSize);
			if(passengerId != null) {
				 return getMatchedPassenger(passengerId, pnrPaxs);
			}
		}
		
		return null;
	}
 

	/**
	 * Check member profile name 
	 * @param booking
	 * @param paxMap
	 * @param nameTitleList
	 * @param loginInfo
	 * @return
	 * @throws BusinessBaseException
	 */
	private RetrievePnrPassenger getMatchedPassengerByProfileName(List<RetrievePnrPassenger> pnrPaxs,
			Map<String, String[]> paxMap, List<String> nameTitleList, ProfilePersonInfo profilePersonInfo,LoginInfo loginInfo) 
			  {
	
		if (!LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType())) {
			return null;
		}
		
		String passengerId = getMatchedPassengerIdFromMemberProfile(profilePersonInfo, paxMap, nameTitleList, shortCompareSize);

		return passengerId == null ? null :getMatchedPassenger(passengerId, pnrPaxs);
	}
	
	
	
	
	private String getMatchedPassengerIdFromTravDoc (ProfileTravelDoc profileTravelDoc, Map<String, String[]> paxMap, List<String> nameTitleList, Integer shortCompareSize)  {
		List<String> passengerIds = NameIdentficationUtil.nameIdentification(profileTravelDoc.getFamilyName(), profileTravelDoc.getGivenName(), paxMap, nameTitleList, shortCompareSize);
		if(!CollectionUtils.isEmpty(passengerIds)) {
			return passengerIds.get(0);
		} else {
			return null;
		}
	}
	
	private String getMatchedPassengerIdFromMemberProfile (ProfilePersonInfo profilePersonInfo, Map<String, String[]> paxMap, List<String> nameTitleList, Integer shortCompareSize){
		List<String> passengerIds = NameIdentficationUtil.nameIdentification(profilePersonInfo.getFamilyName(), profilePersonInfo.getGivenName(), paxMap, nameTitleList, shortCompareSize);
		if(!CollectionUtils.isEmpty(passengerIds)) {
			return passengerIds.get(0);
		} else {
			return null;
		}
	}
	
	private RetrievePnrPassenger getMatchedPassenger (String passengerId, List<RetrievePnrPassenger> pnrPaxs) {
		RetrievePnrPassenger passenger = null;
		for (RetrievePnrPassenger px : pnrPaxs) {
			if (px.getPassengerID().equals(passengerId)) {
				passenger = px;
			}
			
		}
		return passenger;
	}

	@Override
	public void primaryPassengerIdentificationByInFo(LoginInfo loginInfo, RetrievePnrBooking pnrBooking)
			throws BusinessBaseException {
	
		if(pnrBooking == null){
			throw new UnexpectedException("PnrBooking cannot be null", new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		
		if (LoginInfo.LOGINTYPE_RLOC.equals(loginInfo.getLoginType())) {
			primaryPaxIdentificationForRloc(loginInfo.getLoginFamilyName(), loginInfo.getLoginGivenName(), pnrBooking);
		} else if (LoginInfo.LOGINTYPE_ETICKET.equals(loginInfo.getLoginType())) {
			primaryPaxIdentificationForETicket(loginInfo.getLoginFamilyName(), loginInfo.getLoginGivenName(), loginInfo.getLoginEticket(), pnrBooking);
		} else if (LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType()) && BookingBuildUtil.isMiceBooking(pnrBooking.getSkList())) {
		    // handling for mice register ru account
			primaryPaxIdentificationForMice(loginInfo, pnrBooking);
		} else if (LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType())) {
		    primaryPaxIdentificationForMember(loginInfo, pnrBooking);
		}
	}
	
	@Override
	public boolean isPassengerNameMatched(String familyName, String givenName, Map<String, String[]> paxMap, Integer shortCompareSize){
		List<String> nameTitleList = constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE,OneAConstants.NAME_TITLE_TYPE_IN_DB).stream().map(ConstantData::getValue).collect(Collectors.toList());
		List<String> matchedNames = null;
			matchedNames = NameIdentficationUtil.nameIdentification(familyName, givenName, paxMap, nameTitleList, shortCompareSize);
 
		return !CollectionUtils.isEmpty(matchedNames) && matchedNames.size() == 1;
	}
}
