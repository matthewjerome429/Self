package com.cathaypacific.mmbbizrule.v2.dto.request.passengerdetails;

import java.io.Serializable;

import com.cathaypacific.mbcommon.annotation.IsUpdated;

public class UpdateDestinationDTOV2 implements Serializable {

	private static final long serialVersionUID = 2226047896024879210L;

	private boolean transit;

	@IsUpdated
	private UpdateDestinationAddressDTOV2 destinationAddress;

	public boolean isTransit() {
		return transit;
	}

	public void setTransit(boolean transit) {
		this.transit = transit;
	}

	public UpdateDestinationAddressDTOV2 getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(UpdateDestinationAddressDTOV2 destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

}
