package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;
import java.math.BigInteger;

import org.apache.commons.lang.StringUtils;

import com.cathaypacific.mmbbizrule.dto.common.GroupBaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class KtnRedressDTOV2 extends GroupBaseDTO implements Serializable {
	
	private static final long serialVersionUID = -4542003006584510315L;

	private BigInteger qualifierId;
	
	private String number;
	
	private Boolean numberMasked;

	public BigInteger getQualifierId() {
		return qualifierId;
	}

	public void setQualifierId(BigInteger qualifierId) {
		this.qualifierId = qualifierId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public Boolean getNumberMasked() {
		return numberMasked;
	}

	public void setNumberMasked(Boolean numberMasked) {
		this.numberMasked = numberMasked;
	}

	@JsonIgnore
	public boolean isEmpty(){
		
		if(StringUtils.isEmpty(number)){
			return true;
		} else {
			return false;
		}
	}
}


