package com.yb.yff.game.data.dto.facility.config;

import lombok.Data;

import java.util.List;

@Data
public class FacilityPropertyLevel {
	private Integer level;
	private FacilityPropertyLevelNeedDTO need;
	private List<Integer> values;
	private Integer time;
}
