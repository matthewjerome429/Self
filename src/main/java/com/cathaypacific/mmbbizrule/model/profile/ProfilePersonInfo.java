package com.cathaypacific.mmbbizrule.model.profile;

public class ProfilePersonInfo {

	/** Member ID */
	private String memberId;
	/** Member email for RU*/
	private String custUsername;
    
	/** Member Title */
	private String title;

	/** Member family name */
	private String familyName;

	/** Member given name */
	private String givenName;
	
	/** Member gender */
	private String gender;

	/** Member date of birth: yyyy-mm-dd */
	private String dateOfBirth;

	/** Member tier code */
	private String tier;

	/** Club miles of the user */
	private long clubMiles = 0;
	
	/** Club Point of the user */
	private int clubPoint = 0;
	
	/**indicate whether member on holiday */
	private Boolean onHoliday;
	
	/**holiday StartDate*/
	private String holidayStartDate;

	/**holiday EndDate */
	private String holidayEndDate;
	
	/** Asia Miles balance  */ 
	private int asiaMiles = 0 ; 

	/** Member contact */
	private Contact contact;
	
    private boolean paxUnlocked;
	
    /** AM / MPO / RU */
	private String userType;
	
	private String joinDate;
	
	private String birthDate;
	
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}


	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public long getClubMiles() {
		return clubMiles;
	}

	public void setClubMiles(long clubMiles) {
		this.clubMiles = clubMiles;
	}

	public int getClubPoint() {
		return clubPoint;
	}

	public void setClubPoint(int clubPoint) {
		this.clubPoint = clubPoint;
	}

	public String getHolidayEndDate() {
		return holidayEndDate;
	}

	public void setHolidayEndDate(String holidayEndDate) {
		this.holidayEndDate = holidayEndDate;
	}

	public int getAsiaMiles() {
		return asiaMiles;
	}

	public void setAsiaMiles(int asiaMiles) {
		this.asiaMiles = asiaMiles;
	}

	public String getHolidayStartDate() {
		return holidayStartDate;
	}

	public void setHolidayStartDate(String holidayStartDate) {
		this.holidayStartDate = holidayStartDate;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getCustUsername() {
		return custUsername;
	}

	public void setCustUsername(String custUsername) {
		this.custUsername = custUsername;
	}
	public boolean isPaxUnlocked() {
		return paxUnlocked;
	}

	public void setPaxUnlocked(boolean paxUnlocked) {
		this.paxUnlocked = paxUnlocked;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public Boolean getOnHoliday() {
		return onHoliday;
	}

	public void setOnHoliday(Boolean onHoliday) {
		this.onHoliday = onHoliday;
	}
	
}
