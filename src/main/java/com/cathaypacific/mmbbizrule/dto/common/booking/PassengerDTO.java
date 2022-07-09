package com.cathaypacific.mmbbizrule.dto.common.booking;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PassengerDTO implements Serializable{

	private static final long serialVersionUID = 6820444347479622072L;
	/** The passenger id. */
    private String passengerId;
    /** If The passenger is infant without seat, its value will be his parentId. */
    private String parentId;
    /** The passenger type. */
    private String passengerType;
    /** The title. */
    private String title;
    /** The family name. */
    private String familyName;
    /** The given name. */
    private String givenName;
    /**
     * the primary passenger in the booking,
     * For nun member: it is the login passenger<br>
     * For member: it is member self, if cannot find member self in the booking, it is member's companion in profile
     */
	private Boolean primaryPassenger;
	/**
	 * The Companion passenger
	 */
	private Boolean companion;
	/**
	 * The member self if login as member, else will set null. 
	 */
	private Boolean loginMember;
	/**
	 * FQTV/FQTR of login member matched with login member id
	 */
	private boolean loginFFPMatched;
	/** The passenger contact info. */
	private ContactInfoDTO contactInfo;
	/** The passenger member profile contact info. */
	private ContactInfoDTO mpContactInfo;
	/** The emergency contact info. */
	private EmrContactInfoDTO emrContactInfo;
	/** The destination address info. */
	private DestinaitionAddressDTO destinaitionAddress;

	/** user type */
	private String userType;
	
	private KtnRedressDTO ktn;
	
	private KtnRedressDTO redress;
	/** The DOB from 1A travellerInfo */
	private DobDTO dob;
	
	/** True if has UMNR ssr*/
	private boolean unaccompaniedMinor;	/** a sign which used for to tell the passenger is masked or not**/

	private boolean paxUnlocked = true;
	
	/** Passenger Display */
	private PassengerDisplayDTO display;
	
	public String getPassengerType() {
		return passengerType;
	}
	public void setPassengerType(String passengerType) {
		this.passengerType = passengerType;
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
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public ContactInfoDTO getContactInfo() {
		return contactInfo;
	}
	public ContactInfoDTO findContactInfo() {
		if(contactInfo == null) {
			contactInfo = new ContactInfoDTO();
		}
		return contactInfo;
	}
	
	public void setContactInfo(ContactInfoDTO contactInfo) {
		this.contactInfo = contactInfo;
	}
	public EmrContactInfoDTO getEmrContactInfo() {
		return emrContactInfo;
	}
	public EmrContactInfoDTO findEmrContactInfo(){
		if(emrContactInfo == null) {
			emrContactInfo = new EmrContactInfoDTO();
		}
		return emrContactInfo;
		
	}
	public void setEmrContactInfo(EmrContactInfoDTO emrContactInfo) {
		this.emrContactInfo = emrContactInfo;
	}
	public DestinaitionAddressDTO getDestinaitionAddress() {
		return destinaitionAddress;
	}
	public DestinaitionAddressDTO findDestinaitionAddress() {
		if(destinaitionAddress == null) {
			destinaitionAddress = new DestinaitionAddressDTO();
		}
		return destinaitionAddress;
	}
	public void setDestinaitionAddress(DestinaitionAddressDTO destinaitionAddress) {
		this.destinaitionAddress = destinaitionAddress;
	}
	public Boolean getPrimaryPassenger() {
		return primaryPassenger;
	}
	public void setPrimaryPassenger(Boolean primaryPassenger) {
		this.primaryPassenger = primaryPassenger;
	}
	public Boolean getLoginMember() {
		return loginMember;
	}
	public void setLoginMember(Boolean loginMember) {
		this.loginMember = loginMember;
	}
	public boolean isLoginFFPMatched() {
		return loginFFPMatched;
	}
	public void setLoginFFPMatched(boolean loginFFPMatched) {
		this.loginFFPMatched = loginFFPMatched;
	}
	public String getPassengerId() {
		return passengerId;
	}
	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public ContactInfoDTO getMpContactInfo() {
		return mpContactInfo;
	}
	public void setMpContactInfo(ContactInfoDTO mpContactInfo) {
		this.mpContactInfo = mpContactInfo;
	}
	public Boolean isCompanion() {
		return companion;
	}
	public void setCompanion(Boolean companion) {
		this.companion = companion;
	}
	public boolean isUnaccompaniedMinor() {
		return unaccompaniedMinor;
	}
	public void setUnaccompaniedMinor(boolean unaccompaniedMinor) {
		this.unaccompaniedMinor = unaccompaniedMinor;
	}
	public PassengerDisplayDTO getDisplay() {
		return display;
	}
	public PassengerDisplayDTO findDisplay() {
		if (display == null) {
			display = new PassengerDisplayDTO();
		}
		return display;
	}
	public void setDisplay(PassengerDisplayDTO display) {
		this.display = display;
	}
	public DobDTO getDob() {
		return dob;
	}
	public void setDob(DobDTO dob) {
		this.dob = dob;
	} 
	public KtnRedressDTO getKtn() {
		return ktn;
	}
	public void setKtn(KtnRedressDTO ktn) {
		this.ktn = ktn;
	}
	public KtnRedressDTO getRedress() {
		return redress;
	}
	public void setRedress(KtnRedressDTO redress) {
		this.redress = redress;
	}
	public boolean isPaxUnlocked() {
		return paxUnlocked;
	}
	public void setPaxUnlocked(boolean paxUnlocked) {
		this.paxUnlocked = paxUnlocked;
	}
	
}
