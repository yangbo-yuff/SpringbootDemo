package com.yb.yff.sb.constant;

/**
 * Copyright (c) 2024 to 2045  YangBo.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of
 * YangBo. You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license
 * agreement you entered into with YangBo.
 *
 * @author : YangBo
 * @Project: SpringbootDemo
 * @Class: WebResponseCodeConstants
 * @CreatedOn 2024/10/10.
 * @Email: yangboyff@gmail.com
 * @Description: 网络请求响应常量
 *
 * 一般情况下，使用 HTTP 响应状态码 https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Status
 * 虽然说，HTTP 响应状态码作为业务使用表达能力偏弱，但是使用在系统层面还是非常不错的
 * 比较特殊的是，因为之前一直使用 0 作为成功，就不使用 200 啦。
 *
 */
public interface NetResponseCodeConstants {


	ResponseCode HTTP_UNAUTHORIZED = new ResponseCode(400, "账户未登录");
	ResponseCode HTTP_FORBIDDEN = new ResponseCode(401, "没有该操作权限");


	ResponseCode ProxyConnectError = new ResponseCode(-4, "代理连接失败");
	ResponseCode ProxyNotInConnect = new ResponseCode(-3, "代理错误");
	ResponseCode UserNotInConnect = new ResponseCode(-2, "链接没有找到用户");
	ResponseCode RoleNotInConnect = new ResponseCode(-1, "链接没有找到角色");
	ResponseCode SUCCESS = new ResponseCode(0, "操作成功");
	ResponseCode InvalidParam = new ResponseCode(1, "参数有误");
	ResponseCode DBError = new ResponseCode(2, "数据库异常");
	ResponseCode UserExist = new ResponseCode(3, "用户已存在");
	ResponseCode PwdIncorrect = new ResponseCode(4, "密码不正确");
	ResponseCode UserNotExist = new ResponseCode(5, "用户不存在");
	ResponseCode SessionInvalid = new ResponseCode(6, "session无效");
	ResponseCode HardwareIncorrect = new ResponseCode(7, "Hardware错误");
	ResponseCode RoleAlreadyCreate = new ResponseCode(8, "已经创建过角色了");
	ResponseCode RoleNotExist = new ResponseCode(9, "角色不存在");
	ResponseCode CityNotExist = new ResponseCode(10, "城市不存在");
	ResponseCode CityNotMe = new ResponseCode(11, "城市不是自己的");
	ResponseCode UpError = new ResponseCode(12, "升级失败");
	ResponseCode GeneralNotFound = new ResponseCode(13, "武将不存在");
	ResponseCode GeneralNotMe = new ResponseCode(14, "武将不是自己的");
	ResponseCode ArmyNotFound = new ResponseCode(15, "军队不存在");
	ResponseCode ArmyNotMe = new ResponseCode(15, "军队不是自己的");
	ResponseCode ResNotEnough = new ResponseCode(16, "资源不足");
	ResponseCode OutArmyLimit = new ResponseCode(17, "超过带兵限制");
	ResponseCode ArmyBusy = new ResponseCode(18, "军队再忙");
	ResponseCode GeneralBusy = new ResponseCode(19, "将领再忙");
	ResponseCode CannotGiveUp = new ResponseCode(20, "不能放弃");
	ResponseCode BuildNotMe = new ResponseCode(21, "领地不是自己的");
	ResponseCode ArmyNotMain = new ResponseCode(22, "军队没有主将");
	ResponseCode UnReachable = new ResponseCode(23, "不可到达");
	ResponseCode PhysicalPowerNotEnough = new ResponseCode(24, "体力不足");
	ResponseCode DecreeNotEnough = new ResponseCode(25, "政令不足");
	ResponseCode GoldNotEnough = new ResponseCode(26, "金币不足");
	ResponseCode GeneralRepeat = new ResponseCode(27, "重复上阵");
	ResponseCode CostNotEnough = new ResponseCode(28, "cost不足");
	ResponseCode GeneralNoHas = new ResponseCode(29, "没有该合成武将");
	ResponseCode GeneralNoSame = new ResponseCode(30, "合成武将非同名");
	ResponseCode ArmyNotEnough = new ResponseCode(31, "队伍数不足");
	ResponseCode TongShuaiNotEnough = new ResponseCode(32, "统帅不足");
	ResponseCode GeneralStarMax = new ResponseCode(33, "升级到最大星级");
	ResponseCode UnionCreateError = new ResponseCode(34, "联盟创建失败");
	ResponseCode UnionNotFound = new ResponseCode(35, "联盟不存在");
	ResponseCode PermissionDenied = new ResponseCode(36, "权限不足");
	ResponseCode UnionAlreadyHas = new ResponseCode(37, "已经有联盟");
	ResponseCode UnionNotAllowExit = new ResponseCode(38, "不允许退出");
	ResponseCode ContentTooLong = new ResponseCode(39, "内容太长");
	ResponseCode NotBelongUnion = new ResponseCode(40, "不属于该联盟");
	ResponseCode PeopleIsFull = new ResponseCode(41, "用户已满");
	ResponseCode HasApply = new ResponseCode(42, "已经申请过了");
	ResponseCode BuildCanNotDefend = new ResponseCode(43, "不能驻守");
	ResponseCode BuildCanNotAttack = new ResponseCode(44, "不能占领");
	ResponseCode BuildMBSNotFound = new ResponseCode(45, "没有军营");
	ResponseCode BuildWarFree = new ResponseCode(46, "免战中");
	ResponseCode ArmyConscript = new ResponseCode(47, "征兵中");
	ResponseCode BuildGiveUpAlready = new ResponseCode(48, "领地已经在放弃了");
	ResponseCode CanNotBuildNew = new ResponseCode(49, "不能再新建建筑在领地上");
	ResponseCode CanNotTransfer = new ResponseCode(50, "不能调兵");
	ResponseCode HoldIsFull = new ResponseCode(51, "坑位已满");
	ResponseCode ArmyIsOutside = new ResponseCode(52, "队伍在城外");
	ResponseCode CanNotUpBuild = new ResponseCode(53, "不能升级建筑");
	ResponseCode CanNotDestroy = new ResponseCode(54, "不能拆除建筑");
	ResponseCode OutCollectTimesLimit = new ResponseCode(55, "超过征收次数");
	ResponseCode InCdCanNotOperate = new ResponseCode(56, "cd内不能操作");
	ResponseCode OutGeneralLimit = new ResponseCode(57, "武将超过上限了");
	ResponseCode NotHasJiShi = new ResponseCode(58, "没有集市");
	ResponseCode OutPosTagLimit = new ResponseCode(59, "超过了收藏上限");
	ResponseCode OutSkillLimit = new ResponseCode(60, "超过了技能上限");
	ResponseCode UpSkillError = new ResponseCode(61, "装备技能失败");
	ResponseCode DownSkillError = new ResponseCode(62, "取下技能失败");
	ResponseCode OutArmNotMatch = new ResponseCode(63, "兵种不符");
	ResponseCode PosNotSkill = new ResponseCode(64, "该位置没有技能");
	ResponseCode SkillLevelFull = new ResponseCode(65, "技能等级已满");
	ResponseCode RoleNameExist = new ResponseCode(66, "昵称已经存在");
}
