package com.yb.yff.game.jsondb.data.dto;

import lombok.Data;
import java.util.List;

@Data
public class Map_build {

    private List<Map_buildCfg> cfg;

    private String title;

}