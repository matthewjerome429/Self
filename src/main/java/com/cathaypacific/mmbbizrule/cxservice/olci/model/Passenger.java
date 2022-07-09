package com.cathaypacific.mmbbizrule.cxservice.olci.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Passenger implements Serializable {
	
	/**
	 * serial Version UID
	 */
	private static final long serialVersionUID = -1936444523631439721L;

	/** Passenger Unique Customer ID, UCI */
	private String uniqueCustomerId;
	
	/** Passenger Title */
	private String title;
	
	/** Passenger FamilyName */
    private String familyName;
    
    /** Passenger GivenName */
    private String givenName;
    
    /** CPR Gender*/
    private String gender;
    
    /** CPR paxType  */
    private String type;
    
    /** CPR Infant Age */
    private int age;
    
    /** CPR dateOfBirth: yyyy-mm-dd*/
    private String dateOfBirth;
    
    /** CPR destination address */
    private List<AddressDetails> destinationAddresses;
    
    /** CPR rloc:Get from first passenger*/
    private String rloc;
    
    /**The rloc for web(log) display, 
     * if CPR rloc not equal input rloc and cpr rloc company not 1A, will set to input rloc,
     * otherwise will set to cpr rloc
     *  */
    private String displayRloc;
    
    /** CPR rloc Company*/
    private String rlocCompany;
    
    /** Unique match indicator */
    private String uma;
  
    /** Exact match indicator */
    private String ema;
    
    /** This Indicate The Login Passenger*/
	private boolean loginPax = false;

	/** This Indicate The Login member, only for member login*/
	private boolean loginMember = false;
	
	/** The Sector List For This Passenger In This Journey*/
	private List<Flight> flights;

    /** Primary Travel Documents In customerLevel*/
    private List<TravelDocument> primaryTravelDocuments;
    
    /** Secondary Travel Documents In customerLevel*/
    private List<TravelDocument> secondaryTravelDocuments;
    
    /** KTN Travel Documents In customerLevel*/
    private TravelDocument ktnTravelDocument;
    
    /** Group name the passenger belongs to **/
    private String groupName;
    
    /** Group booking indicator **/
    private boolean groupBooking;
    
    /** Staff details **/
    private StaffDetails staffDetails;
    
    /** Fqtv details **/
    private List<FqtvInfo> customerLevelFqtvInfo;
    
    /** Contact mobile list of this passenger */
    private List<ContactMobile> contactMobiles;
    
    /** Contact Email list of this passenger */
    private List<ContactEmail> contactEmails;
    
    /** MICE BookingDTO details */
    private MICEDetails miceDetails;
    
    /** Emergency Contact */
    private List<EmergencyContact> emergencyContacts;
    
    private List<String> eTicketNumberList;
    
    /** passenger info is unlocked */
    private boolean unlock = false;
    
    /** ssr list, serviceType = S, actionCode= MCF */
    private List<String> ssrList;
    
    /** sk list,serviceType = K, actionCode= MCF */
    private List<String> skList;
    
    /** passenger info in this set is always unmasked */
    private Set<String> unmaskInfos;
    
    /** SSR Mapping Rule*/
	private SSRSKMappingRule ssrSkMappingRule;
	
    /**to indicate whether the login is by RLOC or ET */
    private boolean loginET = false;
    
    /** to indicate whether bp inhibit by high priority comment **/
    private boolean inhibitBPByComment = false;
    
    /** to indicate whether acceptance inhibit by high priority comment **/
    private boolean inhibitAcceptanceByComment = false;

    private boolean displayOnly;
    
    /** reverse checkin carrier **/
	private String reverseCheckinCarrier;
    
	public void addFlight(Flight flight) {
		if(flights == null){
			flights = new ArrayList<>();
		}
		flights.add(flight);
	}

    public void addDestinationAddress(AddressDetails addressDetails) {

		if (addressDetails != null) {
			if (destinationAddresses == null) {
				destinationAddresses = new ArrayList<>();
			}
			destinationAddresses.add(addressDetails);
		}

	}
	
	public void addEmergencyContact(EmergencyContact emergencyContact) {

		if (emergencyContact != null) {
			if (emergencyContacts == null) {
				emergencyContacts = new ArrayList<>();
			}
			emergencyContacts.add(emergencyContact);
		}
	}

	public void addContactMobile(ContactMobile mobile) {
		if (mobile != null) {
			if (contactMobiles == null) {
				contactMobiles = new ArrayList<>();
			}
			contactMobiles.add(mobile);
		}
	}

	public void addContactEmail(ContactEmail email) {
		if (email != null) {
			if (contactEmails == null) {
				contactEmails = new ArrayList<>();
			}
			contactEmails.add(email);
		}
	}
 
	public List<AddressDetails> getDestinationAddresses() {
		if (destinationAddresses == null) {
			destinationAddresses = new ArrayList<>();
		}
		return destinationAddresses;
	}

	public void setDestinationAddresses(List<AddressDetails> destinationAddresses) {
		this.destinationAddresses = destinationAddresses;
	}

	public List<EmergencyContact> getEmergencyContacts() {
		if (emergencyContacts == null) {
			emergencyContacts = new ArrayList<>();
		}
		return emergencyContacts;
	}

	public void setEmergencyContacts(List<EmergencyContact> emergencyContacts) {
		this.emergencyContacts = emergencyContacts;
	}

	public boolean isUnlock() {
		return unlock;
	}

	public void setUnlock(boolean unlock) {
		this.unlock = unlock;
	}

	public Set<String> getUnmaskInfos() {
		if (unmaskInfos == null) {
			unmaskInfos = new HashSet<>();
		}
		return unmaskInfos;
	}

	public void setUnmaskInfos(Set<String> unmaskInfoSet) {
		this.unmaskInfos = unmaskInfoSet;
	}

	public List<String> geteTicketNumberList() {
		return eTicketNumberList;
	}

	public void seteTicketNumberList(List<String> eTicketNumberList) {
		this.eTicketNumberList = eTicketNumberList;
	}

	public String getUniqueCustomerId() {
		return uniqueCustomerId;
	}

	public void setUniqueCustomerId(String uniqueCustomerId) {
		this.uniqueCustomerId = uniqueCustomerId;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean getLoginPax() {
		return loginPax;
	}

	public void setLoginPax(boolean loginPax) {
		this.loginPax = loginPax;
	}

	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getRloc() {
		return rloc;
	}

	public void setRloc(String rloc) {
		this.rloc = rloc;
	}

	public List<TravelDocument> getPrimaryTravelDocuments() {
		if (primaryTravelDocuments == null) {
			primaryTravelDocuments = new ArrayList<>();
		}
		return primaryTravelDocuments;
	}

	public void setPrimaryTravelDocuments(List<TravelDocument> primaryTravelDocuments) {
		this.primaryTravelDocuments = primaryTravelDocuments;
	}

	public List<TravelDocument> getSecondaryTravelDocuments() {
		if (secondaryTravelDocuments == null) {
			secondaryTravelDocuments = new ArrayList<>();
		}
		return secondaryTravelDocuments;
	}

	public void setSecondaryTravelDocuments(List<TravelDocument> secondaryTravelDocuments) {
		this.secondaryTravelDocuments = secondaryTravelDocuments;
	}

	public String getUma() {
		return uma;
	}

	public void setUma(String uma) {
		this.uma = uma;
	}

	public String getEma() {
		return ema;
	}

	public void setEma(String ema) {
		this.ema = ema;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public boolean isGroupBooking() {
		return groupBooking;
	}

	public void setGroupBooking(boolean groupBooking) {
		this.groupBooking = groupBooking;
	}

	public StaffDetails getStaffDetails() {
		return staffDetails;
	}

	public void setStaffDetails(StaffDetails staffDetails) {
		this.staffDetails = staffDetails;
	}

	public List<FqtvInfo> getCustomerLevelFqtvInfo() {
		return customerLevelFqtvInfo;
	}

	public void setCustomerLevelFqtvInfo(List<FqtvInfo> customerLevelFqtvInfo) {
		this.customerLevelFqtvInfo = customerLevelFqtvInfo;
	}

	public List<ContactMobile> getContactMobiles() {
		if (contactMobiles == null) {
			contactMobiles = new ArrayList<>();
		}
		return contactMobiles;
	}
	
	public void setContactMobiles(List<ContactMobile> contactMobiles) {
		this.contactMobiles = contactMobiles;
	}
	
	public List<ContactEmail> getContactEmails() {
		if (contactEmails == null) {
			contactEmails = new ArrayList<>();
		}
		return contactEmails;
	}
	
	public void setContactEmails(List<ContactEmail> contactEmails) {
		this.contactEmails = contactEmails;
	}
	
	public MICEDetails getMiceDetails() {
		return miceDetails;
	}

	public void setMiceDetails(MICEDetails miceDetails) {
		this.miceDetails = miceDetails;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public List<String> getSsrList() {
		return ssrList;
	}

	public void setSsrList(List<String> ssrList) {
		this.ssrList = ssrList;
	}

	public List<String> getSkList() {
		return skList;
	}

	public void setSkList(List<String> skList) {
		this.skList = skList;
	}

	public SSRSKMappingRule getSsrSkMappingRule() {
		return ssrSkMappingRule;
	}

	public void setSsrSkMappingRule(SSRSKMappingRule ssrSkMappingRule) {
		this.ssrSkMappingRule = ssrSkMappingRule;
	}

	public boolean isLoginET() {
		return loginET;
	}

	public void setLoginET(boolean loginET) {
		this.loginET = loginET;
	}

	public boolean isLoginMember() {
		return loginMember;
	}

	public void setLoginMember(boolean loginMember) {
		this.loginMember = loginMember;
	}

	public boolean isInhibitBPByComment() {
		return inhibitBPByComment;
	}

	public void setInhibitBPByComment(boolean inhibitBPByComment) {
		this.inhibitBPByComment = inhibitBPByComment;
	}

	public boolean isInhibitAcceptanceByComment() {
		return inhibitAcceptanceByComment;
	}

	public void setInhibitAcceptanceByComment(boolean inhibitAcceptanceByComment) {
		this.inhibitAcceptanceByComment = inhibitAcceptanceByComment;
	}

	public TravelDocument getKtnTravelDocument() {
		return ktnTravelDocument;
	}

	public void setKtnTravelDocument(TravelDocument ktnTravelDocument) {
		this.ktnTravelDocument = ktnTravelDocument;
	}

	public String getRlocCompany() {
		return rlocCompany;
	}

	public void setRlocCompany(String rlocCompany) {
		this.rlocCompany = rlocCompany;
	}

	public String getDisplayRloc() {
		return displayRloc;
	}

	public void setDisplayRloc(String displayRloc) {
		this.displayRloc = displayRloc;
	}

	public boolean isDisplayOnly() {
		return displayOnly;
	}

	public void setDisplayOnly(boolean displayOnly) {
		this.displayOnly = displayOnly;
	}

	public String getReverseCheckinCarrier() {
		return reverseCheckinCarrier;
	}

	public void setReverseCheckinCarrier(String reverseCheckinCarrier) {
		this.reverseCheckinCarrier = reverseCheckinCarrier;
	}

}
