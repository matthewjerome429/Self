package com.cathaypacific.mmbbizrule.cxservice.referencedata.nationality.service;

public interface NationalityCodeService {
	/**
	 * find Three Country Code By Two Country Code
	 * 
	 * @param countryCode
	 * @return
	 * @throws BusinessException
	 */
	public String findThreeCountryCodeByTwoCountryCode(String countryCode);
	
	/**
	 * find Two Country Code By Three Country Code
	 * 
	 * @param countryCode
	 * @return
	 * @throws BusinessException
	 */
	public String findTwoCountryCodeByThreeCountryCode(String countryCode);
}
