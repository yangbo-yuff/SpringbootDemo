package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_produce_mjLevels {

    private Integer level;

    private Facility_produce_mjLevelsNeed need;

    private List<Integer> values;

    private Integer time;

}