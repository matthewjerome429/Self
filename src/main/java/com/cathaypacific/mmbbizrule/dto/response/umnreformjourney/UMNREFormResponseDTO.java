package com.cathaypacific.mmbbizrule.dto.response.umnreformjourney;

import java.util.List;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class UMNREFormResponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = -6937566658129791781L;

	private String oneARloc;
	
	private String ojRloc;
	
	private String gdsRloc;
	
	private List<UMNREFormSegmentDTO> segments;
	
	private List<UMNREFormPassengerDTO> passengers;
	
	public String getOneARloc() {
		return oneARloc;
	}

	public void setOneARloc(String oneARloc) {
		this.oneARloc = oneARloc;
	}

	public List<UMNREFormSegmentDTO> getSegments() {
		return segments;
	}

	public void setSegments(List<UMNREFormSegmentDTO> segments) {
		this.segments = segments;
	}

	public List<UMNREFormPassengerDTO> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<UMNREFormPassengerDTO> passengers) {
		this.passengers = passengers;
	}

	public String getOjRloc() {
		return ojRloc;
	}

	public void setOjRloc(String ojRloc) {
		this.ojRloc = ojRloc;
	}

	public String getGdsRloc() {
		return gdsRloc;
	}

	public void setGdsRloc(String gdsRloc) {
		this.gdsRloc = gdsRloc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
