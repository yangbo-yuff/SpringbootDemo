package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_camp_shuLevels {

    private Integer level;

    private Facility_camp_shuLevelsNeed need;

    private List<Integer> values;

    private Integer time;

}