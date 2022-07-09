package com.cathaypacific.mmbbizrule.oneaservice.updateseat.service;

import java.util.List;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.AswrInfo;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.RemarkInfo;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.UpdateSeatRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.XlwrInfo;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.oneaconsumer.model.header.Session;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc this service is used to update seat for passenger in PNR
 * @author fengfeng.jiang
 * @date Jan 29, 2018 5:19:37 PM
 * @version V1.0
 */
public interface AddSeatService {
	/**
	 * 
	* @Description update seat
	* @param
	* @return RetrievePnrBooking
	* @author fengfeng.jiang
	 * @param aswrInfos 
	 * @param map 
	 */
	public RetrievePnrBooking addSeat(UpdateSeatRequestDTO requestDTO, Session session, List<XlwrInfo> xlwrInfos, List<RemarkInfo> remarkInfos, List<AswrInfo> aswrInfos) throws BusinessBaseException;
}
