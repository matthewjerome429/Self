package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

public class RetrievePnrPassenger {

	/** The passenger id. */
	private String passengerID;
	/**
	 * If The passenger is infant without seat, its value will be his parentId.
	 */
	private String parentId;
	/** The passenger type. */
	private String passengerType;
	/** The family name. */
	private String familyName;
	/** The given name. */
	private String givenName;

	/**
	 * the primary passenger in the booking, For nun member: it is the login
	 * passenger<br>
	 * For member: it is member self, if cannot find member self in the booking,
	 * it is member's companion in profile
	 */
	private Boolean primaryPassenger;
	
	/**
	 * The companion passengers
	 */
	private Boolean companion;
	/**
	 * The member self if login as member, else will set null.
	 */
	private Boolean loginMember;
	/**
	 * FQTV/FQTR of the pax matched with login member id
	 */
	private boolean loginFFPMatched;
	/** The FQTV Info */
	private List<RetrievePnrFFPInfo> fqtvInfos;
	/** The FQTR Info */
	private List<RetrievePnrFFPInfo> fqtrInfos;
	/** The primary travel documents. */
	private List<RetrievePnrTravelDoc> priTravelDocs;
	/** The secondary travel documents. */
	private List<RetrievePnrTravelDoc> secTravelDocs;
	/** The other travel documents.*/
	private List<RetrievePnrTravelDoc> othTravelDocs;
	/** The list of KTN.*/
	private List<RetrievePnrTravelDoc> ktns;
	/** The list of redress.*/
	private List<RetrievePnrTravelDoc> redresses;
	/** The passenger country of residence info. */
	private List<RetrievePnrCountryOfResidence> countryOfResidence;
	/** The passenger mobile info. */
	private List<RetrievePnrContactPhone> contactPhones;
	/** The passenger email. */
	private List<RetrievePnrEmail> emails;
	/** The emergency contact info. */
	private List<RetrievePnrEmrContactInfo> emrContactInfos;
	/** The destination address. */
	private List<RetrievePnrAddressDetails> desAddresses;
	/** The residence address. */
	private List<RetrievePnrAddressDetails> resAddresses;
	/** The DOB from 1A travellerInfo */
	private RetrievePnrDob dob;
	
	/** The concession booking information */
	private RetrievePnrStaffDetail staffDetail;
	
	/** segmentName = FP's longFreetext */
	private List<String> fpLongFreetexts;
	
	public RetrievePnrStaffDetail getStaffDetail() {
		return staffDetail;
	}

	public void setStaffDetail(RetrievePnrStaffDetail staffDetail) {
		this.staffDetail = staffDetail;
	}

	public String getPassengerID() {
		return passengerID;
	}

	public void setPassengerID(String passengerID) {
		this.passengerID = passengerID;
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
	
	public List<RetrievePnrFFPInfo> getFQTRInfos() {
		if (fqtrInfos == null) {
			fqtrInfos = new ArrayList<>();
		}
		return fqtrInfos;
	}
	
	public void setFQTRInfos(List<RetrievePnrFFPInfo> fQTRInfos) {
		fqtrInfos = fQTRInfos;
	}
	

	public List<RetrievePnrFFPInfo> getFQTVInfos() {
		if (fqtvInfos == null) {
			fqtvInfos = new ArrayList<>();
		}
		return fqtvInfos;
	}

	public void setFQTVInfos(List<RetrievePnrFFPInfo> fQTVInfos) {
		fqtvInfos = fQTVInfos;
	}

	public List<RetrievePnrTravelDoc> getPriTravelDocs() {
		if (this.priTravelDocs == null) {
			this.priTravelDocs = new ArrayList<>();
		}
		return priTravelDocs;
	}

	public void setPriTravelDocs(List<RetrievePnrTravelDoc> priTravelDocs) {
		this.priTravelDocs = priTravelDocs;
	}

	public List<RetrievePnrTravelDoc> getSecTravelDocs() {
		if (this.secTravelDocs == null) {
			this.secTravelDocs = new ArrayList<>();
		}
		return secTravelDocs;
	}

	public void setSecTravelDocs(List<RetrievePnrTravelDoc> secTravelDocs) {
		this.secTravelDocs = secTravelDocs;
	}
	
	public List<RetrievePnrTravelDoc> getKtns() {
		if(ktns == null) {
			ktns = new ArrayList<>();
		}
		return ktns;
	}

	public void setKtns(List<RetrievePnrTravelDoc> ktns) {
		this.ktns = ktns;
	}

	public List<RetrievePnrTravelDoc> getRedresses() {
		if(redresses == null) {
			redresses = new ArrayList<>();
		}
		return redresses;
	}

	public void setRedresses(List<RetrievePnrTravelDoc> redresses) {
		this.redresses = redresses;
	}

	public List<RetrievePnrEmrContactInfo> getEmrContactInfos() {
		if (emrContactInfos == null) {
			emrContactInfos = new ArrayList<>();
		}
		return emrContactInfos;
	}

	public void setEmrContactInfos(List<RetrievePnrEmrContactInfo> emrContactInfos) {
		this.emrContactInfos = emrContactInfos;
	}

	public List<RetrievePnrAddressDetails> getDesAddresses() {
		if (desAddresses == null) {
			desAddresses = new ArrayList<>();
		}
		return desAddresses;
	}

	public void setDesAddresses(List<RetrievePnrAddressDetails> desAddresses) {
		this.desAddresses = desAddresses;
	}

	public Boolean isPrimaryPassenger() {
		return primaryPassenger;
	}

	public void setPrimaryPassenger(Boolean primaryPassenger) {
		this.primaryPassenger = primaryPassenger;
	}

	public List<RetrievePnrContactPhone> getContactPhones() {
		if(contactPhones == null){
			contactPhones = new ArrayList<>();
		}
		return contactPhones;
	}

	public void setContactPhones(List<RetrievePnrContactPhone> contactPhones) {
		this.contactPhones = contactPhones;
	}

	public List<RetrievePnrEmail> getEmails() {
		if(emails == null){
			emails = new ArrayList<>();
		}
		return emails;
	}

	public void setEmails(List<RetrievePnrEmail> emails) {
		this.emails = emails;
	}

	public List<RetrievePnrCountryOfResidence> getCountryOfResidence() {
		if (countryOfResidence == null) {
			countryOfResidence = new ArrayList<>();
		}
		return countryOfResidence;
	}

	public void setCountryOfResidence(List<RetrievePnrCountryOfResidence> countryOfResidence) {
		this.countryOfResidence = countryOfResidence;
	}

	public Boolean isLoginMember() {
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

	public List<RetrievePnrAddressDetails> getResAddresses() {
		if(resAddresses == null){
			resAddresses = new ArrayList<>();
		}
		return resAddresses;
	}

	public void setResAddresses(List<RetrievePnrAddressDetails> resAddresses) {
		this.resAddresses = resAddresses;
	}

	public List<RetrievePnrTravelDoc> getOthTravelDocs() {
		if(othTravelDocs == null){
			othTravelDocs = new ArrayList<>();
		}
		return othTravelDocs;
	}

	public void setOthTravelDocs(List<RetrievePnrTravelDoc> othTravelDocs) {
		this.othTravelDocs = othTravelDocs;
	}

	public RetrievePnrDob getDob() {
		return dob;
	}
	
	public RetrievePnrDob findDob() {
		if(dob == null){
			dob = new RetrievePnrDob();
		}
		return dob;
	}

	public void setDob(RetrievePnrDob dob) {
		this.dob = dob;
	}

	public Boolean isCompanion() {
		return companion;
	}

	public void setCompanion(Boolean companion) {
		this.companion = companion;
	}

	public List<String> getFpLongFreetexts() {
		if (fpLongFreetexts == null) {
			fpLongFreetexts = Lists.newArrayList();
		}
		return fpLongFreetexts;
	}

	public void setFpLongFreetexts(List<String> fpLongFreetexts) {
		this.fpLongFreetexts = fpLongFreetexts;
	}
}
