package com.yb.yff.game.jsondb.data.dto.skill.beidong;

import lombok.Data;
import java.util.List;
import java.util.List;

@Data
public class BaizhanjingbingLevels {

    private Integer probability;

    private List<Integer> effect_round;

    private List<Integer> effect_value;

}