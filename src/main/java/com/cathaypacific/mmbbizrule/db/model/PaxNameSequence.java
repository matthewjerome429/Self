package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.cathaypacific.mmbbizrule.constant.PaxNameSequenceEnum;

@Entity
@Table(name = "tb_pax_name_sequence")
@IdClass(PaxNameSequenceKey.class)
public class PaxNameSequence {
	@Id
	@NotNull
	@Column(name = "app_code", length = 5)
	private String appCode;

	@NotNull
	@Column(name = "locale", length = 2)
	private String locale;

	@NotNull
	@Column(name = "family_name_sequence")
	private Integer familyNameSequence;

	@NotNull
	@Column(name = "given_name_sequence")
	private Integer givenNameSequence;

	/*
	 When dispalyModelNameBy is 'LF'
     The familyName and givenName replaced with last name and first name
     When dispalyModelNameBy is 'FG'
     The familyName and givenName not need change 
	 */
	@NotNull
	@Column(name = "dispaly_model_with_last_and_first_name")
	@Enumerated(EnumType.STRING)
	private PaxNameSequenceEnum dispalyModelNameBy;

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

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Integer getFamilyNameSequence() {
		return familyNameSequence;
	}

	public void setFamilyNameSequence(Integer familyNameSequence) {
		this.familyNameSequence = familyNameSequence;
	}
	

	public PaxNameSequenceEnum getDispalyModelNameBy() {
		return dispalyModelNameBy;
	}

	public void setDispalyModelNameBy(PaxNameSequenceEnum dispalyModelNameBy) {
		this.dispalyModelNameBy = dispalyModelNameBy;
	}

	public Integer getGivenNameSequence() {
		return givenNameSequence;
	}

	public void setGivenNameSequence(Integer givenNameSequence) {
		this.givenNameSequence = givenNameSequence;
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
