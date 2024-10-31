package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_army_jfyLevels {

    private Integer level;

    private Facility_army_jfyLevelsNeed need;

    private List<Integer> values;

    private Integer time;

}