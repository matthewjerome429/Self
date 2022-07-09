package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

public class MemberAwardDTOV2 {
	private long asiaMiles;
	
	private long clubPoint;
	
	private long tierBonus;
	
	private String tierLevel;

	public long getAsiaMiles() {
		return asiaMiles;
	}

	public void setAsiaMiles(long asiaMiles) {
		this.asiaMiles = asiaMiles;
	}

	public long getClubPoint() {
		return clubPoint;
	}

	public void setClubPoint(long clubPoint) {
		this.clubPoint = clubPoint;
	}

	public long getTierBonus() {
		return tierBonus;
	}

	public void setTierBonus(long tierBonus) {
		this.tierBonus = tierBonus;
	}

	public String getTierLevel() {
		return tierLevel;
	}

	public void setTierLevel(String tierLevel) {
		this.tierLevel = tierLevel;
	}
	
}
