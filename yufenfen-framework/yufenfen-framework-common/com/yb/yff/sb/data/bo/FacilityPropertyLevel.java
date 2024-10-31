package com.yb.yff.sb.data.bo;

import lombok.Data;

import java.util.List;

@Data
public class FacilityPropertyLevel {
	private Integer level;
	private FacilityPropertyLevelNeed need;
	private List<Integer> values;
	private Integer time;
}
