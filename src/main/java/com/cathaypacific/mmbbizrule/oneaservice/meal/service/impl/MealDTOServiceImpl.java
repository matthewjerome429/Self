package com.cathaypacific.mmbbizrule.oneaservice.meal.service.impl;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mmbbizrule.constant.MealConstants;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.dto.request.meal.MealRequestDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.addMeal.AddMealDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.cancelMeal.CancelMealDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.updateMeal.UpdateMealRequestDetailDTO;
import com.cathaypacific.mmbbizrule.oneaservice.meal.service.MealDTOService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrMeal;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

@Service
public class MealDTOServiceImpl implements MealDTOService {
	

	public CancelMealDetailDTO convertToCancelMealRequest(UpdateMealRequestDetailDTO updateMealRequestDTO) throws BusinessBaseException {
		verifyUpdateMealRequest(updateMealRequestDTO);
		
		CancelMealDetailDTO cancelMealRequestDTO = new CancelMealDetailDTO();
		cancelMealRequestDTO.setSegmentId(updateMealRequestDTO.getSegmentId());
		
		cancelMealRequestDTO.setPaxIds(Lists.newArrayList());
		for(MealRequestDetailDTO paxMealDetailDTO: updateMealRequestDTO.getMealDetails()) {
			// if this is a cancel request
			if(paxMealDetailDTO.getPassengerId() != null) {
				cancelMealRequestDTO.getPaxIds().add(paxMealDetailDTO.getPassengerId());
			}
		}
		
		return (cancelMealRequestDTO.getPaxIds().isEmpty()) ? null : cancelMealRequestDTO;
	}
	
	public AddMealDetailDTO convertToAddMealRequest(RetrievePnrBooking pnr, UpdateMealRequestDetailDTO updateMealRequestDTO) throws BusinessBaseException {
		verifyUpdateMealRequest(updateMealRequestDTO);
		
		AddMealDetailDTO addMealRequestDTO = new AddMealDetailDTO();
		addMealRequestDTO.setSegmentId(updateMealRequestDTO.getSegmentId());
		addMealRequestDTO.setPaxMealDetails(Lists.newArrayList());
		addMealRequestDTO.setCompanyId(updateMealRequestDTO.getCompanyId());
		String operator = updateMealRequestDTO.getOperator();
		String segmentId = updateMealRequestDTO.getSegmentId();
		for(MealRequestDetailDTO paxMealDetailDTO: updateMealRequestDTO.getMealDetails()) {
			
			// if this is an add request
			if(isAddMealRequestDetail(paxMealDetailDTO) && updateMealRequestDTO.getCompanyId() != null) {
				String paxId = paxMealDetailDTO.getPassengerId();
				String mealCode = generateMealCode(paxMealDetailDTO, operator);
				String freeText = generateFreeText(pnr, paxMealDetailDTO, operator, segmentId);
				int quantity = paxMealDetailDTO.getQuantity();
				
				MealRequestDetailDTO mealRequestDetailDTO = new MealRequestDetailDTO();
				mealRequestDetailDTO.setMealCode(mealCode);
				mealRequestDetailDTO.setFreeText(freeText);
				mealRequestDetailDTO.setQuantity(quantity);
				mealRequestDetailDTO.setPassengerId(paxId);
				
				addMealRequestDTO.getPaxMealDetails().add(mealRequestDetailDTO);
			}
		}
		
		return (addMealRequestDTO.getPaxMealDetails().isEmpty()) ? null : addMealRequestDTO;
	}
	
    /**
	 * This method is to build add meal request for all other meals associated with the same SSR of the update meal.
	 * @param updateMealRequestDTO
	 * @return AddMealDetailDTO
	 * @throws BusinessBaseException
	 */
	public AddMealDetailDTO convertToAddAssociatedMealRequest(RetrievePnrBooking pnrBooking, UpdateMealRequestDetailDTO updateMealRequestDTO) throws BusinessBaseException {
		verifyUpdateMealRequest(updateMealRequestDTO);
		
		// Get all main pax ids
		List<String> allMainPaxIds = Lists.newArrayList();
		for(MealRequestDetailDTO paxMealDetailDTO: updateMealRequestDTO.getMealDetails()) {
			if (isAddMealRequestDetail(paxMealDetailDTO) && updateMealRequestDTO.getCompanyId() != null) {	// is add request
				allMainPaxIds.add(paxMealDetailDTO.getPassengerId());
			} else if (paxMealDetailDTO.getPassengerId() != null) {		// is cancel request
				allMainPaxIds.add(paxMealDetailDTO.getPassengerId());
			}
		}
		
		// Create add meal request details for all associated paxs
		List<MealRequestDetailDTO> mealRequestDetailDTOs = Lists.newArrayList();
		AddMealDetailDTO addAssociatedMealRequestDTO = new AddMealDetailDTO();
		addAssociatedMealRequestDTO.setSegmentId(updateMealRequestDTO.getSegmentId());
		addAssociatedMealRequestDTO.setPaxMealDetails(mealRequestDetailDTOs);
		addAssociatedMealRequestDTO.setCompanyId(updateMealRequestDTO.getCompanyId());
		String operator = updateMealRequestDTO.getOperator();
		
		// Loop through the update details
		String segmentId = updateMealRequestDTO.getSegmentId();
		List<String> associatedPaxIds = Lists.newArrayList();
		for(MealRequestDetailDTO paxMealDetailDTO: updateMealRequestDTO.getMealDetails()) {
			
			// If this is a add or cancel request
			if ((isAddMealRequestDetail(paxMealDetailDTO) && updateMealRequestDTO.getCompanyId() != null) ||
					(paxMealDetailDTO.getPassengerId() != null)) {
				
				// Then retrieve the pax who update meal, and other associated paxs
				String mainPaxId = paxMealDetailDTO.getPassengerId();
				String qulifierId = retrieveMealQulifierId(pnrBooking, segmentId, mainPaxId);
				associatedPaxIds = retrieveMealAssociatedPaxIds(pnrBooking, qulifierId, allMainPaxIds);
				
				if (associatedPaxIds != null && !associatedPaxIds.isEmpty()) {
					
					// Loop through all associated pax, and create add meal request for them
					for(String associatedPaxId: associatedPaxIds) {
						RetrievePnrMeal pnrMeal = retrieveMeal(pnrBooking, segmentId, associatedPaxId);
						
						MealRequestDetailDTO mealRequestDetailDTO = new MealRequestDetailDTO();
						if(null == pnrMeal) {
						    continue;
						}
                        mealRequestDetailDTO.setMealCode(BooleanUtils.isTrue(pnrMeal.isPreSelectedMeal())
                                ? generateMealCode(paxMealDetailDTO, operator)
                                : pnrMeal.getMealCode());
						mealRequestDetailDTO.setQuantity(1);
						mealRequestDetailDTO.setPassengerId(associatedPaxId);	
						String freeText = generateFreeTextForAssociate(pnrBooking, pnrMeal,paxMealDetailDTO, operator, segmentId);
						mealRequestDetailDTO.setFreeText(freeText);
						
						// Check if already exist, if not, then add it
						if(!isExistRequestDetails(mealRequestDetailDTOs, mealRequestDetailDTO.getPassengerId())) {
							mealRequestDetailDTOs.add(mealRequestDetailDTO);
						}
					}
				}
				
			}
		}
		
		return (addAssociatedMealRequestDTO.getPaxMealDetails().isEmpty()) ? null : addAssociatedMealRequestDTO;
	}
	
	private boolean isAddMealRequestDetail(MealRequestDetailDTO paxMealDetailDTO) {
		return (paxMealDetailDTO.getMealCode() != null && paxMealDetailDTO.getQuantity() > 0);
	}
	
	private boolean isExistRequestDetails(List<MealRequestDetailDTO> mealRequestDetailDTOs, String paxId) {
		for(MealRequestDetailDTO mealRequestDetailDTO: mealRequestDetailDTOs) {
			if(paxId != null && paxId.equals(mealRequestDetailDTO.getPassengerId())) {
				return true;
			}
		}
		return false;
	}
	
	// Retrieve meal qulifier id from pnr booking
	private String retrieveMealQulifierId(RetrievePnrBooking pnrBooking, String segmentId, String paxId) {
		if (pnrBooking == null) {
			return null;
		}
		
		for(RetrievePnrPassengerSegment pnrPaxSeg: pnrBooking.getPassengerSegments()) {
			if (pnrPaxSeg.getSegmentId() != null && pnrPaxSeg.getSegmentId().equals(segmentId) && 
					pnrPaxSeg.getPassengerId() != null && pnrPaxSeg.getPassengerId().equals(paxId) &&
					pnrPaxSeg.getMeal() != null) {
				return pnrPaxSeg.getMeal().getQulifierId();
			}
		}
		return null;
	}
	
	// Retrieve meal associated pax id from pnr booking
	private List<String> retrieveMealAssociatedPaxIds(RetrievePnrBooking pnrBooking, String qulifierId, List<String> allMainPaxIds) {
		List<String> assocaitedPaxIds = Lists.newArrayList();
		
		if (pnrBooking == null) {
			return assocaitedPaxIds;
		}
		
		for(RetrievePnrPassengerSegment pnrPaxSeg: pnrBooking.getPassengerSegments()) {
			RetrievePnrMeal pnrMeal = pnrPaxSeg.getMeal();
			if(pnrMeal != null && pnrMeal.getQulifierId() != null && pnrMeal.getQulifierId().equals(qulifierId) &&
					pnrPaxSeg.getPassengerId() != null && allMainPaxIds != null && !allMainPaxIds.contains(pnrPaxSeg.getPassengerId())) {
				assocaitedPaxIds.add(pnrPaxSeg.getPassengerId());
			}
			
		}
		return assocaitedPaxIds;
	}
	
	// Retrieve meal from pnrBooking
	private RetrievePnrMeal retrieveMeal(RetrievePnrBooking pnrBooking, String segmentId, String paxId) {
		if (pnrBooking == null) {
			return null;
		}
		
		for(RetrievePnrPassengerSegment pnrPaxSeg: pnrBooking.getPassengerSegments()) {
			if (pnrPaxSeg.getSegmentId() != null && pnrPaxSeg.getSegmentId().equals(segmentId) && 
					pnrPaxSeg.getPassengerId() != null && pnrPaxSeg.getPassengerId().equals(paxId)) {
				return pnrPaxSeg.getMeal();
			}
		}
		
		return null;
	}
	
	private String parseMealCode(MealRequestDetailDTO mealDetail, String operator) {
		String mealCode = mealDetail.getMealCode();
		if(mealCode.startsWith(MealConstants.SPECIAL_MEAL_CODE)) {		// if special meal (format is SPML_XXXX where XXXX is free text)
			return MealConstants.SPECIAL_MEAL_CODE;
		} else if (isIndividualBabyWithBabyMealOnCXKA(mealDetail, operator)){			// if individual baby with baby meal
			return MealConstants.SPECIAL_MEAL_CODE;
		} else if (isNonSeatOccupyingBabyWithChildMeal(mealDetail, operator)) {	// if non-seat occupying baby with child meal
			return MealConstants.SPECIAL_MEAL_CODE;
		}
		return mealCode;
	}
	
	private String parseFreeText(MealRequestDetailDTO mealDetail, String operator) {
		String mealCode = mealDetail.getMealCode();
		String[] mealCodeSplits = mealCode.split(MealConstants.SPECIAL_MEAL_CODE_SEPARATOR);
		
		if(isIndividualBabyWithBabyMealOnCXKA(mealDetail, operator)){					// if individual baby with baby meal
			return MealConstants.SPML_BBML_FREETEXT;
		} else if (isNonSeatOccupyingBabyWithChildMeal(mealDetail, operator)) {	// if non-seat occupying baby with child meal
			return MealConstants.SPML_CHML_FREETEXT;
		} else if (mealCodeSplits.length >= 2){
			if(MealConstants.SPML_LIQUID_MEAL_FREETEXT.contains(mealCodeSplits[1])) {
				return MealConstants.SPML_LIQUID_MEAL_FREETEXT;
			}else {
				// if special meal (format is SPML_XXXX where XXXX is free text)
				return mealCodeSplits[1];
			}
		}
		return null;
	}
	
	private boolean isIndividualBabyWithBabyMealOnCXKA(MealRequestDetailDTO mealDetail, String operator) {
		return MealConstants.BABY_MEAL_CODE.equals(mealDetail.getMealCode()) &&								// check if baby meal	
				!mealDetail.getPassengerId().contains(MealConstants.PASSENGER_INFANT_ID_SUFFIX) &&			// check if not a baby pax id format
				(OneAConstants.COMPANY_CX.equals(operator) || OneAConstants.COMPANY_KA.equals(operator));	// check if operated by CX or KA
	}
	
	private boolean isNonSeatOccupyingBabyWithChildMeal(MealRequestDetailDTO mealDetail, String operator) {
		return MealConstants.CHILD_MEAL_CODE.equals(mealDetail.getMealCode()) &&							// check if child meal	
				mealDetail.getPassengerId().contains(MealConstants.PASSENGER_INFANT_ID_SUFFIX);			// check if non-seat occupied infant
	}
	
	public void verifyUpdateMealRequest(UpdateMealRequestDetailDTO requestDto) throws BusinessBaseException {
		if(requestDto == null || Strings.isNullOrEmpty(requestDto.getSegmentId()) || 
				requestDto.getMealDetails() == null || requestDto.getMealDetails().isEmpty() || requestDto.getOperator().isEmpty()) {
			throw new UnexpectedException("Invalid UpdateMealRequestDTO due to empty parameter(s) received", createSystemErrorInfo());
		}
		
		List<MealRequestDetailDTO> paxMealDetailDTOs = requestDto.getMealDetails();
		for(MealRequestDetailDTO paxMealDetailDTO: paxMealDetailDTOs) {
			if(Strings.isNullOrEmpty(paxMealDetailDTO.getPassengerId())) {
				throw new UnexpectedException("Invalid PaxMealDetailDTO of UpdateMealRequestDTO due to invalid parameter(s) received", createSystemErrorInfo());
			}
		}
	}
	
	public void verifyAddMealRequest(AddMealDetailDTO requestDto) throws BusinessBaseException {
		if(requestDto == null || Strings.isNullOrEmpty(requestDto.getSegmentId()) || 
				requestDto.getPaxMealDetails() == null || requestDto.getPaxMealDetails().isEmpty() ||
				Strings.isNullOrEmpty(requestDto.getCompanyId())) {
			throw new UnexpectedException("Invalid AddMealRequestDTO due to empty parameter(s) received", createSystemErrorInfo());
		}

		List<MealRequestDetailDTO> paxMealDetailDTOs = requestDto.getPaxMealDetails();
		for(MealRequestDetailDTO paxMealDetailDTO: paxMealDetailDTOs) {
			if(Strings.isNullOrEmpty(paxMealDetailDTO.getMealCode()) || paxMealDetailDTO.getQuantity() <= 0 ||  Strings.isNullOrEmpty(paxMealDetailDTO.getPassengerId())) {
				throw new UnexpectedException("Invalid PaxMealDetailDTO of AddMealRequestDTO due to invalid parameter(s) received", createSystemErrorInfo());
			}
		}
	}

	public void verifyCancelMealRequest(CancelMealDetailDTO requestDto) throws BusinessBaseException {
		if(requestDto == null || Strings.isNullOrEmpty(requestDto.getSegmentId()) || 
				requestDto.getPaxIds() == null || requestDto.getPaxIds().isEmpty()) {
			throw new UnexpectedException("Invalid CancelMealRequestDTO due to empty parameter(s) received", createSystemErrorInfo());
		}
	}
	
	private ErrorInfo createSystemErrorInfo() {
		return new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
	}
	
	   /**
     * parse free text
     * @param pnr
     * @param pnrMeal 
     * @param paxMealDetailDTO
     * @param operator
     * @param segmentId
     * @return
     */
    private String generateFreeTextForAssociate(RetrievePnrBooking pnr, RetrievePnrMeal pnrMeal, MealRequestDetailDTO paxMealDetailDTO, String operator,
            String segmentId) {
        if(BooleanUtils.isTrue(paxMealDetailDTO.isPreSelectedMeal())) {
            RetrievePnrSegment pnrSegment = pnr.getSegments().stream()
                    .filter(segment -> null != segment && segmentId.equals(segment.getSegmentID())).findFirst()
                    .orElse(null);
            if (null != pnrSegment) {
                return generateFreeTextByFlightMealInfo(pnrMeal.getMealCode(), pnrSegment.getMarketCompany(),
                        pnrSegment.getMarketSegmentNumber(), pnrSegment.getOriginPort(), pnrSegment.getDestPort());
            }
        }
        return parseFreeText(paxMealDetailDTO, operator);
    }

    /**
     * generate free text
     * @param mealCode
     * @param marketCompany
     * @param marketSegmentNumber
     * @param originPort
     * @param destPort
     * @return
     */
    private String generateFreeTextByFlightMealInfo(String mealCode, String marketCompany, String marketSegmentNumber,
            String originPort, String destPort) {
        return mealCode + " " + marketCompany + marketSegmentNumber + " " + originPort + destPort;
    }

    /**
     * parse free text
     * @param pnr
     * @param paxMealDetailDTO
     * @param operator
     * @param segmentId
     * @return
     */
    private String generateFreeText(RetrievePnrBooking pnr, MealRequestDetailDTO paxMealDetailDTO, String operator,
            String segmentId) {
        if(BooleanUtils.isTrue(paxMealDetailDTO.isPreSelectedMeal())) {
            RetrievePnrSegment pnrSegment = pnr.getSegments().stream()
                    .filter(segment -> null != segment && segmentId.equals(segment.getSegmentID())).findFirst()
                    .orElse(null);
            if (null != pnrSegment) {
                return generateFreeTextByFlightMealInfo(paxMealDetailDTO.getMealCode(), pnrSegment.getMarketCompany(),
                        pnrSegment.getMarketSegmentNumber(), pnrSegment.getOriginPort(), pnrSegment.getDestPort());
            }
        }
        return parseFreeText(paxMealDetailDTO, operator);
    }

    /**
     * parse meal code
     * @param pnr
     * @param paxMealDetailDTO
     * @param operator
     * @param segmentId
     * @return
     */
    private String generateMealCode(MealRequestDetailDTO paxMealDetailDTO, String operator) {
        if (BooleanUtils.isTrue(paxMealDetailDTO.isPreSelectedMeal())) {
            return MealConstants.SPECIAL_MEAL_CODE;
        }
        return parseMealCode(paxMealDetailDTO, operator);
    }
	
}
