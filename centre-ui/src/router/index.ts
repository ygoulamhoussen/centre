import { createRouter, createWebHistory } from 'vue-router'

import Home from '../views/AppHome.vue'
import AboutUs from '../views/AboutUs.vue'
import Fonctionnalites from '../views/Fonctionnalites.vue'
import Login from '../views/Login.vue'
import Gestion from '../views/Gestion.vue' // Ajout de l'import
import ProprieteView from '../views/ProprieteView.vue' // Ajout de l'import

const MAIN_TITLE = 'Gabarit de démarrage VueDsfr'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
  },
  {
    path: '/a-propos',
    name: 'About',
    component: AboutUs,
  },
  {
    path: '/fonctionnalites',
    name: 'Fonctionnalités',
    component: Fonctionnalites,
  },
  {
    path: '/proprieteView',
    name: 'ProprieteView',
    component: ProprieteView,
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/gestion',
    name: 'gestion',
    component: Gestion, // Nouvelle route pour "Mon Compte"
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env?.BASE_URL || ''),
  routes,
})

router.beforeEach((to) => { // Cf. https://github.com/vueuse/head pour des transformations avancées de Head
  const specificTitle = to.meta.title ? `${to.meta.title} - ` : ''
  document.title = `${specificTitle}${MAIN_TITLE}`
})

export default router
