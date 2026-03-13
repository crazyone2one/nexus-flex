import type {AppState} from "/@/store/modules/app/types.ts";
import {useI18n} from "/@/composables/useI18n.ts";
import {featureRouteMap} from "/@/router/constants.ts";
import type {BreadcrumbItem} from "/@/components/nf-breadcrumb/types.ts";
import {cloneDeep} from "lodash-es";
import type {RouteRecordRaw} from "vue-router";
import {OrgProjectApi} from "/@/api/modules/OrgProject.ts";

const useAppStore = defineStore('app', () => {
        const appState = reactive<AppState>({
            loading: false,
            loadingTip: '',
            currentOrgId: '',
            currentProjectId: '',
            innerHeight: 0,
            currentMenuConfig: Object.keys(featureRouteMap),
            breadcrumbList: [],
            topMenus: [],
            currentTopMenu: {} as RouteRecordRaw,
            projectList: []
        })
        const showLoading = (tip = '') => {
            const {t} = useI18n();
            appState.loading = true;
            appState.loadingTip = tip || t('message.loadingDefaultTip');
        }
        const hideLoading = () => {
            const {t} = useI18n();
            appState.loading = false
            appState.loadingTip = t('message.loadingDefaultTip');
        }
        const setCurrentOrgId = (orgId: string) => {
            appState.currentOrgId = orgId
        }
        const setCurrentProjectId = (projectId: string) => {
            appState.currentProjectId = projectId
        }
        const setBreadcrumbList = (breadcrumbs: BreadcrumbItem[] | undefined) => {
            appState.breadcrumbList = breadcrumbs ? cloneDeep(breadcrumbs) : []
        }
        const setTopMenus = (menus: RouteRecordRaw[] | undefined) => {
            appState.topMenus = menus ? [...menus] : []
        }
        const setCurrentTopMenu = (menu: RouteRecordRaw) => {
            appState.currentTopMenu = cloneDeep(menu)
        }
        const getTopMenus = computed(() => {
            return appState.topMenus
        })
        const getCurrentTopMenu = computed(() => {
            return appState.currentTopMenu
        })
        const initProjectList = async () => {
            if (appState.currentOrgId) {
                appState.projectList = await OrgProjectApi.fetchProjectList(appState.currentOrgId);
            } else {
                appState.projectList = [];
            }
        }
        return {
            appState,
            showLoading,
            hideLoading,
            setCurrentOrgId,
            setCurrentProjectId,
            setBreadcrumbList,
            setTopMenus,
            setCurrentTopMenu,
            getTopMenus, getCurrentTopMenu,
            initProjectList
        }
    }
    , {persist: {pick: ['appState.currentOrgId', 'appState.currentProjectId']}})

export default useAppStore;