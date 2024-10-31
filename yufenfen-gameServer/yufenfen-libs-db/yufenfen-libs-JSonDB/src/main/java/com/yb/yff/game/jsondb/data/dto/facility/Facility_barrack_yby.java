package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_barrack_yby {

    private String des;

    private List<Integer> additions;

    private String name;

    private String title;

    private Integer type;

    private List<Facility_barrack_ybyConditions> conditions;

    private List<Facility_barrack_ybyLevels> levels;

}