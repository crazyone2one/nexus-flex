import {defineConfig, presetIcons, presetWind3} from 'unocss'
import transformerDirectives from '@unocss/transformer-directives'

export default defineConfig({
    // ...UnoCSS options
    presets: [presetIcons({
        collections: {
            carbon: () => import('@iconify-json/carbon/icons.json').then(i => i.default)
        }
    }), presetWind3()],
    transformers: [transformerDirectives()],
    safelist: ['i-carbon:chart-combo', 'i-carbon:settings']
})