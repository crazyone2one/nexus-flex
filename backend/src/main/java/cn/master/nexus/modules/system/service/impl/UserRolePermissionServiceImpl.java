package cn.master.nexus.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.nexus.modules.system.entity.UserRolePermission;
import cn.master.nexus.modules.system.mapper.UserRolePermissionMapper;
import cn.master.nexus.modules.system.service.UserRolePermissionService;
import org.springframework.stereotype.Service;

/**
 * 用户组权限 服务层实现。
 *
 * @author 11's papa
 * @since 2026-03-03
 */
@Service
public class UserRolePermissionServiceImpl extends ServiceImpl<UserRolePermissionMapper, UserRolePermission>  implements UserRolePermissionService{

}
