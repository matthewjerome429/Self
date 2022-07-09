package com.cathaypacific.mmbbizrule.oneaservice.pnrsearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.oneaservice.pnrsearch.model.PnrSearchBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnrsearch.model.PnrSearchSegment;
import com.cathaypacific.oneaconsumer.model.response.pausrr_16_1_1a.CodedAttributeInformationType266029C;
import com.cathaypacific.oneaconsumer.model.response.pausrr_16_1_1a.ElementManagementSegmentType;
import com.cathaypacific.oneaconsumer.model.response.pausrr_16_1_1a.PNRSearchReply;
import com.cathaypacific.oneaconsumer.model.response.pausrr_16_1_1a.PNRSearchReply.PnrViews;
import com.cathaypacific.oneaconsumer.model.response.pausrr_16_1_1a.ReservationControlInformationTypeI72884S;
import com.cathaypacific.oneaconsumer.model.response.pausrr_16_1_1a.StructuredBookingRecordImageType;
import com.cathaypacific.oneaconsumer.model.response.pausrr_16_1_1a.StructuredBookingRecordImageType.OriginDestinationDetails;
import com.cathaypacific.oneaconsumer.model.response.pausrr_16_1_1a.StructuredBookingRecordImageType.OriginDestinationDetails.ItineraryInfo;

public class PnrSearchResponseParser {
	
	private static  final String COMPANY_ONEA="1A";
	
	private static final String SEGMENT_ID_QUALIFIER="ST";
	
	private static LogAgent logger = LogAgent.getLogAgent(PnrSearchResponseParser.class);
	
	public List<PnrSearchBooking> paserResponse(PNRSearchReply pnrSearchReply){
		
		if(CollectionUtils.isEmpty(pnrSearchReply.getPnrViews())){
			return Collections.emptyList();
		}
		// response
		List<PnrSearchBooking> pnrSearchBookings = new ArrayList<>();
		
		for (PnrViews pnrViews : pnrSearchReply.getPnrViews()) {
			
			PnrSearchBooking pnrSearchBooking = new PnrSearchBooking();
			StructuredBookingRecordImageType pnrView = pnrViews.getPnrView();
			paserBookingInfo(pnrSearchBooking,pnrView);
		
			//segment info
			parserSegmentInfo(pnrView.getOriginDestinationDetails(), pnrSearchBooking);
			pnrSearchBookings.add(pnrSearchBooking);
		}
		
		return pnrSearchBookings;
	}
	/**
	 * paser Booking info 
	 * @param pnrSearchBooking
	 * @param pnrView
	 */
	private void paserBookingInfo(PnrSearchBooking pnrSearchBooking, StructuredBookingRecordImageType pnrView) {
		// sbrAttributes
		if (pnrView.getPnrHeader() != null && pnrView.getPnrHeader().getSbrAttributes() != null) {
			pnrSearchBooking.setSbrAttributes(pnrView.getPnrHeader().getSbrAttributes().getAttributeDetails().stream()
					.map(CodedAttributeInformationType266029C::getAttributeType).collect(Collectors.toList()));
		}

		paserRloc(pnrSearchBooking, pnrView);
	}
	/**
	 *  Parser flight.
	 * @param originDestinationDetailsList
	 * @param booking
	 */
	private void parserSegmentInfo(OriginDestinationDetails originDestinationDetails,PnrSearchBooking pnrSearchBooking){
		
		if(originDestinationDetails == null){
			return;
		}
		
		List<PnrSearchSegment> segments = new ArrayList<>();
			for (ItineraryInfo itineraryInfo : originDestinationDetails.getItineraryInfo()) {
				if(!isValidSegment(itineraryInfo)){
					continue;
				}
				PnrSearchSegment pnrSearchSegment = new PnrSearchSegment();
				pnrSearchSegment.setSegmentId(getIdByQualifier(itineraryInfo.getElementManagementItinerary(),SEGMENT_ID_QUALIFIER));
				parserArrivalTime(itineraryInfo,pnrSearchSegment);
				parserDepartureTime(itineraryInfo,pnrSearchSegment);
				//flight base info 
				parserSegmentBaseInfo(itineraryInfo,pnrSearchSegment);
				
				segments.add(pnrSearchSegment);
				
		}
			pnrSearchBooking.setSegments(segments);
	}
	/**
	 * check the flight is valid
	 * @param itineraryInfo
	 * @return
	 */
	private boolean isValidSegment(ItineraryInfo itineraryInfo){
		
		//TODO for fix release1 issue, we should only parser flight info, remove any unknow  itineraryInfo
		if (itineraryInfo.getTravelProduct().getProductDetails()==null
				||StringUtils.isEmpty(itineraryInfo.getTravelProduct().getProductDetails().getIdentification())
				|| itineraryInfo.getTravelProduct().getBoardpointDetail() == null
				|| itineraryInfo.getTravelProduct().getOffpointDetail() == null
				|| StringUtils.isEmpty(itineraryInfo.getTravelProduct().getBoardpointDetail().getCityCode())
				|| StringUtils.isEmpty(itineraryInfo.getTravelProduct().getOffpointDetail().getCityCode())) {
			logger.warn("Found invalid ItineraryInfo(segment element in pnr).");
			return false;
		}
		return true;
	}
	/**
	 * Parser segment base info.
	 * @param itineraryInfo
	 * @param segment
	 */
	private void parserSegmentBaseInfo(ItineraryInfo itineraryInfo, PnrSearchSegment pnrSearchSegment) {

		if (itineraryInfo.getTravelProduct() != null) {
			// flight operate company
			if (itineraryInfo.getTravelProduct().getCompanyDetail() != null) {
				// operating info
				pnrSearchSegment.setCompany(itineraryInfo.getTravelProduct().getCompanyDetail().getIdentification());
			}
			// flight operate company
			if (itineraryInfo.getTravelProduct().getProductDetails() != null) {
				pnrSearchSegment.setNumber(itineraryInfo.getTravelProduct().getProductDetails().getIdentification());
			}
			// origin port
			if (itineraryInfo.getTravelProduct().getBoardpointDetail() != null) {
				pnrSearchSegment.setOriginPort(itineraryInfo.getTravelProduct().getBoardpointDetail().getCityCode());
			}
			// dest port
			if (itineraryInfo.getTravelProduct().getOffpointDetail() != null) {
				pnrSearchSegment.setDestPort(itineraryInfo.getTravelProduct().getOffpointDetail().getCityCode());
			}
			// segment status
			if (itineraryInfo.getRelatedProduct() != null
					&& !CollectionUtils.isEmpty(itineraryInfo.getRelatedProduct().getStatus())) {
				pnrSearchSegment.setStatus(itineraryInfo.getRelatedProduct().getStatus());
			}

		}
	}
	
	/**
	 * Parser segment arrival time.
	 * @param itineraryInfo
	 * @param segment
	 */
	private void parserArrivalTime(ItineraryInfo itineraryInfo, PnrSearchSegment pnrSearchSegment) {

		if (itineraryInfo.getTravelProduct() != null && itineraryInfo.getTravelProduct().getProduct() != null) {
			if (StringUtils.isEmpty(itineraryInfo.getTravelProduct().getProduct().getArrDate())) {
				return;
			} else if (StringUtils.isEmpty(itineraryInfo.getTravelProduct().getProduct().getArrTime())) {
				pnrSearchSegment.setArrDateTime(
						DateUtil.convertDateFormat(itineraryInfo.getTravelProduct().getProduct().getArrDate(),
								DateUtil.DATE_PATTERN_DDMMYY, PnrSearchSegment.TIME_FORMAT));
			} else {
				
				pnrSearchSegment.setArrDateTime(DateUtil.convertDateFormat(
						itineraryInfo.getTravelProduct().getProduct().getArrDate()
								+ itineraryInfo.getTravelProduct().getProduct().getArrTime(),
						DateUtil.DATE_PATTERN_DDMMYYHHMM, PnrSearchSegment.TIME_FORMAT));
			}
		}
	}
	/**
	 * Parser segment departure time.
	 * @param itineraryInfo
	 * @param segment
	 */
	private void parserDepartureTime(ItineraryInfo itineraryInfo, PnrSearchSegment pnrSearchSegment){
		
		if(itineraryInfo.getTravelProduct() != null && itineraryInfo.getTravelProduct().getProduct() != null){
			if(StringUtils.isEmpty(itineraryInfo.getTravelProduct().getProduct().getDepDate())){
				return;
			}else if(StringUtils.isEmpty(itineraryInfo.getTravelProduct().getProduct().getDepTime())){
				pnrSearchSegment.setDepDateTime(DateUtil.convertDateFormat(itineraryInfo.getTravelProduct().getProduct().getDepDate(), DateUtil.DATE_PATTERN_DDMMYY, PnrSearchSegment.TIME_FORMAT));
			}else{
				pnrSearchSegment.setDepDateTime(DateUtil.convertDateFormat(itineraryInfo.getTravelProduct().getProduct().getDepDate()+itineraryInfo.getTravelProduct().getProduct().getDepTime(), DateUtil.DATE_PATTERN_DDMMYYHHMM, PnrSearchSegment.TIME_FORMAT));
				 
			}
		}
	
	}
	/**
	 * Get the number by qualifier
	 * @param elementManagementSegmentType
	 * @return
	 */
	private String getIdByQualifier(ElementManagementSegmentType elementManagementSegmentType,String qualifier){
		if(null != elementManagementSegmentType.getElementReference() && qualifier.equals(elementManagementSegmentType.getElementReference().getQualifier())){
			return elementManagementSegmentType.getElementReference().getNumber().toString();
		}
		return null;
	}
	
	
	/**
	 * Get rloc from /Body/PNR_SearchReply/pnrViews/pnrView/pnrHeader/reservationInfo/reservation/controlNumber</br>
	 * will use 1A rloc if exist, otherwise use the first one
	 * @param pnrSearchBooking
	 * @param pnrViews
	 */
	private void paserRloc(PnrSearchBooking pnrSearchBooking, StructuredBookingRecordImageType pnrView){
		
		if (pnrView.getPnrHeader() != null && !CollectionUtils.isEmpty(pnrView.getPnrHeader().getReservationInfo())) {
			ReservationControlInformationTypeI72884S reservationInfo = pnrView.getPnrHeader().getReservationInfo().stream()
					.filter(
							res -> res.getReservation() != null && COMPANY_ONEA.equals(res.getReservation().getControlNumber())
							).findFirst().orElse(pnrView.getPnrHeader().getReservationInfo().get(0));
			pnrSearchBooking.setRloc(reservationInfo.getReservation().getControlNumber());
		}
	}
}
