package cn.master.nexus.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.nexus.modules.system.entity.SystemOrganization;
import cn.master.nexus.modules.system.mapper.SystemOrganizationMapper;
import cn.master.nexus.modules.system.service.SystemOrganizationService;
import org.springframework.stereotype.Service;

/**
 * 组织 服务层实现。
 *
 * @author 11's papa
 * @since 2026-03-03
 */
@Service
public class SystemOrganizationServiceImpl extends ServiceImpl<SystemOrganizationMapper, SystemOrganization>  implements SystemOrganizationService{

}
