package com.cathaypacific.mmbbizrule.dto.common;

public class GroupBaseDTO {
	
	// the lock function removed from sprint20, keep this bean to identify the bundle

	private Boolean unlocked = true;

	public Boolean getUnlocked() {
		return unlocked;
	}

	public void setUnlocked(Boolean unlocked) {
		this.unlocked = unlocked;
	}
	
}
