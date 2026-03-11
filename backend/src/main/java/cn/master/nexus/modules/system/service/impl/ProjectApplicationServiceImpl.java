package cn.master.nexus.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.nexus.modules.system.entity.ProjectApplication;
import cn.master.nexus.modules.system.mapper.ProjectApplicationMapper;
import cn.master.nexus.modules.system.service.ProjectApplicationService;
import org.springframework.stereotype.Service;

/**
 * 项目应用 服务层实现。
 *
 * @author 11's papa
 * @since 2026-03-03
 */
@Service
public class ProjectApplicationServiceImpl extends ServiceImpl<ProjectApplicationMapper, ProjectApplication>  implements ProjectApplicationService{

}
