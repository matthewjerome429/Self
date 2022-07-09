package com.cathaypacific.mmbbizrule.business.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.exception.SoapFaultException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.MealBusiness;
import com.cathaypacific.mmbbizrule.constant.MealOption;
import com.cathaypacific.mmbbizrule.db.dao.MealIneligibilityDAO;
import com.cathaypacific.mmbbizrule.db.model.MealOptionKey;
import com.cathaypacific.mmbbizrule.dto.request.meal.MealRequestDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.addMeal.AddMealDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.cancelMeal.CancelMealDetailDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.updateMeal.UpdateMealRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.meal.updateMeal.UpdateMealRequestDetailDTO;
import com.cathaypacific.mmbbizrule.dto.response.meal.updateMeal.UpdateMealResponseDTO;
import com.cathaypacific.mmbbizrule.handler.FlightBookingConverterHelper;
import com.cathaypacific.mmbbizrule.model.booking.detail.BookingBuildRequired;
import com.cathaypacific.mmbbizrule.oneaservice.meal.service.MealDTOService;
import com.cathaypacific.mmbbizrule.oneaservice.meal.service.MealService;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.oneaconsumer.model.header.Session;
import com.cathaypacific.oneaconsumer.model.header.SessionStatus;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

@Service
public class MealBusinessImpl implements MealBusiness {
    private static LogAgent logger = LogAgent.getLogAgent(MealBusinessImpl.class);
    private static final Gson GSON = new Gson();


    @Autowired
    private PnrInvokeService pnrInvokeService;
    @Autowired
    MealService mealServiceImpl;
    @Autowired
    private MealDTOService mealDTOServiceImpl;

    @Autowired
    private MealIneligibilityDAO mealIneligibilityDao;

    @Autowired
    private FlightBookingConverterHelper flightBookingConverterHelper;

    @Override
    public UpdateMealResponseDTO updateMeal(UpdateMealRequestDTO mealRequestDTO, LoginInfo loginInfo) throws BusinessBaseException {
        logger.debug(String.format("request json:{%s}", GSON.toJson(mealRequestDTO)));

        // Verify request
        for (UpdateMealRequestDetailDTO updateMealDetailDTO : mealRequestDTO.getUpdateMealDetails()) {
            mealDTOServiceImpl.verifyUpdateMealRequest(updateMealDetailDTO);
        }

        // Retrieve RLOC
        RetrievePnrBooking pnr = pnrInvokeService.retrievePnrByRloc(mealRequestDTO.getRloc());

        if (pnr == null) {
            throw new UnexpectedException(
                    String.format("Unable to update meal - Cannot find booking by rloc:%s", mealRequestDTO.getRloc()),
                    new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
        }

        // Build cancel meal request
        List<CancelMealDetailDTO> cancelMealRequestDTOs = Lists.newArrayList();
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
        UpdateMealResponseDTO updateMealResponseDTO = new UpdateMealResponseDTO();
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
            if (booking != null) {
                updateMealResponseDTO.setBooking(flightBookingConverterHelper.flightBookingDTOConverter(booking, loginInfo, new BookingBuildRequired(), true));
            } else if (pnr != null) {
                updateMealResponseDTO.setBooking(flightBookingConverterHelper.flightBookingDTOConverter(pnr, loginInfo, new BookingBuildRequired(), true));
                logger.warn("Didn't proceed any meal update to PNR. The reason maybe a empty request is received.");
            }
        } catch (SoapFaultException e) {
            logger.error(String.format("Update meal failed with request json:{%s}", GSON.toJson(mealRequestDTO)), e);
            throw e;
        }
        updateMealResponseDTO.setSuccess(true);

        // OLSSMMB-16763: Don't remove me!!!
        // --- begin ---
        try {
            for (UpdateMealRequestDetailDTO updateMealRequestDetailDTO : mealRequestDTO.getUpdateMealDetails()) {
                for (MealRequestDetailDTO mealRequestDetailDTO : updateMealRequestDetailDTO.getMealDetails()) {
                    String mealCode = StringUtils.isNotEmpty(mealRequestDetailDTO.getMealCode()) ? mealRequestDetailDTO.getMealCode() : "Standard Meal";
                    logger.info(String.format("Update | Meal | Rloc | %s | Passenger ID | %s | Segment ID | %s | Meal Code | %s",
                            mealRequestDTO.getRloc(),
                            mealRequestDetailDTO.getPassengerId(),
                            updateMealRequestDetailDTO.getSegmentId(),
                            mealCode), true);
                }
            }
        } catch (Exception e) {

        }
        // --- end ---
        return updateMealResponseDTO;
    }

}
