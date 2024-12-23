package com.yb.yff.game.jsondb.data.dto;

import lombok.Data;

@Data
public class BasicConscript {

    private Integer cost_gold;

    private String des;

    private Integer cost_grain;

    private Integer cost_wood;

    private Integer cost_time;

    private Integer cost_stone;

    private Integer cost_iron;

    public BasicConscript(){

    }
    public BasicConscript(Integer initVal){
        cost_gold = initVal;
        cost_grain = initVal;
        cost_wood = initVal;
        cost_stone = initVal;
        cost_iron = initVal;
        cost_time = initVal;
    }
}