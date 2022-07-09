package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;

public abstract class Contact implements Serializable {

	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = -8016566310987008842L;

	/** The marketing company of this contact */
	private String marketingCompany;

	/** Service Identifier, SI */
	private String serviceIdentifier;

	/** CPR contact free text is OLCI version format */
	private boolean olciVersion;

	public String getMarketingCompany() {
		return marketingCompany;
	}

	public void setMarketingCompany(String marketingCompany) {
		this.marketingCompany = marketingCompany;
	}

	public String getServiceIdentifier() {
		return serviceIdentifier;
	}

	public void setServiceIdentifier(String serviceIdentifier) {
		this.serviceIdentifier = serviceIdentifier;
	}

	public boolean isOlciVersion() {
		return olciVersion;
	}

	public void setOlciVersion(boolean olciVersion) {
		this.olciVersion = olciVersion;
	}
}
