package cn.master.nexus.modules.system.service.impl;

import cn.master.nexus.common.constants.InternalUserRole;
import cn.master.nexus.common.constants.ProjectApplicationType;
import cn.master.nexus.common.util.LogUtils;
import cn.master.nexus.modules.system.entity.ProjectApplication;
import cn.master.nexus.modules.system.entity.ProjectVersion;
import cn.master.nexus.modules.system.mapper.ProjectVersionMapper;
import cn.master.nexus.modules.system.service.CreateProjectResourceService;
import cn.master.nexus.modules.system.service.ProjectApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author : 11's papa
 * @since : 2026/3/5, 星期四
 **/
@Component
@RequiredArgsConstructor
public class CreateVersionResourceService implements CreateProjectResourceService {
    public static final String DEFAULT_VERSION = "v1.0";
    public static final String DEFAULT_VERSION_STATUS = "open";
    private final ProjectVersionMapper projectVersionMapper;
    private final ProjectApplicationService projectApplicationService;

    @Override
    public void createResources(String projectId) {
// 初始化版本V1.0, 初始化版本配置项
        ProjectVersion defaultVersion = new ProjectVersion();
        defaultVersion.setProjectId(projectId);
        defaultVersion.setName(DEFAULT_VERSION);
        defaultVersion.setStatus(DEFAULT_VERSION_STATUS);
        defaultVersion.setLatest(true);
        defaultVersion.setCreateUser(InternalUserRole.ADMIN.getValue());
        projectVersionMapper.insertSelective(defaultVersion);
        ProjectApplication projectApplication = new ProjectApplication();
        projectApplication.setProjectId(projectId);
        projectApplication.setType(ProjectApplicationType.VERSION.VERSION_ENABLE.name());
        projectApplication.setTypeValue("FALSE");
        projectApplicationService.saveOrUpdate(projectApplication);
        LogUtils.info("初始化当前项目[" + projectId + "]相关版本资源");
    }
}
