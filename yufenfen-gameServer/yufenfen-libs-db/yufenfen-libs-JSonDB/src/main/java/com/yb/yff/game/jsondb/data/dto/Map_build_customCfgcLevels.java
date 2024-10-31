package com.yb.yff.game.jsondb.data.dto;

import lombok.Data;

@Data
public class Map_build_customCfgcLevels {

    private Map_build_customCfgcLevelsResult result;

    private Integer durable;

    private Integer level;

    private Map_build_customCfgcLevelsNeed need;

    private Integer time;

    private Integer defender;

}