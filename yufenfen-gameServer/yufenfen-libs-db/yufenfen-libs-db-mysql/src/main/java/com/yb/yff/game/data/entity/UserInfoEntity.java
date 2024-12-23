package com.yb.yff.game.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 用户信息表
* </p>
*
* @author yangbo
* @since 2024-11-05
*/
@Data
@TableName("tb_user_info")
public class UserInfoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
    * 用户名
    */
    private String username;

    /**
    * 加密随机数
    */
    private String passcode;

    /**
    * md5密码
    */
    private String passwd;

    /**
    * 用户账号状态。0-默认；1-冻结；2-停号
    */
    private Integer status;

    /**
    * 记录创建时间
    */
    private Date ctime;

    /**
    * 记录最近更新时间
    */
    private Date mtime;
}