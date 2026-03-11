package cn.master.nexus.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.nexus.modules.system.entity.ProjectParameter;
import cn.master.nexus.modules.system.mapper.ProjectParameterMapper;
import cn.master.nexus.modules.system.service.ProjectParameterService;
import org.springframework.stereotype.Service;

/**
 * 项目级参数 服务层实现。
 *
 * @author 11's papa
 * @since 2026-03-03
 */
@Service
public class ProjectParameterServiceImpl extends ServiceImpl<ProjectParameterMapper, ProjectParameter>  implements ProjectParameterService{

}
