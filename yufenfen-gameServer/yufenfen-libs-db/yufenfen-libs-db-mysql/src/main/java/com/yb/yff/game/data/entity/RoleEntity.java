package com.yb.yff.game.data.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
* <p>
* 玩家表
* </p>
*
* @author yangbo
* @since 2024-11-05
*/
@Data
@TableName("tb_role")
public class RoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * roleId
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
    * 用户UID
    */
    private Integer uid;

    /**
    * 头像Id
    */
    private Integer headId;

    /**
    * 性别，0:女 1男
    */
    private Integer sex;

    /**
    * nick_name
    */
    private String nickName;

    /**
    * 上级联盟id
    */
    private Integer parentId;

    /**
    * 征收次数
    */
    private Integer collectTimes;

    /**
    * 最后征收时间
    */
    private Date lastCollectTime;

    /**
    * 收藏的位置
    */
    private String posTags;

    /**
    * 余额
    */
    private Integer balance;

    /**
    * 木
    */
    private Integer wood;

    /**
    * 铁
    */
    private Integer iron;

    /**
    * 石头
    */
    private Integer stone;

    /**
    * 粮食
    */
    private Integer grain;

    /**
    * 金币
    */
    private Integer gold;

    /**
    * 令牌
    */
    private Integer decree;

    /**
    * 登录时间
    */
    private Date loginTime;

    /**
    * 登出时间
    */
    private Date logoutTime;

    /**
    * 个人简介
    */
    private String profile;

    /**
    * 记录创建时间
    */
    private Date ctime;

    /**
    * 记录最近更新时间
    */
    private Date mtime;
}