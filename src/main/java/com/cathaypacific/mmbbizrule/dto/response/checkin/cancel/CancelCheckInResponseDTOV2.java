package com.cathaypacific.mmbbizrule.dto.response.checkin.cancel;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.cathaypacific.mbcommon.dto.BaseResponseDTO;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;

import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

public class CancelCheckInResponseDTOV2 extends BaseResponseDTO {

	private static final long serialVersionUID = -2689175079624980801L;
	
	private CancelCheckInCprJourneyDTO cprJourney;
	
	private boolean anyPassengersCancelledSuccess;

	public CancelCheckInResponseDTOV2() {
		super();
	}
	
	public CancelCheckInResponseDTOV2(List<ErrorInfo> errors) {
		super();
		this.setErrors(errors);
	}
	
	public boolean isAnyPassengersCancelledSuccess() {
		return anyPassengersCancelledSuccess;
	}

	/** any passenger cancel check-in success flag*/
	public void setAnyPassengersCancelledSuccess() {
		this.anyPassengersCancelledSuccess = cprJourney != null && CollectionUtils.isNotEmpty(cprJourney.getPassengers()) 
				&& cprJourney.getPassengers().stream().anyMatch(pax -> pax != null 
				&& BooleanUtils.isTrue(pax.isRequestedCancelCheckIn()) && BooleanUtils.isFalse(pax.isCheckedIn()));;
	}

	public CancelCheckInCprJourneyDTO getCprJourney() {
		return cprJourney;
	}

	public void setCprJourney(CancelCheckInCprJourneyDTO cprJourney) {
		this.cprJourney = cprJourney;
	}
	
}
