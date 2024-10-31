package com.yb.yff.game.jsondb.data.dto.skill.zhudong;

import lombok.Data;

import java.util.List;

@Data
public class Tuji {

    private Integer duration;

    private String des;

    private List<Integer> include_effect;

    private String name;

    private Integer limit;

    private List<Integer> arms;

    private Integer trigger;

    private Integer cfgId;

    private List<TujiLevels> levels;

    private Integer target;

}