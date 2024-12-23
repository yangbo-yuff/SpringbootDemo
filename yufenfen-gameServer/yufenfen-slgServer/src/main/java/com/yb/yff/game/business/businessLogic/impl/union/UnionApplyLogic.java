package com.yb.yff.game.business.businessLogic.impl.union;

import com.yb.yff.game.business.businessDataMgr.JsonConfigMgr;
import com.yb.yff.game.business.businessDataMgr.impl.RoleDataMgrImpl;
import com.yb.yff.game.business.businessDataMgr.impl.UnionMgrImpl;
import com.yb.yff.game.business.businessLogic.IUnionApply;
import com.yb.yff.game.data.constant.myEnum.UnionApplyState;
import com.yb.yff.game.data.dto.role.RoleDTO;
import com.yb.yff.game.data.dto.union.UnionApplyDTO;
import com.yb.yff.game.data.dto.union.UnionDTO;
import com.yb.yff.game.data.dto.union.VerifyReqDTO;
import com.yb.yff.sb.constant.NetResponseCodeConstants;
import com.yb.yff.sb.constant.ResponseCode;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
 * @Class: UnionLogLogic
 * @CreatedOn 2024/12/14.
 * @Email: yangboyff@gmail.com
 * @Description: 联盟申请逻辑
 */
public class UnionApplyLogic {

    private final RoleDataMgrImpl roleDataMgr;
    private final UnionMgrImpl unionMgr;
    private final UnionLogLogic unionLogLogic;
    private final JsonConfigMgr jsonConfigMgr;

    private final IUnionApply unionApply;

    public UnionApplyLogic(RoleDataMgrImpl roleDataMgr, UnionMgrImpl unionMgr, UnionLogLogic unionLogLogic,
                           JsonConfigMgr jsonConfigMgr, IUnionApply unionApply) {
        this.roleDataMgr = roleDataMgr;
        this.unionMgr = unionMgr;
        this.unionLogLogic = unionLogLogic;
        this.jsonConfigMgr = jsonConfigMgr;
        this.unionApply = unionApply;
    }

    /**
     * 审核申请人
     *
     * @param rid
     * @param verifyInfo
     * @return
     */
    public ResponseCode verifyApplicant(Integer rid, VerifyReqDTO verifyInfo) {

        RoleDTO op = roleDataMgr.getRoleDTO(rid);

        UnionDTO unionDTO = unionMgr.getUnion(op.getUnionId());
        if (unionDTO == null) {
            return NetResponseCodeConstants.UnionNotFound;
        }

        UnionApplyDTO apply = unionMgr.getUnionApply(unionDTO.getId(), verifyInfo.getId());

        if (apply == null) {
            return NetResponseCodeConstants.InvalidParam;
        }

        if (!unionDTO.getChairman().equals(rid) && !unionDTO.getViceChairman().equals(rid)) {
            return NetResponseCodeConstants.PermissionDenied;
        }

        Integer memberLimit = jsonConfigMgr.getBasicConfig().getUnion().getMember_limit();
        if (unionDTO.getMembers().size() >= memberLimit) {
            return NetResponseCodeConstants.PeopleIsFull;
        }

        if (roleDataMgr.hasUnion(apply.getRid())) {
            return NetResponseCodeConstants.UnionAlreadyHas;
        }

        if (verifyInfo.getDecide().equals(UnionApplyState.UnionAdopt.getValue())) {
            //同意
            apply.setState(UnionApplyState.UnionAdopt.getValue());

            unionDTO.getMembers().add(apply.getRid());

            unionApply.onUnionAdopt(apply);

            unionMgr.saveUnion2DB(unionDTO);

            unionLogLogic.newJoinLog(op, apply);
        } else {
            // 拒绝
            unionApply.onUnionRefuse(apply);
        }

        apply.setState(verifyInfo.getDecide());

        unionMgr.updateUnionApply2DB(apply);

        return NetResponseCodeConstants.SUCCESS;
    }


    public boolean hasApply(Integer rid, Integer unionId) {
        ConcurrentHashMap<Integer, UnionApplyDTO> unionApplys = unionMgr.getUnionApplyMap(unionId);

        if (unionApplys == null || unionApplys.size() == 0) {
            return false;
        }

        for (Map.Entry<Integer, UnionApplyDTO> entry : unionApplys.entrySet()) {
            UnionApplyDTO unionApply = entry.getValue();
            if (unionApply.getRid().equals(rid) && unionApply.getState().equals(UnionApplyState.UnionUntreated.getValue())) {
                return true;
            }
        }

        return false;
    }
}
