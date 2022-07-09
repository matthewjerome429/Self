package com.cathaypacific.mmbbizrule.cxservice.messagehub.model.domain;

import java.util.List;

public class MetaSBR {

	private String rLoc;
	
	private String PNRCreatedAt;
	
	private String officeId;
	
	private String agentCode;
	
	private List<ActionDTO> actions;

	public String getrLoc() {
		return rLoc;
	}

	public void setrLoc(String rLoc) {
		this.rLoc = rLoc;
	}

	public String getPNRCreatedAt() {
		return PNRCreatedAt;
	}

	public void setPNRCreatedAt(String pNRCreatedAt) {
		PNRCreatedAt = pNRCreatedAt;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public List<ActionDTO> getActions() {
		return actions;
	}

	public void setActions(List<ActionDTO> actions) {
		this.actions = actions;
	}
	
}
