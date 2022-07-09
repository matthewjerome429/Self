package com.cathaypacific.mmbbizrule.v2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.annotation.CheckRloc;
import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.dto.request.updateseat.UpdateSeatRequestDTO;
import com.cathaypacific.mmbbizrule.v2.business.UpdateSeatBusinessV2;
import com.cathaypacific.mmbbizrule.v2.dto.request.updateseat.UpdateSeatRequestDTOV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.updateseat.UpdateSeatResponseDTOV2;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * OLSS-MMB
 * 
 * @Desc this controller is used to add seat or seat preference to oneA for passenger
 * @author fengfeng.jiang
 * @date Jan 26, 2018 2:50:11 PM
 * @version V1.0
 */
@Api(tags = {"Seat APIs"}, description = "Seat APIs")
@RestController
@RequestMapping(path = "/v2")
public class UpdateSeatControllerV2 {
	
	@Autowired
	UpdateSeatBusinessV2 updateSeatBusinessV2;
	
	@PutMapping("/updateSeat")
	@ApiOperation(value = "Update seat info for passenger", response = UpdateSeatResponseDTOV2.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")})
	@CheckLoginInfo
	@CheckRloc(rlocPath="requestDTO.rloc", argIndex = 0)
	public UpdateSeatResponseDTOV2 updateSeat(@RequestBody @Validated UpdateSeatRequestDTOV2 requestDTO, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException{
		return updateSeatBusinessV2.updateSeat(loginInfo, requestDTO);
	}
	
}
