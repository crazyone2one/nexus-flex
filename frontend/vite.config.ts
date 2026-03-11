import {defineConfig, loadEnv} from 'vite'
import UnoCSS from "unocss/vite";
import vue from "@vitejs/plugin-vue";
import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";
import {NaiveUiResolver} from "unplugin-vue-components/resolvers";
import path from "path";

// https://vite.dev/config/
/** @type {import('vite').UserConfig} */
export default defineConfig(({mode}) => {
    const env = loadEnv(mode, process.cwd(), '')
    const isProduction = mode === 'production'
    return {
        define: {
            // 提供从 env var 派生的显式应用程序级常量。
            __APP_ENV__: JSON.stringify(env.APP_ENV),
        },
        server: !isProduction ? {
            proxy: {
                '/front': {
                    target: 'http://127.0.0.1:8080/',
                    changeOrigin: true,
                    rewrite: (path: string) => path.replace(new RegExp('^/front'), ''),
                },
            },
            watch: {
                usePolling: true
            }
        } : {},
        plugins: [UnoCSS(), vue(),
            // 自动导入 Vue API (ref, computed 等)
            AutoImport({
                imports: ['vue',
                    'vue-router',
                    'pinia',
                    {
                        'naive-ui': [
                            'useDialog',
                            'useMessage',
                            'useNotification',
                            'useLoadingBar',
                            'useOsTheme',
                            'useThemeVars',
                        ]
                    }],
                dts: 'src/auto-imports.d.ts'
            }),
            // 自动导入 Naive UI 组件
            Components({
                resolvers: [NaiveUiResolver()],
                dts: 'src/components.d.ts'
            })
        ],
        resolve: {
            alias: [{
                find: /\/@\//,
                replacement: path.resolve(__dirname, '.', 'src') + '/',
            }],
        },

    }
})
