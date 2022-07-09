package com.cathaypacific.mbcommon.utils;

import java.util.UUID;

public class TokenUtil {

	public static String createToken() {
		UUID uuid = UUID.randomUUID();
		String token = uuid.toString();
		token = token.toUpperCase();
		// replace - to space
		token = token.replaceAll("-", "");
		return token;
	}
}
