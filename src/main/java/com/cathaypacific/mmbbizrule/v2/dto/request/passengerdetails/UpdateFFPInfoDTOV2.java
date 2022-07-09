package com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails;

import javax.validation.constraints.Pattern;

import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.constants.ValidationErrorConstants;
import com.cathaypacific.mbcommon.dto.Updated;

public class UpdateFFPInfoDTOV2 extends Updated{

	private static final long serialVersionUID = 3381839242336253814L;

	/** FFP company ID */
	private String companyId;
	
	/** FFP membership number */
	@Pattern(regexp = "^[A-Za-z0-9 ]{0,25}$", message = ValidationErrorConstants.REQUEST_VALIDATION_INVALID_ERROR_CODE)
	private String membershipNumber;
	
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getMembershipNumber() {
		return membershipNumber;
	}
	public void setMembershipNumber(String membershipNumber) {
		this.membershipNumber = membershipNumber;
	}
	
	@Override
	public boolean isBlank(){
		return StringUtils.isEmpty(companyId) && StringUtils.isEmpty(membershipNumber);
	}
	
}