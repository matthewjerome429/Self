package com.cathaypacific.mmbbizrule.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.constant.TicketReminderConstant;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.TicketIssueInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;

public class TicketReminderUtil {

	private TicketReminderUtil() {
		
	}
	
	/**
	 * Build flags for reminders
	 * @param booking
	 */
	public static void buildReminderInfos(Booking booking) {
		if(!CollectionUtils.isEmpty(booking.getPassengerSegments())) {
			booking.setHasIssuedTicket(booking.getPassengerSegments().stream().anyMatch(passengerSegment -> !StringUtils.isEmpty(passengerSegment.getEticketNumber())));
		}
		
		if(!CollectionUtils.isEmpty(booking.getSegments())) {

			buildTicketIssueInfo(booking);
			
			Map<String, Segment> segmentMap = new HashMap<>();
			boolean allSegmentOperatedByCXKA = true;
			
			for(Segment segment : booking.getSegments()) {
				segmentMap.put(segment.getSegmentID(), segment);
				if(!StringUtils.equals(segment.getOperateCompany(), TicketReminderConstant.OPERATE_CX) && !StringUtils.equals(segment.getOperateCompany(), TicketReminderConstant.OPERATE_KA)) {
					allSegmentOperatedByCXKA = false;
				}
			}
			
			List<RetrievePnrDataElements> ssrAndSkList = constructSSRAndSKList(booking);
			if(!CollectionUtils.isEmpty(ssrAndSkList)) {
				for(RetrievePnrDataElements retrievePnrSsrSk : ssrAndSkList) {
					//create ticket reminder for ADTK
					createTicketReminderADTK(retrievePnrSsrSk, segmentMap, booking);
					//create ticket reminder for PCC
					createTicketReminderPCC(retrievePnrSsrSk, segmentMap, allSegmentOperatedByCXKA, booking);
				}
				constructReminderInfos(booking);
			}
			applyReminderCheck(booking);
		}
	}
	
	/**
	 * Create reminder for ticket if booking with PCC
	 * @param retrievePnrSsrSk
	 * @param segmentDTOMap
	 */
	private static void createTicketReminderPCC (RetrievePnrDataElements retrievePnrSsrSk , Map<String, Segment> segmentMap, boolean allSegmentOperatedByCXKA, Booking booking) {
		if (StringUtils.equals(TicketReminderConstant.CK_TYPE, retrievePnrSsrSk.getType()) && StringUtils.contains(retrievePnrSsrSk.getFreeText(), TicketReminderConstant.MUST_VERIFY)) {
				Segment segment = segmentMap.get(retrievePnrSsrSk.getSegmentId());
				if(segment != null) {
					if(!segment.isFlown()) {
						segment.setPcc(true);
					}
				} else {
					if(allSegmentOperatedByCXKA && !booking.getSegments().get(0).isFlown()) {
						booking.setPcc(true);
					}
					
				}
		}
	}
	
	/**
	 * Build TKTL or TKXL informations
	 * @param booking
	 */
	private static void buildTKInfos(Booking booking) {
		if (OneAConstants.TICKET_INDICATOR_TL.equals(booking.getTicketIssueInfo().getIndicator())) {
			booking.setTktl(true);
		} else if(OneAConstants.TICKET_INDICATOR_XL.equals(booking.getTicketIssueInfo().getIndicator())) {
			booking.setTkxl(true);
		}
	}
	
	/**
	 * Create reminder for ticket if booking with ADTK
	 * @param retrievePnrSsrSk
	 * @param segmentDTOMap
	 */
	private static void createTicketReminderADTK (RetrievePnrDataElements retrievePnrSsrSk , Map<String, Segment> segmentMap, Booking booking) {
		if (StringUtils.equals(TicketReminderConstant.ADTK, retrievePnrSsrSk.getType()) &&
				!booking.isHasIssuedAllTickets()) {
			if(segmentMap.get(retrievePnrSsrSk.getSegmentId()) != null) {
				segmentMap.get(retrievePnrSsrSk.getSegmentId()).setAdtk(true);
			} else {
				booking.setAdtk(true);
			}
		}
	}
	
	/**
	 * Construct reminders, if a reminder applies to all segments, move this reminder to booking level
	 * @param booking
	 */
	private static void constructReminderInfos(Booking booking) {
		if(booking.getSegments().size() > 1) {
			boolean pccAppliesToAllSegments = true;
			boolean adtkAppliesToAllSegments = true;
			for (Segment segment : booking.getSegments()) {
					if(!segment.isPcc()) {
						pccAppliesToAllSegments = false;
					}
				if(!segment.isAdtk()) {
					adtkAppliesToAllSegments = false;
				}
			}
			
			if(pccAppliesToAllSegments) {
				booking.setPcc(true);
			}
			
			if(adtkAppliesToAllSegments) {
				booking.setAdtk(true);	
			}
			
		}
	}
	
	/**
	 * Apply reminder priority check
	 * @param booking
	 */
	private static void applyReminderCheck(Booking booking) {
		if(booking.isAdtk()) {
			booking.setTktl(false);
			booking.setTkxl(false);
		}
		
		for (Segment segment : booking.getSegments()) {
			if(booking.isAdtk()) {
				segment.setAdtk(false);
			}
			
			if(booking.isPcc()) {
				segment.setPcc(false);
			}
		}
	}
	
	/**
	 * Construct SSR and SK List
	 * @param booking
	 * @return
	 */
	private static List<RetrievePnrDataElements> constructSSRAndSKList(Booking booking) {
		List<RetrievePnrDataElements> ssrAndSkList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(booking.getSkList())) {
			ssrAndSkList.addAll(booking.getSkList());
		}
		if(!CollectionUtils.isEmpty(booking.getSsrList())) {
			ssrAndSkList.addAll(booking.getSsrList());
		}
		return ssrAndSkList;
	}
	
	
	/**
	 * Build ticket issue info
	 * @param booking
	 * @param ticketIssueInfo
	 */
	private static void buildTicketIssueInfo(Booking booking) {
		if(booking.getTicketIssueInfo() != null && !booking.isHasIssuedAllTickets()) {
			if(StringUtils.isEmpty(booking.getTicketIssueInfo().getTimeZoneOffset())) {
				buildTicketIssueInfoRpLocalTime(booking);
				booking.setAdtk(true);
			} else {
				buildTicketIssueInfoRpLocalTime(booking);
				if(!booking.isHasIssuedTicket()) {
					buildTKInfos(booking);
				}
			}
		}
	}
	
	/**
	 * Convert to local time of first origin port according time zone offset
	 * @param booking
	 * @return
	 */
	private static void buildTicketIssueInfoRpLocalTime(Booking booking) {
		if (booking.getTicketIssueInfo() == null || StringUtils.isEmpty(booking.getTicketIssueInfo().getDate())) {
			return;
		}
		TicketIssueInfo ticketIssueInfo = booking.getTicketIssueInfo();
		String dateString = ticketIssueInfo.getDate() + (StringUtils.isEmpty(booking.getTicketIssueInfo().getTime()) ? "0000" : booking.getTicketIssueInfo().getTime());
		String formattedDateString = DateUtil.convertDateFormat(dateString, TicketReminderConstant.TIME_FORMAT, DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM);
		String officeTimezone = booking.getOfficeTimezone();
		if (StringUtils.isEmpty(officeTimezone)) {
			ticketIssueInfo.setRpLocalDeadLineTime(formattedDateString);
		} else {
			if (StringUtils.isEmpty(ticketIssueInfo.getTimeZoneOffset())) {
				ticketIssueInfo.setRpLocalDeadLineTime(formattedDateString);
				ticketIssueInfo.setTimeZoneOffset(officeTimezone);	
			} else {
				if (!StringUtils.equals(booking.getTicketIssueInfo().getTimeZoneOffset(), officeTimezone)) {
					// convert the time from ticket issue office timezone to booking office timezone
					ticketIssueInfo.setRpLocalDeadLineTime(DateUtil.convertDateTimezone(formattedDateString,
							booking.getTicketIssueInfo().getTimeZoneOffset(), officeTimezone,
							DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM));
				} else {
					ticketIssueInfo.setRpLocalDeadLineTime(formattedDateString);
				}
			}
		}
	}
}
