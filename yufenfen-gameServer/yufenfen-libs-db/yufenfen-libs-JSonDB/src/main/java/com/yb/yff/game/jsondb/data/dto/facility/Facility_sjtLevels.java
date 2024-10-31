package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_sjtLevels {

    private Integer level;

    private Facility_sjtLevelsNeed need;

    private List<Integer> values;

    private Integer time;

}