package com.cathaypacific.mmbbizrule.business.commonapi.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.enums.flightstatus.FlightStatusEnum;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.business.commonapi.TaggingBusiness;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.db.model.AncillaryBannerDeeplinkData;
import com.cathaypacific.mmbbizrule.db.model.MealTagMappingModel;
import com.cathaypacific.mmbbizrule.dto.response.tagging.AncillaryBannerDeeplinkDataDTO;
import com.cathaypacific.mmbbizrule.dto.response.tagging.MealTagMappingDTO;
import com.cathaypacific.mmbbizrule.dto.response.tagging.TaggingDTO;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.service.BookingBuildService;
import com.cathaypacific.mmbbizrule.service.TaggingService;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.google.common.collect.Lists;



@Service
public class TaggingBusinessImpl implements TaggingBusiness{

	@Autowired
	TaggingService taggingService;
	
	@Value("${ancillaryBannerUpgrade.DepartureTime}")
	private int minimumDepartureTime;
	
	private static LogAgent logger = LogAgent.getLogAgent(TaggingBusinessImpl.class);
	
	
	BookingBuildService bookingBuildService;
	
	public TaggingDTO retrieveTagMapping() {
		TaggingDTO taggingDTO = new TaggingDTO();
		
		// Retrieve meal tag mapping and set to tagging DTO
		List<MealTagMappingModel> mealTagMappingModels = taggingService.findMealTagMapping();
		List<MealTagMappingDTO> mealTagMappingDTOs = Lists.newArrayList();
		for(MealTagMappingModel mealTagMappingModel: mealTagMappingModels) {
			MealTagMappingDTO mealTagMappingDTO = new MealTagMappingDTO();
			mealTagMappingDTO.setMealType(mealTagMappingModel.getMealType());
			mealTagMappingDTO.setTaggingName(mealTagMappingModel.getTaggingName());
			mealTagMappingDTOs.add(mealTagMappingDTO);
		}
		taggingDTO.setMealTagMappings(mealTagMappingDTOs);
		
		
		// Retrieve ancillary banner deeplink data
		List<AncillaryBannerDeeplinkData> ancillaryBannerDeeplinkDatas = taggingService.findAncillaryBannerDeeplinkData();
		List<AncillaryBannerDeeplinkDataDTO> ancillaryBannerDeeplinkDataDTOs = Lists.newArrayList();
		for(AncillaryBannerDeeplinkData ancillaryBannerDeeplinkData: ancillaryBannerDeeplinkDatas) {
			AncillaryBannerDeeplinkDataDTO ancillaryBannerDeeplinkDataDTO = new AncillaryBannerDeeplinkDataDTO();
			ancillaryBannerDeeplinkDataDTO.setMarket(ancillaryBannerDeeplinkData.getMarket());
			ancillaryBannerDeeplinkDataDTO.setDtaCoverFor(ancillaryBannerDeeplinkData.getDtaCoverFor());
			ancillaryBannerDeeplinkDataDTO.setDtaDestination(ancillaryBannerDeeplinkData.getDtaDestination());
			ancillaryBannerDeeplinkDataDTOs.add(ancillaryBannerDeeplinkDataDTO);
		}
		taggingDTO.setAncillaryBannerDeeplinkData(ancillaryBannerDeeplinkDataDTOs);
		
		return taggingDTO;
	}
	
	
	
	public boolean checkRedemptionBannerUpgrade(Booking booking) {

		/* Event/hotel condition **/
		if (!hasEventOrHotel(booking)) {
			return false;
		}

		/* Check if ndc booking **/
		if (checkIsNdcBooking(booking)) {
			return false;
		}

		/* Check if all segments are upgraded through Plusgrade **/
		if (BookingBuildUtil.isAllUpgradeBidWonSegment(booking)) {
			return false;
		}

		
		/* OperatedCompany AND MarketedCompany of all flights are CX/KA condition **/
		if (!checkOperateCompany(booking)) {
			return false;
		}
		
		/* Check if booking has infant **/
		if (checkIfBookingContainsINFT(booking)) {
			return false;
		}
		
		
		// Check if there is BKUG booking
		if (!booking.getHasBkug()) {
			return false;
		}

		
		/* Check the subclasses in the booking **/
		List<Segment> segments = checkSegmentConfirmedOrWaitListed(booking.getSegments());
		if (segments.size()<=0) {
			return false;
		};
		
		segments = checkSegmentSubClassesAndDetails(segments);
				
		if (segments.size()<=0) {
			return false;
		};
		

		/* Check if segments not checked in**/
		segments = checkIfSegmentCheckedIn(segments);
		
		if (segments.size()<=0) {
			return false;
		};
		
		/* check if segments depart four days after **/
		segments = checkIfSegmentFourDaysAfter(segments);
		
		if (segments.size()<=0) {
			return false;
		}

		/*olss-9003 check if is staff booking */
		if (booking.isStaffBooking()){
			return false;
		}

		return true;
		
	}
	
	
	
	private List<Segment> checkSegmentConfirmedOrWaitListed(List<Segment> segments) {
		List<Segment> returnSegments = new ArrayList<Segment>();
		for (Segment segment : segments) {
			if ((segment.getSegmentStatus() != null) && 
					((FlightStatusEnum.CONFIRMED.getCode().equals(segment.getSegmentStatus().getStatus().getCode())) || 
					 (FlightStatusEnum.WAITLISTED.getCode().equals(segment.getSegmentStatus().getStatus().getCode())))) {
				returnSegments.add(segment);
			}
		}
		return returnSegments;
	}
	
	
	
	private boolean checkIfBookingContainsINFT(Booking booking) {
		
		List<Passenger> passengers = booking.getPassengers();
		
		if (CollectionUtils.isEmpty(passengers)) {
			return true;
		}
		
		
		for (Passenger p : passengers) {
			if ((p.getPassengerType().equals(PnrResponseParser.PASSENGER_TYPE_INF)) || (p.getPassengerType().equals(PnrResponseParser.PASSENGER_TYPE_INS))) {
				return true;
			}
		}
		
		return false;
	}



	private List<Segment> checkSegmentSubClassesAndDetails(List<Segment> segments) {
		
		List<Segment> returnSegments = new ArrayList<Segment>();
		
		for (Iterator<Segment> segmentIterator = segments.iterator(); segmentIterator.hasNext();) {
			Segment segment = segmentIterator.next();
			
			if (isRedemptionEligible(segment.getSubClass())) {
				
				boolean checkIfOnlyOneSegment = true;	
				
				/* If true: check if there is no duplicated redemption segment **/
				for (Iterator<Segment> checkSegmentIterator = segments.iterator(); checkSegmentIterator.hasNext();) {
					Segment checkSegment = checkSegmentIterator.next();
					
					if (!segment.getSegmentID().equals(checkSegment.getSegmentID())) {
						checkIfOnlyOneSegment = false;
						if ((segment.getMarketCompany()!=null) && 
							(segment.getMarketSegmentNumber()!=null) && 
							(segment.getDestPort()!=null) && 
							(segment.getOriginPort()!=null) && 
							(segment.getDepartureTime()!=null) &&
							(checkSegment.getSubClass()!=null) &&
							(checkSegment.getMarketCompany()!=null) && 
							(checkSegment.getMarketSegmentNumber()!=null) && 
							(checkSegment.getDestPort()!=null) && 
							(checkSegment.getOriginPort()!=null) && 
							(checkSegment.getDepartureTime()!=null)) { 

								if (!((segment.getMarketCompany()+segment.getMarketSegmentNumber()).equals((checkSegment.getMarketCompany()+checkSegment.getMarketSegmentNumber()))) &&
								!(segment.getDestPort().equals(checkSegment.getDestPort())) &&
								!(segment.getOriginPort().equals(checkSegment.getOriginPort())) &&
								!(segment.getDepartureTime().getTime().equals(checkSegment.getDepartureTime().getTime())) && 
								!(isUnableUpgrade(checkSegment.getSubClass()))) {
									
									returnSegments.add(segment);
										
								}
						} else {
							continue;
						}	
					}
				}
				
				if (checkIfOnlyOneSegment) {
					returnSegments.add(segment);
				}

			}else{
				continue;
			}
			
		}
		
		return returnSegments;
		
	}



	private boolean isUnableUpgrade(String subClass) {
		List<String> subclasses = taggingService.findSubclassList(false);
		return subclasses.contains(subClass);
	}



	private boolean isRedemptionEligible(String subClass) {
		List<String> subclasses = taggingService.findSubclassList(true);
		return subclasses.contains(subClass);
	}



	private List<Segment> checkIfSegmentFourDaysAfter(List<Segment> segments) {
		for(Iterator<Segment> segmentIterator = segments.iterator(); segmentIterator.hasNext();) {
			Segment s = segmentIterator.next();
			
			if (s.getDepartureTime() == null
					|| StringUtils.isEmpty(s.getDepartureTime().getTime())
					|| StringUtils.isEmpty(s.getDepartureTime().getTimeZoneOffset())
					|| s.isFlown()) {
				
				segmentIterator.remove();
				
			}
			else {
				try {
					Date departureTimeDate;
					departureTimeDate = DateUtil.getStrToDate(DepartureArrivalTime.TIME_FORMAT,
								s.getDepartureTime().getTime(), s.getDepartureTime().getTimeZoneOffset());
					
					if (!BookingBuildUtil.isCurrentTimeBeforCertainHoursOfDate(departureTimeDate, minimumDepartureTime)) {
						segmentIterator.remove();
					}
					
				} catch (ParseException e) {
					String errorMsg= String.format("Unable to find booking departure data and time");
					logger.error(errorMsg);
				}	
			}
		}
		
		return segments;
		
	}



	private boolean checkIsNdcBooking(Booking booking) {
		if (booking.isNdcBooking()) {
			return true;
		}
		return false;
	}



	private List<Segment> checkIfSegmentCheckedIn(List<Segment> segments) {
		Iterator<Segment> s = segments.iterator();
		while (s.hasNext()) {
			Segment segment = s.next();
			if (segment.isCheckedIn()) {
				s.remove();
			}
		}
		return segments;
	}



	private boolean checkOperateCompany(Booking booking) {
		
		List<Segment> segments = booking.getSegments();
		
		if (CollectionUtils.isEmpty(segments)) {
			return false;
		}
		
		String operatedCompanyOfFirstSegment = segments.get(0).getOperateCompany();
		
		for (Segment s : segments) {
			Boolean isOperatedAndMarketedByCXKA = false;
			
			if ((((s.getOperateCompany().equals(MMBBizruleConstants.KA_OPERATOR)) && (s.getMarketCompany().equals(MMBBizruleConstants.KA_OPERATOR))) ||
				((s.getOperateCompany().equals(MMBBizruleConstants.CX_OPERATOR)) && (s.getMarketCompany().equals(MMBBizruleConstants.CX_OPERATOR)))) &&
				((s.getOperateCompany().equals(operatedCompanyOfFirstSegment)) && (s.getMarketCompany().equals(operatedCompanyOfFirstSegment)))){
				isOperatedAndMarketedByCXKA = true;
			}
			
			
			if (!isOperatedAndMarketedByCXKA) {
				return false;
			}

		}
		
		return true;
	}



	private Boolean hasEventOrHotel(Booking booking) {	
		if ((booking.isHasEvent()) || (booking.isHasHotel())) {
			return false;
		}else {
			return true;
		}
	}



	
	
}
