package com.cathaypacific.mmbbizrule.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.dto.request.oneaoperation.AddRmDetail;
import com.cathaypacific.mmbbizrule.dto.request.umnrform.UmnrFormUpdateRequestDTO;
import com.cathaypacific.mmbbizrule.model.umnreform.UMNREFormRemark;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrRemark;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.util.FreeTextUtil;
import com.cathaypacific.mmbbizrule.oneaservice.pnraddelement.AddRemarkBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.pnraddelement.sevice.AddPnrElementsInvokeService;
import com.cathaypacific.mmbbizrule.oneaservice.pnrcancel.service.DeletePnrService;
import com.cathaypacific.mmbbizrule.service.UMNREFormRemarkService;
import com.cathaypacific.mmbbizrule.service.UMNREFormUpdateService;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.header.SessionStatus;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;

@Service
public class UMNREFormUpdateServiceImpl implements UMNREFormUpdateService {
	
	private static LogAgent logger = LogAgent.getLogAgent(UMNREFormUpdateServiceImpl.class);

	@Autowired
	private PnrInvokeService pnrInvokeService;
	
	@Autowired
	private AddPnrElementsInvokeService addPnrElementsInvokeService;
	
	@Autowired
	private UMNREFormRemarkService umnreFormRemarkServiceImpl;
	
	@Autowired
	private UMNREFormRemarkService umnrEFormRemarkService;
	
	@Autowired
	private DeletePnrService deletePnrService;
	
	@Override
	public boolean updateUMNREForm(UmnrFormUpdateRequestDTO requestDTO) throws BusinessBaseException {
		
		/** Retrieve booking */
		RetrievePnrBooking pnrBooking = pnrInvokeService.retrievePnrByRloc(requestDTO.getRloc());
		
		/** Do 1A delete operations */
		Session session = doDelete(pnrBooking, requestDTO);
		
		/** Do 1A add operations */
		doAdd(pnrBooking, requestDTO, session);
		
		return true;
	}
	
	/**
	 * Do 1A delete operations
	 * 1. delete operations skip, return empty session;
	 * 2. return the session with end status after doing delete.
	 * 
	 * @param pnrBooking
	 * @param requestDTO
	 * @return
	 * @throws BusinessBaseException
	 */
	private Session doDelete(RetrievePnrBooking pnrBooking, UmnrFormUpdateRequestDTO requestDTO) throws BusinessBaseException {
		List<String> otQulifierIds = getDeleteOtQualifierIds(pnrBooking, requestDTO.getPassengerId(), requestDTO.isParentalLock());
		if(CollectionUtils.isEmpty(otQulifierIds)) {
			logger.info(String.format("updateUMNREForm.doDelete -> skip, no OT qulifiers found in booking[%s] for passenger[%s].",
					pnrBooking.getOneARloc(), requestDTO.getPassengerId()));
			return null;
		}
		
		Map<String, List<String>> deleteMap = new HashMap<>();
		deleteMap.put(OneAConstants.OT_QUALIFIER, otQulifierIds);
		
		Session session = new Session();
		deletePnrService.deletePnr(pnrBooking.getOneARloc(), deleteMap, session);
		session.setStatus(SessionStatus.END.getStatus());//set end transaction
		return session;
	}
	
	/**
	 * Do 1A adding operations
	 * 1. add back current UMNR
	 * 2. add parental lock if requested
	 * @param pnrBooking 
	 * 
	 * @param requestDTO
	 * @param session
	 */
	private void doAdd(RetrievePnrBooking pnrBooking, UmnrFormUpdateRequestDTO requestDTO, Session session) throws BusinessBaseException {
		PNRAddMultiElements request = buildAddRequest(pnrBooking, requestDTO, session);
		addPnrElementsInvokeService.addMutiElements(request, session);
	}

	/**
	 * Build 1A adding request
	 * 
	 * @param pnrBooking 
	 * @param requestDTO
	 * @param session 
	 * @return
	 */
	private PNRAddMultiElements buildAddRequest(RetrievePnrBooking pnrBooking, UmnrFormUpdateRequestDTO requestDTO, Session session) {
		AddRemarkBuilder builder = new AddRemarkBuilder();
		
		List<AddRmDetail> addRmDetails = new ArrayList<>();
		
		/** Build UMNR RM details */
		String[] umnrRMFreeTexts = FreeTextUtil.splitFreeTextBySize(umnreFormRemarkServiceImpl.buildUMNREFormRMFreeText(requestDTO), 100);
		for(String freeText : umnrRMFreeTexts) {
			if(StringUtils.isEmpty(freeText)) {
				continue;
			}
			AddRmDetail umnrRMDetail = new AddRmDetail();
			umnrRMDetail.setFreeText(freeText);
			addRmDetails.add(umnrRMDetail);
		}
		
		/** Build parental lock RM details*/
		if(BooleanUtils.isTrue(requestDTO.isParentalLock()) && !BookingBuildUtil.parentalLocked(pnrBooking, requestDTO.getPassengerId())) {
			AddRmDetail parentalLockRMDetail = new AddRmDetail();
			parentalLockRMDetail.setFreeText(OneAConstants.UMNR_EFORM_PARENTAL_LOCK_RM_FREETEXT);
			parentalLockRMDetail.setPassegnerIds(Arrays.asList(requestDTO.getPassengerId()));
			addRmDetails.add(parentalLockRMDetail);
		}
		
		return builder.buildRmRequest(pnrBooking.getOneARloc(), addRmDetails, session);
	}
	
	/**
	 * Get all OT qualifier IDs which should be removed from 1A
	 * 1. Parental lock related RM OT IDs if requestParental lock = false
	 * 2. Current UMNR OT qualifier IDs
	 * 
	 * @param pnrBooking
	 * @param passengerId
	 * @param requestParentalLock
	 * @return
	 */
	private List<String> getDeleteOtQualifierIds(RetrievePnrBooking pnrBooking, String passengerId, boolean requestParentalLock) {
		List<String> otQulifierIds = new ArrayList<>();
		if(!requestParentalLock) {
			otQulifierIds.addAll(getParentalLockedOtQualifierIds(pnrBooking, passengerId));			
		}
		otQulifierIds.addAll(getCurrentUmnrOtQualifierIds(pnrBooking, passengerId));
		return otQulifierIds;
	}

	/**
	 * Get all parental locked RM OT qualifier IDs by passenger IDs.
	 * 
	 * @param pnrBooking
	 * @param passengerId
	 * @return
	 */
	private List<String> getParentalLockedOtQualifierIds(RetrievePnrBooking pnrBooking, String passengerId) {
		List<RetrievePnrRemark> parentalLockedRemarks = BookingBuildUtil.getParentalLockedRemark(pnrBooking, passengerId);
		if(CollectionUtils.isNotEmpty(parentalLockedRemarks)) {
			return parentalLockedRemarks.stream().filter(rm -> rm != null && StringUtils.isNotEmpty(rm.getOtQualifierNumber()))
					.map(RetrievePnrRemark::getOtQualifierNumber).distinct().collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	/**
	 * Get current UMNR OT qualifiers
	 * 
	 * @param pnrBooking
	 * @param passengerId
	 * @return
	 */
	private List<String> getCurrentUmnrOtQualifierIds(RetrievePnrBooking pnrBooking, String passengerId) {
		// Retrieve remarks as a list of models
		List<UMNREFormRemark> umnrEFormRemarks = umnrEFormRemarkService.buildUMNREFormRemark(pnrBooking);
		
		// Get first available EForm remark of the passenger
		UMNREFormRemark umnrEFormRemark = umnrEFormRemarks.stream()
				.filter(remark -> remark != null && CollectionUtils.isNotEmpty(remark.getOtQualifierList()) 
				&& StringUtils.equalsIgnoreCase(remark.getPassengerIdDigit(), passengerId)).findFirst().orElse(null);
		
		return umnrEFormRemark != null ? umnrEFormRemark.getOtQualifierList() : Collections.emptyList();
	}
	
}
