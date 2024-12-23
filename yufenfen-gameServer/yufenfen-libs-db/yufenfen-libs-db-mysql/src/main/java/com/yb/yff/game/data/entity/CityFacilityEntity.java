package com.yb.yff.game.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 城池设施
* </p>
*
* @author yangbo
* @since 2024-11-05
*/
@Data
@TableName("tb_city_facility")
public class CityFacilityEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
    * 城市id
    */
    private Integer cityId;

    private Integer rid;

    /**
    * 设施列表，格式为json结构
    */
    private String facilities;

    /**
    * 记录创建时间
    */
    private Date ctime;

    /**
    * 记录最近更新时间
    */
    private Date mtime;
}