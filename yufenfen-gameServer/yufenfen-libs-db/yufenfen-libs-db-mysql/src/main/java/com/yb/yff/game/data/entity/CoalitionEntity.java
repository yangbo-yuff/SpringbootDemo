package com.yb.yff.game.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 联盟
* </p>
*
* @author yangbo
* @since 2024-11-05
*/
@Data
@TableName("tb_coalition")
public class CoalitionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
    * 联盟名字
    */
    private String name;

    /**
    * 成员
    */
    private String members;

    /**
    * 创建者id
    */
    private Integer createId;

    /**
    * 盟主
    */
    private Integer chairman;

    /**
    * 副盟主
    */
    private Integer viceChairman;

    /**
    * 公告
    */
    private String notice;

    /**
    * 0解散，1运行中
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