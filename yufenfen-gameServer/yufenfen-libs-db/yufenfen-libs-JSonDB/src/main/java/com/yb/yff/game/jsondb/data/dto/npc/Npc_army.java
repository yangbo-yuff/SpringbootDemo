package com.yb.yff.game.jsondb.data.dto.npc;

import lombok.Data;
import java.util.List;

@Data
public class Npc_army {

    private String des;

    private List<Npc_armyArmys> armys;

}