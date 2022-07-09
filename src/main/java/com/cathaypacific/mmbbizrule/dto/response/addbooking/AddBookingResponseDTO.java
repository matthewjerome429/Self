package com.cathaypacific.mmbbizrule.dto.response.addbooking;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mmbbizrule.constant.BookingAddTypeEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class AddBookingResponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 2927899836393964334L;
	
	@ApiModelProperty(value="add booking is success or not", example="true")
	private Boolean success;
	
	@ApiModelProperty(dataType="String", value="booking add type", example="FQTV")
	private BookingAddTypeEnum addType;
	
	@ApiModelProperty(value="CX FQTV already exist in the booking or not for RU member", example="false")
	private Boolean mpoExist;
	
	@ApiModelProperty(value="booking RLOC", example="CXDFGH")
	private String rloc;

	@ApiModelProperty(value="the AM/MPO member id which is found in the booking while login with RU member", example="123456789012")
	private String mpoMemberId;
	
	@ApiModelProperty(value="the login user is one of the travellers or not", example="false")
	private Boolean companionBooking;
	
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public BookingAddTypeEnum getAddType() {
		return addType;
	}

	public void setAddType(BookingAddTypeEnum addType) {
		this.addType = addType;
	}

	public Boolean getMpoExist() {
		return mpoExist;
	}

	public void setMpoExist(Boolean mpoExist) {
		this.mpoExist = mpoExist;
	}

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public String getMpoMemberId() {
		return mpoMemberId;
	}

	public void setMpoMemberId(String mpoMemberId) {
		this.mpoMemberId = mpoMemberId;
	}

	public Boolean getCompanionBooking() {
		return companionBooking;
	}

	public void setCompanionBooking(Boolean isCompanionBooking) {
		this.companionBooking = isCompanionBooking;
	}

}
