<script setup lang="ts">

</script>

<template>
  <router-view v-slot="{ Component, route }">
    <!--    <transition name="fade" mode="out-in" appear>-->
    <!--      &lt;!&ndash; transition内必须有且只有一个根元素，不然会导致二级路由的组件无法渲染 &ndash;&gt;-->
    <!--      <div-->
    <!--          v-show="true"-->
    <!--          class="page-content h-full"-->
    <!--      >-->
    <!--        <keep-alive :include="cacheStore.cacheViews">-->
    <!--          <component :is="Component" :key="route.name"/>-->
    <!--        </keep-alive>-->
    <!--      </div>-->
    <!--    </transition>-->
    <transition name="fade" mode="out-in" appear>
      <!-- transition内必须有且只有一个根元素，不然会导致二级路由的组件无法渲染 -->
      <div
          v-show="true"
          class="page-content h-full"
      >
        <component :is="Component" :key="route.name"/>
      </div>
    </transition>
  </router-view>
</template>

<style scoped>
.page-content {
  min-height: 500px;
  @apply overflow-y-auto;
}
</style>