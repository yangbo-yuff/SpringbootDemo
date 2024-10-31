package com.yb.yff.sb.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Copyright (c) 2024 to 2045  YangBo.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of
 * YangBo. You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license
 * agreement you entered into with YangBo.
 *
 * @author : YangBo
 * @Project: SpringbootDemo
 * @Class: AesEncryptor
 * @CreatedOn 2024/10/19.
 * @Email: yangboyff@gmail.com
 * @Description: AesEncryptor 加解码器
 */
public class AesEncryptor {
	/**
	 * 加密
	 * @param src
	 * @param keyStr
	 * @return
	 * @throws Exception
	 */
	public static String aesCBCEncrypt(byte[] src, String keyStr) throws Exception {
		byte[] key = keyStr.getBytes("UTF-8");
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(key);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

		// Manual Zero Padding
		int blockSize = cipher.getBlockSize();
		int paddedLength = (src.length + blockSize - 1) / blockSize * blockSize;
		byte[] paddedSrc = new byte[paddedLength];
		System.arraycopy(src, 0, paddedSrc, 0, src.length); // Fill the remaining bytes with 0x00

		byte[] encrypted = cipher.doFinal(paddedSrc);
		return bytesToHex(encrypted);
	}

	/**
	 * 解密
	 * @param src
	 * @param keyStr
	 * @return
	 * @throws Exception
	 */
	public static String aesCBCDecrypt(String src, String keyStr) throws Exception {
		byte[] key = keyStr.getBytes("UTF-8");
		byte[] encrypted = hexStringToByteArray(src);
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(key);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

		byte[] decrypted = cipher.doFinal(encrypted);
		byte[] decryptedWithoutPadding = removeTrailingZeros(decrypted);

		return new String(decryptedWithoutPadding, "UTF-8").trim();
	}

	private static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	private static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	private static byte[] removeTrailingZeros(byte[] data) {
		int i = data.length - 1;
		while (i >= 0 && data[i] == 0x00) {
			i--;
		}
		byte[] trimmedData = new byte[i + 1];
		System.arraycopy(data, 0, trimmedData, 0, i + 1);
		return trimmedData;
	}
	public static void main(String[] args) throws Exception {
		String key = "dcb1e561bc014df2";
		String decryptStr = "e09eb643aeea012187614703f9813ebae4d0b239064338924a7403576538ac0a369a64f4a99c4eceea06d4161242f9894e2096a04f31c6bbe4954a54bfe9d67dfe43fc7a2e83c8936ed5689e9253fd1c962ffefaf7cb81ad507e3f6f97b9f106c3d21017bb33bcfd22ee02d51de211591ea7a5f6a913144abeab26904324001ee15f65e9e902fb70718d44c4187b1e4cca8a74138820a3ad6f819bd61fb924ca4af963455d5534641a9510132a1bb3dc";
		String decryptedString= aesCBCDecrypt(decryptStr, key);
		System.out.println("Decrypted data: " + decryptedString);

//		String key = StringUtils.createStrBySize(16);
//
//		// Original plaintext
//		String val = "hello,ase";
//		System.out.println("Original data: " + val);
//
//		// Encrypt
//		String encrypted = aesCBCEncrypt(val.getBytes("UTF-8"), key.getBytes("UTF-8"), key.getBytes("UTF-8"));
//		System.out.println("Encrypted data: " + encrypted);
//
//		// Decrypt
//		String decryptedString= aesCBCDecrypt(encrypted, key.getBytes("UTF-8"), key.getBytes("UTF-8"));
//		System.out.println("Decrypted data: " + decryptedString);
	}
}
