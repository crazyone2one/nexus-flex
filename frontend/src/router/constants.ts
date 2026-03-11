import {
    ApiTestRouteEnum,
    BugManagementRouteEnum,
    CaseManagementRouteEnum,
    TestPlanRouteEnum
} from "/@/enums/routeEnum.ts";

export const featureRouteMap: Record<string, any> = {
    [ApiTestRouteEnum.API_TEST]: 'apiTest',
    [CaseManagementRouteEnum.CASE_MANAGEMENT]: 'caseManagement',
    [TestPlanRouteEnum.TEST_PLAN]: 'testPlan',
    [BugManagementRouteEnum.BUG_MANAGEMENT]: 'bugManagement',
};
export const WHITE_LIST = [
    { name: 'notFound', path: '/notFound', children: [] ,},
    { name: 'index', path: '/index', children: [] },
]
export const NOT_FOUND = {
    name: 'notFound',
};

// 重定向中转站路由
export const REDIRECT_ROUTE_NAME = 'Redirect';

// 首页路由
export const DEFAULT_ROUTE_NAME = 'workbench';

// 无资源/权限路由
export const NO_RESOURCE_ROUTE_NAME = 'no-resource';

// 无项目路由
export const NO_PROJECT_ROUTE_NAME = 'no-project';

// 白板用户首页
export const WHITEBOARD_INDEX = 'index';

export const WHITE_LIST_NAME = WHITE_LIST.map((el) => el.name);

// 全屏无资源页面用于分享全屏的页面
export const NOT_FOUND_RESOURCE = 'notResourceScreen';