package com.cathaypacific.mmbbizrule.db.service;

import com.cathaypacific.olciconsumer.model.response.db.TravelDocNatCoiMapping;

import java.util.List;

public interface TbTravelDocNatCoiCheckCacheHelper {

	public List<TravelDocNatCoiMapping> findDocCoisByAppCode(String appCode);
}
