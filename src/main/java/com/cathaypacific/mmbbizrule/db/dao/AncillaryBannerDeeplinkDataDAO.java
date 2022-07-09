package com.cathaypacific.mmbbizrule.db.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cathaypacific.mmbbizrule.db.model.AncillaryBannerDeeplinkData;
import com.cathaypacific.mmbbizrule.db.model.AncillaryBannerDeeplinkDataKey;
public interface AncillaryBannerDeeplinkDataDAO extends JpaRepository<AncillaryBannerDeeplinkData, AncillaryBannerDeeplinkDataKey> {

	public List<AncillaryBannerDeeplinkData> findByAppCode(String appCode);

}
