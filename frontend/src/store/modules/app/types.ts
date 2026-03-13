import type {BreadcrumbItem} from "/@/components/nf-breadcrumb/types.ts";
import type {RouteRecordRaw} from "vue-router";
import type {ProjectListItem} from "/@/api/types/project.ts";

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
    projectList: ProjectListItem[];
}