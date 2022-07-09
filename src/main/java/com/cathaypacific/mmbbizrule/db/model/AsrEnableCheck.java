package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import static com.cathaypacific.mmbbizrule.constant.TBConstants.TB_REDEMPTION_ASR_IS_FOC;

@Entity
@Table(name = "tb_redemption_asr_check")
@IdClass(AsrEnableCheckKey.class)
public class AsrEnableCheck {
	@Id
	@NotNull
	@Column(name = "app_code", length = 5)
	private String appCode;
	
	@Id
	@NotNull
	@Column(name = "office_id")
	private String officeId;
	
	@Id
	@NotNull
	@Column(name = "tpos")
	private String tpos;
	
	@NotNull
	@Column(name = "seat_selection")
	private boolean seatSelection;
	
	@NotNull
	@Column(name = "last_update_source")
	private String lastUpdateSource;

	@NotNull
	@Column(name = "last_update_timestamp")
	private Date lastUpdateTimeStamp;

    @NotNull
    @Column(name = "foc")
    private String foc;

    public String getAppCode() {
        return appCode;
    }

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getTpos() {
		return tpos;
	}

	public void setTpos(String tpos) {
		this.tpos = tpos;
	}

	public boolean isSeatSelection() {
		return seatSelection;
	}

	public void setSeatSelection(boolean seatSelection) {
		this.seatSelection = seatSelection;
	}

	public String getLastUpdateSource() {
		return lastUpdateSource;
	}

	public void setLastUpdateSource(String lastUpdateSource) {
		this.lastUpdateSource = lastUpdateSource;
	}

	public Date getLastUpdateTimeStamp() {
		return lastUpdateTimeStamp;
	}

    public void setLastUpdateTimeStamp(Date lastUpdateTimeStamp) {
        this.lastUpdateTimeStamp = lastUpdateTimeStamp;
    }

    public String getFoc() {
        return foc;
    }

    public void setFoc(String foc) {
        this.foc = foc;
    }

    public boolean isFoc() {
        return TB_REDEMPTION_ASR_IS_FOC.equals(this.foc);
    }
}
