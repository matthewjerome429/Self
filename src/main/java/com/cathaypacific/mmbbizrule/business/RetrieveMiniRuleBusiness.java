package com.cathaypacific.mmbbizrule.business;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.oneaconsumer.model.response.tmrxrq_17_2_1a.MiniRuleGetFromRecReply;

public interface RetrieveMiniRuleBusiness {
	
	public MiniRuleGetFromRecReply retrieveMiniRuleFromRec(String rloc) throws BusinessBaseException;
	
}