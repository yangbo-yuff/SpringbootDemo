package com.yb.yff.game.jsondb.data.dto.skill.zuiji;

import lombok.Data;

import java.util.List;

@Data
public class Zhongzhan {

    private Integer duration;

    private String des;

    private List<Integer> include_effect;

    private String name;

    private Integer limit;

    private List<Integer> arms;

    private Integer trigger;

    private Integer cfgId;

    private List<ZhongzhanLevels> levels;

    private Integer target;

}