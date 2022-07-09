package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.math.BigInteger;

public class SpecialService {
	
	/** the qulifier id*/
	private BigInteger qulifierId;

	/** SSR/SK type e.g.STFD */
    private String code;
    
    /**
     *  mmb status e.g.CF,RO,RJ
     */
    private String status;
    
    /**
     *  additional status for special ssr
     */
    private String additionalStatus;
    
    /** the free text*/
    private String freeText;

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

    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof SpecialService)) 
        	return false;
        SpecialService specialService = (SpecialService)obj;
        return code.equals(specialService.getCode());
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }

	public String getAdditionalStatus() {
		return additionalStatus;
	}

	public void setAdditionalStatus(String additionalStatus) {
		this.additionalStatus = additionalStatus;
	}

	public String getFreeText() {
		return freeText;
	}

	public void setFreeText(String freeText) {
		this.freeText = freeText;
	}
}
