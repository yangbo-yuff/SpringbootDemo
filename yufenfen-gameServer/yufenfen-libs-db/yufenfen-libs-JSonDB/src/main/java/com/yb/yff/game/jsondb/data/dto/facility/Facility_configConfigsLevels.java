package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;
import java.util.List;

@Data
public class Facility_configConfigsLevels {

    private Integer level;

    private Facility_configConfigsLevelsNeed need;

    private List<Integer> values;

    private Integer time;

}