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
import com.cathaypacific.mmbbizrule.dto.request.meal.updateMeal.UpdateMealRequestDTO;
import com.cathaypacific.mmbbizrule.v2.business.MealBusinessV2;
import com.cathaypacific.mmbbizrule.v2.dto.response.meal.updateMeal.UpdateMealResponseDTOV2;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "/v2")
public class MealControllerV2 {
	
	@Autowired
	MealBusinessV2 mealBusinessImpl;
	
	@PutMapping("/updateMeal")
	@ApiOperation(value = "Update meal", response = UpdateMealResponseDTOV2.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")})
	@CheckLoginInfo
	@CheckRloc(rlocPath="updateMealRequestDTO.rloc", argIndex = 0)
	public UpdateMealResponseDTOV2 updateMeal(@RequestBody @Validated UpdateMealRequestDTO updateMealRequestDTO , @LoginInfoPara @ApiIgnore LoginInfo loginInfo) throws BusinessBaseException{
		return mealBusinessImpl.updateMeal(updateMealRequestDTO, loginInfo);
	}
	
}
