package com.cathaypacific.mmbbizrule.cxservice.changeflight.service;

import com.cathaypacific.mbcommon.exception.ExpectedException;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.ChangeFlightEligibleResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.changeflight.model.ReminderListResponseDTO;
import com.cathaypacific.oneaconsumer.model.response.pnracc_16_1_1a.PNRReply;

public interface ChangeFlightEligibleService {

	/**
	 * call change Flight Eligible By Pnr
	 * @param pnrReplay
	 * @return
	 * @throws ExpectedException
	 * @throws UnexpectedException
	 */
	ChangeFlightEligibleResponseDTO changeFlightEligibleByPnr(PNRReply pnrReplay) throws ExpectedException, UnexpectedException;
    /**
     * call reminder List service By Pnr
     * @param pnrReplay
     * @return
     * @throws UnexpectedException
     */
	ReminderListResponseDTO reminderListResponseByPnr(PNRReply pnrReplay) throws UnexpectedException;

}
