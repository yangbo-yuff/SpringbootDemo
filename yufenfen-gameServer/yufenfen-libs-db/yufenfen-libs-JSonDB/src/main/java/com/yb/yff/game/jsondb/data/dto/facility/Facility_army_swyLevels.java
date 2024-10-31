package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_army_swyLevels {

    private Integer level;

    private Facility_army_swyLevelsNeed need;

    private List<Integer> values;

    private Integer time;

}