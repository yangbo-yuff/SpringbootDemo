package com.yb.yff.game.jsondb.data.dto;

import lombok.Data;
import java.util.List;

@Data
public class Map_build_customCfgc {

    private String name;

    private Integer type;

    private List<Map_build_customCfgcLevels> levels;

}