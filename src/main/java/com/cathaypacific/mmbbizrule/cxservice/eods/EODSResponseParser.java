package com.cathaypacific.mmbbizrule.cxservice.eods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.eodsconsumer.model.response.bookingsummary_v1.BookingSummaryInfo;
import com.cathaypacific.eodsconsumer.model.response.bookingsummary_v1.BookingSummaryRS;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.cxservice.eods.model.EODSBooking;
import com.cathaypacific.mmbbizrule.cxservice.eods.model.EODSBookingPassenger;
import com.cathaypacific.mmbbizrule.cxservice.eods.model.EODSBookingSegment;

public class EODSResponseParser {

	/**
	 * Parse response to a list of EODSBooking
	 * 
	 * @param response
	 * @return List<EODSBooking>
	 */
	public List<EODSBooking> parseResponseAndBuildToBookingList(BookingSummaryRS response){
		if(response == null || response.getBookingSummary()== null||CollectionUtils.isEmpty(response.getBookingSummary().getBookingSummaryInfo())){
			return null;
		}
		
		Map<String, EODSBooking> filterMap = new HashMap<>();
		
		for (BookingSummaryInfo bookingSummaryInfo : response.getBookingSummary().getBookingSummaryInfo()) {
			EODSBooking eodsBooking = new EODSBooking();
			
			//parser basic information
			this.parserBasicInfo(eodsBooking, bookingSummaryInfo);
			
			//parser Passenger
			this.parserPassenger(eodsBooking, bookingSummaryInfo);
			
			//parser segment
			this.parserSegment(eodsBooking, bookingSummaryInfo);
			
			if(filterMap.get(bookingSummaryInfo.getRLOC()) != null) {
				this.constructMap(filterMap, eodsBooking);
			} else {
				filterMap.put(bookingSummaryInfo.getRLOC(), eodsBooking);
			}

		}
		
		return this.converEODSBookings(filterMap);
	}
	
	
	/**
	 * Parse basic information from response
	 * 
	 * @param eodsBooking
	 * @param bookingSummaryInfo
	 */
	private void parserBasicInfo (EODSBooking eodsBooking, BookingSummaryInfo bookingSummaryInfo) {
		
		eodsBooking.setRloc(bookingSummaryInfo.getRLOC());
		if (bookingSummaryInfo.getSegment().getBookingType() != null) {
			eodsBooking.setBookingType(bookingSummaryInfo.getSegment().getBookingType());
		}
		
	}
	
	/**
	 * Parse passenger from response
	 * 
	 * @param eodsBooking
	 * @param bookingSummaryInfo
	 */
	private void parserPassenger (EODSBooking eodsBooking, BookingSummaryInfo bookingSummaryInfo) {
		
		if(bookingSummaryInfo.getPassenger() != null) {
			List<EODSBookingPassenger> passengers = new ArrayList<>();
			EODSBookingPassenger passenger = new EODSBookingPassenger();
			
			passenger.setFamilyName(bookingSummaryInfo.getPassenger().getPassengerLastName());
			passenger.setGivenName(bookingSummaryInfo.getPassenger().getPassengerFirstName());
			passengers.add(passenger);
			
			eodsBooking.setPassengers(passengers);
		}
	}
	
	/**
	 * Parse segment from response
	 * 
	 * @param eodsBooking
	 * @param bookingSummaryInfo
	 */
	private void parserSegment (EODSBooking eodsBooking, BookingSummaryInfo bookingSummaryInfo) {
		
		if(bookingSummaryInfo.getSegment() != null) {
			List<EODSBookingSegment> segments = new ArrayList<>();
			EODSBookingSegment segment = new EODSBookingSegment();
			
			segment.setSegmentNo(String.valueOf(bookingSummaryInfo.getSegment().getSegmentNo()));
			segment.setDepDateTime(DateUtil.getDate2Str(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, bookingSummaryInfo.getSegment().getScheduleDepartureDatetime().toGregorianCalendar().getTime()));
			segment.setArrDateTime(DateUtil.getDate2Str(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, bookingSummaryInfo.getSegment().getScheduleArrivalDatetime().toGregorianCalendar().getTime()));
			segment.setCompany(bookingSummaryInfo.getSegment().getCarrierCode());
			segment.setOriginPort(bookingSummaryInfo.getSegment().getOrigin());
			segment.setDestPort(bookingSummaryInfo.getSegment().getDestination());
			segment.setNumber(bookingSummaryInfo.getSegment().getFlightNo());
			segment.setStatus(bookingSummaryInfo.getSegment().getStatus());
			segments.add(segment);
			
			eodsBooking.setSegments(segments);
		}
	}
	
	/**
	 * Construct a map of EODSBooking with RLOC as key
	 * 
	 * @param filterMap
	 * @param eodsBooking
	 * @param bookingSummaryInfo
	 */
	private void constructMap (Map<String, EODSBooking> filterMap , EODSBooking eodsBooking) {
		EODSBooking mapBooking = filterMap.get(eodsBooking.getRloc());
		if(mapBooking.getBookingType() == null) {
			mapBooking.setBookingType(eodsBooking.getBookingType());
		}
		
		boolean isSegExist = false;
		if(!CollectionUtils.isEmpty(eodsBooking.getSegments())) {
			for (EODSBookingSegment segment : mapBooking.getSegments()) {
				if(StringUtils.equals(segment.getSegmentNo(), eodsBooking.getSegments().get(0).getSegmentNo())) {
					isSegExist = true;
				}
			}
			if(StringUtils.equals(mapBooking.getSegments().get(0).getSegmentNo(), eodsBooking.getSegments().get(0).getSegmentNo())) {
				mapBooking.getPassengers().addAll(eodsBooking.getPassengers());
			}
			
			if(!isSegExist) {
				mapBooking.getSegments().addAll(eodsBooking.getSegments());
			}
		}
	}
	
	
	/**
	 * Convert a map of EODSBooking to EODSBooking list
	 * 
	 * @param filterMap
	 * @param eodsBookings
	 */
	private List<EODSBooking> converEODSBookings (Map<String, EODSBooking> filterMap){
		List<EODSBooking> eodsBookings = new ArrayList<>();
		for (Map.Entry<String, EODSBooking> entry : filterMap.entrySet()) {
			if(entry.getValue() != null) {
				eodsBookings.add(entry.getValue());
		 	}
		}
		return eodsBookings;
	}
}
