package com.yb.yff.game.jsondb.data.dto.general;

import lombok.Data;
import java.util.List;

@Data
public class General_arms {

    private List<General_armsArms> arms;

    private String title;

}