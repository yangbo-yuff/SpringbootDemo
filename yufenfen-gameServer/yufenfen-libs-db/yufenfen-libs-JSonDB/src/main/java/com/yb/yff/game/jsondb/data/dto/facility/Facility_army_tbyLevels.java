package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_army_tbyLevels {

    private Integer level;

    private Facility_army_tbyLevelsNeed need;

    private List<Integer> values;

    private Integer time;

}