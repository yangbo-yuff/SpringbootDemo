package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_addition {

    private String title;

    private List<Facility_additionList> list;

}