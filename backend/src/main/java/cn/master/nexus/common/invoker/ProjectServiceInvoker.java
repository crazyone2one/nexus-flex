package cn.master.nexus.common.invoker;

import cn.master.nexus.modules.system.service.CleanupProjectResourceService;
import cn.master.nexus.modules.system.service.CreateProjectResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : 11's papa
 * @since : 2026/3/5, 星期四
 **/
@Component
@RequiredArgsConstructor
public class ProjectServiceInvoker {
    private final List<CreateProjectResourceService> createProjectResourceServices;
    private final List<CleanupProjectResourceService> cleanupProjectResourceServices;

    public void invokeCreateServices(String projectId) {
        for (CreateProjectResourceService service : createProjectResourceServices) {
            service.createResources(projectId);
        }
    }

    public void invokeServices(String projectId) {
        for (CleanupProjectResourceService service : cleanupProjectResourceServices) {
            service.deleteResources(projectId);
        }
    }
}
