package com.yb.yff.game.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 技能表
* </p>
*
* @author yangbo
* @since 2024-11-05
*/
@Data
@TableName("tb_skill")
public class SkillEntity implements Serializable {

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
    * 技能id
    */
    private Integer cfgId;

    /**
    * 归属将领数组
    */
    private String belongGenerals;

    /**
    * 记录创建时间
    */
    private Date ctime;

    /**
    * 记录最近更新时间
    */
    private Date mtime;
}