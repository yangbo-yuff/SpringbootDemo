package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_produce_mf {

    private String des;

    private List<Integer> additions;

    private String name;

    private String title;

    private Integer type;

    private List<Facility_produce_mfConditions> conditions;

    private List<Facility_produce_mfLevels> levels;

}