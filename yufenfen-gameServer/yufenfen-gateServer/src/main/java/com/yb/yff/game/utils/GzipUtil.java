package com.yb.yff.game.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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
 * @Class: GzipUtil
 * @CreatedOn 2024/10/14.
 * @Email: yangboyff@gmail.com
 * @Description: Gzip压缩工具
 */

@Slf4j
public class GzipUtil {

	/**
	 * 压缩数据为GZIP格式.
	 *
	 * @param data 要压缩的数据
	 * @return 压缩后的字节数组和可能的异常
	 */
	public static byte[] zip(byte[] data) throws IOException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); GZIPOutputStream gzipOutputStream = new GZIPOutputStream(baos) {
			{
				this.def = new Deflater(Deflater.BEST_COMPRESSION);
			}
		}) {
			gzipOutputStream.write(data);
			gzipOutputStream.finish(); // 完成gzip流的压缩并准备关闭
			return baos.toByteArray();
		}
	}

	public static String unZip(byte[] data) throws IOException {
		if (data == null || data.length == 0) {
			return "";
		}

		// 按小端序读取字节流
		ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
		byte[] littleEndianData = new byte[byteBuffer.remaining()];
		byteBuffer.get(littleEndianData);

		try (ByteArrayInputStream bis = new ByteArrayInputStream(littleEndianData);
		     GZIPInputStream gis = new GZIPInputStream(bis);
		     ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

			byte[] buffer = new byte[8192];
			int len;
			while ((len = gis.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
			}

			return bos.toString();//.toByteArray();
		}
	}

		public static String unZip2(byte[] data) throws IOException {
		// 将输入的字节数组转换为小端序的 ByteBuffer
		ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN);

		// 将小端序的字节写入 ByteArrayOutputStream
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		while (buffer.hasRemaining()) {
			byteArrayOutputStream.write(buffer.get());
		}

		// 将 ByteArrayOutputStream 转换为 ByteArrayInputStream
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

		// 使用 GZIPInputStream 进行解压
		try (GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] bufferArray = new byte[1024];
			int len;
			while ((len = gzipInputStream.read(bufferArray)) > 0) {
				outputStream.write(bufferArray, 0, len);
			}
			return outputStream.toString();//.toByteArray();
		}
	}
}
