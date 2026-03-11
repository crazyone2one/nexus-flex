import {NFR} from "/@/api";
import type {AuthenticationResponse} from "/@/api/types/auth.ts";

export const authApi = {
    login: (data: { username: string, password: string }) => {
        const method = NFR.Post<AuthenticationResponse>('/auth/login', data)
        method.meta = {authRole: null,};
        return method;
    },
    refreshToken: (data: { refreshToken: string }) => {
        const method = NFR.Post<AuthenticationResponse>('/auth/refresh', data,);
        method.meta = {authRole: 'refreshToken'};
        return method
    },
    logout: () => {
        const method = NFR.Post('/auth/logout')
        method.meta = {authRole: 'logout'};
        return method;
    },
}