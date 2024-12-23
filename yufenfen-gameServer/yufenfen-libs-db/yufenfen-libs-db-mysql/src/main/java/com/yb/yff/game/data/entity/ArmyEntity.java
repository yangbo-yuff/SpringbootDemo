package com.yb.yff.game.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 军队表
* </p>
*
* @author yangbo
* @since 2024-11-05
*/
@Data
@TableName("tb_army")
public class ArmyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
    * rid
    */
    private Integer rid;

    /**
    * 城市id
    */
    private Integer cityId;

    /**
    * 第几队 1-5队
    */
    @TableField("`order`")
    private Integer order;

    /**
    * 将领
    */
    private String generals;

    /**
    * 士兵
    */
    private String soldiers;

    /**
    * 征兵结束时间
    */
    private String conscriptTimes;

    /**
    * 征兵数量
    */
    private String conscriptCnts;

    /**
    * 命令  0:空闲 1:攻击 2：驻军 3:返回
    */
    private Integer cmd;

    /**
    * 来自x坐标
    */
    private Integer fromX;

    /**
    * 来自y坐标
    */
    private Integer fromY;

    /**
    * 去往x坐标
    */
    private Integer toX;

    /**
    * 去往y坐标
    */
    private Integer toY;

    /**
    * 出发时间
    */
    private Date start;

    /**
    * 到达时间
    */
    private Date end;

    /**
    * 记录创建时间
    */
    private Date ctime;

    /**
    * 记录最近更新时间
    */
    private Date mtime;
}