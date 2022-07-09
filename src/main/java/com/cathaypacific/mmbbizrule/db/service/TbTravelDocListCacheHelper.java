package com.cathaypacific.mmbbizrule.db.service;

import java.util.List;

import com.cathaypacific.olciconsumer.model.response.db.TravelDocList;


public interface TbTravelDocListCacheHelper {

	public List<String> findTravelDocCodeByVersion(int travelDocVersion, List<String> travelDocTypes);

	public List<Integer> findDocVersionByCodeAndType(String travelDocCode, String travelDocType);

	public List<String> findVersionByTypeGroupByCode(String travelDocType);

	public List<TravelDocList> findAll();
}
