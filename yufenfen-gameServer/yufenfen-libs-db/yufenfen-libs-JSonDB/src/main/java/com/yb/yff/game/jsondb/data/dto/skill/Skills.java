package com.yb.yff.game.jsondb.data.dto.skill;

import lombok.Data;

import java.util.List;

@Data
public class Skills {
    private String des;
    private List<Skill> skills;
}