package com.yb.yff.game.jsondb.data.dto.skill;

import lombok.Data;

@Data
public class Skill_outline {

    private Skill_outlineTrigger_type trigger_type;

    private Skill_outlineTarget_type target_type;

    private Skill_outlineEffect_type effect_type;

}