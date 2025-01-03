package com.yb.yff.game.jsondb.data.dto.skill.beidong;

import lombok.Data;

import java.util.List;

@Data
public class Baizhanjingbing {

    private Integer duration;

    private String des;

    private List<Integer> include_effect;

    private String name;

    private Integer limit;

    private List<Integer> arms;

    private Integer trigger;

    private Integer cfgId;

    private List<BaizhanjingbingLevels> levels;

    private Integer target;

}