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
* 将领表
* </p>
*
* @author yangbo
* @since 2024-11-05
*/
@Data
@TableName("tb_general")
public class GeneralEntity implements Serializable {

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
    * 配置id
    */
    private Integer cfgId;

    /**
    * 体力
    */
    private Integer physicalPower;

    /**
    * 经验
    */
    private Integer exp;

    /**
    * 第几队
    */
    @TableField("`order`")
    private Integer order;

    /**
    * level
    */
    @TableField("`level`")
    private Integer level;

    /**
    * 城市id
    */
    private Integer cityId;

    /**
    * 稀有度(星级)
    */
    private Integer star;

    /**
    * 稀有度(星级)进阶等级级
    */
    private Integer starLv;

    /**
    * 兵种
    */
    private Integer arms;

    /**
    * 总属性点
    */
    private Integer hasPrPoint;

    /**
    * 已用属性点
    */
    private Integer usePrPoint;

    /**
    * 攻击距离
    */
    private Integer attackDistance;

    /**
    * 已加攻击属性
    */
    private Integer forceAdded;

    /**
    * 已加战略属性
    */
    private Integer strategyAdded;

    /**
    * 已加防御属性
    */
    private Integer defenseAdded;

    /**
    * 已加速度属性
    */
    private Integer speedAdded;

    /**
    * 已加破坏属性
    */
    private Integer destroyAdded;

    /**
    * 已合成到将领的id
    */
    private Integer parentId;

    /**
    * 合成类型
    */
    private Integer composeType;

    /**
    * 携带的技能
    */
    private String skills;

    /**
    * 0:正常，1:转换掉了
    */
    private Integer state;

    /**
    * 记录创建时间
    */
    private Date ctime;

    /**
    * 记录最近更新时间
    */
    private Date mtime;
}