import type {UserState} from "/@/store/modules/user/types.ts";
import {useAppStore} from "/@/store";
import {composePermissions} from "/@/utils/permission.ts";
import {authApi} from "/@/api/modules/auth.ts";
import {clearToken, setToken} from "/@/utils/auth.ts";

const useUserStore = defineStore('user', () => {
    let userState = reactive<UserState>({
        name: undefined,
        avatar: undefined,
        email: undefined,
        phone: undefined,
        id: undefined,
        userRolePermissions: [],
        userRoles: [],
        userRoleRelations: [],
        lastProjectId: '',
    })
    const isAdmin = computed(() => {
        if (!userState.userRolePermissions) return false;
        return userState.userRolePermissions.findIndex((ur) => ur.userRole.id === 'admin') > -1;
    })
    const currentRole = computed(() => {
        const appStore = useAppStore();
        userState.userRoleRelations?.forEach(ug => {
            userState.userRolePermissions?.forEach(urp => {
                if (urp.userRole.id === ug.roleId) {
                    ug.userRolePermissions = urp.userRolePermissions
                    ug.userRole = urp.userRole
                }
            })
        })
        return {
            projectPermissions: composePermissions(userState.userRoleRelations || [], 'PROJECT', appStore.appState.currentProjectId),
            orgPermissions: composePermissions(userState.userRoleRelations || [], 'ORGANIZATION', appStore.appState.currentOrgId),
            systemPermissions: composePermissions(userState.userRoleRelations || [], 'SYSTEM', 'global'),
        }
    })
    const setInfo = (partial: UserState) => {
        userState.userRolePermissions = partial.userRolePermissions
        userState.userRoles = partial.userRoles
        userState.userRoleRelations = partial.userRoleRelations
        userState.name = partial.name
        userState.avatar = partial.avatar
        userState.email = partial.email
        userState.phone = partial.phone
        userState.id = partial.id
        userState.lastProjectId = partial.lastProjectId
        userState.lastOrganizationId = partial.lastOrganizationId
    }
    const login = async (data: { username: string, password: string }) => {
        try {
            const res = await authApi.login(data);
            const appStore = useAppStore();
            setToken(res.accessToken, res.refreshToken);
            appStore.setCurrentProjectId(res.userDTO?.lastProjectId || '');
            appStore.setCurrentOrgId(res.userDTO?.lastOrganizationId || '');
            setInfo(res.userDTO as UserState);
        } catch (err) {
            clearToken();
            throw err;
        }
    }
    return {userState, isAdmin, currentRole, setInfo, login}
}, {persist: true})
export default useUserStore;