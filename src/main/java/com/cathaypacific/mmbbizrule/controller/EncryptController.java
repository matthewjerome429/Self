package com.cathaypacific.mmbbizrule.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.util.security.EncryptionHelper;
import com.cathaypacific.mmbbizrule.util.security.EncryptionHelper.Encoding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * Created by Zilong Bu.
 */
@Api(tags = {"AES"}, description = "Handle encrypt infomation.")
@RestController
@RequestMapping(path = "/v1")
public class EncryptController {
	
	private static LogAgent LOGGER = LogAgent.getLogAgent(RetrievePnrController.class);
	
	@Autowired
	private EncryptionHelper encryptionHelper;
	
	@GetMapping("/aes/decrypt")
	@ApiOperation(value = "Export Trip Summary")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "channel", value = "Chanenl", required = true, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "encryptedStr", value = "the encrypted str", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "appCode", value = "The app code", required = false, dataType = "string", paramType = "query", defaultValue = ""),
	})
	public String decryptMessage(String encryptedStr,String channel, String appCode) throws UnexpectedException {
		
		String decryptedStr = encryptionHelper.decryptMessage(encryptedStr, Encoding.BASE64URL, channel, appCode);
		LOGGER.debug("Decode encrypted str success:"+decryptedStr);
		
		return decryptedStr;
	}
	
	@GetMapping("/aes/encrypt/loginInfo")
	@ApiOperation(value = "encrypted message as json format, AES+BASE64URL", response = String.class, produces = "text/plain")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "rloc", value = "Booking Rloc", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "eticket", value = "Booking Eticket", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "givenName", value = "Given Name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "familyName", value = "Family Name", required = true, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "channel", value = "The channel of key", required = true, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "appCode", value = "The app code", required = false, dataType = "string", paramType = "query", defaultValue = ""),
	})
	public String getEncryptedLoginInfoDetails(String rloc, String eticket, String givenName, String familyName, String channel, String appCode) throws UnexpectedException {
		Map<String, String> map = new HashMap<>();
		map.put("rloc", rloc);
		map.put("eticket", eticket);
		map.put("givenName", givenName);
		map.put("familyName", familyName);
		
		Gson gson = new GsonBuilder().create();
		String jsonStr = gson.toJson(map);
		LOGGER.debug("Converted request to json string: "+jsonStr);
		
		String encryptedStr = encryptionHelper.encryptMessage(jsonStr, Encoding.BASE64URL, channel, "olss", appCode);
		LOGGER.debug("Encrypted String: "+encryptedStr);
		
		return encryptedStr;
	}
	
	@GetMapping("/aes/encrypt")
	@ApiOperation(value = "encrypted message, AES+BASE64URL", response = String.class, produces = "text/plain")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "message", value = "Booking Rloc", required = false, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "channel", value = "The channel of key", required = true, dataType = "string", paramType = "query", defaultValue = ""),
		@ApiImplicitParam(name = "appCode", value = "The app code", required = false, dataType = "string", paramType = "query", defaultValue = ""),
	})
	public String getEncryptedDetails(String message, String channel, String appCode) throws UnexpectedException {
		String encryptedStr = encryptionHelper.encryptMessage(message, Encoding.BASE64URL, channel, "olss", appCode);
		LOGGER.debug("Encrypted String: "+encryptedStr);
		
		return encryptedStr;
	}
}
