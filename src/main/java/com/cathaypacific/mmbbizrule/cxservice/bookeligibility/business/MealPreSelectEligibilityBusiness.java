package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.business;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mbcommon.loging.LogPerformance;
import com.cathaypacific.mbcommon.utils.DateUtil;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.Courses;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.FlightDetails;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.MealDescription;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.MealDescriptionResponse;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.MealDetailResponse;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.MealDetails;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.PNREligibilityResponse;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.PreSelectedMealPassengerSegment;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.Services;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.service.MealPreSelectEligibilityService;
import com.cathaypacific.mmbbizrule.model.booking.detail.Booking;
import com.cathaypacific.mmbbizrule.model.booking.detail.DepartureArrivalTime;
import com.cathaypacific.mmbbizrule.model.booking.detail.MealDetail;
import com.cathaypacific.mmbbizrule.model.booking.detail.MealSelection;
import com.cathaypacific.mmbbizrule.model.booking.detail.Passenger;
import com.cathaypacific.mmbbizrule.model.booking.detail.PassengerSegment;
import com.cathaypacific.mmbbizrule.model.booking.detail.PreSelectMeal;
import com.cathaypacific.mmbbizrule.model.booking.detail.Segment;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.PnrResponseParser;

@Service
public class MealPreSelectEligibilityBusiness {
    private static LogAgent logger = LogAgent.getLogAgent(MealPreSelectEligibilityBusiness.class);

    @Autowired
    private MealPreSelectEligibilityService bookEligibilityService;

    @Async
    @LogPerformance(message = "Time required to build asyncGetPreSelectedMealInfo.")
    public Future<List<PreSelectedMealPassengerSegment>> asyncGetPreSelectedMealInfo(Booking booking) {
        return new AsyncResult<>(getPNREligibilityRes(booking));
    }

    public List<PreSelectedMealPassengerSegment> getPNREligibilityRes(Booking booking) {
        // 1.call pax com to retrieve eligibilityRes
        PNREligibilityResponse response = bookEligibilityService.retrievePnrEligibility(booking.getOneARloc());

        // 2. parse reponse to passenger segment
        List<PreSelectedMealPassengerSegment> ps = generatePassengerSegmentAccordingToResponse(response, booking);
        if (CollectionUtils.isEmpty(ps)) {
            logger.info("Not find any booking passenger from eligibility reponse.");
            return ps;
        }
        // 3. collect if eligible--> true (collection A) to call full menu
        List<PreSelectedMealPassengerSegment> eligiblePsList = filterEligiblePassengerSegment(ps);
        if (!CollectionUtils.isEmpty(eligiblePsList)) {
            MealDetailResponse mealDetailResponse = bookEligibilityService.retrieveMealDetailsResponse(eligiblePsList);
            // 4. in collection A if has pnr pre meal, prefill 3 response
            prefillFullMenuAndMealForFlight(eligiblePsList, mealDetailResponse);
        }
        // 5. collect if ineligible--> false and has pnr pre meal(collection B) to call
        List<PreSelectedMealPassengerSegment> ineligiblePsList = filterInEligiblePassengerSegment(ps);
        if (!CollectionUtils.isEmpty(ineligiblePsList)) {
            MealDescriptionResponse mealDescriptionResponse = bookEligibilityService
                    .retrieveMealDescriptionResponse(ineligiblePsList);
            // 6. in collection B prefill 5 response
            prefillSingleMenuAndMealForFlight(ineligiblePsList, mealDescriptionResponse);
        }
        return ps;
    }

    /**
     * generate passengersegment according to response
     * 
     * @param response
     * @param booking
     * @return
     */
    private List<PreSelectedMealPassengerSegment> generatePassengerSegmentAccordingToResponse(
            PNREligibilityResponse response, Booking booking) {
        List<PreSelectedMealPassengerSegment> psList = new ArrayList<PreSelectedMealPassengerSegment>();
        if (null != response && !CollectionUtils.isEmpty(response.getPassengers())) {
            for (com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.Passenger passenger : response
                    .getPassengers()) {
                if (null != passenger && !CollectionUtils.isEmpty(passenger.getSegments())) {
                    Passenger bookingPassenger = booking.getPassengers().stream()
                            .filter(bp -> isPreselectedMealPassengerMatched(passenger, bp))
                            .findFirst().orElse(null);
                    if (null != bookingPassenger) {
                        String bpid = bookingPassenger.getPassengerId();
                        for (com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.Segment flight : passenger
                                .getSegments()) {
                            if (null != flight) {
                                Segment bookingFlight = booking.getSegments().stream()
                                        .filter(bf -> (isPreselectedMealFlightMatched(bf,flight)))
                                        .findFirst().orElse(null);
                                if (null != bookingFlight) {
                                    PreSelectedMealPassengerSegment ps = new PreSelectedMealPassengerSegment();
                                    ps.setPassengerId(bpid);
                                    ps.setSegmentId(bookingFlight.getSegmentID());
                                    ps.setCabinClass(bookingFlight.getCabinClass());
                                    ps.setCarrierCode(bookingFlight.getOperateCompany());
                                    ps.setDestination(flight.getDestination());
                                    ps.setFlightDate(flight.getFlightDate());
                                    ps.setStdGmt(flight.getStdGmt());
                                    ps.setStdLocal(flight.getStdLocal());
                                    ps.setFlightNumber(bookingFlight.getOperateSegmentNumber());
                                    ps.setOrigin(flight.getOrigin());
                                    ps.setFlown(bookingFlight.isFlown());
                                    // prefill eligibility to PreSelectedMealPassengerSegment
                                    MealSelection mealSelection = new MealSelection();
                                    mealSelection.setPreSelectMealEligibility(false);
                                    if (null != flight.getEligibilities()
                                            && null != flight.getEligibilities().getMealPreSelect()
                                            && 1 == flight.getEligibilities().getMealPreSelect()) {
                                        mealSelection.setPreSelectMealEligibility(true);
                                    }
                                    ps.setMealSelection(mealSelection);
                                    // prefill pnr meal
                                    prefillPnrMeal(ps, booking);
                                    psList.add(ps);
                                }
                            }
                        }
                    }
                }
            }
        }
        return psList;
    }

    /** check if the preselected passenger match booking passenger according to paxtype and pt
     * @param passenger
     * @param bp
     * @return
     */
    private boolean isPreselectedMealPassengerMatched(
            com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.Passenger preSelectedpassenger,
            Passenger bookingPassenger) {
        String preSelectedPassengerId = preSelectedpassenger.getPT();
        if (StringUtils.equalsIgnoreCase(PnrResponseParser.PASSENGER_TYPE_INF, preSelectedpassenger.getPassengerType())) {
            preSelectedPassengerId = preSelectedpassenger.getPT() + PnrResponseParser.PASSENGER_INFANT_ID_SUFFIX;
        }
        return StringUtils.equalsIgnoreCase(preSelectedPassengerId, bookingPassenger.getPassengerId()) && StringUtils
                .equalsIgnoreCase(preSelectedpassenger.getPassengerType(), bookingPassenger.getPassengerType());
    }
    
    /**
     * map flight info according to ST
     * @param bf
     * @param flight
     * @return
     */
    private boolean isPreselectedMealFlightMatched(Segment bf,
            com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.Segment flight) {
        return StringUtils.equals(bf.getSegmentID(), flight.getST());
    }

    /**
     * prefill pnr meal to PreSelectedMealPassengerSegment
     * 
     * @param ps
     * @param booking
     */
    private void prefillPnrMeal(PreSelectedMealPassengerSegment ps, Booking booking) {
        if (null != booking && !CollectionUtils.isEmpty(booking.getPassengerSegments())) {
            PassengerSegment segment = booking.getPassengerSegments().stream()
                    .filter(sg -> (sg.getPassengerId().equals(ps.getPassengerId())
                            && sg.getSegmentId().equals(ps.getSegmentId()) && null != sg.getMeal() && sg.getMeal().isPreSelectedMeal()))
                    .findFirst().orElse(null);
            if (null != segment && null != segment.getMeal()) {
                MealDetail mealDetail = new MealDetail();
                mealDetail.setCompanyId(segment.getMeal().getCompanyId());
                mealDetail.setMealCode(segment.getMeal().getMealCode());
                mealDetail.setQuantity(segment.getMeal().getQuantity());
                mealDetail.setStatus(segment.getMeal().getStatus());
                mealDetail.setPreSelectedMeal(segment.getMeal().isPreSelectedMeal());
                ps.setMeal(mealDetail);
            }
        }
    }

    /**
     * filter out meal preselect eligible--> true
     * 
     * @param passengerSegments
     * @return
     */
    private List<PreSelectedMealPassengerSegment> filterEligiblePassengerSegment(
            List<PreSelectedMealPassengerSegment> passengerSegments) {
        List<PreSelectedMealPassengerSegment> psList = null;
        if (!CollectionUtils.isEmpty(passengerSegments)) {
            psList = passengerSegments.stream()
                    .filter(ps -> (null != ps.getMealSelection() && ps.getMealSelection().isPreSelectMealEligibility()))
                    .collect(Collectors.toList());
        }
        return psList;
    }

    /**
     * filter out meal preselect ineligible--> false
     * 
     * @param passengerSegments
     * @return
     */
    private List<PreSelectedMealPassengerSegment> filterInEligiblePassengerSegment(
            List<PreSelectedMealPassengerSegment> passengerSegments) {
        List<PreSelectedMealPassengerSegment> psList = null;
        if (!CollectionUtils.isEmpty(passengerSegments)) {
            psList = passengerSegments.stream()
                    .filter(ps -> (null != ps.getMealSelection() && !ps.getMealSelection().isPreSelectMealEligibility()
                            && null != ps.getMeal() && ps.getMeal().isPreSelectedMeal()))
                    .collect(Collectors.toList());
        }
        return psList;
    }

    /**
     * prefill menu and meal
     * 
     * @param psList
     */
    private void prefillFullMenuAndMealForFlight(List<PreSelectedMealPassengerSegment> psList,
            MealDetailResponse mealDetailResponse) {
        if (!CollectionUtils.isEmpty(psList)) {
            List<PreSelectedMealPassengerSegment> psEligibleMenu = psList.stream()
                    .filter(ps -> null != ps && null != ps.getMealSelection()
                            && BooleanUtils.isTrue(ps.getMealSelection().isPreSelectMealEligibility()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(psEligibleMenu)) {
                if (null != mealDetailResponse && !CollectionUtils.isEmpty(mealDetailResponse.getMealDetails())) {
                    for (PreSelectedMealPassengerSegment preSelectedMealPassengerSegment : psEligibleMenu) {
                        com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.MealDetail mealDetail = mealDetailResponse
                                .getMealDetails().stream()
                                .filter(md -> (md.getCarrierCode()
                                        .equals(preSelectedMealPassengerSegment.getCarrierCode())
                                        && md.getFlightNumber()
                                                .equals(preSelectedMealPassengerSegment.getFlightNumber())
                                        && StringUtils.equals(DateUtil.convertDateFormat(
                                                preSelectedMealPassengerSegment.getFlightDate(),
                                                DateUtil.DATE_PATTERN_YYYY_MM_DD, DateUtil.DATE_PATTERN_DDMMYYYY),
                                                md.getFlightDate())))
                                .findFirst().orElse(null);
                        if (null != mealDetail) {
                            prefillPreSelectMealList(preSelectedMealPassengerSegment, mealDetail);
                            prefillMeal(preSelectedMealPassengerSegment);
                        }
                    }
                }
            }

        }}

    /**
     * prefill preSelectMealList
     * 
     * @param preSelectedMealPassengerSegment
     * @param mealDetail
     */
    private void prefillPreSelectMealList(PreSelectedMealPassengerSegment preSelectedMealPassengerSegment,
            com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.MealDetail mealDetail) {
        FlightDetails flightDetails = retrieveFlightDetail(preSelectedMealPassengerSegment, mealDetail);
        if(null != flightDetails ) {
            List<PreSelectMeal> preSelectMeals = new ArrayList<>();
            if (!CollectionUtils.isEmpty(flightDetails.getServices())) {
                for(Services service : flightDetails.getServices()) {
                    for (Courses courses : service.getCourses()) {
                        if (!CollectionUtils.isEmpty(courses.getMealDetails())) {
                            for (MealDetails details : courses.getMealDetails()) {
                                if (null != details) {
                                    PreSelectMeal preSelectMeal = new PreSelectMeal();
                                    preSelectMeal.setDishName(details.getDishName());
                                    preSelectMeal.setOffMenu(details.getOffMenu());
                                    preSelectMeal.setTag(details.getTag());
                                    preSelectMeal.setType(service.getPsCode() + details.getPsCode());
                                    preSelectMeals.add(preSelectMeal);
                                }
                            }
                        }
                    } 
                }
                
            }
            preSelectedMealPassengerSegment.getMealSelection().setPreSelectMeals(preSelectMeals);
        }
    }

    /**
     * prefill preSelectMeal to meal
     * 
     * @param preSelectedMealPassengerSegment
     */
    private void prefillMeal(PreSelectedMealPassengerSegment preSelectedMealPassengerSegment) {
        if (null != preSelectedMealPassengerSegment.getMealSelection()
                && !CollectionUtils.isEmpty(preSelectedMealPassengerSegment.getMealSelection().getPreSelectMeals())) {
            for (PreSelectMeal preSelectMeal : preSelectedMealPassengerSegment.getMealSelection().getPreSelectMeals()) {
                if (null != preSelectedMealPassengerSegment.getMeal() && preSelectedMealPassengerSegment.getMeal().isPreSelectedMeal()
                        && preSelectMeal.getType().equals(preSelectedMealPassengerSegment.getMeal().getMealCode())) {
                    preSelectedMealPassengerSegment.getMeal().setDishName(preSelectMeal.getDishName());
                    preSelectedMealPassengerSegment.getMeal().setTag(preSelectMeal.getTag());
                }
            }
        }
    }

    private FlightDetails retrieveFlightDetail(PreSelectedMealPassengerSegment preSelectedMealPassengerSegment,
            com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.MealDetail eligibleMealDetail) {
        FlightDetails res = null;
        if (null != eligibleMealDetail && !CollectionUtils.isEmpty(eligibleMealDetail.getFlightDetails())) {
            res = eligibleMealDetail.getFlightDetails().stream()
                    .filter(fd -> ((fd.getOrigin().equals(preSelectedMealPassengerSegment.getOrigin())
                            || fd.getDestination().equals(preSelectedMealPassengerSegment.getDestination())) && fd.getEligible()))
                    .findFirst().orElse(null);
        }
        return res;
    }

    private void prefillSingleMenuAndMealForFlight(List<PreSelectedMealPassengerSegment> psList,
            MealDescriptionResponse mealDescriptionResponse) {
        if (!CollectionUtils.isEmpty(psList) && null != mealDescriptionResponse
                && !CollectionUtils.isEmpty(mealDescriptionResponse.getMealDescriptions())) {
            for (PreSelectedMealPassengerSegment ps : psList) {
                for (MealDescription mealDescription : mealDescriptionResponse.getMealDescriptions()) {
                    // prefill meal
                    if (ps.getMeal().isPreSelectedMeal() && ps.getMeal().getMealCode()
                            .equals(mealDescription.getServicePscode() + mealDescription.getDishPscode())) {
                        ps.getMeal().setDishName(mealDescription.getDishName());
                        ps.getMeal().setTag(mealDescription.getTag());
                    }
                }
            }
        }
    }

}
