package com.yb.yff.game.jsondb.data.dto.skill.zuiji;

import lombok.Data;
import java.util.List;
import java.util.List;

@Data
public class ZhongzhanLevels {

    private Integer probability;

    private List<Integer> effect_round;

    private List<Integer> effect_value;

}