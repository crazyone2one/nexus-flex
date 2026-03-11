const ACCESS_TOKEN = 'accessToken';
const REFRESH_TOKEN = 'refreshToken';

const getToken = () => {
    return {
        [ACCESS_TOKEN]: localStorage.getItem(ACCESS_TOKEN),
        [REFRESH_TOKEN]: localStorage.getItem(REFRESH_TOKEN) || ''
    };
};

const setToken = (accessToken: string, refreshToken: string) => {
    localStorage.setItem(ACCESS_TOKEN, accessToken);
    localStorage.setItem(REFRESH_TOKEN, refreshToken);
};

const clearToken = () => {
    localStorage.removeItem(ACCESS_TOKEN);
    localStorage.removeItem(REFRESH_TOKEN);
};

const hasToken = () => {
    return !!localStorage.getItem(ACCESS_TOKEN) && !!localStorage.getItem(REFRESH_TOKEN);
};
export {clearToken, getToken, hasToken, setToken};