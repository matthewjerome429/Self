package com.cathaypacific.mmbbizrule.util.security;

import java.util.List;
import java.util.stream.Stream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cathaypacific.mbcommon.constants.MMBConstants;
import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.UnexpectedException;
import com.cathaypacific.mbcommon.loging.LogAgent;
import com.cathaypacific.mmbbizrule.config.EncryptionConfig;
import com.cathaypacific.mmbbizrule.constant.MMBBizruleConstants;
import com.cathaypacific.mmbbizrule.db.dao.SecurityKeyMappingDao;
import com.cathaypacific.mmbbizrule.db.model.SecurityKeyMapping;

/**
 * Utility class used for the purpose of encrypting and decryption messages when sending them over a network or across VMs or while storing secured information in files or cookies.
 */
@Component
public class EncryptionHelper {
	@Autowired
	private KeyStoreReader keyStoreReader;
	
	@Autowired
	private SecurityKeyMappingDao securityKeyDao;

	/**
	 * Enum to encode either by Base64 or HEX.
	 */
	public enum Encoding {
		/**
		 * Base64 encoding mechanism.
		 */
		BASE64,
		
		/**
		 * Base64 encoding mechanism.
		 */
		BASE64URL,

		/**
		 * HEX encoding mechanism.
		 */
		HEX
	};

	/**
	 * Characters to be intialized for HEX encoding.
	 */
	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * Encoding standard used while constructing <code>String</code>s from byte and char arrays.
	 */
	private static final String ENCODING_STANDARD = "UTF-8";

	/**
	 * Constant which declares the class name.
	 */
	public static final String CLASS_NAME = "EncryptionHelper";

	/**
	 * Algorithms used for all/symmetric encryption purposes in CEP frameworks.
	 */
	private static final String ALGORITHM = "AES/ECB/PKCS5PADDING";

	/**
	 * Cipher instance used to encrypt/decrypt the messages.
	 */
	// private Cipher cipher = null;

	/**
	 * Key instance used for encryption or decryption.
	 */
	// private SecretKey key = null;

	/**
	 * Constant used for AES-256 key length indication.
	 */
//	private static final int KEY_LENGTH = 256;

	/**
	 * Constant used for the SHIFT operations.
	 */
	private static final int SHIFT_TO_FOUR = 4;

	/**
	 * Constant used for HEX String conversions.
	 */
	private static final int CONSTANT_0XA = 0xa;

	/**
	 * Constant used for HEX String conversions.
	 */
	private static final int CONSTANT_0XFO = 0xf0;

	/**
	 * Constant used for HEX String conversions.
	 */
	private static final int CONSTANT_0XOF = 0x0f;
	
	private static final String CEP_KEYSTORE = "cep";
	
	private static final String OLSS_KEYSTORE = "olss";
	
	@Autowired
	private EncryptionConfig encryptionConfig;
	
	private static final LogAgent LOGGER = LogAgent.getLogAgent(EncryptionHelper.class);

	/**
	 * Method used to retrive the key from the KeyStore.
	 * @param keyStoreName TODO
	 * @param keyName
	 *            - The alias used to store the key in the KeyStore. It is usually the application name, e.g. "ibe", "cop", "psc", etc.
	 *
	 * @return The SecreyKey object corresponding to the parameterized key
	 * @throws BusinessException
	 *             If any exceptions occur while loading the keys from the KeyStore
	 */
	protected SecretKey getKeyFromKeyStore(final String keyStoreName, final String keyName) throws UnexpectedException {
		return keyStoreReader.getKey(keyName, keyStoreName);
	}

	/**
	 * Method used for Encrypting a message when String is passed.
	 *
	 * @param message
	 *            - String input
	 * @return String value
	 * @throws BusinessException
	 *             Thrown if the encryption process fails
	 */
	public String encryptMessage(final String message, String keyName) throws UnexpectedException {
		return encryptMessage(message, Encoding.HEX, keyName);
	}

	/**
	 * Method used for Encrypting a message when String & Encoding Mechanism is passed.
	 *
	 * @param message
	 *            - String input
	 * @param encoding
	 *            - Encoding Standard BAse64/Hex
	 * @param keyName
	 * 			  - key name/alias
	 * @return String value
	 * @throws BusinessException
	 *             Exception might thrown while encrypting the message
	 */
	public String encryptMessage(final String message, final Encoding encoding, String keyName) throws UnexpectedException {
		SecretKey key = getKeyFromKeyStore(CEP_KEYSTORE, keyName);
		return encryptMessage(message, key, encoding, "");
	}
	
	/**
	 * Method used for Encrypting a message when String & Encoding Mechanism is passed.
	 *
	 * @param message
	 *            - String input
	 * @param encoding
	 *            - Encoding Standard BAse64/Hex
	 * @param keyName
	 * 			  - key name/alias string input
	 * @param keyStore
	 * 			  - keyStore name string input
	 * @return String value
	 * @throws BusinessException
	 *             Exception might thrown while encrypting the message
	 */
	public String encryptMessage(final String message, final Encoding encoding, String channel, String keyStore, String appCode) throws UnexpectedException {
		SecretKey key = getKeyFromKeyStore(keyStore, getKeyNameByChannel(channel));
		return encryptMessage(message, key, encoding, appCode);
	}

	/**
	 * Method used for encrypting a message based on the key passed.
	 *
	 * @param message
	 *            - Input String to be encrypted
	 * @param pKey
	 *            - Secret Key value used for Encryption
	 * @param encoding
	 *            - Either Base64 or HEX based encoding
	 * @return String encrypted value
	 * @throws BusinessException
	 *             Thrown if the encryption process fails
	 */
	private String encryptMessage(final String message, final SecretKey pKey, final Encoding encoding, String appCode) throws UnexpectedException {
		try {
			byte[] encBytes;
			byte[] inputBytes = message.getBytes(ENCODING_STANDARD);
			List<String> appCodeList = encryptionConfig.getAppCode();
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, pKey);
			encBytes = cipher.doFinal(inputBytes);

			if((appCodeList != null && appCodeList.contains(appCode))){
				return new String(java.util.Base64.getEncoder().encode(encBytes));
			}else if(encoding == Encoding.BASE64) {
				return new String(Base64.encode(encBytes));
			}else if(encoding == Encoding.BASE64URL) {
				return new String(java.util.Base64.getUrlEncoder().encodeToString(encBytes));
			}else {
				return toHexString(encBytes);
			}
		} catch (Exception e) {
			ErrorInfo errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
			throw new UnexpectedException("encryptMessage error: " + e.getMessage(), errorInfo);
		}
	}

	/**
     * Method used for decrypting a message by passing the Encryped String.
     *
     * @param message - Encrypted String value
     * @return String message that is decrypted
     * @throws Exception Thrown if the decryption process fails
     */
//    public final String decryptMessage(final String message, String keyName) throws UnexpectedException {
//        return decryptMessage(message, Encoding.HEX, keyName);
//    }

    /**
     * Method used for decrypting a message by passing the Encryped String.
     *
     * @param message - Encrypted String value
     * @param encoding - The encoding to be used based on the enumerated type
     * {@link Encoding}
     * @return String message that is decrypted
     * @throws Exception Thrown if the decryption process fails
     */
    public String decryptMessage(final String message, final Encoding encoding, String channel, String appCode) throws UnexpectedException {
    	SecretKey key = getKeyFromKeyStore(OLSS_KEYSTORE, getKeyNameByChannel(appCode));
    	return decryptMessage(message, key, encoding, appCode);
    }

    /**
     * Method used for decrypting a message based on the key passed.
     *
     * @param message - Encrypted String value
     * @param pKey - Secret Key
     * @return String message that is Encrypted
     * @throws Exception Thrown if the decryption process fails
     */
//    public final String decryptMessage(final String message, final SecretKey pKey) throws UnexpectedException {
//        return decryptMessage(message, pKey, Encoding.HEX);
//    }

    /**
     * Method used for decrypting a message based on the key passed and the
     * encoding mechanism.
     *
     * @param message - Encrypted String value
     * @param pKey - Secret Key
     * @param encoding - The encoding to be used based on the enumerated type
     * {@link Encoding}
     * @return String message that is decrypted
     * @throws Exception Thrown if the decryption process fails
     */
    public String decryptMessage(final String message, final SecretKey pKey, final Encoding encoding, final String appCode)
    throws UnexpectedException {
    	LOGGER.info("appCode: " + appCode);
        try {
        	Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, pKey);
            List<String> appCodeList = encryptionConfig.getAppCode();
            byte[] decBytes = null;
            if((appCodeList != null && appCodeList.contains(appCode))){
            	LOGGER.info("15Below message: " + message);
        		//String decodeMsg = java.net.URLDecoder.decode(message, ENCODING_STANDARD);
        		decBytes = java.util.Base64.getDecoder().decode(message);
            }else if(encoding == Encoding.BASE64) {
            		decBytes = Base64.decode(message.toCharArray());
            }else if(encoding == Encoding.BASE64URL) {
            		decBytes = java.util.Base64.getUrlDecoder().decode(message.getBytes());
            }else{
            		decBytes = fromHexString(message);
            }
            byte[] recoveredBytes = cipher.doFinal(decBytes);

            return new String(recoveredBytes, ENCODING_STANDARD);
        } catch (Exception e) {
        		ErrorInfo errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
			throw new UnexpectedException("encryptMessage error: " + e.getMessage(), errorInfo);
        }
    }
    
	/**
	 * This method is used for to convert Encrypted value to HexaDecimal String.
	 *
	 * @param valueToEncode
	 *            - The byte value to be encoded
	 * @return String value
	 */
	private static String toHexString(final byte[] valueToEncode) {
		StringBuffer buffer = new StringBuffer(valueToEncode.length * 2);

		for (int i = 0; i < valueToEncode.length; i++) {
			/*
			 * Look up high nibble char
			 */
			buffer.append(HEX_CHAR[(valueToEncode[i] & CONSTANT_0XFO) >>> SHIFT_TO_FOUR]);

			/*
			 * Look up low nibble char
			 */
			buffer.append(HEX_CHAR[valueToEncode[i] & CONSTANT_0XOF]);
		}

		return buffer.toString();
	}

	/**
	 * Converts the parameterized character to a nibble value.
	 *
	 * @param chr
	 *            - Character value to be conveted
	 * @return Native int type nibble value
	 */
	private static int charToNibble(final char chr) {
		int nibbleValue = -1;

		if ('0' <= chr && chr <= '9') {
			nibbleValue = (chr - '0');
		} else if ('a' <= chr && chr <= 'f') {
			nibbleValue = (chr - 'a' + CONSTANT_0XA);
		} else if ('A' <= chr && chr <= 'F') {
			nibbleValue = (chr - 'A' + CONSTANT_0XA);
		} else {
			throw new IllegalArgumentException("Invalid HEX character: " + chr);
		}

		return nibbleValue;
	}

	/**
	 * This method is used to return actual String that is encrypted from Hexa-decimal string.
	 *
	 * @param str
	 *            - Encoded string
	 * @return Decoded byte array
	 */
	private static byte[] fromHexString(final String str) {
		int stringLength = str.length();

		if ((stringLength & 0x1) != 0) {
			/*
			 * 1=odd & 0=even
			 */
			throw new IllegalArgumentException("fromHexString requires an even number of HEX characters");
		}

		byte[] decodedMessage = new byte[stringLength / 2];
		for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
			int high = charToNibble(str.charAt(i));
			int low = charToNibble(str.charAt(i + 1));
			decodedMessage[j] = (byte) ((high << SHIFT_TO_FOUR) | low);
		}

		return decodedMessage;
	}

	
	/**
	 * @param channel
	 * @return key name from DB (by channel name) or default value (cxmb)
	 */
	private String getKeyNameByChannel(String channel) {
		return securityKeyDao.findOneByAppCodeAndChannelName(MMBConstants.APP_CODE, channel)
				.map(mapping -> mapping.getKeyName())
				.orElse(MMBBizruleConstants.MB_KEY);
	}
	
	private final byte[] appendByteArray (byte[] bytesA, byte[] bytesB) {
    	byte[] finalBytes = new byte[bytesA.length + bytesB.length];
    	System.arraycopy(bytesA, 0, finalBytes, 0, bytesA.length); 
        System.arraycopy(bytesB, 0, finalBytes, bytesA.length, bytesB.length); 
        return finalBytes;
    }
    
    private final byte[] getIV() {
    	SecureRandom randomSecureRandom = new SecureRandom();
    	byte[] iv = new byte[16];
    	
    	randomSecureRandom.nextBytes(iv);

    	return iv;
    }
}
