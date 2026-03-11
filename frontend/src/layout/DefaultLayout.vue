<script setup lang="ts">
import {useAppStore} from "/@/store";
import NfBreadcrumb from '/@/components/nf-breadcrumb/index.vue'

const appStore = useAppStore();
</script>

<template>
  <n-layout position="absolute">
    <n-layout-header bordered style="height: 64px; padding: 24px">顶部导航区域</n-layout-header>
    <n-layout position="absolute" has-sider style="top: 64px; bottom: 64px">
      <n-layout-sider content-style="padding: 24px;" :native-scrollbar="false" bordered>
        左侧菜单区域
      </n-layout-sider>
      <n-layout>
        <n-layout-content content-style="padding: 24px;" :native-scrollbar="false">
          <n-spin :show="appStore.appState.loading">
            <nf-breadcrumb/>
            <slot name="page">
              <!--              <page-layout/>-->
              <router-view/>
            </slot>
            <slot></slot>
            <template #description>
              {{ appStore.appState.loadingTip }}
            </template>
          </n-spin>
        </n-layout-content>
      </n-layout>
    </n-layout>
    <n-layout-footer bordered position="absolute" style="height: 64px; padding: 24px">
      成府路
    </n-layout-footer>
  </n-layout>
  <!--  <n-layout>-->
  <!--    <n-layout-header>颐和园路</n-layout-header>-->
  <!--    <n-layout has-sider>-->
  <!--      <n-layout-sider content-style="padding: 24px;">-->
  <!--        海淀桥-->
  <!--      </n-layout-sider>-->
  <!--      <n-layout-content content-style="padding: 24px;">-->
  <!--        <router-view/>-->
  <!--      </n-layout-content>-->
  <!--    </n-layout>-->
  <!--    <n-layout-footer>成府路</n-layout-footer>-->
  <!--  </n-layout>-->
</template>

<style scoped>

</style>