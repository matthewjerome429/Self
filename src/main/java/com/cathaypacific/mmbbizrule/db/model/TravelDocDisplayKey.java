package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class TravelDocDisplayKey implements Serializable {
	
	private static final long serialVersionUID = 218342263594600229L;
	
	@NotNull
    @Column(name="travel_doc_version", length =3)
	private int travelDocVersion;
	
	@NotNull
	@Column(name="travel_doc_group", length=2)
	private String travelDocGroup;
	
	@NotNull
	@Column(name = "mandatory", length=1)
	private String mandatory;

	public int getTravelDocVersion() {
		return travelDocVersion;
	}

	public void setTravelDocVersion(int travelDocVersion) {
		this.travelDocVersion = travelDocVersion;
	}

	public String getTravelDocGroup() {
		return travelDocGroup;
	}

	public void setTravelDocGroup(String travelDocGroup) {
		this.travelDocGroup = travelDocGroup;
	}

	public String getMandatory() {
		return mandatory;
	}

	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}
	
}
