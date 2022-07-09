package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Embeddable
public class NonAirSegmentKey implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6671366641044056321L;
	@Id
	@NotNull
    @Column(name="app_code", length = 5)
    private String appCode;
	@Id
    @NotNull
    @Column(name = "airline_code")
    private String airlineCode;
	@Id
    @NotNull
    @Column(name = "origin")
    private String origin;
	@Id
    @NotNull
    @Column(name = "destination")
    private String destination;
	@Id
    @NotNull
    @Column(name = "type")
    private String type;
    

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAirlineCode() {
		return airlineCode;
	}

	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
