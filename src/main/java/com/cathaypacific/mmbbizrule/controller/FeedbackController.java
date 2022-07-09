package com.cathaypacific.mmbbizrule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.SaveFeedbackBusiness;
import com.cathaypacific.mmbbizrule.dto.request.feedback.SaveFeedbackRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.feedback.SaveFeedbackResponseDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"Feedback API"}, description = "Feedback API")
@RestController
@RequestMapping(path = "/v1")
public class FeedbackController {
	
	@Autowired
	private SaveFeedbackBusiness saveFeedbackBusiness;

	@PutMapping("/saveFeedback")
	@ApiOperation(value = "Save feedback", response = SaveFeedbackResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""), })
	@CheckLoginInfo
	public SaveFeedbackResponseDTO saveFeedback(@RequestBody SaveFeedbackRequestDTO requestDTO, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException{
		
		return saveFeedbackBusiness.saveFeedback(loginInfo, requestDTO);
	}
}
