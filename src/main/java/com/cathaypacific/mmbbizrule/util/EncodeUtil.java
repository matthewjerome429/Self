package com.cathaypacific.mmbbizrule.util;

import java.security.MessageDigest;
import java.util.Base64;

import com.cathaypacific.mbcommon.utils.TokenUtil;
import com.google.gson.Gson;

public class EncodeUtil {
	private static Gson gson = new Gson();
	/**
	 * 
	 * @Description md5 encode
	 * @param
	 * @return String
	 */
	public static String encoderByMd5UTF8(Object obj){
		String ciphertext = "";
		
		try {
			ciphertext = String.valueOf(Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest(gson.toJson(obj).getBytes())));
		} catch (Exception e) {
			ciphertext = TokenUtil.createToken();
		}
		return ciphertext;
    }
	 
}
