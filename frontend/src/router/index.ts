import {createRouter, createWebHashHistory} from "vue-router";
import {NOT_FOUND_ROUTE} from "/@/router/routers/base.ts";
import createRouteGuard from "/@/router/guard";
import appRoutes from "/@/router/routers";

const router = createRouter({
    history: createWebHashHistory(),
    routes: [

        {
            path: '/',
            name: 'nexusIndex',
            component: () => import('/@/layout/DefaultLayout.vue'),
            meta: {
                hideInMenu: true,
                roles: ['*'],
                requiresAuth: true,
            },
            children: [
                // {path: '/dashboard', name: 'dashboard', component: () => import('/@/views/dashboard/index.vue')},
                ...appRoutes,
            ]
        },
        {
            path: '/login', name: 'login', component: () => import('/@/views/login/index.vue'),
            meta: {
                requiresAuth: false,
            },
        },
        NOT_FOUND_ROUTE
    ],
    scrollBehavior() {
        return {top: 0};
    },
})
createRouteGuard(router);
// console.log(router.getRoutes())
export default router