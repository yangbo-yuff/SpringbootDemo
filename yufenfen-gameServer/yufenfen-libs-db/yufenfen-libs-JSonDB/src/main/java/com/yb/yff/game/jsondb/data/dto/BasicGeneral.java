package com.yb.yff.game.jsondb.data.dto;

import lombok.Data;

@Data
public class BasicGeneral {

    private String des;

    private Integer reclamation_time;

    private Integer recovery_physical_power;

    private Integer pr_point;

    private Integer limit;

    private Integer reclamation_cost;

    private Integer draw_general_cost;

    private Integer physical_power_limit;

    private Integer cost_physical_power;

}