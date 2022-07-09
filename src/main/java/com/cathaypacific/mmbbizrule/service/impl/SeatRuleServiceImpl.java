package com.cathaypacific.mmbbizrule.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mmbbizrule.constant.SeatRuleConstants;
import com.cathaypacific.mmbbizrule.constant.TBConstants;
import com.cathaypacific.mmbbizrule.db.dao.SeatRuleDao;
import com.cathaypacific.mmbbizrule.db.model.SeatRuleModel;
import com.cathaypacific.mmbbizrule.service.SeatRuleService;

@Service
public class SeatRuleServiceImpl implements SeatRuleService {

	@Autowired
	private SeatRuleDao seatRuleDao;
	
	@Override
	@Transactional(readOnly=true)
	public List<String> getEligibleRBDForSeatSelection() {
		try(Stream<SeatRuleModel> rules = seatRuleDao.findAllByAppCodeAndTypeAndSeatSelect(MMBConstants.APP_CODE, SeatRuleConstants.RBD, TBConstants.ELIGIBILITY_SEAT_SELECT_VALID)){
			return rules.map(r->r.getValue()).collect(Collectors.toList());
		}
	}

	@Override
	public List<String> getLowRBD() {
		List<SeatRuleModel> rules = seatRuleDao.findAllByAppCodeAndTypeAndLowRBD(MMBConstants.APP_CODE, SeatRuleConstants.RBD, TBConstants.IS_LOW_RBD);
			
		return rules.stream().map(SeatRuleModel :: getValue).collect(Collectors.toList());

	}

}
