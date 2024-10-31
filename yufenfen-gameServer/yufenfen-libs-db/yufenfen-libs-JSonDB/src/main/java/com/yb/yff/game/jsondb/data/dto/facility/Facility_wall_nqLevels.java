package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_wall_nqLevels {

    private Integer level;

    private Facility_wall_nqLevelsNeed need;

    private List<Integer> values;

    private Integer time;

}