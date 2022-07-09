package com.cathaypacific.mmbbizrule.v2.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cathaypacific.olciconsumer.model.response.db.TravelDocDisplay;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.booking.LoungeClass;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
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
import com.cathaypacific.mmbbizrule.dto.common.booking.ContactDetailsDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.DocumentDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.EventBookingDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.EventDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.EventDescriptionDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.EventGuestDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.HotelAmenityDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.HotelBookingDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.HotelDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.HotelDescriptionDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.HotelPositionDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.HotelRoomDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.HotelRoomOptionDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.NameDTO;
import com.cathaypacific.mmbbizrule.dto.common.booking.RoomGuestDetailDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingcustomizedinfo.ClaimedLoungeDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.AirportUpgradeInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.AtciCancelledSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.ClaimedLounge;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneyPassenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneyPassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.CprJourneySegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.FQTVInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Journey;
import com.cathaypacific.mmbbizrule.model.booking.detail.MealDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.PurchasedLounge;
import com.cathaypacific.mmbbizrule.model.booking.detail.RebookMapping;
import com.cathaypacific.mmbbizrule.model.booking.detail.RtfsFlightSummary;
import com.cathaypacific.mmbbizrule.model.booking.detail.RtfsLegSummary;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatPreference;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatSelection;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SegmentStatus;
import com.cathaypacific.mmbbizrule.model.booking.detail.SpecialSeatEligibility;
import com.cathaypacific.mmbbizrule.model.booking.detail.TravelDoc;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPaymentInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRebookInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrTicketPriceInfo;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.AirportUpgradeInfoDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.AtciCancelledSegmentDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.CprJourneyPassengerDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.CprJourneyPassengerSegmentDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.CprJourneySegmentDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.DepartureArrivalTimeDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.DobDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.FQTVInfoDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.FlightBookingDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.JourneyDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.KtnRedressDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.LoungeAccessDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.MealDetailDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.MealSelectionDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.PassengerCheckInMandatoryDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.PassengerDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.PassengerDisplayDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.PassengerSegmentDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.PaymentInfoDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.RebookInfoDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.RebookMappingDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.RtfsFlightSummaryDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.RtfsLegSummaryDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.SeatDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.SeatSelectionDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.SegmentCheckInMandatoryDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.SegmentDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.SegmentDisplayDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.TicketPriceInfoDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.TravelDocDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.common.booking.TravelDocsDTOV2;
import com.google.common.collect.Lists;

@Component
public class DTOConverterV2 {
	
	private static LogAgent logger = LogAgent.getLogAgent(DTOConverterV2.class);
	
	@Autowired
	TbTravelDocDisplayCacheHelper tbTravelDocDisplayCacheHelper;
	
	@Autowired
	TbPortFlightDAO tbPortFlightDAO;
	
	@Autowired
	BizRuleConfig bizRuleConfig;
	
	@Autowired
	AEMService aemService;
	
	public FlightBookingDTOV2 convertToBookingDTO(Booking booking, LoginInfo loginInfo) throws ExpectedException {
		if(null == booking){
			return null;
		}
		FlightBookingDTOV2 bookingDTO = new FlightBookingDTOV2();
		bookingDTO.setDisplayRloc(booking.getDisplayRloc());
		bookingDTO.setOneARloc(booking.getOneARloc());
		bookingDTO.setGdsRloc(booking.getGdsRloc());
		bookingDTO.setOfficeId(booking.getOfficeId());
		bookingDTO.setSpnr(booking.getSpnr());
		bookingDTO.setPos(booking.getPos());
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
		setPassengerDisplayAndMandatory(bookingDTO, booking);
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
			List<TicketPriceInfoDTOV2> ticketPriceInfoDTOs = Lists.newArrayList();
			for (RetrievePnrTicketPriceInfo retrievePnrTicketPriceInfo: booking.getTicketPriceInfo()) {
				TicketPriceInfoDTOV2 ticketPriceInfoDTO = new TicketPriceInfoDTOV2();
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
		bookingDTO.setRebookMapping(getRebookMapping(booking.getRebookMapping(), booking.getAtciRebookMapping()));
		bookingDTO.setIssueSpecialServices(booking.isIssueSpecialServices());
		bookingDTO.setTempLinkedBooking(booking.isTempLinkedBooking());
		setJourneyDTOs(bookingDTO, booking);
		bookingDTO.setGotPnr(booking.isGotPnr());
		bookingDTO.setGotCpr(booking.isGotCpr());
		bookingDTO.setCanCheckIn(booking.getCanCheckIn());
		bookingDTO.setMiceBooking(booking.isMiceBooking());
		bookingDTO.setMergedRlocs(booking.getMergedRlocs());
		repopulateTravelDocumentGender(bookingDTO, booking);
		if(!CollectionUtils.isEmpty(booking.getCprErrors())){
			bookingDTO.setCprErrors(booking.getCprErrors());
		}
		bookingDTO.setFreeRebooking(booking.isFrbkSK());	
		bookingDTO.setAtciCancelledSegments(converToAtciCancelledSegmentDTOV2s(booking.getAtciCancelledSegments()));
		
		return bookingDTO;
	}

	/**
	 * get cancelled segments
	 * @param atciCancelledSegments
	 * @return List<CancelledSegmentDTOV2>
	 */
	private List<AtciCancelledSegmentDTOV2> converToAtciCancelledSegmentDTOV2s(List<AtciCancelledSegment> atciCancelledSegments) {
		if (atciCancelledSegments == null) {
			return null;
		}
		List<AtciCancelledSegmentDTOV2> atciCancelledSegmentDTOs = new ArrayList<>();
		for (AtciCancelledSegment atciCancelledSegment : atciCancelledSegments) {
			AtciCancelledSegmentDTOV2 atciCancelledSegmentDTO = new AtciCancelledSegmentDTOV2();
			atciCancelledSegmentDTO.setSegmentId(atciCancelledSegment.getSegmentId());
			atciCancelledSegmentDTO.setOperateCompany(atciCancelledSegment.getOperateCompany());
			atciCancelledSegmentDTO.setOperateSegmentNumber(atciCancelledSegment.getOperateSegmentNumber());
			atciCancelledSegmentDTO.setOriginPort(atciCancelledSegment.getOriginPort());
			atciCancelledSegmentDTO.setDestPort(atciCancelledSegment.getDestPort());
			atciCancelledSegmentDTO.setDepartureDate(atciCancelledSegment.getDepartureDate());

			if (atciCancelledSegment.getRebookInfo() != null) {
				RebookInfoDTOV2 rebookInfoDTO = new RebookInfoDTOV2();
				rebookInfoDTO.setRebooked(atciCancelledSegment.getRebookInfo().isRebooked());
				rebookInfoDTO.setAccepted(atciCancelledSegment.getRebookInfo().isAccepted());
				rebookInfoDTO.setAcceptFamilyName(atciCancelledSegment.getRebookInfo().getAcceptFamilyName());
				rebookInfoDTO.setAcceptGivenName(atciCancelledSegment.getRebookInfo().getAcceptGivenName());
				rebookInfoDTO.setNewBookedSegmentIds(atciCancelledSegment.getRebookInfo().getNewBookedSegmentIds());
				atciCancelledSegmentDTO.setRebookInfo(rebookInfoDTO);
			}
			atciCancelledSegmentDTOs.add(atciCancelledSegmentDTO);
		}
		
		return atciCancelledSegmentDTOs;
	}

	/**
	 * Re-populate travelDocument gender by cprGender under passenger in CPR
	 * 
	 * @param bookingDTO
	 * @param booking 
	 */
	private void repopulateTravelDocumentGender(FlightBookingDTOV2 bookingDTO, Booking booking) {
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
			
			for(PassengerSegmentDTOV2 passengerSegmentDTOV2 : bookingDTO.getPassengerSegments()) {
				if(passengerSegmentDTOV2 != null && passenger.getPassengerId().equals(passengerSegmentDTOV2.getPassengerId())) {
					passengerSegmentDTOV2.repopulateTravelDocumentGender(passenger.getCprGender());
				}
			}
		}
	}

	/**
	 * set display & mandatory in passenger 
	 * @param bookingDTO
	 * @param booking
	 */
	private void setPassengerDisplayAndMandatory(FlightBookingDTOV2 bookingDTO, Booking booking) {
		if (CollectionUtils.isEmpty(bookingDTO.getPassengers()) || CollectionUtils.isEmpty(bookingDTO.getSegments())
				|| CollectionUtils.isEmpty(booking.getPassengerSegments())) {
			return;
		}
		
		for (PassengerDTOV2 passengerDTO : bookingDTO.getPassengers()) {
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
			List<SegmentDTOV2> segmentDTOs = new ArrayList<>();
			for (SegmentDTOV2 segmentDTO : bookingDTO.getSegments()) {
				if (segmentIds.contains(segmentDTO.getSegmentId())) {
					segmentDTOs.add(segmentDTO);
				}
			}
			
			PassengerDisplayDTOV2 displayDTO = new PassengerDisplayDTOV2();	
			PassengerCheckInMandatoryDTOV2 mandatoryDTO = new PassengerCheckInMandatoryDTOV2();
			// set EC
			displayDTO.setEc(anySegmentShouldDisplay(segmentDTOs, OneAConstants.DISPLAY_GROUP_EC));
			mandatoryDTO.setEc(anySegmentShouldMandatory(segmentDTOs, OneAConstants.DISPLAY_GROUP_EC));
			// set DA
			displayDTO.setDa(anySegmentShouldDisplay(segmentDTOs, OneAConstants.DISPLAY_GROUP_DA));
			mandatoryDTO.setDa(anySegmentShouldMandatory(segmentDTOs, OneAConstants.DISPLAY_GROUP_DA));
			// set KN
			displayDTO.setKn(anySegmentShouldDisplay(segmentDTOs, OneAConstants.DISPLAY_GROUP_KN));
			mandatoryDTO.setKn(anySegmentShouldMandatory(segmentDTOs, OneAConstants.DISPLAY_GROUP_KN));
			// set RN
			displayDTO.setRn(anySegmentShouldDisplay(segmentDTOs, OneAConstants.DISPLAY_GROUP_RN));
			mandatoryDTO.setRn(anySegmentShouldMandatory(segmentDTOs, OneAConstants.DISPLAY_GROUP_RN));
			// set PI
			displayDTO.setPi(anySegmentShouldDisplay(segmentDTOs, OneAConstants.DISPLAY_GROUP_PI));
			mandatoryDTO.setPi(anySegmentShouldMandatory(segmentDTOs, OneAConstants.DISPLAY_GROUP_PI));
			// set EI
			displayDTO.setEi(anySegmentShouldDisplay(segmentDTOs, OneAConstants.DISPLAY_GROUP_EI));
			mandatoryDTO.setEi(anySegmentShouldMandatory(segmentDTOs, OneAConstants.DISPLAY_GROUP_EI));
			passengerDTO.setDisplay(displayDTO);
			passengerDTO.setMandatory(mandatoryDTO);
		}
	
	}

	/**
	 * check if any segment should display the displayGroup
	 * @param segmentDTOs
	 * @param displayGroupEc
	 * @return
	 */
	private boolean anySegmentShouldDisplay(List<SegmentDTOV2> segmentDTOs, String displayGroup) {
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
	
	/**
	 * check if any Segment Bundle Should Mandatory
	 * @param segmentDTOs
	 * @param displayGroupEc
	 * @return
	 */
	private boolean anySegmentShouldMandatory(List<SegmentDTOV2> segmentDTOs, String mandatoryGroup) {
		if (CollectionUtils.isEmpty(segmentDTOs) || StringUtils.isEmpty(mandatoryGroup)) {
			return false;
		}
		if (OneAConstants.DISPLAY_GROUP_EC.equals(mandatoryGroup)) {
			return segmentDTOs.stream().anyMatch(seg -> seg.getMandatory() != null && BooleanUtils.isTrue(seg.getMandatory().getEc()));
		} else if (OneAConstants.DISPLAY_GROUP_DA.equals(mandatoryGroup)) {
			return segmentDTOs.stream().anyMatch(seg -> seg.getMandatory() != null && BooleanUtils.isTrue(seg.getMandatory().getDa()));
		} else if (OneAConstants.DISPLAY_GROUP_KN.equals(mandatoryGroup)) {
			return segmentDTOs.stream().anyMatch(seg -> seg.getMandatory() != null && BooleanUtils.isTrue(seg.getMandatory().getKn()));
		} else if (OneAConstants.DISPLAY_GROUP_RN.equals(mandatoryGroup)) {
			return segmentDTOs.stream().anyMatch(seg -> seg.getMandatory() != null && BooleanUtils.isTrue(seg.getMandatory().getRn()));
		}else if (OneAConstants.DISPLAY_GROUP_PI.equals(mandatoryGroup)) {
			return segmentDTOs.stream().anyMatch(seg -> seg.getMandatory() != null && BooleanUtils.isTrue(seg.getMandatory().getPi()));
		} else if (OneAConstants.DISPLAY_GROUP_EI.equals(mandatoryGroup)) {
			return segmentDTOs.stream().anyMatch(seg -> seg.getMandatory() != null && BooleanUtils.isTrue(seg.getMandatory().getEi()));
		}
		return false;
	}

	/**
	 * get rebookMapping
	 * @param rebookMappings
	 * @param atciRebookMappings
	 * @return
	 */
	private List<RebookMappingDTOV2> getRebookMapping(List<RebookMapping> rebookMappings, List<RebookMapping> atciRebookMappings) {
		if(CollectionUtils.isEmpty(rebookMappings) && CollectionUtils.isEmpty(atciRebookMappings)) {
			return null;
		}
		List<RebookMappingDTOV2> rebookMappingDTOs = new ArrayList<>();
		if (!CollectionUtils.isEmpty(rebookMappings)) {
			for(RebookMapping rebookMapping : rebookMappings) {
				RebookMappingDTOV2 rebookMappingDTO = new RebookMappingDTOV2();
				rebookMappingDTO.setAcceptSegmentIds(rebookMapping.getAcceptSegmentIds());
				rebookMappingDTO.setCancelledSegmentIds(rebookMapping.getCancelledSegmentIds());
				rebookMappingDTOs.add(rebookMappingDTO);
			}
		}
		
		if (!CollectionUtils.isEmpty(atciRebookMappings)) {
			for(RebookMapping atciRebookMapping : atciRebookMappings) {
				RebookMappingDTOV2 rebookMappingDTO = new RebookMappingDTOV2();
				rebookMappingDTO.setAcceptSegmentIds(atciRebookMapping.getAcceptSegmentIds());
				rebookMappingDTO.setAtciCancelledSegmentIds(atciRebookMapping.getCancelledSegmentIds());
				rebookMappingDTOs.add(rebookMappingDTO);
			}
		}

		return rebookMappingDTOs;
	}

	private void setPassenger(FlightBookingDTOV2 bookingDTO, Booking booking) throws ExpectedException{
		List<Passenger> passengers = booking.getPassengers();
		List<PassengerDTOV2> passengerDTOs = bookingDTO.findPassengers();
		for(Passenger passenger : passengers){
		    if(passenger.isGrmc()) {
		        throw new ExpectedException(String.format("GRMC booking:%s", bookingDTO.getDisplayRloc()),
	                    new ErrorInfo(ErrorCodeEnum.ERR_MICE_BOOKING_GRMC_DENY));
		    }
		    if(passenger.isTickedUnissued()) {
		        throw new ExpectedException(
	                    String.format("GRMA/GRMB without issue ticket, booking:%s", bookingDTO.getDisplayRloc()),
	                    new ErrorInfo(ErrorCodeEnum.ERR_MICE_BOOKING_WITHOUT_TICKET));
		    }
			PassengerDTOV2 passengerDTO = new PassengerDTOV2();
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
			passengerDTO.setLinkedRloc(passenger.getLinkedRloc());
			
			if(BooleanUtils.isTrue(passenger.isCompanion()) && BooleanUtils.isTrue(passenger.isPrimaryPassenger())) {
				bookingDTO.setProfileCompanionBooking(true);
			}
			// DOB
			if (passenger.getDob() != null) {
				DobDTOV2 dobDTO = new DobDTOV2();
				dobDTO.setBirthDateDay(passenger.getDob().getBirthDateDay());
				dobDTO.setBirthDateMonth(passenger.getDob().getBirthDateMonth());
				dobDTO.setBirthDateYear(passenger.getDob().getBirthDateYear());
				passengerDTO.setDob(dobDTO);
			}
			
			// KTN 
			if (passenger.getKtn() != null && !StringUtils.isEmpty(passenger.getKtn().getNumber())) {
				KtnRedressDTOV2 ktn = new KtnRedressDTOV2();
				ktn.setNumber(passenger.getKtn().getNumber());
				ktn.setQualifierId(passenger.getKtn().getQualifierId());
				passengerDTO.setKtn(ktn);
			}
			
			// redress number
			if (passenger.getRedress() != null && !StringUtils.isEmpty(passenger.getRedress().getNumber())) {
				KtnRedressDTOV2 redress = new KtnRedressDTOV2();
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
					passengerDTO.findContactInfo().findPhoneInfo().setIsValid(passenger.findContactInfo().findPhoneInfo().getIsValid());
				}

			}
			
			//EmrContact info
			if(passenger.getEmrContactInfo() != null && !passenger.getEmrContactInfo().isEmpty()) {
				passengerDTO.findEmrContactInfo().setCountryCode(passenger.findEmrContactInfo().getCountryCode());
				passengerDTO.findEmrContactInfo().setName(passenger.findEmrContactInfo().getName());
				passengerDTO.findEmrContactInfo().setPhoneNumber(passenger.findEmrContactInfo().getPhoneNumber());
				passengerDTO.findEmrContactInfo().setPhoneCountryNumber(passenger.findEmrContactInfo().getPhoneCountryNumber());
				passengerDTO.findEmrContactInfo().setIsValid(passenger.findEmrContactInfo().getIsValid());
			}
				
			//Destination address
			if(passenger.getDesAddress() != null && !passenger.getDesAddress().isEmpty()) {
				passengerDTO.findDestinationAddress().setCity(passenger.findDesAddress().getCity());
				passengerDTO.findDestinationAddress().setStateCode(passenger.findDesAddress().getStateCode());
				passengerDTO.findDestinationAddress().setStreet(passenger.findDesAddress().getStreet());
				passengerDTO.findDestinationAddress().setZipCode(passenger.findDesAddress().getZipCode());
			}
			passengerDTO.setDestinationTransit(passenger.isDesTransit());

			passengerDTOs.add(passengerDTO);
		}
	}




	private void setSegment(FlightBookingDTOV2 bookingDTO, Booking booking){
		List<Segment> segments = booking.getSegments();
		List<SegmentDTOV2> segmentDTOs = bookingDTO.findSegments();
		List<TravelDocDisplay> tbTravelDocDisplays = tbTravelDocDisplayCacheHelper.findAll();
		List<TbPortFlight> tbPortFlights = tbPortFlightDAO.findByAppCode(MMBConstants.APP_CODE);
		for(Segment segment : segments) {
			SegmentDTOV2 segmentDTO = new SegmentDTOV2();
			segmentDTO.setUpgradeInfo(segment.getUpgradeInfo());
			segmentDTO.setRtfsFlightStatusInfo(segment.getRtfsFlightStatusInfo());
			segmentDTO.setRtfsSummary(convertRtfsFlightDTO(segment.getRtfsSummary()));
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
			segmentDTO.setStatus(Optional.ofNullable(segment.getSegmentStatus()).orElse(new SegmentStatus()).getStatus());
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
			DepartureArrivalTimeDTOV2 arrivalTimeDTO = segmentDTO.findArrivalTime();
		
			arrivalTimeDTO.setRtfsActualTime(segment.findArrivalTime().getRtfsActualTime());
			arrivalTimeDTO.setRtfsEstimatedTime(segment.findArrivalTime().getRtfsEstimatedTime());
			arrivalTimeDTO.setRtfsScheduledTime(segment.findArrivalTime().getRtfsScheduledTime());
			arrivalTimeDTO.setTimeZoneOffset(segment.findArrivalTime().getTimeZoneOffset());
			arrivalTimeDTO.setPnrTime(segment.findArrivalTime().getPnrTime());
			
			DepartureArrivalTimeDTOV2 departureTimeDTO = segmentDTO.findDepartureTime();
			departureTimeDTO.setRtfsActualTime(segment.findDepartureTime().getRtfsActualTime());
			departureTimeDTO.setRtfsEstimatedTime(segment.findDepartureTime().getRtfsEstimatedTime());
			departureTimeDTO.setRtfsScheduledTime(segment.findDepartureTime().getRtfsScheduledTime());
			departureTimeDTO.setTimeZoneOffset(segment.findDepartureTime().getTimeZoneOffset());
			departureTimeDTO.setPnrTime(segment.findDepartureTime().getPnrTime());
			
			segmentDTO.setApiVersion(segment.getApiVersion());
			
			segmentDTO.setTrainCase(segment.getTrainCase());
			//Display & CheckIn Mandatory
			setDisplayAndCheckInMandatory(tbTravelDocDisplays, tbPortFlights, segment, segmentDTO, booking.isIDBooking());
			
			//re-book information
			RetrievePnrRebookInfo rebookInfo = segment.getRebookInfo();
			if(rebookInfo != null) {
				RebookInfoDTOV2 rebookInfoDTO = new RebookInfoDTOV2();
				rebookInfoDTO.setRebooked(rebookInfo.isRebooked());
				rebookInfoDTO.setNewBookedSegmentIds(rebookInfo.getNewBookedSegmentIds());
				rebookInfoDTO.setAccepted(rebookInfo.isAccepted());
				rebookInfoDTO.setAcceptFamilyName(rebookInfo.getAcceptFamilyName());
				rebookInfoDTO.setAcceptGivenName(rebookInfo.getAcceptGivenName());
				segmentDTO.setRebookInfo(rebookInfoDTO);
			}

			segmentDTO.setOpenToCheckIn(segment.isOpenToCheckIn());
			segmentDTO.setPostCheckIn(segment.isPostCheckIn());
			segmentDTO.setWithinTwentyFourHrs(segment.isWithinTwentyFourHrs());
			segmentDTO.setWithinNinetyMins(segment.isWithinNinetyMins());
			segmentDTO.setCheckInRemainingTime(segment.getCheckInRemainingTime());
			segmentDTO.setHasCheckedBaggage(segment.isHasCheckedBaggge());
			segmentDTO.setHasAvailableWifi(segment.isHasAvailableWifi());
			segmentDTO.setGateNumber(segment.getGateNumber());
			segmentDTOs.add(segmentDTO);
		}
		
	}

	private void setPassengerSegment(FlightBookingDTOV2 bookingDTO, Booking booking){
		List<PassengerSegment> passengerSegments = booking.getPassengerSegments();
		List<Passenger> passengers = booking.getPassengers();
		List<PassengerSegmentDTOV2> passengerSegmentDTOs = bookingDTO.findPassengerSegments();
		for(PassengerSegment passengerSegment : passengerSegments) {
			PassengerSegmentDTOV2 passengerSegmentDTO = new PassengerSegmentDTOV2();
			
			//FQTV Info
			FQTVInfo fqtvInfo = passengerSegment.getFqtvInfo();
			FQTVInfoDTOV2 fqtvInfoDTO = passengerSegmentDTO.findFQTVInfo();
			if(fqtvInfo != null && !StringUtils.isEmpty(fqtvInfo.getMembershipNumber())){
				fqtvInfoDTO.setCompanyId(fqtvInfo.getCompanyId());
				fqtvInfoDTO.setMembershipNumber(fqtvInfo.getMembershipNumber());
				fqtvInfoDTO.setTierLevel(fqtvInfo.getTierLevel());
				fqtvInfoDTO.setTopTier(fqtvInfo.isTopTier());
				fqtvInfoDTO.setProductLevel(fqtvInfo.isProductLevel());
				fqtvInfoDTO.setIsAM(fqtvInfo.getAm());
				fqtvInfoDTO.setIsMPO(fqtvInfo.getMpo());
				fqtvInfoDTO.setIsOneWorld(fqtvInfo.getOneWorld());
			}
						
			//Travel Documents & KTN, redress
			TravelDoc priTravelDoc = passengerSegment.getPriTravelDoc();
			TravelDoc secTravelDoc = passengerSegment.getSecTravelDoc();
			
			TravelDoc pPriTravelDoc = null;
			TravelDoc pSecTravelDoc = null;
			if (!checkPassengerSegmentInJourneys(passengerSegment.getPassengerId(),passengerSegment.getSegmentId(),booking)) {
				Passenger passenger = getPassengerById(passengers, passengerSegment.getPassengerId());
				pPriTravelDoc = passenger.getPriTravelDocs().stream().filter(travelDoc -> travelDoc != null).findFirst().orElse(null);
				pSecTravelDoc = passenger.getSecTravelDocs().stream().filter(travelDoc -> travelDoc != null).findFirst().orElse(null);
			}
			
			if (priTravelDoc != null || secTravelDoc != null || pPriTravelDoc != null || pSecTravelDoc != null) {
				TravelDocsDTOV2 travelDoc = new TravelDocsDTOV2();
				
				//Primary Travel Document
				if(priTravelDoc != null) {
					TravelDocDTOV2 travelDocDTO = travelDoc.findPriTravelDoc();
					setTravelDocDTO(priTravelDoc, travelDocDTO, true, false);
				} else {
					if(pPriTravelDoc != null) {
						TravelDocDTOV2 travelDocDTO = travelDoc.findPriTravelDoc();
						setTravelDocDTO(pPriTravelDoc, travelDocDTO, false, false);						
					}
				}
				//Secondary Travel Document
				if(secTravelDoc != null) {
					TravelDocDTOV2 travelDocDTO = travelDoc.findSecTravelDoc();
					setTravelDocDTO(secTravelDoc, travelDocDTO, true, true);
				} else {
					if(pSecTravelDoc != null) {
						TravelDocDTOV2 travelDocDTO = travelDoc.findSecTravelDoc();
						setTravelDocDTO(pSecTravelDoc, travelDocDTO, false, true);						
					}
				}
				
				if(!travelDoc.isEmpty()) {
					passengerSegmentDTO.setTravelDoc(travelDoc);
				}
			}
			
			// country of residence
			if (StringUtils.isNotEmpty(passengerSegment.getCountryOfResidence())) {
				passengerSegmentDTO.setCountryOfResidence(passengerSegment.getCountryOfResidence());
			} else {
				Passenger passenger = getPassengerById(passengers, passengerSegment.getPassengerId());
				if (StringUtils.isNotEmpty(passenger.getCountryOfResidence())) {
					passengerSegmentDTO.setCountryOfResidence(passenger.getCountryOfResidence());
				}
			}
			
			// Set meal
			MealDetail mealDetail = passengerSegment.getMeal();
			if(mealDetail != null) {
				MealDetailDTOV2 mealDetailDTO = new MealDetailDTOV2();
				mealDetailDTO.setCompanyId(mealDetail.getCompanyId());
				mealDetailDTO.setMealCode(mealDetail.getMealCode());
				mealDetailDTO.setQuantity(mealDetail.getQuantity());
				mealDetailDTO.setStatus(mealDetail.getStatus());
				mealDetailDTO.setDishName(mealDetail.getDishName());
				mealDetailDTO.setTag(mealDetail.getTag());
                mealDetailDTO.setPreSelectedMeal(mealDetail.isPreSelectedMeal());
				passengerSegmentDTO.setMeal(mealDetailDTO);
			}
			
			passengerSegmentDTO.setPassengerId(passengerSegment.getPassengerId());
			
			//seat selection
			SeatSelection seatSelection;
			// if within OLCI, then set OLCI seat selection, else set MMB seat selection
			if (withinOlci(passengerSegment.getSegmentId(), booking.getCprJourneys(), booking.getSegments())) {
				seatSelection = passengerSegment.getOlciSeatSelection();
			} else {
				seatSelection = passengerSegment.getMmbSeatSelection();
			}
			
			if (seatSelection != null) {				
				SeatSelectionDTOV2 seatSelectionDTO = new SeatSelectionDTOV2();
				seatSelectionDTO.setEligible(seatSelection.isEligible());
				seatSelectionDTO.setLowRBD(seatSelection.isLowRBD());
				seatSelectionDTO.setDisabilities(seatSelection.getDisabilities());
				seatSelectionDTO.setPaidASR(seatSelection.isPaidASR());
				seatSelectionDTO.setIneligibleReason(seatSelection.getIneligibleReason());
				seatSelectionDTO.setIsSeatPreferenceEligible(seatSelection.getIsSeatPreferenceEligible());
				if(BooleanUtils.isTrue(seatSelection.isEligible())) {
					seatSelectionDTO.setAsrFOC(seatSelection.isAsrFOC());
					seatSelectionDTO.setXlFOC(BooleanUtils.isTrue(seatSelection.isXlFOC()));
					seatSelectionDTO.setXlFOCReason(seatSelection.getXlFOCReason());
				}
	
				//set special seat eligibility
				SpecialSeatEligibility specialSeatEligibility = seatSelection.getSpecialSeatEligibility();
				if(specialSeatEligibility != null){
					seatSelectionDTO.findSpecialSeatEligibility().setExlSeat(specialSeatEligibility.getExlSeat());
					seatSelectionDTO.findSpecialSeatEligibility().setAsrSeat(specialSeatEligibility.getAsrSeat());
					seatSelectionDTO.findSpecialSeatEligibility().setExitRowSeat(specialSeatEligibility.getExitRowSeat());
					seatSelectionDTO.findSpecialSeatEligibility().setBcstSeat(specialSeatEligibility.getBcstSeat());
					seatSelectionDTO.findSpecialSeatEligibility().setUmSeat(specialSeatEligibility.getUmSeat());
				}
				passengerSegmentDTO.setSeatSelection(seatSelectionDTO);
			}
			
			boolean checkedIn = false;
			if (!CollectionUtils.isEmpty(booking.getCprJourneys())) {
				// find CPR journey
				Journey cprJourney = booking.getCprJourneys().stream().filter(journey -> !CollectionUtils.isEmpty(journey.getPassengerSegments()) && journey.getPassengerSegments().stream()
						.anyMatch(ps -> Objects.equals(passengerSegment.getPassengerId(), ps.getPassengerId()) && Objects.equals(passengerSegment.getSegmentId(), ps.getSegmentId()))).findFirst().orElse(null);
			
				// find CPR passengerSegment
				CprJourneyPassengerSegment cprPassengerSegment = cprJourney == null ? null
						: cprJourney.getPassengerSegments().stream()
								.filter(ps -> Objects.equals(passengerSegment.getPassengerId(), ps.getPassengerId())
										&& Objects.equals(passengerSegment.getSegmentId(), ps.getSegmentId()))
								.findFirst().orElse(null);
				
				checkedIn = cprPassengerSegment != null && cprPassengerSegment.getCheckedIn();
			}
			
			//seat and extra seat
			SeatDetail seatDetail = passengerSegment.getSeat();
			if(seatDetail != null){
				if(passengerSegmentDTO.getSeat() == null){
					passengerSegmentDTO.setSeat(new SeatDTOV2());
				}
				passengerSegmentDTO.getSeat().setSeatNo(seatDetail.getSeatNo());
				passengerSegmentDTO.getSeat().setExlSeat(seatDetail.isExlSeat());
				passengerSegmentDTO.getSeat().setAsrSeat(seatDetail.isAsrSeat());
				passengerSegmentDTO.getSeat().setWindowSeat(seatDetail.isWindowSeat());
				passengerSegmentDTO.getSeat().setAisleSeat(seatDetail.isAisleSeat());
				passengerSegmentDTO.getSeat().setPaid(seatDetail.isPaid());
				passengerSegmentDTO.getSeat().setTempSeat(seatDetail.isTempSeat());
				passengerSegmentDTO.getSeat().setStatus(seatDetail.getStatus());
				passengerSegmentDTO.getSeat().setFromDCS(seatDetail.isFromDCS());
				passengerSegmentDTO.getSeat().setPaymentInfo(buildPaymentInfo(seatDetail.getPaymentInfo()));
				
				// if the passengerSegment has checked in and the seat is from OLCI and not paid, mark as free
				if (checkedIn && seatDetail.isFromDCS() && !MMBBizruleConstants.SEAT_PAYMENT_STATUS_PAID.equals(seatDetail.getPaymentStatus())) {
					passengerSegmentDTO.getSeat().setFreeSeat(true);
				} else {
					passengerSegmentDTO.getSeat().setFreeSeat(isFreeSeat(seatDetail, passengerSegment, booking.getCprJourneys(), booking.getSegments()));
				}
			}
			
			if(!CollectionUtils.isEmpty(passengerSegment.getExtraSeats())){
				if(passengerSegmentDTO.getExtraSeats() == null){
					passengerSegmentDTO.setExtraSeats(new ArrayList<>());
				}
				for(SeatDetail extraSeat : passengerSegment.getExtraSeats()){
					if(extraSeat != null){
						SeatDTOV2 seat = new SeatDTOV2();
						seat.setSeatNo(extraSeat.getSeatNo());
						seat.setExlSeat(extraSeat.isExlSeat());
						seat.setPaid(extraSeat.isPaid());
						seat.setTempSeat(extraSeat.isTempSeat());
						seat.setStatus(extraSeat.getStatus());
						seat.setPaymentInfo(buildPaymentInfo(extraSeat.getPaymentInfo()));
						seat.setFreeSeat(isFreeSeat(seatDetail, passengerSegment, booking.getCprJourneys(), booking.getSegments()));
						passengerSegmentDTO.getExtraSeats().add(seat);
					}

				}
			}

			SeatDTOV2 seatDTO = passengerSegmentDTO.getSeat();
			if (seatDTO != null && !isConfirmedSeatStatus(seatDTO.getStatus()) &&
					BooleanUtils.isNotTrue(seatDTO.isFreeSeat()) && !seatDTO.isTempSeat()) {
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
			
			if(passengerSegment.getMealSelection() != null) {
				MealSelectionDTOV2 mealSelectionDTO = new MealSelectionDTOV2();
				mealSelectionDTO.setMealOption(passengerSegment.getMealSelection().getMealOption());
				mealSelectionDTO.setSpecialMealList(passengerSegment.getMealSelection().getSpecialMeals());
				mealSelectionDTO.setPreSelectMealList(passengerSegment.getMealSelection().getPreSelectMeals());
				mealSelectionDTO.setPreSelectMealEligibility(passengerSegment.getMealSelection().isPreSelectMealEligibility());
				passengerSegmentDTO.setMealSelection(mealSelectionDTO);
			}

			if(!CollectionUtils.isEmpty(passengerSegment.getSpecialServices())){
				passengerSegmentDTO.setSpecialServices(BookingBuildUtil.removeDubplicateSepcialService(passengerSegment.getSpecialServices()));
			}
			
			if(StringUtils.isNotEmpty(passengerSegment.getCprApplePassNumber())){
				passengerSegmentDTO.setCprApplePassNumber(passengerSegment.getCprApplePassNumber());
			}
			passengerSegmentDTO.setPickUpNumber(passengerSegment.getPickUpNumber());
			passengerSegmentDTO.setHasEticket(!StringUtils.isEmpty(passengerSegment.getEticketNumber()));
			passengerSegmentDTO.setCxKaEt(BookingBuildUtil.isCxKaET(passengerSegment.getEticketNumber()));
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
				AirportUpgradeInfoDTOV2 airportUpgradeInfoDTO = new AirportUpgradeInfoDTOV2();
				airportUpgradeInfoDTO.setNewCabinClass(airportUpgradeInfo.getNewCabinClass());
				passengerSegmentDTO.setAirportUpgradeInfo(airportUpgradeInfoDTO);
			}
			// upgrade process status
			passengerSegmentDTO.setUpgradeProgressStatus(passengerSegment.getUpgradeProgressStatus());
			passengerSegmentDTOs.add(passengerSegmentDTO);
		}
	}
	
	/**
	 * got cpr segment info of the segment from OLCI
	 * @param segmentId
	 * @param cprJourneys
	 * @param segments 
	 * @return boolean
	 */
	private boolean withinOlci(String segmentId, List<Journey> cprJourneys, List<Segment> segments) {
		if (CollectionUtils.isEmpty(cprJourneys) || StringUtils.isEmpty(segmentId) || CollectionUtils.isEmpty(segments)) {
			return false;
		}
		
		// find the segment by segment id
		Segment segment = segments.stream().filter(seg -> segmentId.equals(seg.getSegmentID())).findFirst().orElse(null);
		// find the matched CPR Journey
		Journey cprJourney = cprJourneys.stream().filter(journey -> !CollectionUtils.isEmpty(journey.getPassengerSegments()) && !CollectionUtils.isEmpty(journey.getSegments())
				&& journey.getSegments().stream().anyMatch(seg -> segmentId.equals(seg.getSegmentId()))).findFirst().orElse(null);
		
		return cprJourney != null && cprJourney.isOpenToCheckIn() && cprJourney.isCanCheckIn()
				&& segment != null && !segment.isPostCheckIn();
	}

	/**
	 * check if the seat is a free seat
	 * @param seatDetail
	 * @param passengerSegment
	 * @param olciJourneys 
	 * @param segments 
	 * @return Boolean
	 */
	private Boolean isFreeSeat(SeatDetail seatDetail, PassengerSegment passengerSegment, List<Journey> olciJourneys, List<Segment> segments) {
		if(seatDetail == null || passengerSegment == null) {
			return false;
		}
		
		SeatSelection seatSelection;
		// if segment enter check in time, use OLCI seat selection
		if (withinOlci(passengerSegment.getSegmentId(), olciJourneys, segments)) {
			seatSelection = passengerSegment.getOlciSeatSelection();
		} else {
			seatSelection = passengerSegment.getMmbSeatSelection();
		}

		if (seatSelection == null) {
			return false;
		}
		
		// since OLCI only return "EXL" indicator, modify the logic
		return BooleanUtils.isTrue(seatDetail.isExlSeat()) && seatSelection.isXlFOC()
				|| !BooleanUtils.isTrue(seatDetail.isExlSeat()) && (BooleanUtils.isTrue(seatSelection.isAsrFOC()) || seatSelection.isSelectedAsrFOC());
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

	private PaymentInfoDTOV2 buildPaymentInfo(RetrievePnrPaymentInfo pnrPaymentInfo){
		PaymentInfoDTOV2 paymentInfoDTO = null;
		if(pnrPaymentInfo!=null){
			paymentInfoDTO = new PaymentInfoDTOV2();
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
	 * @Description set display & check in mandatory
	 * @param tbTravelDocDisplays
	 * @param tbPortFlights
	 * @param segment
	 * @param segmentDTO
	 * @param isIdBooking 
	 * @return void
	 * @author haiwei.jia
	 * 
	 */
	private void setDisplayAndCheckInMandatory(List<TravelDocDisplay> tbTravelDocDisplays, List<TbPortFlight> tbPortFlights,
			Segment segment, SegmentDTOV2 segmentDTO, boolean isIdBooking) {
		SegmentDisplayDTOV2 display = segmentDTO.findDisplay();
		SegmentCheckInMandatoryDTOV2 mandatory = segmentDTO.findMandatory();
		if (segment.getApiVersion() != null) {
			for (TravelDocDisplay tbTravelDocDisplay : tbTravelDocDisplays) {
				String group = tbTravelDocDisplay.getTravelDocGroup();
				String version = String.valueOf(tbTravelDocDisplay.getTravelDocVersion());
				if (group == null || version == null || !version.equals(segment.getApiVersion())) {
					continue;
				}
				calculateDisplayAndMandatory(display, mandatory, tbTravelDocDisplay.isMandatory(), group);
			}
		}	
		
		//set travel doc display by table in DB
		setTdDisplayByDb(tbPortFlights, segment, display);
		
		//EI, PI are always true
		display.setPi(true);
		display.setEi(true);
		mandatory.setPi(true);
		mandatory.setEi(true);
		display.setFf(!isIdBooking
				&& !MMBBizruleConstants.NON_AIR_SECTOR_CARRIER_CODE_DEUTSCHE_BAHN.equals(segment.getOperateCompany()));
		
		//train pick up number display or not
		display.setPn(segment.isTrainPNDisplay());
	}

	/**
	 * Calculate Display And Mandatory
	 * @param display
	 * @param mandatory
	 * @param mdt
	 * @param group
	 */
	private void calculateDisplayAndMandatory(SegmentDisplayDTOV2 display, SegmentCheckInMandatoryDTOV2 mandatory,
			boolean mdt, String group) {
		if (group.equals(TBConstants.TRAVEL_DOC_GROUP_EC)) {
			display.setEc(true);
			mandatory.setEc(mdt);
		} else if (group.equals(TBConstants.TRAVEL_DOC_GROUP_DA)) {
			display.setDa(true);
			mandatory.setDa(mdt);
		} else if (group.equals(TBConstants.TRAVEL_DOC_GROUP_CR)) {
			display.setCr(true);
			mandatory.setCr(mdt);
		} else if (group.equals(TBConstants.TRAVEL_DOC_GROUP_PT)) {
			display.setPt(true);
			mandatory.setPt(mdt);
		} else if (group.equals(TBConstants.TRAVEL_DOC_GROUP_ST)) {
			display.setSt(true);
			mandatory.setSt(mdt);
		} else if (group.equals(TBConstants.TRAVEL_DOC_GROUP_TS)) {
			display.setKn(true);
			mandatory.setKn(mdt);
		} else if (group.equals(TBConstants.TRAVEL_DOC_GROUP_RN)) {
			display.setRn(true);
			mandatory.setRn(mdt);
		}
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
	private void setTdDisplayByDb(List<TbPortFlight> tbPortFlights, Segment segment, SegmentDisplayDTOV2 display) {
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
	private void setTravelDocDTO(TravelDoc travelDoc, TravelDocDTOV2 travelDocDTO, boolean productLevel, boolean isSecondTravelDoc) {
		if(travelDoc == null) {
			return;
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
	public List<LoungeAccessDTOV2> buildLoungeAccess(ClaimedLounge claimedLounge, PurchasedLounge purchasedLounge) {
		List<LoungeAccessDTOV2> loungeAccessList = new ArrayList<>();
		LoungeAccessDTOV2 loungeAccessDTO = null;
		LoungeClass loungeClass = null;
		if (purchasedLounge != null) {
			loungeAccessDTO = new LoungeAccessDTOV2();
			loungeClass = purchasedLounge.getType();
			
			loungeAccessDTO.setType(loungeClass);
			loungeAccessDTO.setTier(purchasedLounge.getTier());
			loungeAccessDTO.setPaymentInfo(buildPaymentInfo(purchasedLounge.getPaymentInfo()));
			loungeAccessDTO.isPurchasedLounge(true);
			loungeAccessList.add(loungeAccessDTO);
		}
		if (claimedLounge != null && claimedLounge.getType() != null && !claimedLounge.getType().equals(loungeClass)) {
			loungeAccessDTO = new LoungeAccessDTOV2();
			loungeAccessDTO.setType(claimedLounge.getType());
			loungeAccessDTO.setTier(claimedLounge.getTier());
			loungeAccessDTO.isPurchasedLounge(false);
			loungeAccessList.add(loungeAccessDTO);
		}
		
		// make sure first lounge is ahead of business lounge
		if (loungeAccessList.size() > 1) {			
			Collections.sort(loungeAccessList, (LoungeAccessDTOV2 arg0, LoungeAccessDTOV2 arg1) -> 
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
	
	/**
	 * set JourneyDTOs
	 * @param bookingDTO
	 * @param booking
	 */
	private void setJourneyDTOs(FlightBookingDTOV2 bookingDTO, Booking booking){
		
		List<Journey> journeys = booking.getCprJourneys();
		if(CollectionUtils.isEmpty(journeys)) {
			return;
		}

		List<JourneyDTOV2> journeyDTOs = bookingDTO.findCprJourneys();
		for (Journey journey : journeys) {
			JourneyDTOV2  journeyDTO = new JourneyDTOV2();
			journeyDTO.setJourneyId(journey.getJourneyId());
			journeyDTO.setBeforeCheckIn(journey.isBeforeCheckIn());
			journeyDTO.setOpenCloseTime(journey.getOpenCloseTime());
			journeyDTO.setNextOpenCloseTime(journey.getNextOpenCloseTime());
			journeyDTO.setEnabledPriorityCheckIn(journey.isEnabledPriorityCheckIn());
			journeyDTO.setCanCheckIn(journey.isCanCheckIn());
			journeyDTO.setCanCheckInAfterUpgrade(journey.isCanCheckInAfterUPgrade());
			journeyDTO.setPriorityCheckInEligible(journey.getPriorityCheckInEligible());
			journeyDTO.setErrors(journey.getErrors());
			journeyDTO.setPassengers(covert2JourneyPassengerDTOs(journey.getPassengers()));
			journeyDTO.setSegments(covert2JourneySegmentDTOs(journey.getSegments()));
			journeyDTO.setPassengerSegments(covertJourneyPassengerSegmentDTOs(journey.getPassengerSegments()));
			journeyDTO.setOpenToCheckIn(journey.isOpenToCheckIn());
			journeyDTO.setPostCheckIn(journey.isPostCheckIn());
			journeyDTO.setInhibitUSBP(journey.getInhibitUSBP());
			journeyDTO.setAllowMBP(journey.getAllowMBP());
			journeyDTO.setAllowSPBP(journey.getAllowSPBP());
			journeyDTO.setOcciEligible(journey.isOcciEligible());
			journeyDTO.setWithUS24h(hasUSFlightWith24hInJourney(journey.getSegments(),bookingDTO.getSegments()));
			journeyDTOs.add(journeyDTO);
		}
	}

	/**
	 * Covert to list of CprJourneySegmentDTOV2
	 * 
	 * @param segments
	 * @return
	 */
	private List<CprJourneySegmentDTOV2> covert2JourneySegmentDTOs(List<CprJourneySegment> cprSegments) {
		if(CollectionUtils.isEmpty(cprSegments)) {
			return null;
		}
		
		List<CprJourneySegmentDTOV2> cprSegmentDTOs = new ArrayList<>();
		for(CprJourneySegment cprSegment : cprSegments) {
			if(cprSegment == null) {
				continue;
			}
			
			CprJourneySegmentDTOV2 cprSegmentDTO = new CprJourneySegmentDTOV2();
			cprSegmentDTO.setSegmentId(cprSegment.getSegmentId());
			cprSegmentDTO.setCanCheckIn(cprSegment.getCanCheckIn());
			cprSegmentDTO.setErrors(cprSegment.getErrors());
			cprSegmentDTOs.add(cprSegmentDTO);
		}
		
		return cprSegmentDTOs;
	}

	/**
	 * Covert to list of CprJourneyPassengerDTOV2
	 * 
	 * @param passengers
	 * @return
	 */
	private List<CprJourneyPassengerDTOV2> covert2JourneyPassengerDTOs(List<CprJourneyPassenger> cprPassengers) {
		if(CollectionUtils.isEmpty(cprPassengers)) {
			return null;
		}
		
		List<CprJourneyPassengerDTOV2> cprPassengerDTOs = new ArrayList<>();
		for(CprJourneyPassenger cprPassenger : cprPassengers) {
			if(cprPassenger == null) {
				continue;
			}
			
			CprJourneyPassengerDTOV2 cprPassengerDTO = new CprJourneyPassengerDTOV2();
			cprPassengerDTO.setPassengerId(cprPassenger.getPassengerId());
			cprPassengerDTO.setCanCheckIn(cprPassenger.getCanCheckIn());
			cprPassengerDTO.setCprUniqueCustomerId(cprPassenger.getCprUniqueCustomerId());
			cprPassengerDTO.setInhibitBP(cprPassenger.isInhibitBP());
			cprPassengerDTO.setAllowMBP(cprPassenger.isAllowMBP());
			cprPassengerDTO.setAllowSPBP(cprPassenger.isAllowSPBP());
			cprPassengerDTO.setErrors(cprPassenger.getErrors());
			cprPassengerDTOs.add(cprPassengerDTO);
		}
		
		return cprPassengerDTOs;
	}

	/**
	 * covert JourneyPassengerSegmentDTOs
	 * 
	 * @param journeyPassengerSegments
	 * @return
	 */
	private List<CprJourneyPassengerSegmentDTOV2> covertJourneyPassengerSegmentDTOs(List<CprJourneyPassengerSegment> journeyPassengerSegments) {
		if(CollectionUtils.isEmpty(journeyPassengerSegments)) {
			return null;
		}
		List<CprJourneyPassengerSegmentDTOV2> journeyPassengerSegmentDTOs = new ArrayList<>();
		for(CprJourneyPassengerSegment journeyPassengerSegment : journeyPassengerSegments) {
			CprJourneyPassengerSegmentDTOV2 journeyPassengerSegmentDTO = new CprJourneyPassengerSegmentDTOV2();
			journeyPassengerSegmentDTO.setSegmentId(journeyPassengerSegment.getSegmentId());
			journeyPassengerSegmentDTO.setCprProductIdentifierDID(journeyPassengerSegment.getCprProductIdentifierDID());
			journeyPassengerSegmentDTO.setCprProductIdentifierJID(journeyPassengerSegment.getCprProductIdentifierJID());
			journeyPassengerSegmentDTO.setPassengerId(journeyPassengerSegment.getPassengerId());
			journeyPassengerSegmentDTO.setCprUniqueCustomerId(journeyPassengerSegment.getCprUniqueCustomerId());
			journeyPassengerSegmentDTO.setCanCheckIn(journeyPassengerSegment.getCanCheckIn());
			journeyPassengerSegmentDTO.setCheckedIn(journeyPassengerSegment.getCheckedIn());
			journeyPassengerSegmentDTO.setErrors(journeyPassengerSegment.getErrors());
			journeyPassengerSegmentDTO.setCheckInStandBy(journeyPassengerSegment.isCheckInStandBy());
			journeyPassengerSegmentDTO.setSecurityNumber(journeyPassengerSegment.getSecurityNumber());
			journeyPassengerSegmentDTOs.add(journeyPassengerSegmentDTO);
		}
		return journeyPassengerSegmentDTOs;
	}
	
	/**
	 * check PassengerSegment In Journeys
	 * 
	 * @param passengerId
	 * @param segmentId
	 * @param booking
	 * @return
	 */
	private Boolean checkPassengerSegmentInJourneys(String passengerId, String segmentId, Booking booking) {
		List<Journey> journeys = booking.getCprJourneys();
		if (CollectionUtils.isEmpty(journeys) || StringUtils.isEmpty(passengerId) || StringUtils.isEmpty(segmentId)) {
			return false;
		}
		
		return journeys.stream().anyMatch(journey -> journey.getPassengerSegments().stream()
				.anyMatch(ps -> ps.getPassengerId().equals(passengerId) && ps.getSegmentId().equals(segmentId)));

	}
	
	/**
	 * Journey has US Flight and with Departure time < 24h
	 * @param journeySegments
	 * @param segments
	 * @return
	 */
	private boolean hasUSFlightWith24hInJourney(List<CprJourneySegment> journeySegments,List<SegmentDTOV2> segments) {
		if (CollectionUtils.isEmpty(journeySegments)) {
			return false;
		}
		return journeySegments.stream().anyMatch(segment -> checkUSFlightWith24h(segment.getSegmentId(),segments));
	}
	
	/**
	 * check US Flight and with Departure time < 24h
	 * @param segmentId
	 * @param segments
	 * @return
	 */
	private boolean checkUSFlightWith24h(String segmentId, List<SegmentDTOV2> segments) {
		if (StringUtils.isEmpty(segmentId) || CollectionUtils.isEmpty(segments)) {
			return false;
		}
		
		SegmentDTOV2 marchedSegment = segments.stream().filter(sg -> sg.getSegmentId().equals(segmentId)).findFirst()
				.orElse(null);
		if(null == marchedSegment){
			return false;
		}
		
		String originCountryCode = aemService.getCountryCodeByPortCode(marchedSegment.getOriginPort());
		String destCountryCode = aemService.getCountryCodeByPortCode(marchedSegment.getDestPort());
		
		return BooleanUtils.isTrue(marchedSegment.isWithinTwentyFourHrs())
				&& (MMBBizruleConstants.COUNTRY_CODE_USA.equalsIgnoreCase(originCountryCode)
						|| MMBBizruleConstants.COUNTRY_CODE_USA.equalsIgnoreCase(destCountryCode));
	}
	
	private RtfsFlightSummaryDTOV2 convertRtfsFlightDTO(RtfsFlightSummary rtfsFlightSummary) {
		if (rtfsFlightSummary == null) {
			return null;
		}
		
		RtfsFlightSummaryDTOV2 rtfsFlightSummaryDTO = new RtfsFlightSummaryDTOV2();
		List<RtfsLegSummaryDTOV2> legDTOs = rtfsFlightSummary.getLegs().stream().map(this::convertRtfsLegDTO).collect(Collectors.toList());
		rtfsFlightSummaryDTO.setLegs(legDTOs);
		return rtfsFlightSummaryDTO;
	}
	
	private RtfsLegSummaryDTOV2 convertRtfsLegDTO(RtfsLegSummary rtfsLegSummary) {
		if (rtfsLegSummary == null) {
			return null;
		}
		
		RtfsLegSummaryDTOV2 rtfsLegSummaryDTO = new RtfsLegSummaryDTOV2();
		rtfsLegSummaryDTO.setSequenceId(rtfsLegSummary.getSequenceId());
		rtfsLegSummaryDTO.setOriginPort(rtfsLegSummary.getOriginPort());
		rtfsLegSummaryDTO.setDestPort(rtfsLegSummary.getDestPort());
		rtfsLegSummaryDTO.setScenarioId(rtfsLegSummary.getScenarioId());
		return rtfsLegSummaryDTO;
	}
}