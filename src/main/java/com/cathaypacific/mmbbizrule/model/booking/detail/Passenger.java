package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.util.ArrayList;
import java.util.List;

import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrStaffDetail;

public class Passenger {

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
     * For non member: it is the login passenger<br>
     * For member: it is member self, if cannot find member self in the booking, it is member's companion in profile
     */
	private Boolean primaryPassenger;
	/**
	 * The member self if login as member, else will set null. 
	 */
	private Boolean loginMember;
	/**
	 * The companion passenger;
	 */
	private Boolean companion;
	/**
	 * FQTV/FQTR of the pax matched with login member id
	 */
	private boolean loginFFPMatched;	
	/** The country of residence */
    private String countryOfResidence;
	/** The primary travel documents. */
	private List<TravelDoc> priTravelDocs;
	/** The secondary travel documents. */
	private List<TravelDoc> secTravelDocs;
	/** The contact info. */
	private ContactInfo contactInfo;
	/** The emergency contact info. */
	private EmrContactInfo emrContactInfo;
	/** The destination address. */
	private DesAddress desAddress;
	/** The destination is transit. */
	private boolean desTransit;
	/** The DOB from 1A travellerInfo */
	private Dob dob;	
	/** The concession booking information */
	private RetrievePnrStaffDetail staffDetail;
	/** True if has UMNR ssr*/
	private Boolean unaccompaniedMinor = false;
	/** The KTN*/
	private TS ktn;
	/** The redress number */
	private TS redress;
	/**--------------------Cpr information start----------------------*/
	/** Passenger Unique Customer ID, UCI, from CPR */
	private String cprUniqueCustomerId;
	
	/** The gender from CPR*/
    private String cprGender;
    
    private boolean correspondingCprPassengerFound;
    
    /** staff linked rloc **/
    private String linkedRloc;
    
    /** not all flight in passenger ticket issued **/
    private boolean tickedUnissued;
    
    /** mice group c **/
    private boolean grmc;
	
	public String getPassengerId() {
		return passengerId;
	}
	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
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
	public String getCountryOfResidence() {
		return countryOfResidence;
	}
	public void setCountryOfResidence(String countryOfResidence) {
		this.countryOfResidence = countryOfResidence;
	}
	public List<TravelDoc> getPriTravelDocs() {
		if(priTravelDocs == null) {
			priTravelDocs = new ArrayList<>();
		}
		return priTravelDocs;
	}
	public void setPriTravelDocs(List<TravelDoc> priTravelDocs) {
		this.priTravelDocs = priTravelDocs;
	}
	public List<TravelDoc> getSecTravelDocs() {
		if(secTravelDocs == null) {
			secTravelDocs = new ArrayList<>();
		}
		return secTravelDocs;
	}
	public void setSecTravelDocs(List<TravelDoc> secTravelDocs) {
		this.secTravelDocs = secTravelDocs;
	}
	public Boolean isPrimaryPassenger() {
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
	public EmrContactInfo getEmrContactInfo() {
		return emrContactInfo;
	}
	public EmrContactInfo findEmrContactInfo() {
		if(emrContactInfo == null){
			emrContactInfo = new EmrContactInfo();
		}
		return emrContactInfo;
	}
	
	public void setEmrContactInfo(EmrContactInfo emrContactInfo) {
		this.emrContactInfo = emrContactInfo;
	}
	public DesAddress getDesAddress() {
		return desAddress;
	}
	public DesAddress findDesAddress() {
		if(desAddress == null){
			desAddress = new DesAddress();
		}
		return desAddress;
	}
	public void setDesAddress(DesAddress desAddress) {
		this.desAddress = desAddress;
	}
	public boolean isDesTransit() {
		return desTransit;
	}
	public void setDesTransit(boolean desTransit) {
		this.desTransit = desTransit;
	}
	public ContactInfo getContactInfo() {
		return contactInfo;
	}
	public ContactInfo findContactInfo() {
		if(contactInfo == null) {
			contactInfo = new ContactInfo();
		}
		return contactInfo;
	}
	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	public Dob getDob() {
		return dob;
	}
	public Dob findDob() {
		if(dob == null){
			dob = new Dob();
		}
		return dob;
	}
	public void setDob(Dob dob) {
		this.dob = dob;
	}
	public Boolean isCompanion() {
		return companion;
	}
	public void setCompanion(Boolean companion) {
		this.companion = companion;
	}
	public boolean isLoginFFPMatched() {
		return loginFFPMatched;
	}
	public void setLoginFFPMatched(boolean loginFFPMatched) {
		this.loginFFPMatched = loginFFPMatched;
	}
	public RetrievePnrStaffDetail getStaffDetail() {
		return staffDetail;
	}
	public void setStaffDetail(RetrievePnrStaffDetail staffDetail) {
		this.staffDetail = staffDetail;
	}
	public boolean isUnaccompaniedMinor() {
		return unaccompaniedMinor;
	}
	public void setUnaccompaniedMinor(boolean unaccompaniedMinor) {
		this.unaccompaniedMinor = unaccompaniedMinor;
	}
	public TS getKtn() {
		return ktn;
	}
	public TS findKtn() {
		if(ktn == null) {
			ktn = new TS();
		}
		return ktn;
	}
	public void setKtn(TS ktn) {
		this.ktn = ktn;
	}
	public TS getRedress() {
		return redress;
	}
	public TS findRedress() {
		if(redress == null) {
			redress = new TS();
		}
		return redress;
	}
	public void setRedress(TS redress) {
		this.redress = redress;
	}
	public String getCprUniqueCustomerId() {
		return cprUniqueCustomerId;
	}
	public void setCprUniqueCustomerId(String cprUniqueCustomerId) {
		this.cprUniqueCustomerId = cprUniqueCustomerId;
	}
	public String getCprGender() {
		return cprGender;
	}
	public void setCprGender(String cprGender) {
		this.cprGender = cprGender;
	}
    public boolean isCorrespondingCprPassengerFound() {
		return correspondingCprPassengerFound;
	}
	public void setCorrespondingCprPassengerFound(boolean correspondingCprPassengerFound) {
		this.correspondingCprPassengerFound = correspondingCprPassengerFound;
	}
	public String getLinkedRloc() {
        return linkedRloc;
    }
    public void setLinkedRloc(String linkedRloc) {
        this.linkedRloc = linkedRloc;
    }
    public boolean isTickedUnissued() {
        return tickedUnissued;
    }
    public void setTickedUnissued(boolean tickedUnissued) {
        this.tickedUnissued = tickedUnissued;
    }
    public boolean isGrmc() {
        return grmc;
    }
    public void setGrmc(boolean grmc) {
        this.grmc = grmc;
    }
    
	
}
