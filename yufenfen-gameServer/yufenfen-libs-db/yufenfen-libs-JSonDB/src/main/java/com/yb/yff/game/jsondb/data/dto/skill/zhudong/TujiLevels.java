package com.yb.yff.game.jsondb.data.dto.skill.zhudong;

import lombok.Data;

import java.util.List;

@Data
public class TujiLevels {

    private Integer probability;

    private List<Integer> effect_round;

    private List<Integer> effect_value;

}