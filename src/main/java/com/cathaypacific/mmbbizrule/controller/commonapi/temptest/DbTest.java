package com.cathaypacific.mmbbizrule.controller.commonapi.temptest;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mmbbizrule.db.dao.NonAirSegmentDao;
import com.cathaypacific.mmbbizrule.util.BizRulesUtil;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@Api(tags = {" Temp For Test"} , description= "just for test, will delete before go live")
@RestController
@RequestMapping(path = "/v1/common")
public class DbTest {

	@Autowired
	private NonAirSegmentDao nonAirSegmentDao;
	@GetMapping("/dbtest/nocache/{times}")
	@ApiOperation(value = "nocache.", produces = "application/json")
	public Long nocache(@PathVariable int times) {
		Long startTime = System.currentTimeMillis();
		for(int i=0; i<times;i++){
			nonAirSegmentDao.findNonAirSegmentType("MMB", "CDG", "NCE", "3A");
		}
		
		return  System.currentTimeMillis() - startTime;
	}

 
	@GetMapping("/dbtest/sharecache/{times}")
	@ApiOperation(value = "sharecache.", produces = "application/json")
	public Long sharecache(@PathVariable int times) {
		Long startTime = System.currentTimeMillis();
		for(int i=0; i<times;i++){
			nonAirSegmentDao.findNonAirSegmentTypeShareKeyGeneratorCache("MMB", "CDG", "NCE", "3A");
		}
		
		return  System.currentTimeMillis() - startTime;
	}
	
 
	@GetMapping("/dbtest/cache/{times}")
	@ApiOperation(value = "cache.", produces = "application/json")
	public Long cache(@PathVariable int times) {
		Long startTime = System.currentTimeMillis();
		for(int i=0; i<times;i++){
			nonAirSegmentDao.findNonAirSegmentTypeCache("MMB", "CDG", "NCE", "3A");
		}
		return  System.currentTimeMillis() - startTime;
	}
}
