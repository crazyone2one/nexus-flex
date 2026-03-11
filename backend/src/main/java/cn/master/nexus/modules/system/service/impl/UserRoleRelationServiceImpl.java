package cn.master.nexus.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.nexus.modules.system.entity.UserRoleRelation;
import cn.master.nexus.modules.system.mapper.UserRoleRelationMapper;
import cn.master.nexus.modules.system.service.UserRoleRelationService;
import org.springframework.stereotype.Service;

/**
 * 用户组关系 服务层实现。
 *
 * @author 11's papa
 * @since 2026-03-03
 */
@Service
public class UserRoleRelationServiceImpl extends ServiceImpl<UserRoleRelationMapper, UserRoleRelation>  implements UserRoleRelationService{

}
