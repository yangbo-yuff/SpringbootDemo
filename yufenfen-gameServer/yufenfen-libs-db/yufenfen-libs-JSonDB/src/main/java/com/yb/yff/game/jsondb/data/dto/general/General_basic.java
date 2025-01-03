package com.yb.yff.game.jsondb.data.dto.general;

import lombok.Data;

import java.util.List;

@Data
public class General_basic {

    private String title;

    private List<General_basicLevels> levels;

}