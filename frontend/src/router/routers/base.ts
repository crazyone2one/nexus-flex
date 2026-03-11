import type {RouteRecordRaw} from "vue-router";
import {NO_RESOURCE_ROUTE_NAME} from "/@/router/constants.ts";

export const INDEX_ROUTE: RouteRecordRaw = {
    path: '/index',
    name: 'nexusIndex',
    component: () => import('/@/layout/DefaultLayout.vue'),
    meta: {
        hideInMenu: true,
        roles: ['*'],
        requiresAuth: true,
    },
};
export const NOT_FOUND_ROUTE: RouteRecordRaw = {
    path: '/:pathMatch(.*)*',
    name: 'notFound',
    component: () => import('/@/views/base/NotFound/index.vue')
}
export const NO_RESOURCE: RouteRecordRaw = {
    path: '/no-resource',
    name: NO_RESOURCE_ROUTE_NAME,
    component: () => import('/@/views/base/NoResource/index.vue'),
    meta: {
        hideInMenu: true,
    },
};