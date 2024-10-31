package com.yb.yff.game.jsondb.data.dto.general;

import lombok.Data;

import java.util.List;

@Data
public class GeneralList {

    private Integer speed_grow;

    private Integer cost;

    private Integer star;

    private Integer force_grow;

    private Integer defense_grow;

    private Integer destroy_grow;

    private Integer probability;

    private Integer destroy;

    private Integer speed;

    private Integer camp;

    private Integer defense;

    private String name;

    private Integer strategy_grow;

    private Integer force;

    private List<Integer> arms;

    private Integer cfgId;

    private Integer strategy;

    private Integer physical_power_limit;

    private Integer cost_physical_power;

}