<script setup lang="ts">
import {useI18n} from "/@/composables/useI18n.ts";
import type {OrgProjectTableItem} from "/@/api/types/project.ts";
import {type DataTableColumns} from 'naive-ui'
import {hasAnyPermission} from "/@/utils/permission.ts";
import {usePagination} from "alova/client";
import {OrgProjectApi} from "/@/api/modules/OrgProject.ts";

const {t} = useI18n()
const keyword = ref('')
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
const dataColumns = computed<DataTableColumns<OrgProjectTableItem>>(() => {
  return [
    {type: 'selection'},
    {
      title: t('system.organization.ID'),
      key: 'num'
    },
    {
      title: t('system.organization.name'),
      key: 'name'
    },
    {
      title: t('system.organization.status'),
      key: 'enable'
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
        <n-button v-permission="['SYSTEM_ORGANIZATION_PROJECT:READ+ADD']" type="primary">
          {{ t('system.organization.createProject') }}
        </n-button>
      </div>
      <div>
        <n-input v-model:value="keyword" :placeholder="t('system.organization.searchIndexPlaceholder')"
                 class="w-[240px]" clearable/>
      </div>
    </n-flex>
    <n-data-table :columns="dataColumns" :data="data"/>
  </n-card>
</template>

<style scoped>

</style>