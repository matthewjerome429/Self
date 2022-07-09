package com.cathaypacific.mmbbizrule.controller;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.cxservice.olci.model.response.CancelCheckInResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.olci.service.OLCIService;
import com.cathaypacific.mmbbizrule.dto.response.cancelcheckin.BookingCancelCheckInResponseDTO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "/v1")
public class CancelCheckInController {

    @Autowired
    private OLCIService olciService;

    @PutMapping("/cancelcheckin/{rloc}")
    @ApiOperation(value = " cancel check in", response = BookingCancelCheckInResponseDTO.class, produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = "")})
    @CheckLoginInfo
    public BookingCancelCheckInResponseDTO cancelCheckIn(@PathVariable("rloc") String rloc, @ApiIgnore @LoginInfoPara LoginInfo loginInfo) throws BusinessBaseException, InterruptedException {

        return  olciService.cancelCheckInByLogin(loginInfo,rloc);
    }

}
