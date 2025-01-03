package com.yb.yff.game.jsondb.data.dto;

import lombok.Data;

import java.util.List;

@Data
public class MapRes {

    private Integer safeDistance;

    private List<List<Integer>> resList;

    private Integer w;

    private Integer h;

}