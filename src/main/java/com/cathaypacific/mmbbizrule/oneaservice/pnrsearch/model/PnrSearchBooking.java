package com.cathaypacific.mmbbizrule.oneaservice.pnrsearch.model;

import java.util.List;

public class PnrSearchBooking {
	/** Format of time*/
	public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm";
	
    private String rloc;
    
    private List<String> sbrAttributes;
    
    private List<PnrSearchSegment> segments;
    
    
	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public List<PnrSearchSegment> getSegments() {
		return segments;
	}

	public void setSegments(List<PnrSearchSegment> segments) {
		this.segments = segments;
	}

	public List<String> getSbrAttributes() {
		return sbrAttributes;
	}

	public void setSbrAttributes(List<String> sbrAttributes) {
		this.sbrAttributes = sbrAttributes;
	}

}
