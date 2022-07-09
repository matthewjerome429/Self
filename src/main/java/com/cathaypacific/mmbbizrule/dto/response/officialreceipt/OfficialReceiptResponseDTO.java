package com.cathaypacific.mmbbizrule.dto.response.officialreceipt;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

public class OfficialReceiptResponseDTO extends BaseResponseDTO {
	
	private static final long serialVersionUID = -51241418747726086L;
	
	private List<OfficialReceiptPassengerDTO> passengers;
	
	private Boolean hasBCode;
	
	public List<OfficialReceiptPassengerDTO> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<OfficialReceiptPassengerDTO> passengers) {
		this.passengers = passengers;
	}

	public Boolean getHasBCode() {
		return hasBCode;
	}

	public void setHasBCode(Boolean hasBCode) {
		this.hasBCode = hasBCode;
	}

	public Boolean getNoCXKATicket() {
		if(CollectionUtils.isEmpty(passengers)) {
			return true;
		}
		
		return passengers.stream().allMatch(pax -> !BooleanUtils.isTrue(pax.isCxKaTicket()));
	}
	
}
