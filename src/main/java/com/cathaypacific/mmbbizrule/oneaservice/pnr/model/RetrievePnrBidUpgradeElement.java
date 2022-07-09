package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

public class RetrievePnrBidUpgradeElement extends DataElement {
	
	private String fromSubClass;//original subclass
	
	private String toSubClass;//[to be]upgraded sub class

	public String getFromSubClass() {
		return fromSubClass;
	}

	public void setFromSubClass(String fromSubClass) {
		this.fromSubClass = fromSubClass;
	}

	public String getToSubClass() {
		return toSubClass;
	}

	public void setToSubClass(String toSubClass) {
		this.toSubClass = toSubClass;
	}
	
}
