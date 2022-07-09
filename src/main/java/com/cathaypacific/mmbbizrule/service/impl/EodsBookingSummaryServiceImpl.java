package com.cathaypacific.mmbbizrule.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.cxservice.eods.model.EODSBooking;
import com.cathaypacific.mmbbizrule.cxservice.eods.model.EODSBookingSegment;
import com.cathaypacific.mmbbizrule.cxservice.eods.service.EODSBookingService;
import com.cathaypacific.mmbbizrule.model.booking.summary.FlightBookingSummaryConvertBean;
import com.cathaypacific.mmbbizrule.service.EodsBookingSummaryService;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;

@Service
public class EodsBookingSummaryServiceImpl implements EodsBookingSummaryService{
	
	private static LogAgent logger = LogAgent.getLogAgent(EodsBookingSummaryServiceImpl.class);
	
	@Value("${flight.flown.limithours}")
	private int flightFlownLimithours;
	
	/*@Value("${givenName.maxCharacterToMatch}")
	private Integer shortCompareSize;*/
	
	@Value("${mmb.flight.passed.time}")
	private long flightPassedTime;

	@Autowired
	private EODSBookingService eodsBookingService;
	
 
/*	@Autowired
	private RetrieveProfileService retrieveProfileService;
	
	@Autowired
	private ConstantDataDAO constantDataDAO;*/
	
	@Async
	@Override
	public Future<List<FlightBookingSummaryConvertBean>> asynGetEodsBookingList(LoginInfo loginInfo, String token) throws BusinessBaseException {
		return new AsyncResult<>(this.getEodsBookingList(loginInfo, token));
	}
	
	@Override
	public List<FlightBookingSummaryConvertBean> getEodsBookingList(LoginInfo loginInfo, String token) throws BusinessBaseException {
		List<EODSBooking> eodsBookings = this.eodsBookingService.getBookingList(loginInfo.getMemberId());
		if(CollectionUtils.isEmpty(eodsBookings)) {
			return Collections.emptyList();
		}
		
		StringBuilder sb= new StringBuilder();
		eodsBookings.stream().forEach(pb->sb.append(pb.getRloc()+" "));
		logger.info(String.format("Retrieved EODS rloc(s) for member [%s]:[%s]", loginInfo.getMemberId(), sb.toString()));
		
		List<FlightBookingSummaryConvertBean> flightBookingSummarys = new ArrayList<>();
		
		for(EODSBooking eodsBooking : eodsBookings) {
			// check booking status
			if (!isValidBooking(eodsBooking, loginInfo, token)) {
				continue;
			}
			
			FlightBookingSummaryConvertBean flightBookingSummary = new FlightBookingSummaryConvertBean();
			flightBookingSummarys.add(flightBookingSummary);
			
			flightBookingSummary.setInEods(true);
			flightBookingSummary.setRloc(eodsBooking.getRloc());
		}
		
		return flightBookingSummarys;
	}

	
	/**
	 * check booking status
	 * @param pnrBooking
	 * @param memberId
	 * @return
	 * @throws BusinessBaseException 
	 */
	private boolean isValidBooking(EODSBooking eodsBooking, LoginInfo loginInfo, String token) throws BusinessBaseException {
		
		// 2)check departure time roughly
		if (!anyFlightInFlownLimitTime(eodsBooking.getSegments())) {
			logger.debug(String.format("All flights flown before 3 days: %s", eodsBooking.getRloc()));
			return false;
		}
		// no need check name because we will check sk cust  
		/*if (!passengersValid(eodsBooking.getPassengers(), loginInfo, token)) {
			logger.debug(String.format("Passenger is not valid: %s", eodsBooking.getRloc()));
			return false;
		}*/
		return true;
	}
	
	/**
	 * filter out the flights which flown < Limit time
	 * 
	 * @param pnrSegments
	 * @return
	 */
	private boolean anyFlightInFlownLimitTime (List<EODSBookingSegment> eodsSegments) {

		if (CollectionUtils.isEmpty(eodsSegments)) {
			return false;
		}

		for (EODSBookingSegment eodsSearchSegment : eodsSegments) {
			if (StringUtils.isEmpty(eodsSearchSegment.getDepDateTime())) {
				continue;
			}

			String eodsDepDateTime = eodsSearchSegment.getDepDateTime();
			try {
				// assume -1200 as time zone, if this not in the 3 days, that's
				// mean the flight depart time not in 3 days in any time zone
				if (BookingBuildUtil.checkFlightInFlownLimitTimeWithoutTz(EODSBookingSegment.TIME_FORMAT, flightFlownLimithours, eodsDepDateTime)) {
					return true;
				}
				// since there no time zone, so just filter out the flight
				// roughly,
				// e.g. system time - departure time <= flightFlownLimithours +
				// 24 hours
				/**
				 * if (System.currentTimeMillis() - pnrDate.getTime() <=
				 * (flightFlownLimithours + 24) * 3600000) { return true; }
				 */
			} catch (ParseException e) {
				logger.error(String.format(
						"Parse segment departure time error, filghtNum:%s, departure time in pnr search response:%s, expect format:%s",
						eodsSearchSegment + eodsSearchSegment.getNumber(), eodsSearchSegment.getDepDateTime(),
						EODSBookingSegment.TIME_FORMAT));
			}
		}
		return false;
	}
  
	/*private boolean passengersValid(List<EODSBookingPassenger> passengers, LoginInfo loginInfo, String token) throws BusinessBaseException {
		
		List<String> nameTitleList = constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE,OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList());
		Map<String, String[]> passengersMap = new HashMap<>();
		for (int i= 0; i< passengers.size(); i++) {
			String[] nameArray = new String[] {passengers.get(i).getGivenName(),passengers.get(i).getFamilyName()};
			passengersMap.put(String.valueOf(i), nameArray);
		}
		
		//For AM/MPO member, conduct name matching against name in the member profile or travel doc
		if (MMBBizruleConstants.AM_MEMBER.equals(loginInfo.getUserType()) || MMBBizruleConstants.MPO_MEMBER.equals(loginInfo.getUserType())) {
			ProfilePersonInfo profilePersonInfo = retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), token);
			if (profilePersonInfo != null && memberProfileValid(profilePersonInfo, passengersMap, nameTitleList)) {
				return true;
			}
		}

		ProfilePreference profilePreference = retrieveProfileService.retrievePreference(loginInfo.getMemberId(), token);
		if (profilePreference == null) {
			logger.warn("No profile preference found for memberId: %s", loginInfo.getMemberId());
			return false;
		}
		for(ProfileTravelDoc profileTravelDoc : profilePreference.getPersonalTravelDocuments()) {
			if (profileTravelDoc != null && travelDocValid(profileTravelDoc, passengersMap, nameTitleList)) {
				return true;
			}
		}
		for(TravelCompanion travelCompanion : profilePreference.getTravelCompanions()) {
			if(travelCompanion != null && travelCompanion.getTravelDocument() != null && travelDocValid(travelCompanion.getTravelDocument(), passengersMap, nameTitleList)) {
				return true;
			}
			
		}
		logger.warn("No mane match for passengers");
		return false;
	}
	
	private boolean travelDocValid(ProfileTravelDoc profileTravelDoc, Map<String, String[]> passengersMap, List<String> nameTitleList) throws BusinessBaseException {
		List<String> result = NameIdentficationUtil.nameIdentification(profileTravelDoc.getFamilyName(), profileTravelDoc.getGivenName(), passengersMap, nameTitleList, shortCompareSize);
		return (result != null && result.size() == 1) ? true : false ;
	}
	
	
	private boolean memberProfileValid(ProfilePersonInfo profilePersonInfo, Map<String, String[]> passengersMap, List<String> nameTitleList) throws BusinessBaseException {
		List<String> result = NameIdentficationUtil.nameIdentification(profilePersonInfo.getFamilyName(), profilePersonInfo.getGivenName(), passengersMap, nameTitleList, shortCompareSize);
		return (result != null && result.size() == 1) ? true : false ;
	}*/
}
