package com.cathaypacific.mmbbizrule.db.dao;

import org.springframework.data.repository.CrudRepository;

import com.cathaypacific.mmbbizrule.db.model.PowerVoltageFrequency;
import com.cathaypacific.mmbbizrule.db.model.PowerVoltageFrequencyKey;

public interface PowerVoltageFrequencyDAO extends CrudRepository<PowerVoltageFrequency, PowerVoltageFrequencyKey>{

	public PowerVoltageFrequency findByAppCodeAndCountryCode(String appCode, String countryCode);
}
