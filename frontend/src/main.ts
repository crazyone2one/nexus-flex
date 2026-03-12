import {createApp} from 'vue'
import './styles/global.css'
import App from './App.vue'
import 'virtual:uno.css'
import 'vfonts/Lato.css'
import 'vfonts/FiraCode.css'
import pinia from './store/index.ts'
import router from './router/index.ts'
import {setupI18n} from "/@/i18n";
import useLocale from '/@/i18n/use-locale.ts'
import {userApi} from "/@/api/modules/user.ts";
import directive from './directive';

const bootstrap = async () => {
    const app = createApp(App)
    app.use(pinia)
    app.use(router)
    await setupI18n(app)
    // 获取默认语言
    const localLocale = localStorage.getItem('NF-locale');
    if (!localLocale) {
        const defaultLocale = await userApi.getDefaultLocale();
        const {changeLocale} = useLocale();
        await changeLocale(defaultLocale);
    }
    app.use(directive);
    app.mount('#app')
}
bootstrap().then(() => {
})
