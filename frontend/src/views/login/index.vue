<script setup lang="ts">
import {useForm} from "alova/client";
import {authApi} from "/@/api/modules/auth.ts";
import {useI18n} from "/@/composables/useI18n.ts";
import {setToken} from "/@/utils/auth.ts";
import {useAppStore, useUserStore} from "/@/store";
import {SettingRouteEnum} from "/@/enums/routeEnum.ts";
import {NO_PROJECT_ROUTE_NAME, NO_RESOURCE_ROUTE_NAME} from "/@/router/constants.ts";
import {getFirstRouteNameByPermission, routerNameHasPermission} from "/@/utils/permission.ts";
import type {UserState} from "/@/store/modules/user/types.ts";

const {t} = useI18n();
const router = useRouter();
const appStore = useAppStore();
const userStore = useUserStore();
const rules = {
  username: {required: true, message: t('login.form.userName.errMsg'), trigger: 'blur'},
  password: {required: true, message: t('login.form.password.errMsg'), trigger: 'blur'}
}
const {form, loading, send} = useForm(form1 => authApi.login(form1), {
  initialForm: {
    username: 'admin',
    password: '123456'
  }
});
const handleLogin = () => {
  send().then(res => {
    window.$message?.success(t('login.form.login.success'));
    const {accessToken, refreshToken, userDTO} = res;
    setToken(accessToken, refreshToken);
    appStore.setCurrentProjectId(userDTO?.lastProjectId || '');
    appStore.setCurrentOrgId(userDTO?.lastOrganizationId || '');
    userStore.setInfo(userDTO as UserState);
    if (
        (!appStore.appState.currentProjectId || appStore.appState.currentProjectId === 'no_such_project') &&
        !router.currentRoute.value.path.startsWith(SettingRouteEnum.SETTING)
    ) {
      console.log(1)
      // 没有项目权限（用户所在的当前项目被禁用&用户被移除出去该项目/白板用户没有项目）且访问的页面非系统菜单模块，则重定向到无项目权限页面
      router.push({
        name: NO_PROJECT_ROUTE_NAME,
      });
      return;
    }
    const {redirect, ...othersQuery} = router.currentRoute.value.query;
    const redirectHasPermission =
        redirect &&
        ![NO_RESOURCE_ROUTE_NAME, NO_PROJECT_ROUTE_NAME].includes(redirect as string) &&
        routerNameHasPermission(redirect as string, router.getRoutes());
    const currentRouteName = getFirstRouteNameByPermission(router.getRoutes());
    router.push({
      name: redirectHasPermission ? (redirect as string) : currentRouteName,
      query: {
        ...othersQuery,
        orgId: appStore.appState.currentOrgId,
        pId: appStore.appState.currentProjectId,
      },
    });
  })
}
</script>

<template>
  <div class="login-container">
    <n-card title="欢迎登录" style="width: 400px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);">
      <n-form :model="form" :rules="rules" ref="formRef">
        <n-form-item path="username">
          <n-input v-model:value="form.username" :placeholder="t('login.form.userName.placeholderOther')"
                   :maxlength="64"/>
        </n-form-item>
        <n-form-item path="password">
          <n-input v-model:value="form.password" type="password" :placeholder="t('login.form.password.placeholder')"
                   :maxlength="64" clearable/>
        </n-form-item>
        <n-button type="primary" block @click="handleLogin" :loading="loading">
          {{ t('login.form.login') }}
        </n-button>
      </n-form>
    </n-card>
  </div>
</template>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
}
</style>