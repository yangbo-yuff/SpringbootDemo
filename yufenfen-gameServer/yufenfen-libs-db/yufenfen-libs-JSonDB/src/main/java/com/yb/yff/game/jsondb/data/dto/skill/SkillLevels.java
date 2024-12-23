package com.yb.yff.game.jsondb.data.dto.skill;

import lombok.Data;

import java.util.List;

@Data
public class SkillLevels {

    private Integer probability;

    private List<Integer> effect_round;

    private List<Integer> effect_value;

}