package com.cathaypacific.mbcommon.model.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.cathaypacific.mbcommon.enums.booking.BookingDataGroupEnum;
/**
 * TODO will remove lock info in sprint 20
 * @author zilong.bu
 *
 */
@Deprecated
public class UnlockedPaxInfo {

	private String passengerId;
	
	private boolean unlocked = false;
	/** To Identify the groups checked or not, only need do check once for the groups.*/
	private boolean groupsChecked;
	/** Customer unlocked groups */
	private Set<BookingDataGroupEnum> customerLevelUnlockedGroups;

	/**key:ST, segment id in pnr; value: unlocked groups */
	private Map<String , Set<BookingDataGroupEnum>> productLevelUnlockedGroupsMap;
	
	public boolean isUnlocked() {
		return unlocked;
	}

	public void setUnlocked(boolean unlocked) {
		this.unlocked = unlocked;
	}
 
	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public Map<String, Set<BookingDataGroupEnum>> getProductLevelUnlockedGroupsMap() {
		return productLevelUnlockedGroupsMap;
	}

	public void setProductLevelUnlockedGroupsMap(Map<String, Set<BookingDataGroupEnum>> productLevelUnlockedGroupsMap) {
		this.productLevelUnlockedGroupsMap = productLevelUnlockedGroupsMap;
	}

	public Set<BookingDataGroupEnum> getCustomerLevelUnlockedGroups() {
		return customerLevelUnlockedGroups;
	}

	public void setCustomerLevelUnlockedGroups(Set<BookingDataGroupEnum> customerLevelUnlockedGroups) {
		this.customerLevelUnlockedGroups = customerLevelUnlockedGroups;
	}
	
	public void addCustomerLevelUnlockedGroups(Set<BookingDataGroupEnum> customerLevelUnlockedGroups) {
		if(this.customerLevelUnlockedGroups == null){
			this.customerLevelUnlockedGroups =  new HashSet<>();
		}
		this.customerLevelUnlockedGroups.addAll(customerLevelUnlockedGroups);
	}
	
	public void addProductLevelUnlockedGroups(String segmentId, Set<BookingDataGroupEnum> productLevelUnlockedGroups) {

		if (this.productLevelUnlockedGroupsMap == null) {
			this.productLevelUnlockedGroupsMap = new HashMap<>();
		}
		this.productLevelUnlockedGroupsMap.computeIfAbsent(segmentId, k -> new HashSet<>()).addAll(productLevelUnlockedGroups);
	}

	public boolean isGroupsChecked() {
		return groupsChecked;
	}

	public void setGroupsChecked(boolean groupsChecked) {
		this.groupsChecked = groupsChecked;
	}

	
}
