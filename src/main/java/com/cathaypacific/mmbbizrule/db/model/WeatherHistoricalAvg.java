package com.cathaypacific.mmbbizrule.db.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.cathaypacific.mmbbizrule.model.common.destination.WeatherInfoAvg;

@Entity
@IdClass(WeatherHistoricalAvgKey.class)
@Table(name="tb_weather_historical_avg")
public class WeatherHistoricalAvg {
	@Id
	@NotNull
    @Column(name="app_code", length = 5)
	private String appCode;

	@NotNull
    @Column(name="country_name", length = 100)
	private String countryName;
	
	@Id
	@NotNull
    @Column(name="port_code", length = 5)
	private String portCode;
	
	@NotNull
    @Column(name="jan_max_temp", length =5)
	private String janMaxTemp;
	
	@NotNull
    @Column(name="jan_mean_temp", length =5)
	private String janMeanTemp;
	
	@NotNull
    @Column(name="jan_min_temp", length =5)
	private String janMStringemp;
	
	@NotNull
    @Column(name="jan_humidity", length =5)
	private String janHumidity;
	
	@NotNull
    @Column(name="feb_max_temp", length =5)
	private String febMaxTemp;
	
	@NotNull
    @Column(name="feb_mean_temp", length =5)
	private String febMeanTemp;
	
	@NotNull
    @Column(name="feb_min_temp", length =5)
	private String febMStringemp;
	
	@NotNull
    @Column(name="feb_humidity", length =5)
	private String febHumidity;
	
	@NotNull
    @Column(name="mar_max_temp", length =5)
	private String marMaxTemp;
	
	@NotNull
    @Column(name="mar_mean_temp", length =5)
	private String marMeanTemp;
	
	@NotNull
    @Column(name="mar_min_temp", length =5)
	private String marMStringemp;
	
	@NotNull
    @Column(name="mar_humidity", length =5)
	private String marHumidity;
	
	@NotNull
    @Column(name="apr_max_temp", length =5)
	private String aprMaxTemp;
	
	@NotNull
    @Column(name="apr_mean_temp", length =5)
	private String aprMeanTemp;
	
	@NotNull
    @Column(name="apr_min_temp", length =5)
	private String aprMStringemp;
	
	@NotNull
    @Column(name="apr_humidity", length =5)
	private String aprHumidity;
	
	@NotNull
    @Column(name="may_max_temp", length =5)
	private String mayMaxTemp;
	
	@NotNull
    @Column(name="may_mean_temp", length =5)
	private String mayMeanTemp;
	
	@NotNull
    @Column(name="may_min_temp", length =5)
	private String mayMStringemp;
	
	@NotNull
    @Column(name="may_humidity", length =5)
	private String mayHumidity;
	
	@NotNull
    @Column(name="jun_max_temp", length =5)
	private String junMaxTemp;
	
	@NotNull
    @Column(name="jun_mean_temp", length =5)
	private String junMeanTemp;
	
	@NotNull
    @Column(name="jun_min_temp", length =5)
	private String junMStringemp;
	
	@NotNull
    @Column(name="jun_humidity", length =5)
	private String junHumidity;
	
	@NotNull
    @Column(name="jul_max_temp", length =5)
	private String julMaxTemp;
	
	@NotNull
    @Column(name="jul_mean_temp", length =5)
	private String julMeanTemp;
	
	@NotNull
    @Column(name="jul_min_temp", length =5)
	private String julMStringemp;
	
	@NotNull
    @Column(name="jul_humidity", length =5)
	private String julHumidity;
	
	@NotNull
    @Column(name="aug_max_temp", length =5)
	private String augMaxTemp;
	
	@NotNull
    @Column(name="aug_mean_temp", length =5)
	private String augMeanTemp;
	
	@NotNull
    @Column(name="aug_min_temp", length =5)
	private String augMStringemp;
	
	@NotNull
    @Column(name="aug_humidity", length =5)
	private String augHumidity;
	
	@NotNull
    @Column(name="sep_max_temp", length =5)
	private String sepMaxTemp;
	
	@NotNull
    @Column(name="sep_mean_temp", length =5)
	private String sepMeanTemp;
	
	@NotNull
    @Column(name="sep_min_temp", length =5)
	private String sepMStringemp;
	
	@NotNull
    @Column(name="sep_humidity", length =5)
	private String sepHumidity;
	
	@NotNull
    @Column(name="oct_max_temp", length =5)
	private String octMaxTemp;
	
	@NotNull
    @Column(name="oct_mean_temp", length =5)
	private String octMeanTemp;
	
	@NotNull
    @Column(name="oct_min_temp", length =5)
	private String octMStringemp;
	
	@NotNull
    @Column(name="oct_humidity", length =5)
	private String octHumidity;
	
	@NotNull
    @Column(name="nov_max_temp", length =5)
	private String novMaxTemp;
	
	@NotNull
    @Column(name="nov_mean_temp", length =5)
	private String novMeanTemp;
	
	@NotNull
    @Column(name="nov_min_temp", length =5)
	private String novMStringemp;
	
	@NotNull
    @Column(name="nov_humidity", length =5)
	private String novHumidity;
	
	@NotNull
    @Column(name="dec_max_temp", length =5)
	private String decMaxTemp;
	
	@NotNull
    @Column(name="dec_mean_temp", length =5)
	private String decMeanTemp;
	
	@NotNull
    @Column(name="dec_min_temp", length =5)
	private String decMStringemp;
	
	@NotNull
    @Column(name="dec_humidity", length =5)
	private String decHumidity;
	
	@NotNull
	@Column(name="last_update_source", length = 8)
	private String lastUpdateSource;
	
	@NotNull
	@Column(name="last_update_timestamp")
	private Timestamp lastUpdateTimeStamp;

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getPortCode() {
		return portCode;
	}

	public void setPortCode(String portCode) {
		this.portCode = portCode;
	}

	public String getLastUpdateSource() {
		return lastUpdateSource;
	}

	public void setLastUpdateSource(String lastUpdateSource) {
		this.lastUpdateSource = lastUpdateSource;
	}

	public Timestamp getLastUpdateTimeStamp() {
		return lastUpdateTimeStamp;
	}

	public void setLastUpdateTimeStamp(Timestamp lastUpdateTimeStamp) {
		this.lastUpdateTimeStamp = lastUpdateTimeStamp;
	}

	public String getJanMaxTemp() {
		return janMaxTemp;
	}

	public void setJanMaxTemp(String janMaxTemp) {
		this.janMaxTemp = janMaxTemp;
	}

	public String getJanMeanTemp() {
		return janMeanTemp;
	}

	public void setJanMeanTemp(String janMeanTemp) {
		this.janMeanTemp = janMeanTemp;
	}

	public String getJanMStringemp() {
		return janMStringemp;
	}

	public void setJanMStringemp(String janMStringemp) {
		this.janMStringemp = janMStringemp;
	}

	public String getJanHumidity() {
		return janHumidity;
	}

	public void setJanHumidity(String janHumidity) {
		this.janHumidity = janHumidity;
	}

	public String getFebMaxTemp() {
		return febMaxTemp;
	}

	public void setFebMaxTemp(String febMaxTemp) {
		this.febMaxTemp = febMaxTemp;
	}

	public String getFebMeanTemp() {
		return febMeanTemp;
	}

	public void setFebMeanTemp(String febMeanTemp) {
		this.febMeanTemp = febMeanTemp;
	}

	public String getFebMStringemp() {
		return febMStringemp;
	}

	public void setFebMStringemp(String febMStringemp) {
		this.febMStringemp = febMStringemp;
	}

	public String getFebHumidity() {
		return febHumidity;
	}

	public void setFebHumidity(String febHumidity) {
		this.febHumidity = febHumidity;
	}

	public String getMarMaxTemp() {
		return marMaxTemp;
	}

	public void setMarMaxTemp(String marMaxTemp) {
		this.marMaxTemp = marMaxTemp;
	}

	public String getMarMeanTemp() {
		return marMeanTemp;
	}

	public void setMarMeanTemp(String marMeanTemp) {
		this.marMeanTemp = marMeanTemp;
	}

	public String getMarMStringemp() {
		return marMStringemp;
	}

	public void setMarMStringemp(String marMStringemp) {
		this.marMStringemp = marMStringemp;
	}

	public String getMarHumidity() {
		return marHumidity;
	}

	public void setMarHumidity(String marHumidity) {
		this.marHumidity = marHumidity;
	}

	public String getAprMaxTemp() {
		return aprMaxTemp;
	}

	public void setAprMaxTemp(String aprMaxTemp) {
		this.aprMaxTemp = aprMaxTemp;
	}

	public String getAprMeanTemp() {
		return aprMeanTemp;
	}

	public void setAprMeanTemp(String aprMeanTemp) {
		this.aprMeanTemp = aprMeanTemp;
	}

	public String getAprMStringemp() {
		return aprMStringemp;
	}

	public void setAprMStringemp(String aprMStringemp) {
		this.aprMStringemp = aprMStringemp;
	}

	public String getAprHumidity() {
		return aprHumidity;
	}

	public void setAprHumidity(String aprHumidity) {
		this.aprHumidity = aprHumidity;
	}

	public String getMayMaxTemp() {
		return mayMaxTemp;
	}

	public void setMayMaxTemp(String mayMaxTemp) {
		this.mayMaxTemp = mayMaxTemp;
	}

	public String getMayMeanTemp() {
		return mayMeanTemp;
	}

	public void setMayMeanTemp(String mayMeanTemp) {
		this.mayMeanTemp = mayMeanTemp;
	}

	public String getMayMStringemp() {
		return mayMStringemp;
	}

	public void setMayMStringemp(String mayMStringemp) {
		this.mayMStringemp = mayMStringemp;
	}

	public String getMayHumidity() {
		return mayHumidity;
	}

	public void setMayHumidity(String mayHumidity) {
		this.mayHumidity = mayHumidity;
	}

	public String getJunMaxTemp() {
		return junMaxTemp;
	}

	public void setJunMaxTemp(String junMaxTemp) {
		this.junMaxTemp = junMaxTemp;
	}

	public String getJunMeanTemp() {
		return junMeanTemp;
	}

	public void setJunMeanTemp(String junMeanTemp) {
		this.junMeanTemp = junMeanTemp;
	}

	public String getJunMStringemp() {
		return junMStringemp;
	}

	public void setJunMStringemp(String junMStringemp) {
		this.junMStringemp = junMStringemp;
	}

	public String getJunHumidity() {
		return junHumidity;
	}

	public void setJunHumidity(String junHumidity) {
		this.junHumidity = junHumidity;
	}

	public String getJulMaxTemp() {
		return julMaxTemp;
	}

	public void setJulMaxTemp(String julMaxTemp) {
		this.julMaxTemp = julMaxTemp;
	}

	public String getJulMeanTemp() {
		return julMeanTemp;
	}

	public void setJulMeanTemp(String julMeanTemp) {
		this.julMeanTemp = julMeanTemp;
	}

	public String getJulMStringemp() {
		return julMStringemp;
	}

	public void setJulMStringemp(String julMStringemp) {
		this.julMStringemp = julMStringemp;
	}

	public String getJulHumidity() {
		return julHumidity;
	}

	public void setJulHumidity(String julHumidity) {
		this.julHumidity = julHumidity;
	}

	public String getAugMaxTemp() {
		return augMaxTemp;
	}

	public void setAugMaxTemp(String augMaxTemp) {
		this.augMaxTemp = augMaxTemp;
	}

	public String getAugMeanTemp() {
		return augMeanTemp;
	}

	public void setAugMeanTemp(String augMeanTemp) {
		this.augMeanTemp = augMeanTemp;
	}

	public String getAugMStringemp() {
		return augMStringemp;
	}

	public void setAugMStringemp(String augMStringemp) {
		this.augMStringemp = augMStringemp;
	}

	public String getAugHumidity() {
		return augHumidity;
	}

	public void setAugHumidity(String augHumidity) {
		this.augHumidity = augHumidity;
	}

	public String getSepMaxTemp() {
		return sepMaxTemp;
	}

	public void setSepMaxTemp(String sepMaxTemp) {
		this.sepMaxTemp = sepMaxTemp;
	}

	public String getSepMeanTemp() {
		return sepMeanTemp;
	}

	public void setSepMeanTemp(String sepMeanTemp) {
		this.sepMeanTemp = sepMeanTemp;
	}

	public String getSepMStringemp() {
		return sepMStringemp;
	}

	public void setSepMStringemp(String sepMStringemp) {
		this.sepMStringemp = sepMStringemp;
	}

	public String getSepHumidity() {
		return sepHumidity;
	}

	public void setSepHumidity(String sepHumidity) {
		this.sepHumidity = sepHumidity;
	}

	public String getOctMaxTemp() {
		return octMaxTemp;
	}

	public void setOctMaxTemp(String octMaxTemp) {
		this.octMaxTemp = octMaxTemp;
	}

	public String getOctMeanTemp() {
		return octMeanTemp;
	}

	public void setOctMeanTemp(String octMeanTemp) {
		this.octMeanTemp = octMeanTemp;
	}

	public String getOctMStringemp() {
		return octMStringemp;
	}

	public void setOctMStringemp(String octMStringemp) {
		this.octMStringemp = octMStringemp;
	}

	public String getOctHumidity() {
		return octHumidity;
	}

	public void setOctHumidity(String octHumidity) {
		this.octHumidity = octHumidity;
	}

	public String getNovMaxTemp() {
		return novMaxTemp;
	}

	public void setNovMaxTemp(String novMaxTemp) {
		this.novMaxTemp = novMaxTemp;
	}

	public String getNovMeanTemp() {
		return novMeanTemp;
	}

	public void setNovMeanTemp(String novMeanTemp) {
		this.novMeanTemp = novMeanTemp;
	}

	public String getNovMStringemp() {
		return novMStringemp;
	}

	public void setNovMStringemp(String novMStringemp) {
		this.novMStringemp = novMStringemp;
	}

	public String getNovHumidity() {
		return novHumidity;
	}

	public void setNovHumidity(String novHumidity) {
		this.novHumidity = novHumidity;
	}

	public String getDecMaxTemp() {
		return decMaxTemp;
	}

	public void setDecMaxTemp(String decMaxTemp) {
		this.decMaxTemp = decMaxTemp;
	}

	public String getDecMeanTemp() {
		return decMeanTemp;
	}

	public void setDecMeanTemp(String decMeanTemp) {
		this.decMeanTemp = decMeanTemp;
	}

	public String getDecMStringemp() {
		return decMStringemp;
	}

	public void setDecMStringemp(String decMStringemp) {
		this.decMStringemp = decMStringemp;
	}

	public String getDecHumidity() {
		return decHumidity;
	}

	public void setDecHumidity(String decHumidity) {
		this.decHumidity = decHumidity;
	}
	
	
	public WeatherInfoAvg getJanWeatherInfo() {
		WeatherInfoAvg weatherInfoAvg = new WeatherInfoAvg();	
		weatherInfoAvg.setHumidity(getJanHumidity());
		weatherInfoAvg.setMaxTemperature(getJanMaxTemp());
		weatherInfoAvg.setMeanTemperature(getJanMeanTemp());
		weatherInfoAvg.setMinTemperature(getJanMStringemp());	
		return weatherInfoAvg;
	}
	
	public WeatherInfoAvg getFebWeatherInfo() {
		WeatherInfoAvg weatherInfoAvg = new WeatherInfoAvg();	
		weatherInfoAvg.setHumidity(getFebHumidity());
		weatherInfoAvg.setMaxTemperature(getFebMaxTemp());
		weatherInfoAvg.setMeanTemperature(getFebMeanTemp());
		weatherInfoAvg.setMinTemperature(getFebMStringemp());	
		return weatherInfoAvg;
	}
	
	public WeatherInfoAvg getMarWeatherInfo() {
		WeatherInfoAvg weatherInfoAvg = new WeatherInfoAvg();	
		weatherInfoAvg.setHumidity(getMarHumidity());
		weatherInfoAvg.setMaxTemperature(getMarMaxTemp());
		weatherInfoAvg.setMeanTemperature(getMarMeanTemp());
		weatherInfoAvg.setMinTemperature(getMarMStringemp());	
		return weatherInfoAvg;
	}
	
	public WeatherInfoAvg getAprWeatherInfo() {
		WeatherInfoAvg weatherInfoAvg = new WeatherInfoAvg();	
		weatherInfoAvg.setHumidity(getAprHumidity());
		weatherInfoAvg.setMaxTemperature(getAprMaxTemp());
		weatherInfoAvg.setMeanTemperature(getAprMeanTemp());
		weatherInfoAvg.setMinTemperature(getAprMStringemp());	
		return weatherInfoAvg;
	}
	
	public WeatherInfoAvg getMayWeatherInfo() {
		WeatherInfoAvg weatherInfoAvg = new WeatherInfoAvg();	
		weatherInfoAvg.setHumidity(getMayHumidity());
		weatherInfoAvg.setMaxTemperature(getMayMaxTemp());
		weatherInfoAvg.setMeanTemperature(getMayMeanTemp());
		weatherInfoAvg.setMinTemperature(getMayMStringemp());	
		return weatherInfoAvg;
	}
	
	public WeatherInfoAvg getJunWeatherInfo() {
		WeatherInfoAvg weatherInfoAvg = new WeatherInfoAvg();	
		weatherInfoAvg.setHumidity(getJunHumidity());
		weatherInfoAvg.setMaxTemperature(getJunMaxTemp());
		weatherInfoAvg.setMeanTemperature(getJunMeanTemp());
		weatherInfoAvg.setMinTemperature(getJunMStringemp());	
		return weatherInfoAvg;
	}
	
	public WeatherInfoAvg getJulWeatherInfo() {
		WeatherInfoAvg weatherInfoAvg = new WeatherInfoAvg();	
		weatherInfoAvg.setHumidity(getJulHumidity());
		weatherInfoAvg.setMaxTemperature(getJulMaxTemp());
		weatherInfoAvg.setMeanTemperature(getJulMeanTemp());
		weatherInfoAvg.setMinTemperature(getJulMStringemp());	
		return weatherInfoAvg;
	}
	
	public WeatherInfoAvg getAugWeatherInfo() {
		WeatherInfoAvg weatherInfoAvg = new WeatherInfoAvg();	
		weatherInfoAvg.setHumidity(getAugHumidity());
		weatherInfoAvg.setMaxTemperature(getAugMaxTemp());
		weatherInfoAvg.setMeanTemperature(getAugMeanTemp());
		weatherInfoAvg.setMinTemperature(getAugMStringemp());	
		return weatherInfoAvg;
	}
	
	public WeatherInfoAvg getSepWeatherInfo() {
		WeatherInfoAvg weatherInfoAvg = new WeatherInfoAvg();	
		weatherInfoAvg.setHumidity(getSepHumidity());
		weatherInfoAvg.setMaxTemperature(getSepMaxTemp());
		weatherInfoAvg.setMeanTemperature(getSepMeanTemp());
		weatherInfoAvg.setMinTemperature(getSepMStringemp());	
		return weatherInfoAvg;
	}
	
	public WeatherInfoAvg getOctWeatherInfo() {
		WeatherInfoAvg weatherInfoAvg = new WeatherInfoAvg();	
		weatherInfoAvg.setHumidity(getOctHumidity());
		weatherInfoAvg.setMaxTemperature(getOctMaxTemp());
		weatherInfoAvg.setMeanTemperature(getOctMeanTemp());
		weatherInfoAvg.setMinTemperature(getOctMStringemp());	
		return weatherInfoAvg;
	}
	
	public WeatherInfoAvg getNovWeatherInfo() {
		WeatherInfoAvg weatherInfoAvg = new WeatherInfoAvg();	
		weatherInfoAvg.setHumidity(getNovHumidity());
		weatherInfoAvg.setMaxTemperature(getNovMaxTemp());
		weatherInfoAvg.setMeanTemperature(getNovMeanTemp());
		weatherInfoAvg.setMinTemperature(getNovMStringemp());	
		return weatherInfoAvg;
	}
	
	public WeatherInfoAvg getDecWeatherInfo() {
		WeatherInfoAvg weatherInfoAvg = new WeatherInfoAvg();	
		weatherInfoAvg.setHumidity(getDecHumidity());
		weatherInfoAvg.setMaxTemperature(getDecMaxTemp());
		weatherInfoAvg.setMeanTemperature(getDecMeanTemp());
		weatherInfoAvg.setMinTemperature(getDecMStringemp());	
		return weatherInfoAvg;
	}

}
