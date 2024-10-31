package com.yb.yff.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.yff.game.data.entity.UserInfoEntity;
import com.yb.yff.game.mapper.UserInfoMapper;
import com.yb.yff.game.service.IUserInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author yangbo
 * @since 2024-10-07
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoEntity> implements IUserInfoService {

}
