package com.cathaypacific.mmbbizrule.oneaservice.pnrcancel.service;

import java.util.List;
import java.util.Map;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;


/**
 * 
 * OLSS-MMB
 * 
 * @Desc this service is used to delete passenger info of PNR
 * @author fengfeng.jiang
 * @date Jan 8, 2018 5:19:37 PM
 * @version V1.0
 */
public interface DeletePnrService {
	
	/**
	* @Description delete passenger info
	* @param
	* @return RetrievePnrBooking
	* @author fengfeng.jiang
	*/
	public RetrievePnrBooking deletePnr(String rloc, Map<String, List<String>> map, Session session) throws BusinessBaseException;
	
	/**
	* @Description delete passenger info without response
	* @param rloc
	* @param map
	* @param session
	*/
	public void deletePnrWithoutParser(String rloc, Map<String, List<String>> map, Session session) throws BusinessBaseException;
	
	/**
	 * @Description async delete passenger info without response
	 * @param rloc
	 * @param map
	 * @param session
	 */
	public void asyncDeletePnrWithoutParser(String rloc, Map<String, List<String>> map, Session session);
	
	/**
	 * cancel Booking
	 * 
	 * @param rloc
	 * @param session
	 * @return
	 */
	public PNRReply cancelBooking(String rloc, Session session) throws BusinessBaseException;

}
