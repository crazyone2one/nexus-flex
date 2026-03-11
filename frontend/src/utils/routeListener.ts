import mitt, {type Handler} from 'mitt';
import type {RouteLocationNormalized} from 'vue-router';

const emitter = mitt();

const key = Symbol('ROUTE_CHANGE');

let latestRoute: RouteLocationNormalized;


export const setRouteEmitter = (to: RouteLocationNormalized) => {
    emitter.emit(key, to);
    latestRoute = to;
}
export const listenerRouteChange = (handler: (route: RouteLocationNormalized) => void, immediate = true) => {
    emitter.on(key, handler as Handler);
    if (immediate && latestRoute) {
        handler(latestRoute);
    }
}
export const removeRouteListener = () => {
    emitter.off(key);
}