package com.cathaypacific.mmbbizrule.oneaservice.meal.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.constants.OneAErrorConstants;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.constant.MealConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.db.dao.CabinClassDAO;
import com.cathaypacific.mmbbizrule.db.dao.SpecialMealDAO;
import com.cathaypacific.mmbbizrule.dto.request.meal.addMeal.AddMealDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.cancelMeal.CancelMealDetailDTO;
import com.cathaypacific.mmbbizrule.oneaservice.handler.OneAErrorHandler;
import com.cathaypacific.mmbbizrule.oneaservice.meal.MealRequestBuilder;
import com.cathaypacific.mmbbizrule.oneaservice.meal.service.MealDTOService;
import com.cathaypacific.mmbbizrule.oneaservice.meal.service.MealService;
import com.cathaypacific.mmbbizrule.oneaservice.model.common.OneAError;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrMeal;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.util.FreeTextUtil;
import com.cathaypacific.mmbbizrule.oneaservice.pnrcancel.service.DeletePnrService;
import com.cathaypacific.mmbbizrule.util.BookingBuildUtil;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.request.pnradd_16_1_1a.PNRAddMultiElements;
import com.cathaypacific.oneaconsumer.model.response.OneaResponse;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.cathaypacific.oneaconsumer.webservice.service.client.OneAWSClient;
import com.google.common.collect.Lists;

@Service
public class MealServiceImpl implements MealService{
	@Autowired
	@Qualifier("mmbOneAWSClient")
	private OneAWSClient mmbOneAWSClient;
	@Autowired
	private DeletePnrService deletePnrService;
	@Autowired
	private MealRequestBuilder mealRequestBuilder;
	@Autowired
	private MealDTOService mealDTOServiceImpl;
	@Autowired
	private OneAErrorHandler oneAErrorHandler;	
	@Autowired
	private PnrResponseParser pnrResponseParser;
	@Autowired
	private CabinClassDAO cabinClassDAO;
	@Autowired
	private SpecialMealDAO specialMealDao;
	
	private static LogAgent logger = LogAgent.getLogAgent(MealServiceImpl.class);
	
	@Override
	public RetrievePnrBooking cancelMeal(RetrievePnrBooking pnr, List<CancelMealDetailDTO> requestDtos, Session session) throws BusinessBaseException {
		// Verify the request
		for(CancelMealDetailDTO cancelMealRequestDTO: requestDtos) {
			mealDTOServiceImpl.verifyCancelMealRequest(cancelMealRequestDTO);
		}
		
		// Proceed to cancel meal from PNR
		Map<String, List<String>> removeOTMap = this.buildRemoveOTMap(pnr, requestDtos);
		return (!removeOTMap.isEmpty()) ? deletePnrService.deletePnr(pnr.getOneARloc(), removeOTMap, session) : null;
	}

	@Override
	public RetrievePnrBooking addMeal(String rloc, List<AddMealDetailDTO> requestDtos, Session session) throws BusinessBaseException {
		// Verify the request
		for(AddMealDetailDTO requestDto: requestDtos) {
			mealDTOServiceImpl.verifyAddMealRequest(requestDto);
		}
		
		// Proceed to add meal to PNR
		PNRAddMultiElements request = mealRequestBuilder.buildRequest(rloc, requestDtos, session);
		OneaResponse<PNRReply> pnrReplay =mmbOneAWSClient.addMultiElements(request, session);
		if(pnrReplay == null){
			return null;
		}
		
		//check onea error for update
		checkOneAResponseError(pnrReplay.getBody(), pnrReplay.getOneAAction().getInterfaceCode());
			
		return pnrResponseParser.paserResponse(pnrReplay.getBody());
	}
	
	
	/**
	 * Needs to proceed OT map
	 * @param pnr
	 * @param requestDtos
	 * @return
	 */
	public boolean isRemoveOTMap(RetrievePnrBooking pnr, List<CancelMealDetailDTO> requestDtos) {
		return !this.buildRemoveOTMap(pnr, requestDtos).isEmpty();
	}
	
	/**
	 * Build map for removing OT
	 * @return
	 */
	private Map<String, List<String>> buildRemoveOTMap(RetrievePnrBooking pnr, List<CancelMealDetailDTO> requestDtos) {
		Map<String, List<String>> cancelDetailMap = new HashMap<>();
		List<String> qulifierList = Lists.newArrayList();
		for(CancelMealDetailDTO cancelMealRequestDTO: requestDtos) {
			List<String> qulifiers = this.buildCancelQulifierList(pnr.getPassengerSegments(), pnr.getSegments(), cancelMealRequestDTO);
			
			// Remove duplicated qulifiers because one OT can refer to multi-paxs
			for(String qulifier: qulifiers) {
				if(!qulifierList.contains(qulifier)) {
					qulifierList.add(qulifier);
				}
			}
		}
		if(!qulifierList.isEmpty()) {
			cancelDetailMap.put(OneAConstants.OT_QUALIFIER, qulifierList);
		}
		
		return cancelDetailMap;
	}
	
	
	/**
	 * Build a list of qulifier which should be removed from PNR
	 * @param paxSegments
	 * @param segments
	 * @param requestDto
	 * @return
	 */
	private List<String> buildCancelQulifierList(List<RetrievePnrPassengerSegment> paxSegments, List<RetrievePnrSegment> segments, CancelMealDetailDTO requestDto){
		List<String> qulifierList = new ArrayList<>();
		
		Optional.ofNullable(requestDto.getPaxIds()).orElseGet(Collections::emptyList).forEach(paxId->{
			RetrievePnrPassengerSegment passengerSegment = PnrResponseParser.getPassengerSegmentByIds(paxSegments, paxId, requestDto.getSegmentId());
			if(passengerSegment != null && passengerSegment.getMeal() != null && passengerSegment.getMeal().getQulifierId() != null) {
				/*
				 * check whether the meal type exists in the MMB table
				 */
                if (segments != null) {
                    segments.forEach(segment -> {
                        if (segment != null && segment.getSegmentID() != null
                                && segment.getSegmentID().equals(requestDto.getSegmentId())) {
                            RetrievePnrMeal pnrMeal = passengerSegment.getMeal();
                            if (null != pnrMeal) {
                                if (BooleanUtils.isTrue(pnrMeal.isPreSelectedMeal())) {
                                    qulifierList.add(pnrMeal.getQulifierId());
                                } else {
                                    String cabinClass = BookingBuildUtil.getCabinClassBySubClass(cabinClassDAO,
                                            segment.getMarketSubClass());
                                    List<String> mealList = Collections.emptyList();
                                    if (cabinClass != null) {
                                        mealList = getMealList(segment, cabinClass);
                                    }
                                    // String[] arr =
                                    // Optional.ofNullable(passengerSegment.getMeal().getMealCode()).orElse("").split(MealConstants.SPECIAL_MEAL_CODE_SEPARATOR);
                                    String paxMealCode = passengerSegment.getMeal().getMealCode();
                                    if (StringUtils.isNotEmpty(paxMealCode) && (mealList.contains(paxMealCode)
                                            || paxMealCode.contains(MealConstants.BABY_MEAL_CODE)
                                            || paxMealCode.contains(MealConstants.CHILD_MEAL_CODE)
                                            // special  handle for LIQUID meal (details: OLSSMMB-6375)
                                            || paxMealCode.contains(MealConstants.SPML_LIQUID_MEAL_FREETEXT) 
                                    )) {
                                        qulifierList.add(passengerSegment.getMeal().getQulifierId());
                                    }
                                }
                            }
                        }
                    });
                }
			}
		});

		return qulifierList;
	}
	
	@Override
	public List<String> getMealList(RetrievePnrSegment segment, String cabinClass) {
		return specialMealDao.getMealList(
				MMBConstants.APP_CODE,
				segment.getPnrOperateCompany(),
				cabinClass,
				segment.getOriginPort(),
				segment.getDestPort()
		);
	}
	
	private void checkOneAResponseError(PNRReply pnrReply,String oneAWsCallCode) throws BusinessBaseException{
		List<OneAError> oneAErrorCodeList = PnrResponseParser.getAllErrors(pnrReply);
		oneAErrorHandler.parseOneAErrorCode(oneAErrorCodeList, MMBConstants.APP_CODE,
				OneAErrorConstants.MMB_ACTION_NO_SPECIFIC, oneAWsCallCode);
	}
}
