package com.yb.yff.game.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 战报表
* </p>
*
* @author yangbo
* @since 2024-11-05
*/
@Data
@TableName("tb_war_report")
public class WarReportEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
    * 攻击方id
    */
    private Integer aRid;

    /**
    * 防守方id,0为系统npc
    */
    private Integer dRid;

    /**
    * 开始攻击方军队
    */
    private String bAArmy;

    /**
    * 开始防守方军队
    */
    private String bDArmy;

    /**
    * 开始攻击方军队
    */
    private String eAArmy;

    /**
    * 开始防守方军队
    */
    private String eDArmy;

    /**
    * 开始攻击方将领
    */
    private String bAGeneral;

    /**
    * 开始防守方将领
    */
    private String bDGeneral;

    /**
    * 结束攻击方将领
    */
    private String eAGeneral;

    /**
    * 结束防守方将领
    */
    private String eDGeneral;

    /**
    * 回合战报数据
    */
    private String rounds;

    /**
    * 0失败，1打平，2胜利
    */
    private Integer result;

    /**
    * 攻击方战报是否已阅 0:未阅 1:已阅
    */
    private Integer aIsRead;

    /**
    * 攻击方战报是否已阅 0:未阅 1:已阅
    */
    private Integer dIsRead;

    /**
    * 破坏了多少耐久
    */
    private Integer destroy;

    /**
    * 是否攻占 0:否 1:是
    */
    private Integer occupy;

    /**
    * x坐标
    */
    private Integer x;

    /**
    * y坐标
    */
    private Integer y;

    /**
    * 记录创建时间
    */
    private Date ctime;

    /**
    * 记录最近更新时间
    */
    private Date mtime;
}