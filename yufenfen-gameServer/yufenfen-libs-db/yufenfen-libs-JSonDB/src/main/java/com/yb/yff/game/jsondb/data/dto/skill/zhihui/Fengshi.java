package com.yb.yff.game.jsondb.data.dto.skill.zhihui;

import lombok.Data;

import java.util.List;

@Data
public class Fengshi {

    private Integer duration;

    private String des;

    private List<Integer> include_effect;

    private String name;

    private Integer limit;

    private List<Integer> arms;

    private Integer trigger;

    private Integer cfgId;

    private List<FengshiLevels> levels;

    private Integer target;

}