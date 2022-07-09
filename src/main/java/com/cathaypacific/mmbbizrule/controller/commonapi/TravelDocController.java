package com.cathaypacific.mmbbizrule.controller.commonapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mmbbizrule.business.commonapi.TravelDocBusiness;
import com.cathaypacific.mmbbizrule.dto.response.commonapi.traveldoc.TBTravelDocNatCoiMappingDTO;
import com.cathaypacific.mmbbizrule.dto.response.commonapi.traveldoc.TravedocTypesResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.commonapi.traveldoc.TravelDocNatCoiCheckResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = {"Common APIs"} , description= "Common APIs")
@RestController
@RequestMapping(path = "/v1")
public class TravelDocController {
	
	@Autowired
	private TravelDocBusiness travelDocBusiness;
	
	@GetMapping("/common/traveldoc/types/{apiVersion}")
	@ApiOperation(value = "Retrieve traveldoc types by api version.", produces = "application/json")
	public TravedocTypesResponseDTO getTravedocTypes(@PathVariable Integer apiVersion) {

		return travelDocBusiness.getTravedocTypesByApiVersion(apiVersion);
	}
	
	@RequestMapping(path = "/common/traveldoc/natandcoi", method = { RequestMethod.GET })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = TravelDocNatCoiCheckResponseDTO.class),
			@ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 500, message = "Failure") })
	public TravelDocNatCoiCheckResponseDTO showNATAndCOI(){

		TravelDocNatCoiCheckResponseDTO responseDTO = new TravelDocNatCoiCheckResponseDTO();	
		List<TBTravelDocNatCoiMappingDTO> docNatCoiCheckList = travelDocBusiness.getNATAndCOI();
		responseDTO.setDocNatCoiCheckList(docNatCoiCheckList);
		return responseDTO;
	
	}	
}
