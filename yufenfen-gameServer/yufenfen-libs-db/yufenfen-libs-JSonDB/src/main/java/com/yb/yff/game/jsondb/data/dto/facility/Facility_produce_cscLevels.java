package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_produce_cscLevels {

    private Integer level;

    private Facility_produce_cscLevelsNeed need;

    private List<Integer> values;

    private Integer time;

}