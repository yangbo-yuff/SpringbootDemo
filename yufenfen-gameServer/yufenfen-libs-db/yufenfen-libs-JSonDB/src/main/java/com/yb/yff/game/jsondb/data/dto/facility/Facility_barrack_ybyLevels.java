package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_barrack_ybyLevels {

    private Integer level;

    private Facility_barrack_ybyLevelsNeed need;

    private List<Integer> values;

    private Integer time;

}