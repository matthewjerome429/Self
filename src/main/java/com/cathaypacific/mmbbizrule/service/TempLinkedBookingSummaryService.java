package com.cathaypacific.mmbbizrule.service;

import java.util.List;
import com.cathaypacific.mmbbizrule.model.booking.summary.FlightBookingSummaryConvertBean;

public interface TempLinkedBookingSummaryService {

	/**
	 * get Temp Linked booking list
	 * @param mmbToken
	 * @return
	 */
	public List<FlightBookingSummaryConvertBean> getTempLinkedABookingList(String mmbToken);
}
