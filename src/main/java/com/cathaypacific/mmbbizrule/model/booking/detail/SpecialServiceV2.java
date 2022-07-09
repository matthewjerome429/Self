package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.math.BigInteger;

public class SpecialServiceV2 {
	
	/** 
	 * the qulifier id
	 * */
	private BigInteger qulifierId;

	/** 
	 * SSR/SK type e.g.STFD 
	 * */
    private String code;
    
    /**
     *  mmb status e.g.CF,RO,RJ
     */
    private String status;
    
    /**
     *  additional status for special ssr
     */
    private String additionalStatus;

	public BigInteger getQulifierId() {
		return qulifierId;
	}

	public void setQulifierId(BigInteger qulifierId) {
		this.qulifierId = qulifierId;
	}

	public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public String getAdditionalStatus() {
		return additionalStatus;
	}

	public void setAdditionalStatus(String additionalStatus) {
		this.additionalStatus = additionalStatus;
	}

}
