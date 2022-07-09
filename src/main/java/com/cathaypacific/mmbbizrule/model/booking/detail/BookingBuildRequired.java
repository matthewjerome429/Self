package com.cathaypacific.mmbbizrule.model.booking.detail;

import java.lang.reflect.Field;

import com.cathaypacific.mbcommon.loging.LogAgent;

/**
 * Please note only boolean value can be added to this bean,
 * and true means get the information.
 * @author zilong.bu
 *
 */
public class BookingBuildRequired {

	private static LogAgent logger = LogAgent.getLogAgent(BookingBuildRequired.class);

	/** the code of application, either MMB or OLCI */
	private String appCode;
	
	private boolean baggageAllowances = true;		//interface: ticketProcessInvokeService.getInfoByEtickets
	
	/**
	 * |- RTFS
	 */
	private boolean rtfs = true;					//interface: flightStatusService.populateFlightDetailTime
	
	/**
	 * interface: OLCI-getJourneyResponseByRLOC
	 * 	|- claimed lounge
	 */
	private boolean cprCheck = true;    			//interface: OLCI-getJourneyResponseByRLOC
	
	/**
	 * Using cached CPR from OLCI by useSession or not, default not.
	 * */
	private boolean useCprSession = false;
	
	/**
	 * When filterMergePNR = ture, OLCI will return the mergedRlocs, and will do not accept/cancel the merged rlocs automatically
	 * */
	private boolean filerMergePNR = true;
	
	/**
	 * |- OperateInfoAndStops
	 * 		|- passenagerContactInfo
	 * 		|- memberAward
	 */
	//affect MemberAward, RedemptionBookChecking, CanIssueTicketChecking, IncompleteRedemptionBooking
	private boolean operateInfoAndStops = true;		//interface: airFlightInfoService.getAirFlightInfo
	
	private boolean passenagerContactInfo = true;	//interface: memberProfile
	
	private boolean memberAward = true;				//interface: memberAwardService.getMemberAward
	
	/**
	 * interface: nationalityCodeService
	 * 	|- emergencyContactInfo
	 *	|- countryOfResidence
	 * 	|- travelDocument
	 */
	private boolean emergencyContactInfo = true;
	
	private boolean countryOfResidence = true;
	
	private boolean travelDocument = true;
	
	/*==================================== DB ====================================*/
	private boolean mealSelection = true;
	
	private boolean seatSelection = true;
	
	// other type flag
	private boolean summaryPage = false;    // some booking only can display in summary page, so this to control some rule, e.g. group booking
	
	private boolean fqtvHolidayCheck = false;  // member profile
	
	private boolean bookableUpgradeStatusCheck = false; // Bookable Upgrade Status Check
	
	/**
	 * mice booking checking by default is true,
	 * if same api do not want to mice logic please set to false
	 */
	private boolean miceBookingCheck = true;
	
	/**
	 * link booking checking by default is true,
     * if same api do not want to link booking logic please set to false
	 */
	private boolean linkBookingCheck = true;
	
	private String journeyId;
	
	/**
	 * need to call pax com to retrieve pre-selected meal
	 * we will not call pax com for preselected
	 */
	private boolean preSelectedMeal = false;
	
	public boolean baggageAllowances() {
		return baggageAllowances;
	}

	public void setBaggageAllowances(boolean baggageAllowances) {
		this.baggageAllowances = baggageAllowances;
	}
	
	public boolean rtfs() {
		return rtfs;
	}

	/**
	 * cprCheck was dependency on RTFS, if RTFS = false, then cprCheck = false;
	 */
	public void setRtfs(boolean rtfs) {
		if(!rtfs) {
			this.setCprCheck(false);
		}
		this.rtfs = rtfs;
	}

	public boolean isCprCheck() {
		return cprCheck;
	}

	public void setCprCheck(boolean cprCheck) {
		this.cprCheck = cprCheck;
	}

	public boolean passenagerContactInfo() {
		return passenagerContactInfo;
	}

	public void setPassenagerContactInfo(boolean passenagerContactInfo) {
		this.passenagerContactInfo = passenagerContactInfo;
	}

	public boolean emergencyContactInfo() {
		return emergencyContactInfo;
	}

	public void setEmergencyContactInfo(boolean emergencyContactInfo) {
		this.emergencyContactInfo = emergencyContactInfo;
	}

	public boolean countryOfResidence() {
		return countryOfResidence;
	}

	public void setCountryOfResidence(boolean countryOfResidence) {
		this.countryOfResidence = countryOfResidence;
	}

	public boolean travelDocument() {
		return travelDocument;
	}

	public void setTravelDocument(boolean travelDocument) {
		this.travelDocument = travelDocument;
	}

	public boolean operateInfoAndStops() {
		return this.operateInfoAndStops;
	}
	
	/**
	 * passenagerContactInfo was dependency on operateInfoAndStops,
	 * if operateInfoAndStops = false, then passenagerContactInfo = false & memberAward = false;
	 */
	public void setOperateInfoAndStops(boolean operateInfoAndStops) {
		if(!operateInfoAndStops) {
			this.setPassenagerContactInfo(false);
			this.setMemberAward(false);
		}
		this.operateInfoAndStops = operateInfoAndStops;
	}

	public boolean memberAward() {
		return memberAward;
	}

	public void setMemberAward(boolean memberAward) {
		this.memberAward = memberAward;
	}

	public boolean mealSelection() {
		return mealSelection;
	}

	public void setMealSelection(boolean mealSelection) {
		this.mealSelection = mealSelection;
	}

	public boolean seatSelection() {
		return seatSelection;
	}

	public void setSeatSelection(boolean seatSelection) {
		this.seatSelection = seatSelection;
	}

	public boolean isSummaryPage() {
		return summaryPage;
	}

	public void setSummaryPage(boolean summaryPage) {
		this.summaryPage = summaryPage;
	}

	public boolean requireFqtvHolidayCheck() {
		return fqtvHolidayCheck;
	}

	public void setFqtvHolidayCheck(boolean fqtvHolidayCheck) {
		this.fqtvHolidayCheck = fqtvHolidayCheck;
	}
	
	public boolean isBookableUpgradeStatusCheck() {
		return bookableUpgradeStatusCheck;
	}

	public void setBookableUpgradeStatusCheck(boolean bookableUpgradeStatusCheck) {
		this.bookableUpgradeStatusCheck = bookableUpgradeStatusCheck;
	}
	
	public boolean isUseCprSession() {
		return useCprSession;
	}

	public void setUseCprSession(boolean useCprSession) {
		this.useCprSession = useCprSession;
	}

	public boolean isFilerMergePNR() {
		return filerMergePNR;
	}

	public void setFilerMergePNR(boolean filerMergePNR) {
		this.filerMergePNR = filerMergePNR;
	}
	
    public boolean isMiceBookingCheck() {
        return miceBookingCheck;
    }

    public void setMiceBookingCheck(boolean miceBookingCheck) {
        this.miceBookingCheck = miceBookingCheck;
    }

    public boolean isLinkBookingCheck() {
        return linkBookingCheck;
    }

    public void setLinkBookingCheck(boolean linkBookingCheck) {
        this.linkBookingCheck = linkBookingCheck;
    }
    
    public String getJourneyId() {
		return journeyId;
	}

	public void setJourneyId(String journeyId) {
		this.journeyId = journeyId;
	}
	

	public boolean isPreSelectedMeal() {
        return preSelectedMeal;
    }

    public void setPreSelectedMeal(boolean preSelectedMeal) {
        this.preSelectedMeal = preSelectedMeal;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (baggageAllowances ? 1231 : 1237);
        result = prime * result + (cprCheck ? 1231 : 1237);
        result = prime * result + (useCprSession ? 1231 : 1237);
        result = prime * result + (filerMergePNR ? 1231 : 1237);
        result = prime * result + (countryOfResidence ? 1231 : 1237);
        result = prime * result + (emergencyContactInfo ? 1231 : 1237);
        result = prime * result + (mealSelection ? 1231 : 1237);
        result = prime * result + (memberAward ? 1231 : 1237);
        result = prime * result + (operateInfoAndStops ? 1231 : 1237);
        result = prime * result + (passenagerContactInfo ? 1231 : 1237);
        result = prime * result + (rtfs ? 1231 : 1237);
        result = prime * result + (seatSelection ? 1231 : 1237);
        result = prime * result + (summaryPage ? 1231 : 1237);
        result = prime * result + (travelDocument ? 1231 : 1237);
        result = prime * result + (miceBookingCheck ? 1231 : 1237);
        result = prime * result + (linkBookingCheck ? 1231 : 1237);
        result = prime * result + (preSelectedMeal ? 1231 : 1237);
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BookingBuildRequired other = (BookingBuildRequired) obj;
        if (baggageAllowances != other.baggageAllowances)
            return false;
        if (cprCheck != other.cprCheck)
            return false;
        if (useCprSession != other.useCprSession)
            return false;
        if (filerMergePNR != other.filerMergePNR)
            return false;
        if (countryOfResidence != other.countryOfResidence)
            return false;
        if (emergencyContactInfo != other.emergencyContactInfo)
            return false;
        if (mealSelection != other.mealSelection)
            return false;
        if (memberAward != other.memberAward)
            return false;
        if (operateInfoAndStops != other.operateInfoAndStops)
            return false;
        if (passenagerContactInfo != other.passenagerContactInfo)
            return false;
        if (rtfs != other.rtfs)
            return false;
        if (seatSelection != other.seatSelection)
            return false;
        if (summaryPage != other.summaryPage)
            return false;
        if (travelDocument != other.travelDocument)
            return false;
        if (miceBookingCheck != other.miceBookingCheck)
            return false;
        if (linkBookingCheck != other.linkBookingCheck)
            return false;
        if (preSelectedMeal != other.preSelectedMeal)
            return false;
        
        return true;
    }
	
}