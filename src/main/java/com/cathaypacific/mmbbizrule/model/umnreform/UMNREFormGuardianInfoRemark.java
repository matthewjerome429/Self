package com.cathaypacific.mmbbizrule.model.umnreform;

public class UMNREFormGuardianInfoRemark {
	
	private String name;
	
	private String relationship;
	
	private String phoneNumber;
	
	private UMNREFormAddressRemark address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public UMNREFormAddressRemark getAddress() {
		if (address == null) {
			address = new UMNREFormAddressRemark();
		}
		return address;
	}

	public void setAddress(UMNREFormAddressRemark address) {
		this.address = address;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	
	
	
}
