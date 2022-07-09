package com.cathaypacific.mmbbizrule.db.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cathaypacific.mmbbizrule.db.model.RedemptionTPOSCheck;
import com.cathaypacific.mmbbizrule.db.model.RedemptionTPOSCheckKey;

public interface RedemptionTPOSCheckDAO extends CrudRepository<RedemptionTPOSCheck,RedemptionTPOSCheckKey>{
	
	public List<RedemptionTPOSCheck> findByAppCode(String appCode);
	
	public List<RedemptionTPOSCheck> findByAppCodeAndType(String appCode, String type);
}
