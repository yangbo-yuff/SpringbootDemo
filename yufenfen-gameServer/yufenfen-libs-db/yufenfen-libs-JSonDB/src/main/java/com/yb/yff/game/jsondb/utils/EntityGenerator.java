package com.yb.yff.game.jsondb.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
 * @Class: EntityGenerator
 * @CreatedOn 2024/10/25.
 * @Email: yangboyff@gmail.com
 * @Description: 文件生成器
 */
public class EntityGenerator {
	private static final String TEMPLATE_DIR = "D:\\Develop\\Java\\SpringbootDemo\\yufenfen-gameServer\\yufenfen-libs-db\\yufenfen-libs-JSonDB\\src\\main\\resources\\templates";
	private static final String TEMPLATE_NAME = "entity.ftl";

	public static void generateEntity(String packageName, String entityName, String outputDir, List<String> fields, List<String> importPackages) {
		try {
			// 配置FreeMarker
			Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
			cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_DIR));

			// 加载模板
			Template template = cfg.getTemplate(TEMPLATE_NAME);

			// 准备数据模型
			Map<String, Object> dataModel = new HashMap<>();
			dataModel.put("packageName", packageName);
			dataModel.put("entity", entityName);
			dataModel.put("fields", fields);
			dataModel.put("importPackages", importPackages);

			// 创建输出文件
			File outputFile = new File(outputDir, entityName + ".java");
			try (Writer out = new FileWriter(outputFile)) {
				// 处理模板并生成文件
				template.process(dataModel, out);
			}

			System.out.println("实体文件生成成功: " + outputFile.getAbsolutePath());

		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			// 示例数据
			String packageName = "com.example";
			String entityName = "User";
			List<String> importPackages = List.of("lombok.Data");
			List<String> fields = new ArrayList<>();
			fields.add("Long id");
			fields.add("String name");
			fields.add("Integer age");
			String outputDir = "src/main/java/com/example"; // 输出目录

			// 确保输出目录存在
			File dir = new File(outputDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			// 生成实体类文件
			generateEntity(packageName, entityName, outputDir, fields, importPackages);

			System.out.println("Entity file generated successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}



//class Field {
//	private String fieldType;
//	private String fieldName;
//
//	public Field(String fieldType, String fieldName) {
//		if (fieldType == null || fieldName == null) {
//			throw new IllegalArgumentException("fieldType and fieldName must not be null");
//		}
//		this.fieldType = fieldType;
//		this.fieldName = fieldName;
//	}
//
//	public String getFieldType() {
//		return fieldType;
//	}
//
//	public String getFieldName() {
//		return fieldName;
//	}
//}
