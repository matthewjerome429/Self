package com.cathaypacific.mmbbizrule.cxservice.changeflight.model;

import java.io.Serializable;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.common.BaseResponseDTO;

public class ChangeFlightEligibleResponseDTO extends BaseResponseDTO implements Serializable{
	
	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = -3268959444143634290L;
		 	
	private boolean canChangeFlight;
	
	private boolean atcBooking;
	
	private String retrieveOfficeId;
	
	private String timeStamp;
	
	public boolean isCanChangeFlight() {
		return canChangeFlight;
	}

	public void setCanChangeFlight(boolean canChangeFlight) {
		this.canChangeFlight = canChangeFlight;
	}

	public boolean isAtcBooking() {
		return atcBooking;
	}

	public void setAtcBooking(boolean atcBooking) {
		this.atcBooking = atcBooking;
	}

	public String getRetrieveOfficeId() {
		return retrieveOfficeId;
	}

	public void setRetrieveOfficeId(String retrieveOfficeId) {
		this.retrieveOfficeId = retrieveOfficeId;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

}
