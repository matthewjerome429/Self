package com.cathaypacific.mmbbizrule.controller;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.aop.logininfocheck.CheckLoginInfo;
import com.cathaypacific.mbcommon.aop.logininfocheck.LoginInfoPara;
import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mbcommon.model.login.LoginInfo;
import com.cathaypacific.mmbbizrule.business.ExportBusiness;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.util.Ical4jUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.fortuna.ical4j.model.Calendar;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = {"Export"}, description = "Export API")
@RestController
@RequestMapping(path = "/v1")
public class ExportController {
	
	@Autowired
	private ExportBusiness exportBusiness;
	
	@GetMapping("/export/tripsummary")
	@ApiOperation(value = "Export Trip Summary")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "MMB-Token", value = "The MMB Token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
		@ApiImplicitParam(name = "onearloc", value = "flight booking rloc", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "ojrloc", value = "package/hotel booking rloc", required = false, dataType = "string", paramType = "query", defaultValue = "")
	})
	@CheckLoginInfo
	public void exportTripSummary(HttpServletResponse response, @ApiIgnore @LoginInfoPara LoginInfo loginInfo,
			@RequestParam(value = "onearloc", required = false) String oneARloc,
			@RequestParam(value = "ojrloc", required = false) String ojRloc) throws IOException, BusinessBaseException, ParseException {
		// Get Calendar
		Calendar calendar = exportBusiness.getTripSummaryCal(oneARloc, ojRloc, loginInfo);
		
		// Build fileName
		String rloc = StringUtils.isEmpty(ojRloc) ? oneARloc : ojRloc;
		String fileName = MMBBizruleConstants.EXPORT_CALENDAR_FILE_PREFIX + rloc + MMBBizruleConstants.EXPORT_CALENDAR_FILE_SUFFIX;
		
		response.setHeader(MMBConstants.CONTENT_DISPOSITION, MMBConstants.CONTENT_DISPOSITION_VALUE_PREFIX + fileName);
		Ical4jUtil.output(calendar, response.getOutputStream());
	}
	
}
