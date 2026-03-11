import type {BreadcrumbItem} from "/@/components/nf-breadcrumb/types.ts";
import type {RouteRecordRaw} from "vue-router";

export interface AppState {
    loading: boolean;
    loadingTip: string;
    currentOrgId: string;
    currentProjectId: string;
    innerHeight: number;
    currentMenuConfig: string[];
    breadcrumbList: BreadcrumbItem[];
    topMenus: RouteRecordRaw[];
    currentTopMenu: RouteRecordRaw;
}