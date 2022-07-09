package com.cathaypacific.mmbbizrule.dto.response.bookingproperties.additional;

import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingAdditionalInfoResponseDTO extends BaseResponseDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1726926626976771200L;
	
	
	/** plus grade URL */
	private String plusgradeUrl;
	private String olciPlusgradeUrl;
	
	/** properties of passengerSegment */
	/** properties of Segment */

	private List<SegmentAdditionalInfoDTO> segments;
	
	/** properties of passengerSegment */
	private List<PassengerSegmentAdditionalInfoDTO> passengerSegments;
	
	private Boolean hzmTransportationEligible;
	
	private String hzmTransportationType;
	
	private boolean paidLoungePassAvailable;
	
	public List<SegmentAdditionalInfoDTO> getSegments() {
		return segments;
	}

	public String getPlusgradeUrl() {
		return plusgradeUrl;
	}

	public void setPlusgradeUrl(String plusgradeUrl) {
		this.plusgradeUrl = plusgradeUrl;
	}
	
	public String getOlciPlusgradeUrl() {
		return olciPlusgradeUrl;
	}

	public void setOlciPlusgradeUrl(String plusgradeUrl) {
		this.olciPlusgradeUrl = plusgradeUrl;
	}

	public void setSegments(List<SegmentAdditionalInfoDTO> segmentInfo) {
		this.segments = segmentInfo;
	}

	public void addSegment(SegmentAdditionalInfoDTO segmentInfo) {
		segments = segments == null ? new ArrayList<>() : segments;
		segments.add(segmentInfo);
	}

	public List<PassengerSegmentAdditionalInfoDTO> getPassengerSegments() {
		return passengerSegments;
	}

	public void setPassengerSegments(List<PassengerSegmentAdditionalInfoDTO> passengerSegments) {
		this.passengerSegments = passengerSegments;
	}

	public Boolean getHzmTransportationEligible() {
		return hzmTransportationEligible;
	}

	public void setHzmTransportationEligible(Boolean hzmTransportationEligible) {
		this.hzmTransportationEligible = hzmTransportationEligible;
	}

	public String getHzmTransportationType() {
		return hzmTransportationType;
	}

	public void setHzmTransportationType(String hzmTransportationType) {
		this.hzmTransportationType = hzmTransportationType;
	}

	public boolean isPaidLoungePassAvailable() {
		return paidLoungePassAvailable;
	}

	public void setPaidLoungePassAvailable(boolean paidLoungePassAvailable) {
		this.paidLoungePassAvailable = paidLoungePassAvailable;
	}

}
