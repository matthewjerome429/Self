package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tb_doc_nat_coi_check")
@IdClass(TBTravelDocNatCoiMappingKey.class)
public class TBTravelDocNatCoiMapping {

	@Id
	@NotNull
	@Column(name = "app_code", length = 5)
	private String appCode;
	
	@Id
	@NotNull
	@Column(name = "doc_type", length = 4)
	private String docType;
	
	@Id
	@NotNull
	@Column(name = "nat", length = 3)
	private String nat;
	
	@Id
	@NotNull
	@Column(name = "coi", length = 3)
	private String coi;
	
	@NotNull
	@Column(name = "reminder", length = 1)
	private String reminder;
	
	@NotNull
	@Column(name = "last_update_source", length = 8)
	private String lastUpdateSource;
	
	@NotNull
	@Column(name = "last_update_timestamp")
	private Timestamp lastUpdateTimestamp;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getNat() {
		return nat;
	}

	public void setNat(String nat) {
		this.nat = nat;
	}

	public String getCoi() {
		return coi;
	}

	public void setCoi(String coi) {
		this.coi = coi;
	}

	public String getReminder() {
		return reminder;
	}

	public void setReminder(String reminder) {
		this.reminder = reminder;
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
