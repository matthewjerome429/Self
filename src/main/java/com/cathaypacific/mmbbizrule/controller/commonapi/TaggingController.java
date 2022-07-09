package com.cathaypacific.mmbbizrule.controller.commonapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.business.commonapi.TaggingBusiness;
import com.cathaypacific.mmbbizrule.dto.response.tagging.TaggingDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"Common APIs"} , description= "Common APIs")
@RestController
@RequestMapping(path = "/v1")
public class TaggingController {
	
	@Autowired
	TaggingBusiness taggingBusiness;
	
	@GetMapping("/common/tagMapping")
	@ApiOperation(value = "Retrieve tag mapping", response = TaggingDTO.class, produces = "application/json")
	public TaggingDTO retrieveTagMapping() throws BusinessBaseException{
		return taggingBusiness.retrieveTagMapping();
	}

}
