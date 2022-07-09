package com.cathaypacific.mmbbizrule.controller.commonapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mmbbizrule.business.commonapi.DestinationInfoBusiness;
import com.cathaypacific.mmbbizrule.dto.response.commonapi.destinationinfo.DestinationInfoDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"Common APIs"} , value="Get destination info")
@RestController
@RequestMapping(path = "/v1")
public class DestinationCityInfoController {
	
	@Autowired
	private DestinationInfoBusiness destinationInfoBusiness;

	@RequestMapping(path = "/common/destinationinfo/{countryCode}/{arriveDate}", method = { RequestMethod.GET })
	@ApiOperation(value = " pax name sequence", response = DestinationInfoDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "countryCode", value = "Country Code", required = true, dataType = "string", paramType = "path", defaultValue = ""),
		@ApiImplicitParam(name = "arriveDate", value = "Arrive Date", required = true, dataType = "string", paramType = "path", defaultValue = "")
	})
	public DestinationInfoDTO getDestinationInfo(@PathVariable String countryCode, @PathVariable String arriveDate){

		DestinationInfoDTO responseDTO = new DestinationInfoDTO();	
		
		responseDTO.setCurrencyInfo(destinationInfoBusiness.getCurrencyInfoByCountryCode(countryCode));
		responseDTO.setPowerInfo(destinationInfoBusiness.getPowerInfoByCountryCode(countryCode));
		return responseDTO;
	
	}	
	
	@RequestMapping(path = "/common/destinationinfo/{countryCode}/{arriveDate}/{destPort}", method = { RequestMethod.GET })
	@ApiOperation(value = " pax name sequence", response = DestinationInfoDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "countryCode", value = "Country Code", required = true, dataType = "string", paramType = "path", defaultValue = ""),
		@ApiImplicitParam(name = "arriveDate", value = "Arrive Date", required = true, dataType = "string", paramType = "path", defaultValue = ""),
		@ApiImplicitParam(name = "destPort", value = "Dest Port", required = true, dataType = "string", paramType = "path", defaultValue = "")
	})
	public DestinationInfoDTO getDestinationInfo(@PathVariable String countryCode, @PathVariable String arriveDate, @PathVariable String destPort){

		DestinationInfoDTO responseDTO = new DestinationInfoDTO();	
		
		responseDTO.setCurrencyInfo(destinationInfoBusiness.getCurrencyInfoByCountryCode(countryCode));
		responseDTO.setPowerInfo(destinationInfoBusiness.getPowerInfoByCountryCode(countryCode));
		responseDTO.setWeatherInfoAvg(destinationInfoBusiness.getWeatherInfoByPortCode(destPort, arriveDate));
		return responseDTO;
	
	}	
}
