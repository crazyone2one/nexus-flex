package cn.master.nexus.modules.system.service.impl;

import cn.master.nexus.common.constants.*;
import cn.master.nexus.common.exception.BusinessException;
import cn.master.nexus.common.invoker.ProjectServiceInvoker;
import cn.master.nexus.common.util.JSON;
import cn.master.nexus.common.util.Translator;
import cn.master.nexus.modules.log.dto.LogDTO;
import cn.master.nexus.modules.log.service.OperationLogService;
import cn.master.nexus.modules.system.dto.AddProjectRequest;
import cn.master.nexus.modules.system.dto.ProjectDTO;
import cn.master.nexus.modules.system.dto.UpdateProjectNameRequest;
import cn.master.nexus.modules.system.dto.UpdateProjectRequest;
import cn.master.nexus.modules.system.dto.request.ProjectAddMemberBatchRequest;
import cn.master.nexus.modules.system.entity.Project;
import cn.master.nexus.modules.system.entity.SystemUser;
import cn.master.nexus.modules.system.entity.UserRoleRelation;
import cn.master.nexus.modules.system.mapper.ProjectMapper;
import cn.master.nexus.modules.system.mapper.SystemUserMapper;
import cn.master.nexus.modules.system.mapper.UserRoleRelationMapper;
import cn.master.nexus.modules.system.service.ProjectService;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.master.nexus.modules.system.entity.table.ProjectTableDef.PROJECT;
import static cn.master.nexus.modules.system.entity.table.SystemUserTableDef.SYSTEM_USER;
import static cn.master.nexus.modules.system.entity.table.UserRoleRelationTableDef.USER_ROLE_RELATION;

/**
 * 项目 服务层实现。
 *
 * @author 11's papa
 * @since 2026-03-03
 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectService {
    private final ProjectServiceInvoker serviceInvoker;
    private final Translator translator;
    private final UserRoleRelationMapper userRoleRelationMapper;
    private final OperationLogService operationLogService;
    private final SystemUserMapper systemUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectDTO add(AddProjectRequest addProjectDTO, String createUser, String path, String module) {
        Project project = new Project();
        ProjectDTO projectDTO = new ProjectDTO();

        project.setName(addProjectDTO.getName());
        project.setOrganizationId(addProjectDTO.getOrganizationId());
        checkProjectExistByName(project);
        project.setUpdateUser(createUser);
        project.setCreateUser(createUser);
        project.setEnable(addProjectDTO.getEnable());
        project.setAllResourcePool(addProjectDTO.isAllResourcePool());
        project.setDescription(addProjectDTO.getDescription());

        // 判断是否有模块设置
        if (CollectionUtils.isNotEmpty(addProjectDTO.getModuleIds())) {
            project.setModuleSetting(addProjectDTO.getModuleIds());
            projectDTO.setModuleIds(addProjectDTO.getModuleIds());
        }
        mapper.insertSelective(project);
        BeanUtils.copyProperties(project, projectDTO);
        serviceInvoker.invokeCreateServices(project.getId());
        ProjectAddMemberBatchRequest memberRequest = new ProjectAddMemberBatchRequest();
        memberRequest.setProjectIds(List.of(project.getId()));
        memberRequest.setUserIds(addProjectDTO.getUserIds());
        addProjectAdmin(memberRequest, createUser, path, OperationLogType.ADD.name(), translator.get("add"), module);
        return projectDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(String id, String userName) {
        checkProjectNotExist(id);
        Project project = new Project();
        project.setId(id);
        project.setDeleteUser(userName);
        project.setDeleted(true);
        project.setDeleteTime(LocalDateTime.now());
        return mapper.update(project);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectDTO update(UpdateProjectRequest request, String updateUser, String path, String module) {
        Project project = new Project();
        ProjectDTO projectDTO = new ProjectDTO();
        project.setId(request.getId());
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setOrganizationId(request.getOrganizationId());
        project.setEnable(request.getEnable());
        project.setAllResourcePool(request.isAllResourcePool());
        project.setUpdateUser(updateUser);
        checkProjectExistByName(project);
        checkProjectNotExist(project.getId());
        BeanUtils.copyProperties(project, projectDTO);

        List<UserRoleRelation> userRoleRelations = QueryChain.of(UserRoleRelation.class).where(UserRoleRelation::getSourceId).eq(project.getId())
                .and(UserRoleRelation::getRoleId).eq(InternalUserRole.PROJECT_ADMIN.getValue()).list();
        List<String> orgUserIds = userRoleRelations.stream().map(UserRoleRelation::getUserId).toList();
        List<LogDTO> logDTOList = new ArrayList<>();
        List<String> deleteIds = orgUserIds.stream()
                .filter(item -> !request.getUserIds().contains(item))
                .toList();

        List<String> insertIds = request.getUserIds().stream()
                .filter(item -> !orgUserIds.contains(item))
                .toList();
        if (CollectionUtils.isNotEmpty(deleteIds)) {
            QueryChain<UserRoleRelation> deleteChain = QueryChain.of(UserRoleRelation.class).where(UserRoleRelation::getSourceId).eq(project.getId())
                    .and(UserRoleRelation::getRoleId).eq(InternalUserRole.PROJECT_ADMIN.getValue())
                    .and(UserRoleRelation::getUserId).in(deleteIds);
            Objects.requireNonNull(userRoleRelationMapper.selectListByQuery(deleteChain)).forEach(userRoleRelation -> {
                SystemUser user = systemUserMapper.selectOneById(userRoleRelation.getUserId());
                String logProjectId = OperationLogConstants.SYSTEM;
                if (OperationLogModule.SETTING_ORGANIZATION_PROJECT.equals(module)) {
                    logProjectId = OperationLogConstants.ORGANIZATION;
                }
                LogDTO logDTO = new LogDTO(logProjectId, project.getOrganizationId(), userRoleRelation.getId(), updateUser, OperationLogType.DELETE.name(), module, translator.get("delete") + translator.get("project_admin") + ": " + user.getName());
                setLog(logDTO, path, HttpMethodConstants.POST.name(), logDTOList);
            });
            userRoleRelationMapper.deleteByQuery(deleteChain);
        }
        if (CollectionUtils.isNotEmpty(insertIds)) {
            ProjectAddMemberBatchRequest memberRequest = new ProjectAddMemberBatchRequest();
            memberRequest.setProjectIds(List.of(project.getId()));
            memberRequest.setUserIds(insertIds);
            addProjectAdmin(memberRequest, updateUser, path, OperationLogType.ADD.name(), translator.get("add"), module);
        }
        if (CollectionUtils.isNotEmpty(logDTOList)) {
            operationLogService.batchAdd(logDTOList);
        }
        // 判断是否有模块设置
        if (CollectionUtils.isNotEmpty(request.getModuleIds())) {
            project.setModuleSetting(request.getModuleIds());
            projectDTO.setModuleIds(request.getModuleIds());
        } else {
            project.setModuleSetting(new ArrayList<>());
            projectDTO.setModuleIds(new ArrayList<>());
        }
        mapper.update(project);
        return projectDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(String id, String updateUser) {
        updateChain().set(PROJECT.ENABLE, true)
                .set(PROJECT.UPDATE_USER, updateUser)
                .where(PROJECT.ID.eq(id)).update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(String id, String updateUser) {
        updateChain().set(PROJECT.ENABLE, false)
                .set(PROJECT.UPDATE_USER, updateUser)
                .where(PROJECT.ID.eq(id)).update();
    }

    @Override
    public void rename(UpdateProjectNameRequest request, String userName) {
        checkProjectNotExist(request.id());
        Project project = Project.builder().id(request.id()).name(request.name()).organizationId(request.organizationId()).build();
        checkProjectExistByName(project);
        project.setUpdateUser(userName);
        mapper.update(project);
    }

    @Override
    public void buildUserInfo(List<ProjectDTO> records) {
        if (!records.isEmpty()) {
            List<String> projectIds = records.stream().map(ProjectDTO::getId).toList();
            List<ProjectDTO> projectDTOList = getProjectExtendDTOList(projectIds);
            Map<String, ProjectDTO> projectMap = projectDTOList.stream().collect(Collectors.toMap(ProjectDTO::getId, projectDTO -> projectDTO));
            records.forEach(projectDTO -> {
                projectDTO.setMemberCount(projectMap.get(projectDTO.getId()).getMemberCount());
                if (CollectionUtils.isNotEmpty(projectDTO.getModuleSetting())) {
                    projectDTO.setModuleIds(projectDTO.getModuleSetting());
                }
            });
        }
    }

    private List<ProjectDTO> getProjectExtendDTOList(List<String> projectIds) {
        QueryChain<UserRoleRelation> queryChain = QueryChain.of(UserRoleRelation.class)
                .select(USER_ROLE_RELATION.SOURCE_ID, SYSTEM_USER.ID).from(USER_ROLE_RELATION)
                .leftJoin(SYSTEM_USER).on(USER_ROLE_RELATION.USER_ID.eq(SYSTEM_USER.ID))
                .where(USER_ROLE_RELATION.SOURCE_ID.in(projectIds));

        return queryChain().select(PROJECT.ID)
                .select("count(distinct temp.id) as memberCount")
                .from(PROJECT)
                .leftJoin(queryChain).as("temp").on(PROJECT.ID.eq("temp.source_id"))
                .groupBy(PROJECT.ID)
                .listAs(ProjectDTO.class);
    }

    private void addProjectAdmin(ProjectAddMemberBatchRequest request, String createUser, String path, String type,
                                 String content, String module) {
        List<LogDTO> logDTOList = new ArrayList<>();
        List<UserRoleRelation> userRoleRelations = new ArrayList<>();
        request.getProjectIds().forEach(projectId -> {
            Project project = mapper.selectOneById(projectId);
            Map<String, String> nameMap = addUserPre(request.getUserIds(), createUser, path, module, projectId, project);
            request.getUserIds().forEach(userId -> {
                QueryChain<UserRoleRelation> queryChain = QueryChain.of(UserRoleRelation.class)
                        .where(UserRoleRelation::getUserId).eq(userId)
                        .and(UserRoleRelation::getSourceId).eq(projectId)
                        .and(UserRoleRelation::getRoleId).eq(InternalUserRole.PROJECT_ADMIN.getValue());
                if (!queryChain.exists()) {
                    UserRoleRelation adminRole = new UserRoleRelation();
                    adminRole.setUserId(userId);
                    adminRole.setRoleId(InternalUserRole.PROJECT_ADMIN.getValue());
                    adminRole.setSourceId(projectId);
                    adminRole.setCreateUser(createUser);
                    adminRole.setOrganizationId(project.getOrganizationId());
                    userRoleRelations.add(adminRole);
                    String logProjectId = OperationLogConstants.SYSTEM;
                    if (OperationLogModule.SETTING_ORGANIZATION_PROJECT.equals(module)) {
                        logProjectId = OperationLogConstants.ORGANIZATION;
                    }
                    LogDTO logDTO = new LogDTO(logProjectId, project.getOrganizationId(), adminRole.getId(), createUser, type, module, content + translator.get("project_admin") + ": " + nameMap.get(userId));
                    setLog(logDTO, path, HttpMethodConstants.POST.name(), logDTOList);
                }
            });
        });
        if (CollectionUtils.isNotEmpty(userRoleRelations)) {
            userRoleRelationMapper.insertBatch(userRoleRelations);
        }
        operationLogService.batchAdd(logDTOList);
    }

    private Map<String, String> addUserPre(List<String> userIds, String createUser, String path, String module, String projectId, Project project) {
        checkProjectNotExist(projectId);
        List<SystemUser> users = QueryChain.of(SystemUser.class).where(SystemUser::getId).in(userIds).list();
        if (userIds.size() != users.size()) {
            throw new BusinessException(translator.get("user_not_exist"));
        }
        // 把id和名称放一个map中
        Map<String, String> userMap = users.stream().collect(Collectors.toMap(SystemUser::getId, SystemUser::getName));
        checkOrgRoleExit(userIds, project.getOrganizationId(), createUser, userMap, path, module);
        return userMap;
    }

    private void checkProjectNotExist(String projectId) {
        queryChain().where(Project::getId).eq(projectId).oneOpt()
                .orElseThrow(() -> new BusinessException(translator.get("project_is_not_exist")));
    }

    private void checkOrgRoleExit(List<String> userId, String orgId, String createUser, Map<String, String> nameMap, String path, String module) {
        List<LogDTO> logDTOList = new ArrayList<>();
        List<UserRoleRelation> userRoleRelations = QueryChain.of(UserRoleRelation.class).where(UserRoleRelation::getUserId).in(userId)
                .and(UserRoleRelation::getSourceId).eq(orgId).list();
        List<String> orgUserIds = userRoleRelations.stream().map(UserRoleRelation::getUserId).toList();
        if (CollectionUtils.isNotEmpty(userId)) {
            List<UserRoleRelation> userRoleRelation = new ArrayList<>();
            userId.forEach(id -> {
                if (!orgUserIds.contains(id)) {
                    UserRoleRelation memberRole = new UserRoleRelation();
                    memberRole.setUserId(id);
                    memberRole.setRoleId(InternalUserRole.ORG_MEMBER.getValue());
                    memberRole.setSourceId(orgId);
                    memberRole.setCreateUser(createUser);
                    memberRole.setOrganizationId(orgId);
                    userRoleRelation.add(memberRole);
                    LogDTO logDTO = new LogDTO(orgId, orgId, memberRole.getId(), createUser, OperationLogType.ADD.name(), module, translator.get("add") + translator.get("organization_member") + ": " + nameMap.get(id));
                    setLog(logDTO, path, HttpMethodConstants.POST.name(), logDTOList);
                }
            });
            if (CollectionUtils.isNotEmpty(userRoleRelation)) {
                userRoleRelationMapper.insertBatch(userRoleRelation);
            }

        }
        operationLogService.batchAdd(logDTOList);
    }

    private void setLog(LogDTO dto, String path, String method, List<LogDTO> logDTOList) {
        dto.setPath(path);
        dto.setMethod(method);
        dto.setOriginalValue(JSON.toJSONBytes(StringUtils.EMPTY));
        logDTOList.add(dto);
    }

    private void checkProjectExistByName(Project project) {
        boolean exists = queryChain().where(Project::getName).eq(project.getName())
                .and(Project::getOrganizationId).eq(project.getOrganizationId())
                .and(Project::getId).ne(project.getId()).exists();
        if (exists) {
            throw new BusinessException(translator.get("project_name_already_exists"));
        }
    }
}
