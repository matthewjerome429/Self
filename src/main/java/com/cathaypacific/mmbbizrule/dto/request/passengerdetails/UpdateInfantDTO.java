package com.cathaypacific.mmbbizrule.dto.request.passengerdetails;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

import com.cathaypacific.mbcommon.annotation.IsUpdated;

import io.swagger.annotations.ApiModel;
@ApiModel
public class UpdateInfantDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2304380235551949561L;

	private String passengerId;
	// destination address
	@IsUpdated
	private UpdateDestinationAddressDTO destinationAddress;
	// KTN
	@IsUpdated
	private UpdateTsDTO ktn;
	// redress no
	@IsUpdated
	private UpdateTsDTO redress;
	@Valid
	private List<UpdateBasicSegmentInfoDTO> segments;
	
	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public List<UpdateBasicSegmentInfoDTO> getSegments() {
		return segments;
	}

	public void setSegments(List<UpdateBasicSegmentInfoDTO> segments) {
		this.segments = segments;
	}

	public UpdateDestinationAddressDTO getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(UpdateDestinationAddressDTO destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public UpdateTsDTO getKtn() {
		return ktn;
	}

	public void setKtn(UpdateTsDTO ktn) {
		this.ktn = ktn;
	}

	public UpdateTsDTO getRedress() {
		return redress;
	}

	public void setRedress(UpdateTsDTO redress) {
		this.redress = redress;
	}

}
