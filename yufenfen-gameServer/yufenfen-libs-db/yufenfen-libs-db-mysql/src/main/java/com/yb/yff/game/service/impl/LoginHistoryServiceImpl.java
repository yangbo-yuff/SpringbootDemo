package com.yb.yff.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.yff.game.data.entity.LoginHistoryEntity;
import com.yb.yff.game.mapper.LoginHistoryMapper;
import com.yb.yff.game.service.ILoginHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户登录表 服务实现类
 * </p>
 *
 * @author yangbo
 * @since 2024-11-01
 */
@Service
public class LoginHistoryServiceImpl extends ServiceImpl<LoginHistoryMapper, LoginHistoryEntity> implements ILoginHistoryService {

}
