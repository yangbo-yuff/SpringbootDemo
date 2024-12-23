package com.yb.yff.game.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 角色建筑
* </p>
*
* @author yangbo
* @since 2024-11-05
*/
@Data
@TableName("tb_map_role_build")
@FieldNameConstants
public class MapRoleBuildEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer rid;

    /**
    * 建筑类型
    */
    @TableField("type")
    private Integer type;

    /**
    * 建筑等级
    */
    private Integer level;

    /**
    * 建筑操作等级
    */
    private Integer opLevel;

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
    private Long endTime;

    /**
     * 要塞操作类型 的操作类型，1 新建，2升级，3拆除
     * 配合 endTime 使用
     * 一般情况下为Null
     */
    private Integer operationType;

    /**
    * 占领时间
    */
    private Long occupyTime;

    /**
    * 放弃时间
    */
    private Long giveupTime;

    /**
    * 记录创建时间
    */
    private Date ctime;

    /**
    * 记录最近更新时间
    */
    private Date mtime;
}