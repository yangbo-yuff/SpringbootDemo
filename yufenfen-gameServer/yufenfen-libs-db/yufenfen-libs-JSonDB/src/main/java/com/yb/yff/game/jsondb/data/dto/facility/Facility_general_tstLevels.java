package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_general_tstLevels {

    private Integer level;

    private Facility_general_tstLevelsNeed need;

    private List<Integer> values;

    private Integer time;

}