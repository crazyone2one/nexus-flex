import type {RouteRecordRaw} from "vue-router";

const Dashboard: RouteRecordRaw = {
    path: '/dashboard',
    name: 'dashboard',
    component: () => import('/@/views/dashboard/index.vue'),
    meta: {
        order: 0, locale: 'menu.workbench',
        collapsedLocale: 'menu.workbenchHomeSort',
        icon: 'carbon:chart-combo',
    }
}
export default Dashboard