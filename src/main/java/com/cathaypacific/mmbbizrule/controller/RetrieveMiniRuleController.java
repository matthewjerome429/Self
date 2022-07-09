package com.cathaypacific.mmbbizrule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.business.RetrieveMiniRuleBusiness;
import com.cathaypacific.mmbbizrule.dto.request.oneaoperation.AddRmRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.oneaoperation.AddSkRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.oneaoperation.OneAOperationResponseDTO;
import com.cathaypacific.oneaconsumer.model.request.tmrxrq_17_2_1a.MiniRuleGetFromRec;
import com.cathaypacific.oneaconsumer.model.response.tmrxrq_17_2_1a.MiniRuleGetFromRecReply;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"Mini rule"}, description = "Mini rule APIs")
@RestController
@RequestMapping(path = "/v1/minirule")
public class RetrieveMiniRuleController {
	
	@Autowired
	private RetrieveMiniRuleBusiness retrieveMiniRuleBusiness;
	
	@PostMapping("/byrec")
	@ApiOperation(value = "get minirule by rec", response = MiniRuleGetFromRecReply.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "rloc", value = "rloc", required = true, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "Access-Channel", value = "The Access Channel", required = true, dataType = "string", paramType = "header", defaultValue = "MMB"),
	})
	public MiniRuleGetFromRecReply retrieveMiniRule(String rloc) throws BusinessBaseException {
		return retrieveMiniRuleBusiness.retrieveMiniRuleFromRec(rloc);
	}
	

}
