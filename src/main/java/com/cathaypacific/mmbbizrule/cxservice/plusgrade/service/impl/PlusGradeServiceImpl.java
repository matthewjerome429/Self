package com.cathaypacific.mmbbizrule.cxservice.plusgrade.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mbcommon.utils.LanguageUtil;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileService;
import com.cathaypacific.mmbbizrule.cxservice.plusgrade.model.PlusgradeRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.plusgrade.model.PlusgradeRequestFlightDTO;
import com.cathaypacific.mmbbizrule.cxservice.plusgrade.model.PlusgradeRequestTicketDTO;
import com.cathaypacific.mmbbizrule.cxservice.plusgrade.model.PlusgradeResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.plusgrade.service.PlusGradeService;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrFFPInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;

@Service
public class PlusGradeServiceImpl implements PlusGradeService {
	
	private static final LogAgent LOGGER = LogAgent.getLogAgent(PlusGradeServiceImpl.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private RetrieveProfileService retrieveProfileService;
	
	@Value("${plusgrade.endPoint}")
	private String url;

	@Value("${plusgrade.headerAuthorization}")
	private String headerAuthorization;
	
	@Value("${plusgrade.olciapiKey}")
	private String olciapiKey;
	
	@Value("${plusgrade.apiKey}")
	private String apiKey;
	
	@Value("${plusgrade.marketPage}")
	private String marketPage;
	
	@Override
	public String getPlusGradeUrl(Booking booking,List<String> eligibleSegmentIds, LoginInfo loginInfo, String language, RetrievePnrBooking pnrBooking, boolean isOlci) {
		String plusGradeUrl = null;
		if(booking == null) {			
			return plusGradeUrl;
		}
		//get list of upgradeBidEligible segment IDs
		if(eligibleSegmentIds.isEmpty()) {			
			return plusGradeUrl;
		}
		
		//language
		language = LanguageUtil.getLanguageByName(language);
		
		try {
			LOGGER.debug("Calling plusgrade url: "+url);
			String key = isOlci ? olciapiKey : apiKey;
			HttpEntity<PlusgradeRequestDTO> entity = new HttpEntity<>(buildRequest(booking, loginInfo, language, eligibleSegmentIds, pnrBooking, key), buildHeaders());
			ResponseEntity<PlusgradeResponseDTO> response = restTemplate.postForEntity(url, entity, PlusgradeResponseDTO.class);		
			if(response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK) {
				PlusgradeResponseDTO plusgradeResponseDTO = response.getBody();
				if(plusgradeResponseDTO == null) {					
					return plusGradeUrl;
				}				
				if(plusgradeResponseDTO.getOfferUrl() != null) {					
					plusGradeUrl =plusgradeResponseDTO.getOfferUrl();
				} else  if(plusgradeResponseDTO.getModifyUrl() != null){					
					plusGradeUrl =plusgradeResponseDTO.getModifyUrl();
				}
			} else {
				LOGGER.warn("plusgrade exception: No url returned. Http status code: " + response.getStatusCode());
			}	
		} catch(HttpStatusCodeException exception) {
			LOGGER.error(String.format("plusgrade exception, statusCode: %s", exception.getStatusCode()), exception);
			if (exception.getStatusCode() != HttpStatus.NOT_FOUND) {
				return plusGradeUrl;
				// OLSS-6323 Remove default link
				// plusGradeUrl = marketPage + language;
			}
		} catch(Exception e) {
			// OLSS-6323 Remove default link
			// plusGradeUrl = marketPage + language;
			LOGGER.warn("plusgrade exception: ", e);
			return plusGradeUrl;
		}
		
		return plusGradeUrl;
	}

	/**
	 * @return HttpHeaders
	 */
	private HttpHeaders buildHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-applicationName", "MMB");
		headers.add("X-correlationId", MDC.get("req.sessionId") + ":" + MDC.get("req.traceId"));
		headers.add("Authorization", headerAuthorization);
		return headers;
	}

	/**
	 * @param booking
	 * @param loginInfo
	 * @param language
	 * @param eligibleSegmentIds
	 * @param pnrBooking 
	 * @return plusgradeRequestDTO
	 */
	private PlusgradeRequestDTO buildRequest(Booking booking, LoginInfo loginInfo, String language, List<String> eligibleSegmentIds, RetrievePnrBooking pnrBooking, String key) {
		PlusgradeRequestDTO plusgradeRequestDTO = new PlusgradeRequestDTO();
		plusgradeRequestDTO.setApiKey(key);
        plusgradeRequestDTO.setLanguage(language);
        plusgradeRequestDTO.setCurrency("XXX");
		plusgradeRequestDTO.setPnr(booking.getDisplayRloc());
		
		List<Passenger> passengers = booking.getPassengers();
		if(CollectionUtils.isNotEmpty(passengers)) {
			plusgradeRequestDTO.setPax(String.valueOf(passengers.size()));
			for(Passenger passenger : passengers) {
				if(OneAConstants.PASSENGER_TYPE_INF.equals(passenger.getPassengerType()) || OneAConstants.PASSENGER_TYPE_INS.equals(passenger.getPassengerType())) {
					plusgradeRequestDTO.setInfant(true);
					break;
				}
			}
			
			//set tickets for request
	        List<PlusgradeRequestTicketDTO> tickets = new ArrayList<>();
        	for (Passenger passenger : passengers) {                 	
        		PlusgradeRequestTicketDTO plusgradeRequestTicketDTO = new PlusgradeRequestTicketDTO();
        		plusgradeRequestTicketDTO.setFirstName(passenger.getGivenName());
        		plusgradeRequestTicketDTO.setLastName(passenger.getFamilyName());
        		plusgradeRequestTicketDTO.setPrimaryTraveller(passenger.isPrimaryPassenger());
        		tickets.add(plusgradeRequestTicketDTO);
       	 	}
        	if(CollectionUtils.isNotEmpty(tickets)) {
        		plusgradeRequestDTO.setTickets(tickets);
        	}			
		}
		
		String officeId = booking.getOfficeId();
		if (officeId != null && officeId.length() > 3) {
			plusgradeRequestDTO.setPointOfSale(officeId.substring(0, 3));
		}
		
		buildPrimaryPaxInfo(plusgradeRequestDTO, loginInfo, booking, pnrBooking);
		
        //set segment for request
        List<PlusgradeRequestFlightDTO> flights = new ArrayList<>();
        List<Segment> segments = booking.getSegments();
        for(String segmentId : eligibleSegmentIds) {
        	Segment segment = getSegmentById(segments, segmentId);
        	if(segment == null) {
        		continue;
        	}
        	
        	PlusgradeRequestFlightDTO plusgradeRequestFlightDTO = new PlusgradeRequestFlightDTO();
        	plusgradeRequestFlightDTO.setCarrierCode(segment.getMarketCompany());
        	plusgradeRequestFlightDTO.setDestination(segment.getDestPort());
        	plusgradeRequestFlightDTO.setOrigination(segment.getOriginPort());
        	plusgradeRequestFlightDTO.setFlightNumber(segment.getMarketSegmentNumber());
        	plusgradeRequestFlightDTO.setFareClass(segment.getSubClass());
        	
        	String departureDateTime = segment.getDepartureTime().getRtfsScheduledTime();
        	if(StringUtils.isNotEmpty(departureDateTime)) {
        		try {
        			plusgradeRequestFlightDTO.setDepartureDate(DateUtil.convertDateFormat(departureDateTime, DepartureArrivalTime.TIME_FORMAT, DateUtil.DATE_PATTERN_YYYY_MM_DD));
        			plusgradeRequestFlightDTO.setDepartureTime(DateUtil.convertDateFormat(departureDateTime, DepartureArrivalTime.TIME_FORMAT, DateUtil.TIME_PATTERN_HH_MM));
        		} catch(Exception e) {
        			LOGGER.warn(String.format("can't not convert departureDateTime:[%s] to [%s] or [%s]", departureDateTime, DateUtil.DATE_PATTERN_YYYY_MM_DD, DateUtil.TIME_PATTERN_HH_MM), e);
        		}
        	}
        	
        	flights.add(plusgradeRequestFlightDTO);
        }
        if(CollectionUtils.isNotEmpty(flights)) {
        	plusgradeRequestDTO.setFlights(flights);
        }
        
        return plusgradeRequestDTO;
	}
	
	/**
	 * build primary passenger information
	 * @param plusgradeRequestDTO
	 * @param loginInfo
	 * @param booking
	 * @param pnrBooking
	 */
	private void buildPrimaryPaxInfo(PlusgradeRequestDTO plusgradeRequestDTO, LoginInfo loginInfo, Booking booking, RetrievePnrBooking pnrBooking) {
		String loginType = loginInfo.getLoginType();
		List<Passenger> passengers = booking.getPassengers();
		
		List<RetrievePnrPassenger> pnrPassengers = pnrBooking.getPassengers();
		if(LoginInfo.LOGINTYPE_MEMBER.equals(loginType)) {
			RetrievePnrPassenger pax = getMemberLoginPax(pnrPassengers);
			if(pax == null) {
				String memberId = loginInfo.getMemberId();
				plusgradeRequestDTO.setLoyaltyNumber(memberId);
				//call memberProfile
				ProfilePersonInfo profilePersonInfo = retrieveProfileService.retrievePersonInfo(memberId, loginInfo.getMmbToken());
				if(profilePersonInfo != null) {
					plusgradeRequestDTO.setFirstName(profilePersonInfo.getGivenName());
					plusgradeRequestDTO.setLastName(profilePersonInfo.getFamilyName());
				}
			} else {
				plusgradeRequestDTO.setFirstName(pax.getGivenName());
				plusgradeRequestDTO.setLastName(pax.getFamilyName());
				buildContactInfo(plusgradeRequestDTO, passengers, pax.getPassengerID());
				buildFQTVInfo(plusgradeRequestDTO, pax);
			}
		} else {
			RetrievePnrPassenger pax = getNonMemberLoginPax(pnrPassengers);
			if(pax == null) {
				return;
			}
			plusgradeRequestDTO.setFirstName(pax.getGivenName());
			plusgradeRequestDTO.setLastName(pax.getFamilyName());
			buildContactInfo(plusgradeRequestDTO, passengers, pax.getPassengerID());
			buildFQTVInfo(plusgradeRequestDTO, pax);
		}
	}
	
	/**
	 * @param pnrPassengers
	 * @return
	 */
	private RetrievePnrPassenger getNonMemberLoginPax(List<RetrievePnrPassenger> pnrPassengers) {
		if(CollectionUtils.isEmpty(pnrPassengers)) {
			return null;
		}
		return pnrPassengers.stream().filter(pax -> pax != null && BooleanUtils.isTrue(pax.isPrimaryPassenger())).findFirst().orElse(null);
	}

	/**
	 * @param plusgradeRequestDTO
	 * @param passengers
	 * @param passengerID
	 */
	private void buildContactInfo(PlusgradeRequestDTO plusgradeRequestDTO, List<Passenger> passengers, String passengerID) {
		Passenger passenger = getPassengerById(passengers, passengerID);
		if(passenger == null || passenger.getContactInfo() == null) {
			return;
		}
		if(passenger.getContactInfo().getEmail() != null) {
			plusgradeRequestDTO.setEmail(passenger.getContactInfo().getEmail().getEmailAddress());			
		}
		if(passenger.getContactInfo().getPhoneInfo() != null) {
			plusgradeRequestDTO.setMobilePhone(passenger.getContactInfo().getPhoneInfo().getPhoneNo());
		}
	}

	/**
	 * @param plusgradeRequestDTO
	 * @param pax
	 */
	private void buildFQTVInfo(PlusgradeRequestDTO plusgradeRequestDTO, RetrievePnrPassenger pax) {
		List<RetrievePnrFFPInfo> fqtvs = pax.getFQTVInfos();
		if(CollectionUtils.isEmpty(fqtvs)) {
			return;
		}
		RetrievePnrFFPInfo fqtv = fqtvs.stream().filter(f -> f != null).findFirst().orElse(null);
		if(fqtv != null) {
			plusgradeRequestDTO.setLoyaltyCompany(fqtv.getFfpCompany());
			plusgradeRequestDTO.setLoyaltyNumber(fqtv.getFfpMembershipNumber());
			buildLoyaltyLevel(plusgradeRequestDTO, fqtv.getTierLevel());			
		}
	}

	/**
	 * @param plusgradeRequestDTO
	 * @param tierLevel
	 */
	private void buildLoyaltyLevel(PlusgradeRequestDTO plusgradeRequestDTO, String tierLevel) {
		String memberTier = "0";
		if("GR".equals(tierLevel)) {
			memberTier = "1";
		} else if("AM".equals(tierLevel)) {
			memberTier = "2";
		} else if("SL".equals(tierLevel)) {
			memberTier = "3";
		} else if("DM".equals(tierLevel) || "IN".equals(tierLevel)) {
			memberTier = "5";
		}
		plusgradeRequestDTO.setLoyaltyLevel(memberTier);
	}

	/**
	 * @param pnrPassengers
	 * @return
	 */
	private RetrievePnrPassenger getMemberLoginPax(List<RetrievePnrPassenger> pnrPassengers) {
		if(CollectionUtils.isEmpty(pnrPassengers)) {
			return null;
		}
		for(RetrievePnrPassenger pnrPassenger : pnrPassengers) {
			if(BooleanUtils.isTrue(pnrPassenger.isLoginMember())) {
				return pnrPassenger;
			}
		}
		return null;
	}
	
	/**
	 * @param passengers
	 * @param passengerId
	 * @return
	 */
	private Passenger getPassengerById(List<Passenger> passengers, String passengerId) {
		if(CollectionUtils.isEmpty(passengers) || StringUtils.isEmpty(passengerId)) {
			return null;
		}
		return passengers.stream().filter(pax -> pax != null && passengerId.equals(pax.getPassengerId())).findFirst().orElse(null);
	}

	/**
	 * @param segments
	 * @param segmentId
	 * @return
	 */
	private Segment getSegmentById(List<Segment> segments, String segmentId) {
		if (CollectionUtils.isEmpty(segments) || StringUtils.isEmpty(segmentId)) {
			return null;
		}
		return segments.stream().filter(seg -> seg != null && segmentId.equals(seg.getSegmentID())).findFirst().orElse(null);
	}

}
