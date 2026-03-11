export type LocaleType = 'zh-CN' | 'en-US';

export const loadLocalePool: LocaleType[] = [];

export function setLoadLocalePool(cb: (lp: LocaleType[]) => void) {
    cb(loadLocalePool);
}
