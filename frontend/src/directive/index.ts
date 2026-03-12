import outerClick from './outerClick';
import permission from './permission';
import type {App} from "vue";

export default {
    install(Vue: App) {
        Vue.directive('permission', permission);
        Vue.directive('outer', outerClick);
    },
};
