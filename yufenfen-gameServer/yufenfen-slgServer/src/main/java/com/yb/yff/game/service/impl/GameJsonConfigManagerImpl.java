package com.yb.yff.game.service.impl;

import com.yb.yff.game.jsondb.data.dto.MapRes;
import com.yb.yff.game.jsondb.data.dto.facility.*;
import com.yb.yff.game.jsondb.utils.JSonConfigLoader;
import com.yb.yff.game.service.IGameJsonConfigManager;
import com.yb.yff.game.utils.CityPositionUtils;
import com.yb.yff.sb.data.bo.FacilitiesBO;
import com.yb.yff.sb.data.bo.FacilityBO;
import com.yb.yff.sb.data.bo.FacilityPropertyAddition;
import com.yb.yff.sb.data.bo.FacilityPropertyBO;
import com.yb.yff.sb.data.dto.city.NationalMap;
import com.yb.yff.sb.data.dto.city.NationalMaps;
import com.yb.yff.sb.data.dto.city.PositionDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
 * @Class: GameJsonConfigManagerImpl
 * @CreatedOn 2024/10/29.
 * @Email: yangboyff@gmail.com
 * @Description: dd
 */
@Service
public class GameJsonConfigManagerImpl implements IGameJsonConfigManager {
	@Value("classpath:data/conf/json/**/*.json")
	private Resource[] resources;

	private HashMap<String, Object> jSonConfigs = new HashMap();


	@PostConstruct
	public void readAllJsonData() {
		JSonConfigLoader.loadAllJsonConfig(resources, jSonConfigs);

		// 加工数据
		processingData();
	}


	/**
	 * 读取JSON配置
	 *
	 * @param clazz
	 * @return
	 */
	@Override
	public <T> T getJsonConfig(Class<T> clazz) {
		String key = clazz.getSimpleName();
		return (T) jSonConfigs.get(key);
	}


	private void processingData() {
		// 加工地图数据
		processingMapData();

		// 加工设施数据
		processingFacilityData();
	}

	private void processingMapData() {
		// 处理地图资源数据
		MapRes mapRes = getJsonConfig(MapRes.class);
		CityPositionUtils.MapWith = mapRes.getW();
		CityPositionUtils.MapHeight = mapRes.getH();
		CityPositionUtils.SafeDistance = mapRes.getSafeDistance();
		AtomicInteger index = new AtomicInteger();
		List<NationalMap> nMapRes = new ArrayList<>();
		mapRes.getResList().stream().forEach(item -> {
			NationalMap map = new NationalMap();
			PositionDTO pos = new PositionDTO();
			pos.setX(index.get() % CityPositionUtils.MapWith);
			pos.setY(index.get() / CityPositionUtils.MapHeight);
			map.setId(index.get());
			map.setPos(pos);
			map.setType(item.get(0));
			map.setLevel(item.get(1));
			nMapRes.add(map);

			index.getAndIncrement();
		});

		// 缓存加工后的数据
		NationalMaps nationalMaps = new NationalMaps();
		nationalMaps.setNMaps(nMapRes);
		addJsonConfig(nationalMaps);

		// 移除未加工的数据
		rmJsonConfig(MapRes.class);
	}

	/**
	 *
	 */
	private void processingFacilityData() {
		List<Class<?>> processClasses = new ArrayList<>();
		processClasses.add(Facility_army_jfy.class);
		processClasses.add(Facility_army_jjy.class);
		processClasses.add(Facility_army_swy.class);
		processClasses.add(Facility_army_tby.class);
		processClasses.add(Facility_barrack_by.class);
		processClasses.add(Facility_barrack_yby.class);
		processClasses.add(Facility_camp_han.class);
		processClasses.add(Facility_camp_qun.class);
		processClasses.add(Facility_camp_shu.class);
		processClasses.add(Facility_camp_wei.class);
		processClasses.add(Facility_camp_wu.class);
		processClasses.add(Facility_city.class);
		processClasses.add(Facility_fct.class);
		processClasses.add(Facility_general_jc.class);
		processClasses.add(Facility_general_tst.class);
		processClasses.add(Facility_market.class);
		processClasses.add(Facility_mbs.class);
		processClasses.add(Facility_produce_csc.class);
		processClasses.add(Facility_produce_fmc.class);
		processClasses.add(Facility_produce_ltc.class);
		processClasses.add(Facility_produce_mf.class);
		processClasses.add(Facility_produce_mj.class);
		processClasses.add(Facility_sjt.class);
		processClasses.add(Facility_wall_cq.class);
		processClasses.add(Facility_wall_nq.class);
		processClasses.add(Facility_warehouse.class);

		/*******************************************/
		/************** 初始化 设备列表 **************/
		/*******************************************/
		Map<Integer, FacilityPropertyBO> facilityPropertyMaps = new HashMap<>();

		List<Facility_additionList> additionList = getJsonConfig(Facility_addition.class).getList();

		for (Class<?> clazz : processClasses) {
			FacilityPropertyBO facilityPropertyBO = processingMapData(facilityPropertyMaps, clazz);
			List<FacilityPropertyAddition> additionDetails = new ArrayList<>();
			facilityPropertyBO.getAdditions().stream().forEach(item -> {
				FacilityPropertyAddition addition = new FacilityPropertyAddition();
				BeanUtils.copyProperties(additionList.get(item-1), addition);
				additionDetails.add(addition);
			});
			facilityPropertyBO.setAddition_details(additionDetails);
		}


		/*******************************************/
		/************** 初始化 设备列表 **************/
		/*******************************************/
		Facility facility = getJsonConfig(Facility.class);
		List<FacilityFacilityList> facilityListChild = facility.getFacilityList();

		List<FacilityBO> facilitieList = new ArrayList<>();

		facilityListChild.stream().forEach(item -> {
			FacilityBO facilitie = new FacilityBO();
			facilitie.setType(item.getType());
			facilitie.setName(item.getName());
			facilitie.setLevel(0);
			facilitie.setUp_time(0);
			facilitie.setProperty(facilityPropertyMaps.get(item.getType()));
			facilitieList.add(facilitie);
		});

		FacilitiesBO cityFacilitiesBO = new FacilitiesBO();
		cityFacilitiesBO.setFacilities(facilitieList);

		addJsonConfig(cityFacilitiesBO);


		// 移除未加工的数据
		for (Class<?> clazz : processClasses) {
			rmJsonConfig(clazz);
		}
		rmJsonConfig(Facility.class);
	}

	private <T> FacilityPropertyBO processingMapData(Map<Integer, FacilityPropertyBO> facilityPropertyMaps, Class<T> clazz) {
		T data = getJsonConfig(clazz);
		FacilityPropertyBO facilityPropertyBO = new FacilityPropertyBO();
		BeanUtils.copyProperties(data, facilityPropertyBO);
		facilityPropertyMaps.put(facilityPropertyBO.getType(), facilityPropertyBO);

		return facilityPropertyBO;
	}

	private <T> void rmJsonConfig(Class<T> clazz) {
		String key = clazz.getSimpleName();
		jSonConfigs.remove(key);
	}

	private <T> void addJsonConfig(T data) {
		String key = data.getClass().getSimpleName();
		jSonConfigs.put(key, data);
	}
}
