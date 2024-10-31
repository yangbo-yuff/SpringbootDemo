package com.yb.yff.game.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 玩家城池
* </p>
*
* @author yangbo
* @since 2024-10-27
*/
@Data
@TableName("tb_map_role_city")
public class MapRoleCityEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * cityId
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
    * roleId
    */
    private Integer rid;

    /**
    * x坐标
    */
    private Integer x;

    /**
    * y坐标
    */
    private Integer y;

    /**
    * 城池名称
    */
    private String name;

    /**
    * 是否是主城
    */
    private Byte isMain;

    /**
    * 当前耐久
    */
    private Integer curDurable;

    private Date createdAt;

    /**
    * 占领时间
    */
    private Date occupyTime;

    /**
    * 记录创建时间
    */
    private Date ctime;

    /**
    * 记录最近更新时间
    */
    private Date mtime;
}