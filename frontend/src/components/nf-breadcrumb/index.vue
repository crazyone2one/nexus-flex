<script setup lang="ts">
import {useAppStore} from "/@/store";
import {useI18n} from "/@/composables/useI18n.ts";
import {listenerRouteChange} from "/@/utils/routeListener.ts";
import type {BreadcrumbItem} from "/@/components/nf-breadcrumb/types.ts";

const appStore = useAppStore();
const {t} = useI18n();
const router = useRouter();
const route = useRoute();
const isEdit = ref(true);
listenerRouteChange(newRoute => {
  const {name, meta} = newRoute;
  isEdit.value = false;
  if (name === appStore.appState.currentTopMenu.name) {
    appStore.setBreadcrumbList(appStore.appState.currentTopMenu?.meta?.breadcrumbs);
  } else if ((name as string).includes(appStore.appState.currentTopMenu.name as string)) {
    // 顶部菜单内下钻的父子路由命名是包含关系，子路由会携带完整的父路由名称
    const currentBreads = meta.breadcrumbs;
    appStore.setBreadcrumbList(currentBreads);
    // 下钻的三级路由一般都会区分编辑添加场景，根据场景展示不同的国际化路由信息
    const editTag = currentBreads && currentBreads[currentBreads.length - 1].editTag;
    setTimeout(() => {
      // 路由异步挂载，这里使用同步或者nextTick都取不到变化后的路由参数，所以使用定时器
      isEdit.value = editTag && route.query[editTag];
    }, 100);
  } else {
    appStore.setBreadcrumbList([]);
  }
}, true)
const jumpTo = (crumb: BreadcrumbItem, index: number) => {
// 点击当前页面的面包屑，不跳转
  if (index === appStore.appState.breadcrumbList.length - 1) {
    return;
  }
  if (crumb.isBack && window.history.state.back) {
    router.back();
  } else {
    const query: Record<string, any> = {};
    if (crumb.query) {
      crumb.query.forEach((key) => {
        query[key] = route.query[key];
      });
    }
    router.replace({name: crumb.name, query});
  }
}
</script>

<template>
  <n-breadcrumb v-if="appStore.appState.breadcrumbList.length > 0" class="z-10">
    <n-breadcrumb-item v-for="(crumb, index) in appStore.appState.breadcrumbList" :key="crumb.name"
                       @click="jumpTo(crumb, index)">
      {{ isEdit ? t(crumb.editLocale || crumb.locale) : t(crumb.locale) }}
    </n-breadcrumb-item>
  </n-breadcrumb>
</template>

<style scoped>

</style>