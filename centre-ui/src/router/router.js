import { createRouter, createWebHistory } from 'vue-router';
import HomePage from '../views/HomePage.vue';
import ContactPage from '../views/ContactPage.vue';
import FormationsPage from '../views/FormationsPage.vue';
import AdminPage from '../views/AdminPage.vue';
import LoginPage from '../views/LoginPage.vue';
import { useStore } from '../stores/store'; // Import the store

const routes = [
  { path: '/', component: HomePage },
  { path: '/contact', component: ContactPage },
  { path: '/formations', component: FormationsPage },
  {
    path: '/admin',
    component: AdminPage,
    meta: { requiresAuth: true, requiresAdmin: true }
  },
  { path: '/login', component: LoginPage }
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Navigation guard to check for authentication and admin access
router.beforeEach((to, from, next) => {
  const userProfile = JSON.parse(sessionStorage.getItem('userProfile'));
  const isAuthenticated = userProfile !== null;
  const isAdmin = isAuthenticated && userProfile.role === 'ADMINISTRATEUR';

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!isAuthenticated) {
      next({ path: '/login' });
    } else if (to.matched.some(record => record.meta.requiresAdmin) && !isAdmin) {
      next({ path: '/' });
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
