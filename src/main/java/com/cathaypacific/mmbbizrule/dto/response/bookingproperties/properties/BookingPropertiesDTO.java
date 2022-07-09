package com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties;

import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class BookingPropertiesDTO extends BaseResponseDTO{

	private static final long serialVersionUID = -3910008840467400558L;
	
	/** properties of passenger */
	private List<PassengerPropertiesDTO> passengerProperties;
	
	/** properties of segment */
	private List<SegmentPropertiesDTO> segmentProperties;
	
	/** properties of passengerSegment */
	private List<PassengerSegmentPropertiesDTO> passengerSegmentProperties;
	
	/** is staff booking */
	private Boolean staffBooking;
	
	/** is group booking */
	private Boolean groupBooking;
	
	/** is mice booking **/
	private Boolean miceBooking;

	public List<PassengerPropertiesDTO> getPassengerProperties() {
		return passengerProperties;
	}
	
	public List<PassengerPropertiesDTO> findPassengerProperties() {
		if(passengerProperties == null){
			passengerProperties = new ArrayList<>();
		}
		return passengerProperties;
	}

	public void setPassengerProperties(List<PassengerPropertiesDTO> passengerProperties) {
		this.passengerProperties = passengerProperties;
	}

	public List<SegmentPropertiesDTO> getSegmentProperties() {
		return segmentProperties;
	}
	
	public List<SegmentPropertiesDTO> findSegmentProperties() {
		if(segmentProperties == null){
			segmentProperties = new ArrayList<>();
		}
		return segmentProperties;
	}

	public void setSegmentProperties(List<SegmentPropertiesDTO> segmentProperties) {
		this.segmentProperties = segmentProperties;
	}

	public List<PassengerSegmentPropertiesDTO> getPassengerSegmentProperties() {
		return passengerSegmentProperties;
	}
	
	public List<PassengerSegmentPropertiesDTO> findPassengerSegmentProperties() {
		if(passengerSegmentProperties == null){
			passengerSegmentProperties = new ArrayList<>();
		}
		return passengerSegmentProperties;
	}

	public void setPassengerSegmentProperties(List<PassengerSegmentPropertiesDTO> passengerSegmentProperties) {
		this.passengerSegmentProperties = passengerSegmentProperties;
	}

	public Boolean getStaffBooking() {
		return staffBooking;
	}

	public void setStaffBooking(Boolean staffBooking) {
		this.staffBooking = staffBooking;
	}

	public Boolean getGroupBooking() {
		return groupBooking;
	}

	public void setGroupBooking(Boolean groupBooking) {
		this.groupBooking = groupBooking;
	}

    public Boolean getMiceBooking() {
        return miceBooking;
    }

    public void setMiceBooking(Boolean miceBooking) {
        this.miceBooking = miceBooking;
    }

}
