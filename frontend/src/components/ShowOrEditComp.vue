<script setup lang="ts">
import {type InputInst, NInput} from 'naive-ui'
import {withDirectives} from "vue";
import permission from "/@/directive/permission";

export interface PermissionButtonProps {
  permissions: string[]
  permissionMode?: 'any' | 'all',
  directive?: any
  value: string | number
  onUpdateValue?: (value: string | number) => void
}

const props = withDefaults(defineProps<PermissionButtonProps>(), {
  permissionMode: 'any',
  directive: permission,
})
const isEdit = ref(false)
const inputRef = ref<InputInst | null>(null)
const inputValue = ref(props.value);
const handleOnClick = () => {
  isEdit.value = true
  nextTick(() => {
    inputRef.value?.focus()
  })
}
const handleChange = () => {
  props.onUpdateValue?.(String(inputValue.value))
  isEdit.value = false
}
const renderShowOrEdit = () => {
  const node = h('div', {onClick: handleOnClick},
      isEdit.value ? h(NInput, {
        ref: inputRef, value: String(inputValue.value),
        onUpdateValue: (value) => inputValue.value = value,
        onBlur: handleChange
      }, {}) : props.value)
  // 使用 withDirectives 包装
  // 格式：[[directive, value, arg, modifiers]]
  return withDirectives(node, [
    [props.directive || permission, props.permissions]
  ])
}
</script>

<template>
  <component :is="renderShowOrEdit"/>
</template>

<style scoped>

</style>