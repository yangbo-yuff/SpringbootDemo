package com.yb.yff.game.utils;

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
 * @Class: MysqlBatisPlusMapperCodeGenerator
 * @CreatedOn 2024/10/6.
 * @Email: yangboyff@gmail.com
 * @Description: Mapper代码生成器类
 */

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class MysqlBatisPlusMapperCodeGenerator {
	static final String outputRootDir = System.getProperty("user.dir") + "/yufenfen-gameServer/yufenfen-libs-db/yufenfen-libs-db-mysql";

	public static void main(String[] args) {
		FastAutoGenerator.create("jdbc:mysql://localhost:3306/slgGameDB?autoReconnect=true&useServerPreparedStmts=false&rewriteBatchedStatements=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true&serverTimezone=Asia/Shanghai",
						"root", "123456")
				.globalConfig(builder -> {
					builder.author("yangbo") // 设置作者
							.outputDir(outputRootDir + "/src/main/java/com/yb/yff/game/myBatisPlusMapperGeneratorFile") // 指定输出目录
							.disableOpenDir() // 生成后不打开输出目录
							.dateType(DateType.ONLY_DATE); // 使用 Date 类型而不是 LocalDateTime
				})
				.packageConfig(builder -> {
					builder.parent("com.yb.yff.game") // 设置父包名
							.moduleName("myBatisPlusMapperGeneratorFile") // 设置模块名
							.pathInfo(getPathInfo()); // 设置XML生成路径
				})
				.strategyConfig(builder -> {
					builder.addTablePrefix("tb_") // 过滤掉表前缀 "tb_"
							.entityBuilder()
							.naming(NamingStrategy.underline_to_camel) // 表名转驼峰命名
							.columnNaming(NamingStrategy.underline_to_camel) // 字段名生转驼峰命名
							.formatFileName("%sEntity") // 实体类命名格式：表名 + "Entity"
							.enableLombok(); // 启用 Lombok
				})
				.templateConfig(builder -> {
					builder.entity("/templates/entity.java"); // 配置自定义模板
				})
				.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
				.execute();

	}

	private static Map<OutputFile, String> getPathInfo() {
		Map<OutputFile, String> pathInfo = new HashMap<>();
		// service 输出路径
		pathInfo.put(OutputFile.controller, outputRootDir + "/src/main/java/com/yb/yff/game/myBatisPlusMapperGeneratorFile/controller");
		// service 输出路径
		pathInfo.put(OutputFile.service, outputRootDir + "/src/main/java/com/yb/yff/game/myBatisPlusMapperGeneratorFile/service");
		pathInfo.put(OutputFile.serviceImpl, outputRootDir + "/src/main/java/com/yb/yff/game/myBatisPlusMapperGeneratorFile/service/impl");
		// entitys 输出路径
		pathInfo.put(OutputFile.entity, outputRootDir + "/src/main/java/com/yb/yff/game/myBatisPlusMapperGeneratorFile/entity");
		// Mapper 接口输出路径
		pathInfo.put(OutputFile.mapper, outputRootDir + "/src/main/java/com/yb/yff/game/myBatisPlusMapperGeneratorFile/mapper");
		// Mapper XML 文件输出路径
		pathInfo.put(OutputFile.xml, outputRootDir + "/src/main/resources/myBatisPlusMapperGeneratorFile/mapper");
		return pathInfo;
	}
}
