package cn.master.nexus.auth.service;

import cn.master.nexus.auth.dto.UserDTO;
import cn.master.nexus.auth.dto.UserRolePermissionDTO;
import cn.master.nexus.auth.dto.UserRoleResourceDTO;
import cn.master.nexus.common.constants.UserRoleType;
import cn.master.nexus.common.exception.BusinessException;
import cn.master.nexus.common.util.Translator;
import cn.master.nexus.modules.system.entity.*;
import cn.master.nexus.modules.system.mapper.SystemUserMapper;
import com.mybatisflex.core.query.QueryChain;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.master.nexus.modules.system.entity.table.ProjectTableDef.PROJECT;
import static cn.master.nexus.modules.system.entity.table.SystemOrganizationTableDef.SYSTEM_ORGANIZATION;
import static cn.master.nexus.modules.system.entity.table.SystemUserTableDef.SYSTEM_USER;
import static cn.master.nexus.modules.system.entity.table.UserRolePermissionTableDef.USER_ROLE_PERMISSION;
import static cn.master.nexus.modules.system.entity.table.UserRoleRelationTableDef.USER_ROLE_RELATION;
import static cn.master.nexus.modules.system.entity.table.UserRoleTableDef.USER_ROLE;

/**
 * @author : 11's papa
 * @since : 2026/3/10, 星期二
 **/
@Service
@RequiredArgsConstructor
public class AuthService {
    private final Translator translator;
    private final SystemUserMapper systemUserMapper;

    public UserDTO getUserDTO(String userId) {
        UserDTO userDTO = QueryChain.of(SystemUser.class).where(SYSTEM_USER.ID.eq(userId)).oneAs(UserDTO.class);
        if (userDTO == null) {
            return null;
        }
        UserRolePermissionDTO dto = getUserRolePermission(userId);
        userDTO.setUserRoleRelations(dto.getUserRoleRelations());
        userDTO.setUserRoles(dto.getUserRoles());
        userDTO.setUserRolePermissions(dto.getList());
        return userDTO;
    }

    private UserRolePermissionDTO getUserRolePermission(String userId) {
        UserRolePermissionDTO permissionDTO = new UserRolePermissionDTO();
        List<UserRoleResourceDTO> list = new ArrayList<>();
        List<UserRoleRelation> userRoleRelations = QueryChain.of(UserRoleRelation.class)
                .where(USER_ROLE_RELATION.USER_ID.eq(userId)).list();
        if (CollectionUtils.isEmpty(userRoleRelations)) {
            return permissionDTO;
        }
        permissionDTO.setUserRoleRelations(userRoleRelations);
        List<String> roleList = userRoleRelations.stream().map(UserRoleRelation::getRoleId).toList();
        List<UserRole> userRoles = QueryChain.of(UserRole.class).where(USER_ROLE.ID.in(roleList)).list();
        permissionDTO.setUserRoles(userRoles);
        for (UserRole ur : userRoles) {
            UserRoleResourceDTO dto = new UserRoleResourceDTO();
            dto.setUserRole(ur);
            List<UserRolePermission> userRolePermissions = QueryChain.of(UserRolePermission.class).where(USER_ROLE_PERMISSION.ROLE_ID.eq(ur.getId())).list();
            dto.setUserRolePermissions(userRolePermissions);
            list.add(dto);
        }
        permissionDTO.setList(list);
        return permissionDTO;
    }

    public void autoSwitch(UserDTO user) {
        // 判断是否是系统管理员
        if (isSystemAdmin(user)) {
            return;
        }
        // 用户有 last_project_id 权限
        if (hasLastProjectPermission(user)) {
            return;
        }
        // 用户有 last_organization_id 权限
        if (hasLastOrganizationPermission(user)) {
            return;
        }
        // 判断其他权限
        checkNewOrganizationAndProject(user);
    }

    private void checkNewOrganizationAndProject(UserDTO user) {
        List<UserRoleRelation> userRoleRelations = user.getUserRoleRelations();
        List<String> projectRoleIds = user.getUserRoles()
                .stream().filter(ug -> Strings.CS.equals(ug.getType(), UserRoleType.PROJECT.name()))
                .map(UserRole::getId)
                .toList();
        List<UserRoleRelation> project = userRoleRelations.stream().filter(ug -> projectRoleIds.contains(ug.getRoleId()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(project)) {
            List<String> organizationIds = user.getUserRoles()
                    .stream()
                    .filter(ug -> Strings.CS.equals(ug.getType(), UserRoleType.ORGANIZATION.name()))
                    .map(UserRole::getId)
                    .toList();
            List<UserRoleRelation> organizations = userRoleRelations.stream().filter(ug -> organizationIds.contains(ug.getRoleId()))
                    .toList();
            if (CollectionUtils.isNotEmpty(organizations)) {
                // 获取所有的组织
                List<String> orgIds = organizations.stream().map(UserRoleRelation::getSourceId).toList();
                List<SystemOrganization> organizationsList = QueryChain.of(SystemOrganization.class)
                        .where(SYSTEM_ORGANIZATION.ID.in(orgIds).and(SYSTEM_ORGANIZATION.ENABLE.eq(true))).list();
                if (CollectionUtils.isNotEmpty(organizationsList)) {
                    String wsId = organizationsList.getFirst().getId();
                    switchUserResource(wsId, user);
                }
            } else {
                // 用户登录之后没有项目和组织的权限就把值清空
                user.setLastOrganizationId(StringUtils.EMPTY);
                user.setLastProjectId(StringUtils.EMPTY);
                updateUser(user);
            }
        } else {
            UserRoleRelation userRoleRelation = project.stream().filter(p -> StringUtils.isNotBlank(p.getSourceId()))
                    .toList().getFirst();
            String projectId = userRoleRelation.getSourceId();
            Project p = QueryChain.of(Project.class).where(PROJECT.ID.eq(projectId)).one();
            String wsId = p.getOrganizationId();
            user.setId(user.getId());
            user.setLastProjectId(projectId);
            user.setLastOrganizationId(wsId);
            updateUser(user);
        }
    }

    public void switchUserResource(String wsId, UserDTO user) {
        // UserDTO userDTO = getUserDTO(SessionUtils.getUserId());
        user.setLastOrganizationId(wsId);
        user.setLastProjectId(StringUtils.EMPTY);
        List<Project> projects = getProjectListByWsAndUserId(user.getId(), wsId);
        if (CollectionUtils.isNotEmpty(projects)) {
            user.setLastProjectId(projects.getFirst().getId());
        }
        SystemUser newUser = new SystemUser();
        BeanUtils.copyProperties(user, newUser);
        systemUserMapper.update(newUser);
    }

    public void updateUser(UserDTO user) {
        if (StringUtils.isNotBlank(user.getEmail())) {
            boolean exists = QueryChain.of(SystemUser.class).where(SYSTEM_USER.ID.ne(user.getId())
                    .and(SYSTEM_USER.EMAIL.eq(user.getEmail()))).exists();
            if (exists) {
                throw new BusinessException(translator.get("user_email_already_exists"));
            }
        }
        // 变更前
        SystemUser oldUser = QueryChain.of(SystemUser.class).where(SYSTEM_USER.ID.eq(user.getId())).one();
        if (user.getLastOrganizationId() != null && !Strings.CS.equals(user.getLastOrganizationId(), oldUser.getLastOrganizationId())
                && !isSuperUser(user.getId())) {
            List<Project> projects = getProjectListByWsAndUserId(user.getId(), user.getLastOrganizationId());
            if (!projects.isEmpty()) {
                // 如果传入的 last_project_id 是 last_organization_id 下面的
                boolean present = projects.stream().anyMatch(p -> Strings.CS.equals(p.getId(), user.getLastProjectId()));
                if (!present) {
                    user.setLastProjectId(projects.getFirst().getId());
                }
            } else {
                user.setLastProjectId(StringUtils.EMPTY);
            }
        }
        systemUserMapper.update(user);
    }

    private List<Project> getProjectListByWsAndUserId(String userId, String organizationId) {
        List<Project> projects = QueryChain.of(Project.class).where(PROJECT.ORGANIZATION_ID.eq(organizationId)
                .and(PROJECT.ENABLE.eq(true))).list();
        List<UserRoleRelation> userRoleRelations = QueryChain.of(UserRoleRelation.class).where(USER_ROLE_RELATION.USER_ID.eq(userId)).list();
        List<Project> projectList = new ArrayList<>();
        userRoleRelations.forEach(userRoleRelation -> projects.forEach(project -> {
            if (Strings.CS.equals(userRoleRelation.getSourceId(), project.getId())) {
                if (!projectList.contains(project)) {
                    projectList.add(project);
                }
            }
        }));
        return projectList;
    }

    private boolean hasLastOrganizationPermission(UserDTO user) {
        if (StringUtils.isNotBlank(user.getLastOrganizationId())) {
            List<SystemOrganization> organizations = QueryChain.of(SystemOrganization.class)
                    .where(SYSTEM_ORGANIZATION.ID.eq(user.getLastOrganizationId())
                            .and(SYSTEM_ORGANIZATION.ENABLE.eq(true))).list();
            if (organizations.isEmpty()) {
                return false;
            }
            List<UserRoleRelation> userRoleRelations = user.getUserRoleRelations().stream()
                    .filter(ug -> Strings.CS.equals(user.getLastOrganizationId(), ug.getSourceId()))
                    .toList();
            if (CollectionUtils.isNotEmpty(userRoleRelations)) {
                List<Project> projects = QueryChain.of(Project.class)
                        .where(PROJECT.ORGANIZATION_ID.eq(user.getLastOrganizationId())
                                .and(PROJECT.ENABLE.eq(true))).list();
                // 组织下没有项目
                if (CollectionUtils.isEmpty(projects)) {
                    user.setLastProjectId(StringUtils.EMPTY);
                    updateUser(user);
                    return true;
                }
                // 组织下有项目，选中有权限的项目
                List<String> projectIds = projects.stream().map(Project::getId).toList();
                List<UserRoleRelation> roleRelations = user.getUserRoleRelations();
                List<String> projectRoleIds = user.getUserRoles()
                        .stream().filter(ug -> UserRoleType.PROJECT.name().equals(ug.getType()))
                        .map(UserRole::getId)
                        .toList();
                List<String> projectIdsWithPermission = roleRelations.stream().filter(ug -> projectRoleIds.contains(ug.getRoleId()))
                        .map(UserRoleRelation::getSourceId)
                        .filter(StringUtils::isNotBlank)
                        .filter(projectIds::contains)
                        .toList();
                List<String> intersection = projectIds.stream().filter(projectIdsWithPermission::contains).toList();
                // 当前组织下的所有项目都没有权限
                if (CollectionUtils.isEmpty(intersection)) {
                    user.setLastProjectId(StringUtils.EMPTY);
                    updateUser(user);
                    return true;
                }
                Optional<Project> first = projects.stream().filter(p -> intersection.getFirst().equals(p.getId())).findFirst();
                if (first.isPresent()) {
                    Project project = first.get();
                    String wsId = project.getOrganizationId();
                    user.setId(user.getId());
                    user.setLastProjectId(project.getId());
                    user.setLastOrganizationId(wsId);
                    updateUser(user);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasLastProjectPermission(UserDTO user) {
        if (StringUtils.isNotBlank(user.getLastProjectId())) {
            List<UserRoleRelation> userRoleRelations = user.getUserRoleRelations().stream()
                    .filter(ug -> Strings.CS.equals(user.getLastProjectId(), ug.getSourceId()))
                    .toList();
            if (CollectionUtils.isNotEmpty(userRoleRelations)) {
                List<Project> projects = QueryChain.of(Project.class).where(PROJECT.ID.eq(user.getLastProjectId())
                        .and(PROJECT.ENABLE.eq(true))).list();
                if (CollectionUtils.isNotEmpty(projects)) {
                    Project project = projects.getFirst();
                    if (Strings.CS.equals(project.getOrganizationId(), user.getLastOrganizationId())) {
                        return true;
                    }
                    // last_project_id 和 last_organization_id 对应不上了
                    user.setLastOrganizationId(project.getOrganizationId());
                    updateUser(user);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isSystemAdmin(UserDTO user) {
        if (isSuperUser(user.getId())) {
            // 如果是系统管理员，判断是否有项目权限
            if (StringUtils.isNotBlank(user.getLastProjectId())) {
                List<Project> projects = QueryChain.of(Project.class).where(PROJECT.ID.eq(user.getLastProjectId())
                        .and(PROJECT.ENABLE.eq(true))).list();
                if (CollectionUtils.isNotEmpty(projects)) {
                    Project project = projects.getFirst();
                    if (user.getLastOrganizationId().equals(project.getOrganizationId())) {
                        return true;
                    }
                    // last_project_id 和 last_organization_id 对应不上了
                    user.setLastOrganizationId(project.getOrganizationId());
                    updateUser(user);
                    return true;
                }
            }
            // 项目没有权限  则取当前组织下的第一个项目
            if (StringUtils.isNotBlank(user.getLastOrganizationId())) {
                List<SystemOrganization> organizations = QueryChain.of(SystemOrganization.class).where(SYSTEM_ORGANIZATION.ID.eq(user.getLastOrganizationId())
                        .and(SYSTEM_ORGANIZATION.ENABLE.eq(true))).list();
                if (CollectionUtils.isNotEmpty(organizations)) {
                    SystemOrganization organization = organizations.getFirst();
                    List<Project> projectList = QueryChain.of(Project.class).where(PROJECT.ORGANIZATION_ID.eq(organization.getId())
                            .and(PROJECT.ENABLE.eq(true))).list();
                    if (CollectionUtils.isNotEmpty(projectList)) {
                        Project project = projectList.getFirst();
                        user.setLastProjectId(project.getId());
                        updateUser(user);
                        return true;
                    } else {
                        // 组织下无项目, 走前端逻辑, 跳转到无项目的路由
                        updateUser(user);
                        return true;
                    }
                }
            }
            // 项目和组织都没有权限
            Project project = getEnableProjectAndOrganization();
            if (project != null) {
                user.setLastProjectId(project.getId());
                user.setLastOrganizationId(project.getOrganizationId());
                updateUser(user);
                return true;
            }
            return true;
        }
        return false;
    }

    private Project getEnableProjectAndOrganization() {
        return QueryChain.of(Project.class).select(PROJECT.ALL_COLUMNS)
                .from(PROJECT).leftJoin(SYSTEM_ORGANIZATION).on(SYSTEM_ORGANIZATION.ID.eq(PROJECT.ORGANIZATION_ID))
                .where(PROJECT.ENABLE.eq(true).and(SYSTEM_ORGANIZATION.ENABLE.eq(true)))
                .limit(1).one();
    }


    public boolean isSuperUser(String id) {
        return QueryChain.of(UserRoleRelation.class)
                .where(USER_ROLE_RELATION.USER_ID.eq(id).and(USER_ROLE_RELATION.ROLE_ID.eq("admin"))).exists();
    }
}
