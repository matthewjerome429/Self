package com.cathaypacific.mmbbizrule.business.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.common.AdcMessage;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mbcommunication.model.request.adcemail.AdcErrorMessageEmailRequestDTO;
import com.cathaypacific.mbcommunication.webservice.service.client.AdcErrorMessageEmailClient;
import com.cathaypacific.mmbbizrule.aem.model.City;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.business.AdcErrorMessageEmailService;
import com.cathaypacific.mmbbizrule.constant.CommonConstants;
import com.cathaypacific.mmbbizrule.constant.TicketReminderConstant;
import com.cathaypacific.mmbbizrule.dto.request.checkin.accept.CheckInAcceptRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.email.ContactTypeEnum;
import com.cathaypacific.mmbbizrule.dto.request.email.FFPInfoDTO;
import com.cathaypacific.mmbbizrule.dto.request.email.FIFBelowRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.email.PaxContactDTO;
import com.cathaypacific.mmbbizrule.dto.request.email.PaxInfo;
import com.cathaypacific.mmbbizrule.dto.request.email.PaxSegmentDTO;
import com.cathaypacific.mmbbizrule.dto.request.email.RemarkDTO;
import com.cathaypacific.mmbbizrule.dto.request.email.RemarkTypeEnum;
import com.cathaypacific.mmbbizrule.dto.request.email.SegmentDTO;
import com.cathaypacific.mmbbizrule.dto.request.email.TemplateTypeEnum;
import com.cathaypacific.mmbbizrule.dto.response.checkin.accept.CheckInAcceptPassengerDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.accept.CheckInAcceptPassengerSegmentDTO;
import com.cathaypacific.mmbbizrule.dto.response.checkin.accept.CheckInAcceptResponseDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.ContactInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.FQTVInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.util.EmailUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

@Service
public class AdcErrorMessageEmailServiceImpl implements AdcErrorMessageEmailService {
	
	private static LogAgent logger = LogAgent.getLogAgent(AdcErrorMessageEmailServiceImpl.class);
	
	@Autowired
	private AdcErrorMessageEmailClient adcErrorMessageEmailClient;
	
	@Autowired
	private AEMService aemService;
	
	@Override
	public void sendEmail(List<AdcMessage> adcMessages, CheckInAcceptResponseDTO checkInAcceptResponse,
			CheckInAcceptRequestDTO requestDTO, Booking bookingMergedCpr) {
		try {
			List<FIFBelowRequestDTO> fifBelowRequests = buildFIFBelowRequests(adcMessages, checkInAcceptResponse, requestDTO, bookingMergedCpr);
			if (CollectionUtils.isNotEmpty(fifBelowRequests)) {
				for (FIFBelowRequestDTO fifBelowRequest : fifBelowRequests) {
					adcErrorMessageEmailClient.sendEmail(buildAdcEmailRequest(fifBelowRequest));
				}
			}
			logger.info(String.format("ADC Message | Sent Out | RLOC | %s | Journey | %s", requestDTO.getRloc(), requestDTO.getJourneyId()), true);
		} catch (Exception e) {
			logger.error(String.format("Send ADC Error Message failed. by rloc: %s.", requestDTO.getRloc()), e);
		}
	}
	
	/**
	 * 
	 * @param adcMessages
	 * @param checkInAcceptResponse 
	 * @param requestDTO
	 * @param bookingMergedCpr
	 * @return
	 * @throws ExpectedException 
	 */
	private List<FIFBelowRequestDTO> buildFIFBelowRequests(List<AdcMessage> adcMessages, CheckInAcceptResponseDTO checkInAcceptResponse, CheckInAcceptRequestDTO requestDTO,
			Booking bookingMergedCpr) throws ExpectedException {
		List<FIFBelowRequestDTO> fifBelowRequests = new ArrayList<> ();
		if (null == checkInAcceptResponse || null == checkInAcceptResponse.getCprJourney()) {
			logger.warn(String.format("Cannot send ADC Error Message -> not allow sent ADC Error Message by rloc: %s.", requestDTO.getRloc()));
			return fifBelowRequests;
		}

		// get check in request passenger list
		List<Passenger> requiredBPPaxList = getRequiredAdcPaxList(checkInAcceptResponse, requestDTO, bookingMergedCpr);
		List<Segment> requiredSegments = getRequiredSegments(requiredBPPaxList, checkInAcceptResponse, bookingMergedCpr);
		boolean hasAnyContact = requiredBPPaxList.stream().anyMatch(this::checkHasContactInfo);
		if (!hasAnyContact) {
			logger.warn(String.format("Cannot send ADC Error Message -> all passengers no contact info by rloc: %s.", requestDTO.getRloc()));
			return fifBelowRequests;
		}
		List<String> paxIds = adcMessages.stream().map(AdcMessage::getPassengerId).distinct().collect(Collectors.toList());
		for(String paxId : paxIds) {
			Passenger passenger = requiredBPPaxList.stream().filter(pax -> Objects.equals(paxId, pax.getPassengerId())).findFirst().orElse(null);
			List<AdcMessage> msgs = adcMessages.stream().filter(adcMsg -> Objects.equals(paxId, adcMsg.getPassengerId())).collect(Collectors.toList());
			if(passenger == null || CollectionUtils.isEmpty(msgs)) {
				continue;
			}
			List<String> adcSegIds = msgs.stream().map(AdcMessage::getSegmentId).collect(Collectors.toList());
			List<Segment> segments = requiredSegments.stream().filter(seg -> adcSegIds.contains(seg.getSegmentID())).collect(Collectors.toList());
			FIFBelowRequestDTO fif = new FIFBelowRequestDTO();
			fif.setLanguageCode(getLanguageCode(MMBUtil.getCurrentAcceptLanguage()));
			Locale locale = new Locale(fif.getLanguageCode());
			// Rloc
			fif.setRloc(requestDTO.getRloc());
			fif.setTemplateType(TemplateTypeEnum.EMAIL_TEMPLATE_TYPE_CANCEL_BOOKING);
			// add paxContacts
			fif.setPaxContacts(Lists.newArrayList());
			
			if (passenger.getContactInfo() != null && passenger.getContactInfo().getEmail() != null) {
				PaxContactDTO recipientDTO = new PaxContactDTO();
				recipientDTO.setContactType(ContactTypeEnum.CONTACT_TYPE_EMAIL);
				recipientDTO.setContactDetail(passenger.getContactInfo().getEmail().getEmailAddress());
				fif.getPaxContacts().add(recipientDTO);
			} else {
				if (!StringUtils.isEmpty(passenger.getParentId())) {
					Passenger parentPax = bookingMergedCpr.getPassengers().stream().filter(pax -> Objects.equals(passenger.getParentId(), pax.getPassengerId())).findFirst().orElse(null);
					if (parentPax != null && parentPax.getContactInfo() != null && parentPax.getContactInfo().getEmail() != null) {
						PaxContactDTO recipientDTO = new PaxContactDTO();
						recipientDTO.setContactType(ContactTypeEnum.CONTACT_TYPE_EMAIL);
						recipientDTO.setContactDetail(parentPax.getContactInfo().getEmail().getEmailAddress());
						fif.getPaxContacts().add(recipientDTO);
					}
				}
			}
			
			// add remarks
			RemarkDTO remark = new RemarkDTO();
			remark.setRemark(CommonConstants.EMAIL_REMARK_OLCI_ADC_ERROR);
			remark.setType(RemarkTypeEnum.REMARK_TYPE_NOTIFICATION);
			fif.findRemarks().add(remark);
			// add segments
			Map<String, List<AdcMessage>> map = new HashMap<>();
			addSegments(fif, segments, msgs, map, locale);
			// add paxInfos
			addPaxInfos(fif, passenger, bookingMergedCpr);
			// add paxSegments
			addPaxSegments(fif, map);
			
			fifBelowRequests.add(fif);
		}
		return fifBelowRequests;
	}
	
	/**
	 * add Pax Segments
	 * @param request
	 * @param map
	 */
	private void addPaxSegments(FIFBelowRequestDTO request, Map<String, List<AdcMessage>> map) {
		if(CollectionUtils.isEmpty(request.getSegments()) || CollectionUtils.isEmpty(request.getPaxInfos())){
			return;
		}
		
		for (SegmentDTO segmentDTO : request.getSegments()) {
			List<AdcMessage> adcMsgs = map.get(segmentDTO.getSegmentSeq());
			if(CollectionUtils.isEmpty(adcMsgs)) {
				continue;
			}
			PaxSegmentDTO  paxSegment = new PaxSegmentDTO();
			for(AdcMessage msg : adcMsgs) {
				RemarkDTO remark = new RemarkDTO();
				remark.setType(RemarkTypeEnum.REMARK_TYPE_ADCMSG);
				remark.setRemark(msg.getAdcCprMessage());
				paxSegment.findRemarks().add(remark);
			}
			paxSegment.setPaxSeq(String.valueOf(1));
			paxSegment.setSegmentSeq(segmentDTO.getSegmentSeq());
			request.findPaxSegments().add(paxSegment);
		}
	}
	
	/**
	 * add Pax Infos
	 * @param request
	 * @param passenger
	 * @param bookingMergedCpr
	 */
	private void addPaxInfos(FIFBelowRequestDTO request, Passenger passenger, Booking bookingMergedCpr) {
		PaxInfo paxDto = new PaxInfo();
		paxDto.setPaxSeq(String.valueOf(1));
		paxDto.setFirstName(passenger.getGivenName());
		paxDto.setLastName(passenger.getFamilyName());
		paxDto.setTitle(passenger.getTitle());
		List<FQTVInfo> fqtvInfos = getFQTVsByPaxId(passenger.getPassengerId(), bookingMergedCpr);
		FQTVInfo fqtvInfo = fqtvInfos.stream().filter(info -> info != null 
				&& TicketReminderConstant.OPERATE_CX.equals(info.getCompanyId())).findFirst().orElse(null);
		if(fqtvInfo != null && !StringUtils.isEmpty(fqtvInfo.getMembershipNumber())){
			FFPInfoDTO ffpInfo =new FFPInfoDTO();
			ffpInfo.setMemberTier(fqtvInfo.getTierLevel());
			ffpInfo.setMemberNo(fqtvInfo.getMembershipNumber());
			paxDto.setFfpInfo(ffpInfo);
		}
		request.findPaxInfos().add(paxDto);
	}
	
	/**
	 * get list of FQTV by passenger ID
	 * 
	 * @param passengerId
	 * @param booking
	 * @return
	 */
	private List<FQTVInfo> getFQTVsByPaxId(String passengerId, Booking booking) {
		if(booking == null || CollectionUtils.isEmpty(booking.getPassengerSegments()) || StringUtils.isEmpty(passengerId)) {
			return Collections.emptyList();
		}
		return booking.getPassengerSegments().stream().filter(ps -> ps != null && StringUtils.equalsIgnoreCase(ps.getPassengerId(), passengerId)
				&& ps.getFqtvInfo() != null && !StringUtils.isEmpty(ps.getFqtvInfo().getMembershipNumber()))
				.map(PassengerSegment::getFqtvInfo).collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @param request
	 * @param segments
	 * @param map 
	 * @param msgs 
	 * @param locale 
	 */
	private void addSegments(FIFBelowRequestDTO request,List<Segment> segments, List<AdcMessage> msgs, Map<String, List<AdcMessage>> map, Locale locale) {
		if(CollectionUtils.isEmpty(segments)) {
			return;
		}
		
		int seq = 1;
		for (Segment segment : segments) {
			SegmentDTO segmentDTO = new SegmentDTO();
			// add adcMessage map
			List<AdcMessage> segMsgs = msgs.stream().filter(msg -> Objects.equals(segment.getSegmentID(), msg.getSegmentId())).collect(Collectors.toList());
			map.put(String.valueOf(seq), segMsgs);
			// STD time
			String arrivalTime = "";
			String departureTime = "";
			if (segment.getArrivalTime() != null) {
				arrivalTime = DateUtil.convertDateFormat(segment.getArrivalTime().getPnrTime(),
						DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, DateUtil.DATETIME_FORMAT_XML);
			}

			if (segment.getDepartureTime() != null) {
				departureTime = DateUtil.convertDateFormat(segment.getDepartureTime().getPnrTime(),
						DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, DateUtil.DATETIME_FORMAT_XML);
			}
			segmentDTO.setSegmentSeq(String.valueOf(seq));
			segmentDTO.setFlightNo(segment.getOperateSegmentNumber());
			segmentDTO.setArrivalDate(arrivalTime);
			segmentDTO.setDepartureDate(departureTime);
			segmentDTO.setCabinClass(segment.getCabinClass());
			if (!StringUtils.isEmpty(segment.getOriginalSubClass())) {
				segmentDTO.setBookingClass(segment.getOriginalSubClass());
			} else {
				segmentDTO.setBookingClass(segment.getSubClass());
			}

			segmentDTO.setCarrierCode(
					StringUtils.isEmpty(segment.getOperateCompany()) ? segment.getMarketCompany() : segment.getOperateCompany());
			segmentDTO.setFlightNo(StringUtils.isEmpty(segment.getOperateSegmentNumber())
					? segment.getMarketSegmentNumber()
					: segment.getOperateSegmentNumber());

			segmentDTO.setArrivalAirport(findCityNameByAirportCode(segment.getDestPort(), locale));
			segmentDTO.setDepartAirport(findCityNameByAirportCode(segment.getOriginPort(), locale));
			request.findSegments().add(segmentDTO);
			seq++;
		}
	}
	
	/**
	 * find CityName By AirportCode
	 * @param airportCode
	 * @return
	 */
	private String findCityNameByAirportCode(String airportCode, Locale locale) {
		City city = aemService.retrieveCityByAirportCode(airportCode, locale);
		if(city != null) {
			if(!StringUtils.isEmpty(city.getName())) {
				return city.getName();
			} else if (!StringUtils.isEmpty(city.getDefaultName())){
				return city.getDefaultName();
			}
		}
		return airportCode;
	}
	
	/**
	 * build ADC required passengerList
	 *
	 * @param journey
	 * @param requestDTO
	 * @return
	 * @throws ExpectedException
	 */
	private List<Passenger> getRequiredAdcPaxList(CheckInAcceptResponseDTO checkInAcceptResponse,
			CheckInAcceptRequestDTO requestDTO, Booking bookingMergedCpr) throws ExpectedException {
		List<CheckInAcceptPassengerDTO> journeyPassengers = checkInAcceptResponse.getCprJourney().getPassengers();
		if (journeyPassengers == null || journeyPassengers.isEmpty()) {
			throw new ExpectedException(String.format("no passenger in journey -> journey id[%s].",
					checkInAcceptResponse.getCprJourney().getJourneyId()), new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}
		
		List<Passenger> passengers= bookingMergedCpr.getPassengers();
		// get uniqueCustomers from requestDTO base on cprJourney
		List<Passenger> resultPaxList = new ArrayList<>();
		for (CheckInAcceptPassengerDTO journeyPassenger : journeyPassengers) {
			Passenger passenger = passengers.stream().filter(pax -> pax.getPassengerId().equals(journeyPassenger.getPassengerId())
					&& requestDTO.getPassengerIds().contains(pax.getPassengerId())).findFirst().orElse(null);
			Passenger infantPax = passengers.stream().filter(pax -> pax.getPassengerId().equals(journeyPassenger.getPassengerId())
					&& requestDTO.getPassengerIds().contains(pax.getParentId())).findFirst().orElse(null);
			if(null != passenger){
				resultPaxList.add(passenger);
			}
			if(null != infantPax){
				resultPaxList.add(infantPax);
			}
		}
		
		return resultPaxList;
	}
	
	/**
	 * get Required Segments
	 * @param requiredBPPaxList
	 * @param checkInAcceptResponse
	 * @param bookingMergedCpr
	 * @return
	 */
	private List<Segment> getRequiredSegments(List<Passenger> requiredBPPaxList,
			CheckInAcceptResponseDTO checkInAcceptResponse, Booking bookingMergedCpr) {
		if(checkInAcceptResponse.getCprJourney() != null && CollectionUtils.isNotEmpty(checkInAcceptResponse.getCprJourney().getPassengerSegments())
				&& CollectionUtils.isNotEmpty(requiredBPPaxList) && CollectionUtils.isNotEmpty(bookingMergedCpr.getSegments())) {
			List<String> paxIdList = requiredBPPaxList.stream().map(Passenger::getPassengerId).collect(Collectors.toList());
			List<String> segIdList = checkInAcceptResponse.getCprJourney().getPassengerSegments().stream()
			.filter(ps -> paxIdList.contains(ps.getPassengerId())).map(CheckInAcceptPassengerSegmentDTO::getSegmentId).distinct().collect(Collectors.toList());
			return bookingMergedCpr.getSegments().stream().filter(seg -> segIdList.contains(seg.getSegmentID())).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}
	
	/**
	 * check whether has contact info
	 * 
	 * @param passengers
	 * @return
	 */
	private boolean checkHasContactInfo(Passenger passenger) {
		if (null == passenger || null == passenger.getContactInfo() || null == passenger.getContactInfo().getEmail()
				|| null == passenger.getContactInfo().getPhoneInfo()) {
			return false;
		}

		ContactInfo contact = passenger.getContactInfo();

		return !StringUtils.isEmpty(contact.getEmail().getEmailAddress())
				&& !StringUtils.isEmpty(contact.getPhoneInfo().getPhoneCountryNumber())
				&& !StringUtils.isEmpty(contact.getPhoneInfo().getPhoneNo());
	}
	
	private AdcErrorMessageEmailRequestDTO buildAdcEmailRequest(FIFBelowRequestDTO fifBelowRequest) {
		AdcErrorMessageEmailRequestDTO requestDTO = new AdcErrorMessageEmailRequestDTO();
		requestDTO.setRloc(fifBelowRequest.getRloc());
		requestDTO.setLanguageCode(fifBelowRequest.getLanguageCode());
		requestDTO.setPaxContact(buildPaxContact(fifBelowRequest.getPaxContacts()));
		requestDTO.setPaxInfo(buildPaxInfo(fifBelowRequest.getPaxInfos()));
		requestDTO.setSegments(buildPaxContact(fifBelowRequest.getPaxSegments(), fifBelowRequest.getSegments()));
		logger.debug("request json:"+new Gson().toJson(requestDTO));
		return requestDTO;
	}
	
	private List<com.cathaypacific.mbcommunication.model.request.adcemail.SegmentDTO> buildPaxContact(
			List<PaxSegmentDTO> paxSegments, List<SegmentDTO> segments) {
		List<com.cathaypacific.mbcommunication.model.request.adcemail.SegmentDTO> segs = new ArrayList<> ();
	    for(SegmentDTO segment : segments) {
	    	com.cathaypacific.mbcommunication.model.request.adcemail.SegmentDTO segmentDTO = new com.cathaypacific.mbcommunication.model.request.adcemail.SegmentDTO();
	    	PaxSegmentDTO paxSegmentDTO = paxSegments.stream().filter(ps -> Objects.equals(ps.getSegmentSeq(), segment.getSegmentSeq())).findFirst().orElse(null);
	    	segmentDTO.setArrivalAirport(segment.getArrivalAirport());
	    	segmentDTO.setArrivalDate(buildAdcEmailDate(segment.getArrivalDate()));
	    	segmentDTO.setBookingClass(segment.getBookingClass());
	    	segmentDTO.setCabinClass(segment.getCabinClass());
	    	segmentDTO.setCarrierCode(segment.getCarrierCode());
	    	segmentDTO.setDepartAirport(segment.getDepartAirport());
	    	segmentDTO.setDepartureDate(buildAdcEmailDate(segment.getDepartureDate()));
	    	segmentDTO.setFlightNo(segment.getFlightNo());
	    	segmentDTO.setSegmentSeq(segment.getSegmentSeq());
	    	if(paxSegmentDTO != null) {
	    		segmentDTO.setRemarks(buildRemarks(paxSegmentDTO.getRemarks()));
	    	}
	    	segs.add(segmentDTO);
	    }
		return segs;
	}
	
	private String buildAdcEmailDate(String date) {
		if(StringUtils.isEmpty(date)) {
			return date;
		}
		return DateUtil.convertDateFormat(date, DateUtil.DATETIME_FORMAT_XML, DateUtil.DATE_PATTERN_EEE + " " + DateUtil.DATE_PATTERN_DDMMMYYYY_WITH_SPACE);
	}
	
	private List<String> buildRemarks(List<RemarkDTO> remarkDTOs) {
		List<String> remarks  = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(remarkDTOs)) {
			for (RemarkDTO remarkDTO : remarkDTOs) {
				remarks.add(remarkDTO.getRemark());
			}
		}
		return remarks;
	}
	
	private com.cathaypacific.mbcommunication.model.request.adcemail.PaxInfo buildPaxInfo(List<PaxInfo> paxInfos) {
		com.cathaypacific.mbcommunication.model.request.adcemail.PaxInfo paxInfo = new com.cathaypacific.mbcommunication.model.request.adcemail.PaxInfo();
		PaxInfo info = paxInfos.get(0);
		paxInfo.setTitle(EmailUtil.formatTextToTitleCase(info.getTitle()));
		paxInfo.setPaxSeq(info.getPaxSeq());
		paxInfo.setLastName(EmailUtil.formatTextToTitleCase(info.getLastName()));
		paxInfo.setFirstName(EmailUtil.formatTextToTitleCase(info.getFirstName()));
		paxInfo.setFfpInfo(buildFfpInfo(info.getFfpInfo()));
		return paxInfo;
	}
	
	private com.cathaypacific.mbcommunication.model.request.adcemail.FFPInfoDTO buildFfpInfo(FFPInfoDTO ffpInfo) {
		if(ffpInfo != null) {
			com.cathaypacific.mbcommunication.model.request.adcemail.FFPInfoDTO ffp = new com.cathaypacific.mbcommunication.model.request.adcemail.FFPInfoDTO();
			ffp.setMemberNo(ffpInfo.getMemberNo());
			ffp.setMemberTier(ffpInfo.getMemberTier());
			ffp.setMemberType(buildMemberType(ffpInfo.getMemberTier()));
			return ffp;
		}
		return null;
	}
	
	private String buildMemberType(String memberTier) {
		if(StringUtils.isNotEmpty(memberTier)) {
			if(MMBConstants.RU_MEMBER.equalsIgnoreCase(memberTier)) {
				return MMBConstants.RU_MEMBER;
			} else if(MMBConstants.AM_MEMBER.equalsIgnoreCase(memberTier)) {
				return MMBConstants.AM_MEMBER;
			} else {
				return MMBConstants.MPO_MEMBER;
			}
		}
		return null;
	}
	
	private com.cathaypacific.mbcommunication.model.request.adcemail.PaxContactDTO buildPaxContact(
			List<PaxContactDTO> paxContacts) {
		com.cathaypacific.mbcommunication.model.request.adcemail.PaxContactDTO paxContact = new com.cathaypacific.mbcommunication.model.request.adcemail.PaxContactDTO();
		PaxContactDTO contactDTO = paxContacts.stream().filter(contact -> Objects.equals(contact.getContactType(), ContactTypeEnum.CONTACT_TYPE_EMAIL)
				&& !StringUtils.isEmpty(contact.getContactDetail())).findFirst().orElse(null);
		if (contactDTO != null) {
			paxContact.setContactDetail(contactDTO.getContactDetail());
		}
		return paxContact;
	}
	
	/**
	 * 
	 * @param currentAcceptLanguage
	 * @return
	 */
	private String getLanguageCode(String currentAcceptLanguage) {
		if(StringUtils.isNotEmpty(currentAcceptLanguage)){
			currentAcceptLanguage = currentAcceptLanguage.replace("-", "_");
			String[] languageLocales = currentAcceptLanguage.split("_");
			String language = languageLocales[0];
			return language.toUpperCase();
		}
		return "EN";
	}
}
