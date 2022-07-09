package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="tb_nonair_segment")
@IdClass(NonAirSegmentKey.class)
public class NonAirSegment {
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
    
	@NotNull
	@Column(name = "last_update_source")
	private String lastUpdateSource;

	@NotNull
	@Column(name = "last_update_timestamp")
	private Date lastUpdateTimestamp;

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

	public String getLastUpdateSource() {
		return lastUpdateSource;
	}

	public void setLastUpdateSource(String lastUpdateSource) {
		this.lastUpdateSource = lastUpdateSource;
	}

	public Date getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

}
