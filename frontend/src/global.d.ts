export {};
declare module '*.vue' {
    import { DefineComponent } from 'vue';
    const component: DefineComponent<{}, {}, any>;
    export default component;
}
declare global {
    export interface Window {
        /** NProgress instance */
        NProgress?: import('nprogress').NProgress;
        /** Loading bar instance */
        $loadingBar?: import('naive-ui').LoadingBarProviderInst;
        /** Dialog instance */
        $dialog?: import('naive-ui').DialogProviderInst;
        /** Message instance */
        $message?: import('naive-ui').MessageProviderInst;
        /** Notification instance */
        $notification?: import('naive-ui').NotificationProviderInst;
    }

    /** Build time of the project */
    export const BUILD_TIME: string;
    export type Recordable<T = any> = Record<string, T>;
}
declare module 'vue-router' {
    interface RouteMeta {
        roles?: string[]; // 角色数组
        requiresAuth?: boolean; // 是否需要权限，默认需要
        icon?: string; // 菜单icon
        locale?: string; // 国际化语言单词
        collapsedLocale?: string; // 收起时的国际化语言单词
        hideInMenu?: boolean; // 此路由不在菜单展示
        hideChildrenInMenu?: boolean; // 子路由不展示在菜单
        activeMenu?: string; // 激活状态
        order?: number; // 排序权重
        noAffix?: boolean; // tab展示设置，设置为true则不在tab列表展示激活页面的tab
        isCache?: boolean; // 缓存设置，true则不缓存
        isTopMenu?: boolean; // 是否为顶部菜单
        breadcrumbs?: BreadcrumbItem[]; // 面包屑
    }
}