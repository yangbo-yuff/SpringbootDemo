package com.yb.yff.game.jsondb.data.dto.skill;

import lombok.Data;

import java.util.List;

@Data
public class Skill {

    private Integer duration;

    private String des;

    private List<Integer> include_effect;

    private String name;

    private Integer limit;

    private List<Integer> arms;

    private Integer trigger;

    private Integer cfgId;

    private List<SkillLevels> levels;

    private Integer target;

}