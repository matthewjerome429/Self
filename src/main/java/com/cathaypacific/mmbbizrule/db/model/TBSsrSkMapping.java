package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

@Entity
@IdClass(TBSsrSkMappingKey.class)
@Table(name = "TB_SSR_SK_MAPPING")
public class TBSsrSkMapping {
	@Id
	@NotNull
	@Column(name = "APP_CODE", length = 5)
	private String appCode;
	@Id
	@NotNull
	@Column(name = "SSR_SK_CODE", length = 4)
	private String ssrSkCode;

	@NotNull
	@Column(name = "ACCEPTANCE")
	private String acceptance;

	@NotNull
	@Column(name = "BP")
	private String bp;

	@NotNull
	@Column(name = "SEAT")
	private String seat;
	
	@NotNull
	@Column(name = "SPECIAL_SEAT")
	private String specialSeat;

	@Column(name = "MSG_CODE_ACCEPTANCE")
	private String msgCodeAcceptance;

	@Column(name = "MSG_CODE_BP")
	private String msgCodeBp;

	@Column(name = "UPGRADE_BID")
	private String upgradeBid;
	
	@Column(name = "ASR_FOC")
	private String asrFOC;
	
	@Column(name = "COMPANION_ASR_FOC")
	private String companionAsrFOC;
	
	@Column(name = "SELECTED_ASR_FOC")
	private String selectedAsrFOC;
	
	@CreatedBy
	@NotNull
	@Column(name = "LAST_UPDATE_SOURCE")
	private String lastUpdateSource;

	@CreatedDate
	@NotNull
	@Column(name = "LAST_UPDATE_TIMESTAMP")
	private Timestamp lastUpdateTimestamp;

	public TBSsrSkMapping() {

	}

	public TBSsrSkMapping(String appCode, String ssrSkCode, String acceptance, String bp, String seat,
			String specialSeat, String msgCodeAcceptance, String msgCodeBp, String upgradeBid, String lastUpdateSource,
			Timestamp lastUpdateTimestamp) {
		this.appCode = appCode;
		this.ssrSkCode = ssrSkCode;
		this.acceptance = acceptance;
		this.bp = bp;
		this.seat = seat;
		this.specialSeat = specialSeat;
		this.msgCodeAcceptance = msgCodeAcceptance;
		this.msgCodeBp = msgCodeBp;
		this.upgradeBid = upgradeBid;
		this.lastUpdateSource = lastUpdateSource;
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getSsrSkCode() {
		return ssrSkCode;
	}

	public void setSsrSkCode(String ssrSkCode) {
		this.ssrSkCode = ssrSkCode;
	}

	public String getAcceptance() {
		return acceptance;
	}

	public void setAcceptance(String acceptance) {
		this.acceptance = acceptance;
	}

	public String getBp() {
		return bp;
	}

	public void setBp(String bp) {
		this.bp = bp;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public String getSpecialSeat() {
		return specialSeat;
	}

	public void setSpecialSeat(String specialSeat) {
		this.specialSeat = specialSeat;
	}

	public String getMsgCodeAcceptance() {
		return msgCodeAcceptance;
	}

	public void setMsgCodeAcceptance(String msgCodeAcceptance) {
		this.msgCodeAcceptance = msgCodeAcceptance;
	}

	public String getMsgCodeBp() {
		return msgCodeBp;
	}

	public void setMsgCodeBp(String msgCodeBp) {
		this.msgCodeBp = msgCodeBp;
	}

	public String getUpgradeBid() {
		return upgradeBid;
	}

	public void setUpgradeBid(String upgradeBid) {
		this.upgradeBid = upgradeBid;
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

	public String getAsrFOC() {
		return asrFOC;
	}

	public void setAsrFOC(String asrFOC) {
		this.asrFOC = asrFOC;
	}

	public String getCompanionAsrFOC() {
		return companionAsrFOC;
	}

	public void setCompanionAsrFOC(String companionAsrFOC) {
		this.companionAsrFOC = companionAsrFOC;
	}

	public String getSelectedAsrFOC() {
		return selectedAsrFOC;
	}

	public void setSelectedAsrFOC(String selectedAsrFOC) {
		this.selectedAsrFOC = selectedAsrFOC;
	}

}
