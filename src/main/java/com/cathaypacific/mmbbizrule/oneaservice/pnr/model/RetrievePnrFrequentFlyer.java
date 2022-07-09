package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.math.BigInteger;

public class RetrievePnrFrequentFlyer {
	
	/** FQTV company ID */
	private String companyId;
	
	/** FQTV company */
	private String fqtvCompany;
	
	/** FQTV membership number */
	private String fqtvMembershipNumber;
	
	/** FQTV membership tier level */
	private String tierLevel;
	
	/** determine whether tier is top or not*/
	private Boolean isTopTier;
	
	/** OT number in pnr */
	private BigInteger qualifierId;
	
}
