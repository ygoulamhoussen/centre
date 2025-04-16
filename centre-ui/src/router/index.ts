import { createRouter, createWebHistory } from 'vue-router'

import Home from '../views/AppHome.vue'
import AboutUs from '../views/AboutUs.vue'
import Fonctionnalites from '../views/Fonctionnalites.vue'
import Login from '../views/Login.vue'
import Gestion from '../views/Gestion.vue' // Ajout de l'import
import ProprieteListView from '../views/ProprieteListView.vue' // Ajout de l'import
import ProprieteEtape1 from '../views/ProprieteEtape1.vue' // Ajout de l'import
import ProprieteEtape2 from '../views/ProprieteEtape2.vue' // Ajout de l'import
import ProprieteEtape3 from '../views/ProprieteEtape3.vue' // Ajout de l'import
import ProprieteEtape4 from '../views/ProprieteEtape4.vue'

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
    path: '/ProprieteListView',
    name: 'ProprieteListView',
    component: ProprieteListView,
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
  {
    path: '/propriete/etape-1',
    name: 'ProprieteEtape1',
    component: ProprieteEtape1,
  },
  {
    path: '/propriete/etape-2',
    name: 'ProprieteEtape2',
    component: ProprieteEtape2,
  },
  {
    path: '/propriete/etape-3',
    name: 'ProprieteEtape3',
    component: ProprieteEtape3,
  },
  {
    path: '/propriete/etape-4',
    name: 'ProprieteEtape4',
    component: ProprieteEtape4,
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
