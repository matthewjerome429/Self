package com.cathaypacific.mmbbizrule.oneaservice.pnr.service;


import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;

public interface PnrInvokeService {
	
	/**
	 * 
	* @Description get Booking by 1A rloc or gds rloc
	* @param
	* @return Booking
	* @author fengfeng.jiang
	 * @throws ExpectedException 
	*/
	public RetrievePnrBooking retrievePnrByRloc(String rloc) throws BusinessBaseException;
	
	public RetrievePnrBooking PNRReplyToBooking(PNRReply pnr, String rloc) throws BusinessBaseException;

	public PNRReply retrievePnrReplyByOneARloc(String rloc) throws BusinessBaseException;
	
	public PNRReply retrievePnrReplyByOneARloc(String rloc, Session session) throws BusinessBaseException;
}
