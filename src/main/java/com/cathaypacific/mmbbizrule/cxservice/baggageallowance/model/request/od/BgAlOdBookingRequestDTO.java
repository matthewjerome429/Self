package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.od;

import java.io.Serializable;
import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.common.od.BgAlOdSegmentDTO;

public class BgAlOdBookingRequestDTO implements Serializable {

	private static final long serialVersionUID = 4899155687906581837L;

	private String bookingId;

	private List<BgAlOdSegmentDTO> segments;

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public List<BgAlOdSegmentDTO> getSegments() {
		return segments;
	}

	public void setSegments(List<BgAlOdSegmentDTO> segments) {
		this.segments = segments;
	}

}
