package com.yb.yff.game.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yb.yff.game.data.entity.LoginLastEntity;
import com.yb.yff.game.mapper.LoginLastMapper;
import com.yb.yff.game.service.ILoginLastService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 最后一次用户登录表 服务实现类
 * </p>
 *
 * @author yangbo
 * @since 2024-11-01
 */
@Service
public class LoginLastServiceImpl extends ServiceImpl<LoginLastMapper, LoginLastEntity> implements ILoginLastService {

}
