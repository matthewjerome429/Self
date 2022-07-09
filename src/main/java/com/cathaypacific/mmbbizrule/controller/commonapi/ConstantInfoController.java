package com.cathaypacific.mmbbizrule.controller.commonapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mmbbizrule.business.commonapi.ConstantInfoBusiness;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"Common APIs"} , description= "Common APIs")
@RestController
@RequestMapping(path = "/v1")
public class ConstantInfoController {

	@Autowired
	private ConstantInfoBusiness constantInfoBusiness;

	@GetMapping("/common/titlelist")
	@ApiOperation(value = "Retrieve title list.", produces = "application/json")
	public List<String> getTitleList() {

		return constantInfoBusiness.getTitleList();
	}

}
