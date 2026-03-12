<script setup lang="ts">
import {type MenuOption} from "naive-ui";
import useMenuTree from "/@/layout/useMenuTree.ts";
import {type RouteRecordRaw, RouterLink} from "vue-router";
import {useI18n} from "/@/composables/useI18n.ts";
import {SettingRouteEnum} from "/@/enums/routeEnum.ts";
import {listenerRouteChange} from "/@/utils/routeListener.ts";

const collapsed = ref(false)
const {menuTree} = useMenuTree();
const {t} = useI18n()
const selectedKey = ref('')
const renderMenuOptions = () => {
  function travel(_route: (RouteRecordRaw | null)[] | null, nodes = []) {
    if (_route) {
      _route.forEach(element => {
        const option: MenuOption = {
          label: () => element?.name === SettingRouteEnum.SETTING_ORGANIZATION
              ? t(element?.meta?.locale as string)
              : h(RouterLink, {to: {name: element?.name}}, {default: () => t(element?.meta?.locale as string)}),
          key: element?.name as string,
          icon: () => h('div', {class: `i-${element?.meta?.icon}`}),
        };
        if (element?.name !== SettingRouteEnum.SETTING) {
          option.type = 'group'
        }
        if (element?.name === SettingRouteEnum.SETTING_ORGANIZATION) {
          option.disabled = true
        }
        if (element?.children && element.children.length !== 0) {
          option.children = travel(element?.children);
        }
        nodes.push(option as never);
      })
    }
    return nodes
  }

  return travel(menuTree.value)
}
const findMenuOpenKeys = (target: string) => {
  const result: string[] = [];
  let isFind = false;
  const backtrack = (item: RouteRecordRaw | null, keys: string[]) => {
    if (target.includes(item?.name as string)) {
      result.push(...keys);
      if (result.length >= 2) {
        // 由于目前存在三级子路由，所以至少会匹配到三层才算结束
        isFind = true;
        return;
      }
    }
    if (item?.children?.length) {
      item.children.forEach((el) => {
        backtrack(el, [...keys, el.name as string]);
      });
    }
  };

  menuTree.value?.forEach((el: RouteRecordRaw | null) => {
    if (isFind) return; // 节省性能
    backtrack(el, [el?.name as string]);
  });
  return result;
}
listenerRouteChange(newRoute => {
  const menuOpenKeys = findMenuOpenKeys((newRoute.name) as string);
  selectedKey.value = menuOpenKeys[menuOpenKeys.length - 1] as string;
}, true)
const menuOptions = computed(() => {
  return renderMenuOptions()
})
</script>

<template>
  <n-layout-sider :native-scrollbar="false" bordered
                  collapse-mode="width"
                  :collapsed-width="64"
                  :width="240"
                  :collapsed="collapsed"
                  show-trigger
                  @collapse="collapsed = true"
                  @expand="collapsed = false">
    <n-menu
        v-model:value="selectedKey"
        :collapsed="collapsed"
        :collapsed-width="64"
        :collapsed-icon-size="24"
        :options="menuOptions"
    />
    <n-float-button position="fixed" shape="circle" bottom="65px" left="15px" type="primary"
                    menu-trigger="click">
      <n-icon>
        <div class="i-carbon:user-avatar" />
      </n-icon>
      <template #menu>
        <n-float-button shape="square" type="primary">
          <n-icon>
            <div class="i-carbon:user-sponsor"/>
          </n-icon>
        </n-float-button>
        <n-float-button shape="square" type="primary">
          <n-icon>
            <div class="i-carbon:port-output"/>
          </n-icon>
        </n-float-button>
      </template>
    </n-float-button>
  </n-layout-sider>
</template>

<style scoped>

</style>