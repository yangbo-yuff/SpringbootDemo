package com.yb.yff.game.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 用户登录表
* </p>
*
* @author yangbo
* @since 2024-11-05
*/
@Data
@TableName("tb_login_history")
public class LoginHistoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
    * 用户UID
    */
    private Integer uid;

    /**
    * 登录状态，0登录，1登出
    */
    private Integer state;

    /**
    * ip
    */
    private String ip;

    /**
    * hardware
    */
    private String hardware;

    /**
    * 记录创建时间
    */
    private Date ctime;

    /**
    * 记录最近更新时间
    */
    private Date mtime;
}