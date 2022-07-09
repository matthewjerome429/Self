package com.cathaypacific.mmbbizrule.oneaservice.changepnrelement.service;

import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.pnrchg_16_1_1a.PNRChangeElement;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;

public interface ChangePnrElementInvokeService {
	
	public PNRReply changePnrElement(PNRChangeElement request, Session session, String rloc) throws Exception;
	
}
