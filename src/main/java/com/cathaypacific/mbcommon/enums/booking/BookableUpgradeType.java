package com.cathaypacific.mbcommon.enums.booking;

import io.swagger.annotations.ApiModelProperty;

public enum BookableUpgradeType {
	/** For short/long haul*/
	@ApiModelProperty(value="Short Haul")
	BUD,
	
	/** For short haul*/
	@ApiModelProperty(value="Medium Short Haul")
	BUG;
}
