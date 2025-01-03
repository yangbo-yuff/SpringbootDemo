package com.yb.yff.game.jsondb.data.dto.npc;

import lombok.Data;

import java.util.List;

@Data
public class Npc_armyArmys {

    private List<Npc_armyArmysArmy> army;

    private String des;

    private Integer soldiers;

}