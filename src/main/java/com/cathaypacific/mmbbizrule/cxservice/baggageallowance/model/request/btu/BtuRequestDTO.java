package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.btu;

import java.io.Serializable;
import java.util.List;

public class BtuRequestDTO implements Serializable {

	private static final long serialVersionUID = 138192847327279368L;

	private String btuId;

	private List<BgAlBtuSegmentDTO> segments;

	private String baggageAllowanceType;

	public String getBtuId() {
		return btuId;
	}

	public void setBtuId(String btuId) {
		this.btuId = btuId;
	}

	public List<BgAlBtuSegmentDTO> getSegments() {
		return segments;
	}

	public void setSegments(List<BgAlBtuSegmentDTO> segments) {
		this.segments = segments;
	}

	public String getBaggageAllowanceType() {
		return baggageAllowanceType;
	}

	public void setBaggageAllowanceType(String baggageAllowanceType) {
		this.baggageAllowanceType = baggageAllowanceType;
	}

}
