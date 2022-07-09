package com.cathaypacific.mmbbizrule.business;

import org.springframework.validation.annotation.Validated;

import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.baggage.BaggageAllowanceRequestDTO;
import com.cathaypacific.mmbbizrule.dto.request.baggage.ExtraBaggageRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.baggage.BaggageAllowanceResponseDTO;
import com.cathaypacific.mmbbizrule.dto.response.baggage.ExtraBaggageResponseDTO;

import springfox.documentation.annotations.ApiIgnore;

public interface BaggageBusiness {
	
	public BaggageAllowanceResponseDTO getBaggageAllowance(@ApiIgnore @Validated BaggageAllowanceRequestDTO requestDTO,
			@ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException;

	public ExtraBaggageResponseDTO getExtraBaggage(@ApiIgnore @Validated ExtraBaggageRequestDTO requestDTO,
			@ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException;

}
