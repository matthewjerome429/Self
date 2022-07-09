package com.cathaypacific.mmbbizrule.v2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mmbbizrule.cxservice.messagehub.model.request.EventsRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.messagehub.model.response.EventsResponseDTO;
import com.cathaypacific.mmbbizrule.v2.business.MessageHubBusiness;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = { "Message Hub Operations" })
@RestController
@RequestMapping(path = "/v2")
public class MessageHubController {

    @Autowired
    private MessageHubBusiness messageHubBusiness;

    /**
     * retrieve events from message hub
     * 
     * @param requestDTO
     * @return
     * @throws Exception
     */
    @GetMapping("/messagehub/events")
    @ApiOperation(value = "Get events from message hub", response = EventsResponseDTO.class, produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
            @ApiImplicitParam(name = "rloc", value = "Rloc", required = true, dataType = "string", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "eventTypes", value = "Event Types", required = true, dataType = "string", paramType = "query", allowMultiple = true, defaultValue = ""), })
    @CheckLoginInfo
    public EventsResponseDTO getEvents(@ApiIgnore @Validated EventsRequestDTO requestDTO) throws Exception {
        return messageHubBusiness.getEvents(requestDTO);
    }

}
