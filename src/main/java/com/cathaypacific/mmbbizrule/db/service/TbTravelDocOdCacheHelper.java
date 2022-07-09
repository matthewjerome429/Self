package com.cathaypacific.mmbbizrule.db.service;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

import com.cathaypacific.olciconsumer.model.response.db.TravelDocOD;


public interface TbTravelDocOdCacheHelper {

	public int findTravelDocVersion(String appCode, List<String> origins, List<String> destinations);

	List<TravelDocOD> findByAppCodeInAndStartDateBefore(Collection<String> appCode, Date startDate);
}
