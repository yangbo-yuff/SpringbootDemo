package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;
import java.util.List;
import java.util.List;
import java.util.List;

@Data
public class Facility_configConfigs {

    private String des;

    private List<Integer> additions;

    private String name;

    private String title;

    private Integer type;

    private List<Facility_configConfigsConditions> conditions;

    private List<Facility_configConfigsLevels> levels;

}