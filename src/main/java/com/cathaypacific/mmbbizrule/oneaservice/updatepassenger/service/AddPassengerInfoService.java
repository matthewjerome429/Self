package com.cathaypacific.mmbbizrule.oneaservice.updatepassenger.service;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc this service is used to add passenger info of PNR
 * @author fengfeng.jiang
 * @date Jan 8, 2018 5:19:37 PM
 * @version V1.0
 */
public interface AddPassengerInfoService {
	/**
	 * 
	* @Description add passenger info
	* @param
	* @return RetrievePnrBooking
	* @author fengfeng.jiang
	 * @param map 
	 */
	public RetrievePnrBooking addPassengerInfo(PNRAddMultiElements request, Session session) throws BusinessBaseException;
	
}
