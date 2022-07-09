package com.cathaypacific.mbcommon.model.common;

import java.util.List;

public class TokenMaskInfo {

	private List<MaskInfo> maskInfos;
	
	private boolean maskInfoUpdated;

	public List<MaskInfo> getMaskInfos() {
		return maskInfos;
	}

	public void setMaskInfos(List<MaskInfo> maskInfos) {
		this.maskInfos = maskInfos;
	}

	public boolean isMaskInfoUpdated() {
		return maskInfoUpdated;
	}

	public void setMaskInfoUpdated(boolean maskInfoUpdated) {
		this.maskInfoUpdated = maskInfoUpdated;
	}
	
}
