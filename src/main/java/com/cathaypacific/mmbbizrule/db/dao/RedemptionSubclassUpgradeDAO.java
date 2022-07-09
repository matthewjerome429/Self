package com.cathaypacific.mmbbizrule.db.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.cathaypacific.mmbbizrule.db.model.RedemptionSubclassUpgrade;
import com.cathaypacific.mmbbizrule.db.model.RedemptionSubclassUpgradeKey;

public interface RedemptionSubclassUpgradeDAO extends CrudRepository<RedemptionSubclassUpgrade, RedemptionSubclassUpgradeKey>{
	
	@Query(nativeQuery=true, value="select subclass FROM tb_redemption_subclass_upgrade as a "
			+ "where a.app_code = :appCode AND a.upgrade = :upgrade")
	public List<String> getSubclassList(@Param("appCode") String appCode, @Param("upgrade") boolean upgrade);	
}
