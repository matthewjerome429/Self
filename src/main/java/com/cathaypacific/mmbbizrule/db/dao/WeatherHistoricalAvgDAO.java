package com.cathaypacific.mmbbizrule.db.dao;

import org.springframework.data.repository.CrudRepository;

import com.cathaypacific.mmbbizrule.db.model.WeatherHistoricalAvg;
import com.cathaypacific.mmbbizrule.db.model.WeatherHistoricalAvgKey;

public interface WeatherHistoricalAvgDAO extends CrudRepository<WeatherHistoricalAvg, WeatherHistoricalAvgKey>{

	public WeatherHistoricalAvg findByAppCodeAndPortCode(String appCode, String portCode);
}
