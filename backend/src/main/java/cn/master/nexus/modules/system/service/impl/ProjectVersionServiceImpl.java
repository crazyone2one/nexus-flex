package cn.master.nexus.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.nexus.modules.system.entity.ProjectVersion;
import cn.master.nexus.modules.system.mapper.ProjectVersionMapper;
import cn.master.nexus.modules.system.service.ProjectVersionService;
import org.springframework.stereotype.Service;

/**
 * 版本管理 服务层实现。
 *
 * @author 11's papa
 * @since 2026-03-03
 */
@Service
public class ProjectVersionServiceImpl extends ServiceImpl<ProjectVersionMapper, ProjectVersion>  implements ProjectVersionService{

}
