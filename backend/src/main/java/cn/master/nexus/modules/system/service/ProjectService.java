package cn.master.nexus.modules.system.service;

import cn.master.nexus.modules.system.dto.AddProjectRequest;
import cn.master.nexus.modules.system.dto.ProjectDTO;
import cn.master.nexus.modules.system.dto.UpdateProjectNameRequest;
import cn.master.nexus.modules.system.dto.UpdateProjectRequest;
import cn.master.nexus.modules.system.entity.Project;
import com.mybatisflex.core.service.IService;

import java.util.List;

/**
 * 项目 服务层。
 *
 * @author 11's papa
 * @since 2026-03-03
 */
public interface ProjectService extends IService<Project> {
    ProjectDTO add(AddProjectRequest addProjectDTO, String createUser, String path, String module);

    int delete(String id, String userName);

    ProjectDTO update(UpdateProjectRequest request, String updateUser, String path, String module);

    void enable(String id, String updateUser);

    void disable(String id, String updateUser);

    void rename(UpdateProjectNameRequest request, String userName);

    void buildUserInfo(List<ProjectDTO> records);
}
