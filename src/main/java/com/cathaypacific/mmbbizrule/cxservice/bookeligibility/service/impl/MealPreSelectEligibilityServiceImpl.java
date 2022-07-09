package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.service.impl;

import java.io.StringWriter;
import java.util.List;

import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.aop.tokenlevelcache.TokenLevelCacheable;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.loging.LogPerformance;
import com.cathaypacific.mbcommon.token.TokenCacheKeyEnum;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mbcommon.utils.MMBUtil;
import com.cathaypacific.mmbbizrule.config.BookEligibilityConfig;
import com.cathaypacific.mmbbizrule.constant.OneAConstants;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.MealDescriptionRequest;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.MealDescriptionRequestInfo;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.MealDescriptionResponse;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.MealDetailRequestInfo;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.MealDetailResponse;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.MealDetailsRequest;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.PNREligibilityRequest;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.PNREligibilityResponse;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.PreSelectedMealPassengerSegment;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.service.MealPreSelectEligibilityService;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.util.MarshallerFactory;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.service.PnrInvokeService;
import com.cathaypacific.mmbbizrule.util.BookEligibilityHttpClientService;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;
import com.google.gson.Gson;

@Service
public class MealPreSelectEligibilityServiceImpl implements MealPreSelectEligibilityService {

    private static LogAgent logger = LogAgent.getLogAgent(MealPreSelectEligibilityServiceImpl.class);
    @Autowired
    private PnrInvokeService pnrInvokeService;
    @Autowired
    private BookEligibilityHttpClientService bookEligibilityHttpClientService;

    @Autowired
    private BookEligibilityConfig bookEligibilityConfig;

    private static final Gson gson = new Gson();

    @LogPerformance(message = "Time required to retirieve Pnr-Eligibility")
    @TokenLevelCacheable(name = TokenCacheKeyEnum.PRESELECTEDMEAL_ELIGIBILITY)
    @Override
    public PNREligibilityResponse retrievePnrEligibility(String rloc) {
        String json = "";
        PNRReply pnrReply = getPNRReplyStr(rloc);
        try {
            if (null != pnrReply) {
                PNREligibilityRequest request = new PNREligibilityRequest();
                StringWriter sw = new StringWriter();
                Jaxb2Marshaller marshaller = MarshallerFactory.getMarshaller(PNRReply.class);
                marshaller.marshal(pnrReply, new StreamResult(sw));
                request.setPnrReply(sw.toString());
                request.setJobId(MMBUtil.getCurrentMMBToken());
                json = bookEligibilityHttpClientService
                        .postPnrEligibilityJson(bookEligibilityConfig.getPnrEligibilityUrl(), request);
            }
        } catch (Exception e) {
            logger.error("Retirieve Pnr-Eligibility fail", e);
            return null;
        }
        PNREligibilityResponse response = null;
        response = gson.fromJson(json, PNREligibilityResponse.class);
        return response;
    }

    private PNRReply getPNRReplyStr(String rloc) {
        PNRReply pnrReplay = null;
        try {
            pnrReplay = pnrInvokeService.retrievePnrReplyByOneARloc(rloc);
        } catch (Exception e) {
            logger.error("Retrieve pnrReply fail", e);
        }
        return pnrReplay;
    }

    @LogPerformance(message = "Time required to retirieve Meal Details")
    @TokenLevelCacheable(name = TokenCacheKeyEnum.PRESELECTEDMEAL_DETAIL)
    @Override
    public MealDetailResponse retrieveMealDetailsResponse(List<PreSelectedMealPassengerSegment> passengerSegments) {
        String json = "";
        try {
            if (!CollectionUtils.isEmpty(passengerSegments)) {
                json = bookEligibilityHttpClientService.postMealDetailsJson(bookEligibilityConfig.getMealdetailsUrl(),
                        gson.toJson(buildMealDetailsReq(passengerSegments)));
            }
        } catch (Exception e) {
            logger.error("Retirieve Meal Details fail", e);
            return null;
        }
        MealDetailResponse response = null;
        response = gson.fromJson(json, MealDetailResponse.class);
        return response;
    }

    private MealDetailsRequest buildMealDetailsReq(List<PreSelectedMealPassengerSegment> passengerSegments) {
        MealDetailsRequest mealDetailsRequest = new MealDetailsRequest();
        mealDetailsRequest.setJobId(MMBUtil.getCurrentMMBToken());
        passengerSegments.stream().forEach(ps -> {
            // calling the APIs only when there is upcoming sector operated by CX/KA
            if (!ps.isFlown() && !StringUtils.isEmpty(ps.getCarrierCode())
                    && (OneAConstants.COMPANY_CX.equals(ps.getCarrierCode())
                            || OneAConstants.COMPANY_KA.equals(ps.getCarrierCode()))) {
                MealDetailRequestInfo info = new MealDetailRequestInfo();
                info.setBookingClass(ps.getCabinClass());
                info.setCarrierCode(ps.getCarrierCode());
                info.setDestination(ps.getDestination());
                info.setFlightDate(DateUtil.convertDateFormat(ps.getFlightDate(), DateUtil.DATE_PATTERN_YYYY_MM_DD,
                        DateUtil.DATE_PATTERN_DDMMYYYY));
                info.setFlightNumber(ps.getFlightNumber());
                info.setOrigin(ps.getOrigin());
                mealDetailsRequest.addInfo(info);
            }
        });
        return mealDetailsRequest;
    }

    @LogPerformance(message = "Time required to retirieve Meal Description")
    @TokenLevelCacheable(name = TokenCacheKeyEnum.PRESELECTEDMEAL_DESCRIPTION)
    @Override
    public MealDescriptionResponse retrieveMealDescriptionResponse(
            List<PreSelectedMealPassengerSegment> passengerSegments) {
        String json = "";
        try {
            if (!CollectionUtils.isEmpty(passengerSegments)) {
                json = bookEligibilityHttpClientService.postMealDescriptionJson(
                        bookEligibilityConfig.getMealdescriptionUrl(),
                        gson.toJson(buildMealDescriptionReq(passengerSegments)));
            }
        } catch (Exception e) {
            logger.error("Retirieve Meal Description fail", e);
            return null;
        }
        MealDescriptionResponse response = null;
        response = gson.fromJson(json, MealDescriptionResponse.class);
        return response;
    }

    private MealDescriptionRequest buildMealDescriptionReq(List<PreSelectedMealPassengerSegment> passengerSegments) {
        MealDescriptionRequest mealDescriptionRequest = new MealDescriptionRequest();
        mealDescriptionRequest.setJobId(MMBUtil.getCurrentMMBToken());
        passengerSegments.stream().forEach(ps -> {
            // calling the APIs only when there is upcoming sector operated by CX/KA
            if (!ps.isFlown() && !StringUtils.isEmpty(ps.getCarrierCode())
                    && (OneAConstants.COMPANY_CX.equals(ps.getCarrierCode())
                            || OneAConstants.COMPANY_KA.equals(ps.getCarrierCode()))) {
                MealDescriptionRequestInfo info = new MealDescriptionRequestInfo();
                info.setCarrierCode(ps.getCarrierCode());
                info.setDestination(ps.getDestination());
                info.setDishPscode(ps.getMeal().getMealCode().substring(3, 6));
                info.setFlightDate(DateUtil.convertDateFormat(ps.getFlightDate(), DateUtil.DATE_PATTERN_YYYY_MM_DD,
                        DateUtil.DATE_PATTERN_DDMMYYYY));
                info.setFlightNumber(ps.getFlightNumber());
                info.setOrigin(ps.getOrigin());
                info.setServicePscode(ps.getMeal().getMealCode().substring(0, 3));
                mealDescriptionRequest.addInfo(info);
            }
        });
        return mealDescriptionRequest;
    }

}
