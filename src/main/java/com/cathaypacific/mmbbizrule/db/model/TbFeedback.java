package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TB_FEEDBACK")
@IdClass(TbFeedbackKey.class)
public class TbFeedback {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	
	@NotNull
	@Column(name = "APP_CODE", length = 5)
	private String appCode;

	@Column(name = "RLOC", length = 7)
	private String rloc;

	@Column(name = "MEMBER_ID", length = 10)
	private String memberId;

	@Column(name = "E_TICKET")
	private String eticket;
	
	@NotNull
	@Column(name = "PAGE", length = 8)
	private String page;
	
	@NotNull
	@Column(name = "STAR_RATING", length = 1)
	private int starRating;

	@Column(name = "MC_OPTION", length = 5)
	private String mcOption;

	@Column(name = "COMMENT", length = 1000)
	private String comment;

	@NotNull
	@Column(name = "last_update_source", length = 8)
	private String lastUpdateSource;
	
	@NotNull
	@Column(name = "last_update_timestamp")
	private Timestamp lastUpdateTimestamp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getEticket() {
		return eticket;
	}

	public void setEticket(String eticket) {
		this.eticket = eticket;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public int getStarRating() {
		return starRating;
	}

	public void setStarRating(int starRating) {
		this.starRating = starRating;
	}

	public String getMcOption() {
		return mcOption;
	}

	public void setMcOption(String mcOption) {
		this.mcOption = mcOption;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
