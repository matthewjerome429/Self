package com.cathaypacific.mmbbizrule.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.model.booking.summary.FlightBookingSummaryConvertBean;
import com.cathaypacific.mmbbizrule.model.booking.summary.OjBookingSummaryConvertBean;
import com.cathaypacific.mmbbizrule.model.booking.summary.TempLinkedBooking;
import com.cathaypacific.mmbbizrule.repository.TempLinkedBookingRepository;
import com.cathaypacific.mmbbizrule.service.TempLinkedBookingSummaryService;

@Service
public class TempLinkedBookingSummaryServiceImpl implements TempLinkedBookingSummaryService {

	private static LogAgent logger = LogAgent.getLogAgent(TempLinkedBookingSummaryServiceImpl.class);
	
	@Autowired
	private TempLinkedBookingRepository linkTempBookingRepository;
	
	@Override
	public List<FlightBookingSummaryConvertBean> getTempLinkedABookingList(String mmbToken) {
		List<TempLinkedBooking> linkedBookings = linkTempBookingRepository.getLinkedBookings(mmbToken);
		List<FlightBookingSummaryConvertBean> result= Collections.emptyList();
		if(!CollectionUtils.isEmpty(linkedBookings)){
			result = new ArrayList<>();
			for (TempLinkedBooking tempAssociatedBooking : linkedBookings) {
				OjBookingSummaryConvertBean summaryBean = new OjBookingSummaryConvertBean();
				summaryBean.setRloc(tempAssociatedBooking.getRloc());
				summaryBean.setFamilyName(tempAssociatedBooking.getFamilyName());
				summaryBean.setGivenName(tempAssociatedBooking.getGivenName());
				result.add(summaryBean);
			}
			String rlocs = result.stream().map(FlightBookingSummaryConvertBean::getRloc).collect(Collectors.joining(" "));
			logger.info(String.format("Retrieved temp linked(added) rloc(s) for member:[%s]",rlocs));
		}
		return result;
	}
	 
	
}
