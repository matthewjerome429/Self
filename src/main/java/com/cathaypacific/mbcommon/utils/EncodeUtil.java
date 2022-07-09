package com.cathaypacific.mbcommon.utils;

import java.security.MessageDigest;
import java.util.Base64;

import com.google.gson.Gson;

public class EncodeUtil {
	
	private static Gson gson = new Gson();
	
	private EncodeUtil(){
		//do nothing
	}
	
	/**
	 * Encoder the obj
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
	
	/**
	 * Encoder the obj
	 * @Description md5 encode
	 * @param
	 * @return String
	 */
	public static String encoderByMd5UTF8(Object... objs ){
		String ciphertext = "";
		StringBuilder sb = new StringBuilder();
		for (Object obj :objs) {
			sb.append(gson.toJson(obj));
		}
		try {
			ciphertext = String.valueOf(Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest(sb.toString().getBytes())));
		} catch (Exception e) {
			ciphertext = TokenUtil.createToken();
		}
		return ciphertext;
    }
}
