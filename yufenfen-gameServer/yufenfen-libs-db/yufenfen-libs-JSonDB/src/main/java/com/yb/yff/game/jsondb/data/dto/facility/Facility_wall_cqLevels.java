package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_wall_cqLevels {

    private Integer level;

    private Facility_wall_cqLevelsNeed need;

    private List<Integer> values;

    private Integer time;

}