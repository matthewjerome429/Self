package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@IdClass(TravelDocListKey.class)
@Table(name="tb_travel_doc_list")
public class TravelDocList implements Serializable {
	
	private static final long serialVersionUID = 219342263594600329L;

	@Id
	@NotNull
    @Column(name="travel_doc_version", length =3)
	private int travelDocVersion;
	
	@Id
	@NotNull
	@Column(name="travel_doc_code", length =2)
	private String travelDocCode;
	
	@Id
	@NotNull
	@Column(name="travel_doc_type", length =2)
	private String travelDocType;
	
	@NotNull
	@Column(name="last_update_source", length=8)
	private String lastUpdateSource;
	
	@NotNull
	@Column(name="last_update_timestamp")
	private Timestamp lastUpdateTimestamp;

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

	public String getLastUpdateSource() {
		return lastUpdateSource;
	}

	public void setLastUpdateSource(String lastUpdateSource) {
		this.lastUpdateSource = lastUpdateSource;
	}

	public Timestamp getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Timestamp lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

}
