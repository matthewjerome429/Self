package com.cathaypacific.mmbbizrule.db.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cathaypacific.mmbbizrule.db.model.TBSsrSkMapping;
import com.cathaypacific.mmbbizrule.db.model.TBSsrSkMappingKey;

public interface TBSsrSkMappingDAO extends CrudRepository<TBSsrSkMapping, TBSsrSkMappingKey> {
	
	public List<TBSsrSkMapping> findByAppCodeAndSeat(String appCode,String seat);
	
	public List<TBSsrSkMapping> findByAppCodeAndSpecialSeat(String appCode,String specialSeatIndicator);
	
	public List<TBSsrSkMapping> findByAppCodeAndUpgradeBid(String appCode,String upgradeBidIndicator);
	
	public List<TBSsrSkMapping> findByAppCodeAndAsrFOC(String appCode,String asrFOC);
	
	public List<TBSsrSkMapping> findByAppCodeAndCompanionAsrFOC(String appCode,String companionAsrFOC);
	
	public List<TBSsrSkMapping> findByAppCodeAndSelectedAsrFOC(String appCode,String selectedAsrFOC);
}