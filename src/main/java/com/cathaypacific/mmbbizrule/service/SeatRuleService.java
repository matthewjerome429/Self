package com.cathaypacific.mmbbizrule.service;

import java.util.List;

public interface SeatRuleService {
	
	public List<String> getEligibleRBDForSeatSelection();
	
	public List<String> getLowRBD();
}
