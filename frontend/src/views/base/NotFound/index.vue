<script setup lang="ts">
import {getFirstRouteNameByPermission} from "/@/utils/permission.ts";

const router = useRouter();
const back = () => {
  router.push({name: getFirstRouteNameByPermission(router.getRoutes())});
};
</script>

<template>
  <div class="absolute left-[50%] top-[50%] ml-[-95px] mt-[-121px] text-center">
    <n-result status="404" title="404 资源不存在" description="生活总归带点荒谬">
      <template #footer>
        <n-button type="primary" @click="back">back</n-button>
      </template>
    </n-result>
  </div>
</template>

<style scoped>

</style>