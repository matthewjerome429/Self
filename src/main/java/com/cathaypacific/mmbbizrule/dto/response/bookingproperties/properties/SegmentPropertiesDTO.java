package com.cathaypacific.mmbbizrule.dto.response.bookingproperties.properties;

import java.io.Serializable;

public class SegmentPropertiesDTO implements Serializable{
	
	private static final long serialVersionUID = 6864617691419485140L;

	/** segment id */
	private String segmentId;
	
	/** marketed by CX/KA */
	private Boolean marketedByCxKa;
	
	/** operated by CX/KA */
	private Boolean operatedByCxKa;
	
	/** before 24 hours of STD */
	private Boolean before24H;
	
	/** Wait list */
	private Boolean waitlisted;
	
	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public Boolean isMarketedByCxKa() {
		return marketedByCxKa;
	}

	public void setMarketedByCxKa(Boolean marketedByCxKa) {
		this.marketedByCxKa = marketedByCxKa;
	}

	public Boolean isOperatedByCxKa() {
		return operatedByCxKa;
	}

	public void setOperatedByCxKa(Boolean operatedByCxKa) {
		this.operatedByCxKa = operatedByCxKa;
	}

	public Boolean isBefore24H() {
		return before24H;
	}

	public void setBefore24H(Boolean before24h) {
		before24H = before24h;
	}

	public Boolean isWaitlisted() {
		return waitlisted;
	}

	public void setWaitlisted(Boolean waitlisted) {
		this.waitlisted = waitlisted;
	}
}
