package com.yb.yff.game.jsondb.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

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
 * @Class: JSonConfigLoader
 * @CreatedOn 2024/10/26.
 * @Email: yangboyff@gmail.com
 * @Description: Json配置文件加载器
 */
public class JSonConfigLoader {

	/**
	 * 加载所有json配置文件,
	 * 请先执行JSon2EntityUtils.createEntityByFolder，创建对应的实体类，再执行加载
	 * @param resources
	 * @param jSonConfigs
	 */
	public static void loadAllJsonConfig(Resource[] resources, HashMap<String, Object> jSonConfigs) {
		for (Resource resource : resources) {
			String fileName = resource.getFilename();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
				StringBuilder content = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					content.append(line);
				}
				String className = JSon2EntityUtils.getClassNameByFilepath(resource.getFile().getPath());
				Class<?> clazz = Class.forName(className);
				;

				Object data = JSON.parseObject(content.toString(), clazz);
				jSonConfigs.put(clazz.getSimpleName(), data);
				System.out.println("Successfully read JSON from File: " + fileName);
			} catch (Exception e) {
				// 打印错误信息，方便调试
				System.err.println("Error reading JSON from " + fileName + ": " + e.getMessage());
			}
		}
	}
}
