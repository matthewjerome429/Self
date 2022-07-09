package com.cathaypacific.mmbbizrule.v2.dto.common.booking;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PassengerDTOV2 implements Serializable{

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
  @ApiModelProperty(value = "if true the passenger is primary in the booking.", required = false)
	private Boolean primaryPassenger;
	/**
	 * The Companion passenger
	 */
  @ApiModelProperty(value = "if true the passenger is member's companion in profile.", required = false)
	private Boolean companion;
	/**
	 * The member self if login as member, else will set null. 
	 */
  @ApiModelProperty(value = "if true the passenger is login as member.", required = false)
	private Boolean loginMember;
	/**
	 * FQTV/FQTR of login member matched with login member id
	 */
  @ApiModelProperty(value = "if true the passenger is login as member and can matched FQTV/FQTR with member id.", required = true)
	private boolean loginFFPMatched;
	/** The passenger contact info. */
	private ContactInfoDTOV2 contactInfo;
	/** The passenger member profile contact info. */
	private ContactInfoDTOV2 mpContactInfo;
	/** The emergency contact info. */
	private EmrContactInfoDTOV2 emrContactInfo;
	/** The destination address info. */
	private DestinationAddressDTOV2 destinationAddress;
	/** The destination is transit without address */
	private boolean destinationTransit;

	/** user type */
	@ApiModelProperty(value = "user type of passenger.", required = true)
	private String userType;
	
	private KtnRedressDTOV2 ktn;
	
	private KtnRedressDTOV2 redress;
	/** The DOB from 1A travellerInfo */
	private DobDTOV2 dob;
	
	/** True if has UMNR ssr*/
	@ApiModelProperty(value = "if true the passenger has UMNR SSR.", required = true)
	private boolean unaccompaniedMinor;	/** a sign which used for to tell the passenger is masked or not**/

	@ApiModelProperty(value = "if false the passenger is locked.", required = true)
	private boolean paxUnlocked = true;
	
	/** Passenger Display */
	private PassengerDisplayDTOV2 display;
	
	/** Passenger Check In Mandatory */
	private PassengerCheckInMandatoryDTOV2 mandatory;
	
	/** linked rloc **/
	private String linkedRloc;
	
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
	public ContactInfoDTOV2 getContactInfo() {
		return contactInfo;
	}
	public ContactInfoDTOV2 findContactInfo() {
		if(contactInfo == null) {
			contactInfo = new ContactInfoDTOV2();
		}
		return contactInfo;
	}
	
	public void setContactInfo(ContactInfoDTOV2 contactInfo) {
		this.contactInfo = contactInfo;
	}
	public EmrContactInfoDTOV2 getEmrContactInfo() {
		return emrContactInfo;
	}
	public EmrContactInfoDTOV2 findEmrContactInfo(){
		if(emrContactInfo == null) {
			emrContactInfo = new EmrContactInfoDTOV2();
		}
		return emrContactInfo;
		
	}
	public void setEmrContactInfo(EmrContactInfoDTOV2 emrContactInfo) {
		this.emrContactInfo = emrContactInfo;
	}
	public DestinationAddressDTOV2 getDestinationAddress() {
		return destinationAddress;
	}
	public DestinationAddressDTOV2 findDestinationAddress() {
		if(destinationAddress == null) {
			destinationAddress = new DestinationAddressDTOV2();
		}
		return destinationAddress;
	}
	public void setDestinationAddress(DestinationAddressDTOV2 destinaitionAddress) {
		this.destinationAddress = destinaitionAddress;
	}
	public boolean isDestinationTransit() {
		return destinationTransit;
	}
	public void setDestinationTransit(boolean destinationTransit) {
		this.destinationTransit = destinationTransit;
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
	public ContactInfoDTOV2 getMpContactInfo() {
		return mpContactInfo;
	}
	public void setMpContactInfo(ContactInfoDTOV2 mpContactInfo) {
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
	public PassengerDisplayDTOV2 getDisplay() {
		return display;
	}
	public PassengerDisplayDTOV2 findDisplay() {
		if (display == null) {
			display = new PassengerDisplayDTOV2();
		}
		return display;
	}
	public void setDisplay(PassengerDisplayDTOV2 display) {
		this.display = display;
	}
	public DobDTOV2 getDob() {
		return dob;
	}
	public void setDob(DobDTOV2 dob) {
		this.dob = dob;
	} 
	public KtnRedressDTOV2 getKtn() {
		return ktn;
	}
	public void setKtn(KtnRedressDTOV2 ktn) {
		this.ktn = ktn;
	}
	public KtnRedressDTOV2 getRedress() {
		return redress;
	}
	public void setRedress(KtnRedressDTOV2 redress) {
		this.redress = redress;
	}
	public boolean isPaxUnlocked() {
		return paxUnlocked;
	}
	public void setPaxUnlocked(boolean paxUnlocked) {
		this.paxUnlocked = paxUnlocked;
	}
	public PassengerCheckInMandatoryDTOV2 getMandatory() {
		return mandatory;
	}
	public void setMandatory(PassengerCheckInMandatoryDTOV2 mandatory) {
		this.mandatory = mandatory;
	}
	public PassengerCheckInMandatoryDTOV2 findMandatory() {
		if (mandatory == null) {
			mandatory = new PassengerCheckInMandatoryDTOV2();
		}
		return mandatory;
	}
    public String getLinkedRloc() {
        return linkedRloc;
    }
    public void setLinkedRloc(String linkedRloc) {
        this.linkedRloc = linkedRloc;
    }
	
}
