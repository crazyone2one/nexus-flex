package cn.master.nexus.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.nexus.modules.system.entity.OrganizationParameter;
import cn.master.nexus.modules.system.mapper.OrganizationParameterMapper;
import cn.master.nexus.modules.system.service.OrganizationParameterService;
import org.springframework.stereotype.Service;

/**
 * 组织参数 服务层实现。
 *
 * @author 11's papa
 * @since 2026-03-03
 */
@Service
public class OrganizationParameterServiceImpl extends ServiceImpl<OrganizationParameterMapper, OrganizationParameter>  implements OrganizationParameterService{

}
