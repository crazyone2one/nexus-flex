import {NFR} from "/@/api";
import type {LocaleType} from "/@/i18n/helper.ts";

export const userApi = {
    getDefaultLocale: () => {
        const method = NFR.Get<LocaleType>('/user/local/config/default-locale', {cacheFor: 0});
        method.meta = {
            authRole: null
        };
        return method;
    },
}