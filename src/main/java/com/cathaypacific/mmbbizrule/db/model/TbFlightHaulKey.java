package com.cathaypacific.mmbbizrule.db.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class TbFlightHaulKey implements Serializable {

	private static final long serialVersionUID = 3266861213245122687L;

	@NotNull
	@Column(name = "APP_CODE", length = 10)
	private String appCode;

	@NotNull
	@Column(name = "APT_CODE_ONE", length = 3)
	private String aptCodeOne;

	@NotNull
	@Column(name = "APT_CODE_TWO", length = 3)
	private String aptCodeTwo;
	
	@NotNull
	@Column(name = "OPT_CX", length =1)
	private String optCx;
	
	@NotNull
	@Column(name = "OPT_KA", length =1)
	private String optKa;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAptCodeOne() {
		return aptCodeOne;
	}

	public void setAptCodeOne(String aptCodeOne) {
		this.aptCodeOne = aptCodeOne;
	}

	public String getAptCodeTwo() {
		return aptCodeTwo;
	}

	public void setAptCodeTwo(String aptCodeTwo) {
		this.aptCodeTwo = aptCodeTwo;
	}

	public String getOptCx() {
		return optCx;
	}

	public void setOptCx(String optCx) {
		this.optCx = optCx;
	}

	public String getOptKa() {
		return optKa;
	}

	public void setOptKa(String optKa) {
		this.optKa = optKa;
	}

}
