package com.cathaypacific.mmbbizrule.db.dao;

import org.springframework.data.repository.CrudRepository;

import com.cathaypacific.mmbbizrule.db.model.CountryCurrencyMap;
import com.cathaypacific.mmbbizrule.db.model.CountryCurrencyMapKey;

public interface CountryCurrencyDAO extends CrudRepository<CountryCurrencyMap, CountryCurrencyMapKey>{

	public CountryCurrencyMap findByAppCodeAndCountryCode(String appCode, String countryCode);
}
