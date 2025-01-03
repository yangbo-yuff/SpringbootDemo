CREATE DATABASE IF NOT EXISTS slgGameDB DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

USE slgGameDB;

CREATE TABLE IF NOT EXISTS `tb_user_info` (
    `id` int unsigned NOT NULL AUTO_INCREMENT,
    `username` varchar(20) NOT NULL COMMENT '用户名',
    `passcode` char(12) NOT NULL DEFAULT '' COMMENT '加密随机数',
    `passwd` char(64) NOT NULL DEFAULT '' COMMENT 'md5密码',
    `status` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '用户账号状态。0-默认；1-冻结；2-停号',
    `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`username`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '用户信息表';

CREATE TABLE IF NOT EXISTS `tb_login_history` (
    `id` int unsigned NOT NULL AUTO_INCREMENT,
    `uid` int unsigned NOT NULL DEFAULT 0 COMMENT '用户UID',
    `state` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '登录状态，0登录，1登出',
    `ip` varchar(31) NOT NULL DEFAULT '' COMMENT 'ip',
    `hardware` varchar(64) NOT NULL DEFAULT '' COMMENT 'hardware',
    `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '用户登录表';

CREATE TABLE IF NOT EXISTS `tb_login_last` (
    `id` int unsigned NOT NULL AUTO_INCREMENT,
    `uid` int unsigned NOT NULL DEFAULT 0 COMMENT '用户UID',
    `login_time` TIMESTAMP NULL DEFAULT NULL COMMENT '登录时间',
    `logout_time` TIMESTAMP NULL DEFAULT NULL COMMENT '登出时间',
    `ip` varchar(31) NOT NULL DEFAULT '' COMMENT 'ip',
    `is_logout` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '是否logout,1:logout，0:login',
    `session` varchar(100) COMMENT '会话',
    `hardware` varchar(64) NOT NULL DEFAULT '' COMMENT 'hardware',
    `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    UNIQUE KEY (`uid`),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '最后一次用户登录表';

CREATE TABLE IF NOT EXISTS `tb_role` (
    `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'roleId',
    `uid` int unsigned NOT NULL COMMENT '用户UID',
    `head_id` int unsigned NOT NULL DEFAULT 0 COMMENT '头像Id',
    `sex` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '性别，0:女 1男',
    `nick_name` varchar(100) COMMENT 'nick_name',
    `parent_id` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '上级联盟id',
    `collect_times` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '征收次数',
    `last_collect_time` timestamp DEFAULT '2013-03-15 14:38:09' COMMENT '最后征收时间',
    `pos_tags` varchar(512) COMMENT '收藏的位置',
    `balance` int unsigned NOT NULL DEFAULT 0 COMMENT '余额',
    `wood` int unsigned NOT NULL DEFAULT 0 COMMENT '木',
    `iron` int unsigned NOT NULL DEFAULT 0 COMMENT '铁',
    `stone` int unsigned NOT NULL DEFAULT 0 COMMENT '石头',
    `grain` int unsigned NOT NULL DEFAULT 0 COMMENT '粮食',
    `gold` int unsigned NOT NULL DEFAULT 0 COMMENT '金币',
    `decree` int unsigned NOT NULL DEFAULT 0 COMMENT '令牌',
    `login_time` TIMESTAMP NULL DEFAULT NULL COMMENT '登录时间',
    `logout_time` TIMESTAMP NULL DEFAULT NULL COMMENT '登出时间',
    `profile` varchar(500) COMMENT '个人简介',
    `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    UNIQUE KEY (`uid`, `nick_name`),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '玩家表';

CREATE TABLE IF NOT EXISTS `tb_map_role_city` (
    `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'cityId',
    `rid` int unsigned NOT NULL COMMENT 'roleId',
    `x` int unsigned NOT NULL COMMENT 'x坐标',
    `y` int unsigned NOT NULL COMMENT 'y坐标',
    `name` varchar(100) NOT NULL DEFAULT '城池' COMMENT '城池名称',
    `is_main` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '是否是主城',
    `cur_durable` int unsigned NOT NULL COMMENT '当前耐久',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `occupy_time` timestamp DEFAULT '2013-03-15 14:38:09' COMMENT '占领时间',
    `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '玩家城池';

CREATE TABLE IF NOT EXISTS `tb_map_role_build` (
    `id` int unsigned NOT NULL AUTO_INCREMENT,
    `rid` int unsigned NOT NULL,
    `type` int unsigned NOT NULL COMMENT '建筑类型',
    `level` tinyint unsigned NOT NULL COMMENT '建筑等级',
    `op_level` tinyint unsigned COMMENT '建筑操作等级',
    `x` int unsigned NOT NULL COMMENT 'x坐标',
    `y` int unsigned NOT NULL COMMENT 'y坐标',
    `name` varchar(100) NOT NULL COMMENT '名称',
    `max_durable` int unsigned NOT NULL COMMENT '最大耐久',
    `cur_durable` int unsigned NOT NULL COMMENT '当前耐久',
    `operation_type` int NOT NULL DEFAULT '0'  COMMENT '建筑物操作类型：0 无操作, 1:新建, 2:升级，3:拆除',
    `end_time` bigint unsigned COMMENT '新建、升级、拆除结束时间',
    `occupy_time` bigint unsigned COMMENT '占领时间',
    `giveUp_time` bigint unsigned DEFAULT '0' COMMENT '放弃时间',
    `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '角色建筑';

CREATE TABLE IF NOT EXISTS `tb_city_facility` (
    `id` int unsigned NOT NULL AUTO_INCREMENT,
    `city_id` int unsigned NOT NULL COMMENT '城市id',
    `rid` int unsigned NOT NULL,
    `facilities` varchar(4096) NOT NULL COMMENT '设施列表，格式为json结构',
    `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    UNIQUE KEY (`cityId`),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '城池设施';

CREATE TABLE IF NOT EXISTS `tb_general` (
    `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `rid` int unsigned NOT NULL COMMENT 'rid',
    `cfg_id` int unsigned NOT NULL COMMENT '配置id',
    `physical_power` int unsigned NOT NULL COMMENT '体力',
    `exp` int unsigned NOT NULL COMMENT '经验',
    `order` tinyint NOT NULL COMMENT '第几队',
    `level` tinyint unsigned NOT NULL DEFAULT 1 COMMENT 'level',
    `city_id` int NOT NULL DEFAULT 0 COMMENT '城市id',
    `star` int NOT NULL DEFAULT 0 COMMENT '稀有度(星级)',
    `star_lv` int NOT NULL DEFAULT 0 COMMENT '稀有度(星级)进阶等级级',
    `arms` int NOT NULL DEFAULT 0 COMMENT '兵种',
    `has_pr_point` int NOT NULL DEFAULT 0 COMMENT '总属性点',
    `use_pr_point` int NOT NULL DEFAULT 0 COMMENT '已用属性点',
    `attack_distance` int NOT NULL DEFAULT 0 COMMENT '攻击距离',
    `force_added` int NOT NULL DEFAULT 0 COMMENT '已加攻击属性',
    `strategy_added` int NOT NULL DEFAULT 0 COMMENT '已加战略属性',
    `defense_added` int NOT NULL DEFAULT 0 COMMENT '已加防御属性',
    `speed_added` int NOT NULL DEFAULT 0 COMMENT '已加速度属性',
    `destroy_added` int NOT NULL DEFAULT 0 COMMENT '已加破坏属性',
    `parent_id` int NOT NULL DEFAULT 0 COMMENT '已合成到将领的id',
    `compose_type` int NOT NULL DEFAULT 0 COMMENT '合成类型',
    `skills` varchar(128) COMMENT '携带的技能',
    `state` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '0:正常，1:转换掉了',
    `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '将领表';

CREATE TABLE IF NOT EXISTS `tb_army` (
    `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `rid` int unsigned NOT NULL COMMENT 'rid',
    `city_id` int unsigned NOT NULL COMMENT '城市id',
    `order` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '第几队 1-5队',
    `generals` varchar(256) NOT NULL DEFAULT '[0, 0, 0]' COMMENT '将领',
    `soldiers` varchar(256) NOT NULL DEFAULT '[0, 0, 0]' COMMENT '士兵',
    `conscript_times` varchar(256) NOT NULL DEFAULT '[0, 0, 0]' COMMENT '征兵结束时间',
    `conscript_cnts` varchar(256) NOT NULL DEFAULT '[0, 0, 0]' COMMENT '征兵数量',
    `cmd` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '命令  0:空闲 1:攻击 2：驻军 3:返回',
    `from_x` int unsigned NOT NULL COMMENT '来自x坐标',
    `from_y` int unsigned NOT NULL COMMENT '来自y坐标',
    `to_x` int unsigned COMMENT '去往x坐标',
    `to_y` int unsigned COMMENT '去往y坐标',
    `start` timestamp NULL DEFAULT NULL COMMENT '出发时间',
    `end` TIMESTAMP NULL DEFAULT NULL COMMENT '到达时间',
    `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    UNIQUE KEY (`rid`, `cityId`, `order`),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '军队表';

CREATE TABLE IF NOT EXISTS `tb_war_report` (
    `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `a_rid` int unsigned NOT NULL COMMENT '攻击方id',
    `d_rid` int unsigned NOT NULL DEFAULT 0 COMMENT '防守方id,0为系统npc',
    `b_a_army` varchar(512) NOT NULL COMMENT '开始攻击方军队',
    `b_d_army` varchar(512) NOT NULL COMMENT '开始防守方军队',
    `e_a_army` varchar(512) NOT NULL COMMENT '开始攻击方军队',
    `e_d_army` varchar(512) NOT NULL COMMENT '开始防守方军队',
    `b_a_general` varchar(512) NOT NULL COMMENT '开始攻击方将领',
    `b_d_general` varchar(512) NOT NULL COMMENT '开始防守方将领',
    `e_a_general` varchar(512) NOT NULL COMMENT '结束攻击方将领',
    `e_d_general` varchar(512) NOT NULL COMMENT '结束防守方将领',
    `rounds` varchar(10240) NOT NULL COMMENT '回合战报数据',
    `result` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '0失败，1打平，2胜利',
    `a_is_read` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '攻击方战报是否已阅 0:未阅 1:已阅',
    `d_is_read` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '攻击方战报是否已阅 0:未阅 1:已阅',
    `destroy` int unsigned COMMENT '破坏了多少耐久',
    `occupy` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '是否攻占 0:否 1:是',
    `x` int unsigned COMMENT 'x坐标',
    `y` int unsigned COMMENT 'y坐标',
    `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '战报表';

CREATE TABLE IF NOT EXISTS `tb_coalition` (
    `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `name` varchar(20) NOT NULL COMMENT '联盟名字',
    `members` varchar(2048) NOT NULL COMMENT '成员',
    `create_id` int unsigned NOT NULL COMMENT '创建者id',
    `chairman` int unsigned NOT NULL COMMENT '盟主',
    `vice_chairman` int unsigned NOT NULL DEFAULT 0 COMMENT '副盟主',
    `notice` varchar(256) COMMENT '公告',
    `state` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '0解散，1运行中',
    `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '联盟';

CREATE TABLE IF NOT EXISTS `tb_coalition_apply` (
    `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `union_id` int unsigned NOT NULL COMMENT '联盟id',
    `rid` int unsigned NOT NULL COMMENT '申请者的rid',
    `state` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '申请状态，0未处理，1拒绝，2通过',
    `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '联盟申请表';

CREATE TABLE IF NOT EXISTS `tb_coalition_log` (
    `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `union_id` int unsigned NOT NULL COMMENT '联盟id',
    `op_rid` int unsigned NOT NULL COMMENT '操作者id',
    `target_id` int unsigned COMMENT '被操作的对象',
    `des` varchar(256) NOT NULL COMMENT '描述',
    `state` tinyint unsigned NOT NULL COMMENT '0:创建,1:解散,2:加入,3:退出,4:踢出,5:任命,6:禅让,7:修改公告',
    `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '联盟日志表';

CREATE TABLE IF NOT EXISTS `tb_skill` (
    `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
    `rid` int unsigned NOT NULL COMMENT 'rid',
    `cfg_id` int unsigned NOT NULL COMMENT '技能id',
    `belong_generals` varchar(256) NOT NULL Default '[]' COMMENT '归属将领数组',
    `ctime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最近更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '技能表';