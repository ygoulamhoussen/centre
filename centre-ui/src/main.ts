import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPersistedstate from 'pinia-plugin-persistedstate'

import App from './App.vue'
import router from './router'

// DSFR styles
import '@gouvfr/dsfr/dist/core/core.main.min.css'
import '@gouvfr/dsfr/dist/component/component.main.min.css'
import '@gouvfr/dsfr/dist/utility/utility.main.min.css'
import '@gouvfr/dsfr/dist/scheme/scheme.min.css'
import '@gouvfr/dsfr/dist/utility/icons/icons.min.css'
import '@gouvminint/vue-dsfr/styles'

// Styles custom
import './main.css'

const app = createApp(App)

const pinia = createPinia()
pinia.use(piniaPersistedstate)

app.config.globalProperties.$backendUrl = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080'

app
  .use(pinia)
  .use(router)
  .mount('#app')
