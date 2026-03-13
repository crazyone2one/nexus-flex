<script setup lang="ts">
import {useForm} from "alova/client";
import {OrgProjectApi} from "/@/api/modules/OrgProject.ts";
import {useI18n} from "/@/composables/useI18n.ts";
import {type FormInst, type FormRules} from "naive-ui";
import {useAppStore} from "/@/store";
import type {CreateOrUpdateSystemProjectParams} from "/@/api/types/project.ts";

const {t} = useI18n()
const appStore = useAppStore()
const showModal = defineModel<boolean>('showModal', {required: true});
const {currentProject = {} as CreateOrUpdateSystemProjectParams} = defineProps<{
  currentProject?: CreateOrUpdateSystemProjectParams
}>();
const formRef = ref<FormInst | null>(null)
const isEdit = computed(() => !!currentProject?.id);
const title = computed(() => {
  return isEdit ? t('system.project.updateProject') : t('system.project.createProject');
})
const emit = defineEmits<{
  (e: 'cancel', shouldSearch: boolean): void;
  (e: 'reload', shouldSearch: boolean): void;
}>();
const rules: FormRules = {
  name: [{required: true, message: t('system.project.projectNameRequired'), trigger: 'blur'},
    {max: 255, message: t('common.nameIsTooLang')},],
  num: {required: true, message: t('system.project.projectNumRequired'), trigger: ['blur']},
}
const {form, loading, send} = useForm(formData => OrgProjectApi.createOrUpdateProject(formData), {
  initialForm: {
    name: '',
    organizationId: '100001', description: '', enable: true,
    num: "",
    userIds: ['100001'],
    moduleIds: ['bugManagement', 'caseManagement', 'apiTest', 'testPlan']
  },
  resetAfterSubmiting: true
})
const handleCancel = (search: boolean) => {
  emit('cancel', search);
  formRef.value?.restoreValidation()
  showModal.value = false
}
const handleSubmit = (search: boolean) => {
  formRef.value?.validate(err => {
    if (!err) {
      send().then(() => {
        window.$message?.success(isEdit ? t('system.project.updateProjectSuccess') : t('system.project.createProjectSuccess'));
        handleCancel(search)
        appStore.initProjectList()
      })
    }
  })
}
watchEffect(() => {
  if (currentProject.id) {
    if (currentProject) {
      form.value = Object.assign({}, currentProject)
    }
  }
})
</script>

<template>
  <base-modal v-model:show-modal="showModal" :is-edit="isEdit" :title="title"
              :loading="loading"
              @submit="handleSubmit"
              @cancel="handleCancel">
    <div>
      <n-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-placement="top"
          :label-width="'auto'"
      >
        <n-form-item :label="t('system.project.name')" path="name">
          <n-input v-model:value="form.name" :placeholder="t('system.project.projectNamePlaceholder')"/>
        </n-form-item>
        <n-form-item :label="t('system.project.num')" path="num">
          <n-input v-model:value="form.num" :placeholder="t('system.project.projectNumPlaceholder')"/>
        </n-form-item>
        <n-form-item :label="t('common.desc')" path="description">
          <n-input type="textarea" v-model:value="form.description"
                   :placeholder="t('system.project.descriptionPlaceholder')"/>
        </n-form-item>
      </n-form>
    </div>
    <template #actionLeft>
      <div class="flex flex-row items-center gap-[4px]">
        <n-switch v-model:value="form.enable" size="small"/>
      </div>
    </template>
  </base-modal>
</template>

<style scoped>

</style>