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
@IdClass(TravelDocDisplayKey.class)
@Table(name="tb_travel_doc_display")
public class TbTravelDocDisplay implements Serializable {
	
	private static final long serialVersionUID = 228342263594600229L;

	@Id
	@NotNull
    @Column(name="travel_doc_version", length =3)
	private int travelDocVersion;
	
	@Id
	@NotNull
	@Column(name="travel_doc_group", length=2)
	private String travelDocGroup;
	
	@Id
	@NotNull
	@Column(name = "mandatory", length=1)
	private String mandatory;
	
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
