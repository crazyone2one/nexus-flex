<script setup lang="ts">
import {useI18n} from "/@/composables/useI18n.ts";

const {t} = useI18n()
const showModal = defineModel<boolean>('showModal', {required: true})
const {preset = 'dialog', isEdit = false, title = '', loading = false} = defineProps<{
  preset?: 'dialog' | 'card',
  isEdit?: boolean,
  loading?: boolean,
  title?: string
}>()
const emit = defineEmits<{
  (e: 'cancel', shouldSearch: boolean): void;
  (e: 'submit', shouldSearch: boolean): void;
}>();
</script>

<template>
  <n-modal v-model:show="showModal" :preset="preset" title="Dialog">
    <template #header>
      <div>{{ title }}</div>
    </template>
    <div>
      <slot></slot>
    </div>
    <template #action>
      <div class="flex flex-row justify-between">
        <div class="flex flex-row items-center gap-[4px]">
          <slot name="actionLeft"></slot>
        </div>
        <div class="flex flex-row gap-[14px]">
          <n-button secondary :disabled="loading" @click="emit('cancel', false)">{{ t('common.cancel') }}</n-button>
          <n-button type="primary" :disabled="loading" @click="emit('submit', true)">
            {{ isEdit ? t('common.update') : t('common.create') }}
          </n-button>
        </div>
      </div>
    </template>
  </n-modal>
</template>

<style scoped>

</style>