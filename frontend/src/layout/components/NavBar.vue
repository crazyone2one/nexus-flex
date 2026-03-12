<script setup lang="ts">
import usePathMap from "/@/composables/usePathMap.ts";
import {MENU_LEVEL, type PathMapRoute} from "/@/config/pathMap.ts";
import TopMenu from "/@/layout/components/TopMenu.vue";

const route = useRoute();
const showProjectSelect = computed(() => {
  const {getRouteLevelByKey} = usePathMap();
  // 非项目级别页面不需要展示项目选择器
  const level = getRouteLevelByKey(route.name as PathMapRoute);
  return level === MENU_LEVEL[2] || level === null;
});
</script>

<template>
  <n-layout-header bordered style="height: 64px; padding: 24px" class="flex h-full justify-between bg-transparent">
    <div class="flex w-[200px] items-center px-[16px]">
      <div class="one-line-text flex max-w-[145px] items-center">
        <div class="mr-[4px] h-[34px] w-[32px] i-carbon:bee-bat"/>
      </div>
    </div>
    <div class="flex flex-1 items-center">
      <template v-if="showProjectSelect">
        <n-select class="mr-[8px] w-[200px] focus-within:!bg-[var(--color-text-n8)] hover:!bg-[var(--color-text-n8)]">
        </n-select>
      </template>
      <top-menu/>
    </div>
    <div class="flex list-none"></div>
  </n-layout-header>
</template>

<style scoped>

</style>