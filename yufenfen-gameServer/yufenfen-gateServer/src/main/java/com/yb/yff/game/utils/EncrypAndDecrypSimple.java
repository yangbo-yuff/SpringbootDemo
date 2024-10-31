//package com.yb.yff.game.utils;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.codec.binary.Base64;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.nio.charset.StandardCharsets;
//import java.util.zip.Deflater;
//import java.util.zip.GZIPInputStream;
//import java.util.zip.GZIPOutputStream;
//
///**
// * Copyright (c) 2024 to 2045  YangBo.
// * All rights reserved.
// * <p>
// * This software is the confidential and proprietary information of
// * YangBo. You shall not disclose such Confidential Information and
// * shall use it only in accordance with the terms of the license
// * agreement you entered into with YangBo.
// *
// * @author : YangBo
// * @Project: SpringbootDemo
// * @Class: EncrypAndDecrypSimple
// * @CreatedOn 2024/10/14.
// * @Email: yangboyff@gmail.com
// * @Description: 用于websocket通信的 简单加解密
// */
//
//@Slf4j
//public class EncrypAndDecrypSimple {
//	static Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
//	/**
//	 * 压缩文本
//	 * */
//	public static String compress(String str) {
//		if (str == null || str.isEmpty()) {
//			return str;
//		}
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		GZIPOutputStream gzip = null;
//		try {
//			gzip = new GZIPOutputStream(out){
//				{
//					this.def = deflater;
//				}
//			};
//			gzip.write(str.getBytes(StandardCharsets.UTF_8));
//		} catch (Exception e) {
//			log.info("GZipUtils.compress error" + e);
//		} finally {
//			if (gzip != null) {
//				try {
//					gzip.close();
//				} catch (Exception e) {
//					log.info("GZipUtils.compress gzip close error" +
//							e);
//				}
//			}
//		}
//		return new String(Base64.encodeBase64(out.toByteArray()), StandardCharsets.UTF_8);
//	}
//
//	/**
//	 * 解压文本
//	 * */
//	public static String decompress(byte[] compressed) {
//
//		ByteArrayOutputStream out = null;
//		ByteArrayInputStream in = null;
//		GZIPInputStream gZipIns = null;
//		String decompressedStr = null;
//		try {
//			out = new ByteArrayOutputStream();
//			in = new ByteArrayInputStream(compressed);
//			gZipIns = new GZIPInputStream(in);
//			byte[] buffer = new byte[1024];
//			int offset;
//			while ((offset = gZipIns.read(buffer)) != -1) {
//				out.write(buffer, 0, offset);
//			}
//			decompressedStr = out.toString();
//		} catch (Exception e) {
//			log.info("GZipUtils.decompress error" + e);
//		} finally {
//			if (gZipIns != null) {
//				try {
//					gZipIns.close();
//				} catch (Exception e) {
//					log.info("GZipUtils.decompress gZipIns close error" + e);
//				}
//			}
//			if (in != null) {
//				try {
//					in.close();
//				} catch (Exception e) {
//					log.info("GZipUtils.decompress in close error" + e);
//				}
//			}
//			if (out != null) {
//				try {
//					out.close();
//				} catch (Exception e) {
//					log.info("GZipUtils.decompress out close error" + e);
//				}
//			}
//		}
//		return decompressedStr;
//	}
//
//	public static String unZip(byte[] data) throws IOException {
//		// 将输入的字节数组转换为小端序的 ByteBuffer
////		ByteBuffer buffer = ByteBuffer.wrap(data);
////		buffer.order(ByteOrder.LITTLE_ENDIAN);
//
//		// 将小端序的字节写入 ByteArrayOutputStream
//		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//		while (buffer.hasRemaining()) {
//			byteArrayOutputStream.write(buffer.get());
//		}
//
//		// 将 ByteArrayOutputStream 转换为 ByteArrayInputStream
//		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
//
//		// 使用 GZIPInputStream 进行解压
//		try (GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {
//			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//			byte[] bufferArray = new byte[1024];
//			int len;
//			while ((len = gzipInputStream.read(bufferArray)) > 0) {
//				outputStream.write(bufferArray, 0, len);
//			}
//			return outputStream.toString();//.toByteArray();
//		}
//	}
//
////	/**
////	 * 加密
////	 * @param data
////	 * @returns {string}
////	 */
////	public static String encrypt(Object data) {
////		var key = crypto.enc.Utf8.parse(this._key);
////		var iv = crypto.enc.Utf8.parse(this._key);
////
////		if (typeof(data) == 'object') {
////			data = JSON.stringify(data);
////		}
////		let srcs = crypto.enc.Utf8.parse(data);
////		let encrypted = crypto.AES.encrypt(srcs, key, {iv:iv, mode:crypto.mode.CBC, padding:crypto.pad.ZeroPadding});
////
////
////		return encrypted.ciphertext.toString()
////	}
////
////
////	public static String decrypt(String message) {
////		var key = crypto.enc.Utf8.parse(this._key);
////		var iv = crypto.enc.Utf8.parse(this._key);
////
////		let encryptedHexStr = crypto.enc.Hex.parse(message);
////		let srcs = crypto.Base64.stringify(encryptedHexStr);
////		let decrypt = crypto.AES.decrypt(srcs, key, {iv:iv, mode:crypto.mode.CBC, padding:crypto.pad.ZeroPadding});
////		// console.log("decrypt:", decrypt);
////
////		let decryptedStr = decrypt.toString(crypto.enc.Utf8);
////		// console.log("decryptedStr 1111:", typeof(decryptedStr), decryptedStr, decryptedStr.length);
////		var str = decryptedStr.replaceAll("\u0000", "")
////
////		// console.log("decryptedStr 2222:", typeof(str), str, str.length);
////		return str;
////	}
//}
