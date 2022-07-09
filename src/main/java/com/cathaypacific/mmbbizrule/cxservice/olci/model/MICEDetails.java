package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public class MICEDetails implements Serializable {
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5347325269878934227L;

	/** Possible values: GRMA or GRMB or GRMC */
	private String ssrCode;

	private String serviceType;

	private boolean seatAvailable;

	public String getSsrCode() {
		return ssrCode;
	}

	public void setSsrCode(String ssrCode) {
		this.ssrCode = ssrCode;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public boolean isSeatAvailable() {
		return seatAvailable;
	}

	public void setSeatAvailable(boolean seatAvailable) {
		this.seatAvailable = seatAvailable;
	}
}
