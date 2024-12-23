package com.yb.yff.game.jsondb.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
 * @Class: JSon2ObjectUtils
 * @CreatedOn 2024/10/25.
 * @Email: yangboyff@gmail.com
 * @Description: 根据Json生成实体类
 */
public class JSon2EntityUtils {

	public static String outputRootDir = "D:\\Develop\\Java\\SpringbootDemo\\yufenfen-gameServer\\yufenfen-libs-db\\yufenfen-libs-JSonDB\\src\\main\\java";

	public static final String jsonFolderPath = "D:\\Develop\\Java\\SpringbootDemo\\yufenfen-gameServer\\yufenfen-libs-db\\yufenfen-libs-JSonDB\\src\\main\\resources\\data\\conf\\json";
	public static final String jsonFileResourcesPath = "\\\\data\\\\conf\\\\json\\\\";
	public static final String DTORootPackageName = "com.yb.yff.game.jsondb.data.dto";

	/**
	 * 递归读取指定目录及其所有子目录下的文件名
	 *
	 * @param directory 目录路径
	 * @return 文件名列表
	 */
	public static void createEntityByFolder(String directory) {

		File dir = new File(directory);

		if (dir.exists() && dir.isDirectory()) {
			createEntitys(dir);
		} else {
			System.err.println("指定的路径不是一个有效的目录: " + directory);
		}
	}

	/**
	 * 递归遍历目录，创建实体类
	 *
	 * @param dir 当前目录
	 */
	private static void createEntitys(File dir) {
		File[] entries = dir.listFiles();
		if (entries != null) {
			for (File entry : entries) {
				if (entry.isDirectory()) {
					createEntitys(entry);
				} else {
					createEntity(entry);
				}
			}
		}
	}

	/**
	 * 创建实体类
	 *
	 * @param entry
	 * @return
	 */
	public static void createEntity(File entry) {
		String filePath = entry.getAbsolutePath();

		try {
			String content = readFileContent(entry);

			String className = getClassNameByFilepath(filePath);

			String packageName = className.substring(0, className.lastIndexOf("."));
			String classSimpleName = className.substring(className.lastIndexOf(".") + 1);

			Object data = JSON.parseObject(content);

			// 根目录 parentName 为空字符串;
			String parentName = "";

			createEntity((JSONObject) data, packageName, parentName + classSimpleName);
		} catch (Exception e) {
			System.err.println("创建文件失败: " + filePath);
			e.printStackTrace();
		}
	}

	/**
	 * 如果Json包含数组，只支持一重数组
	 * @param data
	 * @param packageName     包名
	 * @param classSimpleName 带上父节点name,避免命名冲突
	 */
	private static void createEntity(JSONObject data, String packageName, String classSimpleName) {
		List<String> fields = new ArrayList<>();
		List<String> imports = new ArrayList<>();
		imports.add("lombok.Data");// @Data注解包

		data.entrySet().forEach(entry -> {
			String propertyName = entry.getKey();

			if (entry.getValue() instanceof JSONArray) { // 列表 处理
				JSONArray array = (JSONArray) entry.getValue();
				if (array.size() == 0) {
					return;
				}
				imports.add("java.util.List");
				String propertyType = "";

				// 拿列表中第一个值,判断是否需要再进入一层进行创建实体类
				Object firstValue = array.get(0);
				if (firstValue instanceof JSONArray) {
					// 只支持一重数组，列表的item还是列表,这是可能个无穷解，用Object代替
					propertyType = "List<Object>";
				} else if (firstValue instanceof JSONObject) {
					// 列表中第一个值是JSONObject,创建实体类
					propertyType = classSimpleName + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

					// 当前parentName + 当前name作为下一层节点的parentName
					createEntity((JSONObject) array.get(0), packageName, propertyType);
//					imports.add(packageName + "." + propertyType);

					propertyType = "List<" + propertyType + ">";
				} else {
					// 父节点Name+列表Name, 避免 列表name 与 Java 类冲突，如 list
					propertyType = "List<" + array.get(0).getClass().getSimpleName() + ">";
				}

				fields.add(propertyType + " " + propertyName);

			} else if (entry.getValue() instanceof JSONObject) { // 自定义封装数据类型 JSONObject 处理
				// 首字母大写, 并带上父节点name,避免命名冲突
				String propertyType = classSimpleName + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

				// 当前name作为下一层节点的parentName
				createEntity((JSONObject) entry.getValue(), packageName, propertyType);
//				imports.add(packageName + "." + propertyType);
				fields.add(propertyType + " " + propertyName);

			} else { // 非自定义封装的数据类型 处理
				String propertyType = entry.getValue().getClass().getSimpleName();
				fields.add(propertyType + " " + propertyName);
			}

		});

		// TODO 根据 classSimpleName, imports 和 propertys 生成实体类代码
		String relativeRath = packageName.replaceAll("\\.", "\\\\");
		String outputDir = outputRootDir + "\\" + relativeRath;
		checkFolder(outputDir);
		EntityGenerator.generateEntity(packageName, classSimpleName, outputDir, fields, imports);
	}

	private static void checkFolder(String folderPath) {
		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}

	/**
	 * 读取文件内容
	 *
	 * @param file 文件对象
	 * @return 文件内容
	 * @throws IOException 如果读取文件时发生错误
	 */
	private static String readFileContent(File file) throws IOException {
		StringBuilder contentBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				contentBuilder.append(line).append("\n");
			}
		}
		return contentBuilder.toString();
	}

	public static String getClassNameByFilepath(String filePath) {
		String[] filePaths = filePath.split(jsonFileResourcesPath);

		String jsonFilePathName = filePaths[1];

		// 砍掉 后缀 .json
		jsonFilePathName = jsonFilePathName.substring(0, jsonFilePathName.length() - 5);

		// 替换 / 为 .
		jsonFilePathName = jsonFilePathName.replaceAll("\\\\", ".");

		// 首字母大写
		String simpleName = jsonFilePathName.substring(jsonFilePathName.lastIndexOf(".") + 1);
		simpleName = simpleName.substring(0, 1).toUpperCase() + simpleName.substring(1);
		jsonFilePathName = jsonFilePathName.substring(0, jsonFilePathName.lastIndexOf(".") + 1) + simpleName;

		return DTORootPackageName + "." + jsonFilePathName;
	}

	public static void main(String[] args) {
		createEntityByFolder(jsonFolderPath);
	}
}
