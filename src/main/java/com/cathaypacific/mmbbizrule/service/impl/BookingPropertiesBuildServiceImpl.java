package com.cathaypacific.mmbbizrule.service.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.BookingCommercePropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.BookingPropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.PassengerPropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.PassengerSegmentPropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.SeatPreferencePropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.SeatPropertiesDTO;
import com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties.SegmentPropertiesDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.SeatPreference;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.model.booking.detail.SegmentStatus;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrDataElements;
import com.cathaypacific.mmbbizrule.service.BookingPropertiesBuildService;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;

@Service
public class BookingPropertiesBuildServiceImpl implements BookingPropertiesBuildService{
	
	private static LogAgent logger = LogAgent.getLogAgent(BookingPropertiesBuildServiceImpl.class);
	

	
	@Override
	public BookingPropertiesDTO buildBookingProperties(Booking booking) {
		BookingPropertiesDTO bookingProperties = new BookingPropertiesDTO();
		
		buildBookingProperties(bookingProperties, booking);

		return bookingProperties;
	}
	
	public BookingCommercePropertiesDTO buildBookingCommerceProperties(Booking booking) {
		
		BookingCommercePropertiesDTO bookingCommerceProperties = new BookingCommercePropertiesDTO();
		
		buildBookingProperties(bookingCommerceProperties, booking);
		
		if (booking == null) {
			return bookingCommerceProperties;
		}
		
		bookingCommerceProperties.setPos(booking.getPosForAep());
		bookingCommerceProperties.setCreateDate(booking.getCreateDate());
		
		return bookingCommerceProperties;
	}

	private void buildBookingProperties(BookingPropertiesDTO bookingProperties, Booking booking) {
		if (booking == null) {
			return;
		}

		bookingProperties.setGroupBooking(booking.isGroupBooking());
		bookingProperties.setMiceBooking(booking.isMiceBooking());
		bookingProperties.setStaffBooking(booking.isStaffBooking());

		buildPassengerProperties(bookingProperties, booking.getPassengers());

		buildSegmentProperties(bookingProperties, booking.getSegments());

		buildPassengerSegmentProperties(bookingProperties, booking);
	}
	
	/**
	 * build passengerSegment properties
	 * 
	 * @param bookingProperties
	 * @param passengerSegments
	 */
	private void buildPassengerSegmentProperties(BookingPropertiesDTO bookingProperties,
			Booking booking) {
		if(CollectionUtils.isEmpty(booking.getPassengerSegments())){
			return;
		}
		List<PassengerSegmentPropertiesDTO> passengerSegmentPropertiesList = bookingProperties.findPassengerSegmentProperties();
		
		for(PassengerSegment ps : booking.getPassengerSegments()){
			PassengerSegmentPropertiesDTO passengerSegmentProperties = new PassengerSegmentPropertiesDTO();
			passengerSegmentProperties.setPassengerId(ps.getPassengerId());
			passengerSegmentProperties.setSegmentId(ps.getSegmentId());
			passengerSegmentProperties.setEticketIssued(!StringUtils.isEmpty(ps.getEticketNumber()));
			if(!StringUtils.isEmpty(ps.getEticketNumber())){
				passengerSegmentProperties.setCxKaET(BookingBuildUtil.isCxKaET(ps.getEticketNumber()));
			}
			buildSeatProperties(passengerSegmentProperties, ps);
			passengerSegmentPropertiesList.add(passengerSegmentProperties);
		}
		
		buildHaveWaiverBaggage(bookingProperties.getPassengerSegmentProperties(), booking);

	}

	/**
	 * build seat properties
	 * @param passengerSegmentProperties
	 * @param seat
	 */
	private void buildSeatProperties(PassengerSegmentPropertiesDTO passengerSegmentProperties,
			PassengerSegment passengerSegment) {
		
		passengerSegmentProperties.setSeat(buildSeatProperties(passengerSegment.getSeat()));
		passengerSegmentProperties.setPnrSeat(buildSeatProperties(passengerSegment.getPnrSeat()));
		passengerSegmentProperties.setCprSeat(buildSeatProperties(passengerSegment.getCprSeat()));
		passengerSegmentProperties.setSeatPreference(buildSeatPreferenceProperties(passengerSegment.getPreference()));
	}
	
	private SeatPropertiesDTO buildSeatProperties(SeatDetail seat) {
		if (seat == null) {
			return null;
		}
		
		SeatPropertiesDTO seatProperties = new SeatPropertiesDTO();
		seatProperties.setAsrSeat(seat.isAsrSeat());
		seatProperties.setExlSeat(seat.isExlSeat());
		seatProperties.setFromDCS(seat.isFromDCS());
		seatProperties.setPaid(seat.isPaid());
		seatProperties.setSeatNo(seat.getSeatNo());
		seatProperties.setStatus(seat.getStatus());
		seatProperties.setTempSeat(seat.isTempSeat());
		return seatProperties;
	}
	
	private SeatPreferencePropertiesDTO buildSeatPreferenceProperties(SeatPreference seatPreference) {
		if (seatPreference == null) {
			return null;
		}
		
		SeatPreferencePropertiesDTO seatPreferenceProperties = new SeatPreferencePropertiesDTO();
		seatPreferenceProperties.setPreferenceCode(seatPreference.getPreferenceCode());
		seatPreferenceProperties.setStatus(seatPreference.getStatus());
		return seatPreferenceProperties;
	}

	/**
	 * build haveWaiverBaggage
	 * 
	 * @param passengerSegmentProperties
	 * @param booking
	 */
	private void buildHaveWaiverBaggage(List<PassengerSegmentPropertiesDTO> passengerSegmentPropertiesList,
			Booking booking) {
		if(CollectionUtils.isEmpty(booking.getSkList())){
			return;
		}
		
		for(RetrievePnrDataElements sk : booking.getSkList()){
			if(OneAConstants.SK_TYPE_BAGW.equals(sk.getType())){
				PassengerSegmentPropertiesDTO passengerSegmentProperties = passengerSegmentPropertiesList.stream()
						.filter(psp -> psp != null && !StringUtils.isEmpty(psp.getPassengerId())
								&& !StringUtils.isEmpty(psp.getSegmentId())
								&& psp.getPassengerId().equals(sk.getPassengerId())
								&& psp.getSegmentId().equals(sk.getSegmentId()))
						.findFirst().orElse(null);
				if(passengerSegmentProperties == null){
					continue;
				}
				
				passengerSegmentProperties.setHaveWaiverBaggage(true);
			}
		}
		
	}

 

	/**
	 * build segment properties
	 * 
	 * @param bookingProperties
	 * @param segments
	 */
	private void buildSegmentProperties(BookingPropertiesDTO bookingProperties, List<Segment> segments) {
		if(CollectionUtils.isEmpty(segments)){
			return;
		}
		// sort the segments by departure PNR time
		List<Segment> sortedSegments = sortSegmentsByPnrTime(segments);
		
		for(Segment segment : sortedSegments){
			SegmentPropertiesDTO segmentProperties = new SegmentPropertiesDTO();
			segmentProperties.setSegmentId(segment.getSegmentID());
			segmentProperties.setMarketedByCxKa(OneAConstants.COMPANY_CX.equals(segment.getMarketCompany())
					|| OneAConstants.COMPANY_KA.equals(segment.getMarketCompany()));
			segmentProperties.setOperatedByCxKa(OneAConstants.COMPANY_CX.equals(segment.getOperateCompany())
					|| OneAConstants.COMPANY_KA.equals(segment.getOperateCompany()));
			
			// Wait list
			Optional.ofNullable(segment).map(Segment::getSegmentStatus).map(SegmentStatus::getStatus).ifPresent(
					status -> segmentProperties.setWaitlisted(FlightStatusEnum.WAITLISTED.equals(status))
				);
			
			// before 24 hours of STD
			if (segment.getDepartureTime() != null && segment.getDepartureTime().getRtfsScheduledTime() != null) {
				Date stdDate;
				try {
					stdDate = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT, segment.getDepartureTime().getScheduledTime(), segment.getDepartureTime().getTimeZoneOffset());
				} catch (ParseException e) {
					logger.warn(String.format("Failed to conver string %s to date", segment.getDepartureTime().getRtfsScheduledTime()));
					bookingProperties.findSegmentProperties().add(segmentProperties);
					continue;
				}
				
				segmentProperties.setBefore24H(dateBefore24H(stdDate));
			}
			bookingProperties.findSegmentProperties().add(segmentProperties);
		}
		
	}

	/**
	 * 
	* @Description sort segments by PNR time
	* @param segments
	* @return List<Segment>
	* @author haiwei.jia
	 */
	private List<Segment> sortSegmentsByPnrTime(List<Segment> segments) {
		return segments.stream().sorted((seg1, seg2) -> {
			if(seg1.getDepartureTime() == null 
					|| StringUtils.isEmpty(seg1.getDepartureTime().getPnrTime())
					|| StringUtils.isEmpty(seg1.getDepartureTime().getTimeZoneOffset())){
				return 1;
			} else if(seg2.getDepartureTime() == null 
					|| StringUtils.isEmpty(seg2.getDepartureTime().getPnrTime())
					|| StringUtils.isEmpty(seg2.getDepartureTime().getTimeZoneOffset())){
				return -1;
			} else{
				Date departureTime1 = null;
				Date departureTime2 = null;
				try {
					departureTime1 = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT,
							seg1.getDepartureTime().getPnrTime(), seg1.getDepartureTime().getTimeZoneOffset());
				} catch (ParseException e) {
					logger.warn(String.format("Failed to conver string %s to date", departureTime1));
					return 1;
				}
				try {
					departureTime2 = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT,
							seg2.getDepartureTime().getPnrTime(), seg2.getDepartureTime().getTimeZoneOffset());
				} catch (ParseException e) {
					logger.warn(String.format("Failed to conver string %s to date", departureTime1));
					return -1;
				}
				return departureTime1.compareTo(departureTime2);
			}
		}).collect(Collectors.toList());
	}

	/**
	 * current time is before 24 hours of input date
	 * 
	 * @return boolean
	 */
	private boolean dateBefore24H(Date date) {
		if(date == null){
			return false;
		}
		
		Calendar currentTime = Calendar.getInstance();
		currentTime.setTime(DateUtil.getGMTTime());
		currentTime.add(Calendar.DAY_OF_YEAR, 1);
		return currentTime.getTime().before(date);
	}

	/**
	 * build passenger properties
	 *  
	 * @param bookingProperties
	 * @param passengers
	 */
	private void buildPassengerProperties(BookingPropertiesDTO bookingProperties, List<Passenger> passengers) {
		if(CollectionUtils.isEmpty(passengers)){
			return;
		}
		
		for(Passenger pax : passengers){
			PassengerPropertiesDTO passengerProperties = new PassengerPropertiesDTO();
			passengerProperties.setPassengerId(pax.getPassengerId());
			passengerProperties.setFamilyName(pax.getFamilyName());
			passengerProperties.setGivenName(pax.getGivenName());
			passengerProperties.setTitle(pax.getTitle());
			passengerProperties.setInfant(OneAConstants.PASSENGER_TYPE_INF.equals(pax.getPassengerType()));
			
			bookingProperties.findPassengerProperties().add(passengerProperties);
		}
	}

}
