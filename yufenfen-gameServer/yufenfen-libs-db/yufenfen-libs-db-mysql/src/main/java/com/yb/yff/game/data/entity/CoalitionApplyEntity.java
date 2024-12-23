package com.yb.yff.game.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 联盟申请表
* </p>
*
* @author yangbo
* @since 2024-11-05
*/
@Data
@TableName("tb_coalition_apply")
public class CoalitionApplyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * id
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
    * 联盟id
    */
    private Integer unionId;

    /**
    * 申请者的rid
    */
    private Integer rid;

    /**
    * 申请状态，0未处理，1拒绝，2通过
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