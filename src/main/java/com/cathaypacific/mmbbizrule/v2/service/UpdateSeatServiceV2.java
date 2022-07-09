package com.cathaypacific.mmbbizrule.v2.service;

import java.util.List;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.RemarkInfo;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.Journey;
import com.cathaypacific.mmbbizrule.v2.dto.request.updateseat.UpdateSeatRequestDTOV2;
import com.cathaypacific.olciconsumer.model.request.allocateSeat.AllocateSeatRequestDTO;
import com.cathaypacific.olciconsumer.model.response.allocateSeat.AllocateSeatResponseDTO;
import com.cathaypacific.olciconsumer.model.response.changeseat.ChangeSeatResponseDTO;

public interface UpdateSeatServiceV2 {
	
	/**
	 * Allocate seat after user temporarily save seat
	 * If success, return response body
	 * If fail, return null
	 * @param requestDTO
	 * @param rloc
	 * @return
	 */
	public AllocateSeatResponseDTO allocateSeat(AllocateSeatRequestDTO requestDTO, String rloc);
	
	/**
	 * save seat to token data
	 * @param bookingBefore 
	 * @return
	 * @throws BusinessBaseException 
	 */
	public void saveTempSeat(UpdateSeatRequestDTOV2 requestDTO, Booking bookingBefore) throws BusinessBaseException;
	
	/**
	 * change seat for checked in passenger
	 * @param requestDTO
	 * @param cprJourneys
	 * @return ChangeSeatResponseDTO
	 * @throws BusinessBaseException
	 */
	public ChangeSeatResponseDTO changeSeat(UpdateSeatRequestDTOV2 requestDTO, List<Journey> cprJourneys) throws BusinessBaseException;
	
	/**
	 * add remark for the voluntary seat change from EXL to non EXL
	 * @param remarkInfos
	 * @param rloc
	 * @throws BusinessBaseException
	 */
	public void addRemark(List<RemarkInfo> remarkInfos, String rloc) throws BusinessBaseException;
}
