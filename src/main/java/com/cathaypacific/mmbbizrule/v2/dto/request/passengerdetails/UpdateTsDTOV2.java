package com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails;

import java.math.BigInteger;

import javax.validation.constraints.Pattern;

import org.apache.commons.lang.StringUtils;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mbcommon.dto.Updated;
import com.cathaypacific.mmbbizrule.validator.group.MaskFieldGroup;

public class UpdateTsDTOV2 extends Updated {

	private static final long serialVersionUID = 6555491124527153896L;
	
	private BigInteger qualifierId;
	
	@Pattern(regexp = "^[A-Za-z0-9]{0,25}$", groups = {MaskFieldGroup.class}, message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String number;

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
	
	@Override
	public boolean isBlank() {
		return StringUtils.isEmpty(number);
	}

}
