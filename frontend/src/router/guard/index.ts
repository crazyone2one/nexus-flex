import type {Router} from "vue-router";
import setupUserLoginInfoGuard from "/@/router/guard/userLoginInfo.ts";
import usePathMap from "/@/composables/usePathMap.ts";
import {setRouteEmitter} from "/@/utils/routeListener.ts";
import {useAppStore} from "/@/store";
import {MENU_LEVEL, type PathMapRoute} from "/@/config/pathMap.ts";

const setupPageGuard = (router: Router) => {

    const {getRouteLevelByKey} = usePathMap();
    router.beforeEach((to, _, next) => {
        // 监听路由变化
        setRouteEmitter(to);
        const appStore = useAppStore();
        const urlOrgId = to.query.orgId;
        const urlProjectId = to.query.pId;
        if (urlOrgId) {
            appStore.setCurrentOrgId(urlOrgId as string);
        }
        if (urlProjectId) {
            appStore.setCurrentProjectId(urlProjectId as string);
        }
        switch (getRouteLevelByKey(to.name as PathMapRoute)) {
            case MENU_LEVEL[1]:
                if (urlOrgId === undefined) {
                    to.query = {
                        ...to.query,
                        orgId: appStore.appState.currentOrgId,
                    };
                    next(to);
                    return;
                }
                break;
            case MENU_LEVEL[2]:
                if (urlOrgId === undefined && urlProjectId === undefined) {
                    to.query = {
                        ...to.query,
                        orgId: appStore.appState.currentOrgId,
                        pId: appStore.appState.currentProjectId,
                    };
                    next(to);
                    return;
                }
                break;
            case MENU_LEVEL[0]: // 系统级别的页面，无需携带组织ID和项目ID
            default:
                next();
                break;
        }
    })
}

export default function createRouteGuard(router: Router) {
    // 设置路由监听守卫
    setupPageGuard(router);
    // 设置用户登录校验守卫
    setupUserLoginInfoGuard(router);
    // 设置菜单权限守卫
    // setupPermissionGuard(router);
}