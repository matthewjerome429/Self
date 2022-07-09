package com.cathaypacific.mbcommon.model.common;

import java.io.Serializable;
import java.util.List;

public class AdcMessage implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7754027257510362617L;

	/** The message in 1A response */
	private String adcCprMessage;

	/**
	 * the flag of inhibit do on line check in
	 */
	//TODO need to confirm with philip how to handling if the adc message inhibit checkin, because the ADC message always display on confirmation page in mmb inflow olci.
	private boolean inhibitCheckIn;

	private List<String> adcMessageKeys;

	private String passengerId;
	
	private String cprUniqueCustomerId;
	
	private String segmentId;
	
	/** Unique flight id, DID */
	private String cprProductIdentifierDID;
	
	public String getAdcCprMessage() {
		return adcCprMessage;
	}

	public void setAdcCprMessage(String adcCprMessage) {
		this.adcCprMessage = adcCprMessage;
	}

	public List<String> getAdcMessageKeys() {
		return adcMessageKeys;
	}

	public void setAdcMessageKeys(List<String> adcMessageKeys) {
		this.adcMessageKeys = adcMessageKeys;
	}

	public boolean isInhibitCheckIn() {
		return inhibitCheckIn;
	}

	public void setInhibitCheckIn(boolean inhibitCheckIn) {
		this.inhibitCheckIn = inhibitCheckIn;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getCprUniqueCustomerId() {
		return cprUniqueCustomerId;
	}

	public void setCprUniqueCustomerId(String cprUniqueCustomerId) {
		this.cprUniqueCustomerId = cprUniqueCustomerId;
	}

	public String getSegmentId() {
		return segmentId;
	}

	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}

	public String getCprProductIdentifierDID() {
		return cprProductIdentifierDID;
	}

	public void setCprProductIdentifierDID(String cprProductIdentifierDID) {
		this.cprProductIdentifierDID = cprProductIdentifierDID;
	}


}
