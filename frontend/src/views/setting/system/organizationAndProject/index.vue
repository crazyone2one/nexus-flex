<script setup lang="ts">
import {useI18n} from "/@/composables/useI18n.ts";
import type {CreateOrUpdateSystemProjectParams, OrgProjectTableItem} from "/@/api/types/project.ts";
import {type DataTableColumns, NButton, NSwitch} from 'naive-ui'
import {hasAnyPermission} from "/@/utils/permission.ts";
import {usePagination, useRequest} from "alova/client";
import {OrgProjectApi} from "/@/api/modules/OrgProject.ts";
import AddProjectModal from "/@/views/setting/system/organizationAndProject/components/AddProjectModal.vue";
import {withDirectives} from "vue";
import permission from "/@/directive/permission";
import PermissionButton from "/@/components/PermissionButton.vue";
import ShowOrEditComp from "/@/components/ShowOrEditComp.vue";

const {t} = useI18n()
const keyword = ref('')
const addProjectVisible = ref(false);
const hasOperationPermission = computed(() =>
    hasAnyPermission([
      'SYSTEM_ORGANIZATION_PROJECT:READ+RECOVER',
      'SYSTEM_ORGANIZATION_PROJECT:READ+UPDATE',
      'SYSTEM_ORGANIZATION_PROJECT:READ+DELETE',
    ])
);
const operationWidth = computed(() => {
  if (hasOperationPermission.value) {
    return 250;
  }
  if (hasAnyPermission(['PROJECT_BASE_INFO:READ'])) {
    return 100;
  }
  return 50;
});

const {data, send: fetchData} = usePagination((page, pageSize) => {
  const params = {page, pageSize, keyword: keyword.value}
  return OrgProjectApi.fetchProjectPage(params)
}, {
  initialData: {total: 0, data: []},
  immediate: false,
  data: resp => resp.records,
  total: resp => resp.totalRow,
  watchingStates: [keyword]
})
const currentUpdateProject = ref<CreateOrUpdateSystemProjectParams>();
const handleAddProject = () => {
  addProjectVisible.value = true
}
const handleEditProject = (record: OrgProjectTableItem) => {
  const {id, name, num, description, enable, organizationId, moduleIds, allResourcePool, resourcePoolIds, adminList} =
      record;
  addProjectVisible.value = true
  currentUpdateProject.value = {
    id,
    name, num,
    description,
    enable,
    organizationId,
    moduleIds,
    userIds: adminList.map(o => o.userId),
    resourcePoolIds, allResourcePool
  }
}
const {
  send: deleteProject,
  loading: deleteLoading
} = useRequest((id) => OrgProjectApi.deleteProject(id), {immediate: false})
const handleDeleteProject = (record: OrgProjectTableItem) => {
  window.$dialog?.error({
    title: t('system.project.deleteName', {name: record.name}),
    content: t('system.project.deleteTip'),
    positiveText: t('common.confirmDelete'),
    negativeText: t('common.cancel'),
    maskClosable: false,
    loading: deleteLoading,
    onPositiveClick() {
      deleteProject(record.id).then(() => {
        window.$message?.success(t('common.deleteSuccess'))
        fetchData()
      })
    },
  })
}
const handleAddProjectCancel = (shouldSearch: boolean) => {
  addProjectVisible.value = false
  if (shouldSearch) {
    fetchData()
  }
}
const {
  send: enableOrDisableProject,
  loading: enableLoading
} = useRequest((id, enable) => OrgProjectApi.enableOrDisableProject(id, enable), {immediate: false})
const handleEnableChange = (isEnable: boolean, record: OrgProjectTableItem) => {
  const title = isEnable ? t('system.project.enableTitle') : t('system.project.endTitle');
  const content = isEnable ? t('system.project.enableContent') : t('system.project.endContent');
  window.$dialog?.info({
    title,
    content,
    positiveText: isEnable ? t('common.confirmStart') : t('common.confirmClose'),
    negativeText: t('common.cancel'),
    loading: enableLoading,
    maskClosable: false,
    onPositiveClick() {
      enableOrDisableProject(record.id, isEnable).then(() => {
        window.$message?.success(isEnable ? t('common.enableSuccess') : t('common.closeSuccess'))
        fetchData()
      })
    },
  })
}
const handleNameChange = async (va: boolean, record: OrgProjectTableItem) => {
  await OrgProjectApi.renameProject({id: record.id, name: va, organizationId: record.organizationId})
  window.$message?.success(t('common.updateSuccess'))
  await fetchData()
}
const dataColumns = computed<DataTableColumns<OrgProjectTableItem>>(() => {
  return [
    {type: 'selection'},
    {
      title: t('system.organization.ID'),
      key: 'num'
    },
    {
      title: t('system.organization.name'),
      key: 'name',
      render(row) {
        return h(ShowOrEditComp, {
          value: row.name, permissions: ['SYSTEM_ORGANIZATION_PROJECT:READ+UPDATE'],
          onUpdateValue: (value) => handleNameChange(value, row)
        }, {})
      }
    },
    {
      title: t('system.organization.member'),
      key: 'memberCount'
    },
    {
      title: t('system.organization.status'),
      key: 'enable',
      render(row) {
        return h(NSwitch, {
          value: row.enable,
          size: 'small',
          disabled: !hasAnyPermission(['SYSTEM_ORGANIZATION_PROJECT:READ+UPDATE']),
          onUpdateValue: (value) => handleEnableChange(value, row)
        }, {})
      }
    },
    {
      title: t('common.desc'),
      key: 'description'
    },
    {
      title: hasOperationPermission.value ? t('system.organization.operation') : "",
      key: 'actions',
      fixed: 'right',
      width: operationWidth.value,
      render(row) {
        if (!row.enable) {
          return withDirectives(
              h(NButton, {text: true}, {default: () => t('common.delete')}),
              [[permission, ['SYSTEM_ORGANIZATION_PROJECT:READ+DELETE']]]
          );
        } else {
          return [
            h(PermissionButton, {
              permissions: ['SYSTEM_ORGANIZATION_PROJECT:READ+UPDATE'],
              label: t('common.edit'),
              type: 'primary', text: true,
              onBtnClick: () => handleEditProject(row)
            }, {}),
            h(PermissionButton, {
              permissions: ['PROJECT_BASE_INFO:READ'],
              label: t('system.project.enterProject'),
              type: 'primary', text: true,
              onBtnClick: () => handleEditProject(row)
            }, {}),
            h(PermissionButton, {
              permissions: ['SYSTEM_ORGANIZATION_PROJECT:READ+DELETE'],
              label: t('common.delete'),
              type: 'error', text: true,
              onBtnClick: () => handleDeleteProject(row)
            }, {})
          ];
        }
      }
    }
  ]
})

onMounted(() => {
  fetchData()
})
</script>

<template>
  <n-card>
    <n-flex justify="space-between" class="mb-4">
      <div>
        <n-button v-permission="['SYSTEM_ORGANIZATION_PROJECT:READ+ADD']" type="primary"
                  @click="handleAddProject">
          {{ t('system.organization.createProject') }}
        </n-button>
      </div>
      <div>
        <n-input v-model:value="keyword" :placeholder="t('system.organization.searchIndexPlaceholder')"
                 class="w-[240px]" clearable/>
      </div>
    </n-flex>
    <n-data-table :columns="dataColumns" :data="data" :row-key="r=>r.id"/>
  </n-card>
  <add-project-modal v-model:show-modal="addProjectVisible" :current-project="currentUpdateProject"
                     @cancel="handleAddProjectCancel"/>
</template>

<style scoped>

</style>