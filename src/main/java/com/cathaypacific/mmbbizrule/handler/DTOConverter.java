package com.cathaypacific.mmbbizrule.handler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.cathaypacific.mmbbizrule.model.booking.detail.*;
import com.cathaypacific.olciconsumer.model.response.db.TravelDocDisplay;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.enums.booking.LoungeClass;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.config.BizRuleConfig;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJAdress;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJContactDetails;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJDocument;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJEvent;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJEventBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJEventDescription;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJEventGuest;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJHotel;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJHotelAmenity;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJHotelBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJHotelDescription;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJHotelPosition;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJHotelRoom;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJHotelRoomOption;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJName;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJRoomGuestDetail;
import com.cathaypacific.mmbbizrule.db.dao.TbPortFlightDAO;
import com.cathaypacific.mmbbizrule.db.model.TbPortFlight;
import com.cathaypacific.mmbbizrule.db.service.TbTravelDocDisplayCacheHelper;
import com.cathaypacific.mmbbizrule.dto.common.booking.AdressDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.AirportUpgradeInfoDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.BaggageAllowanceDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.BaggageDetailDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.CheckInBaggageDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.ContactDetailsDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.DepartureArrivalTimeDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.DobDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.DocumentDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.EventBookingDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.EventDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.EventDescriptionDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.EventGuestDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.FQTVInfoDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.FlightBookingDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.HotelAmenityDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.HotelBookingDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.HotelDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.HotelDescriptionDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.HotelPositionDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.HotelRoomDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.HotelRoomOptionDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.KtnRedressDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.MealDetailDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.MealSelectionDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.NameDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.PassengerDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.PassengerDisplayDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.PassengerSegmentDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.PaymentInfoDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.PurchasedBaggageDetailDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.RebookInfoDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.RebookMappingDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.RoomGuestDetailDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.SeatDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.SeatSelectionDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.SegmentDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.SegmentDisplayDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.SharedWaiverBaggageDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.TicketPriceInfoDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.TravelDocDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.TravelDocsDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.ClaimedLoungeDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.LoungeAccessDTO;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPaymentInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRebookInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTicketPriceInfo;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.google.common.collect.Lists;

@Component
public class DTOConverter {
	
	private static LogAgent logger = LogAgent.getLogAgent(DTOConverter.class);
	
	@Autowired
	TbTravelDocDisplayCacheHelper tbTravelDocDisplayCacheHelper;
	
	@Autowired
	TbPortFlightDAO tbPortFlightDAO;
	
	@Autowired
	BizRuleConfig bizRuleConfig;
	
	public FlightBookingDTO convertToBookingDTO(Booking booking, LoginInfo loginInfo) {
		FlightBookingDTO bookingDTO = new FlightBookingDTO();
		bookingDTO.setRloc(booking.getDisplayRloc());
		bookingDTO.setOneARloc(booking.getOneARloc());
		bookingDTO.setGdsRloc(booking.getGdsRloc());
		bookingDTO.setOfficeId(booking.getOfficeId());
		bookingDTO.setSpnr(booking.getSpnr());
		bookingDTO.setPos(booking.getPos());
		bookingDTO.setCanCheckIn(booking.getCanCheckIn());
		bookingDTO.setCanIssueTicketChecking(booking.isCanIssueTicket());
		bookingDTO.setEncryptedRloc(booking.getEncryptedRloc());
		bookingDTO.setMandatoryContactInfo(booking.getMandatoryContactInfo());
		bookingDTO.setCreateDate(booking.getCreateDate());
		bookingDTO.setRpCityCode(booking.getRpCityCode());
		bookingDTO.setIsExistBcode(booking.isCorporateBooking());
		bookingDTO.setCompanionBooking(BookingBuildUtil.isCompanionBooking(booking,loginInfo));
/*		bookingDTO.setSkList(booking.getSkList());
		bookingDTO.setSsrList(booking.getSsrList());
		bookingDTO.setTicketList(booking.getTicketList());*/
		setPassenger(bookingDTO, booking);
		setSegment(bookingDTO, booking);
		setPassengerDisplay(bookingDTO, booking);
		if(!CollectionUtils.isEmpty(booking.getPassengerSegments())){
			setPassengerSegment(bookingDTO, booking);
		}
		bookingDTO.setBookingOnhold(booking.isBookingOnhold());
		bookingDTO.setTktl(booking.isTktl());
		bookingDTO.setTkxl(booking.isTkxl());
		bookingDTO.setAdtk(booking.isAdtk());
		bookingDTO.setPcc(booking.isPcc());
		bookingDTO.setIssueTicketsExisting(booking.isHasIssuedTicket());
		bookingDTO.setTicketIssueInfo(booking.getTicketIssueInfo());
		bookingDTO.setMemberLogin(LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType()));
		if(booking.getBookingPackageInfo() != null) {
			bookingDTO.setFlightOnly(booking.getBookingPackageInfo().isFlightOnly());
			bookingDTO.setPackageBooking(booking.getBookingPackageInfo().isPackageBooking());
		}
		
		bookingDTO.setIbeBooking(booking.isIbeBooking());
		bookingDTO.setTrpBooking(booking.isTrpBooking());
		bookingDTO.setAppBooking(booking.isAppBooking());
		bookingDTO.setGdsBooking(booking.isGdsBooking());
		bookingDTO.setGdsGroupBooking(booking.isGroupBooking());
		bookingDTO.setGccBooking(booking.isGccBooking());
		bookingDTO.setRedBooking(booking.isRedemptionBooking());
		bookingDTO.setStaffBooking(booking.isStaffBooking());
		bookingDTO.setIdBooking(booking.isIDBooking());
		bookingDTO.setRedUpgrade(booking.isHasFqtu());
		bookingDTO.setHasInsurance(booking.isHasInsurance());
		bookingDTO.setHasHotel(booking.isHasHotel());
		
		// Ticket price info
		if (!CollectionUtils.isEmpty(booking.getTicketPriceInfo())) {
			List<TicketPriceInfoDTO> ticketPriceInfoDTOs = Lists.newArrayList();
			for (RetrievePnrTicketPriceInfo retrievePnrTicketPriceInfo: booking.getTicketPriceInfo()) {
				TicketPriceInfoDTO ticketPriceInfoDTO = new TicketPriceInfoDTO();
				ticketPriceInfoDTO.setPassengerIds(retrievePnrTicketPriceInfo.getPaxIds());
				ticketPriceInfoDTO.setSegmentIds(retrievePnrTicketPriceInfo.getSegmentIds());
				ticketPriceInfoDTO.setPrice(retrievePnrTicketPriceInfo.getPrice());
				ticketPriceInfoDTO.setCurrency(retrievePnrTicketPriceInfo.getCurrency());
				ticketPriceInfoDTOs.add(ticketPriceInfoDTO);
			}
			bookingDTO.setTicketPriceInfo(ticketPriceInfoDTOs);
		}
		bookingDTO.setBookingDwCodeList(booking.getBookingDwCode());
		bookingDTO.setBookingWaiveReminders(booking.getBookingWaiveReminders());
		bookingDTO.setRebookMapping(getRebookMapping(booking.getRebookMapping()));
		bookingDTO.setIssueSpecialServices(booking.isIssueSpecialServices());
		bookingDTO.setTempLinkedBooking(booking.isTempLinkedBooking());
		repopulateTravelDocumentGender(bookingDTO, booking);
		return bookingDTO;
	}

	/**
	 * Re-populate travelDocument gender by cprGender under passenger in CPR
	 * 
	 * @param bookingDTO
	 * @param booking
	 */
	private void repopulateTravelDocumentGender(FlightBookingDTO bookingDTO, Booking booking) {
		if(bookingDTO == null || booking == null
				|| CollectionUtils.isEmpty(bookingDTO.getPassengerSegments())
				|| CollectionUtils.isEmpty(booking.getPassengers())) {
			return;
		}
		
		for(Passenger passenger : booking.getPassengers()) {
			if(passenger == null || StringUtils.isEmpty(passenger.getPassengerId())
					|| StringUtils.isEmpty(passenger.getCprGender())) {
				continue;
			}
			
			for(PassengerSegmentDTO passengerSegmentDTO : bookingDTO.getPassengerSegments()) {
				if(passengerSegmentDTO != null && passenger.getPassengerId().equals(passengerSegmentDTO.getPassengerId())) {
					passengerSegmentDTO.repopulateTravelDocumentGender(passenger.getCprGender());
				}
			}
		}
		
	}

	/**
	 * set display in passenger 
	 * @param bookingDTO
	 * @param booking
	 */
	private void setPassengerDisplay(FlightBookingDTO bookingDTO, Booking booking) {
		if (CollectionUtils.isEmpty(bookingDTO.getPassengers()) || CollectionUtils.isEmpty(bookingDTO.getSegments())
				|| CollectionUtils.isEmpty(booking.getPassengerSegments())) {
			return;
		}
		
		for (PassengerDTO passengerDTO : bookingDTO.getPassengers()) {
			String passengerId = passengerDTO.getPassengerId();
			// segment id list of the passenger
			List<String> segmentIds = new ArrayList<>();
			if (!StringUtils.isEmpty(passengerId)) {
				segmentIds = booking.getPassengerSegments().stream()
						.filter(ps -> passengerId.equals(ps.getPassengerId())
								&& !StringUtils.isEmpty(ps.getSegmentId()))
						.map(ps -> ps.getSegmentId()).collect(Collectors.toList());
						
			}
			// segmentDTOs of the passenger
			List<SegmentDTO> segmentDTOs = new ArrayList<>();
			for (SegmentDTO segmentDTO : bookingDTO.getSegments()) {
				if (segmentIds.contains(segmentDTO.getSegmentId())) {
					segmentDTOs.add(segmentDTO);
				}
			}
			
			PassengerDisplayDTO displayDTO = new PassengerDisplayDTO();	
			// set EC
			displayDTO.setEc(anySegmentShouldDisplay(segmentDTOs, OneAConstants.DISPLAY_GROUP_EC));
			// set DA
			displayDTO.setDa(anySegmentShouldDisplay(segmentDTOs, OneAConstants.DISPLAY_GROUP_DA));
			// set KN
			displayDTO.setKn(anySegmentShouldDisplay(segmentDTOs, OneAConstants.DISPLAY_GROUP_KN));
			// set RN
			displayDTO.setRn(anySegmentShouldDisplay(segmentDTOs, OneAConstants.DISPLAY_GROUP_RN));
			// set PI
			displayDTO.setPi(anySegmentShouldDisplay(segmentDTOs, OneAConstants.DISPLAY_GROUP_PI));
			// set EI
			displayDTO.setEi(anySegmentShouldDisplay(segmentDTOs, OneAConstants.DISPLAY_GROUP_EI));
			
			passengerDTO.setDisplay(displayDTO);
		}
	
	}

	/**
	 * check if any segment should display the displayGroup
	 * @param segmentDTOs
	 * @param displayGroupEc
	 * @return
	 */
	private boolean anySegmentShouldDisplay(List<SegmentDTO> segmentDTOs, String displayGroup) {
		if (CollectionUtils.isEmpty(segmentDTOs) || StringUtils.isEmpty(displayGroup)) {
			return false;
		}
		
		if (OneAConstants.DISPLAY_GROUP_EC.equals(displayGroup)) {
			return segmentDTOs.stream().anyMatch(seg -> seg.getDisplay() != null && BooleanUtils.isTrue(seg.getDisplay().isEc()));
		} else if (OneAConstants.DISPLAY_GROUP_DA.equals(displayGroup)) {
			return segmentDTOs.stream().anyMatch(seg -> seg.getDisplay() != null && BooleanUtils.isTrue(seg.getDisplay().isDa()));
		} else if (OneAConstants.DISPLAY_GROUP_KN.equals(displayGroup)) {
			return segmentDTOs.stream().anyMatch(seg -> seg.getDisplay() != null && BooleanUtils.isTrue(seg.getDisplay().isKn()));
		} else if (OneAConstants.DISPLAY_GROUP_RN.equals(displayGroup)) {
			return segmentDTOs.stream().anyMatch(seg -> seg.getDisplay() != null && BooleanUtils.isTrue(seg.getDisplay().isRn()));
		} else if (OneAConstants.DISPLAY_GROUP_PI.equals(displayGroup)) {
			return segmentDTOs.stream().anyMatch(seg -> seg.getDisplay() != null && BooleanUtils.isTrue(seg.getDisplay().isPi()));
		} else if (OneAConstants.DISPLAY_GROUP_EI.equals(displayGroup)) {
			return segmentDTOs.stream().anyMatch(seg -> seg.getDisplay() != null && BooleanUtils.isTrue(seg.getDisplay().isEi()));
		} else {
			return false;
		}
	}

	private List<RebookMappingDTO> getRebookMapping(List<RebookMapping> rebookMappings) {
		if(CollectionUtils.isEmpty(rebookMappings)) {
			return null;
		}
		List<RebookMappingDTO> rebookMappingDTOs = new ArrayList<>();
		for(RebookMapping rebookMapping : rebookMappings) {
			RebookMappingDTO rebookMappingDTO = new RebookMappingDTO();
			rebookMappingDTO.setAcceptSegmentIds(rebookMapping.getAcceptSegmentIds());
			rebookMappingDTO.setCancelledSegmentIds(rebookMapping.getCancelledSegmentIds());
			rebookMappingDTOs.add(rebookMappingDTO);
		}
		return rebookMappingDTOs;
	}

	private void setPassenger(FlightBookingDTO bookingDTO, Booking booking){
		List<Passenger> passengers = booking.getPassengers();
		List<PassengerDTO> passengerDTOs = bookingDTO.getPassengers();
		for(Passenger passenger : passengers){
			PassengerDTO passengerDTO = new PassengerDTO();
			passengerDTO.setPassengerId(passenger.getPassengerId());
			passengerDTO.setFamilyName(passenger.getFamilyName());
			passengerDTO.setGivenName(passenger.getGivenName());
			passengerDTO.setPassengerType(passenger.getPassengerType());
			passengerDTO.setParentId(passenger.getParentId());
			passengerDTO.setTitle(passenger.getTitle());
			passengerDTO.setPrimaryPassenger(passenger.isPrimaryPassenger());
			passengerDTO.setLoginMember(passenger.getLoginMember());
			passengerDTO.setCompanion(passenger.isCompanion());
			passengerDTO.setLoginFFPMatched(passenger.isLoginFFPMatched());
			passengerDTO.setUnaccompaniedMinor(passenger.isUnaccompaniedMinor());
			
			if(BooleanUtils.isTrue(passenger.isCompanion()) && BooleanUtils.isTrue(passenger.isPrimaryPassenger())) {
				bookingDTO.setProfileCompanionBooking(true);
			}
			
			// DOB
			if (passenger.getDob() != null) {
				DobDTO dobDTO = new DobDTO();
				dobDTO.setBirthDateDay(passenger.getDob().getBirthDateDay());
				dobDTO.setBirthDateMonth(passenger.getDob().getBirthDateMonth());
				dobDTO.setBirthDateYear(passenger.getDob().getBirthDateYear());
				passengerDTO.setDob(dobDTO);
			}
			
			// KTN 
			if (passenger.getKtn() != null && !StringUtils.isEmpty(passenger.getKtn().getNumber())) {
				KtnRedressDTO ktn = new KtnRedressDTO();
				ktn.setNumber(passenger.getKtn().getNumber());
				ktn.setQualifierId(passenger.getKtn().getQualifierId());
				passengerDTO.setKtn(ktn);
			}
			
			// redress number
			if (passenger.getRedress() != null && !StringUtils.isEmpty(passenger.getRedress().getNumber())) {
				KtnRedressDTO redress = new KtnRedressDTO();
				redress.setNumber(passenger.getRedress().getNumber());
				redress.setQualifierId(passenger.getRedress().getQualifierId());
				passengerDTO.setRedress(redress);
			}
			
			//Contact info
			if(passenger.getContactInfo() != null){
				if(passenger.getContactInfo().getEmail() != null && !passenger.getContactInfo().getEmail().isEmpty()){
					passengerDTO.findContactInfo().findEmail().setEmail(passenger.findContactInfo().findEmail().getEmailAddress());
					passengerDTO.findContactInfo().findEmail().setType(passenger.findContactInfo().findEmail().getType());
					passengerDTO.findContactInfo().findEmail().setOlssContact(passenger.findContactInfo().findEmail().isOlssContact());
				}

				if(passenger.getContactInfo().getPhoneInfo() != null && !passenger.getContactInfo().getPhoneInfo().isEmpty()){
					passengerDTO.findContactInfo().findPhoneInfo().setCountryCode(passenger.findContactInfo().findPhoneInfo().getCountryCode());
					passengerDTO.findContactInfo().findPhoneInfo().setPhoneNo(passenger.findContactInfo().findPhoneInfo().getPhoneNo());
					passengerDTO.findContactInfo().findPhoneInfo().setPhoneCountryNumber(passenger.findContactInfo().findPhoneInfo().getPhoneCountryNumber());
					passengerDTO.findContactInfo().findPhoneInfo().setType(passenger.findContactInfo().findPhoneInfo().getType());
					passengerDTO.findContactInfo().findPhoneInfo().setOlssContact(passenger.findContactInfo().findPhoneInfo().isOlssContact());
				}

			}
			
			//EmrContact info
			if(passenger.getEmrContactInfo() != null && !passenger.getEmrContactInfo().isEmpty()) {
				passengerDTO.findEmrContactInfo().setCountryCode(passenger.findEmrContactInfo().getCountryCode());
				passengerDTO.findEmrContactInfo().setName(passenger.findEmrContactInfo().getName());
				passengerDTO.findEmrContactInfo().setPhoneNumber(passenger.findEmrContactInfo().getPhoneNumber());
				passengerDTO.findEmrContactInfo().setPhoneCountryNumber(passenger.findEmrContactInfo().getPhoneCountryNumber());
			}
				
			//Destination address
			if(passenger.getDesAddress() != null && !passenger.getDesAddress().isEmpty()) {
				passengerDTO.findDestinaitionAddress().setCity(passenger.findDesAddress().getCity());
				passengerDTO.findDestinaitionAddress().setStateCode(passenger.findDesAddress().getStateCode());
				passengerDTO.findDestinaitionAddress().setStreet(passenger.findDesAddress().getStreet());
				passengerDTO.findDestinaitionAddress().setZipCode(passenger.findDesAddress().getZipCode());
			}
			
			passengerDTOs.add(passengerDTO);
		}
	}




	private void setSegment(FlightBookingDTO bookingDTO, Booking booking){
		List<Segment> segments = booking.getSegments();
		List<SegmentDTO> segmentDTOs = bookingDTO.getSegments();
		List<TravelDocDisplay> tbTravelDocDisplays = tbTravelDocDisplayCacheHelper.findAll();
		List<TbPortFlight> tbPortFlights = tbPortFlightDAO.findByAppCode(MMBConstants.APP_CODE);
		for(Segment segment : segments) {
			SegmentDTO segmentDTO = new SegmentDTO();
			segmentDTO.setUpgradeInfo(segment.getUpgradeInfo());
			segmentDTO.setRtfsFlightStatusInfo(segment.getRtfsFlightStatusInfo());
			segmentDTO.setAirCraftType(segment.getAirCraftType());
			segmentDTO.setCabinClass(segment.getCabinClass());
			segmentDTO.setDestPort(segment.getDestPort());
			segmentDTO.setDestTerminal(segment.getDestTerminal());
			segmentDTO.setIsFlown(segment.isFlown());
			segmentDTO.setMarketCompany(segment.getMarketCompany());
			segmentDTO.setMarketSegmentNumber(segment.getMarketSegmentNumber());
			segmentDTO.setMarketSubClass(segment.getMarketSubClass());
			segmentDTO.setMarketCabinClass(segment.getMarketCabinClass());
			segmentDTO.setOperateCompany(segment.getOperateCompany());
			segmentDTO.setOperateSegmentNumber(segment.getOperateSegmentNumber());
			segmentDTO.setUnConfirmedOperateInfo(segment.isUnConfirmedOperateInfo());
			if (null != segment.getAdditionalOperatorInfo() && StringUtils.isNotEmpty(segment.getAdditionalOperatorInfo().getOperatorName())){
				segmentDTO.setAdditionalOperatorinfo(segment.getAdditionalOperatorInfo());
			}
			segmentDTO.setOriginPort(segment.getOriginPort());
			segmentDTO.setOriginTerminal(segment.getOriginTerminal());
			segmentDTO.setSegmentId(segment.getSegmentID());
			segmentDTO.setStatus(segment.getSegmentStatus().getStatus());
			segmentDTO.setStops(segment.getStops());
			segmentDTO.setSubClass(segment.getSubClass());
			segmentDTO.setCabinClassUpgraded(!StringUtils.isEmpty(segment.getOriginalSubClass()) && !segment.getOriginalSubClass().equals(segment.getSubClass()));
			segmentDTO.setNumberOfStops(segment.getNumberOfStops());
			segmentDTO.setTotalDuration(segment.getTotalDuration());
			segmentDTO.setUsablePrimaryTavelDocs(segment.getUsablePrimaryTavelDocs());
			segmentDTO.setUsableSecondaryTavelDocs(segment.getUsableSecondaryTavelDocs());
			segmentDTO.setPcc(segment.isPcc());
			segmentDTO.setAdtk(segment.isAdtk());
			segmentDTO.setReminder(segment.getTrainReminder());
			//ues openToCheckIn replace canCheckin flag
			segmentDTO.setCanCheckIn(segment.isCanCheckIn() && segment.isOpenToCheckIn());
			DepartureArrivalTimeDTO arrivalTimeDTO = segmentDTO.findArrivalTime();
		
			arrivalTimeDTO.setRtfsActualTime(segment.findArrivalTime().getRtfsActualTime());
			arrivalTimeDTO.setRtfsEstimatedTime(segment.findArrivalTime().getRtfsEstimatedTime());
			arrivalTimeDTO.setRtfsScheduledTime(segment.findArrivalTime().getRtfsScheduledTime());
			arrivalTimeDTO.setTimeZoneOffset(segment.findArrivalTime().getTimeZoneOffset());
			arrivalTimeDTO.setPnrTime(segment.findArrivalTime().getPnrTime());
			
			DepartureArrivalTimeDTO departureTimeDTO = segmentDTO.findDepartureTime();
			departureTimeDTO.setRtfsActualTime(segment.findDepartureTime().getRtfsActualTime());
			departureTimeDTO.setRtfsEstimatedTime(segment.findDepartureTime().getRtfsEstimatedTime());
			departureTimeDTO.setRtfsScheduledTime(segment.findDepartureTime().getRtfsScheduledTime());
			departureTimeDTO.setTimeZoneOffset(segment.findDepartureTime().getTimeZoneOffset());
			departureTimeDTO.setPnrTime(segment.findDepartureTime().getPnrTime());
			
			segmentDTO.setApiVersion(segment.getApiVersion());
			
			segmentDTO.setTrainCase(segment.getTrainCase());
			//Display
			setDisplay(tbTravelDocDisplays, tbPortFlights, segment, segmentDTO, booking.isIDBooking());
			
			//re-book information
			RetrievePnrRebookInfo rebookInfo = segment.getRebookInfo();
			if(rebookInfo != null) {
				RebookInfoDTO rebookInfoDTO = new RebookInfoDTO();
				rebookInfoDTO.setRebooked(rebookInfo.isRebooked());
				rebookInfoDTO.setNewBookedSegmentIds(rebookInfo.getNewBookedSegmentIds());
				rebookInfoDTO.setAccepted(rebookInfo.isAccepted());
				rebookInfoDTO.setAcceptFamilyName(rebookInfo.getAcceptFamilyName());
				rebookInfoDTO.setAcceptGivenName(rebookInfo.getAcceptGivenName());
				segmentDTO.setRebookInfo(rebookInfoDTO);
			}

			segmentDTO.setCheckedIn(segment.isCheckedIn());
			segmentDTO.setOpenToCheckIn(segment.isOpenToCheckIn());// overwrite by OLCI cancheckin
			segmentDTO.setWithinTwentyFourHrs(segment.isWithinTwentyFourHrs());
			segmentDTO.setWithinNinetyMins(segment.isWithinNinetyMins());
			segmentDTO.setCheckInRemainingTime(segment.getCheckInRemainingTime());
			segmentDTO.setHasCheckedBaggage(segment.isHasCheckedBaggge());
			segmentDTOs.add(segmentDTO);
		}
		
	}

	private void setPassengerSegment(FlightBookingDTO bookingDTO, Booking booking){
		List<PassengerSegment> passengerSegments = booking.getPassengerSegments();
		List<Passenger> passengers = booking.getPassengers();
		List<PassengerSegmentDTO> passengerSegmentDTOs = bookingDTO.getPassengerSegments();
		for(PassengerSegment passengerSegment : passengerSegments) {
			PassengerSegmentDTO passengerSegmentDTO = new PassengerSegmentDTO();
			
			//FQTV Info
			FQTVInfo fqtvInfo = passengerSegment.getFqtvInfo();
			FQTVInfoDTO fqtvInfoDTO = passengerSegmentDTO.findFQTVInfo();
			if(fqtvInfo != null && !StringUtils.isEmpty(fqtvInfo.getMembershipNumber())){
				fqtvInfoDTO.setCompanyId(fqtvInfo.getCompanyId());
				fqtvInfoDTO.setMembershipNumber(fqtvInfo.getMembershipNumber());
				fqtvInfoDTO.setTierLevel(fqtvInfo.getTierLevel());
				fqtvInfoDTO.setTopTier(fqtvInfo.isTopTier());
				fqtvInfoDTO.setProductLevel(fqtvInfo.isProductLevel());
				fqtvInfoDTO.setIsAM(fqtvInfo.getAm());
				fqtvInfoDTO.setIsMPO(fqtvInfo.getMpo());
			}
			
			//Travel Documents & KTN, redress
			TravelDoc priTravelDoc = passengerSegment.getPriTravelDoc();
			TravelDoc secTravelDoc = passengerSegment.getSecTravelDoc();

			Passenger passenger = getPassengerById(passengers, passengerSegment.getPassengerId());
			TravelDoc pPriTravelDoc = passenger.getPriTravelDocs().stream().filter(travelDoc -> travelDoc != null).findFirst().orElse(null);
			TravelDoc pSecTravelDoc = passenger.getSecTravelDocs().stream().filter(travelDoc -> travelDoc != null).findFirst().orElse(null);
			
			if (priTravelDoc != null || secTravelDoc != null || pPriTravelDoc != null || pSecTravelDoc != null) {
				TravelDocsDTO travelDoc = new TravelDocsDTO();
				
				//Primary Travel Document
				if(priTravelDoc != null) {
					TravelDocDTO travelDocDTO = travelDoc.findPriTravelDoc();
					setTravelDocDTO(priTravelDoc, travelDocDTO, true, false);
				} else {
					if(pPriTravelDoc != null) {
						TravelDocDTO travelDocDTO = travelDoc.findPriTravelDoc();
						setTravelDocDTO(pPriTravelDoc, travelDocDTO, false, false);						
					}
				}
				//Secondary Travel Document
				if(secTravelDoc != null) {
					TravelDocDTO travelDocDTO = travelDoc.findSecTravelDoc();
					setTravelDocDTO(secTravelDoc, travelDocDTO, true, true);
				} else {
					if(pSecTravelDoc != null) {
						TravelDocDTO travelDocDTO = travelDoc.findSecTravelDoc();
						setTravelDocDTO(pSecTravelDoc, travelDocDTO, false, true);						
					}
				}
				
				if(!travelDoc.isEmpty()) {
					passengerSegmentDTO.setTravelDoc(travelDoc);
				}
			}
			
			// baggage allowance
			setBaggageAllowance(passengerSegment, passengerSegmentDTO);
			
			// Set meal
			MealDetail mealDetail = passengerSegment.getMeal();
			if(mealDetail != null) {
				MealDetailDTO mealDetailDTO = new MealDetailDTO();
				mealDetailDTO.setCompanyId(mealDetail.getCompanyId());
				mealDetailDTO.setMealCode(mealDetail.getMealCode());
				mealDetailDTO.setQuantity(mealDetail.getQuantity());
				mealDetailDTO.setStatus(mealDetail.getStatus());
				passengerSegmentDTO.setMeal(mealDetailDTO);
			}
			
			passengerSegmentDTO.setPassengerId(passengerSegment.getPassengerId());
			
			//seat and extra seat
			SeatDetail seatDetail = passengerSegment.getSeat();
			if(seatDetail != null){
				if(passengerSegmentDTO.getSeat() == null){
					passengerSegmentDTO.setSeat(new SeatDTO());
				}
				passengerSegmentDTO.getSeat().setSeatNo(seatDetail.getSeatNo());
				passengerSegmentDTO.getSeat().setExlSeat(seatDetail.isExlSeat());
				passengerSegmentDTO.getSeat().setAsrSeat(seatDetail.isAsrSeat());
				passengerSegmentDTO.getSeat().setPaid(seatDetail.isPaid());
				passengerSegmentDTO.getSeat().setStatus(seatDetail.getStatus());
				passengerSegmentDTO.getSeat().setFromDCS(seatDetail.isFromDCS());
				passengerSegmentDTO.getSeat().setPaymentInfo(buildPaymentInfo(seatDetail.getPaymentInfo()));
				passengerSegmentDTO.getSeat().setFreeSeat(isFreeSeat(seatDetail, passengerSegment));
			}
			
			if(!CollectionUtils.isEmpty(passengerSegment.getExtraSeats())){
				if(passengerSegmentDTO.getExtraSeats() == null){
					passengerSegmentDTO.setExtraSeats(new ArrayList<>());
				}
				for(SeatDetail extraSeat : passengerSegment.getExtraSeats()){
					if(extraSeat != null){
						SeatDTO seat = new SeatDTO();
						seat.setSeatNo(extraSeat.getSeatNo());
						seat.setExlSeat(extraSeat.isExlSeat());
						seat.setPaid(extraSeat.isPaid());
						seat.setStatus(extraSeat.getStatus());
						seat.setPaymentInfo(buildPaymentInfo(extraSeat.getPaymentInfo()));
						seat.setFreeSeat(isFreeSeat(seatDetail, passengerSegment));
						passengerSegmentDTO.getExtraSeats().add(seat);
					}

				}
			}

			SeatDTO seatDTO = passengerSegmentDTO.getSeat();
			if (seatDTO != null && !isConfirmedSeatStatus(seatDTO.getStatus()) &&
					BooleanUtils.isNotTrue(seatDTO.isFreeSeat())) {
				logger.warn(String.format("Seat %s is chargeable and unconfirmed. Paid: %b. RLOC: %s.",
						seatDTO.getSeatNo(), seatDTO.isPaid(), bookingDTO.getOneARloc()));

				// Remove seat and extra seats when seat is unconfirmed and not free and unpaid.
				if (BooleanUtils.isNotTrue(seatDTO.isPaid())) {
					passengerSegmentDTO.setSeat(null);
					passengerSegmentDTO.setExtraSeats(null);
				}
			}

			// seat preference
			SeatPreference seatPreference = passengerSegment.getPreference();
			if(seatPreference != null){
				passengerSegmentDTO.setSeatPreference(passengerSegment.getPreference().getPreferenceCode());
				passengerSegmentDTO.setSpecialPreference(passengerSegment.getPreference().isSpeicalPreference());
			}
			
			passengerSegmentDTO.setSegmentId(passengerSegment.getSegmentId());
			passengerSegmentDTO.setMemberAward(passengerSegment.getMemberAward());
			passengerSegmentDTO.setEticket(passengerSegment.getEticketNumber());
			
			//seat selection
			if(passengerSegment.getMmbSeatSelection() != null){
				SeatSelectionDTO seatSelectionDTO = new SeatSelectionDTO();
				seatSelectionDTO.setEligible(passengerSegment.getMmbSeatSelection().isEligible());
				seatSelectionDTO.setLowRBD(passengerSegment.getMmbSeatSelection().isLowRBD());
				seatSelectionDTO.setDisabilities(passengerSegment.getMmbSeatSelection().getDisabilities());
				seatSelectionDTO.setPaidASR(passengerSegment.getMmbSeatSelection().isPaidASR());
				if(BooleanUtils.isTrue(passengerSegment.getMmbSeatSelection().isEligible())) {
					seatSelectionDTO.setAsrFOC(passengerSegment.getMmbSeatSelection().isAsrFOC());
					seatSelectionDTO.setSelectedAsrFOC(passengerSegment.getMmbSeatSelection().isSelectedAsrFOC());
					seatSelectionDTO.setXlFOC(BooleanUtils.isTrue(passengerSegment.getMmbSeatSelection().isXlFOC()));
					seatSelectionDTO.setXlFOCReason(passengerSegment.getMmbSeatSelection().getXlFOCReason());
				}

				//set special seat eligibility
				SpecialSeatEligibility specialSeatEligibility = passengerSegment.getMmbSeatSelection().getSpecialSeatEligibility();
				if(specialSeatEligibility != null){
					seatSelectionDTO.findSpecialSeatEligibility().setExlSeat(specialSeatEligibility.getExlSeat());
					seatSelectionDTO.findSpecialSeatEligibility().setAsrSeat(specialSeatEligibility.getAsrSeat());
					seatSelectionDTO.findSpecialSeatEligibility().setExitRowSeat(specialSeatEligibility.getExitRowSeat());
					seatSelectionDTO.findSpecialSeatEligibility().setBcstSeat(specialSeatEligibility.getBcstSeat());
					seatSelectionDTO.findSpecialSeatEligibility().setUmSeat(specialSeatEligibility.getUmSeat());
				}
				passengerSegmentDTO.setSeatSelection(seatSelectionDTO);
			}
			
			if(passengerSegment.getMealSelection() != null) {
				MealSelectionDTO mealSelectionDTO = new MealSelectionDTO();
				mealSelectionDTO.setMealOption(passengerSegment.getMealSelection().getMealOption());
				List<SpecialMeal> specialMeals = passengerSegment.getMealSelection().getSpecialMeals();
				if(!CollectionUtils.isEmpty(specialMeals)) {
					mealSelectionDTO.setSpecialMealList(specialMeals.stream().map(SpecialMeal::getType).collect(Collectors.toList()));
				}
				passengerSegmentDTO.setMealSelection(mealSelectionDTO);
			}

			if(!CollectionUtils.isEmpty(passengerSegment.getSpecialServices())){
				passengerSegmentDTO.setSpecialServices(BookingBuildUtil.removeDubplicateSepcialService(passengerSegment.getSpecialServices()));
			}
			
			passengerSegmentDTO.setPickUpNumber(passengerSegment.getPickUpNumber());
			passengerSegmentDTO.setHasEticket(!StringUtils.isEmpty(passengerSegment.getEticketNumber()));
			passengerSegmentDTO.setCxKaEt(BookingBuildUtil.isCxKaET(passengerSegment.getEticketNumber()));
			passengerSegmentDTO.setCheckedIn(passengerSegment.isCheckedIn());
			passengerSegmentDTO.setDepartureGate(passengerSegment.getDepartureGate());
			passengerSegmentDTO.setIsTransit(passengerSegment.getIsTransit());
			passengerSegmentDTO.setReverseCheckinCarrier(passengerSegment.getReverseCheckinCarrier());
			if(passengerSegment.getClaimedLounge() != null && passengerSegment.getClaimedLounge().getType() != null){
				ClaimedLoungeDTO claimedLounge = new ClaimedLoungeDTO();
				claimedLounge.setTier(passengerSegment.getClaimedLounge().getTier());
				claimedLounge.setType(passengerSegment.getClaimedLounge().getType());
				passengerSegmentDTO.setClaimedLounge(claimedLounge);
			}
			// Use loungeAccess to replace clainedLounge
			passengerSegmentDTO.setLoungeAccess(buildLoungeAccess(passengerSegment.getClaimedLounge(), passengerSegment.getPurchasedLounge())); 
			// airport upgrade information
			AirportUpgradeInfo airportUpgradeInfo = passengerSegment.getAirportUpgradeInfo();
			if(airportUpgradeInfo != null) {
				AirportUpgradeInfoDTO airportUpgradeInfoDTO = new AirportUpgradeInfoDTO();
				airportUpgradeInfoDTO.setNewCabinClass(airportUpgradeInfo.getNewCabinClass());
				passengerSegmentDTO.setAirportUpgradeInfo(airportUpgradeInfoDTO);
			}
			// upgrade process status
			passengerSegmentDTO.setUpgradeProgressStatus(passengerSegment.getUpgradeProgressStatus());
			passengerSegmentDTOs.add(passengerSegmentDTO);
		}
	}

	/**
	 * check if the seat is a free seat
	 * @param seatDetail
	 * @param passengerSegment
	 * @return
	 */
	private Boolean isFreeSeat(SeatDetail seatDetail, PassengerSegment passengerSegment) {
		if(seatDetail == null || passengerSegment == null || passengerSegment.getMmbSeatSelection() == null) {
			return false;
		}
		SeatSelection seatSelection = passengerSegment.getMmbSeatSelection();

		return (BooleanUtils.isTrue(seatDetail.isAsrSeat()) && (seatSelection.isAsrFOC() || seatSelection.isSelectedAsrFOC()))
				|| (BooleanUtils.isTrue(seatDetail.isExlSeat()) && BooleanUtils.isTrue(seatSelection.isXlFOC()))
				|| (!BooleanUtils.isTrue(seatDetail.isAsrSeat()) && !BooleanUtils.isTrue(seatDetail.isExlSeat()));
	}

	/**
	 * Check if seat is confirmed.
	 *
	 * @param seatStatus
	 * @return
	 */
	private boolean isConfirmedSeatStatus(String seatStatus) {
		return OneAConstants.HK_STATUS.equals(seatStatus) || OneAConstants.KK_STATUS.equals(seatStatus) ||
				OneAConstants.KL_STATUS.equals(seatStatus) || OneAConstants.TK_STATUS.equals(seatStatus);
	}

	private PaymentInfoDTO buildPaymentInfo(RetrievePnrPaymentInfo pnrPaymentInfo){
		PaymentInfoDTO paymentInfoDTO = null;
		if(pnrPaymentInfo!=null){
			paymentInfoDTO = new PaymentInfoDTO();
			paymentInfoDTO.setAmount(pnrPaymentInfo.getAmount());
			paymentInfoDTO.setCurrency(pnrPaymentInfo.getCurrency());
			paymentInfoDTO.setDate(pnrPaymentInfo.getDate());
			paymentInfoDTO.setOfficeId(pnrPaymentInfo.getOfficeId());
			paymentInfoDTO.setQualifierId(pnrPaymentInfo.getQualifierId());
			paymentInfoDTO.setTicket(pnrPaymentInfo.getTicket());
		}
		return paymentInfoDTO;
	}
	/**
	 * 
	* @Description set baggage allowance
	* @param passengerSegment
	* @param passengerSegmentDTO
	* @return void
	* @author haiwei.jia
	 */
	private void setBaggageAllowance(PassengerSegment passengerSegment, PassengerSegmentDTO passengerSegmentDTO) {
		if(passengerSegment.getBaggageAllowance() != null){
			BaggageAllowance baggageAllowance = passengerSegment.getBaggageAllowance();
			BaggageAllowanceDTO baggageAllowanceDTO = passengerSegmentDTO.findBaggageAllowance();
			baggageAllowanceDTO.setCompleted(baggageAllowance.isCompleted());
			baggageAllowanceDTO.setCabinBaggageUnavailable(baggageAllowance.isCabinBaggageUnavailable());
			
			// check-in baggage
			if(baggageAllowance.getCheckInBaggage() != null){
				CheckInBaggage checkInBaggage = baggageAllowance.getCheckInBaggage();
				
				// standard baggage
				if(checkInBaggage.getStandardBaggage() != null){
					passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findStandardBaggage().setAmount(
							checkInBaggage.getStandardBaggage().getAmount());
					passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findStandardBaggage().setUnit(
							checkInBaggage.getStandardBaggage().getUnit());
				}
				// waiver baggage
				if(checkInBaggage.getWaiverBaggage() != null){
					passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findWaiverBaggage().setAmount(
							checkInBaggage.getWaiverBaggage().getAmount());
					passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findWaiverBaggage().setUnit(
							checkInBaggage.getWaiverBaggage().getUnit());
				}
				// purchased baggage
				if (checkInBaggage.getPurchasedBaggages() != null && !checkInBaggage.getPurchasedBaggages().isEmpty()) {
					BigInteger totalAmount = checkInBaggage.getPurchasedBaggages().stream()
							.filter(pb -> pb.getAmount() != null).map(PurchasedBaggageDetail::getAmount)
							.reduce((sum, amount) -> sum.add(amount)).orElse(null);
					PurchasedBaggageDetailDTO purchasedBaggage = passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findPurchasedBaggage();
					purchasedBaggage.setAmount(totalAmount);
					purchasedBaggage.setUnit(checkInBaggage.getPurchasedBaggages().get(0).getUnit());
					
					List<RetrievePnrPaymentInfo> paymentList = checkInBaggage.getPurchasedBaggages().stream().filter(pb->pb.getPaymentInfo()!=null).map(pb->pb.getPaymentInfo()).collect(Collectors.toList());
					if(!paymentList.isEmpty()){
						PaymentInfoDTO pament = new PaymentInfoDTO();
						pament.setAmount(paymentList.stream().filter(pl->pl.getAmount()!=null).map(RetrievePnrPaymentInfo::getAmount).reduce((sum, amount)->sum.add(amount)).orElse(null));
						pament.setCurrency(paymentList.get(0).getCurrency());
						purchasedBaggage.setPaymentInfo(pament);
					}
					
				}
				// shared purchased baggage
				if(!CollectionUtils.isEmpty(checkInBaggage.getSharedWaiverBaggages())){
					// set shared waiver baggage
					setSharedWaiverBaggage(passengerSegmentDTO, checkInBaggage);
				}
				// member baggage
				// if passenger is a member with additional member baggage, new the memberBaggage for frontend display logic
				if(passengerSegment.getFqtvInfo() != null && MMBBizruleConstants.TIERS_WITH_MEMBER_BAGGAGE_ALLOWANCE.contains(passengerSegment.getFqtvInfo().getTierLevel())) {
					passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().setMemberBaggage(new BaggageDetailDTO());
				}
				if (checkInBaggage.getMemberBaggage() != null) {
					passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findMemberBaggage().setAmount(
							checkInBaggage.getMemberBaggage().getAmount());
					passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findMemberBaggage().setUnit(
							checkInBaggage.getMemberBaggage().getUnit());
				}
				
				// if not all kinds of check in baggage is empty, calculate the total amount
				if (passengerSegmentDTO.getBaggageAllowance() != null
						&& passengerSegmentDTO.getBaggageAllowance().getCheckInBaggage() != null
						&& (passengerSegmentDTO.getBaggageAllowance().getCheckInBaggage().getStandardBaggage() != null
								|| passengerSegmentDTO.getBaggageAllowance().getCheckInBaggage()
										.getWaiverBaggage() != null
								|| passengerSegmentDTO.getBaggageAllowance().getCheckInBaggage()
										.getPurchasedBaggage() != null
								|| (passengerSegmentDTO.getBaggageAllowance().getCheckInBaggage()
										.getMemberBaggage() != null
										&& passengerSegmentDTO.getBaggageAllowance().getCheckInBaggage()
												.getMemberBaggage().getAmount() != null))) {
					CheckInBaggageDTO checkInBaggageDTO = passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage();
					List<BaggageDetailDTO> breakdownBaggages = new ArrayList<>();
					if (checkInBaggageDTO.getStandardBaggage() != null && checkInBaggageDTO.getStandardBaggage().getAmount() != null) {
						breakdownBaggages.add(checkInBaggageDTO.getStandardBaggage());
					}
					if (checkInBaggageDTO.getWaiverBaggage() != null && checkInBaggageDTO.getWaiverBaggage().getAmount() != null) {
						breakdownBaggages.add(checkInBaggageDTO.getWaiverBaggage());
					}
					if (checkInBaggageDTO.getPurchasedBaggage() != null && checkInBaggageDTO.getPurchasedBaggage().getAmount() != null) {
						breakdownBaggages.add(checkInBaggageDTO.getPurchasedBaggage());
					}
					// need to check if the amount is null because we would create a empty memberBaggage if the passenger has a tier with member baggage
					if (checkInBaggageDTO.getMemberBaggage() != null && checkInBaggageDTO.getMemberBaggage().getAmount() != null) {
						breakdownBaggages.add(checkInBaggageDTO.getMemberBaggage());
					}
					passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findTotal().setAmount(
							breakdownBaggages.stream().map(BaggageDetailDTO::getAmount).reduce(
									new BigInteger("0"), (a, b) -> {
										return (b != null) ? a.add(b) : a;
									})
							);
					passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findTotal().setUnit(
							breakdownBaggages.stream().map(BaggageDetailDTO::getUnit).findFirst().orElse(null));
				}
			
				// limit
				if (checkInBaggage.getLimit() != null) {
					passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findLimit().setAmount(
							checkInBaggage.getLimit().getAmount());
					passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().findLimit().setUnit(
							checkInBaggage.getLimit().getUnit());
				}
			}
			
			// cabin baggage
			if (baggageAllowance.getCabinBaggage() != null) {
				CabinBaggage cabinBaggage = baggageAllowance.getCabinBaggage();
				
				// standard baggage
				if(cabinBaggage.getStandardBaggage() != null){
					passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().findStandardBaggage().setAmount(
							cabinBaggage.getStandardBaggage().getAmount());
					passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().findStandardBaggage().setUnit(
							cabinBaggage.getStandardBaggage().getUnit());
				}
				// member baggage
				if (cabinBaggage.getMemberBaggage() != null) {
					passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().findMemberBaggage().setAmount(
							cabinBaggage.getMemberBaggage().getAmount());
					passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().findMemberBaggage().setUnit(
							cabinBaggage.getMemberBaggage().getUnit());
				}
				// total
				if (cabinBaggage.getTotal() != null) {
					passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().findTotal().setAmount(
							cabinBaggage.getTotal().getAmount());
					passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().findTotal().setUnit(
							cabinBaggage.getTotal().getUnit());
				}
				// limit
				if (cabinBaggage.getLimit() != null) {
					passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().findLimit().setAmount(
							cabinBaggage.getLimit().getAmount());
					passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().findLimit().setUnit(
							cabinBaggage.getLimit().getUnit());
				}
				// small item
				if (cabinBaggage.getSmallItem() != null) {
					passengerSegmentDTO.findBaggageAllowance().findCabinBaggage().setSmallItem(
							cabinBaggage.getSmallItem());
				}
			}
		}
	}

	/**
	 * 
	* @Description set shared waiver baggage
	* @param passengerSegmentDTO
	* @param checkInBaggage
	* @return void
	* @author haiwei.jia
	 */
	private void setSharedWaiverBaggage(PassengerSegmentDTO passengerSegmentDTO, CheckInBaggage checkInBaggage) {
		List<SharedWaiverBaggageDTO> sharedWaiverBaggageDTOs = new ArrayList<>();
		for(SharedWaiverBaggage sharedWaiverBaggage : checkInBaggage.getSharedWaiverBaggages()){
			if(sharedWaiverBaggage != null && sharedWaiverBaggage.getGroupId() != null && sharedWaiverBaggage.getTotalBaggage() != null){
				SharedWaiverBaggageDTO sharedWaiverBaggageDTO = new SharedWaiverBaggageDTO();
				sharedWaiverBaggageDTO.setGroupId(sharedWaiverBaggage.getGroupId());
				sharedWaiverBaggageDTO.findTotalBaggage().setAmount(sharedWaiverBaggage.getTotalBaggage().getAmount());
				sharedWaiverBaggageDTO.findTotalBaggage().setUnit(sharedWaiverBaggage.getTotalBaggage().getUnit());
				
				sharedWaiverBaggageDTOs.add(sharedWaiverBaggageDTO);
			}
		}
		passengerSegmentDTO.findBaggageAllowance().findCheckInBaggage().setSharedWaiverBaggages(sharedWaiverBaggageDTOs);
	}
	
	/**
	 * 
	 * @Description set display
	 * @param tbTravelDocDisplays
	 * @param tbPortFlights
	 * @param segment
	 * @param segmentDTO
	 * @param isIdBooking 
	 * @return void
	 * @author haiwei.jia
	 * 
	 */
	private void setDisplay(List<TravelDocDisplay> tbTravelDocDisplays, List<TbPortFlight> tbPortFlights,
			Segment segment, SegmentDTO segmentDTO, boolean isIdBooking) {
		SegmentDisplayDTO display = segmentDTO.findDisplay();
		if (segment.getApiVersion() != null) {
			for (TravelDocDisplay tbTravelDocDisplay : tbTravelDocDisplays) {
				String group = tbTravelDocDisplay.getTravelDocGroup();
				String version = String.valueOf(tbTravelDocDisplay.getTravelDocVersion());
				if (group == null || version == null || !version.equals(segment.getApiVersion())) {
					continue;
				}
				if (group.equals(TBConstants.TRAVEL_DOC_GROUP_EC)) {
					display.setEc(true);
				} else if (group.equals(TBConstants.TRAVEL_DOC_GROUP_DA)) {
					display.setDa(true);
				} else if (group.equals(TBConstants.TRAVEL_DOC_GROUP_CR)) {
					display.setCr(true);
				} else if (group.equals(TBConstants.TRAVEL_DOC_GROUP_PT)) {
					display.setPt(true);
				} else if (group.equals(TBConstants.TRAVEL_DOC_GROUP_ST)) {
					display.setSt(true);
				} else if (group.equals(TBConstants.TRAVEL_DOC_GROUP_TS)) {
					display.setKn(true);
				} else if (group.equals(TBConstants.TRAVEL_DOC_GROUP_RN)) {
					display.setRn(true);
				}
			}
		}	
		
		//set travel doc display by table in DB
		setTdDisplayByDb(tbPortFlights, segment, display);
		
		//EI, PI are always true
		display.setPi(true);
		display.setEi(true);
		display.setFf(!isIdBooking
				&& !MMBBizruleConstants.NON_AIR_SECTOR_CARRIER_CODE_DEUTSCHE_BAHN.equals(segment.getOperateCompany()));
		
		//train pick up number display or not
		display.setPn(segment.isTrainPNDisplay());
	}

	/**
	 * 
	* @Description set travel doc display by tb_port_flight table in DB
	* @param tbPortFlights
	* @param segment
	* @param display
	* @return void
	* @author haiwei.jia
	 */
	private void setTdDisplayByDb(List<TbPortFlight> tbPortFlights, Segment segment, SegmentDisplayDTO display) {
		List<String> airlineCodes = new ArrayList<>();
		List<String> origins = new ArrayList<>();
		List<String> destinations = new ArrayList<>();
		
		// for the three field, need to support wild card, so add it in
		airlineCodes.add(TBConstants.TB_PORT_FLIGHT_WILD_CARD_FOR_AIRLINE_CODE);
		origins.add(TBConstants.TB_PORT_FLIGHT_WILD_CARD_FOR_ORIGIN);
		destinations.add(TBConstants.TB_PORT_FLIGHT_WILD_CARD_FOR_DESTINATION);
		
		// for non-air sector, map by marketing company
		if(OneAConstants.EQUIPMENT_TRN.equals(segment.getAirCraftType())
				|| OneAConstants.EQUIPMENT_LCH.equals(segment.getAirCraftType())
				|| OneAConstants.EQUIPMENT_BUS.equals(segment.getAirCraftType())){
			airlineCodes.add(segment.getMarketCompany());
		}
		// for air sector, map by operating company
		else{
			airlineCodes.add(segment.getOperateCompany());
		}
		origins.add(segment.getOriginPort());
		destinations.add(segment.getDestPort());
		
		List<TbPortFlight> mathcedTbPortFlights;

		if(segment.getOperateSegmentNumber() != null){
			mathcedTbPortFlights = tbPortFlights.stream()
					.filter(portflight -> portflight != null 
							&& MMBConstants.APP_CODE.equals(portflight.getAppCode())
							&& airlineCodes.contains(portflight.getAirlineCode())
							&& Integer.valueOf(segment.getOperateSegmentNumber()) >= portflight.getFlightNumFrom()
							&& Integer.valueOf(segment.getOperateSegmentNumber()) <= portflight.getFlightNumTo()
							&& origins.contains(portflight.getOrigin())
							&& destinations.contains(portflight.getDestination()))
					.collect(Collectors.toList());	
		}
		else{
			mathcedTbPortFlights = tbPortFlights.stream()
					.filter(portflight -> portflight != null 
							&& MMBConstants.APP_CODE.equals(portflight.getAppCode())
							&& airlineCodes.contains(portflight.getAirlineCode())
							&& portflight.getFlightNumFrom() == MMBBizruleConstants.FLIGHT_NUMBER_MIN
							&& portflight.getFlightNumTo() == MMBBizruleConstants.FLIGHT_NUMBER_MAX
							&& origins.contains(portflight.getOrigin())
							&& destinations.contains(portflight.getDestination()))
					.collect(Collectors.toList());	
		}
		//get one tbPortFlight from mathcedTbPortFlights
		TbPortFlight tbPortFlight = getTbPortFlight(mathcedTbPortFlights);
		
		if(tbPortFlight == null || !TBConstants.SEGMENT_ACTION_ALLOW.equals(tbPortFlight.getValue())){
			display.setPt(false);
			display.setSt(false);	
		}	
	}
	
	/**
	 * 
	* @Description get one tbPortFlight from tbPortFlightList (copied from OLCI)
	* @param tbPortFlightList
	* @return TbPortFlight
	* @author haiwei.jia
	 */
	public TbPortFlight getTbPortFlight(List<TbPortFlight> tbPortFlightList) {
		TbPortFlight tbPortFlight = null;
		if (tbPortFlightList != null && !tbPortFlightList.isEmpty()) {
			List<TbPortFlight> filteredTBPortFlightList = new ArrayList<>();
			int[] scoreList = new int[tbPortFlightList.size()];
			int minScore = 999;
			int i = 0;
			for (TbPortFlight f:tbPortFlightList) {
				int score = 0;
				if (f.getAirlineCode().contains("*")) {
					score++;
				}
				if (f.getOrigin().contains("*")) {
					score++;
				}
				if (f.getDestination().contains("*")) {
					score++;
				}
				if (i==0 || score < minScore) {
					minScore = score;
				} 
				scoreList[i] = score;
				i++;
			}
			for (i=0; i<scoreList.length; i++) {
				if (scoreList[i] == minScore) {
					filteredTBPortFlightList.add(tbPortFlightList.get(i));
				}
			}
			for (TbPortFlight f:filteredTBPortFlightList) {
				if (TBConstants.SEGMENT_ACTION_INHIBIT.equals(f.getValue())) {
					return f;
				}
			}
			for (TbPortFlight f:filteredTBPortFlightList) {
				if (TBConstants.SEGMENT_ACTION_EXCLUDE.equals(f.getValue())) {
					return f;
				}
			}
			for (TbPortFlight f:filteredTBPortFlightList) {
				if (TBConstants.SEGMENT_ACTION_DISPLAY.equals(f.getValue())) {
					return f;
				}
			}
			for (TbPortFlight f:filteredTBPortFlightList) {
				if (TBConstants.SEGMENT_ACTION_ALLOW.equals(f.getValue())) {
					return f;
				}
			}
		}
		return tbPortFlight;
	}

	/**
	 * @param passenger
	 * @param travelDoc
	 * @param travelDocDTO
	 * @param productLevel
	 */
	private void setTravelDocDTO(TravelDoc travelDoc, TravelDocDTO travelDocDTO, boolean productLevel, boolean isSecondTravelDoc) {
		if(travelDoc == null) {
			return;
		}
		if (!isSecondTravelDoc) {
			travelDocDTO.setCountryOfResidence(travelDoc.getCountryOfResidence());
		}
		travelDocDTO.setBirthDateDay(travelDoc.getBirthDateDay());
		travelDocDTO.setBirthDateMonth(travelDoc.getBirthDateMonth());
		travelDocDTO.setBirthDateYear(travelDoc.getBirthDateYear());
		travelDocDTO.setCompanyId(travelDoc.getCompanyId());
		travelDocDTO.setCountryOfIssuance(travelDoc.getCountryOfIssuance());
		travelDocDTO.setExpiryDateDay(travelDoc.getExpiryDateDay());
		travelDocDTO.setExpiryDateMonth(travelDoc.getExpiryDateMonth());
		travelDocDTO.setExpiryDateYear(travelDoc.getExpiryDateYear());
		travelDocDTO.setFamilyName(travelDoc.getFamilyName());
		travelDocDTO.setGender(travelDoc.getGender());
		travelDocDTO.setGivenName(travelDoc.getGivenName());
		travelDocDTO.setInfant(travelDoc.isInfant());
		travelDocDTO.setNationality(travelDoc.getNationality());
		travelDocDTO.setTravelDocumentNumber(travelDoc.getTravelDocumentNumber());
		travelDocDTO.setTravelDocumentType(travelDoc.getTravelDocumentType());
		travelDocDTO.setQualifierId(travelDoc.getQualifierId());
		travelDocDTO.setProductLevel(productLevel);
	}
	
	/**
	 * @Description get Passenger by passengerId
	 * @param passengers
	 * @param passengerId
	 * @return Passenger
	 */
	private Passenger getPassengerById(List<Passenger> passengers, String passengerId) {
		if(CollectionUtils.isEmpty(passengers) || StringUtils.isEmpty(passengerId)){
			return null;
		}
		for(Passenger passenger : passengers){
			if(passenger != null && passengerId.equals(passenger.getPassengerId())){
				return passenger;
			}
		}
		return null;
	}
	
	/**
	 * build lounge access and set highest loungeClass
	 * @param claimedLounge
	 * @param purchasedLounge
	 * @return
	 */
	public List<LoungeAccessDTO> buildLoungeAccess(ClaimedLounge claimedLounge, PurchasedLounge purchasedLounge) {
		List<LoungeAccessDTO> loungeAccessList = new ArrayList<>();
		LoungeAccessDTO loungeAccessDTO = null;
		LoungeClass loungeClass = null;
		if (purchasedLounge != null) {
			loungeAccessDTO = new LoungeAccessDTO();
			loungeClass = purchasedLounge.getType();
			
			loungeAccessDTO.setType(loungeClass);
			loungeAccessDTO.setTier(purchasedLounge.getTier());
			loungeAccessDTO.setPaymentInfo(buildPaymentInfo(purchasedLounge.getPaymentInfo()));
			loungeAccessDTO.isPurchasedLounge(true);
			loungeAccessList.add(loungeAccessDTO);
		}
		if (claimedLounge != null && claimedLounge.getType() != null && !claimedLounge.getType().equals(loungeClass)) {
			loungeAccessDTO = new LoungeAccessDTO();
			loungeAccessDTO.setType(claimedLounge.getType());
			loungeAccessDTO.setTier(claimedLounge.getTier());
			loungeAccessDTO.isPurchasedLounge(false);
			loungeAccessList.add(loungeAccessDTO);
		}
		
		// make sure first lounge is ahead of business lounge
		if (loungeAccessList.size() > 1) {			
			Collections.sort(loungeAccessList, (LoungeAccessDTO arg0, LoungeAccessDTO arg1) -> 
					 (arg1.getType().toString().compareTo(arg0.getType().toString()))); // reverse arguments because BLAC > FLAC;
		}
		return loungeAccessList;
	}
	
	/**
	 * @param eventBooking
	 * @return EventBookingDTO
	 */
	public EventBookingDTO covert2EventBookingDTO(OJEventBooking eventBooking) {
		if(eventBooking == null) {
			return null;
		}
		EventBookingDTO eventBookingDTO = new EventBookingDTO();
		eventBookingDTO.setDetails(covert2EventDTOs(eventBooking.getDetails()));
		return eventBookingDTO;
	}
	
	/**
	 * @param ojEvents
	 * @return List<EventDTO>
	 */
	private List<EventDTO> covert2EventDTOs(List<OJEvent> ojEvents) {
		if(CollectionUtils.isEmpty(ojEvents)) {
			return null;
		}
		List<EventDTO> eventDTOs = new ArrayList<>();
		for(OJEvent event : ojEvents) {
			EventDTO eventDTO = new EventDTO();

			eventDTO.setId(event.getId());
			eventDTO.setOrderDate(event.getOrderDate());

			eventDTO.setReference(event.getReference());
			eventDTO.setBookingReference(event.getBookingReference());
			eventDTO.setBookingStatus(event.getBookingStatus());
			eventDTO.setDate(event.getDate());
			eventDTO.setTime(event.getTime());
			eventDTO.setName(event.getName());
			eventDTO.setDescription(covert2DescriptionDTO(event.getDescription()));
			eventDTO.setGuests(covert2GuestDTOs(event.getGuests()));
			eventDTOs.add(eventDTO);
		}
		return eventDTOs;
	}
	
	/**
	 * @param description
	 * @return EventDescriptionDTO
	 */
	private EventDescriptionDTO covert2DescriptionDTO(OJEventDescription description) {
		if(description == null) {
			return null;
		}
		EventDescriptionDTO descriptionDTO = new EventDescriptionDTO();
		descriptionDTO.setAdditionalInstructions(description.getAdditionalInstructions());
		descriptionDTO.setEssential(description.getEssential());
		descriptionDTO.setFullName(description.getFullName());
		descriptionDTO.setInclusion(description.getInclusion());
		descriptionDTO.setOperatingDetails(description.getOperatingDetails());
		descriptionDTO.setShortName(description.getShortName());
		descriptionDTO.setSmall(description.getSmall());
		return descriptionDTO;
	}
	

	/**
	 * @param documents
	 * @return
	 */
	public List<DocumentDTO> covert2DocumentDTOs(List<OJDocument> documents) {
		if(CollectionUtils.isEmpty(documents)) {
			return null;
		}
		List<DocumentDTO> documentDTOs = new ArrayList<>();
		for(OJDocument document : documents) {
			DocumentDTO documentDTO = new DocumentDTO();
			documentDTO.setFormat(document.getFormat());
			documentDTO.setType(document.getType());
			documentDTO.setUrl(document.getUrl());
			documentDTOs.add(documentDTO);
		}
		return documentDTOs;
	}
	
	/**
	 * @param contact
	 * @return
	 */
	public ContactDetailsDTO covert2ContactDTOs(OJContactDetails contact) {
		if(contact == null || contact.getName() == null) {
			return null;
		}
		OJName name = contact.getName();

		ContactDetailsDTO contactDTO = new ContactDetailsDTO();
		NameDTO nameDTO = new NameDTO();
		nameDTO.setGivenName(name.getGivenName());
		nameDTO.setPrefix(name.getPrefix());
		nameDTO.setSurName(name.getSurName());
		contactDTO.setName(nameDTO);

		return contactDTO;
	}
	/**
	 * @param hotelBooking
	 * @return HotelBookingDTO
	 */
	public HotelBookingDTO covert2HotelBookingDTO(OJBooking ojBooking) {
		if(ojBooking == null || ojBooking.getHotelBooking() == null) {
			return null;
		}
		OJHotelBooking hotelBooking = ojBooking.getHotelBooking();
		HotelBookingDTO hotelBookingDTO = new HotelBookingDTO();
		hotelBookingDTO.setBookingStatus(ojBooking.getBookingStatus());
		hotelBookingDTO.setIsCompleted(hotelBooking.isCompleted());
		hotelBookingDTO.setIsStayed(hotelBooking.isStayed());
		convert2HotelDTOs(hotelBooking.getDetails(), hotelBookingDTO);
		return hotelBookingDTO;
	}
	
	/**
	 * @param ojHotels
	 * @param hotelBookingDTO
	 *
	 */
	private void convert2HotelDTOs(List<OJHotel> ojHotels, HotelBookingDTO hotelBookingDTO) {
		if(CollectionUtils.isEmpty(ojHotels)) {
			return;
		}
		List<HotelDTO> hotelDTOs = new ArrayList<>();
		int invaildCount = 0;
		for(OJHotel ojHotel : ojHotels) {
			HotelDTO hotelDTO = new HotelDTO();
			try {
				if(checkoutFourDaysAgo(ojHotel.getOutDate(), DateUtil.getGMTTime())) {
					invaildCount++;
					continue;
				}
			} catch(Exception e) {
				logger.warn("hotelBooking[bookingReference: %s] checkOutDate compare with now failure", ojHotel.getBookingReference(), e);
			}

			hotelDTO.setId(ojHotel.getId());
			hotelDTO.setOrderDate(ojHotel.getOrderDate());
			hotelDTO.setOutDate(ojHotel.getOutDate());

			hotelDTO.setBookingReference(ojHotel.getBookingReference());
			hotelDTO.setName(ojHotel.getName());
			hotelDTO.setUrlPath(ojHotel.getUrlPath());
			hotelDTO.setDescriptions(covert2DescriptionDTOs(ojHotel.getDescriptions()));
			hotelDTO.setAmenities(covert2AmenityDTOs(ojHotel.getAmenities()));
			hotelDTO.setPosition(covert2PositionDTO(ojHotel.getPosition()));
			hotelDTO.setRooms(covert2RoomDTOs(ojHotel.getRooms()));
			hotelDTO.setIsStayed(ojHotel.isStayed());
			hotelDTO.setIsCompleted(ojHotel.isCompleted());
			hotelDTO.setCheckInDate(ojHotel.getCheckInDate());
			hotelDTO.setCheckInTime(ojHotel.getCheckInTime());
			hotelDTO.setIsInCheckInTime(ojHotel.getIsInCheckInTime());
			hotelDTO.setCheckOutDate(ojHotel.getCheckOutDate());
			hotelDTO.setCheckOutTime(ojHotel.getCheckOutTime());
			hotelDTO.setBookingStatus(ojHotel.getBookingStatus());
			hotelDTO.setAdress(covert2Address(ojHotel.getAdress()));
			hotelDTO.setBookingDate(ojHotel.getBookingDate());
			hotelDTOs.add(hotelDTO);
		}

		if(invaildCount == ojHotels.size()) {
			hotelBookingDTO.setIsAllInVaild(true);
		}
		hotelBookingDTO.setDetails(hotelDTOs);
	}
	/**
	 * @param address
	 * @return AdressDTO
	 */
	private AdressDTO covert2Address(OJAdress address) {
		if(address == null) {
			return null;
		}
		AdressDTO addressDTO = new AdressDTO();
		addressDTO.setCompressed(address.getCompressed());
		return addressDTO;
	}

	/**
	 * @param rooms
	 * @return List<HotelRoomDTO>
	 */
	private List<HotelRoomDTO> covert2RoomDTOs(List<OJHotelRoom> rooms) {
		if(CollectionUtils.isEmpty(rooms)) {
			return null;
		}
		List<HotelRoomDTO> hotelRoomDTOs = new ArrayList<>();
		for(OJHotelRoom room : rooms) {
			HotelRoomDTO roomDTO = new HotelRoomDTO();
			roomDTO.setDuration(room.getDuration());
			roomDTO.setCheckInDate(room.getCheckInDate());
			roomDTO.setCheckInDay(room.getCheckInDay());
			roomDTO.setCheckInTime(room.getCheckInTime());
			roomDTO.setCheckOutDate(room.getCheckOutDate());
			roomDTO.setCheckOutDay(room.getCheckOutDay());
			roomDTO.setCheckOutTime(room.getCheckOutTime());
			roomDTO.setRoomOptions(covert2RoomOptionDTOs(room.getRoomOptions()));
			roomDTO.setAdults(room.getAdults());
			roomDTO.setInfants(room.getInfants());
			roomDTO.setChildren(room.getChildren());
			roomDTO.setIsStayed(room.isStayed());
			roomDTO.setIsCompleted(room.isCompleted());
			roomDTO.setBookingReference(room.getBookingReference());
			roomDTO.setBookingStatus(room.getBookingStatus());
			hotelRoomDTOs.add(roomDTO);
		}
		return hotelRoomDTOs;
	}

	/**
	 * @param options
	 * @return List<HotelRoomOptionDTO>
	 */
	private List<HotelRoomOptionDTO> covert2RoomOptionDTOs(List<OJHotelRoomOption> options) {
		if(CollectionUtils.isEmpty(options)) {
			return null;
		}
		List<HotelRoomOptionDTO> optionDTOs = new ArrayList<>();
		for(OJHotelRoomOption option : options) {
			HotelRoomOptionDTO optionDTO = new HotelRoomOptionDTO();
			optionDTO.setSelectedBedType(option.getSelectedBedType());
			optionDTO.setCode(option.getCode());
			optionDTO.setName(option.getName());
			optionDTO.setDescription(option.getDescription());
			optionDTO.setSmokingPreference(option.getSmokingPreference());
			optionDTO.setGuestDetails(covert2GuestDetailDTOs(option.getGuestDetails()));
			optionDTOs.add(optionDTO);
		}
		return optionDTOs;
	}

	/**
	 * @param guests
	 * @return List<RoomGuestDetailDTO>
	 */
	private List<RoomGuestDetailDTO> covert2GuestDetailDTOs(List<OJRoomGuestDetail> guests) {
		if(CollectionUtils.isEmpty(guests)) {
			return null;
		}
		List<RoomGuestDetailDTO> guestDTOs = new ArrayList<>();
		for(OJRoomGuestDetail guest : guests) {
			RoomGuestDetailDTO guestDTO = new RoomGuestDetailDTO();
			guestDTO.setGivenName(guest.getGivenName());
			guestDTO.setMiddleName(guest.getMiddleName());
			guestDTO.setSurname(guest.getSurname());
			guestDTO.setPaxType(guest.getPaxType());
			guestDTO.setPrefix(guest.getPrefix());
			guestDTOs.add(guestDTO);
		}
		return guestDTOs;
	}

	/**
	 * @param guests
	 * @return List<EventGuestDTO>
	 */
	private List<EventGuestDTO> covert2GuestDTOs(List<OJEventGuest> guests) {
		if(CollectionUtils.isEmpty(guests)) {
			return null;
		}
		List<EventGuestDTO> guestDTOs = new ArrayList<>();
		for(OJEventGuest guest : guests) {
			EventGuestDTO guestDTO = new EventGuestDTO();
			guestDTO.setGivenName(guest.getGivenName());
			guestDTO.setPrefix(guest.getPrefix());
			guestDTO.setRph(guest.getRph());
			guestDTO.setSurName(guest.getSurName());
			guestDTO.setTicketCategory(guest.getTicketCategory());
			guestDTO.setType(guest.getType());
			guestDTOs.add(guestDTO);
		}
		return guestDTOs;
	}

	/**
	 * @param position
	 * @return HotelPositionDTO
	 */
	private HotelPositionDTO covert2PositionDTO(OJHotelPosition position) {
		if(position == null) {
			return null;
		}
		HotelPositionDTO positionDTO = new HotelPositionDTO();
		positionDTO.setAddress(position.getAddress());
		positionDTO.setCity(position.getCity());
		positionDTO.setLatitude(position.getLatitude());
		positionDTO.setLongitude(position.getLongitude());
		return positionDTO;
	}

	/**
	 * @param amenities
	 * @return List<HotelAmenityDTO>
	 */
	private List<HotelAmenityDTO> covert2AmenityDTOs(List<OJHotelAmenity> amenities) {
		if(CollectionUtils.isEmpty(amenities)) {
			return null;
		}
		List<HotelAmenityDTO> amenityDTOs = new ArrayList<>();
		for(OJHotelAmenity amenity : amenities) {
			HotelAmenityDTO amenityDTO = new HotelAmenityDTO();
			amenityDTO.setCode(amenity.getCode());
			amenityDTOs.add(amenityDTO);
		}
		return amenityDTOs;
	}

	/**
	 * @param descriptions
	 * @return List<HotelDescriptionDTO>
	 */
	private List<HotelDescriptionDTO> covert2DescriptionDTOs(List<OJHotelDescription> descriptions) {
		if(CollectionUtils.isEmpty(descriptions)) {
			return null;
		}
		List<HotelDescriptionDTO> descriptionDTOs = new ArrayList<>();
		for(OJHotelDescription description : descriptions) {
			HotelDescriptionDTO descriptionDTO = new HotelDescriptionDTO();
			descriptionDTO.setCode(description.getCode());
			descriptionDTO.setText(description.getText());
			descriptionDTOs.add(descriptionDTO);
		}
		return descriptionDTOs;
	}
	/**
	 * @Description check if the booking is checked out 4 days ago
	 * @param checkoutDate
	 * @param now
	 * @return
	 */
	private boolean checkoutFourDaysAgo(Date checkoutDate, Date now) {
		long day = (now.getTime() - checkoutDate.getTime())/(24*60*60*1000);
		return day > 4;
	}
}