package com.yb.yff.game.jsondb.data.dto.facility;

import lombok.Data;

import java.util.List;

@Data
public class Facility_config {

    private List<Facility_configConfigs> configs;

    private String title;

}