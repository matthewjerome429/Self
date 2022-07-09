package com.cathaypacific.mmbbizrule.oneaservice.pnr.model;

import java.util.ArrayList;
import java.util.List;

public class DataElement {

	private List<String> referenceStList;

	private List<String> referencePtList;

	private List<String> referenceOtList;

	public List<String> getReferenceStList() {
		return referenceStList;
	}

	public void setReferenceStList(List<String> referenceStList) {
		this.referenceStList = referenceStList;
	}

	public void addReferenceSt(String st) {
		if (referenceStList == null) {
			referenceStList = new ArrayList<>();
		}
		referenceStList.add(st);
	}

	public List<String> getReferencePtList() {
		return referencePtList;
	}

	public void setReferencePtList(List<String> referencePtList) {
		this.referencePtList = referencePtList;
	}

	public void addReferencePt(String pt) {
		if (referencePtList == null) {
			referencePtList = new ArrayList<>();
		}
		referencePtList.add(pt);
	}

	public List<String> getReferenceOtList() {
		return referenceOtList;
	}

	public void setReferenceOtList(List<String> referenceOtList) {
		this.referenceOtList = referenceOtList;
	}

	public void addReferenceOt(String ot) {
		if (referenceOtList == null) {
			referenceOtList = new ArrayList<>();
		}
		referenceOtList.add(ot);
	}
}
