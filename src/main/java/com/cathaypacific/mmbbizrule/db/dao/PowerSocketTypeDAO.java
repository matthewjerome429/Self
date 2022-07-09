package com.cathaypacific.mmbbizrule.db.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.cathaypacific.mmbbizrule.db.model.PowerSocketType;
import com.cathaypacific.mmbbizrule.db.model.PowerSocketTypeKey;

public interface PowerSocketTypeDAO extends CrudRepository<PowerSocketType, PowerSocketTypeKey>{
	
	public List<PowerSocketType> findByAppCodeAndCountryCode(String appCode, String countryCode);
}
