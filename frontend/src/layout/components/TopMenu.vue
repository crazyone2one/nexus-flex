<script setup lang="ts">

import {listenerRouteChange} from "/@/utils/routeListener.ts";
import appClientMenus from "/@/router/appMenus.ts";
import {cloneDeep} from "lodash-es";
import {usePermission} from "/@/composables/usePermission.ts";
import {type RouteRecordName, type RouteRecordRaw, RouterLink} from "vue-router";
import {useAppStore} from "/@/store";
import type {MenuOption} from "naive-ui";
import {useI18n} from "/@/composables/useI18n.ts";

const {t} = useI18n()
const appStore = useAppStore()
const copyRouters = cloneDeep(appClientMenus) as RouteRecordRaw[];
const permission = usePermission();
const activeMenus: Ref<RouteRecordName[]> = ref([]);

const renderMenuOptions = (menus: RouteRecordRaw[]) => {
  function travel(_route: (RouteRecordRaw | null)[] | null, nodes = []) {
    if (_route) {
      _route.forEach(element => {
        const option: MenuOption = {
          label: () => h(RouterLink, {to: {name: element?.name}}, {default: () => t(element?.meta?.locale as string)}),
          key: element?.name as string,
        };
        if (element?.meta?.icon) {
          option.icon = () => h('div', {class: `i-${element?.meta?.icon}`})
        }
        nodes.push(option as never);
      })
    }
    return nodes
  }

  return travel(menus)
}
const setCurrentTopMenu = (name: string) => {
  // 先判断全等，避免同级路由出现命名包含情况
  const secParentFullSame = appStore.appState.topMenus.find((route: RouteRecordRaw) => {
    return name === route?.name;
  });

  // 非全等的情况下，一定是父子路由包含关系
  const secParentLike = appStore.appState.topMenus.find((route: RouteRecordRaw) => {
    return name.includes(route?.name as string);
  });

  if (secParentFullSame) {
    appStore.setCurrentTopMenu(secParentFullSame);
  } else if (secParentLike) {
    appStore.setCurrentTopMenu(secParentLike);
  }
}
const checkAuthMenu = () => {
  const topMenus = appStore.getTopMenus;
  appStore.setTopMenus(topMenus);
}
watch(
    () => appStore.getCurrentTopMenu?.name,
    (val) => {
      checkAuthMenu()
      activeMenus.value = [val || ''];
    },
    {immediate: true,}
);
const menuOptions = computed(() => {
  return renderMenuOptions(appStore.getTopMenus)
})
listenerRouteChange(newRoute => {
  const {name} = newRoute;
  for (let i = 0; i < copyRouters.length; i++) {
    const firstRoute = copyRouters[i];
    if (permission.accessRouter(firstRoute as RouteRecordRaw)) {
      if (name && firstRoute?.name && (name as string).includes(firstRoute.name as string)) {
        // 先判断二级菜单是否顶部菜单
        let currentParent = firstRoute?.children?.some((item) => item.meta?.isTopMenu)
            ? (firstRoute as RouteRecordRaw)
            : undefined;
        if (!currentParent) {
          // 二级菜单非顶部菜单，则判断三级菜单是否有顶部菜单
          currentParent = firstRoute?.children?.find(
              (item) => name && item?.name && (name as string).includes(item.name as string)
          );
        }
        let filterMenuTopRouter =
            currentParent?.children?.filter((item: any) => permission.accessRouter(item) && item.meta?.isTopMenu) || [];
        // if (appStore.getPackageType === 'community') {
        //   filterMenuTopRouter = filterMenuTopRouter.filter(
        //       (item) => item.name !== RouteEnum.SETTING_SYSTEM_AUTHORIZED_MANAGEMENT
        //   );
        // }
        appStore.setTopMenus(filterMenuTopRouter);
        setCurrentTopMenu(name as string);
        // router.push({name: filterMenuTopRouter[0]?.name})
        return;
      }
    }
  }

  // 切换到没有顶部菜单的路由时，清空顶部菜单
  appStore.setTopMenus([]);
  setCurrentTopMenu('');
}, true)
</script>

<template>
  <n-menu v-show="appStore.getTopMenus.length>0"
          :options="menuOptions"
          mode="horizontal"/>
</template>

<style scoped>

</style>