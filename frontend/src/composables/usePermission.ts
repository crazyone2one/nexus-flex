import type {RouteLocationNormalized, RouteRecordRaw} from "vue-router";
import {useUserStore} from "/@/store";
import {hasAnyPermission, topLevelMenuHasPermission} from "/@/utils/permission.ts";

export const usePermission = () => {
    const firstLevelMenu = ['testPlan', 'bugManagement', 'caseManagement', 'apiTest'];
    return {
        accessRouter(route: RouteLocationNormalized | RouteRecordRaw) {
            if (
                (useUserStore().userState.lastProjectId === 'no_such_project' || useUserStore().userState.lastProjectId === '') &&
                route.name === 'projectManagement'
            ) {
                return false;
            }
            if (firstLevelMenu.includes(route.name as string)) {
                // 一级菜单: 创建项目时 被勾选的模块
                return topLevelMenuHasPermission(route);
            }
            return (
                route.meta?.requiresAuth === false ||
                !route.meta?.roles ||
                route.meta?.roles?.includes('*') ||
                hasAnyPermission(route.meta?.roles || [])
            );
        }
    }
}