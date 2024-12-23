package com.yb.yff.game.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 联盟日志表
* </p>
*
* @author yangbo
* @since 2024-11-05
*/
@Data
@TableName("tb_coalition_log")
public class CoalitionLogEntity implements Serializable {

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
    * 操作者id
    */
    private Integer opRid;

    /**
    * 被操作的对象
    */
    private Integer targetId;

    /**
    * 描述
    */
    private String des;

    /**
    * 0:创建,1:解散,2:加入,3:退出,4:踢出,5:任命,6:禅让,7:修改公告
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