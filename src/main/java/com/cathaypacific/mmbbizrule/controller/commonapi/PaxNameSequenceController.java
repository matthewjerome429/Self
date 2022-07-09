package com.cathaypacific.mmbbizrule.controller.commonapi;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mmbbizrule.business.commonapi.PaxNameSequenceBusiness;
import com.cathaypacific.mmbbizrule.dto.response.namesequence.PaxNameSequenceDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"Common APIs"} , description= "Common APIs")
@RestController
@RequestMapping(path = "/v1")
public class PaxNameSequenceController {
	@Autowired
	private PaxNameSequenceBusiness paxNameSequenceBusiness;

	@GetMapping("/common/paxnameSequence/{locale}")
	@ApiOperation(value = " pax name sequence", response = PaxNameSequenceDTO.class, produces = "application/json")
	public PaxNameSequenceDTO findPaxNameSequence(@RequestHeader(name = MMBConstants.APP_CODE) String appCode,
			@PathVariable String locale) {
		String checkedAppCode = StringUtils.isEmpty(appCode) ? MMBConstants.APP_CODE : appCode;

		return paxNameSequenceBusiness.findPaxNameSequence(checkedAppCode, locale);
	}
}
