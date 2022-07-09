package com.cathaypacific.mmbbizrule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.annotation.CheckRloc;
import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.MealBusiness;
import com.cathaypacific.mmbbizrule.dto.request.meal.updateMeal.UpdateMealRequestDTO;
import com.cathaypacific.mmbbizrule.dto.response.meal.updateMeal.UpdateMealResponseDTO;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "/v1")
public class MealController {
	
	@Autowired
	MealBusiness mealBusinessImpl;
	
	
	@PutMapping("/updateMeal")
	@ApiOperation(value = "Update meal", response = UpdateMealResponseDTO.class, produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")})
	@CheckLoginInfo
	@CheckRloc(rlocPath="updateMealRequestDTO.rloc", argIndex = 0)
	public UpdateMealResponseDTO updateMeal(@RequestBody @Validated UpdateMealRequestDTO updateMealRequestDTO , @LoginInfoPara @ApiIgnore LoginInfo loginInfo) throws BusinessBaseException{
	 
		return mealBusinessImpl.updateMeal(updateMealRequestDTO, loginInfo);
	}

}
