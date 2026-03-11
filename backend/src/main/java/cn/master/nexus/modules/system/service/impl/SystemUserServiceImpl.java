package cn.master.nexus.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.nexus.modules.system.entity.SystemUser;
import cn.master.nexus.modules.system.mapper.SystemUserMapper;
import cn.master.nexus.modules.system.service.SystemUserService;
import org.springframework.stereotype.Service;

/**
 * 用户 服务层实现。
 *
 * @author 11's papa
 * @since 2026-03-03
 */
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser>  implements SystemUserService{

}
