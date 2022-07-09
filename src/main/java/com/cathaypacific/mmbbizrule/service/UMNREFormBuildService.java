package com.cathaypacific.mmbbizrule.service;

import java.text.ParseException;

import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.response.umnreformjourney.UMNREFormResponseDTO;
import com.cathaypacific.mmbbizrule.oneaservice.pnr.model.RetrievePnrBooking;

public interface UMNREFormBuildService {

	public UMNREFormResponseDTO buildUMNREForm(RetrievePnrBooking booking) throws ParseException;
	
	public boolean hasEFormRemark(String paxId, RetrievePnrBooking pnrBooking);
}
