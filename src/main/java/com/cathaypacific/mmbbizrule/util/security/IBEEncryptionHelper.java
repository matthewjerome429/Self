package com.cathaypacific.mmbbizrule.util.security;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.UnexpectedException;

@Component
public class IBEEncryptionHelper {

	private static final String ALGORITHM_AES = "AES";

	private static final Pattern KEY_CONFIG_PATTERN = Pattern.compile("(?<keyId>\\w+):(?<key>[0-9A-Fa-f]{64})");

	private static final String KEY_CONFIG_PATTERN_GROUP_KEY_ID = "keyId";

	private static final String KEY_CONFIG_PATTERN_GROUP_KEY = "key";

	@Value("#{'${ibe.changeFlight.keys}'.split(',')}")
	private List<String> keyConfigs;

	@Value("#{'${ibe.changeFlight.activeKeys}'.split(',')}")
	private List<String> activeKeyIds;

	private Random random = new Random();

	private KeyInfo[] keyInfos;

	public static class KeyInfo {
		private String keyId;
		private SecretKey secretKey;

		public String getKeyId() {
			return keyId;
		}

		public void setKeyId(String keyId) {
			this.keyId = keyId;
		}

		public SecretKey getSecretKey() {
			return secretKey;
		}

		public void setSecretKey(SecretKey secretKey) {
			this.secretKey = secretKey;
		}

	}

	@PostConstruct
	private void loadKey() {

		List<KeyInfo> keyInfoList = new ArrayList<>();
		for (String keyConfig : keyConfigs) {
			Matcher matcher = KEY_CONFIG_PATTERN.matcher(keyConfig);
			if (matcher.matches()) {
				String keyId = matcher.group(KEY_CONFIG_PATTERN_GROUP_KEY_ID);
				String keyHex = matcher.group(KEY_CONFIG_PATTERN_GROUP_KEY);

				if (activeKeyIds.contains(keyId)) {
					KeyInfo keyInfo = new KeyInfo();
					keyInfo.setKeyId(keyId);

					byte[] keyBytes = hexToBytes(keyHex);
					SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, ALGORITHM_AES);
					keyInfo.setSecretKey(secretKeySpec);
					keyInfoList.add(keyInfo);
				}
			}
		}

		keyInfos = new KeyInfo[keyInfoList.size()];
		keyInfoList.toArray(keyInfos);
	}

	private byte[] hexToBytes(String hex) {
		if (StringUtils.isEmpty(hex)) {
			return new byte[0];
		}

		if ((hex.length() % 2) != 0) {
			hex = "0" + hex;
		}

		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			String hexDigit = hex.substring(i, i + 2);
			int number = Integer.parseInt(hexDigit, 16);
			byte digitByte = (byte) number;
			bytes[i / 2] = digitByte;
		}

		return bytes;
	}
	
	private String bytesToHex(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return "";
		}
		
		StringBuilder hexBuilder = new StringBuilder(bytes.length * 2);
		for (byte digitByte : bytes) {
			int number = digitByte & 0xFF;
			
			// Convert first argument to hex, width 2 with zero-padding.
			String hexDigit = String.format("%1$02X", number);
			
			hexBuilder.append(hexDigit);
		}
		
		return hexBuilder.toString();
	}

	private KeyInfo getKeyById(String keyId) {

		return Stream.of(keyInfos).filter(keyInfo -> Objects.equals(keyInfo.getKeyId(), keyId)).findFirst().orElse(
				null);
	}

	/**
	 * Get one of active key ID randomly.
	 * 
	 * @return
	 */
	public String getActiveKeyId() {

		if (keyInfos.length == 0) {
			return null;
		}

		int index = random.nextInt(keyInfos.length);
		return keyInfos[index].getKeyId();
	}

	/**
	 * Encrypt message by specified key and encode result to Hex.
	 * 
	 * @param plaintext
	 * @param keyId
	 * @return
	 * @throws UnexpectedException 
	 */
	public String encryptMessage(String plaintext, String keyId) throws UnexpectedException {
		if (StringUtils.isEmpty(plaintext)) {
			return "";
		}
		
		KeyInfo keyInfo = getKeyById(keyId);
		if (keyInfo == null) {
			throw new UnexpectedException("Key is not found", new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}

		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
			cipher.init(Cipher.ENCRYPT_MODE, keyInfo.getSecretKey());

			byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
			byte[] ciphertextBytes = cipher.doFinal(plaintextBytes);

			return bytesToHex(ciphertextBytes);
		} catch (Exception ex) {
			throw new UnexpectedException("Encrypt message failed", new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM), ex);
		}
	}

	public String decryptMessage(String ciphertext, String keyId) throws UnexpectedException {
		if (StringUtils.isEmpty(ciphertext)) {
			return "";
		}

		KeyInfo keyInfo = getKeyById(keyId);
		if (keyInfo == null) {
			throw new UnexpectedException("Key is not found", new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM));
		}

		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM_AES);
			cipher.init(Cipher.DECRYPT_MODE, keyInfo.getSecretKey());

			byte[] ciphertextBytes = hexToBytes(ciphertext);
			byte[] plaintextBytes = cipher.doFinal(ciphertextBytes);

			return new String(plaintextBytes);
		} catch (Exception ex) {
			throw new UnexpectedException("Decrypt message failed", new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM), ex);
		}
	}

}
