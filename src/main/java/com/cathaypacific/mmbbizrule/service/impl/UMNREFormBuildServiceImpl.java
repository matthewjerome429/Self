package com.cathaypacific.mmbbizrule.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.config.BookingStatusConfig;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.mbcommonsvc.constant.ContactType;
import com.cathaypacific.mmbbizrule.cxservice.referencedata.timezone.service.AirportTimeZoneService;
import com.cathaypacific.mmbbizrule.db.dao.BookingStatusDAO;
import com.cathaypacific.mmbbizrule.db.dao.ConstantDataDAO;
import com.cathaypacific.mmbbizrule.db.model.BookingStatus;
import com.cathaypacific.mmbbizrule.db.model.ConstantData;
import com.cathaypacific.mmbbizrule.dto.common.booking.PhoneInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormAddressDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormGuardianInfoDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormJourneyDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormPassengerDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormSegmentDTO;
import com.cathaypacific.mmbbizrule.handler.BookingBuildHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.SegmentStatus;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormAddressRemark;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormGuardianInfoRemark;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormPortInfoRemark;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormRemark;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormSegmentRemark;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.model.AirFlightInfoBean;
import com.cathaypacific.mmbbizrule.oneaservice.airflightinfo.service.AirFlightInfoService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDepartureArrivalTime;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.util.FreeTextUtil;
import com.cathaypacific.mmbbizrule.service.UMNREFormBuildService;
import com.cathaypacific.mmbbizrule.service.UMNREFormRemarkService;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

@Service
public class UMNREFormBuildServiceImpl implements UMNREFormBuildService {
	
	@Autowired
	private UMNREFormRemarkService umnrEFormRemarkService;
	
	@Autowired
	private BookingStatusDAO bookingStatusDAO;
	
	@Autowired
	private BookingStatusConfig bookingStatusConfig;
	
	@Autowired
	private ConstantDataDAO constantDataDAO;
	
	@Autowired
	private BizRuleConfig bizRuleConfig;
	
	@Autowired
	private AEMService aemService;
	
	@Autowired
	private AirFlightInfoService airFlightInfoService;
	
	@Autowired
	private AirportTimeZoneService airportTimeZoneService;
	
	@Autowired
	private BookingBuildHelper bookingBuildHelper;
	
	@Value("${mmb.flight.passed.time}")
	private long flightPassedTime;
	
	private static final long UMNR_JOURNEY_SEGMENT_INTERVAL_IN_MILSEC = 18000000;	// 5 hours
	
	private static LogAgent logger = LogAgent.getLogAgent(UMNREFormBuildServiceImpl.class);

	/**
	 * Check if the specified passenger id has complete UMNR EForm remark
	 * @param paxId
	 * @param pnrBooking
	 * @return true if the pax has UMNR EForm remark
	 * @throws UnexpectedException
	 * @throws ParseException
	 */
	public boolean hasEFormRemark(String paxId, RetrievePnrBooking pnrBooking) {
		try {
			// Retrieve remarks as a list of models
			return umnrEFormRemarkService.buildUMNREFormRemark(pnrBooking).stream().anyMatch(
				rm -> StringUtils.equalsIgnoreCase(rm.getPassengerIdDigit(), paxId)
			);
			
			/** BASED ON R3.1 BUSINESS LOGIC - THERE IS NO NEED TO CHECK FULL JOURNEYS. COMMENT THIS FOR FUTURE REFERENCES.
			// RetrievePnrBooking may not contains required info which should be set when in need.
			setRequiredInfoToPnrSegment(pnrBooking);
			
			// Available booking status
			List<BookingStatus> bookingStatusList = bookingStatusDAO.findAvailableStatus(MMBConstants.APP_CODE);
			
			// Build passenger-segment mapping for journey
			List<RetrievePnrSegment> paxSegments = buildUMNRJourneySegmentsByPaxId(paxId, pnrBooking, bookingStatusList);
			
			// Build journey for the pax
			List<List<RetrievePnrSegment>> journeys = calUMNRJourneys(paxSegments);
			
			// Each remark should contain full journey's info. Try to find it.
			for (List<RetrievePnrSegment> journeySegments: journeys) {
				isRemarkFound = isJourneyInfoExistInRemark(journeySegments, umnrEFormRemark);
				
				// If any journey info not found, return false
				if (!isRemarkFound) {
					return false;
				}
			}
			**/
		} catch (Exception e) {
			logger.error("Error occured during checking whether UMNR remark exists for passenger id:" + paxId, e);
		}
		
		return false;
	}
	
	/**
	 * Check if journey info exists in UMNR EForm remark.
	 * @param journeySegments a list of segments
	 * @param umnrEFormRemark the remark which will be checked
	 * @return true if journey info exists in UMNR EForm remark.
	 */
	/*private boolean isJourneyInfoExistInRemark(List<RetrievePnrSegment> journeySegments, UMNREFormRemark umnrEFormRemark) {
		RetrievePnrSegment firstSegment = journeySegments.get(0);
		RetrievePnrSegment lastSegment = journeySegments.get(journeySegments.size() - 1);
		
		String firstFlightNumber = firstSegment.getPnrOperateCompany() + firstSegment.getPnrOperateSegmentNumber();
		String lastFlightNumber = lastSegment.getPnrOperateCompany() + lastSegment.getPnrOperateSegmentNumber();
		
		// Check if remark has the journey's info
		return 
			// The remark should has departure flight info
			umnrEFormRemark.getSegments().stream().anyMatch(
				segment ->StringUtils.equalsIgnoreCase(segment.getFlightNumber(), firstFlightNumber)
			) &&
			// The remark should has arrival flight info
			umnrEFormRemark.getSegments().stream().anyMatch(
				segment ->StringUtils.equalsIgnoreCase(segment.getFlightNumber(), lastFlightNumber)
			);
	}*/
	
	/**
	 * Build UMNREFormResponseDTO based on RetrievePnrBooking
	 * @return UMNREFormResponseDTO
	 * @throws ParseException 
	 */
	public UMNREFormResponseDTO buildUMNREForm(RetrievePnrBooking pnrBooking) throws ParseException {
		// RetrievePnrBooking may not contains required info which should be set when in need.
		setRequiredInfoToPnrSegment(pnrBooking);
		
		UMNREFormResponseDTO umnrEFormResponseDTO = new UMNREFormResponseDTO();
		
		umnrEFormResponseDTO.setOneARloc(pnrBooking.getOneARloc());
		umnrEFormResponseDTO.setOjRloc(pnrBooking.getSpnr());
		umnrEFormResponseDTO.setGdsRloc(pnrBooking.getGdsRloc());

		// Build passenger-segment mapping for journey
		Map<RetrievePnrPassenger, List<RetrievePnrSegment>> umnrPaxSegMap = buildUMNRPaxSegmentMapping(pnrBooking);
		
		// Build UMNR EForm segment DTO
		List<UMNREFormSegmentDTO> umnrEFormSegmentDTOs = buildUMNREFormSegments(umnrPaxSegMap);
		umnrEFormResponseDTO.setSegments(umnrEFormSegmentDTOs);
		
		// Retrieve remarks as a list of models
		List<UMNREFormRemark> umnrEFormRemarks = umnrEFormRemarkService.buildUMNREFormRemark(pnrBooking);
		
		// Build UMNR EForm passenger DTO
		umnrEFormResponseDTO.setPassengers(buildUMNREFormPassengers(umnrPaxSegMap, umnrEFormRemarks, pnrBooking));

		return umnrEFormResponseDTO;
	}
	
	/**
	 * Build pax-segment mapping
	 * @param pnrBooking
	 * @return
	 */
	private Map<RetrievePnrPassenger, List<RetrievePnrSegment>> buildUMNRPaxSegmentMapping(RetrievePnrBooking pnrBooking) {
		Map<RetrievePnrPassenger, List<RetrievePnrSegment>> umnrPaxSegMap = Maps.newHashMap();
		
		// Available booking status
		List<BookingStatus> bookingStatusList = bookingStatusDAO.findAvailableStatus(MMBConstants.APP_CODE);
		
		// Retrieve UMNR sector per pax
		for (RetrievePnrPassenger pnrPax: pnrBooking.getPassengers()) {
			List<RetrievePnrSegment> pnrSegments = buildUMNRJourneySegmentsByPaxId(pnrPax.getPassengerID(), pnrBooking, bookingStatusList);
			if (!pnrSegments.isEmpty()) {
				umnrPaxSegMap.put(pnrPax, pnrSegments);
			}
		}
		
		return umnrPaxSegMap;
	}
	
	/**
	 * Retrieve a list of eligible segment of a UMNR pax
	 * @param paxId
	 * @param pnrBooking
	 * @param bookingStatusList
	 * @return
	 * @throws UnexpectedException
	 */
	private List<RetrievePnrSegment> buildUMNRJourneySegmentsByPaxId(String paxId, RetrievePnrBooking pnrBooking, List<BookingStatus> bookingStatusList) {
		// Retrieve UMNR sector per pax
		return pnrBooking.getSegments().stream()
			.filter(pnrSegment -> pnrBooking.getSsrList().stream().anyMatch(
				ssr ->
					// the SSR is a UMNR SSR
					StringUtils.equalsIgnoreCase(OneAConstants.UNACCOMPANIED_MINOR_SSR_CODE_UMNR, ssr.getType())
					// the UMNR SSR belongs to the pax
					&& (StringUtils.isEmpty(ssr.getPassengerId()) || StringUtils.equalsIgnoreCase(ssr.getPassengerId(), paxId))
					// the UMNR SSR belongs to the segment
					&& (StringUtils.isEmpty(ssr.getSegmentId()) || StringUtils.equalsIgnoreCase(ssr.getSegmentId(), pnrSegment.getSegmentID()))
					// the segment is operated by CX/KA
					&& (StringUtils.equalsIgnoreCase(pnrSegment.getPnrOperateCompany(), MMBBizruleConstants.CX_OPERATOR) || StringUtils.equalsIgnoreCase(pnrSegment.getPnrOperateCompany(), MMBBizruleConstants.KA_OPERATOR))
					// the segment is not flown
					&& isSectorValid(pnrSegment, bookingStatusList)
			)
		).collect(Collectors.toList());
	}
	
	/**
	 * Build a list of UMNREFormPassengerDTO based on passenger-segment mapping and UMNR remarks
	 * @param umnrPaxSegMap
	 * @param umnrEFormRemarks
	 * @return List of UMNREFormPassengerDTO
	 * @throws ParseException 
	 */
	private List<UMNREFormPassengerDTO> buildUMNREFormPassengers(Map<RetrievePnrPassenger, List<RetrievePnrSegment>> umnrPaxSegMap,
			List<UMNREFormRemark> umnrEFormRemarks, RetrievePnrBooking pnrBooking) throws ParseException {
		List<UMNREFormPassengerDTO> umnreFormPassengerDTOs = new ArrayList<>();
		
		List<String> nameTitles = constantDataDAO.findByAppCodeAndType(MMBConstants.APP_CODE,OneAConstants.NAME_TITLE_TYPE_IN_DB).stream()
				.map(ConstantData::getValue).collect(Collectors.toList());
		
		/**
		 * Build UMNREFormPassengerDTO for each passengers based on umnrPaxSegMap,
		 * Loop by pnrBooking instead of looping map to keep PNR pax sequence
		 */
		for(RetrievePnrPassenger umnrPax: pnrBooking.getPassengers()) {
		    // mice will only return login pax
            if (!umnrPaxSegMap.containsKey(umnrPax) || (BookingBuildUtil.isMiceBooking(pnrBooking.getSkList())
                    && !BooleanUtils.isTrue(umnrPax.isPrimaryPassenger()))) {
                continue;
            }
			
			// Get the first available EForm remark of the passenger
			UMNREFormRemark umnrEFormRemark = umnrEFormRemarks.stream()
					.filter(remark -> StringUtils.equalsIgnoreCase(remark.getPassengerIdDigit(), umnrPax.getPassengerID())).findFirst().orElse(null);
			
			UMNREFormPassengerDTO umnrEFormPassengerDTO = new UMNREFormPassengerDTO();
			umnrEFormPassengerDTO.setPassengerId(umnrPax.getPassengerID());
			umnrEFormPassengerDTO.setFamilyName(umnrPax.getFamilyName());
			umnrEFormPassengerDTO.setAge(buildAge(pnrBooking, umnrPax, umnrEFormRemark));
			umnrEFormPassengerDTO.setPrimaryPassenger(BooleanUtils.isTrue(umnrPax.isPrimaryPassenger()));

			String title = BookingBuildUtil.retrievePassengerUpperCaseTitle(umnrPax.getGivenName(), nameTitles);
			umnrEFormPassengerDTO.setTitle(title);
			umnrEFormPassengerDTO.setGivenName(BookingBuildUtil.removeTitleFromGivenName(umnrPax.getGivenName(), title));
			umnrEFormPassengerDTO.setGender(buildGender(umnrEFormRemark, title));
			
			if(umnrEFormRemark != null) {
				umnrEFormPassengerDTO.setPermanentAddress(buildPermanentAddress(umnrEFormRemark.getAddress())); // UMNR address
				umnrEFormPassengerDTO.setParentInfo(buildParentInfo(umnrEFormRemark.getParentInfo())); // Parent info
			}
			
			umnrEFormPassengerDTO.setUmnrEFormJourneys(buildUMNREFormJourneys(pnrBooking, umnrPaxSegMap.get(umnrPax), umnrEFormRemark, umnrPax.getPassengerID()));	// Build journey for each pax
			umnrEFormPassengerDTO.setParentalLock(!umnrEFormPassengerDTO.isUMEFormRmExist() || BookingBuildUtil.parentalLocked(pnrBooking, umnrPax.getPassengerID()));
			umnreFormPassengerDTOs.add(umnrEFormPassengerDTO);
		}
		
		return umnreFormPassengerDTOs;
	}

	/**
	 * Build permanent address
	 * 
	 * @param addressRemark
	 * @return
	 */
	private UMNREFormAddressDTO buildPermanentAddress(UMNREFormAddressRemark addressRemark) {
		if(addressRemark == null) {
			return null;
		}
		UMNREFormAddressDTO addressDTO = new UMNREFormAddressDTO();
		addressDTO.setCity(addressRemark.getCity());
		addressDTO.setStreet(addressRemark.getStreet());
		addressDTO.setBuilding(addressRemark.getBuilding());
		addressDTO.setCountryCode(addressRemark.getCountryCode());
		addressDTO.setDefaultCountryName(aemService.getCountryNameByCountryCode(addressRemark.getCountryCode()));
		return addressDTO;
	}

	/**
	 * Retrieve gender
	 * 1. from booking
	 * 2. by title
	 * 
	 * @param umnrEFormRemark
	 * @param title
	 * @return
	 */
	private String buildGender(UMNREFormRemark umnrEFormRemark, String title) {
		String gender = Optional.ofNullable(umnrEFormRemark).map(UMNREFormRemark::getGender).orElse(null);
		if (StringUtils.isEmpty(gender)) {
			gender = BookingBuildUtil.retrieveGenderByTitle(title, bizRuleConfig.getMaleNameTitle(), bizRuleConfig.getFemaleNameTitle());
		}
		return gender;
	}

	/**
	 * Retrieve age from PNR first. Then from SSR.
	 * 
	 * @param pnrBooking
	 * @param umnrPax
	 * @param umnrEFormRemark
	 * @return
	 */
	private String buildAge(RetrievePnrBooking pnrBooking, RetrievePnrPassenger umnrPax, UMNREFormRemark umnrEFormRemark) {
		String age = Optional.ofNullable(umnrEFormRemark).map(UMNREFormRemark::getAge).orElse(null);
		if (StringUtils.isEmpty(age)) {
			age = BookingBuildUtil.retrieveUMNRAge(pnrBooking, umnrPax.getPassengerID());
		}
		try {
			// Remove leading zero
			age = (age != null) ? "" + Integer.parseInt(age) : null;	
		} catch (NumberFormatException e) {
			// If cannot parse the age, ignore and don't prefill it
			logger.warn(String.format("The age of UMNR remark of passenger id %s cannot be parsed: %s. The age will be ignored.",
					umnrPax.getPassengerID(), age), e);
			age = null;
		}
		return age;
	}
	
	private UMNREFormGuardianInfoDTO buildParentInfo(UMNREFormGuardianInfoRemark parentInfo) {
		UMNREFormGuardianInfoDTO dto = new UMNREFormGuardianInfoDTO();
		
		dto.setName(parentInfo.getName());
		dto.setRelationship(null);
		
		// Build phone
		String fullPhoneNumber = (parentInfo.getPhoneNumber() != null) ? parentInfo.getPhoneNumber().replaceAll("-", "") : null;
		PhoneInfoDTO phoneInfo = BizRulesUtil.parserPhoneNumber(fullPhoneNumber);
		if (bookingBuildHelper.isValidPhoneNumber(phoneInfo.getPhoneCountryNumber(), phoneInfo.getPhoneNo(), ContactType.EMR_CONTACT)) {
			dto.setCountryCode(phoneInfo.getCountryCode());
			dto.setPhoneCountryNumber(phoneInfo.getPhoneCountryNumber());
			dto.setPhoneNumber(phoneInfo.getPhoneNo());
		} else {
			dto.setPhoneNumber(phoneInfo.getPhoneCountryNumber() + phoneInfo.getPhoneNo());
		}

		// Build address
		UMNREFormAddressDTO address = new UMNREFormAddressDTO();
		dto.setAddress(address);
		if (parentInfo.getAddress() != null) {
			address.setCity(parentInfo.getAddress().getCity());
			address.setStreet(parentInfo.getAddress().getStreet());
			address.setBuilding(parentInfo.getAddress().getBuilding());
			address.setCountryCode(parentInfo.getAddress().getCountryCode());
		}
		address.setDefaultCountryName(
			aemService.getCountryNameByCountryCode(parentInfo.getAddress().getCountryCode())
		);
		
		return dto;
	}
	
	/**
	 * Build a list of UMNREFormJourneyDTO based on pax's valid segments (Operated by CX/KA AND Has UMNR SSR)
	 * @param pnrPassenger
	 * @param pnrSegments
	 * @param umnrEFormRemark
	 * @return List of UMNREFormJourneyDTO
	 * @throws ParseException 
	 */
	private List<UMNREFormJourneyDTO> buildUMNREFormJourneys(
			RetrievePnrBooking pnrBooking, List<RetrievePnrSegment> pnrSegments, UMNREFormRemark umnrEFormRemark, String paxId
		) throws ParseException {
		List<UMNREFormJourneyDTO> umnreFormJourneyDTOs = Lists.newArrayList();
		List<List<RetrievePnrSegment>> journeys = calUMNRJourneys(pnrSegments);
		
		for (int i = 0; i < journeys.size(); i++) {
			List<RetrievePnrSegment> journeySegments = journeys.get(i);
			// Build the Journey with specified list of current segments
			String journeyId = "" + (i + 1);
			umnreFormJourneyDTOs.add(buildUMNREFormJourney(pnrBooking, journeySegments, umnrEFormRemark, journeyId, paxId));
		}
		
		return umnreFormJourneyDTOs;
	}
	
	/**
	 * Calculate journeys by a list of PNR segments
	 * @param pnrPassenger
	 * @param pnrSegments
	 * @return a list of journeys which contain a list of segments
	 * @throws ParseException 
	 */
	private List<List<RetrievePnrSegment>> calUMNRJourneys(List<RetrievePnrSegment> pnrSegments) throws ParseException {
		List<List<RetrievePnrSegment>> journeys = Lists.newArrayList();
		
		List<RetrievePnrSegment> journeySegments = Lists.newArrayList();
		RetrievePnrSegment currPnrSegment = null;
		RetrievePnrSegment nextPnrSegment = null;
		for (int i = 0; i < pnrSegments.size(); i++) {
			currPnrSegment = pnrSegments.get(i);
			
			// Anyway the current one must be part of Journey
			if (!journeySegments.contains(currPnrSegment)) {
				journeySegments.add(currPnrSegment);
			}
			
			// Retrieve the next segment to compare
			if (pnrSegments.size() >= i + 1 + 1) {			// First 1 is convert to size; Second 1 is next one
				nextPnrSegment = pnrSegments.get(i + 1);
			} else {
				nextPnrSegment = null;
			}
			
			// Check if both segments belong to the same Journey
			if (nextPnrSegment != null && isWithinXMillisecondsInterval(currPnrSegment, nextPnrSegment, UMNR_JOURNEY_SEGMENT_INTERVAL_IN_MILSEC) && isConnectedFlight(currPnrSegment, nextPnrSegment)) {
				journeySegments.add(nextPnrSegment);
			} else {
				// If not, build the Journey with specified list of current segments
				journeys.add(journeySegments);

				// Handle the next segment
				journeySegments = Lists.newArrayList();
				if (nextPnrSegment != null) {
					journeySegments.add(nextPnrSegment);
				}
			}
		}
		
		return journeys;
	}
	
	/**
	 * Build UMNREFormJourneyDTO based on ONE journey's list of segments, remark and journey id
	 * @param journeySegments
	 * @param umnrEFormRemark
	 * @param journeyId
	 * @return UMNREFormJourneyDTO
	 */
	private UMNREFormJourneyDTO buildUMNREFormJourney(
			RetrievePnrBooking pnrBooking, List<RetrievePnrSegment> journeySegments, UMNREFormRemark umnrEFormRemark, String journeyId, String paxId
		) {
		UMNREFormJourneyDTO umnreFormJourneyDTO = new UMNREFormJourneyDTO();
		umnreFormJourneyDTO.setJourneyId(journeyId);
		
		for (int i = 0; i < journeySegments.size(); i++) {
			RetrievePnrSegment pnrSegment = journeySegments.get(i);
			umnreFormJourneyDTO.getSegmentIds().add(pnrSegment.getSegmentID());
			
			// Set guardian info of the FIRST segment in the journey
			if (i == 0) {
				umnreFormJourneyDTO.setPersonSeeingOffDeparture(buildGuardianInfo(pnrBooking, umnrEFormRemark, pnrSegment, pnrSegment.getOriginPort(), paxId));
			}
			
			// Set guardian info of the LAST segment in the journey
			if (i == journeySegments.size() - 1) {
				umnreFormJourneyDTO.setPersonMeetingArrival(buildGuardianInfo(pnrBooking, umnrEFormRemark, pnrSegment, pnrSegment.getDestPort(), paxId));
			}
		}
		
		return umnreFormJourneyDTO;
	}
	
	/**
	 * Build UMNREFormGuardianInfoDTO based on specified EForm remark and given segment and airport code
	 * @param umnrEFormRemarks
	 * @param pnrSegment
	 * @param airportCode
	 * @return UMNREFormGuardianInfoDTO
	 */
	private UMNREFormGuardianInfoDTO buildGuardianInfo(
			RetrievePnrBooking pnrBooking, UMNREFormRemark umnrEFormRemark, RetrievePnrSegment pnrSegment, String airportCode, String paxId
		) {
		String flightDate = DateUtil.convertDateFormat(
								pnrSegment.getDepartureTime().getPnrTime(),
								RetrievePnrDepartureArrivalTime.TIME_FORMAT,
								DateUtil.DATE_PATTERN_DDMMYYYY
							);
		String flightNumber = pnrSegment.getMarketCompany() + pnrSegment.getMarketSegmentNumber();
		
		// Retrieve the segment remark by flight number
		UMNREFormPortInfoRemark portInfoRemark = (umnrEFormRemark != null) ? retrievePortInfoRemark(umnrEFormRemark, flightNumber, flightDate, airportCode) : null;
		
		// Find OSI by port code
		RetrievePnrDataElements paxOsi = retrieveOSI(pnrBooking, pnrSegment, paxId, airportCode);
		
		// Retrieve name from remark. Then OSI.
		String name = (portInfoRemark != null) ? portInfoRemark.getGuardianInfo().getName() : null;
		if (name == null && paxOsi != null) {
			name = BookingBuildUtil.retrieveNameFromOSI(paxOsi);
		}
		
		// Retrieve relationship from remark.
		String relationship = (portInfoRemark != null) ? portInfoRemark.getGuardianInfo().getRelationship() : null;
		
		// Retrieve phone number from remark. Then OSI.
		String fullPhoneNumber = (portInfoRemark != null) ? portInfoRemark.getGuardianInfo().getPhoneNumber() : null;
		if (fullPhoneNumber == null && paxOsi != null) {
			fullPhoneNumber = BookingBuildUtil.retrievePhoneNumberFromOSI(paxOsi);
		}
		
		// Parse the full phone number
		String countryCode = null;
		String phoneCountryNumber = null;
		String phoneNumber = null;
		if (fullPhoneNumber != null) {
			fullPhoneNumber = fullPhoneNumber.replaceAll("-", "");
			PhoneInfoDTO phoneInfo = BizRulesUtil.parserPhoneNumber(fullPhoneNumber);
			countryCode = phoneInfo.getCountryCode();
			phoneCountryNumber = phoneInfo.getPhoneCountryNumber();
			phoneNumber = phoneInfo.getPhoneNo();
		}
		
		// Return null if empty
		if (countryCode == null && phoneNumber == null && portInfoRemark == null && name == null && relationship == null) {
			return null;
		}
		

		// Build the UMNREFormGuardianInfoDTO
		UMNREFormGuardianInfoDTO umnrEFormGuardianInfoDTO = new UMNREFormGuardianInfoDTO();
		if (bookingBuildHelper.isValidPhoneNumber(phoneCountryNumber, phoneNumber, ContactType.EMR_CONTACT)) {
			umnrEFormGuardianInfoDTO.setCountryCode(countryCode);
			umnrEFormGuardianInfoDTO.setPhoneCountryNumber(phoneCountryNumber);
			umnrEFormGuardianInfoDTO.setPhoneNumber(phoneNumber);
		} else {
			umnrEFormGuardianInfoDTO.setPhoneNumber(phoneCountryNumber + phoneNumber);
		}
		umnrEFormGuardianInfoDTO.setName(name);
		umnrEFormGuardianInfoDTO.setRelationship(relationship);
		if (portInfoRemark != null) {
			UMNREFormAddressDTO addressDTO = new UMNREFormAddressDTO();
			UMNREFormAddressRemark addressRemark = portInfoRemark.getGuardianInfo().getAddress();
			addressDTO.setBuilding(addressRemark.getBuilding());
			addressDTO.setStreet(addressRemark.getStreet());
			addressDTO.setCity(addressRemark.getCity());
			addressDTO.setCountryCode(addressRemark.getCountryCode());
			addressDTO.setDefaultCountryName(
				aemService.getCountryNameByCountryCode(addressRemark.getCountryCode())
			);
			umnrEFormGuardianInfoDTO.setAddress(addressDTO);
		}
		return umnrEFormGuardianInfoDTO;
	}
	
	/**
	 * Build a list of UMNREFormSegmentDTO based on all UMNR paxs' segments
	 * @param umnrPaxSegMap
	 * @return a list of UMNREFormSegmentDTO
	 */
	private List<UMNREFormSegmentDTO> buildUMNREFormSegments(Map<RetrievePnrPassenger, List<RetrievePnrSegment>> umnrPaxSegMap) {
		List<UMNREFormSegmentDTO> umnreFormSegmentDTOs = Lists.newArrayList();
		
		// Convert each RetrievePnrSegment to UMNREFormSegmentDTO
		for(Entry<RetrievePnrPassenger, List<RetrievePnrSegment>> entry: umnrPaxSegMap.entrySet()) {
			List<RetrievePnrSegment> pnrSegments = entry.getValue();
			
			for (RetrievePnrSegment pnrSegment: pnrSegments) {
				if (umnreFormSegmentDTOs.stream().noneMatch(umnreFormSegmentDTO -> StringUtils.equalsIgnoreCase(umnreFormSegmentDTO.getSegmentId(), pnrSegment.getSegmentID()))) {
					UMNREFormSegmentDTO umnreFormSegmentDTO = new UMNREFormSegmentDTO();
					umnreFormSegmentDTO.setSegmentId(pnrSegment.getSegmentID());
					umnreFormSegmentDTO.setArrivalTime(pnrSegment.getArrivalTime().getPnrTime());
					umnreFormSegmentDTO.setDepartureTime(pnrSegment.getDepartureTime().getPnrTime());
					umnreFormSegmentDTO.setOriginPort(pnrSegment.getOriginPort());
					umnreFormSegmentDTO.setDestPort(pnrSegment.getDestPort());
					umnreFormSegmentDTO.setOperatingCompany(pnrSegment.getPnrOperateCompany());
					umnreFormSegmentDTO.setOperatingSegmentNumber(pnrSegment.getPnrOperateSegmentNumber());
					umnreFormSegmentDTO.setMarketingCompany(pnrSegment.getMarketCompany());
					umnreFormSegmentDTO.setMarketingSegmentNumber(pnrSegment.getMarketSegmentNumber());
					umnreFormSegmentDTOs.add(umnreFormSegmentDTO);
				}
			}
	    }
		return umnreFormSegmentDTOs;
	}
	
	/**
	 * Check if the interval difference of two air-sectors are smaller than the given default interval
	 * @param currPnrSegment
	 * @param nextPnrSegment
	 * @param millisecondsInterval
	 * @return true if the interval difference of two air-sectors are smaller than the given default interval
	 * @throws ParseException
	 */
	private boolean isWithinXMillisecondsInterval(RetrievePnrSegment currPnrSegment, RetrievePnrSegment nextPnrSegment, long millisecondsInterval) throws ParseException {
		Date arrivalDate = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT, currPnrSegment.getArrivalTime().getPnrTime());
		Date departureDate = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT, nextPnrSegment.getDepartureTime().getPnrTime());
		
		// milliseconds difference
		long diffInMilsec = departureDate.getTime() - arrivalDate.getTime();
		
		return (diffInMilsec <= millisecondsInterval);
	}
	
	/**
	 * Check if the given two air-sectors are connected flight
	 * @param currPnrSegment
	 * @param nextPnrSegment
	 * @return true if the given two air-sectors are connected flight
	 */
	private boolean isConnectedFlight(RetrievePnrSegment currPnrSegment, RetrievePnrSegment nextPnrSegment) {
		return StringUtils.equalsIgnoreCase(currPnrSegment.getDestPort(), nextPnrSegment.getOriginPort());
	}
	
	/**
	 * Check if the segment is valid
	 * @param pnrSegment
	 * @param bookingStatusList
	 * @return true if the segment is valid
	 * @throws UnexpectedException
	 */
	private boolean isSectorValid(RetrievePnrSegment pnrSegment, List<BookingStatus> bookingStatusList) {
		SegmentStatus segmentStatus = null;
		try {
			segmentStatus = BookingBuildUtil.generateFlightStatus(
				BookingBuildUtil.buildDepartArrivalTime(pnrSegment.getDepartureTime()),
				pnrSegment.getStatus(),
				bookingStatusList,
				bookingStatusConfig,
				flightPassedTime
			);
		} catch (Exception e) {
			logger.error("Error occured when checking isSectorValid for UMNR segments", e);
			return false;
		}
		
		return !segmentStatus.isFlown() &&
				!segmentStatus.isDisable() &&
				segmentStatus.getStatus() != FlightStatusEnum.CANCELLED &&
				segmentStatus.getStatus() != FlightStatusEnum.WAITLISTED;
	}
	
	/**
	 * Retrieve UMNR OSI based on pax id and airport code
	 * @param pnrBooking
	 * @param paxId
	 * @param airportCode
	 * @return RetrievePnrDataElements OSI
	 */
	private RetrievePnrDataElements retrieveOSI(RetrievePnrBooking pnrBooking, RetrievePnrSegment pnrSegment, String paxId, String airportCode) {
		if (pnrBooking.getOsiList() == null) {
			return null;
		}
		
		// Find OSI by port code first
		String cityCode = aemService.getCityCodeByPortCode(airportCode);
		List<RetrievePnrDataElements> osis = pnrBooking.getOsiList().stream().filter(osi -> {
				String osiFreeText = null;
				if (osi.getOtherDataList() != null && !osi.getOtherDataList().isEmpty()) {
					osiFreeText = osi.getOtherDataList().get(0).getFreeText();
				}
				String portCode = FreeTextUtil.parsePortCodeFromOSIUMNRFreeText(osiFreeText);
				return 	StringUtils.equalsIgnoreCase(portCode, airportCode) ||
						StringUtils.equalsIgnoreCase(portCode, cityCode);
			}
		).collect(Collectors.toList());
		
		// Find OSI based on pax id (also include no pax association)
		List<RetrievePnrDataElements> paxOsis = osis.stream().filter(
			osi -> StringUtils.equalsIgnoreCase(osi.getPassengerId(), paxId) || StringUtils.isEmpty(osi.getPassengerId())
		).collect(Collectors.toList());
		
		if (paxOsis.size() == 1) {
			// If only one osi, use it for all segments
			return paxOsis.get(0);
		} else {
			// If more than one osi, follow segment order in PNR
			int osiIndex = this.findOsiIndexBySegmentAndPortCode(pnrBooking.getSegments(), pnrSegment, airportCode);
			if (osiIndex != -1 && paxOsis.size() - 1 >= osiIndex) {
				return paxOsis.get(osiIndex);
			}
		}
		
		return null;
	}
	
	/** 
	 * This logic is to retrieve the expected osi position
	 * @param pnrSegments
	 * @param targetSegment
	 * @param airportCode
	 * @return
	 */
	private int findOsiIndexBySegmentAndPortCode(List<RetrievePnrSegment> pnrSegments, RetrievePnrSegment targetSegment, String airportCode) {
		int osiIndex = -1;
		
		for(RetrievePnrSegment pnrSegment: pnrSegments) {
			if (StringUtils.equals(pnrSegment.getDestPort(), airportCode) ||
				StringUtils.equals(pnrSegment.getOriginPort(), airportCode)) {
				osiIndex++;
			}
			if (StringUtils.equals(pnrSegment.getSegmentID(), targetSegment.getSegmentID())) {
				break;
			}
		}
		
		return osiIndex;
	}
	
	/**
	 * @param umnrEFormRemark
	 * @param flightNumber 
	 * @param flightDate ONLY accept DDMMYYYY format
	 * @param airportCode
	 * @return
	 */
	private UMNREFormPortInfoRemark retrievePortInfoRemark(UMNREFormRemark umnrEFormRemark, String flightNumber, String flightDate, String airportCode) {
		if (umnrEFormRemark.getSegments() == null) {
			return null;
		}
		
		return umnrEFormRemark.getSegments().stream().filter(
			// Filter segment remark by flight number and flight date
			segRm -> 
				StringUtils.equalsIgnoreCase(segRm.getFlightNumber(), flightNumber) &&
				StringUtils.equalsIgnoreCase(segRm.getFlightDate(), flightDate)
		).findFirst().orElse(new UMNREFormSegmentRemark()).getPortInfo().stream().filter(
			// Filter port info by airport code	
			portInfo -> StringUtils.equalsIgnoreCase(portInfo.getAirportCode(), airportCode)
		).findFirst().orElse(null);
	}
	
	/**
	 * Pre-process required before executing this service to build the DTO. Only execute if the data is missing.
	 * @param pnrBooking
	 */
	private void setRequiredInfoToPnrSegment(RetrievePnrBooking pnrBooking) {
		pnrBooking.getSegments().forEach(pnrSegment -> {
			// Set operator
			if (StringUtils.isEmpty(pnrSegment.getPnrOperateCompany())) {
				AirFlightInfoBean airFlightInfoBean = airFlightInfoService.getAirFlightInfo(
					pnrSegment.getDepartureTime().getTime(), pnrSegment.getOriginPort(),
					pnrSegment.getDestPort(), pnrSegment.getMarketCompany(),
					pnrSegment.getMarketSegmentNumber(), pnrSegment.getAirCraftType());
				BookingBuildUtil.setOperateByCompanyAndFlightNumber(pnrSegment, airFlightInfoBean);
			}
			
			// Set timezone offset
			if (StringUtils.isEmpty(pnrSegment.getDepartureTime().getTimeZoneOffset())) {
				String originPortOffset = airportTimeZoneService.getAirPortTimeZoneOffset(pnrSegment.getOriginPort());
				if (StringUtils.isEmpty(originPortOffset)) {
					logger.warn(String.format("Cannot find available timezone for originPort:%s", pnrSegment.getOriginPort()));
				}
				pnrSegment.getDepartureTime().setTimeZoneOffset(originPortOffset);
			}
			if (StringUtils.isEmpty(pnrSegment.getArrivalTime().getTimeZoneOffset())) {
				String destPortOffset = airportTimeZoneService.getAirPortTimeZoneOffset(pnrSegment.getDestPort());
				if (StringUtils.isEmpty(destPortOffset)) {
					logger.warn(String.format("Cannot find available timezone for destPort:%s", pnrSegment.getDestPort()));
				}
				pnrSegment.getArrivalTime().setTimeZoneOffset(destPortOffset);
			}
		});
	}

}
