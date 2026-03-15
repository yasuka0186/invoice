import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import './assets/styles/main.css'

/**
 * Vueアプリケーションの初期化処理
 * - Pinia を登録
 * - Router を登録
 * - App.vue をマウント
 */
const app = createApp(App)

app.use(createPinia())
app.use(router)

app.mount('#app')
