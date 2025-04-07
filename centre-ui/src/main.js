import { createApp } from 'vue';
import App from './App.vue';
import router from './router/router';
import { createPinia } from 'pinia';

// Import DSFR styles
import '@gouvfr/dsfr/dist/dsfr.min.css';
import '@gouvfr/dsfr/dist/utility/utility.min.css';

// Import DSFR scripts
import '@gouvfr/dsfr/dist/dsfr.module.min.js';

const app = createApp(App);
const pinia = createPinia();

// Définir l'URL du backend en fonction de l'environnement
const backendUrl = process.env.VUE_APP_BACKEND_URL || "http://localhost:8080";

// Ajouter l'URL du backend comme propriété globale
app.config.globalProperties.$backendUrl = backendUrl;

app.use(pinia);
app.use(router);
app.mount('#app');
