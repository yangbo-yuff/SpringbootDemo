package com.yb.yff.game.jsondb.data.dto.general;

import lombok.Data;
import java.util.List;

@Data
public class General_armsArms {

    private List<Integer> harm_ratio;

    private General_armsArmsCondition condition;

    private General_armsArmsChange_cost change_cost;

    private String name;

    private Integer id;

}