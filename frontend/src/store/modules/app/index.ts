import type {AppState} from "/@/store/modules/app/types.ts";
import {useI18n} from "/@/composables/useI18n.ts";
import {featureRouteMap} from "/@/router/constants.ts";
import type {BreadcrumbItem} from "/@/components/nf-breadcrumb/types.ts";
import {cloneDeep} from "lodash-es";
import type {RouteRecordRaw} from "vue-router";

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
            currentTopMenu: {} as RouteRecordRaw
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
        return {appState, showLoading, hideLoading, setCurrentOrgId, setCurrentProjectId, setBreadcrumbList}
    }
    , {persist: {pick: ['appState.currentOrgId', 'appState.currentProjectId']}})

export default useAppStore;