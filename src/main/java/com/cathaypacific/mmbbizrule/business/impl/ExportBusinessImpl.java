package com.cathaypacific.mmbbizrule.business.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.aem.model.AirportDetails;
import com.cathaypacific.mmbbizrule.aem.service.AEMService;
import com.cathaypacific.mmbbizrule.business.ExportBusiness;
import com.cathaypacific.mmbbizrule.config.BookingStatusConfig;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.cxservice.memberprofile.service.RetrieveProfileService;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJEvent;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJEventBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJHotel;
import com.cathaypacific.mmbbizrule.cxservice.oj.model.build.OJHotelBooking;
import com.cathaypacific.mmbbizrule.cxservice.oj.service.OJBookingService;
import com.cathaypacific.mmbbizrule.db.dao.CabinClassDAO;
import com.cathaypacific.mmbbizrule.db.model.BookingStatus;
import com.cathaypacific.mmbbizrule.db.model.CabinClass;
import com.cathaypacific.mmbbizrule.handler.BookingBuildHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.summary.TempLinkedBooking;
import com.cathaypacific.mmbbizrule.model.profile.ProfilePersonInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBookingPackageInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDepartureArrivalTime;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.repository.TempLinkedBookingRepository;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.cathaypacific.mmbbizrule.util.Ical4jUtil;
import com.google.common.base.Objects;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;

@Service
public class ExportBusinessImpl implements ExportBusiness {
	
	@Value("${online.checkin.link}")
	private String olciLink;
	
	@Value("${manage.booking.link}")
	private String mmbLink;
	
	@Value("${mmb.flight.passed.time}")
	private long flightPassedTime;
	
	@Autowired
	private BookingStatusConfig bookingStatusConfig;
	
	@Autowired
	private CabinClassDAO cabinClassDAO;
	
	@Autowired
	private AEMService aemService;
	
	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Autowired
	private OJBookingService ojBookingService;
	
	@Autowired
	private RetrieveProfileService retrieveProfileService;
	
	@Autowired
	private BookingBuildHelper bookingBuildHelper;
	
	@Autowired
	private TempLinkedBookingRepository tempLinkedBookingRepository;
	
	@Override
	public Calendar getTripSummaryCal(String oneARloc, String ojRloc, LoginInfo loginInfo) throws BusinessBaseException, ParseException {
		// validate ojRloc & oneARloc
		if(StringUtils.isEmpty(ojRloc) && StringUtils.isEmpty(oneARloc)) {
			throw new UnexpectedException("ojRloc/oneARloc at least one of them should be not empty for export function",
					new ErrorInfo(ErrorCodeEnum.ERR_BUSSINESS_UNKNOW));
		}
		
		// Get flightBooking & ojBooking
		Map<String, Object> map = getFlightAndOJBookings(oneARloc, ojRloc, loginInfo);
		RetrievePnrBooking flightBooking = (RetrievePnrBooking) map.get(MMBBizruleConstants.BOOKING_TYPE_FLIGHT);
		OJBooking ojBooking = (OJBooking) map.get(MMBBizruleConstants.BOOKING_TYPE_PACKAGE);
		
		// Build events of flight/hotel/event
		List<VEvent> events = new ArrayList<>();
		buildFlightVEvents(events, flightBooking);
		buildOJVEvent(events, ojBooking);
		
		Calendar calendar = new Calendar();
		Ical4jUtil.setDefaultProperties(calendar);
		Ical4jUtil.setEvents(calendar, events);
		
		return calendar;
	}
	
	/**
	 * Get flightBooking & ojBooking by oneARloc/ojRloc/loginInfo.
	 * 
	 * @param oneARloc
	 * @param ojRloc
	 * @param loginInfo
	 * @return
	 * @throws BusinessBaseException
	 */
	private Map<String, Object> getFlightAndOJBookings(String oneARloc, String ojRloc, LoginInfo loginInfo) 
			throws BusinessBaseException {
		RetrievePnrBooking flightBooking = null;
		OJBooking ojBooking = null;
		if(StringUtils.isNotEmpty(oneARloc)) {
			flightBooking = pnrInvokeService.retrievePnrByRloc(oneARloc);
			if(flightBooking != null && !isFlightOnlyBooking(flightBooking)) {				
				String[] names = getNames(flightBooking.getPrimaryPassenger(), loginInfo);
				ojBooking = getOJBooking(flightBooking.getSpnr(), names[0], names[1],loginInfo);
			}
		} else if(StringUtils.isNotEmpty(ojRloc)) {
			String[] names = getNames(null, loginInfo);
			ojBooking = getOJBooking(ojRloc, names[0], names[1],loginInfo);
			
			if(ojBooking != null && ojBooking.getFlightBooking() != null 
					&& StringUtils.isNoneEmpty(ojBooking.getFlightBooking().getBookingReference())) {
				oneARloc = ojBooking.getFlightBooking().getBookingReference();
				flightBooking = pnrInvokeService.retrievePnrByRloc(oneARloc);
			}
			
			if(flightBooking != null && isFlightOnlyBooking(flightBooking)) {
				ojBooking = null;
			}
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put(MMBBizruleConstants.BOOKING_TYPE_FLIGHT, flightBooking);
		map.put(MMBBizruleConstants.BOOKING_TYPE_PACKAGE, ojBooking);
		return map;
	}
	
	/**
	 * Judge booking is flight only or not.
	 * 
	 * @param flightBooking
	 * @return boolean
	 */
	private boolean isFlightOnlyBooking(RetrievePnrBooking flightBooking) {
		RetrievePnrBookingPackageInfo packageBookingInfo = BookingBuildUtil.buildBookingPackageInfo(flightBooking.getSkList(), flightBooking.getRemarkList());
		return packageBookingInfo != null && BooleanUtils.isTrue(packageBookingInfo.isFlightOnly());
	}

	/**
	 * Get ojBooking by ojRloc/familyName/givenName
	 * 
	 * @param flightBooking
	 * @param familyName
	 * @param givenName
	 * @return
	 * @throws BusinessBaseException
	 */
	private OJBooking getOJBooking(String ojRloc, String familyName, String givenName, LoginInfo loginInfo) throws BusinessBaseException {
		// check temp linked booking
		TempLinkedBooking tempLinkedBooking = tempLinkedBookingRepository
				.getLinkedBookings(loginInfo.getMmbToken())
				.stream()
				.filter(linkedBooking -> Objects.equal(ojRloc, linkedBooking.getRloc()))
				.findFirst()
				.orElse(null);
		if (tempLinkedBooking != null) {
			familyName = tempLinkedBooking.getFamilyName();
			givenName = tempLinkedBooking.getGivenName();
		}
		return ojBookingService.getBooking(givenName, familyName, ojRloc);
	}
	
	/**
	 * Get familyName/givenName follow orders below:
	 * 1. primaryPassenger
	 * 2. member profile
	 * 3. loginInfo
	 * 
	 * String[] index: 0: familyName; 1: givenName.
	 * 
	 * @param retrievePnrPassenger
	 * @param loginInfo
	 * @return
	 */
	private String[] getNames(RetrievePnrPassenger retrievePnrPassenger, LoginInfo loginInfo) {
		String familyName = StringUtils.EMPTY;
		String givenName = StringUtils.EMPTY;
		if(retrievePnrPassenger != null) {
			familyName = retrievePnrPassenger.getFamilyName();
			givenName = retrievePnrPassenger.getGivenName();
		}
		
		if((StringUtils.isEmpty(familyName) || StringUtils.isEmpty(givenName)) 
				&& LoginInfo.LOGINTYPE_MEMBER.equals(loginInfo.getLoginType())) {
			ProfilePersonInfo profilePersonInfo = retrieveProfileService.retrievePersonInfo(loginInfo.getMemberId(), loginInfo.getMmbToken());
			familyName = profilePersonInfo.getFamilyName();
			givenName = profilePersonInfo.getGivenName();
		}
		
		if(StringUtils.isEmpty(familyName) || StringUtils.isEmpty(givenName)) {
			familyName = loginInfo.getLoginFamilyName();
			givenName = loginInfo.getLoginGivenName();
		}
		
		return new String[] {familyName, givenName};
	}

	/**
	 * Build flight events.
	 * 
	 * @param events
	 * @param flightBooking
	 * @throws ParseException
	 */
	private void buildFlightVEvents(List<VEvent> events, RetrievePnrBooking flightBooking) throws ParseException {
		if(flightBooking == null) {
			return;
		}
		
		List<RetrievePnrSegment> segments = flightBooking.getSegments();
		for(RetrievePnrSegment segment : segments) {
			events.add(buildSectorVEvent(segment, flightBooking));		
		}
	}
	
	/**
	 * Build OJ events.
	 * 
	 * @param events
	 * @param ojBooking
	 * @throws ParseException 
	 */
	private void buildOJVEvent(List<VEvent> events, OJBooking ojBooking) throws ParseException {
		if(ojBooking == null) {
			return;
		}
		
		buildHotelVEvent(events, ojBooking.getHotelBooking(), ojBooking.getBookingReference());
		buildEventVEvent(events, ojBooking.getEventBooking(), ojBooking.getBookingReference());
	}
	
	/**
	 * Build hotel events.
	 * 
	 * @param events
	 * @param hotelBooking
	 * @param ojRloc 
	 * @throws ParseException 
	 */
	private void buildHotelVEvent(List<VEvent> events, OJHotelBooking hotelBooking, String ojRloc) throws ParseException {
		if(hotelBooking == null) {
			return;
		}
		
		List<OJHotel> hotels = hotelBooking.getDetails();
		for(OJHotel hotel : hotels) {
			events.add(buildSectorVEvent(hotel, ojRloc));
		}
	}

	/**
	 * Build event VEvents.
	 * 
	 * @param events
	 * @param eventBooking
	 * @param ojRloc 
	 * @throws ParseException 
	 */
	private void buildEventVEvent(List<VEvent> events, OJEventBooking eventBooking, String ojRloc) throws ParseException {
		if(eventBooking == null) {
			return;
		}
		
		List<OJEvent> ojEvents = eventBooking.getDetails();
		for(OJEvent ojEvent : ojEvents) {
			events.add(buildSectorVEvent(ojEvent, ojRloc));
		}
	}

	/**
	 * Build flight sector event.
	 * 
	 * @param segment
	 * @param flightBooking
	 * @return
	 * @throws ParseException
	 */
	private VEvent buildSectorVEvent(RetrievePnrSegment segment, RetrievePnrBooking flightBooking) throws ParseException {
		Date departureTime = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT, segment.getDepartureTime().getPnrTime());
		Date arrivalTime = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT, segment.getArrivalTime().getPnrTime());

		VEvent event = new VEvent(new DateTime(departureTime.getTime()), new DateTime(arrivalTime.getTime()), buildFlightEventSummary(segment));

		PropertyList<Property> eventProperties = event.getProperties();
		eventProperties.add(buildFlightDescription(segment, flightBooking));
		eventProperties.add(Ical4jUtil.getRandomUid());
		return event;
	}
	
	/**
	 * Build hotel sector VEvent.
	 * 
	 * @param hotel
	 * @param ojRloc
	 * @return
	 * @throws ParseException
	 */
	private VEvent buildSectorVEvent(OJHotel hotel, String ojRloc) throws ParseException {
		Date checkInDate = DateUtil.getStrToDate(OJHotel.CHECK_DATE_TIME_FORMAT, hotel.getCheckInDate() + " " + hotel.getCheckInTime());
		Date checkOutDate = DateUtil.getStrToDate(OJHotel.CHECK_DATE_TIME_FORMAT, hotel.getCheckOutDate()+ " " + hotel.getCheckOutTime());
		
		VEvent event = new VEvent(new DateTime(checkInDate), new DateTime(checkOutDate),
				buildHotelEventSummary(hotel, checkInDate, checkOutDate));
		
		PropertyList<Property> eventProperties = event.getProperties();
		eventProperties.add(buildHotelDescription(hotel, ojRloc));
		eventProperties.add(Ical4jUtil.getRandomUid());
		return event;
	}
	
	/**
	 * Build event sector VEvent.
	 * 
	 * @param ojEvent
	 * @param ojRloc
	 * @return
	 * @throws ParseException 
	 */
	private VEvent buildSectorVEvent(OJEvent ojEvent, String ojRloc) throws ParseException {
		Date date = DateUtil.getStrToDate(OJEvent.DATE_FORMAT, ojEvent.getDate());
		String dateStr = DateUtil.convertDateFormat(ojEvent.getDate(), OJEvent.DATE_FORMAT, DateUtil.DATE_PATTERN_DDMMM);
		VEvent event = new VEvent(new net.fortuna.ical4j.model.Date(date), buildEventVEventSummary(ojEvent, dateStr));
		
		PropertyList<Property> eventProperties = event.getProperties();
		eventProperties.add(buildEventDescription(ojEvent, ojRloc, dateStr));
		eventProperties.add(Ical4jUtil.getRandomUid());
		
		return event;
	}

	private String buildEventVEventSummary(OJEvent ojEvent, String date) {
		StringBuilder summary = new StringBuilder();
		summary.append(ojEvent.getName());
		summary.append("/");
		summary.append(date);
		
		return summary.toString();
	}

	/**
	 * Build hotel VEvent summary.
	 * 
	 * @param hotel
	 * @param checkInDate
	 * @param checkOutDate
	 * @return
	 */
	private String buildHotelEventSummary(OJHotel hotel, Date checkInDate, Date checkOutDate) {
		StringBuilder summary = new StringBuilder();
		summary.append(hotel.getName());
		summary.append("/");
		summary.append(DateUtil.getDate2Str(DateUtil.DATE_PATTERN_DDMMM, checkInDate));
		summary.append(" to ");
		summary.append(DateUtil.getDate2Str(DateUtil.DATE_PATTERN_DDMMM, checkOutDate));
		
		return summary.toString();
	}
	
	/**
	 * Build flight event summary.
	 * Format: <Carrier Code><Flight number>/<Departure Date-DDMMM>/<Departure airport code><Arrival airport code>
	 * 
	 * @param segment
	 * @return
	 */
	private String buildFlightEventSummary(RetrievePnrSegment segment) {
		String departureDate = DateUtil.convertDateFormat(segment.getDepartureTime().getPnrTime(),
				RetrievePnrDepartureArrivalTime.TIME_FORMAT, DateUtil.DATE_PATTERN_DDMMM);
		
		StringBuilder summary = new StringBuilder();
		summary.append(segment.getMarketCompany());
		summary.append(segment.getMarketSegmentNumber());
		summary.append("/");
		summary.append(departureDate);
		summary.append("/");
		summary.append(segment.getOriginPort());
		summary.append(segment.getDestPort());
		
		return summary.toString();
	}
	
	/**
	 * Build event sector event description.
	 * 
	 * Sample:
	 * My Cathay Pacific Booking Details
	 * Booking Reference: VBBYMKS
	 * Event/Travel Extra Name: Banana Pocket Wi-Fi Rental for Thailand, Korea, Japan or Taiwan
	 * Event/Travel Extra City: Tokyo
	 * Date: 24Apr/15:00
	 * Manage Booking: http://www.cathaypacific.com/mb/
	 * 
	 * @param ojEvent
	 * @param ojRloc
	 * @param date
	 * @return
	 */
	private Property buildEventDescription(OJEvent ojEvent, String ojRloc, String date) {
		Description description = new Description();

		StringBuilder message = new StringBuilder();
		// Title
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_TITLE).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		// SPNR/OJRloc
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_BOOKING_REFERENCE).append(ojRloc).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		// Event/Travel Extra Name
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_EVENT_NAME).append(ojEvent.getName()).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		// Event/Travel Extra City
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_EVENT_CITY_NAME).append(ojEvent.getCityName()).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		// Date
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_EVENT_DATE).append(date).append("/").append(ojEvent.getTime()).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		// Footer
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_MMB_LINK).append(mmbLink);
		
		description.setValue(message.toString());
		return description;
	}

	/**
	 * Build hotel sector event description.
	 * 
	 * Sample:
	 * My Cathay Pacific Booking Details
	 * Booking Reference: VBBYMKS
	 * Hotel Name: Four Seasons Marunouchi
	 * Hotel City: Tokyo
	 * Check-in date: 24Apr
	 * Check-in time: 15:00
	 * Check-out date: 25Apr
	 * Check-out time: 11:00
	 * Manage Booking: http://www.cathaypacific.com/mb/
	 * 
	 * @param hotel
	 * @param ojRloc
	 * @return
	 */
	private Property buildHotelDescription(OJHotel hotel, String ojRloc) {
		Description description = new Description();

		StringBuilder message = new StringBuilder();
		// Title
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_TITLE).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		// SPNR/OJRloc
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_BOOKING_REFERENCE).append(ojRloc).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		// Hotel Name
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_HOTEL_NAME).append(hotel.getName()).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		// Hotel City
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_HOTEL_CITY).append(hotel.getAdress() != null ? hotel.getAdress().getCityName() : StringUtils.EMPTY).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		/* Check-in/out date/time */
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_HOTEL_CHECK_IN_DATE).append(DateUtil.convertDateFormat(hotel.getCheckInDate(), OJHotel.CHECK_DATE_FORMAT, DateUtil.DATE_PATTERN_DDMMM)).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_HOTEL_CHECK_IN_TIME).append(hotel.getCheckInTime()).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_HOTEL_CHECK_OUT_DATE).append(DateUtil.convertDateFormat(hotel.getCheckOutDate(), OJHotel.CHECK_DATE_FORMAT, DateUtil.DATE_PATTERN_DDMMM)).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_HOTEL_CHECK_OUT_TIME).append(hotel.getCheckOutTime()).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		// Footer
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_MMB_LINK).append(mmbLink);
		
		description.setValue(message.toString());
		return description;
	}

	/**
	 * Build flight sector event description.
	 * 
	 * Sample:
	 * My Cathay Pacific Booking Details\n
	 * Booking Reference: V4BYMK\n
	 * Flight No.: CX338\n
	 * Origin: Brussels Airport(BRU)\n
	 * Destination: Hong Kong International Airport(HKG)\n
	 * Depart: 13:10\n
	 * Arrive: 06:35(+1)\n
	 * Class: Economy( O )\n
	 * Online Check-In available on: 22Apr 13:10\n
	 * *** Check your flight departure time online before going to the airport ***\n
	 * Online Check-In: http://www.cathaypacific.com/olci/\n
	 * Manage Booking: http://www.cathaypacific.com/cx/en_HK/manage-booking/manage-booking/manage-booking-now.html
	 * 
	 * @param segment
	 * @param flightBooking
	 * @return Description
	 * @throws ParseException 
	 */
	private Description buildFlightDescription(RetrievePnrSegment segment, RetrievePnrBooking flightBooking) throws ParseException {
		Description description = new Description();
		
		StringBuilder message = new StringBuilder();
		// Title
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_TITLE).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		// RLOC
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_BOOKING_REFERENCE).append(flightBooking.getOneARloc()).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		// Marketing carrier Code+flight number
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_FLIGHT_NO).append(segment.getMarketCompany()).append(segment.getMarketSegmentNumber()).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		
		// Origin & Destination
		AirportDetails orginDetails = aemService.getAirportDetailsByCode(segment.getOriginPort());
		AirportDetails destDetails = aemService.getAirportDetailsByCode(segment.getDestPort());
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_ORIGIN).append(orginDetails != null ? orginDetails.getAirportFullName() : StringUtils.EMPTY).append("(").append(segment.getOriginPort()).append(")").append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_DESTIONATION).append(destDetails != null ? destDetails.getAirportFullName() : StringUtils.EMPTY).append("(").append(segment.getDestPort()).append(")").append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		
		// Depart & Arrive
		Date departureDate = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT, segment.getDepartureTime().getPnrTime());
		Date arrivalDate = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT, segment.getArrivalTime().getPnrTime());
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_DEPART).append(DateUtil.getDate2Str(DateUtil.TIME_PATTERN_HH_MM, departureDate)).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_ARRIVE).append(DateUtil.getDate2Str(DateUtil.TIME_PATTERN_HH_MM, arrivalDate)).append(calculateDiff(departureDate, arrivalDate)).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		
		// Class
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_CLASS).append(buildFlightClass(segment)).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		
		// build checkinTime & reminder
		String checkinTimeReminder = buildDescriptionReminder(segment, flightBooking);
		if(StringUtils.isNotEmpty(checkinTimeReminder)) {
			message.append(checkinTimeReminder);
		}
		
		// Footer
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_CHECK_IN_LINK).append(olciLink).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
		message.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_MMB_LINK).append(mmbLink);
		
		description.setValue(message.toString());
		return description;
	}
	
	/**
	 * Calculate diff between departureDate and arrivalDate.
	 * 
	 * @param departureDate
	 * @param arrivalDate
	 * @return
	 * @throws ParseException 
	 */
	private String calculateDiff(Date departureDate, Date arrivalDate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATE_PATTERN_YYYY_MM_DD);
		
		String stdFormat = sdf.format(departureDate);
		Date std = sdf.parse(stdFormat);
		
		String atdFormat = sdf.format(arrivalDate);
		Date atd = sdf.parse(atdFormat);
		
		long diff = atd.getTime() - std.getTime();
		int d = (int) (diff/(1000 * 60 * 60 * 24));
		if(d > 0) {
			return "(+" + d + ")";
		} else if(d == 0) {
			return StringUtils.EMPTY;
		} else {
			return "(" + d + ")";
		}
	}

	/**
	 * Build flight event description checkinTime & reminder.
	 * 
	 * @param segment
	 * @param flightBooking
	 * @return
	 * @throws ParseException
	 */
	private String buildDescriptionReminder(RetrievePnrSegment segment, RetrievePnrBooking flightBooking) throws ParseException {
		StringBuilder reminder = new StringBuilder();
		
		int calendarValue = 0;
		if(BookingBuildUtil.isNonMiceGroupBooking(flightBooking.getSkList(),flightBooking.getSsrList())) {
			// Do nothing.
		} else if(BookingBuildUtil.isMiceBooking(flightBooking.getSkList())) {
			calendarValue = -2;
		} else if(flightBooking.isStaffBooking()) {
			BookingStatus availableBookingStatus = bookingBuildHelper.getFirstAvailableStatus(segment.getStatus());
			if(BookingBuildUtil.containsAvlBookingStatus(availableBookingStatus, bookingStatusConfig.getWaitlistedList()) 
					|| BookingBuildUtil.containsAvlBookingStatus(availableBookingStatus, bookingStatusConfig.getStandbyList())) {
				calendarValue = -1;
			} else {
				calendarValue = -2;
			}
		} else {
			calendarValue = -2;
		}
		
		if(calendarValue != 0) {
			reminder.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_CHECK_IN_TIME).append(getCheckinTime(segment, java.util.Calendar.DAY_OF_MONTH, calendarValue)).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);
			reminder.append(MMBBizruleConstants.EXPORT_CALENDAR_DESCRIPTION_CHECK_IN_REMINDER).append(MMBBizruleConstants.EXPORT_CALENDER_LINESEPARATOR);			
		}
		
		return reminder.toString();
	}

	/**
	 * Get check-in time by departureTime & calendar.
	 * 
	 * @param segment
	 * @return
	 * @throws ParseException
	 */
	private String getCheckinTime(RetrievePnrSegment segment, int calendar, int calendarValue) throws ParseException {
		Date departureDate = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT, segment.getDepartureTime().getPnrTime());
		java.util.Calendar cal = DateUtil.getDateToCal(departureDate, calendar, calendarValue);
		return DateUtil.getDate2Str(DateUtil.DATE_PATTERN_DDMMMHHMM, cal.getTime());
	}

	/**
	 * Build flight event description class.
	 * For CX/KA marketing and operating, display marketing cabin class + marketing RBD
	 * For CX/KA marketing but interline operating, display marketing cabin class + marketing RBD
	 * For interline marketing and operating, display marketing RBD only
	 * For interline marketing but CX/KA operating, display marketing RBD only
	 * 
	 * @param segment
	 * @return String
	 */
	private String buildFlightClass(RetrievePnrSegment segment) {
		String marketCompany = segment.getMarketCompany();
		
		String cabinClass = null;
		String rbd = segment.getMarketSubClass();
		if(OneAConstants.COMPANY_CX.equalsIgnoreCase(marketCompany) ||
				OneAConstants.COMPANY_KA.equalsIgnoreCase(marketCompany)) {
			cabinClass = BookingBuildUtil.getCabinClassBySubClass(cabinClassDAO, rbd);
		}
		
		StringBuilder classBulider = new StringBuilder();
		if(StringUtils.isNotEmpty(cabinClass)) {
			List<CabinClass> cabinClasses = cabinClassDAO.findByAppCodeAndBasicClassAndSubclass(TBConstants.APP_CODE, cabinClass, rbd);
			CabinClass cabin = cabinClasses.stream().filter(c->StringUtils.isNoneEmpty(c.getDescription())).findFirst().orElse(null);
			if(cabin != null && StringUtils.isNotEmpty(cabin.getDescription())) {
				classBulider.append(cabin.getDescription());				
			}
		}
		classBulider.append("( ").append(rbd).append(" )");
		
		return classBulider.toString();
	}

}
