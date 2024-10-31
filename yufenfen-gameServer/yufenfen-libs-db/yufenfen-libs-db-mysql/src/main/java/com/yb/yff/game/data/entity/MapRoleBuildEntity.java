package com.yb.yff.game.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 角色建筑
* </p>
*
* @author yangbo
* @since 2024-10-27
*/
@Data
@TableName("tb_map_role_build")
public class MapRoleBuildEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer rid;

    /**
    * 建筑类型
    */
    private Integer type;

    /**
    * 建筑等级
    */
    private Byte level;

    /**
    * 建筑操作等级
    */
    private Byte opLevel;

    /**
    * x坐标
    */
    private Integer x;

    /**
    * y坐标
    */
    private Integer y;

    /**
    * 名称
    */
    private String name;

    /**
    * 最大耐久
    */
    private Integer maxDurable;

    /**
    * 当前耐久
    */
    private Integer curDurable;

    /**
    * 建造、升级、拆除结束时间
    */
    private Date endTime;

    /**
    * 占领时间
    */
    private Date occupyTime;

    /**
    * 放弃时间
    */
    private Integer giveupTime;

    /**
    * 记录创建时间
    */
    private Date ctime;

    /**
    * 记录最近更新时间
    */
    private Date mtime;
}