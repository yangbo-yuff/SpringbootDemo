package com.yb.yff.game.constant;

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
 * @Class: GameBusinessType
 * @CreatedOn 2024/10/2.
 * @Email: yangboyff@gmail.com
 * @Description: 游戏业务类型
 */
public class GameBusinessType {
	public static final String GAME_BUSINESS_TYPE_ACCOUNT_LOGIN = "account/login";
	public static final String GAME_BUSINESS_TYPE_ACCOUNT_LOGOUT = "account/logout";
	public static final String GAME_BUSINESS_TYPE_ACCOUNT_RELOGIN = "account/reLogin";
	public static final String GAME_BUSINESS_TYPE_ACCOUNT_ROBLOGIN = "robLogin";

	public static final String GAME_BUSINESS_TYPE_ROLE_CREATE = "role/create";
	public static final String GAME_BUSINESS_TYPE_ROLE_ROLELIST = "role/roleList";
	public static final String GAME_BUSINESS_TYPE_ROLE_ENTERSERVER = "role/enterServer";
	public static final String GAME_BUSINESS_TYPE_ROLE_MYCITY = "role/myCity";
	public static final String GAME_BUSINESS_TYPE_ROLE_MYROLERES = "role/myRoleRes";
	public static final String GAME_BUSINESS_TYPE_ROLE_MYPROPERTY = "role/myProperty";
	public static final String GAME_BUSINESS_TYPE_ROLE_UPPOSITION = "role/upPosition";
	public static final String GAME_BUSINESS_TYPE_ROLE_POSTAGLIST = "role/posTagList";
	public static final String GAME_BUSINESS_TYPE_ROLE_OPPOSTAG = "role/opPosTag";

	public static final String GAME_BUSINESS_TYPE_NATIONMAP_CONFIG = "nationMap/config";
	public static final String GAME_BUSINESS_TYPE_NATIONMAP_SCANBLOCK = "nationMap/scanBlock";
	public static final String GAME_BUSINESS_TYPE_NATIONMAP_GIVEUP = "nationMap/giveUp";
	public static final String GAME_BUSINESS_TYPE_NATIONMAP_BUILD = "nationMap/build";
	public static final String GAME_BUSINESS_TYPE_NATIONMAP_UPBUILD = "nationMap/upBuild";
	public static final String GAME_BUSINESS_TYPE_NATIONMAP_DELBUILD = "nationMap/delBuild";

	public static final String GAME_BUSINESS_TYPE_CITY_FACILITIES = "city/facilities";
	public static final String GAME_BUSINESS_TYPE_CITY_UPFACILITY = "city/upFacility";


	public static final String GAME_BUSINESS_TYPE_GENERAL_MYGENERALS = "general/myGenerals";
	public static final String GAME_BUSINESS_TYPE_GENERAL_DRAWGENERAL = "general/drawGeneral";
	public static final String GAME_BUSINESS_TYPE_GENERAL_COMPOSEGENERAL = "general/composeGeneral";
	public static final String GAME_BUSINESS_TYPE_GENERAL_ADDPRGENERAL = "general/addPrGeneral";
	public static final String GAME_BUSINESS_TYPE_GENERAL_CONVERT = "general/convert";

	public static final String GAME_BUSINESS_TYPE_GENERAL_UPSKILL = "general/upSkill";
	public static final String GAME_BUSINESS_TYPE_GENERAL_DOWNSKILL = "general/downSkill";
	public static final String GAME_BUSINESS_TYPE_GENERAL_LVSKILL = "general/lvSkill";

	public static final String GAME_BUSINESS_TYPE_ARMY_MYLIST = "army/myList";
	public static final String GAME_BUSINESS_TYPE_ARMY_MYONE = "army/myOne";
	public static final String GAME_BUSINESS_TYPE_ARMY_DISPOSE = "army/dispose";
	public static final String GAME_BUSINESS_TYPE_ARMY_CONSCRIPT = "army/conscript";
	public static final String GAME_BUSINESS_TYPE_ARMY_ASSIGN = "army/assign";

	public static final String GAME_BUSINESS_TYPE_WAR_REPORT = "war/report";
	public static final String GAME_BUSINESS_TYPE_WAR_READ = "war/read";

	public static final String GAME_BUSINESS_TYPE_UNION_CREATE = "union/create";
	public static final String GAME_BUSINESS_TYPE_UNION_JOIN = "union/join";
	public static final String GAME_BUSINESS_TYPE_UNION_LIST = "union/list";
	public static final String GAME_BUSINESS_TYPE_UNION_MEMBER = "union/member";
	public static final String GAME_BUSINESS_TYPE_UNION_APPLYLIST = "union/applyList";
	public static final String GAME_BUSINESS_TYPE_UNION_DISMISS = "union/dismiss";
	public static final String GAME_BUSINESS_TYPE_UNION_VERIFY = "union/verify";
	public static final String GAME_BUSINESS_TYPE_UNION_EXIT = "union/exit";
	public static final String GAME_BUSINESS_TYPE_UNION_KICK = "union/kick";
	public static final String GAME_BUSINESS_TYPE_UNION_APPOINT = "union/appoint";
	public static final String GAME_BUSINESS_TYPE_UNION_ABDICATE = "union/abdicate";
	public static final String GAME_BUSINESS_TYPE_UNION_MODNOTICE = "union/modNotice";
	public static final String GAME_BUSINESS_TYPE_UNION_INFO = "union/info";
	public static final String GAME_BUSINESS_TYPE_UNION_LOG = "union/log";
	public static final String GAME_BUSINESS_TYPE_UNION_APPLY_PUSH = "unionApply/push";

	public static final String GAME_BUSINESS_TYPE_INTERIOR_COLLECT = "interior/collect";
	public static final String GAME_BUSINESS_TYPE_INTERIOR_OPENCOLLECT = "interior/openCollect";
	public static final String GAME_BUSINESS_TYPE_INTERIOR_TRANSFORM = "interior/transform";

	public static final String GAME_BUSINESS_TYPE_WAR_REPORTPUSH = "warReport/push";
	public static final String GAME_BUSINESS_TYPE_GENERAL_PUSH = "general/push";
	public static final String GAME_BUSINESS_TYPE_ARMY_PUSH = "army/push";
	public static final String GAME_BUSINESS_TYPE_ROLEBUILD_PUSH = "roleBuild/push";
	public static final String GAME_BUSINESS_TYPE_ROLECITY_PUSH = "roleCity/push";
	public static final String GAME_BUSINESS_TYPE_FACILITY_PUSH = "facility/push";
	public static final String GAME_BUSINESS_TYPE_ROLERES_PUSH = "roleRes/push";

	public static final String GAME_BUSINESS_TYPE_SKILL_LIST = "skill/list";
	public static final String GAME_BUSINESS_TYPE_SKILL_PUSH = "skill/push";

	public static class Chat {
		public static final String GAME_BUSINESS_TYPE_CHAT_LOGIN = "chat/login";
		public static final String GAME_BUSINESS_TYPE_CHAT_CHAT = "chat/chat";
		public static final String GAME_BUSINESS_TYPE_CHAT_HISTORY = "chat/history";
		public static final String GAME_BUSINESS_TYPE_CHAT_JOIN = "chat/join";
		public static final String GAME_BUSINESS_TYPE_CHAT_EXIT = "chat/exit";
		public static final String GAME_BUSINESS_TYPE_CHAT_PUSH = "chat/push";
	}
}
