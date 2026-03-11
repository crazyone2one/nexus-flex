package cn.master.nexus.modules.system.service;

import cn.master.nexus.common.constants.OperationLogModule;
import cn.master.nexus.modules.system.dto.AddProjectRequest;
import cn.master.nexus.modules.system.dto.ProjectDTO;
import cn.master.nexus.modules.system.dto.UpdateProjectNameRequest;
import cn.master.nexus.modules.system.dto.UpdateProjectRequest;
import cn.master.nexus.modules.system.dto.request.ProjectRequest;
import cn.master.nexus.modules.system.entity.Project;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.master.nexus.modules.system.entity.table.ProjectTableDef.PROJECT;
import static cn.master.nexus.modules.system.entity.table.SystemOrganizationTableDef.SYSTEM_ORGANIZATION;

/**
 * @author : 11's papa
 * @since : 2026/3/5, 星期四
 **/
@Service
@RequiredArgsConstructor
public class SystemProjectService {
    private final ProjectService projectService;
    private final static String PREFIX = "/system/project";
    private final static String ADD_PROJECT = PREFIX + "/add";
    private final static String UPDATE_PROJECT = PREFIX + "/update";
    private final static String REMOVE_PROJECT_MEMBER = PREFIX + "/remove-member/";
    private final static String ADD_MEMBER = PREFIX + "/add-member";

    public ProjectDTO add(AddProjectRequest addProjectDTO, String createUser) {
        return projectService.add(addProjectDTO, createUser, ADD_PROJECT, OperationLogModule.SETTING_SYSTEM_ORGANIZATION);
    }

    public int delete(String id, String userName) {
        return projectService.delete(id, userName);
    }

    public ProjectDTO update(UpdateProjectRequest request, String updateUser) {
        return projectService.update(request, updateUser, UPDATE_PROJECT, OperationLogModule.SETTING_SYSTEM_ORGANIZATION);
    }

    public Page<ProjectDTO> page(ProjectRequest request) {
        Page<ProjectDTO> page = QueryChain.of(Project.class).select(PROJECT.ALL_COLUMNS, SYSTEM_ORGANIZATION.NAME.as("organizationName"))
                .from(PROJECT)
                .innerJoin(SYSTEM_ORGANIZATION).on(SYSTEM_ORGANIZATION.ID.eq(PROJECT.ORGANIZATION_ID))
                .where(PROJECT.ORGANIZATION_ID.eq(request.getOrganizationId()))
                .and(PROJECT.NAME.like(request.getKeyword()).or(PROJECT.NUM.like(request.getKeyword())))
                .orderBy(PROJECT.CREATE_TIME.desc())
                .pageAs(new Page<>(request.getCurrent(), request.getPageSize()), ProjectDTO.class);
        projectService.buildUserInfo(page.getRecords());
        return page;
    }

    public List<Project> list() {
        return null;
    }

    public Project get(String id) {
        return null;
    }

    public void rename(UpdateProjectNameRequest request, String userName) {
        projectService.rename(request, userName);
    }

    public void disable(String id, String userName) {
        projectService.disable(id, userName);
    }

    public void enable(String id, String userName) {
        projectService.enable(id, userName);
    }
}
