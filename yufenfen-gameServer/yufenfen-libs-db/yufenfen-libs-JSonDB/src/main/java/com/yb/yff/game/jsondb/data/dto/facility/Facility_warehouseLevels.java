package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_warehouseLevels {

    private Integer level;

    private Facility_warehouseLevelsNeed need;

    private List<Integer> values;

    private Integer time;

}