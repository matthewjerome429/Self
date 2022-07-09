package com.cathaypacific.mmbbizrule.v2.business.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.constant.MealConstants;
import com.cathaypacific.mmbbizrule.db.dao.ConsentRecordDAOV2;
import com.cathaypacific.mmbbizrule.db.model.TbConsentRecord;
import com.cathaypacific.mmbbizrule.dto.request.meal.MealRequestDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.addMeal.AddMealDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.cancelMeal.CancelMealDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.updateMeal.UpdateMealRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.updateMeal.UpdateMealRequestDetailDTO;
import com.cathaypacific.mmbbizrule.handler.BookingBuildHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.model.booking.detail.SpecialMeal;
import com.cathaypacific.mmbbizrule.oneaservice.meal.service.MealDTOService;
import com.cathaypacific.mmbbizrule.oneaservice.meal.service.MealService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassenger;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrPassengerSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrSegment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.service.PaxNameIdentificationService;
import com.cathaypacific.mmbbizrule.v2.business.MealBusinessV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.meal.updateMeal.UpdateMealResponseDTOV2;
import com.cathaypacific.mmbbizrule.v2.handler.FlightBookingConverterHelperV2;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.header.SessionStatus;
import com.google.common.collect.Lists;

@Service
public class MealBusinessImplV2 implements MealBusinessV2 {
	
    private static LogAgent logger = LogAgent.getLogAgent(MealBusinessImplV2.class);

    @Autowired
    private PnrInvokeService pnrInvokeService;
    
    @Autowired
    private MealService mealServiceImpl;
    
    @Autowired
    private MealDTOService mealDTOServiceImpl;
    
    @Autowired
    private BookingBuildHelper bookingBuildHelper;

    @Autowired
    private FlightBookingConverterHelperV2 flightBookingConverterHelper;
    
    @Autowired
	private PaxNameIdentificationService paxNameIdentificationService;
    
    @Autowired
	private ConsentRecordDAOV2 consentRecordDAOV2;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UpdateMealResponseDTOV2 updateMeal(UpdateMealRequestDTO mealRequestDTO, LoginInfo loginInfo) throws BusinessBaseException {

        // Verify request
        for(UpdateMealRequestDetailDTO updateMealDetailDTO : mealRequestDTO.getUpdateMealDetails()) {
            mealDTOServiceImpl.verifyUpdateMealRequest(updateMealDetailDTO);
        }

        // Retrieve booking
        RetrievePnrBooking pnrBooking = pnrInvokeService.retrievePnrByRloc(mealRequestDTO.getRloc());
        if(pnrBooking == null) {
            throw new UnexpectedException(String.format("Unable to update meal - Cannot find booking by rloc:%s",
            		mealRequestDTO.getRloc()), new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
        }
        
        // Identify login(primary) passenger
        paxNameIdentificationService.primaryPassengerIdentificationByInFo(loginInfo, pnrBooking);
        
        // Record consent, only access-channel = "WEB" will trigger record consent.
        if(MMBBizruleConstants.ACCESS_CHANNEL_WEB.equalsIgnoreCase(MMBUtil.getCurrentAccessChannel())) {
        	recordConsent(mealRequestDTO, pnrBooking);        	
        }

        // Update meal
        UpdateMealResponseDTOV2 updateMealResponseDTO = updateMeal(mealRequestDTO, loginInfo, pnrBooking);
        updateMealResponseDTO.setSuccess(true);

        // OLSSMMB-16763: Don't remove me!!!
        logging(mealRequestDTO);
        
        return updateMealResponseDTO;
    }

	/**
	 * Record consent
	 * 
	 * @param mealRequestDTO
	 * @param pnrBooking
	 * @throws ExpectedException 
	 */
	private void recordConsent(UpdateMealRequestDTO mealRequestDTO, RetrievePnrBooking pnrBooking) throws ExpectedException {
		String rloc = mealRequestDTO.getRloc();
		List<RetrievePnrPassenger> pnrPassengers = pnrBooking.getPassengers();
		List<RetrievePnrSegment> pnrSegments = pnrBooking.getSegments();
		
		RetrievePnrPassenger loginPassenger = pnrBooking.getPassengers().stream()
				.filter(passenger -> passenger != null && BooleanUtils.isTrue(passenger.isPrimaryPassenger())).findFirst().orElse(null);		

		List<UpdateMealRequestDetailDTO> updateMealRequestDetailDTOs = mealRequestDTO.getUpdateMealDetails();
		for(UpdateMealRequestDetailDTO updateMealRequestDetailDTO : updateMealRequestDetailDTOs) {
			if(updateMealRequestDetailDTO == null) {
				continue;
			}
			
			RetrievePnrSegment pnrSegment = PnrResponseParser.getSegmentById(pnrSegments, updateMealRequestDetailDTO.getSegmentId());
			if(pnrSegment == null) {
				throw new ExpectedException(String.format("recordConsent -> failue, can't find segment[%s] in booking[%s]",
						updateMealRequestDetailDTO.getSegmentId(), rloc), new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
			}
			
			// All special meals under the segment
			List<SpecialMeal> consentMeals = getConsentMeals(pnrSegment);
			if(CollectionUtils.isEmpty(consentMeals)) {
				logger.info(String.format("recordConsent -> skip the segment[%s] due to no consent meals for the segment in booking[%s]", pnrSegment.getSegmentID(), rloc));
				continue;
			}
			
			List<MealRequestDetailDTO> mealDetails = updateMealRequestDetailDTO.getMealDetails();
			for(MealRequestDetailDTO mealDetail : mealDetails) {
				if(mealDetail == null || StringUtils.isEmpty(mealDetail.getMealCode())) {
					continue;
				}
				
				boolean needRecordConsent = consentMeals.stream().anyMatch(meal -> meal != null && mealDetail.getMealCode().equalsIgnoreCase(meal.getType()));
				if(!needRecordConsent) {
					logger.info(String.format("recordConsent -> skip the request meal[%s] due to consent=false in the segment[%s] in booking[%s]",
							mealDetail.getMealCode(), pnrSegment.getSegmentID(), rloc));
					continue;
				}
				
				RetrievePnrPassenger pnrPassenger = PnrResponseParser.getPassengerById(pnrPassengers, mealDetail.getPassengerId());
				if(pnrPassenger == null) {
					throw new ExpectedException(String.format("recordConsent -> failue, can't find passenger[%s] in booking[%s]",
							mealDetail.getPassengerId(), rloc), new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
				}
				
				RetrievePnrPassengerSegment pnrPassengerSegment = PnrResponseParser.getPassengerSegmentByIds(pnrBooking.getPassengerSegments(),
						mealDetail.getPassengerId(), pnrSegment.getSegmentID());
				if(pnrPassengerSegment == null) {
					throw new ExpectedException(String.format("recordConsent -> failue, can't find passenger[%s]segment[%s] in booking[%s]",
							mealDetail.getPassengerId(), pnrSegment.getSegmentID(), rloc), new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
				}
				
				saveConsentRecord(loginPassenger, rloc, pnrSegment, mealDetail, pnrPassenger, pnrPassengerSegment);
			}
		}
	}

	/**
	 * Get all special meals which need to record consent by segment
	 * 
	 * @param pnrSegment
	 * @return
	 */
	private List<SpecialMeal> getConsentMeals(RetrievePnrSegment pnrSegment) {
		if(pnrSegment == null) {
			return Collections.emptyList();
		}
		
		List<SpecialMeal> specialMeals = bookingBuildHelper.getSpecialMeals(MMBConstants.APP_CODE, pnrSegment.getPnrOperateCompany(), 
				bookingBuildHelper.getCabinClassBySubClass(pnrSegment.getSubClass()), pnrSegment.getOriginPort(), pnrSegment.getDestPort());
		
		return specialMeals.stream().filter(meal -> meal != null && BooleanUtils.isTrue(meal.isConsent())).collect(Collectors.toList());
	}

	/**
	 * Save consent record
	 * 
	 * @param loginPassenger
	 * @param rloc
	 * @param pnrSegment
	 * @param mealDetail
	 * @param pnrPassenger
	 * @param pnrPassengerSegment
	 * @return TbConsentRecord
	 */
	private TbConsentRecord saveConsentRecord(RetrievePnrPassenger loginPassenger, String rloc, RetrievePnrSegment pnrSegment,
			MealRequestDetailDTO mealDetail, RetrievePnrPassenger pnrPassenger, RetrievePnrPassengerSegment pnrPassengerSegment) {
		TbConsentRecord tbConsentRecord = new TbConsentRecord();
		tbConsentRecord.setRloc(rloc);
		tbConsentRecord.setConsentDate(new Date());
		tbConsentRecord.setSsr(mealDetail.getMealCode());
		tbConsentRecord.setAppCode(MMBUtil.getCurrentAppCode());
		tbConsentRecord.setLoginSource(MMBUtil.getCurrentAccessChannel());
		
		String acceptLanguage = MMBUtil.getCurrentAcceptLanguage();
		if(StringUtils.isNotEmpty(acceptLanguage) && acceptLanguage.length() >= 4) {
			tbConsentRecord.setLanguage(acceptLanguage.substring(0, 2));			
			tbConsentRecord.setLocale(acceptLanguage.substring(3));
		}
		
		/** login passenger */
		if(loginPassenger != null) {
			tbConsentRecord.setLoginPaxFirstName(loginPassenger.getGivenName());
			tbConsentRecord.setLoginPaxLastName(loginPassenger.getFamilyName());			
		}
		
		/** segment */
		tbConsentRecord.setMarketingcarrier(pnrSegment.getMarketCompany());
		tbConsentRecord.setOperatingCarrier(pnrSegment.getPnrOperateCompany());
		tbConsentRecord.setFlightNo(pnrSegment.getMarketSegmentNumber());
		try{
			tbConsentRecord.setFlightDepartureTime(DateUtil.getStrToDate(DateUtil.DATETIME_PATTERN_YYYY_MM_DDHHMM, pnrSegment.getDepartureTime().getTime()));
		} catch(Exception e) {
			logger.warn(String.format("saveConsentRecord -> ignore flightDepartureTime column, booking[%s] by passengerId[%s] segmentId[%s]",
					rloc, mealDetail.getPassengerId(), pnrSegment.getSegmentID()), e);
		}
		
		/** passenger */
		tbConsentRecord.setSsrPaxFirstName(pnrPassenger.getGivenName());
		tbConsentRecord.setSsrPaxLastName(pnrPassenger.getFamilyName());
		
		try {
			tbConsentRecord.seteTicket(bookingBuildHelper.getEticketNumber(pnrPassengerSegment, pnrSegment));
		} catch(Exception e) {
			logger.warn(String.format("saveConsentRecord -> skip eticket column, can't get ticket number in booking[%s] by passengerId[%s] segmentId[%s]",
					rloc, mealDetail.getPassengerId(), pnrSegment.getSegmentID()), e);
		}
		
		return consentRecordDAOV2.save(tbConsentRecord);
	}

	/**
	 * Adding meal
	 * 
	 * @param mealRequestDTO
	 * @param loginInfo
	 * @param pnr
	 * @return
	 * @throws BusinessBaseException
	 * @throws ExpectedException
	 * @throws SoapFaultException
	 */
	private UpdateMealResponseDTOV2 updateMeal(UpdateMealRequestDTO mealRequestDTO,
			LoginInfo loginInfo, RetrievePnrBooking pnr) throws BusinessBaseException {
		// Build cancel meal request
        List<CancelMealDetailDTO> cancelMealRequestDTOs = new ArrayList<>();
        for (UpdateMealRequestDetailDTO updateMealDetailDTO : mealRequestDTO.getUpdateMealDetails()) {
            CancelMealDetailDTO cancelMealRequestDTO = mealDTOServiceImpl.convertToCancelMealRequest(updateMealDetailDTO);
            if (cancelMealRequestDTO != null) {
                cancelMealRequestDTOs.add(cancelMealRequestDTO);
            }
        }

        // Build add meal request
        List<AddMealDetailDTO> addMealRequestDTOs = Lists.newArrayList();
        for (UpdateMealRequestDetailDTO updateMealDetailDTO : mealRequestDTO.getUpdateMealDetails()) {
            AddMealDetailDTO addMealRequestDTO = mealDTOServiceImpl.convertToAddMealRequest(pnr, updateMealDetailDTO);
            if (addMealRequestDTO != null) {
                addMealRequestDTOs.add(addMealRequestDTO);
            }
        }

        /*
         * Handle Meal SSR associated with multi-paxs
         * Since meal SSR can be associated with multi-paxs, if one of the meals is removed, the whole SSR will be removed.
         * It causes all the associated meals are also removed.
         * We have to add the other meals back after removing the whole SSR
         */
        for (UpdateMealRequestDetailDTO updateMealDetailDTO : mealRequestDTO.getUpdateMealDetails()) {
            AddMealDetailDTO addAssociatedMealRequestDTO = mealDTOServiceImpl.convertToAddAssociatedMealRequest(pnr, updateMealDetailDTO);
            if (addAssociatedMealRequestDTO != null && !addAssociatedMealRequestDTO.getPaxMealDetails().isEmpty()) {
                addMealRequestDTOs.add(addAssociatedMealRequestDTO);    // Combine with add meal request list
            }
        }

        // Check if session is needed
        Session session = null;
        if (mealServiceImpl.isRemoveOTMap(pnr, cancelMealRequestDTOs) && !addMealRequestDTOs.isEmpty()) {
            session = new Session();
        }
        UpdateMealResponseDTOV2 updateMealResponseDTO = new UpdateMealResponseDTOV2();
        // Start to process 1A request here
        try {
            // Send cancel meal request to 1A
            RetrievePnrBooking booking = null;
            if (!cancelMealRequestDTOs.isEmpty()) {
                booking = mealServiceImpl.cancelMeal(pnr, cancelMealRequestDTOs, session);
            }

            // Update session status
            if (booking != null && session != null) {
                session = booking.getSession();
                session.setStatus(SessionStatus.END.getStatus());//set end transaction
            }

            // Send add meal request to 1A
            if (!addMealRequestDTOs.isEmpty()) {
                booking = mealServiceImpl.addMeal(mealRequestDTO.getRloc(), addMealRequestDTOs, session);
            }

            // If it has removed meal or add meal, the booking variable won't be empty.
            // If didn't process any update, we can use pnr variable to be the booking.
            BookingBuildRequired bookingBuildRequired = new BookingBuildRequired();
            bookingBuildRequired.setPreSelectedMeal(true);
            if (booking != null) {
                updateMealResponseDTO.setBooking(flightBookingConverterHelper.flightBookingDTOConverter(booking, loginInfo, bookingBuildRequired, true));
            } else if (pnr != null) {
                updateMealResponseDTO.setBooking(flightBookingConverterHelper.flightBookingDTOConverter(pnr, loginInfo, bookingBuildRequired, true));
                logger.warn("Didn't proceed any meal update to PNR. The reason maybe a empty request is received.");
            }
        } catch (SoapFaultException e) {
            logger.error(String.format("updateMeal => failure, booking[%s]", mealRequestDTO.getRloc()), e);
            throw e;
        }
		return updateMealResponseDTO;
	}
	
	/**
	 * Logging
	 * 
	 * @param mealRequestDTO
	 */
	private void logging(UpdateMealRequestDTO mealRequestDTO) {
		// --- begin ---
        try {
            for (UpdateMealRequestDetailDTO updateMealRequestDetailDTO : mealRequestDTO.getUpdateMealDetails()) {
                for (MealRequestDetailDTO mealRequestDetailDTO : updateMealRequestDetailDTO.getMealDetails()) {
                    if(BooleanUtils.isTrue(mealRequestDetailDTO.isPreSelectedMeal()) && StringUtils.isNotEmpty(mealRequestDetailDTO.getMealCode())) {
                        String mealCode = MealConstants.SPECIAL_MEAL_CODE;
                        String freeText = mealRequestDetailDTO.getMealCode().substring(0,2);
                        logger.info(String.format(
                                "Update | Meal | Rloc | %s | Passenger ID | %s | Segment ID | %s | Meal Code | %s | FreeText | %s*",
                                mealRequestDTO.getRloc(), mealRequestDetailDTO.getPassengerId(),
                                updateMealRequestDetailDTO.getSegmentId(), mealCode, freeText), true);
                    } else {
                        String mealCode = StringUtils.isNotEmpty(mealRequestDetailDTO.getMealCode())
                                ? mealRequestDetailDTO.getMealCode()
                                : "Standard Meal";
                        logger.info(String.format(
                                "Update | Meal | Rloc | %s | Passenger ID | %s | Segment ID | %s | Meal Code | %s",
                                mealRequestDTO.getRloc(), mealRequestDetailDTO.getPassengerId(),
                                updateMealRequestDetailDTO.getSegmentId(), mealCode), true);
                    }
                   
                }
            }
        } catch (Exception e) {

        }
        // --- end ---
	}
	
}
