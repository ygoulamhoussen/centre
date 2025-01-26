import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router/router'; // Update the import to use router.js
import '@gouvfr/dsfr/dist/dsfr.min.css';

document.addEventListener('DOMContentLoaded', () => {
    import('@gouvfr/dsfr/dist/dsfr.module.min.js').then(() => {
      console.log('DSFR initialized');
    });
});

const app = createApp(App);
const pinia = createPinia();

app.use(pinia);
app.use(router);
app.mount('#app');
