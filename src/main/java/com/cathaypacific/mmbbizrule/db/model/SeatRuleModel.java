package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="tb_seat_rule")
@IdClass(SeatRuleKey.class)
public class SeatRuleModel {

	@Id
	@NotNull
    @Column(name="app_code")
	private String appCode;
	
	@Id
	@NotNull
    @Column(name="type")
	private String type;
	
	@Id
	@NotNull
    @Column(name="value")
	private String value;
	
	@Column(name="asr_foc")
	private String asrFOC;
	
	@Column(name="seat_select")
	private String seatSelect;
	
	@Column(name="low_rbd")
	private String lowRBD;
	
	@NotNull
    @Column(name = "last_update_source")
    private String lastUpdateSource;

    @NotNull
    @Column(name = "last_update_timestamp")
    private Date lastUpdateTimeStamp;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSeatSelect() {
		return seatSelect;
	}

	public void setSeatSelect(String seatSelect) {
		this.seatSelect = seatSelect;
	}

	public String getLowRBD() {
		return lowRBD;
	}

	public void setLowRBD(String lowRBD) {
		this.lowRBD = lowRBD;
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

	

	public String getAsrFOC() {
		return asrFOC;
	}

	public void setAsrFOC(String asrFOC) {
		this.asrFOC = asrFOC;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appCode == null) ? 0 : appCode.hashCode());
		result = prime * result + ((lastUpdateSource == null) ? 0 : lastUpdateSource.hashCode());
		result = prime * result + ((lastUpdateTimeStamp == null) ? 0 : lastUpdateTimeStamp.hashCode());
		result = prime * result + ((lowRBD == null) ? 0 : lowRBD.hashCode());
		result = prime * result + ((asrFOC == null) ? 0 : asrFOC.hashCode());
		result = prime * result + ((seatSelect == null) ? 0 : seatSelect.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SeatRuleModel other = (SeatRuleModel) obj;
		if (appCode == null) {
			if (other.appCode != null)
				return false;
		} else if (!appCode.equals(other.appCode))
			return false;
		if (lastUpdateSource == null) {
			if (other.lastUpdateSource != null)
				return false;
		} else if (!lastUpdateSource.equals(other.lastUpdateSource))
			return false;
		if (lastUpdateTimeStamp == null) {
			if (other.lastUpdateTimeStamp != null)
				return false;
		} else if (!lastUpdateTimeStamp.equals(other.lastUpdateTimeStamp))
			return false;
		if (lowRBD == null) {
			if (other.lowRBD != null)
				return false;
		} else if (!lowRBD.equals(other.lowRBD))
			return false;
		if (asrFOC == null) {
			if (other.asrFOC != null)
				return false;
		} else if (!asrFOC.equals(other.asrFOC))
			return false;
		if (seatSelect == null) {
			if (other.seatSelect != null)
				return false;
		} else if (!seatSelect.equals(other.seatSelect))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SeatRuleModel [appCode=" + appCode + ", type=" + type + ", value=" + value + ", asrFOC=" + asrFOC
				+ ", seatSelect=" + seatSelect + ", lowRBD=" + lowRBD + ", lastUpdateSource=" + lastUpdateSource
				+ ", lastUpdateTimeStamp=" + lastUpdateTimeStamp + "]";
	}
}
