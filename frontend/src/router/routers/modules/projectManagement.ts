import type {RouteRecordRaw} from "vue-router";
import {ProjectManagementRouteEnum} from "/@/enums/routeEnum";

const ProjectManagement: RouteRecordRaw = {
    path: '/project-management',
    name: ProjectManagementRouteEnum.PROJECT_MANAGEMENT,
    redirect: '/project-management/permission',
    component: () => import('/@/layout/DefaultLayout.vue'),
    meta: {
        locale: 'menu.projectManagement',
        collapsedLocale: 'menu.projectManagementShort',
        icon: 'icon-icon_project-settings-filled',
        order: 1,
        hideChildrenInMenu: true,
        roles: [
            'PROJECT_BASE_INFO:READ',
            'PROJECT_TEMPLATE:READ',
            'PROJECT_FILE_MANAGEMENT:READ',
            'PROJECT_MESSAGE:READ',
            'PROJECT_CUSTOM_FUNCTION:READ',
            'PROJECT_LOG:READ',
            'PROJECT_ENVIRONMENT:READ',
            // 菜单管理
            'PROJECT_APPLICATION_WORKSTATION:READ',
            'PROJECT_APPLICATION_TEST_PLAN:READ',
            'PROJECT_APPLICATION_BUG:READ',
            'PROJECT_APPLICATION_CASE:READ',
            'PROJECT_APPLICATION_API:READ',
            'PROJECT_APPLICATION_UI:READ',
            'PROJECT_APPLICATION_PERFORMANCE_TEST:READ',
            // 菜单管理
            'PROJECT_USER:READ',
            'PROJECT_GROUP:READ',
        ],
    },
    children: [
        {
            path: 'permission',
            name: ProjectManagementRouteEnum.PROJECT_MANAGEMENT_PERMISSION,
            component: () => import('/@/views/project-management/pro-permission/index.vue'),
            redirect: '/project-management/permission/basicInfo',
            meta: {
                locale: 'menu.projectManagement.projectPermission',
                roles: [
                    'PROJECT_BASE_INFO:READ',
                    // 菜单管理
                    'PROJECT_APPLICATION_WORKSTATION:READ',
                    'PROJECT_APPLICATION_TEST_PLAN:READ',
                    'PROJECT_APPLICATION_BUG:READ',
                    'PROJECT_APPLICATION_CASE:READ',
                    'PROJECT_APPLICATION_API:READ',
                    'PROJECT_APPLICATION_UI:READ',
                    'PROJECT_APPLICATION_PERFORMANCE_TEST:READ',
                    // 菜单管理
                    'PROJECT_USER:READ',
                    'PROJECT_GROUP:READ',
                ],
                isTopMenu: true,
            },
            children: [
                // 基本信息
                {
                    path: 'basicInfo',
                    name: ProjectManagementRouteEnum.PROJECT_MANAGEMENT_PERMISSION_BASIC_INFO,
                    component: () => import('/@/views/project-management/pro-permission/basicInfos/index.vue'),
                    meta: {
                        locale: 'project.permission.basicInfo',
                        roles: ['PROJECT_BASE_INFO:READ'],
                    },
                },
                {
                    path: 'projectVersion',
                    name: ProjectManagementRouteEnum.PROJECT_MANAGEMENT_PERMISSION_VERSION,
                    component: () => import('/@/views/project-management/pro-permission/projectVersion/index.vue'),
                    meta: {
                        locale: 'project.permission.projectVersion',
                        roles: ['PROJECT_VERSION:READ'],
                    },
                },
            ]
        },
        {
            path: 'fileManagement',
            name: ProjectManagementRouteEnum.PROJECT_MANAGEMENT_FILE_MANAGEMENT,
            component: () => import('/@/views/project-management/fileManagement/index.vue'),
            meta: {
                locale: 'menu.projectManagement.fileManagement',
                roles: ['PROJECT_FILE_MANAGEMENT:READ'],
                isTopMenu: true,
            },
        },
        {
            path: 'log',
            name: ProjectManagementRouteEnum.PROJECT_MANAGEMENT_LOG,
            component: () => import('/@/views/project-management/log/index.vue'),
            meta: {
                locale: 'menu.projectManagement.log',
                roles: ['PROJECT_LOG:READ'],
                isTopMenu: true,
            },
        },
    ]
}
export default ProjectManagement;