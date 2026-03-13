<script setup lang="ts">
import {type ButtonProps, NButton} from "naive-ui";
import {withDirectives} from "vue";
import permission from "/@/directive/permission";

export interface PermissionButtonProps {
  permissions: string[]
  permissionMode?: 'any' | 'all',
  directive?: any
  label: string
  buttonProps?: ButtonProps
}

const props = withDefaults(defineProps<PermissionButtonProps>(), {
  permissionMode: 'any',
  directive: permission,
})
const emit = defineEmits<{
  (e: 'btnClick'): void;
}>();
const renderButton = () => {
  const node = h(NButton, {
    ...props.buttonProps,
    ...useAttrs(),
    class: 'not-last:mr-4 px-1 text-sm rounded-sm',
    onClick: () => emit('btnClick')
  }, {default: () => props.label})
  // 2. 如果没有权限配置或指令，直接返回基础按钮
  if (!props.permissions || props.permissions.length === 0) {
    return node;
  }
  // 3. 使用 withDirectives 包装
  // 格式：[[directive, value, arg, modifiers]]
  return withDirectives(node, [
    [props.directive || permission, props.permissions]
  ])
}
</script>

<template>
  <component :is="renderButton"/>
</template>

<style scoped>

</style>