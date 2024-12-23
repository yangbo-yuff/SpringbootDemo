package com.yb.yff.game.jsondb.data.dto;

import lombok.Data;
import java.util.List;

@Data
public class Map_build_custom {

    private List<Map_build_customCfgc> cfgc;

    private String title;

}