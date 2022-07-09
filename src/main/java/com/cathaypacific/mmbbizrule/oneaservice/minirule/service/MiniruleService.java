package com.cathaypacific.mmbbizrule.oneaservice.minirule.service;


import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.response.tmrxrq_17_2_1a.MiniRuleGetFromRecReply;

public interface MiniruleService {
	public MiniRuleGetFromRecReply retrieveMiniRuleByRec(String rloc, Session session) throws BusinessBaseException;
	
}