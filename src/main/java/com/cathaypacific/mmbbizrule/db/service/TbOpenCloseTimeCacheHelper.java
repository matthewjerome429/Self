package com.cathaypacific.mmbbizrule.db.service;

import java.util.Date;

import com.cathaypacific.olciconsumer.model.response.db.OpenCloseTime;

public interface TbOpenCloseTimeCacheHelper {

	public OpenCloseTime findByOriginAndAirlineCodeAndAppCodeAndPaxType(
		String origin, String airlineCode, String appCode, String paxType, Date departDate
	);
}
