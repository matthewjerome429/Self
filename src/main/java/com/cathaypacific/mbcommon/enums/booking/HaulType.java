package com.cathaypacific.mbcommon.enums.booking;

import io.swagger.annotations.ApiModelProperty;

public enum HaulType {
	
	/** short haul*/
	@ApiModelProperty(value="Short Haul")
	S,
	
	/** medium short haul*/
	@ApiModelProperty(value="Medium Short Haul")
	MS,
	
	/** ultra short haul*/
	@ApiModelProperty(value="Ultra Short Haul")
	US,
	
	/** medium haul*/
	@ApiModelProperty(value="Medium Haul")
	M,
	
	/** long haul*/
	@ApiModelProperty(value="Long Haul")
	L,
	
	/** medium long haul*/
	@ApiModelProperty(value="Medium Long Haul")
	ML,
	
	/** ultra long haul*/
	@ApiModelProperty(value="Ultra Long Haul")
	UL;
	
}
