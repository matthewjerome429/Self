package com.cathaypacific.mmbbizrule.oneaservice.pnrsearch.service;

import java.util.List;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.oneaservice.pnrsearch.model.PnrSearchBooking;

public interface PnrSearchService {
	
	public List<PnrSearchBooking> retrieveBookingList(String memberId) throws BusinessBaseException;

}
