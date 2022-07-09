package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class TravelDocListKey implements Serializable {
	
	private static final long serialVersionUID = 218342263594600279L;
	
	@NotNull
    @Column(name="travel_doc_version", length =3)
	private int travelDocVersion;
	
	@NotNull
	@Column(name="travel_doc_code", length =2)
	private String travelDocCode;
	
	@NotNull
	@Column(name="travel_doc_type", length =2)
	private String travelDocType;

	public int getTravelDocVersion() {
		return travelDocVersion;
	}

	public void setTravelDocVersion(int travelDocVersion) {
		this.travelDocVersion = travelDocVersion;
	}

	public String getTravelDocCode() {
		return travelDocCode;
	}

	public void setTravelDocCode(String travelDocCode) {
		this.travelDocCode = travelDocCode;
	}

	public String getTravelDocType() {
		return travelDocType;
	}

	public void setTravelDocType(String travelDocType) {
		this.travelDocType = travelDocType;
	}
	
}
