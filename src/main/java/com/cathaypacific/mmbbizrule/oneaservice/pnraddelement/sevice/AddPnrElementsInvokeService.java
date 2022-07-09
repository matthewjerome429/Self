package com.cathaypacific.mmbbizrule.oneaservice.pnraddelement.sevice;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;

public interface AddPnrElementsInvokeService {
	
	PNRReply addMutiElements(PNRAddMultiElements request, Session session) throws BusinessBaseException;
	
}
