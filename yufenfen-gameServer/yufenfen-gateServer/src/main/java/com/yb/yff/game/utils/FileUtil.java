package com.yb.yff.game.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

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
 * @Class: FileUtil
 * @CreatedOn 2024/10/15.
 * @Email: yangboyff@gmail.com
 * @Description: 文件工具类
 */
public class FileUtil {

	/**
	 * 保存字节数组到指定文件
	 *
	 * @param data 要保存的字节数组
	 * @param path 文件路径
	 * @throws IOException 如果写入文件时发生错误
	 */
	public static void saveBytesToFile(byte[] data, String path) throws IOException {
		Path filePath = Path.of(path);
		Files.write(filePath, data, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
	}

	/**
	 * 保存字符串到指定文件
	 *
	 * @param content 要保存的字符串
	 * @param path    文件路径
	 * @throws IOException 如果写入文件时发生错误
	 */
	public static void saveStringToFile(String content, String path) throws IOException {
		Path filePath = Path.of(path);
		Files.writeString(filePath, content, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
	}

	/**
	 * 将文件内容读取为字符串
	 *
	 * @param path 文件路径
	 * @return 文件内容字符串
	 * @throws IOException 如果读取文件时发生错误
	 */
	public static String readFileToString(String path) throws IOException {
		Path filePath = Path.of(path);
		return Files.readString(filePath);
	}

	/**
	 * 将文件内容读取为字节数组
	 *
	 * @param path 文件路径
	 * @return 文件内容字节数组
	 * @throws IOException 如果读取文件时发生错误
	 */
	public static byte[] readFileToBytes(String path) throws IOException {
		Path filePath = Path.of(path);
		return Files.readAllBytes(filePath);
	}
}

