package com.cathaypacific.mmbbizrule.dto.response.bookingcancel;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

public class CancelBookingDataResponseDTO extends BaseResponseDTO {

	private static final long serialVersionUID = 6383798651862809743L;

	private String encryptedData;

	private String aedKey;

	public String getEncryptedData() {
		return encryptedData;
	}

	public void setEncryptedData(String encryptedData) {
		this.encryptedData = encryptedData;
	}

	public String getAedKey() {
		return aedKey;
	}

	public void setAedKey(String aedKey) {
		this.aedKey = aedKey;
	}

}
