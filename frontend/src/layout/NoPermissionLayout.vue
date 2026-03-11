<script setup lang="ts">

import DefaultLayout from "/@/layout/DefaultLayout.vue";
import {useI18n} from "/@/composables/useI18n.ts";

const {t} = useI18n();
const props = defineProps<{
  isProject?: boolean;
}>();
</script>

<template>
  <default-layout>
    <template #page>
      <div class="page">
        <div class="content-wrapper">
          <div class="content">
            <!--          <div :class="`${appStore.isDarkTheme ? 'no-permission-svg-dark' : 'no-permission-svg-bright'}`"></div>-->
            <div class="title">
              <span>{{ props.isProject ? t('common.noProject') : t('common.noResource') }}</span>
            </div>
            <slot></slot>
          </div>
        </div>
      </div>
    </template>
  </default-layout>
</template>

<style scoped>
.page {
  @apply h-full;

  .content-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;

    .content {
      .title {
        display: flex;
        justify-content: center;
        font-size: 16px;

        .user {
          margin-left: 16px;
        }
      }
    }
  }
}
</style>