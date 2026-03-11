package cn.master.nexus.modules.system.service.impl;

import cn.master.nexus.common.util.LogUtils;
import cn.master.nexus.modules.system.entity.ProjectApplication;
import cn.master.nexus.modules.system.entity.ProjectVersion;
import cn.master.nexus.modules.system.mapper.ProjectApplicationMapper;
import cn.master.nexus.modules.system.mapper.ProjectVersionMapper;
import cn.master.nexus.modules.system.service.CleanupProjectResourceService;
import com.mybatisflex.core.query.QueryChain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author : 11's papa
 * @since : 2026/3/5, 星期四
 **/
@Component
@RequiredArgsConstructor
public class CleanupVersionResourceService implements CleanupProjectResourceService {
    private final ProjectVersionMapper projectVersionMapper;
    private final ProjectApplicationMapper projectApplicationMapper;

    @Override
    public void deleteResources(String projectId) {
        QueryChain<ProjectVersionMapper> versionMapperQueryChain = QueryChain.of(ProjectVersionMapper.class).where(ProjectVersion::getProjectId).eq(projectId);
        projectVersionMapper.deleteByQuery(versionMapperQueryChain);
        QueryChain<ProjectApplication> queryChain = QueryChain.of(ProjectApplication.class).where(ProjectApplication::getProjectId).eq(projectId);
        projectApplicationMapper.deleteByQuery(queryChain);
        LogUtils.info("清理当前项目[" + projectId + "]相关版本资源");
    }
}
