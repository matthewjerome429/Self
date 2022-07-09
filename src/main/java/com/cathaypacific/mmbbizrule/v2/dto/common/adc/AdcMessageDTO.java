package com.cathaypacific.mmbbizrule.v2.dto.common.adc;

import java.io.Serializable;
import java.util.List;

public class AdcMessageDTO implements Serializable {


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
	//private boolean inhibitCheckIn;

	private List<String> adcMessageKeys;

	/**
	 * cpr passenger id
	 */
	private String uniqueCustomerId;
	
	/**
	 * cpr segment Id
	 */
	private String productIdentifierDID;
	
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

	public String getUniqueCustomerId() {
		return uniqueCustomerId;
	}

	public void setUniqueCustomerId(String uniqueCustomerId) {
		this.uniqueCustomerId = uniqueCustomerId;
	}

	public String getProductIdentifierDID() {
		return productIdentifierDID;
	}

	public void setProductIdentifierDID(String productIdentifierDID) {
		this.productIdentifierDID = productIdentifierDID;
	}

}
