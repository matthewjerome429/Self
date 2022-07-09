package com.cathaypacific.mmbbizrule.cxservice.bookeligibility.service;

import java.util.List;

import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.MealDescriptionResponse;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.MealDetailResponse;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.PNREligibilityResponse;
import com.cathaypacific.mmbbizrule.cxservice.bookeligibility.model.PreSelectedMealPassengerSegment;

public interface MealPreSelectEligibilityService {

    PNREligibilityResponse retrievePnrEligibility(String rloc);

    MealDetailResponse retrieveMealDetailsResponse(List<PreSelectedMealPassengerSegment> passengerSegments);

    MealDescriptionResponse retrieveMealDescriptionResponse(List<PreSelectedMealPassengerSegment> passengerSegments);
}
