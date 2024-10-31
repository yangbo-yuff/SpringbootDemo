package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_camp_wuLevels {

    private Integer level;

    private Facility_camp_wuLevelsNeed need;

    private List<Integer> values;

    private Integer time;

}