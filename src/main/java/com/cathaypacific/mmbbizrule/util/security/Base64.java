/**
 * Cathay Pacific Confidential and Proprietary.
 * Copyright 2011, Cathay Pacific Airways Limited
 * All rights reserved.
 *
 */
package com.cathaypacific.mmbbizrule.util.security;

import com.cathaypacific.mbcommon.dto.error.ErrorInfo;
import com.cathaypacific.mbcommon.enums.error.ErrorCodeEnum;
import com.cathaypacific.mbcommon.exception.UnexpectedException;

/**
 * Utility class used for encoding and decoding data for encryption and decryption purpose.
 * <p/>
 * It is used to convert binary data to ASCII text which can then be streamed over networks and is ensured to be "padded" for encryption and/or decrytion purposes.
 */
public final class Base64 {
	/**
	 * Hiding the constructor of the utility class.
	 */
	private Base64() {
		throw new UnsupportedOperationException("Do not create an instance of this utility class");
	}

	/**
	 * Encodes the data using the Base64 algorithm and converts the byte data into a character array (whose size is a multiple of four).
	 *
	 * @param data
	 *            - Byte data which is to be encoded
	 * @return The encoded character array
	 */
	public static char[] encode(byte[] data) {
		char out[] = new char[((data.length + 2) / 3) * 4];
		int i = 0;

		for (int index = 0; i < data.length; index += 4) {
			boolean quad = false;
			boolean trip = false;
			int val = 0xff & data[i];
			val <<= 8;

			if (i + 1 < data.length) {
				val |= 0xff & data[i + 1];
				trip = true;
			}

			val <<= 8;
			if (i + 2 < data.length) {
				val |= 0xff & data[i + 2];
				quad = true;
			}

			out[index + 3] = alphabet[quad ? val & 0x3f : 64];
			val >>= 6;

			out[index + 2] = alphabet[trip ? val & 0x3f : 64];
			val >>= 6;

			out[index + 1] = alphabet[val & 0x3f];
			val >>= 6;

			out[index + 0] = alphabet[val & 0x3f];
			i += 3;
		}

		return out;
	}

	/**
	 * Decodes the data using the Base64 algorithm and converts the parameterized character array into an array of equivalent bytes.
	 *
	 * @param data
	 *            - The character array data which is to be decoded
	 * @return The decoded byte array
	 * @throws UnexpectedException 
	 */
	public static byte[] decode(char[] data) throws UnexpectedException {
		int tempLen = data.length;

		for (int ix = 0; ix < data.length; ix++) {
			if (data[ix] > '\377' || codes[data[ix]] < 0) {
				tempLen--;
			}
		}

		int len = (tempLen / 4) * 3;

		if (tempLen % 4 == 3) {
			len += 2;
		}

		if (tempLen % 4 == 2) {
			len++;
		}

		byte out[] = new byte[len];
		int shift = 0;
		int accum = 0;
		int index = 0;

		for (int ix = 0; ix < data.length; ix++) {
			int value = data[ix] <= '\377' ? ((int) (codes[data[ix]])) : -1;

			if (value >= 0) {
				accum <<= 6;
				shift += 6;
				accum |= value;

				if (shift >= 8) {
					shift -= 8;
					out[index++] = (byte) (accum >> shift & 0xff);
				}
			}
		}

		if (index != out.length) {
			ErrorInfo errorInfo = new ErrorInfo(ErrorCodeEnum.ERR_SYSTEM);
			throw new UnexpectedException("Error in Length: Miscalculated data length (wrote " + index + " instead of " + out.length + ")", errorInfo);
		} else {
			return out;
		}
	}

	/**
	 * The ASCII 'character' set used for encoding the incoming data.
	 */
	private static char alphabet[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();

	/**
	 * The byte values used while decoding the incoming data.
	 */
	private static byte codes[];

	static {
		codes = new byte[256];

		for (int i = 0; i < 256; i++) {
			codes[i] = -1;
		}

		for (int i = 65; i <= 90; i++) {
			codes[i] = (byte) (i - 65);
		}

		for (int i = 97; i <= 122; i++) {
			codes[i] = (byte) ((26 + i) - 97);
		}

		for (int i = 48; i <= 57; i++) {
			codes[i] = (byte) ((52 + i) - 48);
		}
		codes[43] = 62;
		codes[47] = 63;
	}
}
