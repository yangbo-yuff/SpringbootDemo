package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_general_jcLevels {

    private Integer level;

    private Facility_general_jcLevelsNeed need;

    private List<Integer> values;

    private Integer time;

}