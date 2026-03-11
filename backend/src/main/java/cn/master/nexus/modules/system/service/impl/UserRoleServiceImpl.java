package cn.master.nexus.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.nexus.modules.system.entity.UserRole;
import cn.master.nexus.modules.system.mapper.UserRoleMapper;
import cn.master.nexus.modules.system.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户组 服务层实现。
 *
 * @author 11's papa
 * @since 2026-03-03
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>  implements UserRoleService{

}
