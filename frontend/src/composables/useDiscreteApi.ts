import { createDiscreteApi, type ConfigProviderProps, darkTheme, lightTheme } from 'naive-ui';

const themeRef = useOsTheme();
const configProviderPropsRef = computed<ConfigProviderProps>(() => ({
    theme: themeRef.value === 'light' ? lightTheme : darkTheme,
}));

const { message, notification, dialog, loadingBar,modal } = createDiscreteApi(
    ['message', 'dialog', 'notification', 'loadingBar', 'modal'],
    {
        configProviderProps: configProviderPropsRef,
    },
);

export {message, notification, dialog, loadingBar, modal}