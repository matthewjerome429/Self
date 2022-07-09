package com.cathaypacific.mmbbizrule.service;

import java.util.Map;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;

public interface PaxNameIdentificationService {
    
    /**
     * Identify pax name for mice booking
     * 
     * @param loginInfo
     * @param booking
     * @throws BusinessBaseException
     */
    public void primaryPaxIdentificationForMice(LoginInfo loginInfo, RetrievePnrBooking booking)
            throws BusinessBaseException;
	/**
	 * Identify pax name for rloc login
	 * 
	 * @param familyName
	 * @param givenName
	 * @param booking
	 * @throws BusinessBaseException
	 * @author haiwei.jia
	 */
	public void primaryPaxIdentificationForRloc(String familyName, String givenName, RetrievePnrBooking booking) throws BusinessBaseException;
	/**
	 * Identify pax name for eticket login
	 * 
	 * @param familyName
	 * @param givenName
	 * @param eTicket
	 * @param booking
	 * @throws BusinessBaseException
	 * @author haiwei.jia
	 */
	public void primaryPaxIdentificationForETicket(String familyName, String givenName, String eTicket, RetrievePnrBooking booking) throws BusinessBaseException;
	
	/**
	 * Identify pax name for member login
	 * 
	 * @param loginMemberId
	 * @param booking
	 * @throws BusinessBaseException
	 * @author haiwei.jia
	 */
	public void primaryPaxIdentificationForMember(LoginInfo loginInfo, RetrievePnrBooking booking) throws BusinessBaseException;
	
	/**
	 * 
	 * 
	 * @param loginMemberId
	 * @param booking
	 * @throws BusinessBaseException
	 * @author jiajian.guo
	 */
	public void primaryPassengerIdentificationByInFo(LoginInfo loginInfo, RetrievePnrBooking pnrBooking)
			throws BusinessBaseException;
	
	/**
	 * 
	 * @param familyName
	 * @param givenName
	 * @param paxMap
	 * @param nameTitleList
	 * @param shortCompareSize
	 * @throws BusinessBaseException
	 */
	public boolean isPassengerNameMatched(String familyName, String givenName, Map<String, String[]> paxMap, Integer shortCompareSize);

}
