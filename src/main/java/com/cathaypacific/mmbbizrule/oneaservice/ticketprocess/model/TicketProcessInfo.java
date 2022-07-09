package com.cathaypacific.mmbbizrule.oneaservice.ticketprocess.model;

import java.util.ArrayList;
import java.util.List;

public class TicketProcessInfo {
	
	private String messageFunction;

	private List<TicketProcessDocGroup> docGroups;

	public String getMessageFunction() {
		return messageFunction;
	}

	public void setMessageFunction(String messageFunction) {
		this.messageFunction = messageFunction;
	}

	public List<TicketProcessDocGroup> getDocGroups() {
		return docGroups;
	}

	public List<TicketProcessDocGroup> findDocGroups() {
		if(docGroups == null){
			docGroups = new ArrayList<>();
		}
		return docGroups;
	}
	
	public void setDocGroups(List<TicketProcessDocGroup> docGroups) {
		this.docGroups = docGroups;
	}
	
}
