import {createServerTokenAuthentication} from 'alova/client';
import {createAlova} from 'alova';
import VueHook from 'alova/vue';
import adapterFetch from 'alova/fetch';
import {message} from '/@/composables/useDiscreteApi';
import {authApi} from "/@/api/modules/auth.ts";
import {clearToken, getToken, setToken} from "/@/utils/auth.ts";
import router from "/@/router";
import type {LocationQueryRaw} from "vue-router";
import {useAppStore} from "/@/store";
import useLocale from '/@/i18n/use-locale.ts'
import {useI18n} from "/@/composables/useI18n.ts";

const {onAuthRequired, onResponseRefreshToken} = createServerTokenAuthentication({
    refreshTokenOnSuccess: {
        isExpired: async (response, method) => {
            // const res = await response.clone().json();
            const isExpired = method.meta && method.meta.isExpired;
            return !method.url.includes('/auth/refresh') && (response.status === 401) && !isExpired;
        },
        handler: async (_, method) => {
            method.meta = method.meta || {};
            method.meta.isExpired = true;
            try {
                const {accessToken, refreshToken} = await authApi.refreshToken({
                    "refreshToken": getToken().refreshToken
                });
                setToken(accessToken, refreshToken);
            } catch (e) {
                clearToken();
                await router.replace({
                    name: 'login',
                    query: {
                        redirect: router.currentRoute.value.name,
                        ...router.currentRoute.value.query,
                    } as LocationQueryRaw,
                });
            }
        }
    },
    assignToken: method => {
        const token = getToken();
        if (token && (!method.meta?.authRole || method.meta?.authRole !== 'refreshToken')) {
            method.config.headers.Authorization = `Bearer ${token.accessToken}`;
        }
    }
});

export const NFR = createAlova({
    baseURL: `${window.location.origin}/${import.meta.env.VITE_API_BASE_URL}`,
    statesHook: VueHook,
    requestAdapter: adapterFetch(),
    timeout: 300 * 1000,
    cacheFor: {
        GET: 0, // 关闭所有GET缓存
        POST: 60 * 60 * 1000 // 设置所有POST缓存1小时
    },
    beforeRequest: onAuthRequired(method => {
        const appStore = useAppStore();
        const {currentLocale} = useLocale();
        method.config.headers = {
            ...method.config.headers,
            'PROJECT': appStore.appState.currentProjectId,
            'ORGANIZATION': appStore.appState.currentOrgId,
            'Accept-Language': currentLocale.value,
        };
        appStore.showLoading();
    }),
    responded: onResponseRefreshToken({
        onSuccess: async (response, method) => {
            const {t} = useI18n();
            // const json = await response.clone().json();
            // const msg: string = json?.data?.message ?? '';
            if (response.status >= 400) {
                switch (response.status) {
                    case 403:
                        message.error(t('api.errMsg403'));
                        break;
                    case 405:
                        message.error( t('api.errMsg405'));
                        break
                    case 408:
                        message.error( t('api.errMsg408'));
                        break
                    case 500:
                        message.error(t('api.errMsg500'));
                        break
                    default:
                        break;
                }
                throw new Error(response.statusText);
            }
            if (method.meta?.isBlob) {
                return response.blob();
            }
            const json = await response.clone().json();
            if (json.code !== 100200) {
                switch (json.code) {
                    case 100400:
                        message.error(json.message || '参数校验失败');
                        break;
                    case 100403:
                        message.error(json.message || t('api.errMsg403'));
                        break;
                    case 100500:
                        message.error(t('api.errMsg500'));
                        break;
                    default:
                        message.error(json.message || t('api.requestError'));
                        break;
                }
                // 抛出错误或返回reject状态的Promise实例时，此请求将抛出错误
                throw new Error(json.message || t('api.requestError'));
            }
            // 解析的响应数据将传给method实例的transform钩子函数，这些函数将在后续讲解
            return json.data;
        },
        onError: async (error) => {
            console.error('Global Error:', error);
            const appStore = useAppStore();
            appStore.hideLoading();
            throw error;
        },
        onComplete: async _ => {
            // 处理请求完成逻辑
            const appStore = useAppStore();
            appStore.hideLoading();
        }
    })
});